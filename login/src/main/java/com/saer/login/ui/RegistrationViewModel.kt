package com.saer.login.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.core.Communication
import com.saer.core.di.IoDispatcher
import com.saer.login.repositories.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val registrationUiCommunication: Communication<RegisterUi>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    var firstName: String = ""
    var lastName: String = ""

    init {
        viewModelScope.launch {
            authRepository.observeAuthState()
                .map {
                    when (it) {
                        is TdApi.AuthorizationStateReady -> RegisterUi.SuccessRegisterUi()
                        is TdApi.AuthorizationStateWaitRegistration -> RegisterUi.WaitEnterNameUi()
                        is TdApi.AuthorizationStateWaitTdlibParameters -> RegisterUi.WaitEnterNameUi()
                        is TdApi.AuthorizationStateWaitEncryptionKey -> RegisterUi.WaitEnterNameUi()
                        else -> RegisterUi.ErrorUi(Throwable(it.javaClass.simpleName))
                    }
                }
                .catch { e ->
                    registrationUiCommunication.map(RegisterUi.ErrorUi(e))
                }
                .collectLatest {
                    registrationUiCommunication.map(it)
                }
        }
    }

    fun observeRegistrationUi(
        lifecycleOwner: LifecycleOwner,
        collector: (value: RegisterUi) -> Unit
    ) {
        registrationUiCommunication.observe(lifecycleOwner, collector)
    }

    fun registerUser() {
        viewModelScope.launch(ioDispatcher) {
            if (firstName.isNotEmpty()) {
                try {
                    registrationUiCommunication.map(RegisterUi.CheckingNameUi())
                    authRepository.sendName(firstName, lastName)
                } catch (e: Throwable) {
                    registrationUiCommunication.map(RegisterUi.ErrorUi(e))
                }
            } else {
                registrationUiCommunication.map(RegisterUi.EnterFirstNameUi())
            }
        }
    }
}