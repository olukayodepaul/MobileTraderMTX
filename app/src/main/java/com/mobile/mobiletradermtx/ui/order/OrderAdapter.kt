package com.mobile.mobiletradermtx.ui.order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.databinding.OrderAdapterBinding
import com.mobile.mobiletradermtx.dto.AllCustomerProductOrder
import com.mobile.mobiletradermtx.util.CircularImage
import kotlin.reflect.KFunction2


class OrderAdapter(
    private var mItems: List<AllCustomerProductOrder>, private val context: Context,
    private val isReturnFunction: KFunction2<AllCustomerProductOrder, OrderAdapterBinding, Unit>
) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding =  OrderAdapterBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item, isReturnFunction)
    }

    override fun getItemCount() = mItems.size

    inner class ViewHolder(private val binding: OrderAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AllCustomerProductOrder, clickListener: KFunction2<AllCustomerProductOrder, OrderAdapterBinding, Unit>) {

            binding.modulecontents.text = item.outletname
            binding.remark.text = ("URNO: ${item.urno}")
            binding.timeago.text = "${item.dates} ${item.trantime}"
            binding.transType.text = "Cash Payment"
            binding.idcheck.text = CircularImage.getNameInitials(item.outletname)
            binding.idcheck.background = CircularImage.getNameInitialsBg(context)

            if(item.trantype.lowercase()=="true") {
                binding.transType.text = "Paid"
            }

            binding.iconsImages.setOnClickListener {
                clickListener(item, binding)
            }
        }
    }
}