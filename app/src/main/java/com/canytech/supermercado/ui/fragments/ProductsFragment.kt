package com.canytech.supermercado.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.ProductCategories
import com.canytech.supermercado.models.Product
import com.canytech.supermercado.ui.adapters.MyCategoriesListAdapter
import com.canytech.supermercado.ui.adapters.MyTrendingListAdapter
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {

//    private lateinit var homeViewModel: HomeViewModel

    fun successTrendingProductsListFromFireStore(trendingList: ArrayList<Product>) {
        hideProgressDialog()

        rv_trending.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_trending.setHasFixedSize(true)
        val adapterProducts = MyTrendingListAdapter(requireActivity(), trendingList)
        rv_trending.adapter = adapterProducts
    }

    fun successFeatureProductsListFromFireStore(featureList: ArrayList<Product>) {
        hideProgressDialog()

        rv_feature.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_feature.setHasFixedSize(true)
        val adapterProducts = MyTrendingListAdapter(requireActivity(), featureList)
        rv_feature.adapter = adapterProducts
    }

    private fun getTrendingProductsListFromFireStore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getTrendingProductsList(this)
    }

    private fun getFeatureProductsListFromFireStore() {
        FireStoreClass().getFeatureProductsList(this)
    }

    private fun getCategoriesListFromFireStore() {
        FireStoreClass().getCategoriesList(this)
    }


    fun successCategoriesListFromFireStore(categoriesList: ArrayList<ProductCategories>) {
        hideProgressDialog()

        rv_categories.layoutManager =
            GridLayoutManager(context, 3)
        rv_categories.setHasFixedSize(true)
        val adapterCategories = MyCategoriesListAdapter(requireActivity(), categoriesList)
        rv_categories.adapter = adapterCategories
    }

    override fun onResume() {
        super.onResume()
        getCategoriesListFromFireStore()
        getTrendingProductsListFromFireStore()
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