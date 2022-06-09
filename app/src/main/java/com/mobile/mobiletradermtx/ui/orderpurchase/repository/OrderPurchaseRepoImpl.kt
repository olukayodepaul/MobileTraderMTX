package com.mobile.mobiletradermtx.ui.orderpurchase.repository

import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.di.NetworkHelper
import com.mobile.mobiletradermtx.dto.OrderParentList


class OrderPurchaseRepoImpl(
    private val retrofitClient: RetrofitService,
    private val appdoa: AppDao,
    private val networkHelper: NetworkHelper
) : OrderPurchaseRepo {

    override suspend fun isNetworkHelper(): Boolean {
        return networkHelper.isNetworkConnected()
    }

    override suspend fun isSalesEntry(employee_id: Int, urno: Int): OrderParentList {
        return retrofitClient.orderPurchase(employee_id, urno)
    }

}

