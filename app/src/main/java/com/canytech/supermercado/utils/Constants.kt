package com.canytech.supermercado.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val USERS: String = "users"
    const val ORDERS: String = "orders"
    const val PRODUCTS: String = "products"
    const val FEATURES: String = "features"
    const val CATEGORIES: String ="category"

    const val MYGROCERYSTORE_PREFERENCES: String = "MyGroceryStorePrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val NAME: String = "name"
    const val MOBILE: String = "mobile"
    const val ADDRESS: String = "address"
    const val GENDER: String = "gender"
    const val IMAGE: String = "image"
    const val COMPLETE_PROFILE: String = "profileCompleted"

    const val USER_ID: String = "user_id"

    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val PRODUCT_IMAGE: String = "Product_Image"

    const val EXTRA_PRODUCT_ID: String = "extra_product_id"

    const val DEFAULT_CART_QUANTITY: String = "1"

    const val CART_ITEMS: String = "cart_items"

    const val PRODUCT_ID: String = "product_id"

    const val CART_QUANTITY: String = "cart_quantity"

    const val HOME: String = "Home"
    const val OFFICE: String = "Office"
    const val OTHER: String = "Other"

    const val ADDRESSES: String = "addresses"

    const val EXTRA_ADDRESS_DETAILS: String = "AddressDetails"
    const val EXTRA_SELECT_ADDRESS: String = "extra_select_address"
    const val ADD_ADDRESS_REQUEST_CODE: Int = 121
    const val EXTRA_SELECTED_ADDRESS: String = "extra_selected_address"



    fun showImageChooser(activity: Activity) {

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {

        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}