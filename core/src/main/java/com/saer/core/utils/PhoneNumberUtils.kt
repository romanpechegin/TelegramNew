package com.saer.core.utils

fun phoneNumberOrNull(phoneNumber: String, phoneLength: Int): String? {
    val onlyDigitPhoneNumber = phoneNumber.filter { it.isDigit() }
    return if (onlyDigitPhoneNumber.length == phoneLength) onlyDigitPhoneNumber
    else null
}