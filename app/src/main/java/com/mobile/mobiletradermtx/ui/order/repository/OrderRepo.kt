package com.mobile.mobiletradermtx.ui.order.repository

import com.mobile.mobiletradermtx.dto.CustomerProductOrder
import com.mobile.mobiletradermtx.dto.RealOrder
import com.mobile.mobiletradermtx.dto.SkuOrdered


interface OrderRepo {
    suspend fun customerOrder(employeeid: Int): CustomerProductOrder
    suspend fun  skuTotalOrdered(orderid: Int): SkuOrdered
    suspend fun orderProduct(employeeid: Int, orderid: Int): RealOrder
}