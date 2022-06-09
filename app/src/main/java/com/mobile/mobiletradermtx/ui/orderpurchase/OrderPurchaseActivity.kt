package com.mobile.mobiletradermtx.ui.orderpurchase

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.mobiletradermtx.R
import com.google.firebase.database.FirebaseDatabase
import com.mobile.mobiletradermtx.ui.order.ReOrderActivity
import com.mobile.mobiletradermtx.databinding.ActivityOrderPurchaseBinding
import com.mobile.mobiletradermtx.dto.Customers
import com.mobile.mobiletradermtx.dto.Orders
import com.mobile.mobiletradermtx.util.FirebaseDatabases
import com.mobile.mobiletradermtx.util.NetworkResult
import com.mobile.mobiletradermtx.util.SessionManager
import com.mobile.mobiletradermtx.util.ToastDialog
import com.nex3z.notificationbadge.NotificationBadge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first


@AndroidEntryPoint
class OrderPurchaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderPurchaseBinding

    private  val viewModel: OrderPurchaseViewModel by viewModels()

    private lateinit var adapter: OrderAdapter

    private var isIntentData: Customers? = null

    private lateinit var database: FirebaseDatabase

    var notificationBadgeView: View? = null

    var notificationBadge: NotificationBadge? = null

    var item_Notification: MenuItem? = null

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        isIntentData = intent.extras!!.getParcelable("isParcelable")!!
        initAdapter()
        refreshAdapter()
        customerOrderResponse()

        database = FirebaseDatabase.getInstance()

        sessionManager = SessionManager(this)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.toolbar.subtitle = isIntentData!!.outletname
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.parentRecyclerView.layoutManager = layoutManager
        adapter = OrderAdapter(applicationContext)
        binding.parentRecyclerView.adapter = adapter
    }

    private fun refreshAdapter() {
        lifecycleScope.launchWhenCreated {
            viewModel.isSalesEntries(isIntentData!!.employee_id!!, isIntentData!!.urno!!)
        }
    }

    private fun customerOrderResponse() {
        lifecycleScope.launchWhenResumed {
            viewModel.orderEntryResponseState.collect {
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
                            if(it.data!!.status==200) {
                                binding.loaders.isVisible = false
                                adapter.notifyDataSetChanged()
                                binding.parentRecyclerView.setItemViewCacheSize(it.data.orderlist!!.size)
                                binding.parentRecyclerView.adapter = adapter
                                itemList(it.data.orderlist!!)
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

    private fun itemList(list: List<Orders>) {
        adapter.addData(list)
        adapter.notifyDataSetChanged()
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
        menuInflater.inflate(R.menu.visitdetail, menu)
        item_Notification = menu!!.findItem(R.id.action_notifications)
        notificationBadgeView = item_Notification!!.actionView
        notificationBadge = notificationBadgeView!!.findViewById(R.id.badge) as NotificationBadge

        notificationBadgeView!!.setOnClickListener {
            val intent = Intent(applicationContext, ReOrderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        setupBadge()
        return true
    }

    private fun setupBadge() = lifecycleScope.launchWhenCreated {
        FirebaseDatabases.setOrderBadge(
            sessionManager.fetchEmployeeId.first(),
            database,
            notificationBadge
        )
    }
}