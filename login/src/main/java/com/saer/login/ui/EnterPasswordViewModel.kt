package com.saer.login.ui

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class EnterPasswordViewModel @Inject constructor(
//    private val authRepository: AuthRepository,
//    private val enterPasswordUiCommunication: Communication<EnterPasswordUi>,
//    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    init {
//        viewModelScope.launch {
//            authRepository.observeAuthState()
//                .map {
//                    when (it) {
//                        is TdApi.AuthorizationStateReady -> TODO()
//                        is TdApi.AuthorizationStateWaitRegistration -> TODO()
//                        is TdApi.AuthorizationStateWaitTdlibParameters -> TODO()
//                        is TdApi.AuthorizationStateWaitEncryptionKey -> TODO()
//                        else -> TODO()
//                    }
//                }
//                .catch { e ->
//                    enterPasswordUiCommunication.map(EnterPasswordUi.ErrorUi(e))
//                }
//                .collectLatest {
//                    enterPasswordUiCommunication.map(it)
//                }
//        }
    }

    fun enterPassword(password: String) {

    }
}