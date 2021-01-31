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
data class AccountInfo(
    val canTrade: Boolean,
    val canDeposit: Boolean,
    val canWithdraw: Boolean,
    @SerialName("availableBalance")
    val availableBalanceUSDT: BigDecimal,
    @SerialName("maxWithdrawAmount")
    val maxWithdrawAmountUSDT: BigDecimal,
    val positions: List<Position>
)
