package com.hermanowicz.currencyapp.screens.currency

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hermanowicz.currencyapp.R
import com.hermanowicz.currencyapp.models.Currency

class CurrencyRecyclerViewAdapter(val context: Context, list: MutableList<Currency>): RecyclerView.Adapter<CurrencyRecyclerViewAdapter.CurrencyFragViewHolder>() {

    var itemList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyFragViewHolder {
        return CurrencyFragViewHolder(LayoutInflater.from(context).inflate(R.layout.landing_list_view_item,parent,false))
    }

    fun updateCurrencyList(updatedList: MutableList<Currency>){
        itemList.clear()
        itemList = updatedList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CurrencyFragViewHolder, position: Int) {
        val model : Currency = itemList[position]
        (model.countryName + " (" + model.code + ")").also { holder.countryName.text = it }
        (model.rate.toString() + " " + model.symbol).also { holder.rate.text = it }
    }

    class CurrencyFragViewHolder(item: View): RecyclerView.ViewHolder(item){
        val countryName : TextView = item.findViewById(R.id.country)
        val rate : TextView = item.findViewById(R.id.currencyRate)
    }
}