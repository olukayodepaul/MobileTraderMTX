package com.mobile.mobiletradermtx.ui.customers

import android.R
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
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.mobile.mobiletradermtx.databinding.ActivityUpdateCustomersBinding
import com.mobile.mobiletradermtx.dto.Customers
import com.mobile.mobiletradermtx.dto.UserSpinnerEntity
import com.mobile.mobiletradermtx.ui.sales.SalesActivity
import com.mobile.mobiletradermtx.util.NetworkResult
import com.mobile.mobiletradermtx.util.PermissionUtility
import com.mobile.mobiletradermtx.util.SessionManager
import com.mobile.mobiletradermtx.util.ToastDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class UpdateCustomersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateCustomersBinding

    private lateinit var sessionManager: SessionManager

    private val viewModel: AddCustomerViewModel by viewModels()

    private lateinit var languageAdapter: LanguageAdapter

    private lateinit var outletClassAdapter: OutletClassAdapter

    private lateinit var outletTypeAdapter: OutletTypeAdapter

    private var hasGps = false

    lateinit var mLocationManager: LocationManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var isIntentData: Customers? = null

    private var outletLanguageId: Int? = null

    private var outletClassId: Int? = null

    private var outletTypeId: Int? = null

    private var outletName: String? = null

    private var contactPerson: String? = null

    private var mobileNumber: String? = null

    private var contactAddress: String? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateCustomersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toobar)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        sessionManager = SessionManager(this)
        isRefresh()
        onActivityResult()
        addCustomerCallBack()
        viewModel.fetchAllSpinners()
        languageAdapter = LanguageAdapter()
        outletClassAdapter = OutletClassAdapter()
        outletTypeAdapter = OutletTypeAdapter()
        isIntentData = intent.extras!!.getParcelable("isParcelable")!!

        binding.toobar.setNavigationOnClickListener {
            onBackPressed()
        }

        lifecycleScope.launchWhenResumed {
            binding.toobar.subtitle =
                "${sessionManager.fetchEmployeeName.first()} (${sessionManager.fetchEmployeeEdcode.first()})"
        }

        binding.includes.errorButton.setOnClickListener {

            binding.widgetNotification.isVisible = true
            binding.widgetContent.isVisible = false

            binding.includes.titles.text = "Cloud Synchronisation"
            binding.includes.subtitle.text = "Sending Data To Server"
            binding.includes.subTitles.text = "Please do not Switch away from this screen, until the app ask you to DO SO."
            binding.includes.progressBar.isVisible = true
            binding.includes.completeButon.isVisible = false
            binding.includes.errorButton.isVisible = false
            binding.includes.passImage.isVisible = false
            binding.includes.failImage.isVisible = false

            isPermissionRequest()
            getCurrentLocation()
        }

        binding.includes.completeButon.setOnClickListener {
            val intent = Intent(applicationContext, SalesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.includes.banner.text = "Update Outlet"
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.mobile.mobiletradermtx.R.menu.map_outlet, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.mobile.mobiletradermtx.R.id.submit -> {

                outletLanguageId = languageAdapter.getValueId(binding.tvFieldLangauge.text.toString())
                outletClassId = outletClassAdapter.getValueId(binding.tvFieldClass.text.toString())
                outletTypeId = outletTypeAdapter.getValueId(binding.tvFieldType.text.toString())
                outletName = binding.tvFieldCustname.text.toString()
                contactPerson = binding.tvFieldContactPerson.text.toString()
                mobileNumber = binding.tvFieldContact.text.toString()
                contactAddress = binding.tvFieldAddress.text.toString()

                if (binding.tvFieldLangauge.text.toString() == "Select Language" ||
                    binding.tvFieldClass.text.toString()=="Select Customer Category" || binding.tvFieldType.text.toString()=="Select Customer Type"
                    || outletName!!.isEmpty() || contactPerson!!.isEmpty() || mobileNumber!!.isEmpty() || contactAddress!!.isEmpty()
                ) {
                    ToastDialog(applicationContext, "Please enter all the field")
                } else {

                    binding.widgetNotification.isVisible = true
                    binding.widgetContent.isVisible = false

                    binding.includes.titles.text = "Cloud Synchronisation"
                    binding.includes.subtitle.text = "Sending Data To Server"
                    binding.includes.subTitles.text = "Please do not Switch away from this screen, until the app ask you to DO SO."
                    binding.includes.progressBar.isVisible = true
                    binding.includes.completeButon.isVisible = false
                    binding.includes.errorButton.isVisible = false
                    binding.includes.passImage.isVisible = false
                    binding.includes.failImage.isVisible = false

                    isPermissionRequest()
                    getCurrentLocation()
                }
            }
        }
        return false
    }


    private fun isRefresh() {
        lifecycleScope.launchWhenResumed {
            viewModel.spinnerResponseState.collect {
                it.let {
                    when (it) {
                        is NetworkResult.Empty -> {
                        }

                        is NetworkResult.Error -> {
                        }

                        is NetworkResult.Loading -> {
                        }

                        is NetworkResult.Success -> {

                            val languages: List<UserSpinnerEntity> =
                                it.data!!.data!!.filter { fil -> fil.sep == 2 }

                            val outlettype: List<UserSpinnerEntity> =
                                it.data.data!!.filter { fil -> fil.sep == 3 }

                            val outletclass: List<UserSpinnerEntity> =
                                it.data.data!!.filter { fil -> fil.sep == 1 }

                            val outletLanguage = ArrayList<String>()
                            val outletClass = ArrayList<String>()
                            val outletType = ArrayList<String>()


                            if (languageAdapter.size() > 0) {
                                languageAdapter.clear()
                            }

                            if (outletClassAdapter.size() > 0) {
                                outletClassAdapter.clear()
                            }

                            if (outletTypeAdapter.size() > 0) {
                                outletTypeAdapter.clear()
                            }

                            for (i in languages.indices) {
                                outletLanguage.add(languages[i].name!!)
                                languageAdapter.add(languages[i].id!!, languages[i].name!!)
                            }

                            for (i in outletclass.indices) {
                                outletClass.add(outletclass[i].name!!)
                                outletClassAdapter.add(outletclass[i].id!!, outletclass[i].name!!)
                            }

                            for (i in outlettype.indices) {
                                outletType.add(outlettype[i].name!!)
                                outletTypeAdapter.add(outlettype[i].id!!, outlettype[i].name!!)
                            }

                            val arrayAdapterLanguage = ArrayAdapter(
                                applicationContext,
                                R.layout.simple_list_item_1,
                                outletLanguage
                            )
                            binding.tvFieldLangauge.setAdapter(arrayAdapterLanguage)
                            binding.tvFieldLangauge.threshold = 1

                            val arrayAdapterOutletType = ArrayAdapter(
                                applicationContext,
                                R.layout.simple_list_item_1,
                                outletType
                            )
                            binding.tvFieldType.setAdapter(arrayAdapterOutletType)
                            binding.tvFieldType.threshold = 1

                            val arrayAdapterOutletClass = ArrayAdapter(
                                applicationContext,
                                R.layout.simple_list_item_1,
                                outletClass
                            )
                            binding.tvFieldClass.setAdapter(arrayAdapterOutletClass)
                            binding.tvFieldClass.threshold = 1

                            if (isIntentData!!.outletlanguageid!! >= 1) {
                                binding.tvFieldLangauge.setText(
                                    binding.tvFieldLangauge.adapter.getItem(
                                        languageAdapter.getIndexById(isIntentData!!.outletlanguageid!!)
                                    ).toString(), false
                                )
                            }

                            if (isIntentData!!.outlettypeid!! >= 1) {
                                binding.tvFieldType.setText(
                                    binding.tvFieldType.adapter.getItem(
                                        outletTypeAdapter.getIndexById(isIntentData!!.outlettypeid!!)
                                    ).toString(), false
                                )
                            }

                            if (isIntentData!!.outletclassid!! >= 1) {
                                binding.tvFieldClass.setText(
                                    binding.tvFieldClass.adapter.getItem(
                                        outletClassAdapter.getIndexById(isIntentData!!.outletclassid!!)
                                    ).toString(), false
                                )
                            }

                            binding.tvFieldCustname.setText(isIntentData!!.outletname)
                            binding.tvFieldContactPerson.setText(isIntentData!!.contactname)
                            binding.tvFieldContact.setText(isIntentData!!.contactphone)
                            binding.tvFieldAddress.setText(isIntentData!!.outletaddress)

                        }
                    }
                }
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
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun isCurrentLocationSetter(location: Location?) {

        stopLocationUpdate()
        lifecycleScope.launchWhenResumed {

            val latitude = location!!.latitude.toString()
            val longitude = location.longitude.toString()
            val employee_id =sessionManager.fetchEmployeeId.first()

            viewModel.updateCustomers(employee_id, isIntentData!!.urno!!, latitude.toDouble(), longitude.toDouble(),  outletName!!,
                contactPerson!!, contactAddress!!,  mobileNumber!!, outletClassId!!, outletLanguageId!!, outletTypeId!!,
            )
        }
    }

    private fun addCustomerCallBack() {
        lifecycleScope.launchWhenResumed {
            viewModel.isCustomerUpdateResponseState.collect {
                it.let {
                    when (it) {

                        is NetworkResult.Empty -> {

                        }

                        is NetworkResult.Error -> {
                            binding.includes.titles.text = "Synchronisation Error"
                            binding.includes.subtitle.text = "Fail to send Data to Server"
                            binding.includes.subTitles.text = it.throwable!!.message.toString()
                            binding.includes.progressBar.isVisible = false
                            binding.includes.completeButon.isVisible = false
                            binding.includes.errorButton.isVisible = true
                            binding.includes.passImage.isVisible = false
                            binding.includes.failImage.isVisible = true
                        }

                        is NetworkResult.Loading -> {

                            binding.widgetContent.isVisible = false
                            binding.widgetNotification.isVisible = true

                            binding.includes.titles.text = "Cloud Synchronisation"
                            binding.includes.subtitle.text = "Sending Data To Server"
                            binding.includes.subTitles.text = "Please do not Switch away from this screen, until the app ask you to DO SO."
                            binding.includes.progressBar.isVisible = true
                            binding.includes.completeButon.isVisible = false
                            binding.includes.errorButton.isVisible = false
                            binding.includes.passImage.isVisible = false
                            binding.includes.failImage.isVisible = false
                        }

                        is NetworkResult.Success -> {
                            if(it.data!!.status==200){
                                binding.includes.titles.text = "Synchronisation Successful"
                                binding.includes.subtitle.text = it.data.notis
                                binding.includes.subTitles.text = "Finish By clicking the Completed Button"
                                binding.includes.progressBar.isVisible = false
                                binding.includes.completeButon.isVisible = true
                                binding.includes.errorButton.isVisible = false
                                binding.includes.passImage.isVisible = true
                                binding.includes.failImage.isVisible = false
                            }else{
                                binding.includes.titles.text = "Synchronisation Error"
                                binding.includes.subtitle.text = "Fail to send Data to Server"
                                binding.includes.subTitles.text = it.data.notis
                                binding.includes.progressBar.isVisible = false
                                binding.includes.completeButon.isVisible = false
                                binding.includes.errorButton.isVisible = true
                                binding.includes.passImage.isVisible = false
                                binding.includes.failImage.isVisible = true
                            }
                        }
                    }
                }
            }
        }
    }
}