package com.mobile.mobiletradermtx.ui.salesrecord.repository

import com.mobile.mobiletradermtx.dto.BasketLimitList
import com.mobile.mobiletradermtx.dto.GeneralResponse
import com.mobile.mobiletradermtx.dto.OrderPosted
import com.mobile.mobiletradermtx.dto.PostSalesResponse



interface SalesRecordRepo {
    suspend fun fetchBasketFromLocalRep() : List<BasketLimitList>
    suspend fun postSales(order: OrderPosted): PostSalesResponse
    suspend fun salesPosted(): List<BasketLimitList>
    suspend fun resetOrders(auto: Int)
    suspend fun setVisitTime(timeago:String, urno:Int )
    suspend fun sendTokenToday(urno: Int, employee_id: Int, curlocation: String, region: Int): GeneralResponse
}

