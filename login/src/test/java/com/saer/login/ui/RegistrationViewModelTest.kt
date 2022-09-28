@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.saer.login.ui

import com.google.common.truth.Truth.assertThat
import com.saer.core.communications.Communication
import com.saer.login.MainDispatcherRule
import com.saer.login.UNIQUE_FIRST_NAME
import com.saer.login.mappers.MapperAuthorisationStateToRegisterUi
import com.saer.login.repositories.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.drinkless.td.libcore.telegram.TdApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class RegistrationViewModelTest(
    private val firstName: String,
    private val lastName: String,
    private val expectedUi: RegisterUi,
    private val clickRegister: Boolean,
) {

    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val registrationUiCommunication: Communication<RegisterUi> = mock()
    private val authRepository: AuthRepository = mock()
    private val mapper: MapperAuthorisationStateToRegisterUi = MapperAuthorisationStateToRegisterUi.Base()
    private lateinit var viewModel: RegistrationViewModel

    @Before
    fun setup() = runTest {
        val authStateFlow =
            MutableStateFlow<TdApi.AuthorizationState>(TdApi.AuthorizationStateWaitTdlibParameters())
        Mockito.`when`(authRepository.observeAuthState()).thenReturn(authStateFlow)
        Mockito.`when`(authRepository.sendName(any(), any())).thenAnswer {
            when (it.arguments[0]) {
                UNIQUE_FIRST_NAME -> authStateFlow.tryEmit(TdApi.AuthorizationStateReady())
//            BUSY_FIRST_NAME -> authStateFlow.tryEmit()
                else -> authStateFlow.tryEmit(TdApi.AuthorizationStateWaitRegistration())
            }
        }

        var data: RegisterUi? = null
        Mockito.`when`(registrationUiCommunication.value).thenAnswer { data }
        Mockito.`when`(registrationUiCommunication.map(any()))
            .thenAnswer {
                data = it.arguments[0] as RegisterUi
                return@thenAnswer null
            }

        viewModel = RegistrationViewModel(
            authRepository = authRepository,
            registrationUiCommunication = registrationUiCommunication,
            ioDispatcher = coroutineRule.testDispatcher,
            mapperAuthorisationStateToRegisterUi = mapper
        )
    }

    @Test
    fun `test enter first and last name `() = runTest {
        viewModel.firstName = firstName
        viewModel.lastName = lastName
        if (clickRegister) viewModel.registerUser()
        assertThat(registrationUiCommunication.value).isInstanceOf(expectedUi::class.java)
    }

    companion object {
        @Parameterized.Parameters(name = "{index}, firstName = {0}, lastName = {1}, expected = {2}, clickRegister = {3}")
        @JvmStatic
        fun data() = listOf(
            arrayOf("", "", RegisterUi.WaitEnterNameUi(), false),
            arrayOf("", "", RegisterUi.EnterFirstNameUi(), true),
            arrayOf(UNIQUE_FIRST_NAME, "", RegisterUi.SuccessRegisterUi(), true),
            arrayOf("", "Ifasdfa", RegisterUi.EnterFirstNameUi(), true)
        )
    }
}