package com.mobile.mobiletradermtx.ui.order

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.mobiletradermtx.dto.CustomerProductOrder
import com.mobile.mobiletradermtx.dto.RealOrder
import com.mobile.mobiletradermtx.dto.SkuOrdered
import com.mobile.mobiletradermtx.ui.order.repository.OrderRepo
import com.mobile.mobiletradermtx.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OrderViewModel @ViewModelInject constructor(private val repo: OrderRepo): ViewModel() {


    //List of all order
    private val _orderResponseState = MutableStateFlow<NetworkResult<CustomerProductOrder>>(NetworkResult.Empty)
    val orderResponseState get() = _orderResponseState

    fun fetchAllSalesEntries(employeeid: Int) = viewModelScope.launch {
        _orderResponseState.value = NetworkResult.Loading
        try {
            _orderResponseState.value = NetworkResult.Success(repo.customerOrder(employeeid))
        }catch (e: Throwable) {
            _orderResponseState.value = NetworkResult.Error(e)
        }
    }

    //list of all sku ordered
    private val _skuOrderedResponseState = MutableStateFlow<NetworkResult<SkuOrdered>>(NetworkResult.Empty)
    val skuOrderedResponseState get() = _skuOrderedResponseState

    fun isSkuOrdered(orderid: Int) = viewModelScope.launch {
        _skuOrderedResponseState.value = NetworkResult.Loading
        try {
            _skuOrderedResponseState.value = NetworkResult.Success(repo.skuTotalOrdered(orderid))
        }catch (e: Throwable) {
            _skuOrderedResponseState.value = NetworkResult.Error(e)
        }
    }

    //post item ordered for
    private val _makeAnOrderResponseState = MutableStateFlow<NetworkResult<RealOrder>>(NetworkResult.Empty)
    val makeAnOrderResponseState get() = _makeAnOrderResponseState

    fun isOrder(employeeid: Int, orderid: Int) = viewModelScope.launch {
        _makeAnOrderResponseState.value = NetworkResult.Loading
        try {
            _makeAnOrderResponseState.value = NetworkResult.Success(repo.orderProduct(employeeid, orderid))
        }catch (e: Throwable) {
            _makeAnOrderResponseState.value = NetworkResult.Error(e)
        }
    }
}