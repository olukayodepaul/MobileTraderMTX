package com.mobile.mobiletradermtx.ui.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.mobile.mobiletradermtx.databinding.ActivityOrderSummaryBinding
import com.mobile.mobiletradermtx.databinding.TokenDialogBinding
import com.mobile.mobiletradermtx.dto.AllCustomerProductOrder
import com.mobile.mobiletradermtx.ui.module.ModulesActivity
import com.mobile.mobiletradermtx.util.NetworkResult
import com.mobile.mobiletradermtx.util.ToastDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class OrderSummary : AppCompatActivity() {

    private lateinit var binding: ActivityOrderSummaryBinding

    private lateinit var passData: AllCustomerProductOrder

    private lateinit var adapter: OrderSummaryAdapter

    private lateinit var bindings: TokenDialogBinding

    private val viewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        passData = intent.extras!!.getParcelable("isParcelable")!!
        isCustomerInfo()
        initAdapter()
        viewModel.isSkuOrdered(passData.orderid)

        isAllSkuOrderedFor()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.confirmOrder.setOnClickListener {
            confirmTokenDialog()
        }

        comfirmOrder()
    }

    private fun isCustomerInfo() {
        binding.custName.text = passData.outletname
        binding.address.text = passData.outletaddress
        binding.contactphone.text = passData.contactphone
        binding.orderId.text = passData.orderid.toString()
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.recycleOrderHistory.layoutManager = layoutManager
        binding.recycleOrderHistory.setHasFixedSize(true)
    }

    private fun isAllSkuOrderedFor() = lifecycleScope.launchWhenResumed {
        viewModel.skuOrderedResponseState.collect {
            it.let {
                when (it) {
                    is NetworkResult.Empty -> {

                    }

                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Error -> {
                        binding.loaders.isVisible = false
                        binding.recycleOrderHistory.isVisible = false
                        ToastDialog(applicationContext, it.throwable!!.message.toString())
                    }

                    is NetworkResult.Success -> {

                        binding.loaders.isVisible = false
                        binding.recycleOrderHistory.isVisible = true

                        if(it.data!!.skuorder.isNullOrEmpty()){
                            ToastDialog(applicationContext, "No Order Record")
                            return@collect
                        }

                        val df = DecimalFormat("#.#")
                        adapter = OrderSummaryAdapter(it.data.skuorder!!)
                        adapter.notifyDataSetChanged()
                        binding.recycleOrderHistory.setItemViewCacheSize(it.data.skuorder!!.size)
                        binding.recycleOrderHistory.adapter = adapter
                        binding.priceTvs.text = String.format("%,.1f", (df.format(it.data.totalamount).toDouble())).trim()

                        binding.skuTvs.text = "total = {Cash Payment}"

                        if(passData.trantype.lowercase()=="true") {
                            binding.skuTvs.text = "total = {Paid}"
                        }

                        binding.qtyTvs.text = it.data.totalqty.toString()

                    }
                }
            }
        }
    }

    private fun confirmTokenDialog() {

        bindings = TokenDialogBinding.inflate(layoutInflater)

        val dialog = MaterialDialog(this)
            .cancelOnTouchOutside(true)
            .cancelable(false)
            .customView(null, bindings.root)

        bindings.closeIcon.setOnClickListener {
            dialog.dismiss()
        }

        bindings.button.setOnClickListener {

            if (bindings.tvFieldCustname.text.toString() == passData.token.toString()) {

                bindings.progressBar.isVisible = true
                bindings.buttons.isVisible = true
                bindings.button.isVisible = false
                viewModel.isOrder(passData.employeeid, passData.orderid)

            } else {
                ToastDialog(applicationContext, "Please Enter Valid Order Verification Code")
            }
        }

        bindings.buttonss.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, ModulesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        dialog.show()
    }

    private fun comfirmOrder() = lifecycleScope.launchWhenResumed {
        viewModel.makeAnOrderResponseState.collect {
            it.let {
                when (it) {

                    is NetworkResult.Empty -> {}

                    is NetworkResult.Loading -> {}

                    is NetworkResult.Error -> {
                        bindings.progressBar.isVisible = false
                        bindings.buttons.isVisible = false
                        bindings.button.isVisible = true
                        ToastDialog(applicationContext, it.throwable!!.message.toString())
                    }

                    is NetworkResult.Success -> {

                        if (it.data!!.status == 200) {
                            bindings.progressBar.isVisible = false
                            bindings.buttons.isVisible = false
                            bindings.button.isVisible = false
                            bindings.buttonss.isVisible = true
                            return@collect
                        }

                        bindings.progressBar.isVisible = false
                        bindings.buttons.isVisible = false
                        bindings.button.isVisible = true
                        bindings.buttonss.isVisible = false
                        ToastDialog(applicationContext, it.data.msg)

                    }
                }
            }
        }
    }


}