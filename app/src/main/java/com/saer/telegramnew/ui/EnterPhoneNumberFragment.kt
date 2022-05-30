package com.saer.telegramnew.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import com.saer.telegramnew.R
import com.saer.telegramnew.appComponent
import com.saer.telegramnew.databinding.FragmentEnterPhoneNumberBinding
import com.saer.telegramnew.model.*
import com.saer.telegramnew.utils.showKeyboard
import javax.inject.Inject


class EnterPhoneNumberFragment : BaseFragment(R.layout.fragment_enter_phone_number) {

    @Inject
    lateinit var viewModel: EnterPhoneNumberFragmentViewModel

    private val binding: FragmentEnterPhoneNumberBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputPhoneNumber.doOnTextChanged { text, _, _, _ ->
            viewModel.inputPhoneNumber(text.toString())
        }

        viewModel.observeResult(viewLifecycleOwner) { result ->
             when (result) {
                is ErrorResult -> {
                    Toast.makeText(context, result.exception.message, Toast.LENGTH_LONG).show()
                    binding.sendCodeButton.visibility = View.GONE
                }
                is PendingResult -> binding.sendCodeButton.visibility = View.GONE
                is SuccessResult -> binding.sendCodeButton.visibility = View.VISIBLE
                is UserActionSuccessResult -> {
                    binding.enterPhoneNumberTitle.text = requireContext().getString(result.message)
                    binding.sendCodeButton.visibility = View.VISIBLE
                }
                is UserActionErrorResult -> {
                    binding.enterPhoneNumberTitle.text = requireContext().getString(result.message)
                    binding.sendCodeButton.visibility = View.GONE
                }
            }
        }

        val formats = ArrayList<String>()
        formats.add(requireContext().getString(R.string.phone_number_musk1))
        val maskedTextChangedListener = MaskedTextChangedListener.installOn(
            binding.inputPhoneNumber,
            requireContext().getString(R.string.phone_number_musk2),
            formats,
            AffinityCalculationStrategy.PREFIX
        )
        binding.inputPhoneNumber.hint = maskedTextChangedListener.placeholder()
        showKeyboard()
    }
}