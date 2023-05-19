package com.arashjahani.cryptoportfolioapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arashjahani.cryptoportfolioapp.data.entity.Symbol
import com.arashjahani.cryptoportfolioapp.databinding.ItemSearchBinding
import com.arashjahani.cryptoportfolioapp.utils.toCurrencyFormat
import com.bumptech.glide.Glide

class SearchItemsAdapter(var items:ArrayList<Symbol>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var clickListener:SearchedItemClickListener

    fun renewItems(it:List<Symbol>){
        items.clear()
        items.addAll(it)

        notifyDataSetChanged()
    }

    fun setItemClickListener(clickListener: SearchedItemClickListener){
        this.clickListener=clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding=ItemSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return SearchItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewHolder=holder as SearchItemHolder

        val symbol:Symbol = items[position]

        viewHolder.binding.lblSymbol.text=symbol.name
        viewHolder.binding.lblSymbolShort.text=symbol.shortName

        viewHolder.binding.lblLastPrice.text="$ ${symbol.lastPrice.toCurrencyFormat()}"

        Glide.with(viewHolder.binding.root.context).load(symbol.icon ?: "")
            .into(viewHolder.binding.imgLogo)

        viewHolder.binding.root.setOnClickListener {
            clickListener.onSymbolClick(symbol)
        }
    }

    class SearchItemHolder(val binding:ItemSearchBinding):RecyclerView.ViewHolder(binding.root){}

}