package com.saer.telegramnew.model

import androidx.annotation.StringRes

sealed class Result<T>

sealed class FinalResult<T> : Result<T>()

class PendingResult<T> : Result<T>() {
    override fun equals(other: Any?): Boolean {
        return other is PendingResult<*>
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class UserActionSuccessResult<T>(
    @StringRes
    val message: Int
) : Result<T>() {
    override fun equals(other: Any?): Boolean {
        if (other !is UserActionSuccessResult<*>) return false
        return message == other.message
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class UserActionErrorResult<T>(
    @StringRes
    val message: Int
) : Result<T>() {
    override fun equals(other: Any?): Boolean {
        if (other !is UserActionErrorResult<*>) return false
        return message == other.message
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class ErrorResult<T>(
    val exception: Exception
) : FinalResult<T>() {
    override fun equals(other: Any?): Boolean {
        return other is ErrorResult<*>
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class SuccessResult<T>(
    val data: T? = null
) : FinalResult<T>() {
    override fun equals(other: Any?): Boolean {
        return other is SuccessResult<*>
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

fun <T> Result<T>?.takeSuccess(): T? {
    return if (this is SuccessResult) {
        return data
    } else {
        null
    }
}