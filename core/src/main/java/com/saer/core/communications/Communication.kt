package com.saer.core.communications

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface Communication<T> {

    val value: T
    suspend fun map(data: T)
    fun observe(
        viewLifecycleOwner: LifecycleOwner,
        collector: (value: T) -> Unit
    )

    open class StateFlow<T : Any>(initValue: T) : Communication<T> {

        private val stateFlow = MutableStateFlow(initValue)

        override suspend fun map(data: T) {
            stateFlow.emit(data)
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
        private val liveData = MutableLiveData(initValue)

        override val value: T
            get() = liveData.value ?: initValue

        override suspend fun map(data: T) {
            liveData.value = data
        }

        override fun observe(viewLifecycleOwner: LifecycleOwner, collector: (value: T) -> Unit) {
            liveData.observe(viewLifecycleOwner, Observer(collector))
        }

    }
}