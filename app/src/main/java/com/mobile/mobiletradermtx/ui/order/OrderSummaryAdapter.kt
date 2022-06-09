package com.mobile.mobiletradermtx.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.databinding.OrderSummaryAdapterBinding
import com.mobile.mobiletradermtx.dto.AllSkuOrdered
import java.text.DecimalFormat


class OrderSummaryAdapter(

    private var mItems: List<AllSkuOrdered>
) :
    RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding =  OrderSummaryAdapterBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item)
    }

    override fun getItemCount() = mItems.size

    inner class ViewHolder(private val binding: OrderSummaryAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AllSkuOrdered) {
            val df = DecimalFormat("#.#")
            binding.skuTv.text = item.skuname.trim()
            binding.qtyTv.text = item.qtyordered.toString().trim()
            binding.priceTv.text =  String.format("%,.1f", (df.format(item.amount*item.qtyordered!!).toDouble())).trim()
        }
    }
}