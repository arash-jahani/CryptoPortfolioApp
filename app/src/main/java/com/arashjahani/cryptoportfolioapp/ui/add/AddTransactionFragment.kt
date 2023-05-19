package com.arashjahani.cryptoportfolioapp.ui.add

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arashjahani.cryptoportfolioapp.R
import com.arashjahani.cryptoportfolioapp.data.entity.AssetTransaction
import com.arashjahani.cryptoportfolioapp.data.entity.RequestStatus
import com.arashjahani.cryptoportfolioapp.data.entity.Symbol
import com.arashjahani.cryptoportfolioapp.databinding.FragmentAddTransactionBinding
import com.arashjahani.cryptoportfolioapp.utils.toCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AddTransactionFragment : Fragment() {

    private var _binding : FragmentAddTransactionBinding?=null
    private val binding get() = _binding!!

    private val mViewModel: AddTransactionViewModel by viewModels()

    var mSymbolId:String?=null
    var mSymbol:Symbol?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentAddTransactionBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareViews()
        initObservers()
        initListeners()
    }

    private fun prepareViews(){

        mSymbolId = arguments?.getString("SYMBOL_ID")

        lifecycleScope.launch {
            mViewModel.getSymbol(mSymbolId).collect{
                mSymbol=it

                binding.lblSymbol.text="${it.name} (${it.shortName})"

                binding.lblTotal.text="Total ${it.shortName}"

                binding.lblUnitPrice.text="1 ${it.shortName} = ${it.lastPrice?.toCurrencyFormat()}"

                binding.txtPrice.setText(it.lastPrice?.toCurrencyFormat())
            }
        }

    }

    private fun initObservers(){
        lifecycleScope.launch {
            mViewModel.status.collect{
                when(it){
                    RequestStatus.LOADING ->{
                        loadingView()
                    }
                    RequestStatus.SUCCESS ->{
                        successView()
                    }
                    else ->{
                        failedView()
                    }
                }
            }
        }
    }


    private fun initListeners(){
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSubmit.setOnClickListener {

            var total=binding.txtTotal.text.toString()

            var transactionType=if(binding.rdgTransactionType.checkedRadioButtonId== R.id.rdBuy) "buy" else "sell"

            var price = binding.txtPrice.text.toString()

            if(total.isEmpty()){
                total="0"
            }
            if(price.isEmpty()){
                price="0"
            }

            lifecycleScope.launch {
                mViewModel.saveTransaction(createTransaction(total,transactionType,price)).collect{
                    Log.v("saved item:","$it")
                }
            }

        }
    }

    private fun createTransaction(total:String,transactionType:String,price:String) : AssetTransaction{

        return AssetTransaction(
            0,
            mSymbol?.id ?: "",
            mSymbol?.name ?: "",
            mSymbol?.shortName ?: "",
            price.replace(",","").toDouble(),
            total.replace(",","").toFloat(),
            transactionType,
            Date().toString(),
            mSymbol?.icon ?: ""
        )

    }


    private fun loadingView(){
        binding.layoutContent.visibility=View.GONE
        binding.loading.visibility=View.VISIBLE
    }

    private fun successView(){
        binding.layoutContent.visibility=View.VISIBLE
        binding.loading.visibility=View.GONE
    }

    private fun failedView(){
        binding.layoutContent.visibility=View.GONE
        binding.loading.visibility=View.GONE

        Toast.makeText(requireContext(),"Request Faield!",Toast.LENGTH_SHORT).show()
    }
}