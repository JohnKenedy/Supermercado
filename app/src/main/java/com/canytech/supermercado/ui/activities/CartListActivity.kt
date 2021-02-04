package com.canytech.supermercado.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
    private lateinit var mCartListItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        for (productTrending in mProductTrendingList) {
            for (cartItem in cartList) {
                if (productTrending.product_id == cartItem.product_id) {
                    cartItem.stock_quantity = productTrending.stock_quantity

                    if (productTrending.stock_quantity.toInt() == 0) {
                        cartItem.cart_quantity = productTrending.stock_quantity
                    }
                }
            }
        }

        for (productFeature in mProductFeatureList) {
            for (cartItem in cartList) {
                if (productFeature.product_id == cartItem.product_id) {
                    cartItem.stock_quantity = productFeature.stock_quantity

                    if (productFeature.stock_quantity.toInt() == 0) {
                        cartItem.cart_quantity = productFeature.stock_quantity
                    }
                }
            }
        }

        mCartListItems = cartList

        if (mCartListItems.size > 0) {
            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            btn_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)
            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, cartList)
            rv_cart_items_list.adapter = cartListAdapter

            var subTotal: Double = 0.0
            for (item in mCartListItems) {

                val availableQuantity = item.stock_quantity.toInt()
                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)
                }
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
        hideProgressDialog()
        mProductTrendingList = trendingList

        getCartItemsList()
    }

    fun successFeatureProductsListFromFireStore(featureList: ArrayList<ProductFeature>) {
        hideProgressDialog()
        mProductFeatureList = featureList

        getCartItemsList()
    }

    private fun getProductsTrendingList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAllTrendingProductsList(this)
    }

    private fun getProductsFeatureList() {
        FireStoreClass().getAllFeatureProductsList(this)
    }

    private fun getCartItemsList() {
//      showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCartList(this@CartListActivity)
    }

    fun itemUpdateSuccess() {
        hideProgressDialog()
        getCartItemsList()
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductsTrendingList()
        getProductsFeatureList()
    }

    fun itemRemovedSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@CartListActivity, resources.getString(R.string.item_removed_successfully),
            Toast.LENGTH_SHORT).show()

        getCartItemsList()
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