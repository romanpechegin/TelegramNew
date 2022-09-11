package com.saer.login.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.core.Communication
import com.saer.core.di.IoDispatcher
import com.saer.login.mappers.MapperAuthorisationStateToRegisterUi
import com.saer.login.repositories.AuthRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RegistrationViewModel @AssistedInject constructor(
    private val authRepository: AuthRepository,
    private val registrationUiCommunication: Communication<RegisterUi>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    mapperAuthorisationStateToRegisterUi: MapperAuthorisationStateToRegisterUi
) : ViewModel() {
    var firstName: String = ""
    var lastName: String = ""

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                registrationUiCommunication.map(RegisterUi.ErrorUi(throwable))
            }
        }

    init {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            authRepository.observeAuthState()
                .map(mapperAuthorisationStateToRegisterUi::map)
                .collectLatest(registrationUiCommunication::map)
        }
    }

    fun observeRegistrationUi(
        lifecycleOwner: LifecycleOwner,
        collector: (value: RegisterUi) -> Unit,
    ) {
        registrationUiCommunication.observe(lifecycleOwner, collector)
    }

    fun registerUser() {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            if (firstName.isNotEmpty()) {
                registrationUiCommunication.map(RegisterUi.CheckingNameUi())
                authRepository.sendName(firstName, lastName)
            } else {
                registrationUiCommunication.map(RegisterUi.EnterFirstNameUi())
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): RegistrationViewModel
    }
}