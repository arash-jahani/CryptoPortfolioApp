package com.arashjahani.cryptoportfolioapp.ui.transaction_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction
import com.arashjahani.cryptoportfolioapp.databinding.ItemTransactionBinding
import com.arashjahani.cryptoportfolioapp.utils.toCurrencyFormat

class TransactionAdapter(var items:ArrayList<AssetTransaction>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var clickListener:TransactionItemsClickListener

    fun setOnItemClickListener(clickListener: TransactionItemsClickListener){
        this.clickListener=clickListener
    }

    fun renewItems(newItems:List<AssetTransaction>){
        items.clear()
        items.addAll(newItems)

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding=ItemTransactionBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return TransactionItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewHolder=holder as TransactionItemHolder

        val transactionItem:AssetTransaction=items[position]

        viewHolder.binding.lblTransactionType.text=transactionItem.transaction_type
        viewHolder.binding.lblAssetAmount.text=transactionItem.amount.toCurrencyFormat()

        viewHolder.binding.lblAssetPrice.text=transactionItem.price.toCurrencyFormat()
        viewHolder.binding.lblTransactionWorth.text=transactionItem.getTransactionWorth().toCurrencyFormat()

        viewHolder.binding.imgDelete.setOnClickListener {
            clickListener.onSymbolDelete(transactionItem._id)
        }

    }

    class TransactionItemHolder(val binding:ItemTransactionBinding):RecyclerView.ViewHolder(binding.root){}
}