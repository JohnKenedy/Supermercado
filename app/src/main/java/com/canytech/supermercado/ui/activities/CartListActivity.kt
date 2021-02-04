package com.canytech.supermercado.ui.activities

import android.os.Bundle
import android.util.Log
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.CartItem
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        for (i in cartList) {
            Log.i("Cart item title", i.title)
        }
    }

    private fun getCartItemsList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCartList(this@CartListActivity)
    }

    override fun onResume() {
        super.onResume()
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