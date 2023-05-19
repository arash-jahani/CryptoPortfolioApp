package com.arashjahani.cryptoportfolioapp.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.arashjahani.cryptoportfolioapp.R
import com.arashjahani.cryptoportfolioapp.data.entity.RequestStatus
import com.arashjahani.cryptoportfolioapp.data.entity.Symbol
import com.arashjahani.cryptoportfolioapp.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchedItemClickListener {

    private var _binding:FragmentSearchBinding?=null
    private val binding get()=_binding!!

    private val mViewModel:SearchViewModel by viewModels()

    private var mSearchItemsAdapter:SearchItemsAdapter?=null
    private var edittextDelayTimer:Handler?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareViews()
        initObservers()
        initListeners()
    }

    private fun prepareViews(){

        mSearchItemsAdapter = SearchItemsAdapter(ArrayList())
        mSearchItemsAdapter?.setItemClickListener(this)

        val divider=DividerItemDecoration(
            binding.rcvSymbols.context,
            DividerItemDecoration.VERTICAL
        )
        binding.rcvSymbols.addItemDecoration(divider)

        binding.rcvSymbols.adapter=mSearchItemsAdapter

        mViewModel.fetchTopCoins()

    }

    private fun initObservers(){

        lifecycleScope.launch {
            mViewModel.symbolsList.collect{
                mSearchItemsAdapter?.renewItems(it)
            }
        }

        lifecycleScope.launch {
            mViewModel.status.collect{
                when(it){
                    RequestStatus.LOADING->{
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

        binding.txtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId ==EditorInfo.IME_ACTION_SEARCH){
                callSearch()
                return@OnEditorActionListener true
            }
            false
        })

        //enable search by typing edittext, the search will be triggered with a delay after typing is finished.
        binding.txtSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(edittextDelayTimer!=null){
                    edittextDelayTimer?.removeCallbacksAndMessages(null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                edittextDelayTimer= Handler(Looper.getMainLooper())
                edittextDelayTimer?.postDelayed(object:Runnable{
                    override fun run() {
                        callSearch()
                    }
                },600)
            }

        })
    }

    private fun callSearch(){
        val searchValue=binding.txtSearch.text.toString().trim()

        if(searchValue.length>=2){
            mViewModel.searchByKeywords(searchValue)
        }else{
            mViewModel.fetchTopCoins()
        }
    }

    override fun onSymbolClick(symbol: Symbol) {

        var bundle= Bundle()
        bundle.putString("SYMBOL_ID",symbol.id)

        findNavController().navigate(R.id.action_searchFragment_to_addTransactionFragment,bundle)

    }

    private fun loadingView(){
        binding.rcvSymbols.visibility=View.GONE
        binding.loading.visibility=View.VISIBLE
    }

    private fun successView(){
        binding.rcvSymbols.visibility=View.VISIBLE
        binding.loading.visibility=View.GONE
    }

    private fun failedView(){
        binding.rcvSymbols.visibility=View.GONE
        binding.loading.visibility=View.GONE

        Toast.makeText(requireContext(),"Request Failed!",Toast.LENGTH_SHORT).show()
    }

}