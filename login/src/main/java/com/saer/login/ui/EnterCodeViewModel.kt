package com.saer.login.ui

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saer.core.Communication
import com.saer.core.Resources
import com.saer.core.di.IoDispatcher
import com.saer.core.di.LoginFeature
import com.saer.login.R
import com.saer.login.repositories.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class EnterCodeViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val enterCodeUiCommunication: Communication<EnterCodeUi>,
    private val authRepository: AuthRepository,
    private val resources: Resources
) : ViewModel() {

    init {
        viewModelScope.launch {
            authRepository.observeAuthState()
                .map {
                    Log.e("TAG", "authorizationState: $it")
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

    fun observeEnterCodeUi(
        lifecycleOwner: LifecycleOwner,
        collector: (value: EnterCodeUi) -> Unit
    ) {
        enterCodeUiCommunication.observe(lifecycleOwner, collector)
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

    fun sendCode(phoneNumber: String) {
        viewModelScope.launch {
            authRepository.checkPhoneNumber(phoneNumber)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @LoginFeature
    class Factory @Inject constructor(
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        private val enterCodeUiCommunication: Communication<EnterCodeUi>,
        private val authRepository: AuthRepository,
        private val resources: Resources
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == EnterCodeViewModel::class.java)
            return EnterCodeViewModel(
                ioDispatcher,
                enterCodeUiCommunication,
                authRepository,
                resources
            ) as T
        }
    }
}
