package com.mobile.mobiletradermtx.ui.order.repository

import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.datasource.RetrofitServices
import com.mobile.mobiletradermtx.dto.*


class OrderRepoImpl(
    private val retrofitClient: RetrofitServices,
    private val appdoa: AppDao,
    private  val retrofitService: RetrofitService
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


    override suspend fun isCurrentMessage(msg: List<EntityAccuracy>) {
        return appdoa.isCurrentMessage(msg)
    }

    override suspend fun getDataAccuracy(): List<EntityAccuracy> {
        return appdoa.getDataAccuracy()
    }

    override suspend fun dataAccuracy(customercode: String): DataAccuracy {
        return retrofitService.dataAccuracy(customercode)
    }


}

