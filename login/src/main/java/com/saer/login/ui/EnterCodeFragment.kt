package com.saer.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.base_classes.BaseFragment
import com.saer.core.Resources
import com.saer.login.R
import com.saer.login.databinding.FragmentEnterCodeBinding
import javax.inject.Inject

class EnterCodeFragment : BaseFragment(R.layout.fragment_enter_code) {

    @Inject
    lateinit var viewModel: EnterCodeViewModel

    @Inject
    lateinit var resources: Resources

    private val binding: FragmentEnterCodeBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.enterCodeEditText.doOnTextChanged { code, _, _, _ ->
            viewModel.enterCode(code = code.toString())
        }

        viewModel.observeEnterCodeUi(lifecycleScope) {
            it.apply(
                resources = resources,
                binding = binding,
                viewModel = viewModel
            )
        }
    }
}