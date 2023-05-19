package com.arashjahani.cryptoportfolioapp.ui.transaction_detail

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.arashjahani.cryptoportfolioapp.R
import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction
import com.arashjahani.cryptoportfolioapp.databinding.FragmentDetailBinding
import com.arashjahani.cryptoportfolioapp.utils.toCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(), TransactionItemsClickListener {

    private var _binding:FragmentDetailBinding?=null
    private val binding get()=_binding!!

    private val mViewModel:DetailViewModel by viewModels()

    private var mAsset:AssetTransaction?=null
    private lateinit var mAdapter:TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.TIRAMISU){
            mAsset=arguments?.getParcelable("ASSET",AssetTransaction::class.java)
        }else{
            mAsset=arguments?.getParcelable("ASSET")
        }

        prepareViews()
        initObservers()
        initListeners()
    }

    private fun prepareViews(){

        mAdapter= TransactionAdapter(ArrayList())
        mAdapter.setOnItemClickListener(this)

        val divider=DividerItemDecoration(binding.rcvTransactions.context,DividerItemDecoration.VERTICAL)
        binding.rcvTransactions.addItemDecoration(divider)

        binding.rcvTransactions.adapter=mAdapter
    }

    private fun initObservers(){

        lifecycleScope.launch {
            mViewModel.getAsset(mAsset?.symbolId ?: "",mAsset?.lastPrice ?: 0.0).collect{

                binding.lblTotalAsset.text="Total ${it.symbol}"
                binding.lblTotalAssetValue.text=it.getCurrentAmount().toCurrencyFormat()

                binding.lblTotalWorthValue.text=it.getCurrentWorth().toCurrencyFormat()

                binding.lblTotalBuyValue.text=it.totalBuyAmount.toCurrencyFormat()
                binding.lblTotalBuyWorthValue.text=it.totalBuyPrice.toCurrencyFormat()
                binding.lblAvgBuyPriceValue.text=it.getAvgBuy().toCurrencyFormat()

                binding.lblTotalSellValue.text=it.totalSellAmount.toCurrencyFormat()
                binding.lblTotalSellWorthValue.text=it.totalSellPrice.toCurrencyFormat()
                binding.lblAvgSellPriceValue.text=it.getAvgSell().toCurrencyFormat()
            }
        }

        lifecycleScope.launch {
            mViewModel.getAssetTransaction(mAsset?.symbolId ?: "").collect{
                mAdapter?.renewItems(it)
            }
        }

    }

    private fun initListeners(){

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnAddTransaction.setOnClickListener {
            mAsset?.symbolId?.let {
                var bundle = Bundle()
                bundle.putString("SYMBOL_ID",it)
                findNavController().navigate(R.id.action_detailFragment_to_addTransactionFragment,bundle)
            }
        }

    }

    override fun onSymbolDelete(_id: Int) {
       mViewModel.deleteTransaction(_id)
    }
}