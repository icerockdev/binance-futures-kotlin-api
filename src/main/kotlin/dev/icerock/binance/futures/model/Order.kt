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
data class Order(
    @SerialName("s")
    val symbol: String,
    @SerialName("S")
    val side: OrderSide,
    @SerialName("o")
    val orderType: OrderType,
    @SerialName("x")
    val executionType: ExecutionType,
    @SerialName("X")
    val orderStatus: OrderStatus,
    @SerialName("i")
    val orderId: Long,
    @SerialName("q")
    val originalQuantity: BigDecimal,
    @SerialName("p")
    val originalPrice: BigDecimal,
    @SerialName("ap")
    val averagePrice: BigDecimal,
    @SerialName("sp")
    val stopPrice: BigDecimal,
    @SerialName("l")
    val lastFilledQuantity: BigDecimal,
    @SerialName("z")
    val filledAccumulatedQuantity: BigDecimal,
    @SerialName("L")
    val lastFilledPrice: BigDecimal,
    @SerialName("rp")
    val realizedProfit: BigDecimal,
    @SerialName("ps")
    val positionSide: PositionSide,
    @SerialName("m")
    val tradeTheMakerSide: Boolean,
    @SerialName("R")
    val reduceOnly: Boolean
) {
    enum class ExecutionType {
        NEW,
        CANCELED,
        CALCULATED,
        EXPIRED,
        TRADE
    }
}
