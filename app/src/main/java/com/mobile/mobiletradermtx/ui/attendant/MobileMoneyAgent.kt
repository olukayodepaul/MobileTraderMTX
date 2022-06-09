package com.mobile.mobiletradermtx.ui.attendant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.mobiletradermtx.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobile.mobiletradermtx.databinding.ActivityMobileMoneyAgentBinding
import com.mobile.mobiletradermtx.databinding.BottomItemSheetBinding
import com.mobile.mobiletradermtx.dto.IsMoneyAgent
import com.mobile.mobiletradermtx.util.NetworkResult
import com.mobile.mobiletradermtx.util.ToastDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MobileMoneyAgent : AppCompatActivity() {

    private lateinit var binding: ActivityMobileMoneyAgentBinding


    private val viewModel: AttendantViewModel by viewModels()

    private lateinit var adapter: MobileMoneyAgentAdapter

    private lateinit var employeeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileMoneyAgentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initAdapter()
        employeeId = intent.getStringExtra("employeeId")!!
        Log.d("checkIntent", employeeId)
        viewModel.isMobileMoneyAgent(employeeId)
        agentStateFlow()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.refresh_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.retry -> {
                viewModel.isMobileMoneyAgent(employeeId)
            }
        }
        return false
    }


    private fun agentStateFlow() {
        lifecycleScope.launchWhenResumed {
            viewModel.isMoneyAgentsResponseState.collect {
                it.let {
                    when (it) {

                        is NetworkResult.Empty -> {}

                        is NetworkResult.Error -> {
                            ToastDialog(applicationContext, it.toString())
                            binding.progressbarHolder.isVisible = false
                        }

                        is NetworkResult.Loading -> {}

                        is NetworkResult.Success -> {

                            val isCacheData = it.data!!

                            if(isCacheData.status ==200) {

                                adapter = MobileMoneyAgentAdapter(isCacheData.orderagent!!, applicationContext, ::handleAdapterEvent)
                                adapter.notifyDataSetChanged()
                                binding.recycler.setItemViewCacheSize(isCacheData.orderagent!!.size)
                                binding.recycler.adapter = adapter
                                binding.progressbarHolder.isVisible = false

                            }else {
                                ToastDialog(applicationContext, isCacheData.msg!!)
                                binding.progressbarHolder.isVisible = false
                            }

                        }
                    }
                }
            }
        }
    }


    private fun handleAdapterEvent(items: IsMoneyAgent) {

        val inflater = LayoutInflater.from(applicationContext)
        val bindings = BottomItemSheetBinding.inflate(inflater)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(bindings.root)
        bindings.textView.text = items.agennama!!.lowercase().replaceFirstChar{it.uppercase()}
        bindings.textView2.text = items.phone
        bindings.textView4.text = items.address!!.lowercase().replaceFirstChar{it.uppercase()}
        dialog.show()

    }


}