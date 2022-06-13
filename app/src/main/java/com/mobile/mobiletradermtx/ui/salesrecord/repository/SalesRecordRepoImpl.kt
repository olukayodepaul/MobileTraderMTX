package com.mobile.mobiletradermtx.ui.salesrecord.repository

import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.datasource.RetrofitServices
import com.mobile.mobiletradermtx.dto.BasketLimitList
import com.mobile.mobiletradermtx.dto.GeneralResponse
import com.mobile.mobiletradermtx.dto.OrderPosted
import com.mobile.mobiletradermtx.dto.PostSalesResponse


class SalesRecordRepoImpl (

    private val retrofitClient: RetrofitService,
    private val appdoa: AppDao,
    private val retrofitServices: RetrofitServices

) : SalesRecordRepo {

    override suspend fun fetchBasketFromLocalRep(): List<BasketLimitList> {
        return appdoa.fetchBasketFromLocalRep()
    }

    override suspend fun postSales(order: OrderPosted): PostSalesResponse {
        return retrofitClient.postSales(order)
    }

    override suspend fun salesPosted(): List<BasketLimitList> {
        return appdoa.salesPosted()
    }

    override suspend fun resetOrders(auto: Int) {
        return appdoa.resetOrders(auto)
    }

    override suspend fun setVisitTime(timeago: String, urno: Int) {
        return appdoa.setVisitTime(timeago, urno)
    }

    override suspend fun sendTokenToday(
        urno: Int,
        employee_id: Int,
        curlocation: String,
        region: Int
    ): GeneralResponse {
        return retrofitServices.sendTokenToday(urno, employee_id, curlocation, region)
    }

}