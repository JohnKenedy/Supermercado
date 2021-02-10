package com.canytech.supermercado.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canytech.supermercado.R
import com.canytech.supermercado.models.ProductCategories
import com.canytech.supermercado.utils.GlideLoader
import kotlinx.android.synthetic.main.item_categories.view.*

open class MyCategoriesListAdapter(
    private val context: Context,
    private var list: ArrayList<ProductCategories>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(
            R.layout.item_categories, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.img_category, holder.itemView.img_category)
            holder.itemView.title_category.text = model.category_title
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}


