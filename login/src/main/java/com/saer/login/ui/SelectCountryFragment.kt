package com.saer.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.saer.base_classes.BaseFragment
import com.saer.core.di.lazyViewModel
import com.saer.login.R
import com.saer.login.databinding.FragmentSelectCountryBinding
import com.saer.login.di.LoginComponentViewModel
import com.saer.login.ui.adapters.CountriesAdapter

class SelectCountryFragment : BaseFragment(R.layout.fragment_select_country) {

    private val viewModel: SelectCountryViewModel by lazyViewModel {
        ViewModelProvider(this).get<LoginComponentViewModel>()
            .loginComponent.selectCountryViewModel().create()
    }

    private val binding: FragmentSelectCountryBinding by viewBinding(CreateMethod.INFLATE)

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
        val adapter = CountriesAdapter(viewModel)
        binding.countriesRecyclerView.adapter = adapter

        viewModel.observeCountries(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.observeChosenCountry(viewLifecycleOwner) {
            if (it.callingCodes.isNotEmpty()) {
                setFragmentResult(COUNTRY_CODE_REQUEST_KEY,
                    bundleOf(Pair(COUNTRY_CODE_REQUEST_KEY, it.countryCode)))
                findNavController().popBackStack()
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbar.addMenuProvider(
            SearchMenuProvider(viewModel),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    companion object {
        const val COUNTRY_CODE_REQUEST_KEY = "country_code_request_key"
    }
}