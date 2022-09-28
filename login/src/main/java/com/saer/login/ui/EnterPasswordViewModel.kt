package com.saer.login.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.core.communications.Communication
import com.saer.core.di.IoDispatcher
import com.saer.login.repositories.AuthRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi

class EnterPasswordViewModel @AssistedInject constructor(
    private val authRepository: AuthRepository,
    private val enterPasswordUiCommunication: Communication<EnterPasswordUi>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    init {
        viewModelScope.launch {
            authRepository.observeAuthState()
                .map {
                    when (it) {
                        is TdApi.AuthorizationStateReady -> EnterPasswordUi.Success()
                        is TdApi.AuthorizationStateWaitRegistration -> EnterPasswordUi.Registration()
                        is TdApi.AuthorizationStateWaitTdlibParameters -> EnterPasswordUi.Wait()
                        is TdApi.AuthorizationStateWaitEncryptionKey -> EnterPasswordUi.Wait()
                        else -> EnterPasswordUi.ErrorUi(Throwable(it.javaClass.simpleName))
                    }
                }
                .catch { e ->
                    enterPasswordUiCommunication.map(EnterPasswordUi.ErrorUi(e))
                }
                .collectLatest {
                    enterPasswordUiCommunication.map(data = it)
                }
        }
    }

    fun observeEnterPasswordUiCommunication(
        viewLifecycleOwner: LifecycleOwner,
        collector: (value: EnterPasswordUi) -> Unit,
    ) = enterPasswordUiCommunication.observe(
        viewLifecycleOwner,
        collector
    )

    fun enterPassword(password: String) {
        viewModelScope.launch(ioDispatcher) {
            authRepository.checkPassword(password)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): EnterPasswordViewModel
    }
}