package com.saer.telegramnew.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.telegramnew.R
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.communications.EnterCodeUiCommunication
import com.saer.telegramnew.interactors.AuthInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class EnterCodeFragmentViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val enterCodeUiCommunication: EnterCodeUiCommunication,
    private val authInteractor: AuthInteractor,
    private val resources: Resources
) : ViewModel() {

    init {
        viewModelScope.launch {
            authInteractor.observeAuthState()
                .map {
                    when (it) {
                        is TdApi.AuthorizationStateReady -> EnterCodeUi.SuccessAuthUi()
                        is TdApi.AuthorizationStateWaitPassword -> EnterCodeUi.WaitPasswordUi()
                        is TdApi.AuthorizationStateWaitCode -> EnterCodeUi.WaitCodeUi()
                        is TdApi.AuthorizationStateWaitPhoneNumber -> EnterCodeUi.WaitPhoneUi()
                        is TdApi.AuthorizationStateWaitTdlibParameters -> EnterCodeUi.WaitCodeUi()
                        is TdApi.AuthorizationStateWaitEncryptionKey -> EnterCodeUi.WaitCodeUi()
                        else -> EnterCodeUi.ErrorCodeUi()
                    }
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
                authInteractor.checkCode(code)
            }
        } else {
            enterCodeUiCommunication.map(EnterCodeUi.WaitCodeUi())
        }
    }
}
