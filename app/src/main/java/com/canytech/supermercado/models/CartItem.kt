package com.canytech.supermercado.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem (
    val user_id: String = "",
    var product_id: String = "",
    val title: String = "",
    val price: String = "",
    val image: String = "",
    val stock_quantity: String = "",
    val cart_quantity: String = "",
    val id: String = ""
): Parcelable