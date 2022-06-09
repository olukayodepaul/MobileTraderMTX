package com.mobile.mobiletradermtx.ui.orderpurchase.repository

import com.mobile.mobiletradermtx.dto.OrderParentList


interface OrderPurchaseRepo {
    suspend fun isNetworkHelper(): Boolean
    suspend fun isSalesEntry(employee_id: Int, urno: Int): OrderParentList
}