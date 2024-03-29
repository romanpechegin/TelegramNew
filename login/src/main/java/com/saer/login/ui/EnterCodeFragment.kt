package com.saer.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.base_classes.BaseFragment
import com.saer.core.di.lazyViewModel
import com.saer.core.utils.hideKeyboard
import com.saer.core.utils.showKeyboard
import com.saer.login.databinding.FragmentEnterCodeBinding
import com.saer.login.di.LoginComponentViewModel

class EnterCodeFragment : BaseFragment() {

    private val viewModel: EnterCodeViewModel by lazyViewModel {
        ViewModelProvider(this).get<LoginComponentViewModel>()
            .loginComponent.enterCodeViewModel().create()
    }

    private val binding: FragmentEnterCodeBinding by viewBinding(CreateMethod.INFLATE)

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

        binding.sendCodeButton.setOnClickListener {
//            viewModel.sendCode(
//                arguments?.let { EnterCodeFragmentArgs.fromBundle(it).phoneNumber } ?: ""
//            )
        }

        binding.enterCodeEditText.doAfterTextChanged { code ->
            viewModel.enterCode(code = code.toString())
        }

        viewModel.observeEnterCodeUi(viewLifecycleOwner) { enterCodeUi ->
            enterCodeUi.apply(
                resources = resources,
                binding = binding,
                viewModel = viewModel
            )
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        showKeyboard(binding.enterCodeEditText)
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