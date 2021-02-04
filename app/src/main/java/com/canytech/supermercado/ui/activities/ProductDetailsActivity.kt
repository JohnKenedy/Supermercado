package com.canytech.supermercado.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.CartItem
import com.canytech.supermercado.models.ProductTrending
import com.canytech.supermercado.utils.Constants
import com.canytech.supermercado.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_feature_product.*
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: String = ""
    private lateinit var mProductDetails: ProductTrending

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product id", mProductId)
        }

        getProductDetails()
        getFeatureProductsDetails()

        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getProductsDetails(this, mProductId)
    }

    private fun getFeatureProductsDetails() {
        FireStoreClass().getFeatureProductsDetails(this, mProductId)
    }

    fun productExistsInCart() {
        hideProgressDialog()
        icon_add_to_cart.visibility = View.GONE
        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE
    }

    fun productDetailsSuccess(product: ProductTrending) {
        mProductDetails = product
        hideProgressDialog()
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image, iv_product_detail_image
        )

        item_title_product.text = product.title
        item_old_price_product.text = product.old_price
        tv_cart_item_price.text = product.price
        item_in_out_stock_quantity.text = product.stock_quantity
        tv_cart_item_unit.text = product.unit
        til_product_description.text = product.description

        FireStoreClass().checkIfItemExistInCart(this, mProductId)
    }

    private fun setupActionBar() {

        setSupportActionBar(top)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white)
        }

        top.setNavigationOnClickListener { onBackPressed() }
    }

    private fun addToCart() {
        val cartItem = CartItem(
            FireStoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            mProductDetails.stock_quantity,
            mProductDetails.unit,
            Constants.DEFAULT_CART_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addCartItems(this, cartItem)
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.success_item_added_to_cart), Toast.LENGTH_SHORT
        ).show()

        btn_add_to_cart.visibility = View.GONE
        icon_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE

    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.btn_add_to_cart -> {
                    addToCart()
                }
                R.id.btn_go_to_cart -> {
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                }
            }
        }
    }
}