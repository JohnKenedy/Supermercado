package com.canytech.supermercado.ui.activities

import android.os.Bundle
import android.provider.SyncStateContract
import android.text.TextUtils
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.Address
import com.canytech.supermercado.utils.Constants
import kotlinx.android.synthetic.main.activity_add_edit_address.*
import kotlinx.android.synthetic.main.activity_address_list.*

class AddEditAddressActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)
        setupActionBar()
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

        }
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

