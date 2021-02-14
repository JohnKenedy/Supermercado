package com.canytech.supermercado.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.canytech.supermercado.R
import com.canytech.supermercado.models.Product
import com.canytech.supermercado.ui.adapters.MyTrendingListAdapter
import com.canytech.supermercado.ui.adapters.SearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment() {

//    private lateinit var dashboardViewModel: DashboardViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rv_search.layoutManager =
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_search.setHasFixedSize(true)
        val adapterSearch = context?.let { SearchAdapter(it, ArrayList<Product>()) }
        rv_search.adapter = adapterSearch

        sv_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = context?.let {
                    SearchAdapter(
                        it,
                        ArrayList()
                    )
                }
                adapter?.filter?.filter(newText)
                return false
            }
        })


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        return root
    }
}

