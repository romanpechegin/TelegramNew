package com.saer.telegramnew.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.saer.telegramnew.R
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.MainDispatcherRule
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.communications.EnterCodeUiCommunication
import com.saer.telegramnew.interactors.AuthInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.drinkless.td.libcore.telegram.TdApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

private const val CORRECT_CODE_FOR_PASSWORD = "123456"
private const val CORRECT_CODE = "111111"

@OptIn(ExperimentalCoroutinesApi::class)
class EnterCodeFragmentViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val enterCodeUiCommunication = TestEnterCodeUiCommunication()
    private val resources = mock<Resources>()
    private val authInteractor = TestAuthInteractor()

    private val viewModel by lazy {
        EnterCodeFragmentViewModel(
            coroutineRule.testDispatcher,
            enterCodeUiCommunication,
            authInteractor,
            resources
        )
    }

    @Before
    fun setup() {
        Mockito.`when`(resources.getInt(R.integer.code_size)).thenReturn(5)
    }

    @Test
    fun `check enter code`() = runTest {

        viewModel.enterCode("")
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitCodeUi::class.java)

        viewModel.enterCode("123")
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitCodeUi::class.java)

        viewModel.enterCode(CORRECT_CODE_FOR_PASSWORD)
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitPasswordUi::class.java)

        viewModel.enterCode("")
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitCodeUi::class.java)

        viewModel.enterCode(CORRECT_CODE)
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.SuccessAuthUi::class.java)

        viewModel.enterCode("asdffdsafads")
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitCodeUi::class.java)
    }

    class TestEnterCodeUiCommunication : EnterCodeUiCommunication {
        private var result: EnterCodeUi? = null

        override val value: EnterCodeUi?
            get() = result

        override fun map(data: EnterCodeUi) {
            result = data
        }

        override fun observe(viewLifecycleOwner: LifecycleOwner, observer: Observer<EnterCodeUi>) =
            Unit

    }

    class TestAuthInteractor : AuthInteractor {
        private val stateFlow =
            MutableStateFlow<TdApi.AuthorizationState>(TdApi.AuthorizationStateWaitCode())

        override fun observeAuthState(): Flow<TdApi.AuthorizationState> = stateFlow

        override suspend fun checkPhoneNumber(phoneNumber: String) {}

        override suspend fun checkCode(code: String) {
            when (code) {
                CORRECT_CODE_FOR_PASSWORD -> stateFlow.emit(TdApi.AuthorizationStateWaitPassword())
                CORRECT_CODE -> stateFlow.emit(TdApi.AuthorizationStateReady())
                else -> stateFlow.emit(TdApi.AuthorizationStateWaitCode())
            }
        }
    }
}