package com.saer.telegramnew.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.CORRECT_PHONE_NUMBER
import com.saer.telegramnew.INCORRECT_PHONE_NUMBER
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.communications.ResultCheckPhoneCommunication
import com.saer.telegramnew.communications.ResultSendCodeCommunication
import com.saer.telegramnew.interactors.AuthInteractor
import com.saer.telegramnew.model.*
import org.junit.Test

class EnterPhoneNumberFragmentViewModelTest {

    private val testResultCheckPhoneCommunication = TestResultCheckPhoneCommunication()
    private val testResultSendCodeCommunication = TestResultSendCodeCommunication()
    private val testAuthInteractor = TestAuthInteractor()
    private val testResources = TestResources()
    private val viewModel =
        EnterPhoneNumberFragmentViewModel(
            testResultCheckPhoneCommunication,
            testResultSendCodeCommunication,
            testAuthInteractor,
            testResources
        )

    @Test
    fun `test input phone number`() {
        assertThat(testResultSendCodeCommunication.result)
            .isEqualTo(null)
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
        assertThat(testResultSendCodeCommunication.result).isEqualTo(SuccessResult<Boolean>())

        viewModel.inputPhoneNumber("+7 (989) 263-47-70")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(SuccessResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(4)
        viewModel.sendCode()
        assertThat(testResultSendCodeCommunication.result).isEqualTo(SuccessResult<Boolean>())

        viewModel.inputPhoneNumber("79892634770")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(SuccessResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(5)
        viewModel.sendCode()
        assertThat(testResultSendCodeCommunication.result).isEqualTo(SuccessResult<Boolean>())

        viewModel.inputPhoneNumber("7989263477")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(ErrorResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(6)
        viewModel.sendCode()
        assertThat(testResultSendCodeCommunication.result).isEqualTo(ErrorResult<Boolean>())

        viewModel.inputPhoneNumber("7989263477asdf^0")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(SuccessResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(7)
        viewModel.sendCode()
        assertThat(testResultSendCodeCommunication.result).isEqualTo(SuccessResult<Boolean>())

        viewModel.inputPhoneNumber("7989263477asdf^00")
        assertThat(testResultCheckPhoneCommunication.result).isEqualTo(ErrorResult<Boolean>())
        assertThat(testResultCheckPhoneCommunication.count).isEqualTo(8)
        viewModel.sendCode()
        assertThat(testResultSendCodeCommunication.result).isEqualTo(ErrorResult<Boolean>())

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

    class TestResultCheckPhoneCommunication : ResultCheckPhoneCommunication {
        var count = 0
        var result: Result<Boolean>? = null

        override val value: Result<Boolean>?
            get() = result

        override fun map(data: Result<Boolean>) {
            count++
            result = data
        }

        override fun observe(viewLifecycleOwner: LifecycleOwner, observer: Observer<Result<Boolean>>) =
            Unit
    }

    class TestResultSendCodeCommunication : ResultSendCodeCommunication {
        var count = 0
        var result: Result<Boolean>? = null

        override val value: Result<Boolean>?
            get() = result

        override fun map(data: Result<Boolean>) {
            count++
            result = data
        }

        override fun observe(
            viewLifecycleOwner: LifecycleOwner,
            observer: Observer<Result<Boolean>>
        ) =
            Unit
    }

    class TestAuthInteractor : AuthInteractor {
        var countCallCheckPhoneNumber = 0

        override fun getToken(login: String, password: String): String {
            return ""
        }

        override fun checkPhoneNumber(phoneNumber: String): Result<Boolean> {
            countCallCheckPhoneNumber++
            return if (phoneNumber == CORRECT_PHONE_NUMBER) SuccessResult()
            else ErrorResult()
        }
    }

    class TestResources : Resources {
        override fun getInt(resId: Int): Int = 11
    }
}