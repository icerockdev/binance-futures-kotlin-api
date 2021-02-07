/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.binance.futures

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.client.features.websocket.WebSockets
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import java.time.Duration

open class FuturesApi(
    private val apiKey: String,
    private val secretKey: String,
    private val socketScope: CoroutineScope,
    private val restEndpoint: String = "https://fapi.binance.com",
    private val wsEndpoint: String = "wss://fstream.binance.com",
    private val configureLogger: Logging.Config.() -> Unit = {
        level = LogLevel.INFO
        logger = Logger.SIMPLE
    }
) {
    protected open val json: Json = Json {
        ignoreUnknownKeys = true
    }

    protected open val httpClient: HttpClient = HttpClient(OkHttp) {
        engine {
            config {
                pingInterval(Duration.ofSeconds(5))
            }
        }

        install(WebSockets)
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
        install(Logging, configureLogger)
    }

    val restApi: FuturesRestApi by lazy {
        FuturesRestApi(
            apiKey = apiKey,
            secretKey = secretKey,
            restEndpoint = restEndpoint,
            httpClient = httpClient
        )
    }
    val wsApi: FuturesWebSocketApi by lazy {
        FuturesWebSocketApi(
            wsEndpoint = wsEndpoint,
            json = json,
            httpClient = httpClient,
            restApi = restApi,
            socketScope = socketScope
        )
    }
}
