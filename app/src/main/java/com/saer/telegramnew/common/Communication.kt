package com.saer.telegramnew.common

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

interface Communication<T> {

    val value: T
    fun map(data: T)
    fun observe(
        lifecycleCoroutineScope: LifecycleCoroutineScope,
        collector: FlowCollector<T>
    )

    class Base<T : Any>(initValue: T) : Communication<T> {

        private val stateFlow = MutableStateFlow(initValue)

        override fun map(data: T) {
            stateFlow.tryEmit(data)
        }

        override fun observe(
            lifecycleCoroutineScope: LifecycleCoroutineScope,
            collector: FlowCollector<T>
        ) {
            lifecycleCoroutineScope.launchWhenResumed {
                stateFlow.collect(collector = collector)
            }
        }

        override val value: T
            get() = stateFlow.value
    }
}