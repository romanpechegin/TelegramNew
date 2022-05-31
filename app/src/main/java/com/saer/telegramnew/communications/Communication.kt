package com.saer.telegramnew.communications

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communication<T> {

    val value: T?
    fun map(data: T)
    fun observe(viewLifecycleOwner: LifecycleOwner, observer: Observer<T>)

    abstract class Base<T : Any> : Communication<T> {

        private val liveData = MutableLiveData<T>()

        override fun map(data: T) {
            liveData.value = data
        }

        override fun observe(viewLifecycleOwner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(viewLifecycleOwner, observer)
        }

        override val value: T? = liveData.value
    }
}