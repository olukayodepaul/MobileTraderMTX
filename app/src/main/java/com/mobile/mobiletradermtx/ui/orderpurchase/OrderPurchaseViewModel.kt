package com.mobile.mobiletradermtx.ui.orderpurchase

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.mobiletradermtx.dto.OrderParentList
import com.mobile.mobiletradermtx.ui.orderpurchase.repository.OrderPurchaseRepo
import com.mobile.mobiletradermtx.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OrderPurchaseViewModel  @ViewModelInject constructor(private val repo : OrderPurchaseRepo): ViewModel() {

    private val _orderEntryResponseState = MutableStateFlow<NetworkResult<OrderParentList>>(NetworkResult.Empty)
    val orderEntryResponseState get() = _orderEntryResponseState

    fun isSalesEntries(employee_id: Int, urno: Int) = viewModelScope.launch {
        _orderEntryResponseState.value = NetworkResult.Loading
        try {
            val data = repo.isSalesEntry(employee_id, urno)

            if(repo.isNetworkHelper()) {
                _orderEntryResponseState.value = NetworkResult.Success(data)
            }else{
                _orderEntryResponseState.value = NetworkResult.Error(Throwable("Please check your WIFI, CELLULAR Data and ETHERNET"))
            }

        } catch (e: Throwable) {
            _orderEntryResponseState.value = NetworkResult.Error(e)
        }
    }
}