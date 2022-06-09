package com.mobile.mobiletradermtx.ui.attendant

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.databinding.LoadoutAdapterBinding
import com.mobile.mobiletradermtx.dto.BasketLimitList
import kotlin.reflect.KFunction1


class LoadOutAdapter(private var mItems: List<BasketLimitList>, private val isReturnFunction: KFunction1<BasketLimitList, Unit>) :
    RecyclerView.Adapter<LoadOutAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding = LoadoutAdapterBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item, isReturnFunction)
    }

    override fun getItemCount() = mItems.size

    inner class ViewHolder(private val binding: LoadoutAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BasketLimitList, isClickRefresh: KFunction1<BasketLimitList, Unit>) {

            binding.sku.text = item.product_name!!.lowercase().replaceFirstChar{it.uppercase()}
            binding.amount.text = NumberFormat.getInstance().format(item.qty)

            binding.total.setOnClickListener {
                isClickRefresh(item)
            }
        }
    }
    
}