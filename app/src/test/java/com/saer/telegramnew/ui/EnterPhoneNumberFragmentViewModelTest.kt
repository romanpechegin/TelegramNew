package com.saer.telegramnew.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.saer.telegramnew.communication.ResultCommunication
import com.saer.telegramnew.model.*
import org.junit.Assert.assertEquals
import org.junit.Test
import com.saer.telegramnew.R

class EnterPhoneNumberFragmentViewModelTest {

    @Test
    fun `test input phone number`() {
        val testCommunicationResult = TestCommunicationResult()
        val viewModel = EnterPhoneNumberFragmentViewModel(testCommunicationResult)

        viewModel.inputPhoneNumber("7")
        assertEquals(UserActionErrorResult<Any>(R.string.enter_phone_number), testCommunicationResult.result)
        assertEquals(1, testCommunicationResult.count)

        viewModel.inputPhoneNumber("")
        assertEquals(UserActionErrorResult<Any>(R.string.enter_phone_number), testCommunicationResult.result)
        assertEquals(2, testCommunicationResult.count)

        viewModel.inputPhoneNumber("+79892634770")
        assertEquals(UserActionSuccessResult<Any>(R.string.click_send_code), testCommunicationResult.result)
        assertEquals(3, testCommunicationResult.count)

        viewModel.inputPhoneNumber("+7 (989) 263-47-70")
        assertEquals(UserActionSuccessResult<Any>(R.string.click_send_code), testCommunicationResult.result)
        assertEquals(4, testCommunicationResult.count)

        viewModel.inputPhoneNumber("79892634770")
        assertEquals(UserActionSuccessResult<Any>(R.string.click_send_code), testCommunicationResult.result)
        assertEquals(5, testCommunicationResult.count)

        viewModel.inputPhoneNumber("7989263477")
        assertEquals(UserActionErrorResult<Any>(R.string.enter_phone_number), testCommunicationResult.result)
        assertEquals(6, testCommunicationResult.count)

        viewModel.inputPhoneNumber("7989263477asdf^5")
        assertEquals(UserActionSuccessResult<Any>(R.string.click_send_code), testCommunicationResult.result)
        assertEquals(7, testCommunicationResult.count)

        viewModel.inputPhoneNumber("7989263477asdf^56")
        assertEquals(UserActionErrorResult<Any>(R.string.enter_phone_number), testCommunicationResult.result)
        assertEquals(8, testCommunicationResult.count)

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