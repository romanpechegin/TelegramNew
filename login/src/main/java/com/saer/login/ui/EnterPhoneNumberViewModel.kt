package com.saer.login.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.core.Communication
import com.saer.core.Resources
import com.saer.core.common.InputMask
import com.saer.core.di.IoDispatcher
import com.saer.core.utils.phoneNumberOrNull
import com.saer.login.R
import com.saer.login.mappers.MapperAuthorisationStateToEnterPhoneUi
import com.saer.login.repositories.AuthRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EnterPhoneNumberViewModel @AssistedInject constructor(
    private val enterPhoneUiCommunication: Communication<EnterPhoneUi>,
    private val inputMaskCommunication: Communication<InputMask>,
    private val authRepository: AuthRepository,
    private val resources: Resources,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
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
            inputMaskCommunication.map(inputMask)
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
    ) = inputMaskCommunication.observe(
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

    @AssistedFactory
    interface Factory {
        fun create(): EnterPhoneNumberViewModel
    }
}