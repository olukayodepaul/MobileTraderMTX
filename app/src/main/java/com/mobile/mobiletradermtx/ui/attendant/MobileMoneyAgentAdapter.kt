package com.mobile.mobiletradermtx.ui.attendant


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.mobiletradermtx.databinding.MoneyAgentBinding
import com.mobile.mobiletradermtx.dto.IsMoneyAgent
import com.mobile.mobiletradermtx.util.StartGoogleMap
import kotlin.reflect.KFunction1


class MobileMoneyAgentAdapter (private var iSAgent: List<IsMoneyAgent>, private var context: Context,
                               private val isReturnFunction: KFunction1<IsMoneyAgent, Unit>
                               ): RecyclerView.Adapter<MobileMoneyAgentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflaters = LayoutInflater.from(parent.context)
        val binding =   MoneyAgentBinding.inflate(layoutInflaters)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = iSAgent[p1]
        p0.bind(item, isReturnFunction)
    }

    override fun getItemCount() = iSAgent.size

    inner class ViewHolder(private val binding: MoneyAgentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IsMoneyAgent, clickListener: KFunction1<IsMoneyAgent,  Unit>) {

            //val letter: String = item.agennama!!.substring(0, 1).uppercase()
            //val generator = ColorGenerator.MATERIAL
            //val drawable = TextDrawable.builder().buildRound(letter, generator.randomColor)
            //binding.IdCheck.setImageDrawable(drawable)
            binding.modulecontents.text = item.agennama!!.lowercase().replaceFirstChar{it.uppercase()}
            binding.transType.text = item.phone
            binding.remark.text = item.address!!.lowercase().replaceFirstChar{it.uppercase()}

            binding.iconsImages.setOnClickListener {
                val dmode = "d".single()
                val destination = "${item.lat},${item.lng}"
                StartGoogleMap.startGoogleMapIntent(context, destination, dmode, 't')
            }

            binding.content.setOnClickListener {
                clickListener(item)
            }

        }
    }
}