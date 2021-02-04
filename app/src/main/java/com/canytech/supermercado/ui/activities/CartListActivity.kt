package com.canytech.supermercado.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.CartItem
import com.canytech.supermercado.models.ProductFeature
import com.canytech.supermercado.models.ProductTrending
import com.canytech.supermercado.ui.adapters.CartItemsListAdapter
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : BaseActivity() {

    private lateinit var mProductTrendingList: ArrayList<ProductTrending>
    private lateinit var mProductFeatureList: ArrayList<ProductFeature>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        if (cartList.size > 0) {
            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            btn_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)
            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, cartList)
            rv_cart_items_list.adapter = cartListAdapter

            var subTotal: Double = 0.0
            for (item in cartList) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                subTotal += (price * quantity)
            }
            tv_sub_total.text = "$$subTotal"
            tv_shipping_charge.text = "S10.0"   // TODO - change Logic shipping charge
            if (subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE
                btn_checkout.visibility = View.VISIBLE
                val total =
                    subTotal + 10                           // TODO - change Logic shipping charge
                tv_total_amount.text = "$$total"
            } else {
                ll_checkout.visibility = View.GONE
                btn_checkout.visibility = View.GONE
            }
        } else {
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            btn_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    fun successTrendingProductsListFromFireStore(trendingList: ArrayList<ProductTrending>) {
        mProductTrendingList = trendingList

        getCartItemsList()
    }

    fun successFeatureProductsListFromFireStore(featureList: ArrayList<ProductFeature>) {
        mProductFeatureList = featureList

        getCartItemsList()
    }

    private fun getProductsTrendingList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAllTrendingProductsList(this)
    }

    private fun getProductsFeatureList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAllFeatureProductsList(this)
    }

    private fun getCartItemsList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCartList(this@CartListActivity)
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductsTrendingList()
        getProductsFeatureList()
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white)
        }

        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }
}