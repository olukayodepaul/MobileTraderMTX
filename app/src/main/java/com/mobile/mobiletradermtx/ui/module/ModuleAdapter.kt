package com.mobile.mobiletradermtx.ui.module

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.databinding.UserModulesAdapterBinding
import com.mobile.mobiletradermtx.dto.UserModules
import com.mobile.mobiletradermtx.ui.messages.MessageActivity
import com.mobile.mobiletradermtx.ui.order.ReOrderActivity
import com.mobile.mobiletradermtx.ui.sales.SalesActivity
import com.mobile.mobiletradermtx.util.CircularImage


class ModuleAdapter(private var mItems: List<UserModules>, private val context: Context) :
    RecyclerView.Adapter<ModuleAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding = UserModulesAdapterBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item)
    }



    override fun getItemCount() = mItems.size

    inner class ViewHolder(private val binding: UserModulesAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserModules) {

            binding.modulecontents.text = item.name!!.lowercase().replaceFirstChar{it.uppercase()}
            binding.remark.text = item.imageurl!!.lowercase().replaceFirstChar{it.uppercase()}
            binding.idchecks.text = CircularImage.getNameInitials(item.name!!)
            binding.idchecks.background = CircularImage.getNameInitialsBg(context)

            binding.parentModules.setOnClickListener {
                when (item.navigationid) {
                    1 -> {
                        val intent = Intent(context, SalesActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                    3 -> {
                        val intent = Intent(context, MessageActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                    6 -> {
                        val intent = Intent(context, ReOrderActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}