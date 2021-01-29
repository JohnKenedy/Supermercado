package com.canytech.supermercado.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canytech.supermercado.R
import com.canytech.supermercado.models.User
import com.canytech.supermercado.utils.Constants
import com.canytech.supermercado.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        var userDetails: User = User()
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {

            // Get the user details fom intent as a ParcelableExtra
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        text_view_user_profile_name.text = userDetails.name
        text_view_user_profile_email.text = userDetails.email
        text_view_user_profile_number.text = userDetails.mobile.toString()
        text_view_user_profile_address.text = userDetails.address

        profile_image.setOnClickListener(this@UserProfileActivity)
        btn_user_profile_submit.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                R.id.profile_image -> {

                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_user_profile_submit -> {
                    if (validateUserProfileDetails()) {
                        showErrorSnackBar("Your details are valid. You can update them.", false)
                    }
                }
            }
        }
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
                    try {
                        val selectedImageFileUri = data.data!!

                        GlideLoader(this).loadUserPicture(selectedImageFileUri, profile_image)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(edit_text_register_number.text.toString().trim { it <= ' ' })
                    || TextUtils.isEmpty(
                edit_text_register_address.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_complete_all_fields), true)
                false
            }
            else -> {
                true
            }
        }
    }
}