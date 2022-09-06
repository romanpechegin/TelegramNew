package com.saer.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.api.BuildConfig
import com.saer.base_classes.BaseFragment
import com.saer.core.Resources
import com.saer.core.common.setPhoneNumberMask
import com.saer.core.utils.hideKeyboard
import com.saer.core.utils.showKeyboard
import com.saer.login.R
import com.saer.login.databinding.FragmentEnterPhoneNumberBinding
import com.saer.login.di.LoginComponentViewModel
import dagger.Lazy
import javax.inject.Inject

class EnterPhoneNumberFragment : BaseFragment(R.layout.fragment_enter_phone_number) {

    @Inject
    internal lateinit var viewModelFactory: Lazy<EnterPhoneNumberViewModel.Factory>

    private val viewModel: EnterPhoneNumberViewModel by viewModels {
        viewModelFactory.get()
    }

    @Inject
    lateinit var resources: Resources

    private val binding: FragmentEnterPhoneNumberBinding by viewBinding(CreateMethod.INFLATE)

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<LoginComponentViewModel>()
            .loginComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

        context?.let { setPhoneNumberMask(binding.inputPhoneNumber, it) }

        binding.sendCodeButton.setOnClickListener {
            viewModel.checkPhoneNumber()
        }

        if (BuildConfig.DEBUG) binding.inputPhoneNumber.setText("9892634770")
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