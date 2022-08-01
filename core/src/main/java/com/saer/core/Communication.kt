package com.saer.core

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface Communication<T> {

    val value: T
    fun map(data: T)
    fun observe(
        viewLifecycleOwner: LifecycleOwner,
        collector: (value: T) -> Unit
    )

    class StateFlow<T : Any>(initValue: T) : Communication<T> {

        private val stateFlow = MutableStateFlow(initValue)

        override fun map(data: T) {
            stateFlow.tryEmit(data)
        }

        override fun observe(viewLifecycleOwner: LifecycleOwner, collector: (value: T) -> Unit) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    stateFlow.collect(collector)
                }
            }
        }

        override val value: T
            get() = stateFlow.value
    }

    class LiveData<T : Any>(private val initValue: T) : Communication<T> {
        private val liveData = MutableLiveData<T>(initValue)

        override val value: T
            get() = liveData.value ?: initValue

        override fun map(data: T) {
            liveData.value = data
        }

        override fun observe(viewLifecycleOwner: LifecycleOwner, collector: (value: T) -> Unit) {
            liveData.observe(viewLifecycleOwner, Observer(collector))
        }

    }
}