/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(BigDecimalSerializer::class)

package dev.icerock.binance.model

import dev.icerock.binance.serializer.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

sealed class UserUpdateEvent {
    abstract val eventType: Type

    enum class Type {
        ORDER_TRADE_UPDATE,
        ACCOUNT_UPDATE,
        ACCOUNT_CONFIG_UPDATE
    }

    @Serializable
    data class OrderUpdate(
        @SerialName("e")
        override val eventType: Type,
        @SerialName("E")
        val eventTime: Long,
        @SerialName("T")
        val transactionTime: Long,
        @SerialName("o")
        val order: Order
    ) : UserUpdateEvent()

    @Serializable
    data class AccountUpdate(
        @SerialName("e")
        override val eventType: Type,
        @SerialName("E")
        val eventTime: Long,
        @SerialName("T")
        val transactionTime: Long,
        @SerialName("a")
        val account: WebSocketAccount
    ) : UserUpdateEvent()
}
