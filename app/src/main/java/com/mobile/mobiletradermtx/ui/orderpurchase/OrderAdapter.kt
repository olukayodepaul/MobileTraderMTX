package com.mobile.mobiletradermtx.ui.orderpurchase


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.databinding.ItemRowParentBinding
import com.mobile.mobiletradermtx.dto.Orders
import com.mobile.mobiletradermtx.util.CircularImage


open class OrderAdapter(private var context: Context) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    var mItems: List<Orders> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding =   ItemRowParentBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item, p1)
    }

    override fun getItemCount() = mItems.size

    inner class ViewHolder(val binding: ItemRowParentBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Orders, p1: Int) {

            binding.modulecontents.text = item.etime

            if(item.remark!!.isNotEmpty()) {
                binding.idcheck.text = CircularImage.getNameInitials(item.remark!!)
                binding.idcheck.background = CircularImage.getNameInitialsBg(context)
            }

            if(item.order!!.isNotEmpty()) {
                val itemListAdapter = OrderItemAdapter(item.order!!)
                val layoutManager = LinearLayoutManager(itemView.context)
                binding.recyclerItems.layoutManager = layoutManager
                binding.recyclerItems.adapter = itemListAdapter
            }

            val isExpandable: Boolean = item.expandable!!

            binding.hostExpandable.isVisible = isExpandable

            if(isExpandable){
                binding.parentModules.setBackgroundColor(Color.parseColor("#CCCCCC"));
            }else{
                binding.parentModules.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            val limitToSalesEntry = item.order!!.filter {totals->
                totals.seperatorname.equals("own brands")
            }

            val isPricing = limitToSalesEntry.sumOf { pricing->
                pricing.pricing!!.toDouble()
            }

            val isInventory = limitToSalesEntry.sumOf { inventory->
                inventory.inventory!!.toDouble()
            }

            val isOrder = limitToSalesEntry.sumOf { order->
                order.orders!!.toDouble()
            }

            binding.qtys.text =  NumberFormat.getInstance().format(isPricing)
            binding.amounts.text = NumberFormat.getInstance().format(isInventory)
            binding.totals.text = NumberFormat.getInstance().format(isOrder)

            binding.parentModules.setOnClickListener {
                item.expandable = !item.expandable!!
                notifyItemChanged(p1)
            }
        }
     }

    fun addData(list: List<Orders>) {
        mItems = list
        notifyDataSetChanged()
    }

}