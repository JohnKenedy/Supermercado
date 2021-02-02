package com.canytech.supermercado.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.ProductFeature
import com.canytech.supermercado.models.ProductTrending
import com.canytech.supermercado.ui.adapters.MyFeatureListAdapter
import com.canytech.supermercado.ui.adapters.MyTrendingListAdapter
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {

//    private lateinit var homeViewModel: HomeViewModel

    fun successProductsListFromFireStore(productsList: ArrayList<ProductTrending>) {
        hideProgressDialog()

        rv_trending.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_trending.setHasFixedSize(true)
        val adapterProducts = MyTrendingListAdapter(requireActivity(), productsList)
        rv_trending.adapter = adapterProducts
    }

    private fun getProductsListFromFireStore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getTrendingProductsList(this)

    }

    private fun getFeatureProductsListFromFireStore() {
        FireStoreClass().getFeatureProductsList(this)

    }

    fun successFeatureProductsListFromFireStore(productsFeatureList: ArrayList<ProductFeature>) {
        hideProgressDialog()

        rv_feature.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_feature.setHasFixedSize(true)
        val adapterFeatureProducts = MyFeatureListAdapter(requireActivity(), productsFeatureList)
        rv_feature.adapter = adapterFeatureProducts
    }

    override fun onResume() {
        super.onResume()
        getProductsListFromFireStore()
        getFeatureProductsListFromFireStore()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_products, container, false)

        return root
    }
}