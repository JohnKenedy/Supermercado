package com.canytech.supermercado.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canytech.supermercado.R
import com.canytech.supermercado.models.User
import com.canytech.supermercado.utils.Constants
import kotlinx.android.synthetic.main.activity_user_profile.*

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
                        showErrorSnackBar("You already have the storage permission.", false)
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
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

                showErrorSnackBar("The permission is granted.", false)
            } else {
                Toast.makeText(
                    this, resources.getString(R.string.storage_permissiob_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}