/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.binance.model

import kotlinx.serialization.Serializable

@Serializable
data class ListenKey(
    val listenKey: String
)
