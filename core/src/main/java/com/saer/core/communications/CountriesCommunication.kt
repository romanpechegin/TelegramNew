package com.saer.core.communications

import com.saer.core.entities.Country

interface CountriesCommunication : Communication<List<Country>> {
    class Base : Communication.StateFlow<List<Country>>(mutableListOf()), CountriesCommunication
}