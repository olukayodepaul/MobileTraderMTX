package com.mobile.mobiletradermtx.ui.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.mobiletradermtx.R
import com.google.firebase.database.FirebaseDatabase
import com.mobile.mobiletradermtx.databinding.ActivityReOrderBinding
import com.mobile.mobiletradermtx.databinding.OrderAdapterBinding
import com.mobile.mobiletradermtx.dto.AllCustomerProductOrder
import com.mobile.mobiletradermtx.util.FirebaseDatabases.setOrderBadge
import com.mobile.mobiletradermtx.util.NetworkResult
import com.mobile.mobiletradermtx.util.SessionManager
import com.mobile.mobiletradermtx.util.StartGoogleMap.startGoogleMapIntent
import com.mobile.mobiletradermtx.util.ToastDialog
import com.nex3z.notificationbadge.NotificationBadge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first


@AndroidEntryPoint
class ReOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReOrderBinding

    private lateinit var adapter: OrderAdapter

    private val viewModel: OrderViewModel by viewModels()

    private lateinit var database: FirebaseDatabase

    var notificationBadgeView: View? = null

    var notificationBadge: NotificationBadge? = null

    var item_Notification: MenuItem? = null

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        sessionManager = SessionManager(this)
        database = FirebaseDatabase.getInstance()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        initAdapter()
        isInitialise()
        isAllOrderRequest()
    }

    private fun isInitialise()=lifecycleScope.launchWhenResumed{
        viewModel.fetchAllSalesEntries(sessionManager.fetchEmployeeId.first())
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.tvRecycler.layoutManager = layoutManager
        binding.tvRecycler.setHasFixedSize(true)
    }

    private fun isAllOrderRequest() = lifecycleScope.launchWhenResumed {
        viewModel.orderResponseState.collect {
            it.let {
                when (it) {

                    is NetworkResult.Empty -> {
                    }

                    is NetworkResult.Loading -> {
                    }

                    is NetworkResult.Error -> {
                        ToastDialog(applicationContext,it.throwable!!.message.toString())
                    }

                    is NetworkResult.Success -> {
                        //check empty list
                        binding.progressCust.isVisible = false

                        if(it.data!!.order.isNullOrEmpty()){
                            ToastDialog(applicationContext,"No Order is place")
                        }else{
                            adapter = OrderAdapter(it.data.order!!, ::handleAdapterEvent)
                            adapter.notifyDataSetChanged()
                            binding.tvRecycler.setItemViewCacheSize(it.data.order!!.size)
                            binding.tvRecycler.adapter = adapter
                        }

                    }
                }
            }
        }
    }

    private fun handleAdapterEvent(
        item: AllCustomerProductOrder,
        isAdapterBinding: OrderAdapterBinding
    ) {

        val popupMenu = PopupMenu(this, isAdapterBinding.iconsImages)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.floatingordermenu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.googlelocation -> {
                    startGoogleMapIntent(applicationContext,  "${item.latitude},${item.longitude}", "l".single(), 't')
                }
                R.id.deliver -> {
                    val intent = Intent(this, OrderSummary::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("isParcelable", item)
                    startActivity(intent)
                }
            }
            true
        }
        popupMenu.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.visitdetail, menu)
        item_Notification = menu!!.findItem(R.id.action_notifications)
        notificationBadgeView = item_Notification!!.actionView
        notificationBadge = notificationBadgeView!!.findViewById(R.id.badge) as NotificationBadge


        addBadge()
        return true
    }

    private fun addBadge()=lifecycleScope.launchWhenResumed {
        setOrderBadge(sessionManager.fetchEmployeeId.first(), database, notificationBadge)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_search -> {
                binding.progressCust.isVisible = true
                isInitialise()
            }
        }
        return false
    }

}