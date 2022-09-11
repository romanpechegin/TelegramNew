package com.saer.login.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saer.core.Communication
import com.saer.core.Resources
import com.saer.core.common.InputMask
import com.saer.core.di.IoDispatcher
import com.saer.core.di.LoginFeature
import com.saer.core.utils.phoneNumberOrNull
import com.saer.login.R
import com.saer.login.mappers.MapperAuthorisationStateToEnterPhoneUi
import com.saer.login.repositories.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class EnterPhoneNumberViewModel(
    private val enterPhoneUiCommunication: Communication<EnterPhoneUi>,
    private val countryCommunication: Communication<InputMask>,
    private val authRepository: AuthRepository,
    private val resources: Resources,
    private val ioDispatcher: CoroutineDispatcher,
    uiMapper: MapperAuthorisationStateToEnterPhoneUi,
) : ViewModel() {

    private var phoneNumber: String = ""
    private var correctPhoneNumber: String? = null

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                enterPhoneUiCommunication.map(EnterPhoneUi.ErrorPhoneUi(throwable))
            }
        }

    init {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            authRepository.observeAuthState()
                .map(uiMapper::map)
                .collectLatest(enterPhoneUiCommunication::map)
        }

        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            val inputMask: InputMask = InputMask.PhoneNumberMask(
                resources = resources,
                currentCountryCode = authRepository.currentCountry().text,
                countries = authRepository.countries().countries
            )
            countryCommunication.map(inputMask)
        }
    }

    fun onStop() {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
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
                viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
                    enterPhoneUiCommunication.map(EnterPhoneUi.WaitEnterPhoneUi())
                }
            }
        }
    }

    fun observeEnterPhoneUi(
        viewLifecycleOwner: LifecycleOwner,
        collector: (value: EnterPhoneUi) -> Unit,
    ) = enterPhoneUiCommunication.observe(
        viewLifecycleOwner = viewLifecycleOwner,
        collector = collector
    )

    fun observeCountryCommunication(
        viewLifecycleOwner: LifecycleOwner,
        collector: (value: InputMask) -> Unit,
    ) = countryCommunication.observe(
        viewLifecycleOwner = viewLifecycleOwner,
        collector = collector
    )

    fun checkPhoneNumber() {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            if (correctPhoneNumber != null) {
                correctPhoneNumber?.let {
                    enterPhoneUiCommunication.map(EnterPhoneUi.PendingResultSendingPhoneUi())
                    authRepository.checkPhoneNumber(it)
                }
            } else enterPhoneUiCommunication.map(EnterPhoneUi.PhoneIsNotComplete())
        }
    }

    @Suppress("UNCHECKED_CAST")
    @LoginFeature
    class Factory @Inject constructor(
        private val enterPhoneUiCommunication: Communication<EnterPhoneUi>,
        private val countryCommunication: Communication<InputMask>,
        private val authRepository: AuthRepository,
        private val resources: Resources,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        private val mapperAuthorisationStateToEnterPhoneUi: MapperAuthorisationStateToEnterPhoneUi,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == EnterPhoneNumberViewModel::class.java)
            return EnterPhoneNumberViewModel(
                enterPhoneUiCommunication,
                countryCommunication,
                authRepository,
                resources,
                ioDispatcher,
                mapperAuthorisationStateToEnterPhoneUi
            ) as T
        }
    }
}