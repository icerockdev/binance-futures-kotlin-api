/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.binance.futures

import dev.icerock.binance.futures.model.AccountInfo
import dev.icerock.binance.futures.model.ListenKey
import dev.icerock.binance.futures.model.NewOrder
import dev.icerock.binance.futures.model.NewOrderResponseType
import dev.icerock.binance.futures.model.OpenOrder
import dev.icerock.binance.futures.model.OrderSide
import dev.icerock.binance.futures.model.OrderType
import dev.icerock.binance.futures.model.Position
import dev.icerock.binance.futures.model.TimeInForce
import dev.icerock.binance.futures.model.WorkingType
import dev.icerock.binance.futures.security.signHmacSHA256
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import java.math.BigDecimal

open class FuturesRestApi(
    private val apiKey: String,
    private val secretKey: String,
    private val restEndpoint: String,
    private val httpClient: HttpClient
) {
    protected open fun restCall(method: String, version: String = "v1"): String {
        return "$restEndpoint/fapi/$version/$method"
    }

    protected open fun HttpRequestBuilder.passApiKey() {
        header("X-MBX-APIKEY", apiKey)
    }

    protected open fun HttpRequestBuilder.passQuerySignature() {
        val query = url.parameters.entries()
            .flatMap { (key, values) ->
                values.map { "$key=$it" }
            }
            .joinToString(separator = "&")
        val signature = signHmacSHA256(message = query, secret = secretKey)
        parameter("signature", signature)
    }

    suspend fun getAccountInfo(): AccountInfo {
        return httpClient.get(restCall("account", version = "v2")) {
            parameter("timestamp", System.currentTimeMillis())
            passApiKey()
            passQuerySignature()
        }
    }

    suspend fun getPositionInfo(symbol: String? = null): List<Position> {
        return httpClient.get(restCall("positionRisk", version = "v2")) {
            if (symbol != null)  parameter("symbol", symbol)
            parameter("timestamp", System.currentTimeMillis())
            passApiKey()
            passQuerySignature()
        }
    }

    suspend fun getAllOpenOrders(symbol: String? = null): List<OpenOrder> {
        return httpClient.get(restCall("openOrders", version = "v1")) {
            if (symbol != null) parameter("symbol", symbol)
            parameter("timestamp", System.currentTimeMillis())
            passApiKey()
            passQuerySignature()
        }
    }

    private suspend fun createNewOrder(
        symbol: String,
        side: OrderSide,
        type: OrderType,
        timeInForce: TimeInForce? = null,
        quantity: BigDecimal? = null,
        price: BigDecimal? = null,
        stopPrice: BigDecimal? = null,
        workingType: WorkingType? = null,
        closePosition: Boolean? = null,
        newOrderRespType: NewOrderResponseType? = null
    ): NewOrder {
        return httpClient.post(restCall("order", version = "v1")) {
            parameter("symbol", symbol)
            parameter("side", side.name)
            parameter("type", type.name)
            if (timeInForce != null) parameter("timeInForce", timeInForce.name)
            if (quantity != null) parameter("quantity", quantity)
            if (price != null) parameter("price", price)
            if (closePosition != null) parameter("closePosition", closePosition)
            if (stopPrice != null) parameter("stopPrice", stopPrice)
            if (workingType != null) parameter("workingType", workingType.name)
            if (newOrderRespType != null) parameter("newOrderRespType", newOrderRespType)
            parameter("timestamp", System.currentTimeMillis())
            passApiKey()
            passQuerySignature()
        }
    }

    suspend fun createNewLimitOrder(
        symbol: String,
        side: OrderSide,
        timeInForce: TimeInForce,
        quantity: BigDecimal,
        price: BigDecimal,
        newOrderRespType: NewOrderResponseType? = null
    ) = createNewOrder(
        symbol = symbol,
        side = side,
        type = OrderType.LIMIT,
        timeInForce = timeInForce,
        quantity = quantity,
        price = price,
        newOrderRespType = newOrderRespType
    )

    suspend fun createNewMarketOrder(
        symbol: String,
        side: OrderSide,
        quantity: BigDecimal,
        newOrderRespType: NewOrderResponseType? = null
    ) = createNewOrder(
        symbol = symbol,
        side = side,
        type = OrderType.MARKET,
        quantity = quantity,
        newOrderRespType = newOrderRespType
    )

    suspend fun createNewTakeProfitMarketOrder(
        symbol: String,
        side: OrderSide,
        workingType: WorkingType,
        price: BigDecimal,
        newOrderRespType: NewOrderResponseType? = null
    ) = createNewOrder(
        symbol = symbol,
        side = side,
        type = OrderType.TAKE_PROFIT_MARKET,
        closePosition = true,
        workingType = workingType,
        stopPrice = price,
        newOrderRespType = newOrderRespType
    )

    suspend fun createNewStopLossMarketOrder(
        symbol: String,
        side: OrderSide,
        workingType: WorkingType,
        price: BigDecimal,
        newOrderRespType: NewOrderResponseType? = null
    ) = createNewOrder(
        symbol = symbol,
        side = side,
        type = OrderType.STOP_MARKET,
        closePosition = true,
        workingType = workingType,
        stopPrice = price,
        newOrderRespType = newOrderRespType
    )

    suspend fun closeAllOpenOrders(symbol: String) {
        return httpClient.delete(restCall("allOpenOrders", version = "v1")) {
            parameter("symbol", symbol)
            parameter("timestamp", System.currentTimeMillis())
            passApiKey()
            passQuerySignature()
        }
    }

    suspend fun startUserDataStream(): String {
        return httpClient.post<ListenKey>(restCall("listenKey")) {
            passApiKey()
        }.listenKey
    }

    suspend fun keepAliveUserDataStream(listenKey: String) {
        return httpClient.put(restCall("listenKey")) {
            passApiKey()
            parameter("listenKey", listenKey)
        }
    }

    suspend fun closeUserDataStream(listenKey: String) {
        return httpClient.delete(restCall("listenKey")) {
            passApiKey()
            parameter("listenKey", listenKey)
        }
    }
}
