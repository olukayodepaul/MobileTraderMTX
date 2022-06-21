package com.mobile.mobiletradermtx.ui.order.repository

import com.mobile.mobiletradermtx.dto.*


interface OrderRepo {
    suspend fun customerOrder(employeeid: Int): CustomerProductOrder
    suspend fun  skuTotalOrdered(orderid: Int): SkuOrdered
    suspend fun orderProduct(employeeid: Int, orderid: Int): RealOrder
    suspend fun isCurrentMessage(msg: List<EntityAccuracy>)
    suspend fun getDataAccuracy() : List<EntityAccuracy>
    suspend fun dataAccuracy(customercode: String) : DataAccuracy
}