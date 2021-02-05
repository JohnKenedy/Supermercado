package com.canytech.supermercado.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.ProductFeature
import com.canytech.supermercado.utils.Constants
import com.canytech.supermercado.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_feature_product.*
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class AddFeatureProductActivity : BaseActivity(), View.OnClickListener {

    private var mFeatureSelectedImageFileURI: Uri? = null
    private var mFeatureProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_feature_product)
        setupActionBar()

        imageView_add_update_feature_product.setOnClickListener(this)
        btn_add_product_feature_submit.setOnClickListener(this)

        val spinnerCategory = listOf<String>(
            "Personal care",
            "Chocolate, biscuits & Snacks",
            "Fruits &amp; vegetables",
            "Breakfast, dairy &amp; cereals",
            "Baby care",
            "Grains, wheat & rice",
            "Beverages"
        )
        val arraySpinnerAdapter = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            spinnerCategory
        )
        spinner_category.adapter = arraySpinnerAdapter
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_feature_product_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white)
        }

        toolbar_add_feature_product_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.imageView_add_update_feature_product -> {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this@AddFeatureProductActivity)
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_add_product_feature_submit -> {
                    if (validateFeatureProductDetails()) {
                        uploadFeatureProductImage()
                    }
                }
            }
        }
    }

    private fun uploadFeatureProductImage() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().uploadImageToCloudStorage(
            this,
            mFeatureSelectedImageFileURI,
            Constants.PRODUCT_IMAGE
        )
    }

    fun productFeatureUploadSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@AddFeatureProductActivity,
            resources.getString(R.string.product_uploaded_success_mesage),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    fun imageFeatureUploadSuccess(imageURL: String) {
//        hideProgressDialog()
//        showErrorSnackBar("Product image is uploaded successfully. Image URL: $imageURL", false)

        mFeatureProductImageURL = imageURL

        uploadFeatureProductDetails()

    }

    private fun uploadFeatureProductDetails() {

        val username = this.getSharedPreferences(
            Constants.MYGROCERYSTORE_PREFERENCES, Context.MODE_PRIVATE
        )
            .getString(Constants.LOGGED_IN_USERNAME, "")!!

        val product = ProductFeature(
            FireStoreClass().getCurrentUserID(),
            username,
            edit_text_product_feature_title.text.toString().trim { it <= ' ' },
            edit_text_feature_product_price.text.toString().trim { it <= ' ' },
            edit_text_product_feature_old_price.text.toString().trim { it <= ' ' },
            edit_text_product_feature_description.text.toString().trim { it <= ' ' },
            edit_text_product_feature_quantity.text.toString().trim { it <= ' ' },
//            edit_text_product_feature_category.text.toString().trim { it <= ' ' },
            edit_text_product_feature_unit.text.toString().trim { it <= ' ' },

            mFeatureProductImageURL
        )

        FireStoreClass().uploadFeatureProductDetails(this, product)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //showErrorSnackBar("The permission is granted.", false)
                Constants.showImageChooser(this)
            } else {

                Toast.makeText(
                    this, resources.getString(R.string.storage_permissiob_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {

                    imageView_add_update_feature_product.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_baseline_edit_24
                        )
                    )

                    mFeatureSelectedImageFileURI = data.data!!

                    try {
                        GlideLoader(this).loadUserPicture(
                            mFeatureSelectedImageFileURI!!,
                            imageView_product_feature_image
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun validateFeatureProductDetails(): Boolean {
        return when {
            mFeatureSelectedImageFileURI == null -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_msg_select_product_image), true
                )
                false
            }

            TextUtils.isEmpty(
                edit_text_product_feature_title.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_product_title), true)
                false
            }

            TextUtils.isEmpty(
                edit_text_product_feature_old_price.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_msg_enter_product_old_price),
                    true
                )
                false
            }

            TextUtils.isEmpty(
                edit_text_feature_product_price.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_product_price), true)
                false
            }

            TextUtils.isEmpty(
                edit_text_product_feature_description.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_msg_enter_product_description),
                    true
                )
                false
            }

            TextUtils.isEmpty(
                edit_text_product_feature_quantity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.error_msg_enter_product_quantity),
                    true
                )
                false
            }
//            TextUtils.isEmpty(
//                edit_text_product_feature_category.text.toString().trim { it <= ' ' }) -> {
//                showErrorSnackBar(
//                    resources.getString(R.string.error_msg_enter_product_category),
//                    true
//                )
//                false
//
//            }
            else -> {
                true
            }
        }
    }
}