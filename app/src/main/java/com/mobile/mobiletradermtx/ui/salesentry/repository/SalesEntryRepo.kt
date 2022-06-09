package com.mobile.mobiletradermtx.ui.salesentry.repository

import com.mobile.mobiletradermtx.dto.BasketLimitList
import com.mobile.mobiletradermtx.dto.BasketLimitResponse


interface SalesEntryRepo {

    suspend fun fetchBasketFromRemoteRep(employee_id: Int): BasketLimitResponse
    suspend fun setBasket(cust: List<BasketLimitList>)
    suspend fun fetchBasketFromLocalRep() : List<BasketLimitList>
    suspend fun deleteBasketFromLocalRep()
    suspend fun updateDailySales(inventory: Double, pricing: Int, order: Double, entry_time: String, controlpricing:Int, controlinventory:Int, controlorder:Int, auto:Int)
    suspend fun validateSalesEntry() : Int
    suspend fun setBasketToInitState()

}