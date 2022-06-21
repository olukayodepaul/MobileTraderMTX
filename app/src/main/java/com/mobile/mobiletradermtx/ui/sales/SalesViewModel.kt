package com.mobile.mobiletradermtx.ui.sales

import android.annotation.SuppressLint
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.mobiletradermtx.dto.*
import com.mobile.mobiletradermtx.ui.sales.repository.SalesRepo
import com.mobile.mobiletradermtx.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SalesViewModel @ViewModelInject constructor(private val repo: SalesRepo): ViewModel() {

    private val _salesResponseState = MutableStateFlow<NetworkResult<IsAllCustomers>>(NetworkResult.Empty)
    val salesResponseState get() = _salesResponseState

    fun fetchAllSalesEntries(employee_id: Int, cacheDate: String, currentDate: String) = viewModelScope.launch {
        _salesResponseState.value = NetworkResult.Loading
        try {
            val mapper = IsAllCustomers()

            if(cacheDate==currentDate){
                val getFromLocalReo = repo.getCustomers()

                if(getFromLocalReo.isNullOrEmpty()) {
                    mapper.entries = emptyList()
                    mapper.message = "Please check your phone storage"
                    mapper.status = 400
                }else {
                    mapper.entries = getFromLocalReo
                    mapper.message = ""
                    mapper.status = 200
                }

                _salesResponseState.value = NetworkResult.Success(mapper)

            }else {

                val pullFromRemoteRepo = repo.getCustomer(employee_id)
                if(pullFromRemoteRepo.status==200) {
                    if(pullFromRemoteRepo.customers.isNullOrEmpty()) {
                        mapper.entries = emptyList()
                        mapper.message = "Customer is not assigned"
                        mapper.status = 400
                    }else{
                        repo.addCustomers(repo.getCustomer(employee_id).customers!!.map { it.toSalesEntry()})
                        val getFromLocalReo = repo.getCustomers()
                        if(getFromLocalReo.isNullOrEmpty()){
                            mapper.entries = emptyList()
                            mapper.message = "Please check your phone storage"
                            mapper.status = 400
                        }else{
                            mapper.entries = getFromLocalReo
                            mapper.message = ""
                            mapper.status = 200
                        }
                    }
                }else{
                    mapper.entries = emptyList()
                    mapper.message = pullFromRemoteRepo.msg!!
                    mapper.status = pullFromRemoteRepo.status!!
                }

                _salesResponseState.value = NetworkResult.Success(mapper)
            }
        } catch (e: Throwable) {
            _salesResponseState.value = NetworkResult.Error(e)
        }
    }

    private val _closeOutletResponseState = MutableStateFlow<NetworkResult<PostSalesResponse>>(NetworkResult.Empty)
    val closeOutletResponseState get() = _closeOutletResponseState

    @SuppressLint("SimpleDateFormat")
    fun fetchAllSalesEntries(salesRecord: IsParcelable) = viewModelScope.launch {
        _closeOutletResponseState.value = NetworkResult.Loading
        try {
            val isResponseModel = OrderPosted()

            isResponseModel.uiid = salesRecord.uii
            isResponseModel.clat = salesRecord.latitude!!.toString()
            isResponseModel.clng = salesRecord.longitude!!.toString()
            isResponseModel.etime = SimpleDateFormat("HH:mm:ss").format(Date())
            isResponseModel.edate = salesRecord.entry_date
            isResponseModel.customerno = salesRecord.data!!.customerno
            isResponseModel.employee_id = salesRecord.data!!.employee_id
            isResponseModel.urno = salesRecord.data!!.urno
            isResponseModel.outletlatitude = salesRecord.data!!.latitude
            isResponseModel.outletlongitude = salesRecord.data!!.longitude
            isResponseModel.outletname = salesRecord.data!!.outletname
            isResponseModel.volumeclass = salesRecord.data!!.volumeclass
            isResponseModel.distance = salesRecord.data!!.distance
            isResponseModel.duration = salesRecord.data!!.duration
            isResponseModel.token = "000000"
            isResponseModel.remark = salesRecord.remark
            isResponseModel.atime = salesRecord.atime
            isResponseModel.order = emptyList()

            val data = repo.postSales(isResponseModel)
            _closeOutletResponseState.value = NetworkResult.Success(data)

        } catch (e: Throwable) {
            _closeOutletResponseState.value = NetworkResult.Error(e)
        }
    }

    fun sentToken(urno:Int) = viewModelScope.launch {
        try {
            repo.sendTokenToday(urno)
        } catch (e: Throwable) {

        }
    }

    //local outlet update
    private val _localOutletUpdateState = MutableStateFlow<NetworkResult<OutletAsyn>>(NetworkResult.Empty)
    val localOutletUpdateState get() = _localOutletUpdateState

    fun localOutletUpdate(urno: Int) = viewModelScope.launch {
        _localOutletUpdateState.value = NetworkResult.Loading
        try {

            val iscustomer = repo.CustomerInfoAsync(urno)
            repo.updateIndividualCustomer(iscustomer.outletclassid, iscustomer.outletlanguageid, iscustomer.outlettypeid, iscustomer.outletname, iscustomer.outletaddress,
                iscustomer.contactname, iscustomer.contactphone, iscustomer.latitude.toDouble(), iscustomer.longitude.toDouble(), urno )

        _localOutletUpdateState.value = NetworkResult.Success(iscustomer)

        }catch (e: Throwable) {
            _localOutletUpdateState.value = NetworkResult.Error(e)
        }
    }

    private val _messageResponseState =
        MutableStateFlow<NetworkResult<NotificationAndMessage>>(NetworkResult.Empty)
    val messageResponseState get() = _messageResponseState

    fun isMessageAccuracy(customerCode: String, entriesDate: String) = viewModelScope.launch {
        _messageResponseState.value = NetworkResult.Loading
        try {

            val isDataAccuracy = repo.getDataAccuracy()
            val entryDate = isDataAccuracy.filter { it.entry_date.equals(entriesDate) }

            val filterReadFromUnreadMessages = isDataAccuracy.count {
                it.status == 1
            }

            if (entryDate.isNotEmpty()) {

                val notificationAndMessage = NotificationAndMessage(
                    filterReadFromUnreadMessages,isDataAccuracy
                )
                _messageResponseState.value = NetworkResult.Success(notificationAndMessage)
            } else {

                val isData = repo.dataAccuracy(customerCode)

                if (isData.status == 200 || isData.accuracy!!.isNotEmpty()) {

                    val result = isData.accuracy!!.map { it.toAccuracyEntity() }
                    repo.isCurrentMessage(result) //save in local Repository
                    val isAccuracyMessage = repo.getDataAccuracy()

                    val filterReadFromUnreadMessage = isAccuracyMessage.count {
                        it.status == 1
                    }

                    val notificationAndMessage = NotificationAndMessage(
                        filterReadFromUnreadMessage, isAccuracyMessage
                    )

                    _messageResponseState.value = NetworkResult.Success(notificationAndMessage)
                } else {
                    val notificationAndMessage = NotificationAndMessage(
                        filterReadFromUnreadMessages,isDataAccuracy
                    )
                    _messageResponseState.value = NetworkResult.Success(notificationAndMessage)
                }
            }
        } catch (e: Throwable) {
            _messageResponseState.value = NetworkResult.Error(e)
        }
    }



}
