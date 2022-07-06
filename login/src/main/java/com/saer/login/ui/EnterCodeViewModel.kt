package com.saer.login.ui

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class EnterCodeViewModel @Inject constructor(
//    private val ioDispatcher: CoroutineDispatcher,
//    private val enterCodeUiCommunication: Communication<EnterCodeUi>,
//    private val authRepository: AuthRepository,
//    private val resources: Resources
) : ViewModel() {

    init {
//        viewModelScope.launch {
//            authRepository.observeAuthState()
//                .map {
//                    when (it) {
//                        is TdApi.AuthorizationStateReady -> EnterCodeUi.SuccessAuthUi()
//                        is TdApi.AuthorizationStateWaitPassword -> EnterCodeUi.WaitPasswordUi()
//                        is TdApi.AuthorizationStateWaitCode -> EnterCodeUi.WaitCodeUi()
//                        is TdApi.AuthorizationStateWaitPhoneNumber -> EnterCodeUi.WaitPhoneUi()
//                        is TdApi.AuthorizationStateWaitTdlibParameters -> EnterCodeUi.WaitCodeUi()
//                        is TdApi.AuthorizationStateWaitEncryptionKey -> EnterCodeUi.WaitCodeUi()
//                        is TdApi.AuthorizationStateWaitRegistration -> EnterCodeUi.EnterNameUi()
//                        else -> EnterCodeUi.ErrorCodeUi(IllegalStateException())
//                    }
//                }
//                .catch { e ->
//                    enterCodeUiCommunication.map(EnterCodeUi.ErrorCodeUi(e))
//                }
//                .collectLatest {
//                    enterCodeUiCommunication.map(it)
//                }
//        }
    }

//    fun observeEnterCodeUi(lifecycleCoroutineScope: LifecycleCoroutineScope, collector: FlowCollector<EnterCodeUi>) {
//        enterCodeUiCommunication.observe(
//            lifecycleCoroutineScope = lifecycleCoroutineScope,
//            collector = collector
//        )
//    }

    fun enterCode(code: String) {
//        val legalCode = code.filter { it.isDigit() }
//
//        if (legalCode.isNotEmpty() && legalCode.length == resources.getInt(R.integer.code_size)) {
//            enterCodeUiCommunication.map(EnterCodeUi.CompleteEnterCodeUi())
//
//                viewModelScope.launch(ioDispatcher) {
//                    try {
//                        authRepository.checkCode(code)
//                    } catch (e: Throwable) {
//                        enterCodeUiCommunication.map(EnterCodeUi.ErrorCodeUi(e))
//                    }
//                }
//        } else {
//            enterCodeUiCommunication.map(EnterCodeUi.WaitCodeUi())
//        }
    }
}
