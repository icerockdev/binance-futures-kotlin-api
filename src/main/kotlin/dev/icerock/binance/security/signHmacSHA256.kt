/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.binance.security

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun signHmacSHA256(message: String, secret: String): String? {
    return try {
        val hmac = Mac.getInstance("HmacSHA256")
        val secretKeySpec = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
        hmac.init(secretKeySpec)

        val signature = hmac.doFinal(message.toByteArray())

        signature.toHexString()
    } catch (e: Exception) {
        throw RuntimeException("Unable to sign message.", e)
    }
}

@ExperimentalUnsignedTypes // just to make it clear that the experimental unsigned types are used
fun ByteArray.toHexString() = asUByteArray().joinToString("") {
    it.toString(16).padStart(2, '0')
}
