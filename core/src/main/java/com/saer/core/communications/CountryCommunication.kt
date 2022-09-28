package com.saer.core.communications

import com.saer.core.entities.Country

interface CountryCommunication : Communication<Country> {
    class Base : Communication.StateFlow<Country>(Country.emptyCountry()), CountryCommunication
}