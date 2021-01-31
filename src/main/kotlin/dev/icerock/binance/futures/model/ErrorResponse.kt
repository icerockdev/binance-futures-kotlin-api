/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.binance.futures.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: Int,
    val msg: String
)
