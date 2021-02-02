package com.canytech.supermercado.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.canytech.supermercado.models.ProductFeature
import com.canytech.supermercado.models.ProductTrending
import com.canytech.supermercado.models.User
import com.canytech.supermercado.ui.activities.*
import com.canytech.supermercado.ui.fragments.ProductsFragment
import com.canytech.supermercado.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        // Here the collection name from which we wants the data
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MYGROCERYSTORE_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(

                    Constants.LOGGED_IN_USERNAME,
                    user.name
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }

            .addOnFailureListener { e ->

                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(activity, imageFileURI)
        )

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            //Image upload is success
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            //Get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when (activity) {
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddTrendingProductActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddFeatureProductActivity -> {
                            activity.imageFeatureUploadSuccess(uri.toString())
                        }

                    }
                }
        }

            .addOnFailureListener { exception ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddTrendingProductActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddFeatureProductActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                // If have some error, It's printed in Log
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadTrendingProductDetails(activity: AddTrendingProductActivity, productTrendingInfo: ProductTrending) {
        mFireStore.collection(Constants.PRODUCTS)
            .document()
            .set(productTrendingInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }

    fun getTrendingProductsList(fragment: Fragment) {
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<ProductTrending> = ArrayList()
                for (i in document.documents) {

                    val product = i.toObject(ProductTrending::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                when(fragment) {
                    is ProductsFragment -> {
                        fragment.successProductsListFromFireStore(productsList)
                    }
                }
            }
    }

    fun uploadFeatureProductDetails(activity: AddFeatureProductActivity, productFeatureInfo: ProductFeature) {
        mFireStore.collection(Constants.FEATURES)
            .document()
            .set(productFeatureInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productFeatureUploadSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }

    fun getFeatureProductsList(fragment: Fragment) {
        mFireStore.collection(Constants.FEATURES)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products Feature List", document.documents.toString())
                val productsFeatureList: ArrayList<ProductFeature> = ArrayList()
                for (i in document.documents) {

                    val productFeature = i.toObject(ProductFeature::class.java)
                    productFeature!!.product_id = i.id

                    productsFeatureList.add(productFeature)
                }

                when(fragment) {
                    is ProductsFragment -> {
                        fragment.successFeatureProductsListFromFireStore(productsFeatureList)
                    }
                }
            }
    }

}