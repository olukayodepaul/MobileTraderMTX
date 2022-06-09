package com.mobile.mobiletradermtx.ui.salesentry

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.mobile.mobiletradermtx.databinding.ActivitySalesEntryBinding
import com.mobile.mobiletradermtx.databinding.SalesEntryAdapterBinding
import com.mobile.mobiletradermtx.dto.BasketLimitList
import com.mobile.mobiletradermtx.dto.IsParcelable
import com.mobile.mobiletradermtx.ui.salesrecord.SalesRecordActivity
import com.nex3z.notificationbadge.NotificationBadge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import com.mobile.mobiletradermtx.R
import com.mobile.mobiletradermtx.ui.order.ReOrderActivity
import com.mobile.mobiletradermtx.util.*


@AndroidEntryPoint
class SalesEntryActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySalesEntryBinding

    private val viewModel: SalesEntryViewModel by viewModels()

    private lateinit var adapter: SalesEntryAdapter

    private lateinit var sessionManager: SessionManager

    var notificationBadgeView: View? = null

    var notificationBadge: NotificationBadge? = null

    var itemNotification: MenuItem? = null

    private lateinit var isIntentData: IsParcelable

    private lateinit var database: FirebaseDatabase

    var limit = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        sessionManager = SessionManager(this)
        database = FirebaseDatabase.getInstance()
        initAdapter()
        refreshAdapter()
        basketResponse()
        validateSalesEntryResponse()
        isIntentData = intent.extras!!.getParcelable("isParcelable")!!
        binding.loader.refreshImG.setOnClickListener(this)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.toolbar.subtitle = isIntentData.data!!.outletname
    }

    private fun refreshAdapter() {
        lifecycleScope.launchWhenCreated {
            viewModel.isUserDailyBaskets(
                sessionManager.fetchEmployeeId.first(),
                sessionManager.fetchDate.first(),
                GeoFencing.currentDate!!
            )
        }
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.tvRecycler.layoutManager = layoutManager
        binding.tvRecycler.setHasFixedSize(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_search -> {
                validateSalesEntry()
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.refreshImG -> {
                refreshAdapter()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.visitdetailss, menu)
        itemNotification = menu!!.findItem(R.id.action_notifications)
        notificationBadgeView = itemNotification!!.actionView
        notificationBadge = notificationBadgeView!!.findViewById(R.id.badge) as NotificationBadge

        notificationBadgeView!!.setOnClickListener {
            val intent = Intent(applicationContext, ReOrderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        setupBadge()
        return true
    }

    private fun setupBadge()= lifecycleScope.launchWhenCreated {
        FirebaseDatabases.setOrderBadge(sessionManager.fetchEmployeeId.first(), database, notificationBadge)
    }

    @SuppressLint("SetTextI18n")
    private fun basketResponse() {
        lifecycleScope.launchWhenResumed {
            viewModel.basketResponseState.collect {
                it.let {
                    when (it) {

                        is NetworkResult.Empty -> {
                        }

                        is NetworkResult.Error -> {
                            binding.loader.root.isVisible = true
                            binding.loader.tvTitle.text = it.throwable!!.message.toString()
                            binding.loader.refreshImG.isVisible = true
                            binding.loader.subTitles.text = "Tape to Refresh"
                            binding.loader.imageLoader.isVisible = true
                            binding.tvRecycler.isVisible = false
                            binding.progressbarHolder.isVisible = false
                            limit = 0
                        }

                        is NetworkResult.Loading -> {
                            binding.loader.root.isVisible = true
                            binding.loader.tvTitle.text = "Connecting to MTx Cloud"
                            binding.loader.refreshImG.isVisible = false
                            binding.loader.subTitles.text = "Please Wait"
                            binding.loader.imageLoader.isVisible = true
                            binding.tvRecycler.isVisible = false
                            binding.progressbarHolder.isVisible = true
                            limit = 0
                        }

                        is NetworkResult.Success -> {
                            if (it.data!!.status == 200) {
                                binding.progressbarHolder.isVisible = false
                                binding.loader.root.isVisible = false
                                binding.tvRecycler.isVisible = true
                                adapter = SalesEntryAdapter(
                                    it.data.data!!,
                                    applicationContext,
                                    ::handlesAdapterEvent
                                )
                                adapter.notifyDataSetChanged()
                                binding.tvRecycler.setItemViewCacheSize(it.data.data!!.size)
                                binding.tvRecycler.adapter = adapter
                                binding.progressbarHolder.isVisible = false
                                limit = 1
                            } else {
                                binding.loader.root.isVisible = true
                                binding.loader.tvTitle.text = it.data.message
                                binding.loader.refreshImG.isVisible = true
                                binding.loader.subTitles.text = "Tape to Refresh"
                                binding.loader.imageLoader.isVisible = false
                                binding.tvRecycler.isVisible = false
                                binding.progressbarHolder.isVisible = false
                                limit = 0
                            }

                        }
                    }
                }
            }
        }
    }

    private fun handlesAdapterEvent(item: BasketLimitList, binding: SalesEntryAdapterBinding) {

        var trasformPricing = 0
        var trasformInventory = 0.0
        var trasformOrder = 0.0
        var controltrasformPricing = 0
        var controltrasformInventory = 0
        var controltrasformOrder = 0

        if (binding.mtPricing.text.toString().isNotEmpty()) {
            trasformPricing = binding.mtPricing.text.toString().toInt()
            controltrasformPricing = 1
        }

        if (binding.mtInventory.text.toString() == ".") {
            binding.mtInventory.setText("")
            controltrasformInventory = 0
        } else if (binding.mtInventory.text.toString().isNotEmpty()) {
            trasformInventory = binding.mtInventory.text.toString().toDouble()
            controltrasformInventory = 1
        }

        if (binding.mtOrder.text.toString() == ".") {
            binding.mtOrder.setText("")
            controltrasformOrder = 0
        } else if (binding.mtOrder.text.toString().isNotEmpty()) {
            if (item.blimit == "true") {
                trasformOrder = binding.mtOrder.text.toString().toDouble()
                controltrasformOrder = 1
            } else {
                trasformOrder = binding.mtOrder.text.toString().toDouble()
                if (trasformOrder > item.order_sold!!) {
                    binding.mtOrder.setText("")
                    trasformOrder = 0.0
                    controltrasformOrder = 0
                } else {
                    controltrasformOrder = 1
                }
            }
        }

        viewModel.updateDailySales(
            trasformInventory,
            trasformPricing,
            trasformOrder,
            SimpleDateFormat("HH:mm:ss").format(Date()),
            controltrasformPricing,
            controltrasformInventory,
            controltrasformOrder,
            item.auto
        )
    }

    private fun validateSalesEntry() {
        viewModel.validateSalesEntries()
    }

    private fun validateSalesEntryResponse() = lifecycleScope.launchWhenCreated {
        viewModel.validateSalesEntryResponseState.collect {
            it.let {
                when (it) {

                    is NetworkResult.Empty -> {
                    }

                    is NetworkResult.Error -> {
                    }

                    is NetworkResult.Loading -> {
                    }

                    is NetworkResult.Success -> {
                        if (it.data!! == 0 && limit == 1) {
                            val intent = Intent(applicationContext, SalesRecordActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtra("isParcelable", isIntentData)
                            startActivity(intent)
                        }else{
                            ToastDialog(applicationContext, "Enter all the field").toast
                            return@collect
                        }
                    }
                }
            }
        }
    }
}