package com.mobile.mobiletradermtx.ui.salesrecord

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.NumberFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.mobiletradermtx.R
import com.mobile.mobiletradermtx.dto.GetRequestToken
import com.mobile.mobiletradermtx.dto.IsParcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobile.mobiletradermtx.databinding.ActivitySalesRecordBinding
import com.mobile.mobiletradermtx.ui.sales.SalesActivity
import com.mobile.mobiletradermtx.util.NetworkResult
import com.mobile.mobiletradermtx.util.SessionManager
import com.mobile.mobiletradermtx.util.ToastDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first


@AndroidEntryPoint
class SalesRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalesRecordBinding

    private val viewModel: SalesRecordViewModel by viewModels()

    private lateinit var adapter: SalesRecordAdapter

    private lateinit var database: FirebaseDatabase

    private lateinit var isIntentData: IsParcelable

    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        database = FirebaseDatabase.getInstance()
        sessionManager = SessionManager(this)
        isIntentData = intent.extras!!.getParcelable("isParcelable")!!
        initWidget()
        initRecyclerView()
        viewModel.fetchSalesRecordEntries()
        isServerResponse()
        setRequestedToken()
        isProcessToken()
        responseFromDateSentToServer()
    }

    private fun initWidget() = lifecycleScope.launchWhenCreated {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun requestToken() = lifecycleScope.launchWhenCreated{
        binding.imageCloud.isVisible = true
        binding.resMessage.text = "Cloud Request, Please Wait"
        binding.progressBar.isVisible = true
        binding.imagePass.isVisible = false
        binding.imageFail.isVisible = false
        viewModel.tokenRecordEntries(isIntentData.data!!.urno!!, sessionManager.fetchEmployeeId.first(), "${isIntentData.latitude},${isIntentData.longitude}", sessionManager.fetchRegion.first())
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.setHasFixedSize(true)

        binding.tokenImage.setOnClickListener {
            if(isIntentData.data!!.th=="b") {
                ToastDialog(applicationContext, "Token Expired, Please contact token administrator")
            }else{
                binding.tvFieldCustname.setText(isIntentData.data!!.defaulttoken)
            }
        }

        binding.closeIcon.setOnClickListener {
            binding.content.isVisible = true
            binding.tokenNotification.isVisible = false
            binding.responseNotification.isVisible = false
        }

        binding.button.setOnClickListener {
            postSalesToServer()
        }

        binding.rcompleteButon.setOnClickListener {
            val intent = Intent(applicationContext, SalesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.rerrorButton.setOnClickListener {
            binding.content.isVisible = false
            binding.tokenNotification.isVisible = true
            binding.responseNotification.isVisible = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_outlet, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submit -> {
                binding.content.isVisible = false
                binding.tokenNotification.isVisible = true
                binding.responseNotification.isVisible = false
            }
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun isServerResponse()=lifecycleScope.launchWhenCreated {
        viewModel.salesRecordResponseState.collect {
                it.let {
                    when (it) {
                        is NetworkResult.Empty -> {
                        }

                        is NetworkResult.Error -> {
                        }

                        is NetworkResult.Loading -> {
                        }

                        is NetworkResult.Success -> {

                            val limitToSalesEntry = it.data!!.filter {filters->
                                filters.seperator.equals("1")
                            }

                            val isPricing = limitToSalesEntry.sumOf { qty ->
                                qty.pricing!!.toDouble()
                            }

                            val isInventory = limitToSalesEntry.sumOf { price ->
                                price.inventory!!.toDouble()
                            }

                            val isQtyOrdered = limitToSalesEntry.sumOf { price ->
                                price.orders!!.toDouble()
                            }

                            val isAmount = limitToSalesEntry.sumOf{ price ->
                                price.orders!!.toDouble() * price.price!!
                            }

                            binding.vamount.text = NumberFormat.getInstance().format(isAmount)
                            binding.vinventory.text = NumberFormat.getInstance().format(isInventory)
                            binding.vpricing.text = NumberFormat.getInstance().format(isPricing)
                            binding.vqtysold.text = NumberFormat.getInstance().format(isQtyOrdered)

                            adapter = SalesRecordAdapter(limitToSalesEntry)
                            adapter.notifyDataSetChanged()
                            binding.recycler.setItemViewCacheSize(limitToSalesEntry.size)
                            binding.recycler.adapter = adapter
                        }
                    }
                }
            }
    }

    private fun setRequestedToken() {
        val references = database.getReference("/defaulttoken/"+isIntentData.data!!.urno)

        references.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val vToken = p0.getValue(GetRequestToken::class.java)

                    binding.imageCloud.isVisible = false
                    binding.progressBar.isVisible = false

                    if(vToken!!.status=="1") {
                        binding.resMessage.text = "Token Approve"
                        binding.imagePass.isVisible = true
                        binding.imageFail.isVisible = false
                    }else {
                        binding.resMessage.text = "Token Decline"
                        binding.imagePass.isVisible = false
                        binding.imageFail.isVisible = true
                    }

                }
            }
        })
    }

    private fun isProcessToken() = lifecycleScope.launchWhenCreated {
        viewModel.tokenResponseState.collect{
            it.let {
                when(it){
                    is NetworkResult.Empty -> {
                    }

                    is NetworkResult.Error -> {
                        binding.imageCloud.isVisible = false
                        binding.resMessage.text = it.throwable!!.message.toString()
                        binding.progressBar.isVisible = false
                        binding.imagePass.isVisible = false
                        binding.imageFail.isVisible = true
                    }

                    is NetworkResult.Loading -> {
                        binding.imageCloud.isVisible = true
                        binding.resMessage.text = "Cloud Request, Please Wait"
                        binding.progressBar.isVisible = true
                        binding.imagePass.isVisible = false
                        binding.imageFail.isVisible = false
                    }

                    is NetworkResult.Success -> {
                        if(it.data!!.status==200){
                            binding.imageCloud.isVisible = true
                            binding.resMessage.text = it.data.msg
                            binding.progressBar.isVisible = false
                            binding.imagePass.isVisible = false
                            binding.imageFail.isVisible = false
                        }else{
                            binding.imageCloud.isVisible = false
                            binding.resMessage.text = it.data.msg
                            binding.progressBar.isVisible = false
                            binding.imagePass.isVisible = false
                            binding.imageFail.isVisible = true
                        }
                    }
                }
            }
        }
    }

    private fun postSalesToServer() = lifecycleScope.launchWhenCreated{
        when{
            isIntentData.data!!.cust_token==binding.tvFieldCustname.text.toString().trim()->{
                initialLoader()
                viewModel.postSalesToServer(isIntentData, binding.tvFieldCustname.text.toString().trim())
            }
            isIntentData.data!!.defaulttoken==binding.tvFieldCustname.text.toString().trim()->{
                initialLoader()
                viewModel.postSalesToServer(isIntentData,binding.tvFieldCustname.text.toString().trim())
            }
            else->{
                ToastDialog(applicationContext, "Invalid Customer Verification code").toast
            }
        }
    }

    private fun initialLoader() {

        binding.content.isVisible = false
        binding.tokenNotification.isVisible = false
        binding.responseNotification.isVisible = true

        binding.rtitles.text = "Data Synchronisation"
        binding.rsubtitle.text = "Sending Data to Server"
        binding.rsubTitles.text = "Please kindly wait. Do not switch away from this screen, until the app ask you to do so"
        binding.rprogressBar.isVisible = true
        binding.rcompleteButon.isVisible = false
        binding.rerrorButton.isVisible = false
        binding.rpassImage.isVisible = false
        binding.rfailImage.isVisible = false

    }

    private fun responseFromDateSentToServer(){

        lifecycleScope.launchWhenResumed {
            viewModel.postSalesResponseState.collect {
                it.let {
                    when (it) {

                        is NetworkResult.Empty -> {
                        }

                        is NetworkResult.Error -> {

                            binding.rtitles.text = "Synchronisation Error"
                            binding.rsubtitle.text = "Fail to send Data to Server"
                            binding.rsubTitles.text = it.throwable!!.message.toString()
                            binding.rprogressBar.isVisible = false
                            binding.rcompleteButon.isVisible = false
                            binding.rerrorButton.isVisible = true
                            binding.rpassImage.isVisible = false
                            binding.rfailImage.isVisible = true
                        }

                        is NetworkResult.Loading -> {

                            binding.content.isVisible = false
                            binding.tokenNotification.isVisible = false
                            binding.responseNotification.isVisible = true

                            binding.rtitles.text = "Synchronisation"
                            binding.rsubtitle.text = "Sending Data To Server"
                            binding.rsubTitles.text =
                                "Please kindly wait. Do not switch away from this screen, until the app ask you to do so"
                            binding.rprogressBar.isVisible = true
                            binding.rcompleteButon.isVisible = false
                            binding.rerrorButton.isVisible = false
                            binding.rpassImage.isVisible = false
                            binding.rfailImage.isVisible = false

                        }

                        is NetworkResult.Success -> {
                            if (it.data!!.status == 200) {
                                binding.rtitles.text = "Synchronisation Successful"
                                binding.rsubtitle.text = it.data.msg
                                binding.rsubTitles.text = ""
                                binding.rprogressBar.isVisible = false
                                binding.rcompleteButon.isVisible = true
                                binding.rerrorButton.isVisible = false
                                binding.rpassImage.isVisible = true
                                binding.rfailImage.isVisible = false
                            } else {
                                binding.rtitles.text = "Synchronisation Completed"
                                binding.rsubtitle.text = it.data.msg
                                binding.rsubTitles.text = ""
                                binding.rprogressBar.isVisible = false
                                binding.rcompleteButon.isVisible = true
                                binding.rerrorButton.isVisible = false
                                binding.rpassImage.isVisible = true
                                binding.rfailImage.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }
}