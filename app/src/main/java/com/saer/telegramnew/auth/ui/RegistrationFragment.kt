package com.saer.telegramnew.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.telegramnew.R
import com.saer.telegramnew.appComponent
import com.saer.telegramnew.base.BaseFragment
import com.saer.telegramnew.databinding.FragmentRegistrationBinding
import javax.inject.Inject

class RegistrationFragment : BaseFragment(R.layout.fragment_registration) {

    @Inject
    lateinit var viewModel: RegistrationFragmentViewModel

    private val binding: FragmentRegistrationBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
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
                requireContext(),
                binding.enterFirstNameEditText,
                binding.enterLastNameEditText
            )
        }

        binding.sendNameButton.setOnClickListener {
            viewModel.registerUser()
        }
    }
}