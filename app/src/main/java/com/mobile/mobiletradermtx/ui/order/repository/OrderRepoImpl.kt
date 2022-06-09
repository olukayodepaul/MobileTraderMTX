package com.mobile.mobiletradermtx.ui.order.repository

import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitServices
import com.mobile.mobiletradermtx.dto.CustomerProductOrder
import com.mobile.mobiletradermtx.dto.RealOrder
import com.mobile.mobiletradermtx.dto.SkuOrdered


class OrderRepoImpl(
    private val retrofitClient: RetrofitServices,
    private val appdoa: AppDao
) : OrderRepo {

    override suspend fun customerOrder(employeeid: Int): CustomerProductOrder {
        return retrofitClient.customerOrder(employeeid)
    }

    override suspend fun skuTotalOrdered(orderid: Int): SkuOrdered {
        return retrofitClient.skuTotalOrder(orderid)
    }

    override suspend fun orderProduct(employeeid: Int, orderid: Int): RealOrder {
        return retrofitClient.orderProduct(employeeid, orderid)
    }
    
}

