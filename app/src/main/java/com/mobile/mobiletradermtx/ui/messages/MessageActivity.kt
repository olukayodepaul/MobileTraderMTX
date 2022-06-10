package com.mobile.mobiletradermtx.ui.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobile.mobiletradermtx.databinding.ActivityMessageBinding
import com.mobile.mobiletradermtx.databinding.BottomItemSheetBinding
import com.mobile.mobiletradermtx.databinding.MessageAdapterBinding
import com.mobile.mobiletradermtx.databinding.MessageBottomSheetBinding
import com.mobile.mobiletradermtx.dto.EntityAccuracy
import com.mobile.mobiletradermtx.util.GeoFencing.currentDate
import com.mobile.mobiletradermtx.util.NetworkResult
import com.mobile.mobiletradermtx.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first


@AndroidEntryPoint
class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding

    private lateinit var adapter: MessageAdapter

    private val viewModel: MessageViewModel by viewModels()

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        navBack()
        initAdapter()
        initViewModels()
        isMessageAccuracyStateFlow()
    }

    private fun navBack() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclers.layoutManager = layoutManager
        binding.recyclers.setHasFixedSize(true)
    }

    private fun initViewModels() = lifecycleScope.launchWhenResumed {
        viewModel.isMessageAccuracy(sessionManager.fetchDynamicCustomerNo.first(),currentDate!!)
    }

    private fun isMessageAccuracyStateFlow() =  lifecycleScope.launchWhenResumed {
        viewModel.messageResponseState.collect {
            it.let {
                when(it) {
                    is NetworkResult.Empty -> {
                    }
                    is NetworkResult.Error -> {
                    }
                    is NetworkResult.Loading -> {
                    }
                    is NetworkResult.Success -> {
                        binding.progressbarHolder.isVisible = false
                        adapter = MessageAdapter(it.data!!,::handleAdapterEvent)
                        adapter.notifyDataSetChanged()
                        binding.recyclers.setItemViewCacheSize(it.data.size)
                        binding.recyclers.adapter = adapter
                    }
                }
            }
        }
    }

     private fun handleAdapterEvent(item: EntityAccuracy, view: MessageAdapterBinding) {
         viewModel.isMessageUpdateAccuracy(2, item._id!!)
         view.custIcons.isVisible = true
         view.iconsImages.isVisible = false
         val inflater = LayoutInflater.from(applicationContext)
         val bindings =  MessageBottomSheetBinding.inflate(inflater)
         val dialog = BottomSheetDialog(this)
         dialog.setContentView(bindings.root)
         bindings.messageBody.text = item.remark!!.lowercase()
         dialog.show()
    }

}