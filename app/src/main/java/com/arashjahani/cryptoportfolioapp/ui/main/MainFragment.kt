package com.arashjahani.cryptoportfolioapp.ui.main

import android.content.Intent
import android.net.Uri
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
import com.arashjahani.cryptoportfolioapp.databinding.FragmentMainBinding
import com.arashjahani.cryptoportfolioapp.utils.toCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(), AssetItemsClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mViewModel: MainViewModel by viewModels()

    private lateinit var mAdapter: AssetsItemsAdapter
    private var mAssetsList = ArrayList<AssetTransaction>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareViews()
        initObservers()
        initListeners()
    }

    private fun prepareViews() {

        //preview
        if (mAssetsList.isEmpty())
            mAssetsList.add(AssetTransaction())

        mAdapter = AssetsItemsAdapter(mAssetsList)
        mAdapter.setOnItemClickListener(this)

        val dividerItemDecoration =
            DividerItemDecoration(binding.rcvAssets.context, DividerItemDecoration.VERTICAL)
        binding.rcvAssets.addItemDecoration(dividerItemDecoration)

        binding.rcvAssets.adapter = mAdapter

    }

    private fun initListeners() {
        binding.lblAbout.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://www.youtube.com/@howtocode-01/about")
            startActivity(i)
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            mViewModel.getAssets().collect{
                mAdapter?.renewItems(it)
            }
        }

        lifecycleScope.launch {
            mViewModel.totalValue.collect{
                binding.lblBalanceValue.text="$${it.toCurrencyFormat()}"
            }
        }

        lifecycleScope.launch {
            mViewModel.totalValueProgitPercent.collect{
                binding.lblBalanceProfitPercent.text="${it.toCurrencyFormat()}%"
            }
        }
    }

    override fun onAssetClick(asset: AssetTransaction) {
        var bundle=Bundle()
        bundle.putParcelable("ASSET",asset)

        findNavController().navigate(R.id.action_mainFragment_to_detailFragment,bundle)
    }

    override fun addNewTransactionClick() {
        findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
    }

}
