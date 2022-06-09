package com.mobile.mobiletradermtx.ui.sales

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.firebase.database.FirebaseDatabase
import com.mobile.mobiletradermtx.databinding.ActivitySalesBinding
import com.mobile.mobiletradermtx.dto.Customers
import com.mobile.mobiletradermtx.dto.CustomersList
import com.mobile.mobiletradermtx.dto.toCustomers
import com.mobile.mobiletradermtx.ui.customers.AddCustomerActivity
import com.mobile.mobiletradermtx.ui.customers.UpdateCustomersActivity
import com.mobile.mobiletradermtx.util.StartGoogleMap.startGoogleMapIntent
import com.nex3z.notificationbadge.NotificationBadge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import com.mobile.mobiletradermtx.R
import com.mobile.mobiletradermtx.dto.IsParcelable
import com.mobile.mobiletradermtx.ui.order.ReOrderActivity
import com.mobile.mobiletradermtx.ui.orderpurchase.OrderPurchaseActivity
import com.mobile.mobiletradermtx.ui.salesentry.SalesEntryActivity
import com.mobile.mobiletradermtx.util.*
import com.mobile.mobiletradermtx.util.FirebaseDatabases.setOrderBadge
import com.mobile.mobiletradermtx.util.GeoFencing.setGeoFencing

