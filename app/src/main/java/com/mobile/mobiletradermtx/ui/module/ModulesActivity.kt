package com.mobile.mobiletradermtx.ui.module

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.mobiletradermtx.R
import com.nex3z.notificationbadge.NotificationBadge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import com.google.firebase.database.FirebaseDatabase
import com.mobile.mobiletradermtx.databinding.ActivityModulesBinding
import com.mobile.mobiletradermtx.ui.messages.MessageActivity
import com.mobile.mobiletradermtx.ui.order.ReOrderActivity
import com.mobile.mobiletradermtx.util.FirebaseDatabases.setOrderBadge
import com.mobile.mobiletradermtx.util.GeoFencing.currentDate
import com.mobile.mobiletradermtx.util.NetworkResult
import com.mobile.mobiletradermtx.util.SessionManager
import com.mobile.mobiletradermtx.util.ToastDialog

@AndroidEntryPoint
class ModulesActivity : AppCompatActivity() {

    lateinit var binding: ActivityModulesBinding

    private val viewModel: ModulesViewModel by viewModels()

    private lateinit var adapters: ModuleAdapter

    private lateinit var sessionManager: SessionManager

    var notificationBadgeView: View? = null

    var notificationBadgeViewMessage: View? = null

    var notificationBadge: NotificationBadge? = null

    var notificationBadgeMessage: NotificationBadge? = null

    var item_Notification: MenuItem? = null

    var item_Notification_message: MenuItem? = null

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityModulesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        database = FirebaseDatabase.getInstance()
        sessionManager = SessionManager(this)
        initAdapter()
        refreshAdapter()
        userModulesResponse()

        lifecycleScope.launchWhenResumed {
            viewModel.isMessageAccuracy(sessionManager.fetchDynamicCustomerNo.first(), currentDate!!)
            binding.toolbar.subtitle = "${sessionManager.fetchEmployeeName.first()} (${sessionManager.fetchEmployeeEdcode.first()})"
        }

    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclers.layoutManager = layoutManager
        binding.recyclers.setHasFixedSize(true)
    }

    private fun refreshAdapter() {
        lifecycleScope.launchWhenCreated {
            viewModel.fetchAllSalesEntries(sessionManager.fetchEmployeeId.first())
        }
    }

    private fun userModulesResponse() {
        lifecycleScope.launchWhenResumed {
            viewModel.userModulesResponseState.collect {
                it.let {
                    when (it) {

                        is NetworkResult.Empty -> {
                        }

                        is NetworkResult.Error -> {
                            binding.loaders.isVisible = false
                            ToastDialog(applicationContext, it.throwable!!.message.toString()).toast
                        }

                        is NetworkResult.Loading -> {
                            binding.loaders.isVisible = true
                        }

                        is NetworkResult.Success -> {
                            if(it.data!!.status==200){
                                binding.loaders.isVisible = false
                                adapters = ModuleAdapter(it.data.modules!!, applicationContext)
                                adapters.notifyDataSetChanged()
                                binding.recyclers.setItemViewCacheSize(it.data.modules!!.size)
                                binding.recyclers.adapter = adapters
                            }else{
                                binding.loaders.isVisible = false
                                ToastDialog(applicationContext, it.data.msg!!)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_search -> {
                refreshAdapter()
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.visitdetails, menu)

        //Notification for Order
        item_Notification = menu!!.findItem(R.id.action_notifications)
        notificationBadgeView = item_Notification!!.actionView
        notificationBadge = notificationBadgeView!!.findViewById(R.id.badge) as NotificationBadge

        //Notification for Messages
        item_Notification_message = menu.findItem(R.id.action_notifications_message)
        notificationBadgeViewMessage = item_Notification_message!!.actionView
        notificationBadgeMessage = notificationBadgeViewMessage!!.findViewById(R.id.Mbadge) as NotificationBadge

        notificationBadgeView!!.setOnClickListener {
            val intent = Intent(applicationContext, ReOrderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        notificationBadgeViewMessage!!.setOnClickListener {
            val intent = Intent(applicationContext, MessageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        setupBadge()
        setMessageBadge()
        return true
    }

    private fun setupBadge()= lifecycleScope.launchWhenCreated {
        setOrderBadge(sessionManager.fetchEmployeeId.first(), database, notificationBadge)
    }

    private fun setMessageBadge() = lifecycleScope.launchWhenCreated {
        lifecycleScope.launchWhenResumed {
            viewModel.messageResponseState.collect {
                it.let {
                    when (it) {

                        is NetworkResult.Empty -> {
                        }

                        is NetworkResult.Error -> {
                        }

                        is NetworkResult.Loading -> {
                        }

                        is NetworkResult.Success -> {
                            if(it.data!!.counts==0){
                                notificationBadgeMessage!!.isVisible = false
                                notificationBadgeMessage!!.setText("${it.data.counts}")
                            }else{
                                notificationBadgeMessage!!.isVisible = true
                                notificationBadgeMessage!!.setText("${it.data.counts}")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            viewModel.isMessageAccuracy(sessionManager.fetchDynamicCustomerNo.first(), currentDate!!)
        }
    }

}