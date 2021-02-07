package com.canytech.supermercado.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.Address
import com.canytech.supermercado.ui.adapters.AddressListAdapter
import com.canytech.supermercado.utils.SwipeToDeleteCallback
import com.canytech.supermercado.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_settings.*

class AddressListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setupActionBar()
        btn_add_address.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getAddressList()
    }

    private fun getAddressList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAddressesList(this)
    }

    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {
        hideProgressDialog()
        if (addressList.size > 0) {
            rv_add_address_list.visibility = View.VISIBLE
            tv_no_address_found.visibility = View.GONE

            rv_add_address_list.layoutManager = LinearLayoutManager(this)
            rv_add_address_list.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this, addressList)
            rv_add_address_list.adapter = addressAdapter

            val editSwipeHandler = object : SwipeToEditCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = rv_add_address_list.adapter as AddressListAdapter
                    adapter.notifyEditItem(this@AddressListActivity, viewHolder.adapterPosition)
                }
            }

            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(rv_add_address_list)

            val deleteSwiperHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    showProgressDialog(resources.getString(R.string.please_wait))

                    FireStoreClass().deleteAddress(this@AddressListActivity, addressList[viewHolder.adapterPosition].id)
                }
            }

            val deleteItemTouchHelper = ItemTouchHelper(deleteSwiperHandler)
            deleteItemTouchHelper.attachToRecyclerView(rv_add_address_list)

        } else {
            rv_add_address_list.visibility = View.GONE
            tv_no_address_found.visibility = View.VISIBLE
        }
    }

    fun deleteAddressSuccess() {
        hideProgressDialog()
        Toast.makeText(this, resources.getString(R.string.your_address_deleted_successfully),
        Toast.LENGTH_SHORT).show()
        getAddressList()
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_address_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white)
        }

        toolbar_add_address_activity.setNavigationOnClickListener { onBackPressed() }
    }

}