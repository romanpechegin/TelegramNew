package com.saer.telegramnew.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import com.saer.telegramnew.R
import com.saer.telegramnew.appComponent
import com.saer.telegramnew.databinding.FragmentEnterPhoneNumberBinding
import com.saer.telegramnew.model.*
import javax.inject.Inject

class EnterPhoneNumberFragment : Fragment() {

    @Inject
    lateinit var viewModel: EnterPhoneNumberFragmentViewModel

    private var _binding: FragmentEnterPhoneNumberBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputPhoneNumber.doOnTextChanged { text, _, _, _ ->
            viewModel.inputPhoneNumber(text.toString())
        }

        viewModel.observeResult(viewLifecycleOwner) { result ->
            binding.sendCodeButton.visibility = when (result) {
                is ErrorResult -> {
                    Toast.makeText(context, result.exception.message, Toast.LENGTH_LONG).show()
                    View.GONE
                }
                is PendingResult -> View.GONE
                is SuccessResult -> View.VISIBLE
                is UserActionSuccessResult -> {
                    binding.enterPhoneNumberTitle.text = requireContext().getString(result.message)
                    View.VISIBLE
                }
                is UserActionErrorResult -> {
                    binding.enterPhoneNumberTitle.text = requireContext().getString(result.message)
                    View.GONE
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}