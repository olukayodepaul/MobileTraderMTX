package com.mobile.mobiletradermtx.ui.attendant

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.databinding.LoadinAdapterBinding
import com.mobile.mobiletradermtx.dto.BasketLimitList


class BankAdapter(private var mItems: List<BasketLimitList>) :
    RecyclerView.Adapter<BankAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding = LoadinAdapterBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item)
    }

    override fun getItemCount() = mItems.size

    inner class ViewHolder(private val binding: LoadinAdapterBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BasketLimitList) {
            binding.sku.text = item.product_name!!.lowercase().replaceFirstChar{it.uppercase()}
            binding.qty.text = NumberFormat.getInstance().format(item.qty)
            binding.amount.text = NumberFormat.getInstance().format(item.qty!! - item.order_sold!!)
            binding.total.text = NumberFormat.getInstance().format((item.qty!! - item.order_sold!!)*item.price!!)
        }
    }
}
