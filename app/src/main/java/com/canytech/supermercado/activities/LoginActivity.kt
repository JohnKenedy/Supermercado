package com.canytech.supermercado.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.canytech.supermercado.R
import com.canytech.supermercado.firestore.FireStoreClass
import com.canytech.supermercado.models.User
import com.canytech.supermercado.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        text_view_login_forgot_password.setOnClickListener(this)
        btn_login_sing_in.setOnClickListener(this)
        btn_login_sing_google.setOnClickListener(this)
        text_view_login_sing_up.setOnClickListener(this)

    }

    fun userLoggedInSuccess(user: User) {

        hideProgressDialog()

        if (user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.text_view_login_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login_sing_in -> {
                    loginRegisteredUser()
                }

                R.id.btn_login_sing_google -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Functionality not implemented",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                R.id.text_view_login_sing_up -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(edit_text_login_email.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(edit_text_login_password.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_password), true)
                false
            }

            else -> {
                true
            }
        }
    }

    private fun loginRegisteredUser() {
        if (validateLoginDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email = edit_text_login_email.text.toString().trim { it <= ' ' }
            val password = edit_text_login_password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        FireStoreClass().getUserDetails(this@LoginActivity)

                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
}