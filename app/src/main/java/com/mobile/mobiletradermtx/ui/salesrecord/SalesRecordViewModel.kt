package com.mobile.mobiletradermtx.ui.salesrecord

import android.annotation.SuppressLint
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.mobiletradermtx.dto.*
import com.mobile.mobiletradermtx.ui.salesrecord.repository.SalesRecordRepo
import com.mobile.mobiletradermtx.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SalesRecordViewModel @ViewModelInject constructor(private val repo: SalesRecordRepo) :ViewModel() {

    private val _salesRecordResponseState = MutableStateFlow<NetworkResult<List<BasketLimitList>>>(NetworkResult.Empty)
    val salesRecordResponseState get() = _salesRecordResponseState

    fun fetchSalesRecordEntries() = viewModelScope.launch {
        _salesRecordResponseState.value = NetworkResult.Loading
        try {
            val data = repo.fetchBasketFromLocalRep()
            _salesRecordResponseState.value = NetworkResult.Success(data)
        } catch (e: Throwable) {
            _salesRecordResponseState.value = NetworkResult.Error(e)
        }
    }


    private val _tokenResponseState = MutableStateFlow<NetworkResult<GeneralResponse>>(NetworkResult.Empty)
    val tokenResponseState get() = _tokenResponseState

    fun tokenRecordEntries( urno: Int, employee_id: Int, curlocation: String, region: Int) = viewModelScope.launch {
        _tokenResponseState.value = NetworkResult.Loading
        try {
            val data = repo.sendTokenToday(urno, employee_id, curlocation, region)
            _tokenResponseState.value = NetworkResult.Success(data)
        } catch (e: Throwable) {
            _tokenResponseState.value = NetworkResult.Error(e)
        }
    }

    private val _postSalesResponseState = MutableStateFlow<NetworkResult<PostSalesResponse>>(NetworkResult.Empty)
    val postSalesResponseState get() = _postSalesResponseState

    @SuppressLint("SimpleDateFormat")
    fun postSalesToServer(salesRecord: IsParcelable, tokeUsed: String ) = viewModelScope.launch {

        _postSalesResponseState.value = NetworkResult.Loading

        try {

            val isLocalOrder =  repo.salesPosted()
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
            isResponseModel.remark = salesRecord.remark
            isResponseModel.distance = salesRecord.data!!.distance
            isResponseModel.duration = salesRecord.data!!.duration
            isResponseModel.token = tokeUsed
            isResponseModel.atime = salesRecord.atime
            isResponseModel.order = isLocalOrder.map { it.toBasketToApi() }

            val httpResponse = repo.postSales(isResponseModel)

            val compilerObject = PostSalesResponse()

            if(httpResponse.status==200){

                val limitToSalesEntry =  isLocalOrder.filter {
                    it.seperator.equals("1")
                }

                limitToSalesEntry.forEach {
                    repo.resetOrders(it.auto)
                }

                repo.setVisitTime(salesRecord.entry_time!!, salesRecord.data!!.urno!!)
                compilerObject.status = httpResponse.status
                compilerObject.msg = httpResponse.msg

            }else{
                compilerObject.status = httpResponse.status
                compilerObject.msg = httpResponse.msg
            }

            _postSalesResponseState.value = NetworkResult.Success(compilerObject)

        } catch (e: Throwable) {
            _postSalesResponseState.value = NetworkResult.Error(e)
        }
    }

}