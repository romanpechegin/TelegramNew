package com.saer.telegramnew.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.telegramnew.R
import com.saer.telegramnew.appComponent
import com.saer.telegramnew.common.setPhoneNumberMask
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

        viewModel.observeCheckPhoneResult(viewLifecycleOwner) { result ->
            when (result) {
                is ErrorResult -> {
//                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_LONG).show()
                    binding.sendCodeButton.visibility = View.GONE
                    binding.enterPhoneNumberTitle.text =
                        requireContext().getString(R.string.enter_phone_number)
                }
                is PendingResult -> binding.sendCodeButton.visibility = View.GONE
                is SuccessResult -> {
                    binding.sendCodeButton.visibility = View.VISIBLE
                    binding.enterPhoneNumberTitle.text =
                        requireContext().getString(R.string.click_send_code)
                }
            }
        }

        viewModel.observeSendCodeResult(viewLifecycleOwner) { result ->
            when (result) {
                is ErrorResult ->
                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_LONG).show()
                is SuccessResult ->
                    findNavController()
                        .navigate(R.id.action_EnterPhoneNumberFragment_to_enterCodeFragment)
                is PendingResult -> {

                }
            }
        }

        setPhoneNumberMask(binding.inputPhoneNumber, requireContext())
        showKeyboard()

        binding.sendCodeButton.setOnClickListener {
            viewModel.sendCode()
        }
    }
}