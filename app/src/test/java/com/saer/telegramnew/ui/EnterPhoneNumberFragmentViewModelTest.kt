package com.saer.telegramnew.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.R
import com.saer.telegramnew.communication.ResultCommunication
import com.saer.telegramnew.model.PendingResult
import com.saer.telegramnew.model.Result
import com.saer.telegramnew.model.UserActionErrorResult
import com.saer.telegramnew.model.UserActionSuccessResult
import org.junit.Test

class EnterPhoneNumberFragmentViewModelTest {

    @Test
    fun `test input phone number`() {
        val testCommunicationResult = TestCommunicationResult()
        val viewModel = EnterPhoneNumberFragmentViewModel(testCommunicationResult)

        viewModel.inputPhoneNumber("7")
        assertThat(testCommunicationResult.result)
            .isEqualTo(UserActionErrorResult<Any>(R.string.enter_phone_number))
        assertThat(testCommunicationResult.count).isEqualTo(1)

        viewModel.inputPhoneNumber("")
        assertThat(testCommunicationResult.result)
            .isEqualTo(UserActionErrorResult<Any>(R.string.enter_phone_number))
        assertThat(testCommunicationResult.count).isEqualTo(2)

        viewModel.inputPhoneNumber("+79892634770")
        assertThat(testCommunicationResult.result)
            .isEqualTo(UserActionSuccessResult<Any>(R.string.click_send_code))
        assertThat(testCommunicationResult.count).isEqualTo(3)

        viewModel.inputPhoneNumber("+7 (989) 263-47-70")
        assertThat(testCommunicationResult.result)
            .isEqualTo(UserActionSuccessResult<Any>(R.string.click_send_code))
        assertThat(testCommunicationResult.count).isEqualTo(4)

        viewModel.inputPhoneNumber("79892634770")
        assertThat(testCommunicationResult.result)
            .isEqualTo(UserActionSuccessResult<Any>(R.string.click_send_code))
        assertThat(testCommunicationResult.count).isEqualTo(5)

        viewModel.inputPhoneNumber("7989263477")
        assertThat(testCommunicationResult.result)
            .isEqualTo(UserActionErrorResult<Any>(R.string.enter_phone_number))
        assertThat(testCommunicationResult.count).isEqualTo(6)

        viewModel.inputPhoneNumber("7989263477asdf^5")
        assertThat(testCommunicationResult.result)
            .isEqualTo(UserActionSuccessResult<Any>(R.string.click_send_code))
        assertThat(testCommunicationResult.count).isEqualTo(7)

        viewModel.inputPhoneNumber("7989263477asdf^56")
        assertThat(testCommunicationResult.result)
            .isEqualTo(UserActionErrorResult<Any>(R.string.enter_phone_number))
        assertThat(testCommunicationResult.count).isEqualTo(8)

    }

    class TestCommunicationResult : ResultCommunication {
        var count = 0
        var result: Result<Any> = PendingResult()

        override fun map(data: Result<Any>) {
            count++
            result = data
        }

        override fun observe(viewLifecycleOwner: LifecycleOwner, observer: Observer<Result<Any>>) =
            Unit
    }


}