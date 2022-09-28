package com.saer.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.core.di.lazyViewModel
import com.saer.login.databinding.FragmentEnterPasswordBinding
import com.saer.login.di.LoginComponentViewModel

class EnterPasswordFragment : Fragment() {

    private val viewModel: EnterPasswordViewModel by lazyViewModel {
        ViewModelProvider(this).get<LoginComponentViewModel>()
            .loginComponent.enterPasswordViewModel().create()
    }

    private val binding: FragmentEnterPasswordBinding by viewBinding(CreateMethod.INFLATE)

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

        binding.applyButton.setOnClickListener {
            viewModel.enterPassword(binding.enterPasswordEditText.text.toString())
        }

        viewModel.observeEnterPasswordUiCommunication(viewLifecycleOwner) {
            it.apply(binding)
        }
    }

}