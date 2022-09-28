package com.saer.core.listeners

import com.saer.core.entities.Country

interface CountryListener {
    fun chooseCountry(country: Country)
}