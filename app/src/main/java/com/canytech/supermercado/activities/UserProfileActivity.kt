package com.canytech.supermercado.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.User
import com.canytech.supermercado.utils.Constants
import com.canytech.supermercado.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User

    private var mSelectedImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {

            // Get the user details fom intent as a ParcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        text_view_user_profile_name.text = mUserDetails.name
        text_view_user_profile_email.text = mUserDetails.email
        text_view_user_profile_number.text = mUserDetails.mobile.toString()
        text_view_user_profile_address.text = mUserDetails.address

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

                //Update info Profile (ADDRESS and MOBILE NUMBER)
                R.id.btn_user_profile_submit -> {

                    showProgressDialog(resources.getString(R.string.please_wait))

                    FireStoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)

                    /*
                    if (validateUserProfileDetails()) {

                        val userHashMap = HashMap<String, Any>()

                        val mobileNumber =
                            edit_text_register_number.text.toString().trim { it <= ' ' }

                        val userAddress =
                            edit_text_register_address.text.toString().trim { it <= ' ' }

                        val gender = if (radio_btn_user_profile_male.isChecked) {
                            Constants.MALE
                        } else {
                            Constants.FEMALE
                        }

                        //create key and value to Firebase, EX: key: gender value: male,
                        // key: userAddress value: Address typed
                        if (mobileNumber.isNotEmpty() || userAddress.isNotEmpty()) {
                            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
                            userHashMap[Constants.ADDRESS] = userAddress
                        }
                        userHashMap[Constants.GENDER] = gender

                        showProgressDialog(resources.getString(R.string.please_wait))

                        FireStoreClass().updateUserProfileData(this, userHashMap)

                        //showErrorSnackBar("Your details are valid. You can update them.", false)
                    } */
                }
            }
        }
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.profile_uptade_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()

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
                        //URI of selected image from phone storage
                        mSelectedImageFileUri = data.data!!

                        //Profile Image
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, profile_image)
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

    fun imageUploadSuccess(imageURL: String) {
        hideProgressDialog()
        Toast.makeText(
            this@UserProfileActivity,
            "Your image is uploaded seccessfully. Image URL is $imageURL",
            Toast.LENGTH_SHORT
        ).show()
    }

}