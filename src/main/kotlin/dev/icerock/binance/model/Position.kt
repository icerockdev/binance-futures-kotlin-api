/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(BigDecimalSerializer::class)

package dev.icerock.binance.model

import dev.icerock.binance.serializer.BigDecimalSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.math.BigDecimal

@Serializable
data class Position(
    val symbol: String,
    val initialMargin: BigDecimal,
    val maintMargin: BigDecimal,
    val unrealizedProfit: BigDecimal,
    val positionInitialMargin: BigDecimal,
    val openOrderInitialMargin: BigDecimal,
    val leverage: BigDecimal,
    val isolated: Boolean,
    val entryPrice: BigDecimal,
    val maxNotional: BigDecimal,
    val positionSide: PositionSide,
    val positionAmt: BigDecimal
)
