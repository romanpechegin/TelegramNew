package com.saer.core

interface Mapper<S, R> {
    fun map(data: S): R
}