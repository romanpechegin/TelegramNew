package com.saer.telegramnew.model

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

class ErrorResult<T>: FinalResult<T>() {
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
        return if (other !is SuccessResult<*>) false
        else data == other.data
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