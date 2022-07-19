package com.saer.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.base_classes.BaseFragment
import com.saer.core.Resources
import com.saer.login.R
import com.saer.login.databinding.FragmentRegistrationBinding
import com.saer.login.di.LoginComponentViewModel
import javax.inject.Inject

class RegistrationFragment : BaseFragment(R.layout.fragment_registration) {

    @Inject
    lateinit var viewModel: RegistrationViewModel

    @Inject
    lateinit var resources: Resources

    private val binding: FragmentRegistrationBinding by viewBinding(CreateMethod.INFLATE)

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

        binding.enterFirstNameEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.firstName = text.toString()
        }

        binding.enterLastNameEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.lastName = text.toString()
        }

        viewModel.observeRegistrationUi(viewLifecycleOwner) {
            it.apply(
                resources = resources,
                enterFirstNameEditText = binding.enterFirstNameEditText,
                enterLastNameEditText = binding.enterLastNameEditText
            )
        }

        binding.sendNameButton.setOnClickListener {
            viewModel.registerUser()
        }
    }
}