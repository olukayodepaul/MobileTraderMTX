package com.mobile.mobiletradermtx.ui.salesentry

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.databinding.SalesEntryAdapterBinding
import com.mobile.mobiletradermtx.dto.BasketLimitList
import kotlin.reflect.KFunction2


class SalesEntryAdapter(private var mItems: List<BasketLimitList>, private val context: Context,
                        private val clickListeners: KFunction2<BasketLimitList, SalesEntryAdapterBinding, Unit>
) :
    RecyclerView.Adapter<SalesEntryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding = SalesEntryAdapterBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item, clickListeners)
    }

    override fun getItemCount() = mItems.size

    inner class ViewHolder(private val binding: SalesEntryAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BasketLimitList , clickListener: KFunction2<BasketLimitList,SalesEntryAdapterBinding, Unit>) {

            binding.tvSkus.text = item.product_name!!.lowercase().replaceFirstChar{it.uppercase()}

            if(item.seperator=="3") {
                binding.perentHost.setBackgroundColor(Color.parseColor("#f1f5f8"))
            }


            binding.mtPricing.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        clickListener(item,binding)
                    }
                }
            )

            binding.mtOrder.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        clickListener(item,binding)
                    }
                }
            )

            binding.mtInventory.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        clickListener(item,binding)
                    }
                }
            )
        }
    }
}