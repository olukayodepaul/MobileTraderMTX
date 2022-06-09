package com.mobile.mobiletradermtx.ui.sales

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.dto.CustomersList
import kotlin.reflect.KFunction2
import com.mobile.mobiletradermtx.R
import com.mobile.mobiletradermtx.databinding.SalesAdapterBinding
import com.mobile.mobiletradermtx.dto.toCustomers
import com.mobile.mobiletradermtx.ui.attendant.BankActivity
import com.mobile.mobiletradermtx.ui.attendant.LoadInActivity
import com.mobile.mobiletradermtx.ui.attendant.LoadOutActivity

class SalesAdapter(private var mItems: List<CustomersList>, private val context: Context,
                   private val isReturnFunction: KFunction2<CustomersList, Int,  Unit>) :
    RecyclerView.Adapter<SalesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding = SalesAdapterBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item, isReturnFunction)
    }

    override fun getItemCount() = mItems.size

    inner class ViewHolder(private val binding: SalesAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CustomersList, clickListener: KFunction2<CustomersList, Int,  Unit>
        ) {

            //val letter: String = item.outletname!!.substring(0, 1).uppercase()
            //val generator = ColorGenerator.MATERIAL
            //val drawable = TextDrawable.builder().buildRound(letter, generator.randomColor)
            //binding.idCheck.setImageDrawable(drawable)
            binding.modulecontents.text = item.outletname!!.lowercase().replaceFirstChar{it.uppercase()}
            binding.remark.text = ("URNO: ${item.urno}, VCL: ${item.volumeclass}")
            binding.timeago.text = item.timeago

            if(item.sort==1) {
                binding.iconsImages.isVisible = false
                binding.modulecontents.text = item.notice
            }

            if(item.sort==2) {
                binding.custIcons.isVisible = false
            }

            if(item.sort==3) {
                binding.iconsImages.isVisible = false
                binding.modulecontents.text = item.notice
            }

            if(item.sort==4) {
                binding.iconsImages.isVisible = false
                binding.modulecontents.text = item.notice
            }

            binding.parentModules.setOnClickListener {
                when(item.sort){
                    1->{
                        val intent = Intent(context, LoadOutActivity::class.java)
                        intent.putExtra("customers",item.toCustomers())
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                    3->{
                        val intent = Intent(context, BankActivity::class.java)
                        intent.putExtra("customers",item.toCustomers())
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                    4->{
                        val intent = Intent(context, LoadInActivity::class.java)
                        intent.putExtra("customers",item.toCustomers())
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                }
            }

            binding.iconsImages.setOnClickListener {
                showPopup(binding, item, clickListener)
            }
        }

        private fun showPopup(
            binding: SalesAdapterBinding,
            item: CustomersList,
            clickItems: KFunction2<CustomersList, Int, Unit>
        ){
            val popupMenu = PopupMenu(context, binding.iconsImages)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.floatingcontextmenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.googleLocations -> {
                        clickItems(item, 1)
                    }
                    R.id.outletClose -> {
                        clickItems(item, 2)
                    }
                    R.id.outletOpen -> {
                        clickItems(item, 3)
                    }
                    R.id.outletUpdate -> {
                        clickItems(item, 4)
                    }
                    R.id.async -> {
                        clickItems(item, 5)
                    }
                    R.id.salesRecord -> {
                        clickItems(item, 6)
                    }
                }
                true
            }
            popupMenu.show()
        }
    }
}