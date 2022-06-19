package com.saer.telegramnew.auth.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.telegramnew.R
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.auth.communication.EnterCodeUiCommunication
import com.saer.telegramnew.auth.interactors.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class EnterCodeViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val enterCodeUiCommunication: EnterCodeUiCommunication,
    private val authRepository: AuthRepository,
    private val resources: Resources
) : ViewModel() {

    init {
        viewModelScope.launch {
            authRepository.observeAuthState()
                .map {
                    when (it) {
                        is TdApi.AuthorizationStateReady -> EnterCodeUi.SuccessAuthUi()
                        is TdApi.AuthorizationStateWaitPassword -> EnterCodeUi.WaitPasswordUi()
                        is TdApi.AuthorizationStateWaitCode -> EnterCodeUi.WaitCodeUi()
                        is TdApi.AuthorizationStateWaitPhoneNumber -> EnterCodeUi.WaitPhoneUi()
                        is TdApi.AuthorizationStateWaitTdlibParameters -> EnterCodeUi.WaitCodeUi()
                        is TdApi.AuthorizationStateWaitEncryptionKey -> EnterCodeUi.WaitCodeUi()
                        is TdApi.AuthorizationStateWaitRegistration -> EnterCodeUi.EnterNameUi()
                        else -> EnterCodeUi.ErrorCodeUi(IllegalStateException())
                    }
                }
                .catch { e ->
                    enterCodeUiCommunication.map(EnterCodeUi.ErrorCodeUi(e))
                }
                .collectLatest {
                    enterCodeUiCommunication.map(it)
                }
        }
    }

    fun observeEnterCodeUi(lifecycleOwner: LifecycleOwner, observer: Observer<EnterCodeUi>) {
        enterCodeUiCommunication.observe(lifecycleOwner, observer)
    }

    fun enterCode(code: String) {
        val legalCode = code.filter { it.isDigit() }

        if (legalCode.isNotEmpty() && legalCode.length == resources.getInt(R.integer.code_size)) {
            enterCodeUiCommunication.map(EnterCodeUi.CompleteEnterCodeUi())

                viewModelScope.launch(ioDispatcher) {
                    try {
                        authRepository.checkCode(code)
                    } catch (e: Throwable) {
                        enterCodeUiCommunication.map(EnterCodeUi.ErrorCodeUi(e))
                    }
                }
        } else {
            enterCodeUiCommunication.map(EnterCodeUi.WaitCodeUi())
        }
    }
}
