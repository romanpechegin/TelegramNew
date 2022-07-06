package com.saer.login.ui

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
//    private val authRepository: AuthRepository,
//    private val registrationUiCommunication: Communication<RegisterUi>,
//    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
//    var firstName: String = ""
//    var lastName: String = ""
//
//    init {
//        viewModelScope.launch {
//            authRepository.observeAuthState()
//                .map {
//                    when (it) {
//                        is TdApi.AuthorizationStateReady -> RegisterUi.SuccessRegisterUi()
//                        is TdApi.AuthorizationStateWaitRegistration -> RegisterUi.WaitEnterNameUi()
//                        is TdApi.AuthorizationStateWaitTdlibParameters -> RegisterUi.WaitEnterNameUi()
//                        is TdApi.AuthorizationStateWaitEncryptionKey -> RegisterUi.WaitEnterNameUi()
//                        else -> RegisterUi.ErrorUi(Throwable(it.javaClass.simpleName))
//                    }
//                }
//                .catch { e ->
//                    registrationUiCommunication.map(RegisterUi.ErrorUi(e))
//                }
//                .collectLatest {
//                    registrationUiCommunication.map(it)
//                }
//        }
//    }
//
//    fun observeRegistrationUi(
//        lifecycleCoroutineScope: LifecycleCoroutineScope,
//        collector: FlowCollector<RegisterUi>
//    ) =
//        registrationUiCommunication.observe(
//            lifecycleCoroutineScope = lifecycleCoroutineScope,
//            collector = collector
//        )
//
//    fun registerUser() {
//        if (firstName.isNotEmpty()) {
//            viewModelScope.launch(ioDispatcher) {
//                try {
//                    registrationUiCommunication.map(RegisterUi.CheckingNameUi())
//                    authRepository.sendName(firstName, lastName)
//                } catch (e: Throwable) {
//                    registrationUiCommunication.map(RegisterUi.ErrorUi(e))
//                }
//            }
//        } else {
//            registrationUiCommunication.map(RegisterUi.EnterFirstNameUi())
//        }
//    }
}