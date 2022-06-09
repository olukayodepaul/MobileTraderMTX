package com.mobile.mobiletradermtx.ui.salesrecord

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.databinding.SalesrecodAdapterBinding
import com.mobile.mobiletradermtx.dto.BasketLimitList


class SalesRecordAdapter(private var mItems: List<BasketLimitList>) :
    RecyclerView.Adapter<SalesRecordAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding =  SalesrecodAdapterBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item)
    }

    override fun getItemCount() = mItems.size

    inner class ViewHolder(private val binding: SalesrecodAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BasketLimitList) {
            binding.tvsku.text = item.product_name!!.lowercase().replaceFirstChar{it.uppercase()}
            binding.tvinventory.text = NumberFormat.getInstance().format(item.inventory)
            binding.tvpricing.text = NumberFormat.getInstance().format(item.pricing)
            binding.tvqtysold.text = NumberFormat.getInstance().format(item.orders)
            binding.tvamount.text = NumberFormat.getInstance().format(item.price!! * item.orders!!)
            }
        }
}
