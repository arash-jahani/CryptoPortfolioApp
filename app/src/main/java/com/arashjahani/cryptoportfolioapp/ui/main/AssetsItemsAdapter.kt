package com.arashjahani.cryptoportfolioapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction
import com.arashjahani.cryptoportfolioapp.databinding.ItemAssetBinding
import com.arashjahani.cryptoportfolioapp.databinding.ItemAssetPreviewBinding
import com.arashjahani.cryptoportfolioapp.utils.toCurrencyFormat
import com.bumptech.glide.Glide

class AssetsItemsAdapter(var items:ArrayList<AssetTransaction>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var clickListener: AssetItemsClickListener

    fun setOnItemClickListener(clickListener: AssetItemsClickListener){
        this.clickListener=clickListener
    }

    fun renewItems(newItems:List<AssetTransaction>){
        items.clear()
        items.addAll(newItems)

        //preview item
        items.add(AssetTransaction())

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if(position==(items.size-1)){
            return 0
        }else{
            return 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==0){
            val binding=ItemAssetPreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)

            return AssetPreviewItemHolder(binding)
        }else{
            val binding=ItemAssetBinding.inflate(LayoutInflater.from(parent.context),parent,false)

            return AssetItemHolder(binding)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position)==0){

            val viewHolder = holder as AssetPreviewItemHolder

            viewHolder.binding.root.setOnClickListener {
                clickListener.addNewTransactionClick()
            }

            viewHolder.binding.btnAddTransaction.setOnClickListener {
                clickListener.addNewTransactionClick()
            }

        }else{

            val viewHolder=holder as AssetItemHolder

            val assetItem:AssetTransaction=items[position]

            viewHolder.binding.lblAsset.text=assetItem.symbol

            viewHolder.binding.lblAssetAmount.text=assetItem.getCurrentAmount().toCurrencyFormat()

            viewHolder.binding.lblAssetLastPrice.text="$${assetItem.lastPrice.toCurrencyFormat()}"
            viewHolder.binding.lblAssetLastPriceChangePercent.text="${assetItem.lastPriceChange.toCurrencyFormat()}%"

            viewHolder.binding.lblAssetValue.text="$${assetItem.getCurrentWorth().toCurrencyFormat()}"
            viewHolder.binding.lblAssetValueProfitPercent.text="${assetItem.getProfitPercent().toCurrencyFormat()}%"

            Glide.with(viewHolder.binding.root.context).load(assetItem.icon ?: "")
                .into(viewHolder.binding.imgAssetLogo)

            viewHolder.binding.root.setOnClickListener {
                clickListener.onAssetClick(assetItem)
            }
        }
    }

    class AssetItemHolder(val binding: ItemAssetBinding):RecyclerView.ViewHolder(binding.root){}
    class AssetPreviewItemHolder(val binding: ItemAssetPreviewBinding):RecyclerView.ViewHolder(binding.root){}
}