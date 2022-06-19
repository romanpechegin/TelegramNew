package com.saer.telegramnew.auth.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.*
import com.saer.telegramnew.auth.communication.EnterCodeUiCommunication
import com.saer.telegramnew.common.Resources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class EnterCodeViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val enterCodeUiCommunication = TestEnterCodeUiCommunication()
    private val resources = mock<Resources>()
    private val authRepository = TestAuthRepository()

    private val viewModel by lazy {
        EnterCodeViewModel(
            coroutineRule.testDispatcher,
            enterCodeUiCommunication,
            authRepository,
            resources
        )
    }

    @Before
    fun setup() {
        Mockito.`when`(resources.getInt(R.integer.code_size)).thenReturn(5)
    }

    @Test
    fun `test enter code`() = runTest {

        viewModel.enterCode("")
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitCodeUi::class.java)

        viewModel.enterCode("123")
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitCodeUi::class.java)

        viewModel.enterCode(CORRECT_CODE_FOR_PASSWORD)
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitPasswordUi::class.java)

        viewModel.enterCode("")
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitCodeUi::class.java)

        viewModel.enterCode(INCORRECT_CODE)
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.ErrorCodeUi::class.java)

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

}