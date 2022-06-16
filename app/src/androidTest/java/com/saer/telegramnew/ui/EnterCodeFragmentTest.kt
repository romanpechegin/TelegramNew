package com.saer.telegramnew.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.saer.telegramnew.utils.getNavController
import org.junit.Test
import com.saer.telegramnew.R
import com.saer.telegramnew.auth.ui.EnterCodeFragment
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EnterCodeFragmentTest {

    @Test
    fun test_input_code() {

        val navController = getNavController<EnterCodeFragment>()

//        onView(R.id.inputCodeEditText).check(matches(withText("")))
//        onView(R.id.inputCodeEditText).perform(replaceText("111111"))
//        onView(R.id.inputCodeEditText).check(matches(withText("111111")))


    }

    
}