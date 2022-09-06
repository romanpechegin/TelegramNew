package com.saer.login.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saer.core.Communication
import com.saer.core.Resources
import com.saer.core.di.IoDispatcher
import com.saer.core.di.LoginFeature
import com.saer.core.utils.phoneNumberOrNull
import com.saer.login.R
import com.saer.login.repositories.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

class EnterPhoneNumberViewModel(
    private val enterPhoneUiCommunication: Communication<EnterPhoneUi>,
    private val authRepository: AuthRepository,
    private val resources: Resources,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var phoneNumber: String = ""
    private var correctPhoneNumber: String? = null

    init {
        viewModelScope.launch {
            authRepository.observeAuthState()
                .map {
                    when (it) {
                        is TdApi.AuthorizationStateReady -> EnterPhoneUi.WaitEnterPhoneUi()
                        is TdApi.AuthorizationStateWaitCode -> EnterPhoneUi.NavigateToEnterCodeUi(
                            phoneNumber
                        )
                        is TdApi.AuthorizationStateWaitPassword -> EnterPhoneUi.WaitEnterPhoneUi()
                        is TdApi.AuthorizationStateWaitPhoneNumber -> EnterPhoneUi.WaitEnterPhoneUi()
                        is TdApi.AuthorizationStateWaitTdlibParameters -> EnterPhoneUi.WaitEnterPhoneUi()
                        is TdApi.AuthorizationStateWaitEncryptionKey -> EnterPhoneUi.WaitEnterPhoneUi()
                        else -> EnterPhoneUi.ErrorPhoneUi(Throwable(it.javaClass.simpleName))
                    }
                }
                .catch { e ->
                    enterPhoneUiCommunication.map(EnterPhoneUi.ErrorPhoneUi(e))
                }
                .collectLatest {
                    enterPhoneUiCommunication.map(it)
                }
        }
    }

    fun onStop() {
        viewModelScope.launch {
            if (enterPhoneUiCommunication.value is EnterPhoneUi.NavigateToEnterCodeUi)
                enterPhoneUiCommunication.map(EnterPhoneUi.WaitEnterPhoneUi())
        }
    }

    fun enterPhoneNumber(phoneNumber: String) {
        if (this.phoneNumber != phoneNumber) {
            this.phoneNumber = phoneNumber
            correctPhoneNumber =
                phoneNumberOrNull(phoneNumber, resources.getInt(R.integer.phone_size))

            if (enterPhoneUiCommunication.value is EnterPhoneUi.PhoneIsNotComplete) {
                viewModelScope.launch {
                    enterPhoneUiCommunication.map(EnterPhoneUi.WaitEnterPhoneUi())
                }
            }
        }
    }

    fun observeEnterPhoneUi(
        viewLifecycleOwner: LifecycleOwner,
        collector: (value: EnterPhoneUi) -> Unit
    ) = enterPhoneUiCommunication.observe(
        viewLifecycleOwner = viewLifecycleOwner,
        collector = collector
    )

    fun checkPhoneNumber() {
        viewModelScope.launch(ioDispatcher) {
            if (correctPhoneNumber != null) {
                try {
                    correctPhoneNumber?.let {
                        enterPhoneUiCommunication.map(EnterPhoneUi.PendingResultSendingPhoneUi())
                        authRepository.checkPhoneNumber(it)
                    }
                } catch (e: Throwable) {
                    enterPhoneUiCommunication.map(EnterPhoneUi.ErrorPhoneUi(e))
                }
            } else enterPhoneUiCommunication.map(EnterPhoneUi.PhoneIsNotComplete())
        }
    }

    @Suppress("UNCHECKED_CAST")
    @LoginFeature
    class Factory @Inject constructor(
        private val enterPhoneUiCommunication: Communication<EnterPhoneUi>,
        private val authRepository: AuthRepository,
        private val resources: Resources,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == EnterPhoneNumberViewModel::class.java)
            return EnterPhoneNumberViewModel(
                enterPhoneUiCommunication,
                authRepository,
                resources,
                ioDispatcher
            ) as T
        }
    }
}