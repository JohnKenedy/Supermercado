package com.canytech.supermercado.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canytech.supermercado.R
import com.canytech.supermercado.models.ProductTrending
import com.canytech.supermercado.ui.activities.ProductDetailActivity
import com.canytech.supermercado.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

open class MyTrendingListAdapter(
    private val context: Context,
    private var list: ArrayList<ProductTrending>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(
            R.layout.item_list_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.item_img_product)
            holder.itemView.item_title_product.text = model.title
            holder.itemView.item_old_price_product.text = model.old_price
            holder.itemView.item_price_product.text = model.price
            holder.itemView.textView_item_unit.text = model.unit
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}


