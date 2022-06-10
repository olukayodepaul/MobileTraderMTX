package com.mobile.mobiletradermtx.ui.salesentry

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.mobiletradermtx.dto.SalesEntryMapperInterface
import com.mobile.mobiletradermtx.dto.toBasketLimit
import com.mobile.mobiletradermtx.ui.salesentry.repository.SalesEntryRepo
import com.mobile.mobiletradermtx.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SalesEntryViewModel @ViewModelInject constructor(private val repo: SalesEntryRepo) :
    ViewModel() {

    private val _basketResponseState = MutableStateFlow<NetworkResult<SalesEntryMapperInterface>>(
        NetworkResult.Empty
    )
    val basketResponseState get() = _basketResponseState

    fun isUserDailyBaskets(employee_id: Int, sysdate: String, curDate: String, urno:Int) =
        viewModelScope.launch {

            _basketResponseState.value = NetworkResult.Loading

            try {

                val dailyBasket = repo.fetchBasketFromLocalRep()

                val mapper = SalesEntryMapperInterface()

                if (sysdate == curDate && dailyBasket.isNotEmpty()) {

                    mapper.status = 200
                    mapper.message = ""
                    mapper.data = dailyBasket
                    _basketResponseState.value = NetworkResult.Success(mapper)

                } else {

                    val remoteData = repo.fetchBasketFromRemoteRep(employee_id)

                    val limitToSalesEntry = remoteData.basketlimit!!.filter { filters ->
                        filters.seperator.equals("1")
                    }

                    if (remoteData.status == 200 && limitToSalesEntry.isNotEmpty()) {
                        val isMapData = remoteData.basketlimit!!.map { it.toBasketLimit() }
                        mapper.status = remoteData.status!!
                        mapper.message = remoteData.msg!!
                        mapper.data = isMapData
                        repo.setBasket(isMapData)
                    } else {
                        mapper.message = "Basket Not Assign"
                        mapper.status = 400
                        mapper.data = emptyList()
                    }

                    //from here you can update the soq before presenting the data to the view.
                    if(mapper.data!!.isNotEmpty()) {
                        val castSoqToLocalData = repo.soqPrediction(urno)
                        if(castSoqToLocalData.status==200 && castSoqToLocalData.soq!!.isNotEmpty()){
                            for(x in castSoqToLocalData.soq!!) {
                                repo.updateCastedSoq(x.soq!!, x.skucode!!)
                            }
                        }
                    }

                    _basketResponseState.value = NetworkResult.Success(mapper)
                }

            } catch (e: Throwable) {
                _basketResponseState.value = NetworkResult.Error(e)
            }
        }

    fun updateDailySales(
        inventory: Double,
        pricing: Int,
        order: Double,
        entry_time: String,
        controlpricing: Int,
        controlinventory: Int,
        controlorder: Int,
        auto: Int
    ) = viewModelScope.launch {
        repo.updateDailySales(inventory, pricing, order, entry_time, controlpricing, controlinventory, controlorder, auto)
    }

    private val _validateSalesEntryResponseState = MutableStateFlow<NetworkResult<Int>>(NetworkResult.Empty)
    val validateSalesEntryResponseState get() = _validateSalesEntryResponseState

    fun validateSalesEntries() = viewModelScope.launch {
        _validateSalesEntryResponseState.value = NetworkResult.Loading
        try {
            val data = repo.validateSalesEntry()
            _validateSalesEntryResponseState.value = NetworkResult.Success(data)
        } catch (e: Throwable) {
            _validateSalesEntryResponseState.value = NetworkResult.Error(e)
        }
    }

}