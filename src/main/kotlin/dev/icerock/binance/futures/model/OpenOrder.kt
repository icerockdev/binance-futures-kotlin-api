/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(BigDecimalSerializer::class)

package dev.icerock.binance.futures.model

import dev.icerock.binance.futures.serializer.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.math.BigDecimal

@Serializable
data class OpenOrder(
    @SerialName("avgPrice")
    val averagePrice: BigDecimal,
    val clientOrderId: String,
    val cumQuote: BigDecimal,
    val executedQty: BigDecimal,
    val orderId: Long,
    @SerialName("origQty")
    val originalQuantity: BigDecimal,
    @SerialName("origType")
    val originalType: OrderType,
    val price: BigDecimal,
    val reduceOnly: Boolean,
    val side: OrderSide,
    val positionSide: PositionSide,
    val status: OrderStatus,
    // please ignore when order type is TRAILING_STOP_MARKET
    val stopPrice: BigDecimal,
    // if Close-All
    val closePosition: Boolean,
    val symbol: String,
    val time: Long,
    val timeInForce: String,
    val type: OrderType,
    // activation price, only return with TRAILING_STOP_MARKET order
    val activatePrice: BigDecimal? = null,
    // callback rate, only return with TRAILING_STOP_MARKET order
    val priceRate: BigDecimal? = null,
    val updateTime: Long,
    val workingType: WorkingType,
    // if conditional order trigger is protected
    val priceProtect: Boolean
)
