package com.saer.core.entities

data class Country(
    val countryCode: String,
    val name: String,
    val englishName: String,
    val isHidden: Boolean,
    val callingCodes: List<String>,
) {
    companion object {
        fun emptyCountry() = Country("", "", "", false, listOf())
    }
}