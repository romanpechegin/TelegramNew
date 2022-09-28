package com.saer.login.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.saer.core.entities.Country
import com.saer.core.listeners.CountryListener
import com.saer.login.databinding.ItemCountryBinding

class CountriesAdapter(
    private val listener: CountryListener
) : ListAdapter<Country, CountryViewHolder>(ItemCallback), View.OnClickListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCountryBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = getItem(position)

        holder.binding.apply {
            root.tag = country
            countryName.text = country.englishName
            countryCode.text = country.callingCodes.first()
        }
    }

    override fun onClick(v: View?) {
        val country = v?.tag as Country?
        if (country != null) listener.chooseCountry(country)
    }
}

object ItemCallback : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.englishName == newItem.englishName
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }
}