package com.mobile.mobiletradermtx.ui.attendant

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.NumberFormat
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.mobile.mobiletradermtx.databinding.ActivityBankBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import com.mobile.mobiletradermtx.R
import com.mobile.mobiletradermtx.util.*


@AndroidEntryPoint
class BankActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBankBinding

    private lateinit var sessionManager: SessionManager

    private lateinit var adapter: BankAdapter

    private val viewModel: AttendantViewModel by viewModels()

    private var hasGps = false

    lateinit var mLocationManager: LocationManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var task_id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        sessionManager = SessionManager(this)
        setSupportActionBar(binding.toolbar)
        initAdapter()
        initWidget()
        onActivityResult()
        basketResponse()
        postBasket()
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.setHasFixedSize(true)
    }

    private fun initWidget() = lifecycleScope.launchWhenCreated{

        binding.toolbar.subtitle = "${sessionManager.fetchEmployeeName.first()} (${sessionManager.fetchEmployeeEdcode.first()})"

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        showLoaders()

        binding.include.clickRefresh.setOnClickListener {
            showLoaders()
        }

        binding.notifications.completeButon.setOnClickListener {
            binding.notifications.root.isVisible = false
            binding.include.root.isVisible = false
            binding.recycler.isVisible = true
        }
    }

    private fun showLoaders()= lifecycleScope.launchWhenCreated{
        binding.notifications.root.isVisible = false
        binding.include.root.isVisible = true
        binding.recycler.isVisible = false
        binding.include.imageLoader.isVisible = true
        binding.include.tvTitle.text = "Data synchronisation"
        binding.include.subTitles.text = "Wait, server request"
        binding.include.clickRefresh.isVisible = false
        viewModel.isUserDailyBaskets( sessionManager.fetchEmployeeId.first(), sessionManager.fetchDate.first(), GeoFencing.currentDate!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.payment_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.breaks -> {
                isPermissionRequest()
                task_id = 4
                getCurrentLocation()
            }
            R.id.payment -> {
                isPermissionRequest()
                task_id = 5
                getCurrentLocation()
            }
            R.id.mobileMoney -> {
                Log.d("checker","checker")
                isMobileMoneyAgentIntent()
            }
            R.id.retry->{
                showLoaders()
            }
        }
        return false
    }

    private fun isPermissionRequest() {

        val usesPermission = PermissionUtility.requestPermission(this)

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        if(usesPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, usesPermission.toTypedArray(), 0)
            return
        }else if(!hasGps){
            isGpsEnableIntent()
            return
        }else if(available == ConnectionResult.API_UNAVAILABLE) {
            ToastDialog(applicationContext, "Play Update the google play service");
            return
        }
    }

    private fun isGpsEnableIntent() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activityResultLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    println("permissionRequest  Granted")
                }
            }
        }
    }

    private fun onActivityResult() {
        activityResultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}
    }

    @SuppressLint("SetTextI18n")
    private fun basketResponse() {
        lifecycleScope.launchWhenResumed {
            viewModel.basketResponseState.collect {
                it.let {
                    when (it) {

                        is NetworkResult.Empty -> { }

                        is NetworkResult.Error -> {
                            binding.include.root.isVisible = true
                            binding.recycler.isVisible = false
                            binding.notifications.root.isVisible = false
                            binding.include.imageLoader.isVisible = false
                            binding.include.tvTitle.text = it.throwable!!.message.toString()
                            binding.include.subTitles.text = "Tap to Retry"
                            binding.include.clickRefresh.isVisible = true

                        }

                        is NetworkResult.Loading -> {}

                        is NetworkResult.Success -> {

                            if(it.data!!.status==200){

                                binding.include.root.isVisible = false
                                binding.recycler.isVisible = true
                                binding.notifications.root.isVisible = false

                                val limitToSalesEntry =  it.data.data!!.filter {
                                        filters->filters.seperator.equals("1")
                                }

                                val atyInRoll = limitToSalesEntry.sumOf {
                                        qty->qty.qty!!.toDouble()
                                }

                                val atyInPrice = limitToSalesEntry.sumOf {
                                        price->price.qty!!- price.order_sold!!.toDouble()
                                }


                                val atyInAmount = limitToSalesEntry.sumOf {
                                        price->(price.qty!!- price.order_sold!!.toDouble())*price.price!!
                                }

                                binding.qtyS.text = NumberFormat.getInstance().format(atyInRoll)
                                binding.amountS.text = NumberFormat.getInstance().format(atyInPrice)
                                binding.totalS.text = NumberFormat.getInstance().format(atyInAmount)

                                adapter = BankAdapter(limitToSalesEntry)
                                adapter.notifyDataSetChanged()
                                binding.recycler.setItemViewCacheSize(limitToSalesEntry.size)
                                binding.recycler.adapter = adapter

                            }else{

                                binding.notifications.root.isVisible = false
                                binding.include.root.isVisible = true
                                binding.recycler.isVisible = false

                                binding.include.imageLoader.isVisible = false
                                binding.include.tvTitle.text = it.data.message
                                binding.include.subTitles.text = "Tap to Retry"
                                binding.include.clickRefresh.isVisible = true
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {

        binding.notifications.root.isVisible = true
        binding.include.root.isVisible = false
        binding.recycler.isVisible = false

        binding.notifications.titles.text = "Cloud Synchronisation"
        binding.notifications.subtitle.text = "Sending Data To Server"
        binding.notifications.subTitles.text = "Please do not Switch away from this screen, until the app ask you to DO SO."
        binding.notifications.progressBar.isVisible = true
        binding.notifications.completeButon.isVisible = false
        binding.notifications.errorButton.isVisible = false
        binding.notifications.passImage.isVisible = false
        binding.notifications.failImage.isVisible = false

        locationRequest = LocationRequest.create().apply {
            interval = 1 * 1000
            fastestInterval = 1 * 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(builder.build())

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            isCurrentLocationSetter(locationResult.lastLocation)
        }
    }

    private fun stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun isCurrentLocationSetter(currentLocation: Location?)=lifecycleScope.launchWhenCreated {
        stopLocationUpdate()
        when(task_id){
            4->{
                lifecycleScope.launchWhenResumed {
                    viewModel.recordTask(sessionManager.fetchEmployeeId.first(), 4, currentLocation!!.latitude.toString(),currentLocation.longitude.toString(), "Taken Your Break" ,
                        GeoFencing.currentTime!!, 0)
                }
            }
            5->{
                lifecycleScope.launchWhenResumed {
                    viewModel.recordTask(sessionManager.fetchEmployeeId.first(), 5, currentLocation!!.latitude.toString(),currentLocation.longitude.toString(), "Make Payment",GeoFencing.currentTime!!, 3)
                }
            }
        }
    }

    private fun postBasket() {
        lifecycleScope.launchWhenResumed {
            viewModel.taskResponseState.collect {
                it.let {
                    when (it) {

                        is NetworkResult.Empty -> {}

                        is NetworkResult.Error -> {

                            binding.notifications.titles.text = "Synchronisation Error"
                            binding.notifications.subtitle.text = "Fail to send Data to Server"
                            binding.notifications.subTitles.text = it.throwable!!.message.toString()
                            binding.notifications.progressBar.isVisible = false
                            binding.notifications.completeButon.isVisible = false
                            binding.notifications.errorButton.isVisible = true
                            binding.notifications.passImage.isVisible = false
                            binding.notifications.failImage.isVisible = true
                        }

                        is NetworkResult.Loading -> {

                            binding.notifications.root.isVisible = true
                            binding.include.root.isVisible = false
                            binding.recycler.isVisible = false

                            binding.notifications.titles.text = "Cloud Synchronisation"
                            binding.notifications.subtitle.text = "Sending Data To Server"
                            binding.notifications.subTitles.text = "Please do not Switch away from this screen, until the app ask you to DO SO."
                            binding.notifications.progressBar.isVisible = true
                            binding.notifications.completeButon.isVisible = false
                            binding.notifications.errorButton.isVisible = false
                            binding.notifications.passImage.isVisible = false
                            binding.notifications.failImage.isVisible = false

                        }

                        is NetworkResult.Success -> {
                            if(it.data!!.status==200) {
                                binding.notifications.titles.text = "Synchronisation Successful"
                                binding.notifications.subtitle.text = it.data.msg
                                binding.notifications.subTitles.text = ""
                                binding.notifications.progressBar.isVisible = false
                                binding.notifications.completeButon.isVisible = true
                                binding.notifications.errorButton.isVisible = false
                                binding.notifications.passImage.isVisible = true
                                binding.notifications.failImage.isVisible = false
                            }else{
                                binding.notifications.titles.text = "Synchronisation Completed"
                                binding.notifications.subtitle.text = it.data.msg
                                binding.notifications.subTitles.text = ""
                                binding.notifications.progressBar.isVisible = false
                                binding.notifications.completeButon.isVisible = true
                                binding.notifications.errorButton.isVisible = false
                                binding.notifications.passImage.isVisible = true
                                binding.notifications.failImage.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isMobileMoneyAgentIntent() = lifecycleScope.launchWhenResumed {

        val intent = Intent(applicationContext, MobileMoneyAgent::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("employeeId",sessionManager.fetchEmployeeId.first().toString())
        startActivity(intent)

    }


}