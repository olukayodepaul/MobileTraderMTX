package com.mobile.mobiletradermtx.ui.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.mobiletradermtx.databinding.ActivityMessageBinding
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
                        Log.d("EPOKHAI 10","")
                    }
                    is NetworkResult.Error -> {
                        Log.d("EPOKHAI 11","")
                    }
                    is NetworkResult.Loading -> {
                        Log.d("EPOKHAI 12","")
                    }
                    is NetworkResult.Success -> {
                        Log.d("EPOKHAI 13","${it.data}")
                        binding.progressbarHolder.isVisible = false
                        adapter = MessageAdapter(it.data!!)
                        adapter.notifyDataSetChanged()
                        binding.recyclers.setItemViewCacheSize(it.data.size)
                        binding.recyclers.adapter = adapter
                    }
                }
            }
        }
    }
}