package com.canytech.supermercado.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.canytech.supermercado.R
import com.canytech.supermercado.models.Product
import com.canytech.supermercado.ui.activities.ProductDetailsActivity
import com.canytech.supermercado.utils.Constants
import com.canytech.supermercado.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(
    private val context: Context,
    private var productList: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var productFilterList = ArrayList<Product>()

    init {
        productFilterList = productList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = productList[position]


        if (holder is MyTrendingListAdapter.MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.item_img_product)
            holder.itemView.item_title_product.text = model.title
            holder.itemView.item_old_price_product.text = model.old_price
            holder.itemView.tv_cart_item_price.text = model.price
            holder.itemView.tv_cart_item_unit.text = model.unit
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    productFilterList = productList
                } else {
                    val resultList = ArrayList<Product>()
                    for (row in productList) {
                        if (row.toString().contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    productFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = productFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                productFilterList = results?.values as ArrayList<Product>
                notifyDataSetChanged()
            }
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}