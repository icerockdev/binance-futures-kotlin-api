/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.binance.futures.model

enum class AccountChangeReason {
    DEPOSIT,
    WITHDRAW,
    ORDER,
    FUNDING_FEE,
    WITHDRAW_REJECT,
    ADJUSTMENT,
    INSURANCE_CLEAR,
    ADMIN_DEPOSIT,
    ADMIN_WITHDRAW,
    MARGIN_TRANSFER,
    MARGIN_TYPE_CHANGE,
    ASSET_TRANSFER,
    OPTIONS_PREMIUM_FEE,
    OPTIONS_SETTLE_PROFIT
}
