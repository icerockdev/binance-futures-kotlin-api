/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.binance.futures.model

enum class TimeInForce {
    /** Good Till Cancel */
    GTC,

    /** Immediate or Cancel */
    IOC,

    /** Fill or Kill */
    FOK,

    /** Good Till Crossing (Post Only) */
    GTX
}