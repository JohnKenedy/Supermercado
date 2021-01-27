package com.canytech.supermercado

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register_sing_up.setOnClickListener {
            validateRegisterDetails()
        }

    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(edit_text_register_name.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_name), true)
                false
            }

            TextUtils.isEmpty(edit_text_register_number.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_number), true)
                false
            }

            TextUtils.isEmpty(edit_text_register_email.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_email), true)
                false
            }
            else -> {
                showErrorSnackBar(resources.getString(R.string.register_completed), false)
                true
            }
        }
    }
}