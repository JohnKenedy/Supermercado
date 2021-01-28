package com.canytech.supermercado.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    }

    override fun onClick(v: View?) {
    }
}