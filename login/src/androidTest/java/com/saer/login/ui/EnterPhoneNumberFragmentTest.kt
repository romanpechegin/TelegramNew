package com.saer.login.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.saer.login.R
import com.saer.login.utils.getNavController
import com.saer.login.utils.isKeyboardShown
import com.saer.login.utils.waitFor
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EnterPhoneNumberFragmentTest {

    @Test
    fun test_main_fragment() {
        val navController = getNavController<EnterPhoneNumberFragment>()

        onView(withId(R.id.enter_phone_number_title)).check(matches(withText(R.string.enter_phone_number)))
        onView(withId(R.id.send_code_button)).check(matches(not(isDisplayed())))
        onView(withId(R.id.input_phone_number)).check(matches(withText("+7 (")))
        onView(withId(R.id.input_phone_number)).check(matches(isFocused()))
        assertThat(isKeyboardShown()).isEqualTo(true)

        onView(withId(R.id.input_phone_number)).perform(replaceText("989"))
        onView(withId(R.id.input_phone_number)).check(matches(withText("+7 (989) ")))
        onView(withId(R.id.enter_phone_number_title)).check(matches(withText(R.string.enter_phone_number)))
        onView(withId(R.id.send_code_button)).check(matches(not(isDisplayed())))

        onView(withId(R.id.input_phone_number)).perform(replaceText("989263477"))
        onView(withId(R.id.input_phone_number)).check(matches(withText("+7 (989) 263-47-7")))
        onView(withId(R.id.enter_phone_number_title)).check(matches(withText(R.string.enter_phone_number)))
        onView(withId(R.id.send_code_button)).check(matches(not(isDisplayed())))

        onView(withId(R.id.input_phone_number)).perform(replaceText("9892634770"))
        onView(withId(R.id.input_phone_number)).check(matches(withText("+7 (989) 263-47-70")))
        onView(withId(R.id.enter_phone_number_title)).check(matches(withText(R.string.click_send_code)))
        onView(withId(R.id.send_code_button)).check(matches(isDisplayed()))

        onView(withId(R.id.input_phone_number)).perform(replaceText(""))
        onView(withId(R.id.input_phone_number)).check(matches(withText("")))
        onView(withId(R.id.enter_phone_number_title)).check(matches(withText(R.string.enter_phone_number)))
        onView(withId(R.id.send_code_button)).check(matches(not(isDisplayed())))

        onView(withId(R.id.input_phone_number)).perform(replaceText("8"))
        onView(withId(R.id.input_phone_number)).check(matches(withText("8 (")))
        onView(withId(R.id.enter_phone_number_title)).check(matches(withText(R.string.enter_phone_number)))
        onView(withId(R.id.send_code_button)).check(matches(not(isDisplayed())))

        onView(withId(R.id.input_phone_number)).perform(replaceText("7"))
        onView(withId(R.id.input_phone_number)).check(matches(withText("+7 (")))
        onView(withId(R.id.enter_phone_number_title)).check(matches(withText(R.string.enter_phone_number)))
        onView(withId(R.id.send_code_button)).check(matches(not(isDisplayed())))

        onView(withId(R.id.input_phone_number)).perform(replaceText("89892634770"))
        onView(withId(R.id.input_phone_number)).check(matches(withText("8 (989) 263-47-70")))
        onView(withId(R.id.enter_phone_number_title)).check(matches(withText(R.string.click_send_code)))
        onView(withId(R.id.send_code_button)).check(matches(isDisplayed()))

        onView(withId(R.id.input_phone_number)).perform(replaceText("898926347701234"))
        onView(withId(R.id.input_phone_number)).check(matches(withText("8 (989) 263-47-70")))
        onView(withId(R.id.enter_phone_number_title)).check(matches(withText(R.string.click_send_code)))
        onView(withId(R.id.send_code_button)).check(matches(isDisplayed()))

        onView(withId(R.id.send_code_button)).perform(click())
        onView(isRoot()).perform(waitFor(5000))
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.enterCodeFragment)
    }
}