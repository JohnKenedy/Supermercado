package com.canytech.supermercado.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCategories(
    val category_title: String = "",
    val img_category: String = "",
    var product_id: String = ""
    ): Parcelable