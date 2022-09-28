package com.saer.core.mappers

interface Mapper<S, R> {
    fun map(data: S): R
}