package com.canytech.supermercado.ui.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.Address
import com.canytech.supermercado.utils.Constants
import kotlinx.android.synthetic.main.activity_add_edit_address.*

class AddEditAddressActivity : BaseActivity() {

    private var mAddressDetails: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)

        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)) {
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)
        }

        setupActionBar()


        if (mAddressDetails != null) {
            if (mAddressDetails!!.id.isNotEmpty()) {
                textView_add_edit_address_title.text = resources.getString(R.string.title_edit_address)
                btn_submit_address.text = resources.getString(R.string.btn_lbl_update)

                edit_text_full_name.setText(mAddressDetails?.name)
                edit_text_phone_number.setText(mAddressDetails?.mobileNumber)
                edit_text_address.setText(mAddressDetails?.address)
                edit_text_zip_code.setText(mAddressDetails?.zipCode)
                edit_text_additional_notes.setText(mAddressDetails?.additionalNote)

                when (mAddressDetails?.type) {
                    Constants.HOME -> {
                        radio_btn_home.isChecked = true
                    }
                    Constants.OFFICE -> {
                        radio_btn_office.isChecked = true
                    }
                    else -> {
                        radio_btn_other.isChecked = true
                        til_other_details.visibility = View.VISIBLE
                        edit_text_other_details.setText(mAddressDetails?.otherDetails)
                    }
                }
            }
        }

        rg_type.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radio_btn_other) {
                til_other_details.visibility = View.VISIBLE
            } else {
                til_other_details.visibility = View.GONE
            }
        }
        btn_submit_address.setOnClickListener {
            saveAddressToFirestore()
        }
    }


    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_edit_address_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white)
        }

        toolbar_add_edit_address_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun saveAddressToFirestore() {
        val fullName: String = edit_text_full_name.text.toString().trim { it <= ' ' }
        val phoneNumber: String = edit_text_phone_number.text.toString().trim { it <= ' ' }
        val address: String = edit_text_address.text.toString().trim { it <= ' ' }
        val zipCode: String = edit_text_zip_code.text.toString().trim { it <= ' ' }
        val additionalNote: String = edit_text_additional_notes.text.toString().trim { it <= ' ' }
        val otherDetails: String = edit_text_other_details.text.toString().trim { it <= ' ' }

        if (validateData()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val addressType: String = when {
                radio_btn_home.isChecked -> {
                    Constants.HOME
                }
                radio_btn_office.isChecked -> {
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }

            val addressModel = Address(
                FireStoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote,
                addressType,
                otherDetails
            )

            if (mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()) {
                FireStoreClass().updateAddress(this, addressModel, mAddressDetails!!.id)
            } else {
                FireStoreClass().addAddress(this, addressModel)
            }
        }
    }

    fun addUpdateAddressSuccess() {
        hideProgressDialog()
        val notifySuccessMessage: String =
            if (mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()) {
                resources.getString(R.string.your_address_update_successfully)
            } else {
                resources.getString(R.string.error_your_address_added_successfully)
            }

        Toast.makeText(
            this@AddEditAddressActivity, notifySuccessMessage,
            Toast.LENGTH_SHORT
        ).show()

        setResult(RESULT_OK)
        finish()

    }

    private fun validateData(): Boolean {
        return when {

            TextUtils.isEmpty(edit_text_full_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_msg_enter_full_name), true
                )
                false
            }

            TextUtils.isEmpty(edit_text_phone_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_msg_enter_phone_number), true
                )
                false
            }

            TextUtils.isEmpty(edit_text_address.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_msg_enter_address), true
                )
                false
            }

            TextUtils.isEmpty(edit_text_zip_code.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_msg_enter_zip_code), true
                )
                false
            }

            radio_btn_other.isChecked && TextUtils.isEmpty(
                edit_text_zip_code.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_msg_enter_zip_code), true
                )
                false
            }
            else -> {
                true
            }
        }
    }
}

