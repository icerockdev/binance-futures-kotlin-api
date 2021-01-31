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
data class WebSocketAccount(
    @SerialName("m")
    val eventReason: AccountChangeReason,
    @SerialName("B")
    val balances: List<AssetBalance>,
    @SerialName("P")
    val positions: List<Position>
) {
    @Serializable
    data class AssetBalance(
        @SerialName("a")
        val asset: String,
        @SerialName("wb")
        val walletBalance: BigDecimal,
        @SerialName("cw")
        val crossWalletBalance: BigDecimal
    )

    @Serializable
    data class Position(
        @SerialName("s")
        val symbol: String,
        @SerialName("pa")
        val positionAmount: BigDecimal,
        @SerialName("ep")
        val entryPrice: BigDecimal,
        @SerialName("cr")
        val accumulatedRealized: BigDecimal,
        @SerialName("up")
        val unrealizedProfit: BigDecimal,
        @SerialName("mt")
        val marginType: String,
        @SerialName("iw")
        val isolatedWallet: BigDecimal,
        @SerialName("ps")
        val positionSide: PositionSide
    )
}
