/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.binance.futures

import dev.icerock.binance.futures.model.UserUpdateEvent
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.wss
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.seconds

open class FuturesWebSocketApi(
    private val socketScope: CoroutineScope,
    private val wsEndpoint: String,
    private val json: Json,
    private val httpClient: HttpClient,
    private val restApi: FuturesRestApi
) {
    protected open fun wsCall(params: String): String {
        return "$wsEndpoint/ws/$params"
    }

    protected val userDataStream: Flow<String> by lazy(::createUserDataStream)

    fun listenOrderTradeUpdateStream(): Flow<UserUpdateEvent.OrderUpdate> {
        return userDataStream
            .mapNotNull(::mapStringToOrderTradeUpdateEvent)
    }

    fun listenAccountUpdateStream(): Flow<UserUpdateEvent.AccountUpdate> {
        return userDataStream
            .mapNotNull(::mapStringToAccountUpdateEvent)
    }

    private fun mapStringToOrderTradeUpdateEvent(string: String): UserUpdateEvent.OrderUpdate? {
        if (string.contains(UserUpdateEvent.Type.ORDER_TRADE_UPDATE.name).not()) return null

        return try {
            json.decodeFromString(UserUpdateEvent.OrderUpdate.serializer(), string)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

    private fun mapStringToAccountUpdateEvent(string: String): UserUpdateEvent.AccountUpdate? {
        if (string.contains(UserUpdateEvent.Type.ACCOUNT_UPDATE.name).not()) return null

        return try {
            json.decodeFromString(UserUpdateEvent.AccountUpdate.serializer(), string)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
    private suspend fun startSocket(
        listenKey: String,
        producerScope: ProducerScope<String>,
        delayDuration: Duration? = null
    ) {
        try {
            httpClient.wss(wsCall(listenKey)) {
                while (isActive) {
                    val frame = incoming.receive()

                    if (frame is Frame.Text) {
                        producerScope.sendBlocking(frame.readText())
                    }
                }
            }
        } catch (t: Throwable) {
            if (t is CancellationException) return

            t.printStackTrace()

            delayDuration?.let { delay(it) }

            val newDelay = delayDuration?.times(2) ?: 1.seconds

            startSocket(listenKey, producerScope, minOf(newDelay, 16.seconds))
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
    private fun createUserDataStream(): Flow<String> {
        return callbackFlow<String> {
            val listenKey = restApi.startUserDataStream()

            val wsTask = async {
                startSocket(listenKey, this@callbackFlow)
            }
            val keepAliveTask = async {
                while (isActive) {
                    delay(KEEP_ALIVE_PERIOD_MINUTES.minutes)

                    restApi.keepAliveUserDataStream(listenKey)
                }
            }

            awaitClose {
                keepAliveTask.cancel()
                wsTask.cancel()
            }
        }.shareIn(
            scope = socketScope,
            started = SharingStarted.Eagerly
        )
    }

    private companion object {
        const val KEEP_ALIVE_PERIOD_MINUTES = 10
    }
}
