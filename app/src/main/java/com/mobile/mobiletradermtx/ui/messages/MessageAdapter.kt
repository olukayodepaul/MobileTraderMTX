package com.mobile.mobiletradermtx.ui.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.databinding.MessageAdapterBinding
import com.mobile.mobiletradermtx.dto.EntityAccuracy


class MessageAdapter(private var mItems: List<EntityAccuracy>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding = MessageAdapterBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item)
    }

    override fun getItemCount() = mItems.size

    inner class ViewHolder(private val binding: MessageAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EntityAccuracy) {

            binding.timeago.text = item.entry_date

            if(item.status==2) {
                binding.custIcons.isVisible = true
                binding.iconsImages.isVisible = false
            }else{
                binding.custIcons.isVisible = false
                binding.iconsImages.isVisible = true
            }

            binding.modulecontents.text = item.remark
        }
    }

}