package com.saer.telegramnew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.login.repositories.AuthRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivityViewModel @AssistedInject constructor(
    authRepository: AuthRepository,
) : ViewModel() {

    var isReady = false
    var isAuth = false

    init {
        viewModelScope.launch {
            authRepository.observeAuthRequired()
                .collectLatest {
                    isAuth = it
                    isReady = true
                }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): MainActivityViewModel
    }
}