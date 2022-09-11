package com.saer.login.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saer.core.Communication
import com.saer.core.Resources
import com.saer.core.di.IoDispatcher
import com.saer.core.di.LoginFeature
import com.saer.login.R
import com.saer.login.mappers.MapperAuthorisationStateToEnterCodeUi
import com.saer.login.repositories.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class EnterCodeViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val enterCodeUiCommunication: Communication<EnterCodeUi>,
    private val authRepository: AuthRepository,
    private val resources: Resources,
    mapperAuthorisationStateToEnterCodeUi: MapperAuthorisationStateToEnterCodeUi
) : ViewModel() {

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                enterCodeUiCommunication.map(EnterCodeUi.ErrorCodeUi(throwable))
            }
        }

    init {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            authRepository.observeAuthState()
                .map(mapperAuthorisationStateToEnterCodeUi::map)
                .collectLatest(enterCodeUiCommunication::map)
        }
    }

    fun onStop() {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            if (enterCodeUiCommunication.value is EnterCodeUi.NavigateToEnterPasswordUi ||
                enterCodeUiCommunication.value is EnterCodeUi.NavigateToRegistrationUi
            ) {
                enterCodeUiCommunication.map(EnterCodeUi.WaitCodeUi())
            }
        }
    }

    fun observeEnterCodeUi(
        lifecycleOwner: LifecycleOwner,
        collector: (value: EnterCodeUi) -> Unit,
    ) {
        enterCodeUiCommunication.observe(lifecycleOwner, collector)
    }

    fun enterCode(code: String) {
        val legalCode = code.filter { it.isDigit() }

        if (legalCode.isNotEmpty() && legalCode.length == resources.getInt(R.integer.code_size)) {
            viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
                enterCodeUiCommunication.map(EnterCodeUi.CompleteEnterCodeUi())
                authRepository.checkCode(code)
            }
        }
    }

    fun sendCode(phoneNumber: String) {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            if (phoneNumber.isEmpty()) enterCodeUiCommunication.map(
                EnterCodeUi.ErrorCodeUi(Throwable("Phone number is null"))
            )
            else {
                enterCodeUiCommunication.map(EnterCodeUi.ResendingCodeUi())
                authRepository.checkPhoneNumber(phoneNumber)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @LoginFeature
    class Factory @Inject constructor(
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        private val enterCodeUiCommunication: Communication<EnterCodeUi>,
        private val authRepository: AuthRepository,
        private val resources: Resources,
        private val mapperAuthorisationStateToEnterCodeUi: MapperAuthorisationStateToEnterCodeUi
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == EnterCodeViewModel::class.java)
            return EnterCodeViewModel(
                ioDispatcher,
                enterCodeUiCommunication,
                authRepository,
                resources,
                mapperAuthorisationStateToEnterCodeUi
            ) as T
        }
    }
}
