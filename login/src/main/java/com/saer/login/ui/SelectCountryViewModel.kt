package com.saer.login.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saer.core.communications.CountriesCommunication
import com.saer.core.communications.CountryCommunication
import com.saer.core.di.IoDispatcher
import com.saer.core.entities.Country
import com.saer.core.listeners.CountryListener
import com.saer.core.mappers.mapToCountries
import com.saer.login.repositories.AuthRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class SelectCountryViewModel @AssistedInject constructor(
    private val authRepository: AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val countriesCommunication: CountriesCommunication,
    private val countryCommunication: CountryCommunication,
) : ViewModel(), CountryListener, SearchQueryListener {
    private var countries: List<Country> = emptyList()

    init {
        viewModelScope.launch(ioDispatcher) {
            countries = authRepository.countries().mapToCountries()
            countriesCommunication.map(countries)
        }
    }

    fun observeCountries(
        lifecycleOwner: LifecycleOwner,
        collector: (value: List<Country>) -> Unit,
    ) {
        countriesCommunication.observe(lifecycleOwner, collector)
    }

    fun observeChosenCountry(
        lifecycleOwner: LifecycleOwner,
        collector: (value: Country) -> Unit,
    ) {
        countryCommunication.observe(lifecycleOwner, collector)
    }

    override fun chooseCountry(country: Country) {
        viewModelScope.launch(ioDispatcher) {
            countryCommunication.map(country)
        }
    }

    override fun onSearchQueryChanged(query: String) {
        viewModelScope.launch(ioDispatcher) {
            countriesCommunication.map(
                countries.filter { it.englishName.contains(query, ignoreCase = true) }
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): SelectCountryViewModel
    }
}