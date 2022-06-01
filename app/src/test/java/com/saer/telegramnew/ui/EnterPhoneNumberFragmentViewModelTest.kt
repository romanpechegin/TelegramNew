package com.saer.telegramnew.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.CORRECT_PHONE_NUMBER
import com.saer.telegramnew.INCORRECT_PHONE_NUMBER
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.communications.EnterPhoneUiCommunication
import com.saer.telegramnew.communications.ResultSendCodeCommunication
import com.saer.telegramnew.interactors.AuthInteractor
import com.saer.telegramnew.model.*
import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.TdApi
import org.junit.Test

class EnterPhoneNumberFragmentViewModelTest {

    private val testResultCheckPhoneCommunication = TestEnterPhoneUiCommunication()
    private val testAuthInteractor = TestAuthInteractor()
    private val testResources = TestResources()
    private val viewModel =
        EnterPhoneNumberFragmentViewModel(
            testResultCheckPhoneCommunication,
            testAuthInteractor,
            testResources
        )

    @Test
    fun `test input phone number`() {
        assertThat(testResultCheckPhoneCommunication.result)
            .isEqualTo(null)

        viewModel.inputPhoneNumber("7")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(ErrorResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(1)

        viewModel.inputPhoneNumber("")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(ErrorResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(2)

        viewModel.inputPhoneNumber("+79892634770")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(SuccessResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(3)
        viewModel.sendCode()

        viewModel.inputPhoneNumber("+7 (989) 263-47-70")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(SuccessResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(4)
        viewModel.sendCode()

        viewModel.inputPhoneNumber("79892634770")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(SuccessResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(5)
        viewModel.sendCode()

        viewModel.inputPhoneNumber("7989263477")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(ErrorResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(6)
        viewModel.sendCode()

        viewModel.inputPhoneNumber("7989263477asdf^0")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(SuccessResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(7)
        viewModel.sendCode()

        viewModel.inputPhoneNumber("7989263477asdf^00")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(ErrorResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(8)
        viewModel.sendCode()
    }

    @Test
    fun `test check phone number`() {
        viewModel.inputPhoneNumber(CORRECT_PHONE_NUMBER)
        assertThat(testResultCheckPhoneCommunication.result)
            .isEqualTo(SuccessResult<Any>())

        viewModel.sendCode()
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(SuccessResult<Any>())

        viewModel.inputPhoneNumber(INCORRECT_PHONE_NUMBER)
        assertThat(testResultCheckPhoneCommunication.result)
            .isEqualTo(ErrorResult<Boolean>())

        viewModel.sendCode()
        assertThat(testResultCheckPhoneCommunication.result)
            .isEqualTo(ErrorResult<Boolean>())

    }

    class TestEnterPhoneUiCommunication : EnterPhoneUiCommunication {
        var count = 0
        var result: EnterPhoneUi? = null

        override val value: EnterPhoneUi?
            get() = result

        override fun map(data: EnterPhoneUi) {
            count++
            result = data
        }

        override fun observe(
            viewLifecycleOwner: LifecycleOwner,
            observer: Observer<EnterPhoneUi>
        ) = Unit
    }

    class TestAuthInteractor : AuthInteractor {
        var countCallCheckPhoneNumber = 0

        override suspend fun checkPhoneNumber(phoneNumber: String) {
            countCallCheckPhoneNumber++
        }

        override fun observeAuthState(): Flow<TdApi.AuthorizationState> {
            TODO("Not yet implemented")
        }
    }

    class TestResources : Resources {
        override fun getInt(resId: Int): Int = 11
    }
}