package com.saer.core.common

import android.widget.EditText
import com.saer.core.R
import com.saer.core.Resources
import com.saer.core.entities.Country

interface InputMask {

    fun setMask(editText: EditText)
    fun currentMaskName(): String

    class EmptyMask : InputMask {
        override fun setMask(editText: EditText) {}
        override fun currentMaskName(): String = ""
    }

//    class PhoneNumberMask(
//        private val resources: Resources,
//        private val currentCountryCode: String,
//        private val countries: List<Country>,
//    ) : InputMask {
//        private var maskedTextChangedListener: MaskedTextChangedListener? = null
//        private var editText: WeakReference<EditText?> = WeakReference(null)
//
//        override fun setMask(editText: EditText) {
//            this.editText = WeakReference(editText)
//            val callingCodes: List<String> = countries
//                .filter { country ->
//                    country.countryCode == currentCountryCode
//                }.map {
//                    it.callingCodes.first()
//                }
//
//            editText.removeTextChangedListener(maskedTextChangedListener)
//            maskedTextChangedListener = MaskedTextChangedListener.installOn(
//                editText = editText,
//                primaryFormat = resources.getString(R.string.phone_number_mask,
//                    callingCodes.first()),
//                affinityCalculationStrategy = AffinityCalculationStrategy.PREFIX
//            )
//            editText.setText("")
//            editText.hint = maskedTextChangedListener?.placeholder()
//        }
//
//        override fun currentMaskName(): String {
//            return countries.first { countryInfo ->
//                countryInfo.countryCode == currentCountryCode
//            }.englishName
//        }
//
//        override fun removeListeners() {
//            editText.get()?.removeTextChangedListener(maskedTextChangedListener)
//        }
//    }

    class PhoneNumberMask(
        private val resources: Resources,
        private val currentCountryCode: String,
        private val countries: List<Country>,
    ) : InputMask {
        override fun setMask(editText: EditText) {
            val callingCodes: List<String> = countries
                .filter { country ->
                    country.countryCode == currentCountryCode
                }.map {
                    it.callingCodes.first()
                }

            val code = "${resources.getString(R.string.phone_number_mask2, callingCodes.first())} "
            editText.hint = code
            editText.setText(code)
            editText.setSelection(code.length)
        }

        override fun currentMaskName(): String {
            return countries.first { countryInfo ->
                countryInfo.countryCode == currentCountryCode
            }.englishName
        }
    }
}