package com.saer.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.base_classes.BaseFragment
import com.saer.core.di.lazyViewModel
import com.saer.core.utils.hideKeyboard
import com.saer.core.utils.showKeyboard
import com.saer.login.R
import com.saer.login.databinding.FragmentEnterPhoneNumberBinding
import com.saer.login.di.LoginComponentViewModel

class EnterPhoneNumberFragment : BaseFragment() {

    private val viewModel: EnterPhoneNumberViewModel by lazyViewModel {
        ViewModelProvider(this).get<LoginComponentViewModel>()
            .loginComponent.enterPhoneNumberViewModel().create()
    }

    private val binding: FragmentEnterPhoneNumberBinding by viewBinding(CreateMethod.INFLATE)

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<LoginComponentViewModel>()
            .loginComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputPhoneNumber.doAfterTextChanged { text ->
            viewModel.enterPhoneNumber(phoneNumber = text.toString())
        }

        viewModel.observeEnterPhoneUi(viewLifecycleOwner) { enterPhoneUi ->
            enterPhoneUi.apply(
                binding = binding,
                resources = resources,
            )
        }

        viewModel.observeCountryCommunication(viewLifecycleOwner) { inputMask ->
            binding.confirmCountryCode.setText(inputMask.currentMaskName())
            inputMask.setMask(binding.inputPhoneNumber)
        }

        binding.sendCodeButton.setOnClickListener {
            viewModel.checkPhoneNumber()
        }

        binding.confirmCountryCode.setOnClickListener {
            findNavController().navigate(R.id.action_enterPhoneNumberFragment_to_selectCountryFragment)
        }

        setFragmentResultListener(SelectCountryFragment.COUNTRY_CODE_REQUEST_KEY) { _, bundle ->
            val countryCode = bundle.get(SelectCountryFragment.COUNTRY_CODE_REQUEST_KEY) as String?
            if (countryCode != null) viewModel.chosenCountry(countryCode)
        }
    }

    override fun onResume() {
        super.onResume()
        showKeyboard(binding.inputPhoneNumber)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }
}