@AndroidEntryPoint
class SalesActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySalesBinding

    private val viewModel: SalesViewModel by viewModels()

    private lateinit var adapter: SalesAdapter

    private lateinit var sessionManager: SessionManager

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    var searchView: SearchView? = null

    var notificationBadgeView: View? = null

    var notificationBadge: NotificationBadge? = null

    var item_Notification: MenuItem? = null

    private var hasGps = false

    lateinit var mLocationManager: LocationManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private var items: Customers? = null

    private var separators: Int? = null

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        setSupportActionBar(binding.toolbar)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        database = FirebaseDatabase.getInstance()
        initAdapter()
        refreshAdapter()
        salesResponse()
        onActivityResult()
        binding.loader.refreshImG.setOnClickListener(this)
        isPostSalesResponse()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        lifecycleScope.launchWhenResumed {
            binding.toolbar.subtitle =
                "${sessionManager.fetchEmployeeName.first()} (${sessionManager.fetchEmployeeEdcode.first()})"
        }

        binding.mapcustomers.setOnClickListener {
            val intent = Intent(applicationContext, AddCustomerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        isOutletUpdateAsync()
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.tvRecycler.layoutManager = layoutManager
        binding.tvRecycler.setHasFixedSize(true)
    }

    private fun refreshAdapter() {
        lifecycleScope.launchWhenCreated {
            viewModel.fetchAllSalesEntries(
                sessionManager.fetchEmployeeId.first(),
                sessionManager.fetchCustomerEntryDate.first(),
                GeoFencing.currentDate!!
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun salesResponse() {
        lifecycleScope.launchWhenResumed {
            viewModel.salesResponseState.collect {
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
                        }

                        is NetworkResult.Loading -> {
                            binding.loader.root.isVisible = true
                            binding.loader.tvTitle.text = "Connecting to MTx Cloud"
                            binding.loader.refreshImG.isVisible = false
                            binding.loader.subTitles.text = "Please Wait"
                            binding.loader.imageLoader.isVisible = true
                            binding.tvRecycler.isVisible = false
                        }

                        is NetworkResult.Success -> {

                            binding.progressbarHolder.isVisible = false


                            if (it.data!!.status == 200) {

                                binding.loader.root.isVisible = false
                                binding.tvRecycler.isVisible = true

                                sessionManager.storeCustomerEntryDate(GeoFencing.currentDate!!)

                                adapter = SalesAdapter(
                                    it.data.entries!!,
                                    applicationContext,
                                    ::handleAdapterEvent
                                )
                                adapter.notifyDataSetChanged()
                                binding.tvRecycler.setItemViewCacheSize(it.data.entries!!.size)
                                binding.tvRecycler.adapter = adapter

                            } else {

                                binding.loader.root.isVisible = true
                                binding.loader.tvTitle.text = it.data.message
                                binding.loader.refreshImG.isVisible = true
                                binding.loader.subTitles.text = "Tape to refresh"
                                binding.loader.imageLoader.isVisible = false
                                binding.tvRecycler.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleAdapterEvent(
        item: CustomersList,
        separator: Int
    ) {


        when (separator) {
            1 -> {
                val dmode = "d".single()
                val destination = "${item.latitude},${item.longitude}"
                startGoogleMapIntent(this, destination, dmode, 't')
            }
            2 -> {
                items = item.toCustomers()
                separators = separator
                isPermissionRequest()
            }
            3 -> {
                items = item.toCustomers()
                separators = separator
                isPermissionRequest()
            }
            4 -> {
                val intent = Intent(applicationContext, UpdateCustomersActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("isParcelable", item.toCustomers())
                startActivity(intent)
            }
            5 -> {
                binding.loader.root.isVisible = true
                binding.tvRecycler.isVisible = false
                binding.loader.tvTitle.text = "Outlet Synchronisation"
                binding.loader.subTitles.text = "Please wait...."
                viewModel.localOutletUpdate(item.urno!!)
            }
            6 -> {
                val intent = Intent(applicationContext, OrderPurchaseActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("isParcelable", item.toCustomers())
                startActivity(intent)
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
        setOrderBadge(sessionManager.fetchEmployeeId.first(), database, notificationBadge)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.refreshImG -> {
                refreshAdapter()
            }
        }
    }

    private fun isPermissionRequest() {

        val usesPermission = PermissionUtility.requestPermission(this)

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        if (usesPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, usesPermission.toTypedArray(), 0)
            return
        } else if (!hasGps) {
            isGpsEnableIntent()
            return
        } else if (available == ConnectionResult.API_UNAVAILABLE) {
            ToastDialog(applicationContext, "Play Update the google play service");
            return
        } else {
            getCurrentLocation()
        }
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

    private fun isGpsEnableIntent() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activityResultLauncher.launch(intent)
    }

    private fun onActivityResult() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {

        locationRequest = LocationRequest.create().apply {
            interval = 1 * 1000
            fastestInterval = 1 * 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(builder.build())

        binding.loader.root.isVisible = true
        binding.tvRecycler.isVisible = false

        binding.loader.tvTitle.text = "Location Request"
        binding.loader.refreshImG.isVisible = false
        binding.loader.subTitles.text = "Please Wait"
        binding.loader.imageLoader.isVisible = true


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

    //location settings.
    private fun stopLocationUpdate() {
        binding.tvRecycler.isVisible = true
        binding.loader.root.isVisible = false
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun isCurrentLocationSetter(currentLocation: Location?) {

        stopLocationUpdate()

        when (separators) {

            3 -> {
                if (items!!.outlet_waiver!!.lowercase() == "true") {

                    val ifIsValidOutlet: Boolean = setGeoFencing(
                        currentLocation!!.latitude,
                        currentLocation.longitude,
                        items!!.latitude!!.toDouble(),
                        items!!.longitude!!.toDouble()
                    )
                    if (!ifIsValidOutlet) {

                       ToastDialog(applicationContext, "You are not at the corresponding outlet")

                    } else {

                        viewModel.sentToken(items!!.urno!!)
                        val intent = Intent(applicationContext, SalesEntryActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        val contentFlow = IsParcelable(
                            currentLocation.latitude.toString(),
                            currentLocation.longitude.toString(),
                            GeoFencing.currentTime,
                            GeoFencing.currentDate,
                            GeoFencing.currentDate + "${items!!.rep_id}" + UUID.randomUUID()
                                .toString(),
                            "Open Outlet",
                            items,
                            SimpleDateFormat("HH:mm:ss").format(Date())
                        )
                        intent.putExtra("isParcelable", contentFlow)
                        startActivity(intent)
                    }
                } else {

                    viewModel.sentToken(items!!.urno!!)
                    val contentFlow = IsParcelable(
                        currentLocation!!.latitude.toString(),
                        currentLocation.longitude.toString(),
                        GeoFencing.currentTime,
                        GeoFencing.currentDate,
                        GeoFencing.currentDate + "${items!!.rep_id}" + UUID.randomUUID().toString(),
                        "Open Outlet",
                        items,
                        SimpleDateFormat("HH:mm:ss").format(Date())
                    )
                    val intent = Intent(applicationContext, SalesEntryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("isParcelable", contentFlow)
                    startActivity(intent)
                }
            }

            2 -> {
                isCloseOutlet(items, currentLocation!!.latitude, currentLocation.longitude)
            }
        }
    }


    private fun isCloseOutlet(items: Customers? = null, lat: Double? = null, lng: Double? = null) {

        binding.contentsLayout.isVisible = false
        binding.closeRequestType.isVisible = true
        binding.imageGoods.isVisible = false

        val contentFlow = IsParcelable(
            lat.toString(),
            lng.toString(),
            GeoFencing.currentTime,
            GeoFencing.currentDate,
            GeoFencing.currentDate + "${items!!.rep_id}" + UUID.randomUUID()
                .toString(),
            " ",
            items,
            SimpleDateFormat("HH:mm:ss").format(Date())
        )

        if (items.outlet_waiver!!.lowercase() == "true") {
            val ifIsValidOutlet: Boolean = setGeoFencing(
                lat!!,
                lng!!,
                items.latitude!!.toDouble(),
                items.longitude!!.toDouble()
            )
            if (!ifIsValidOutlet) {

                binding.contentsLayout.isVisible = false
                binding.closeRequestType.isVisible = true
                binding.cloudIcons.isVisible = true
                binding.allAppTitles.text = "You are not at the corresponding outlet"
                binding.imageGoods.isVisible = false
                binding.progressBars.isVisible = false

            } else {
                viewModel.fetchAllSalesEntries(contentFlow)
            }
        } else {
            viewModel.fetchAllSalesEntries(contentFlow)
        }

        binding.closeIcon.setOnClickListener {
            binding.contentsLayout.isVisible = true
            binding.closeRequestType.isVisible = false
        }

    }

    private fun isPostSalesResponse() {
        lifecycleScope.launchWhenResumed {
            viewModel.closeOutletResponseState.collect {
                it.let {
                    when (it) {
                        is NetworkResult.Empty -> {
                        }

                        is NetworkResult.Error -> {
                            binding.contentsLayout.isVisible = false
                            binding.closeRequestType.isVisible = true
                            binding.cloudIcons.isVisible = false
                            binding.allAppTitles.text = it.throwable!!.message.toString()
                            binding.imageGoods.isVisible = true
                            binding.progressBars.isVisible = false
                        }
                        is NetworkResult.Loading -> {

                        }

                        is NetworkResult.Success -> {

                            if (it.data!!.status == 200) {

                                binding.contentsLayout.isVisible = false
                                binding.closeRequestType.isVisible = true
                                binding.cloudIcons.isVisible = false
                                binding.allAppTitles.text = it.data.msg
                                binding.imageGoods.isVisible = true
                                binding.progressBars.isVisible = false

                            } else {

                                binding.contentsLayout.isVisible = false
                                binding.closeRequestType.isVisible = true
                                binding.cloudIcons.isVisible = true
                                binding.allAppTitles.text = it.data.msg
                                binding.imageGoods.isVisible = false
                                binding.progressBars.isVisible = false

                            }
                        }
                    }
                }
            }
        }
    }

    private fun isOutletUpdateAsync() {
        lifecycleScope.launchWhenResumed {
            viewModel.localOutletUpdateState.collect {
                it.let {
                    when (it) {

                        is NetworkResult.Empty -> {

                        }

                        is NetworkResult.Error -> {
                            binding.loader.root.isVisible = false
                            binding.tvRecycler.isVisible = true
                            ToastDialog(applicationContext, it.throwable!!.message.toString())
                        }

                        is NetworkResult.Loading -> {
                        }

                        is NetworkResult.Success -> {

                            if (it.data!!.status == 200) {
                                binding.loader.root.isVisible = false
                                binding.tvRecycler.isVisible = true
                                ToastDialog(applicationContext, "Successfully Synchronise")

                            }else{
                                binding.loader.root.isVisible = false
                                binding.tvRecycler.isVisible = true
                                ToastDialog(applicationContext, "Synchronisation Error")
                            }
                        }
                    }
                }
            }
        }
    }


}