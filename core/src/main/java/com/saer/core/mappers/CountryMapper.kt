package com.saer.core.mappers

import com.saer.core.entities.Country
import org.drinkless.td.libcore.telegram.TdApi.Countries

class CountryMapper : Mapper<Countries, List<Country>> {
    override fun map(data: Countries): List<Country> {
        return data.mapToCountries()
    }
}

fun Countries.mapToCountries(): List<Country> {
    val countries = ArrayList<Country>()
    this.countries.forEach {
        countries.add(Country(it.countryCode, it.name, it.englishName, it.isHidden, it.callingCodes.toList()))
    }
    return countries
}