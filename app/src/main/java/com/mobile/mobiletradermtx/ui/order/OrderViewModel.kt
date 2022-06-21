package com.mobile.mobiletradermtx.ui.order

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.mobiletradermtx.dto.*
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