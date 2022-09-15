package com.saer.core.common

import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import com.saer.core.R
import com.saer.core.Resources
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

interface InputMask {

    fun setMask(editText: EditText)
    fun currentMaskName(): String

    class EmptyMask : InputMask {
        override fun setMask(editText: EditText) {}
        override fun currentMaskName(): String = ""
    }

    class PhoneNumberMask @Inject constructor(
        private val resources: Resources,
        private val currentCountryCode: String,
        private val countries: Array<TdApi.CountryInfo>,
    ) : InputMask {

        override fun setMask(editText: EditText) {
            val callingCodes: List<String> = countries
                .filter { countryInfo ->
                    countryInfo.countryCode == currentCountryCode
                }.map { countryInfo ->
                    countryInfo.callingCodes.first()
                }

            val maskedTextChangedListener = MaskedTextChangedListener.installOn(
                editText = editText,
                primaryFormat = resources.getString(R.string.phone_number_mask, callingCodes.first()),
                affinityCalculationStrategy = AffinityCalculationStrategy.PREFIX,
            )
            editText.hint = maskedTextChangedListener.placeholder()
        }

        override fun currentMaskName(): String {
            return countries.first { countryInfo ->
                countryInfo.countryCode == currentCountryCode
            }.englishName
        }
    }
}