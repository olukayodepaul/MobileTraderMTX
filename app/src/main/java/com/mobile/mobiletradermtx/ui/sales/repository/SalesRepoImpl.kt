package com.mobile.mobiletradermtx.ui.sales.repository

import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.datasource.RetrofitServices
import com.mobile.mobiletradermtx.dto.*


class SalesRepoImpl (
    private val retrofitClient: RetrofitService,
    private val appdoa: AppDao,
    private val retrofitService: RetrofitServices): SalesRepo {

    override suspend fun getCustomer(employee_id: Int): CustomersResponse {
        return retrofitClient.getCustomers(employee_id)
    }

    override suspend fun getCustomers(): List<CustomersList> {
        return appdoa.getCustomers()
    }

    override suspend fun addCustomers(cust: List<CustomersList>) {
        return appdoa.addCustomers(cust)
    }

    override suspend fun postSales(order: OrderPosted): PostSalesResponse {
        return retrofitClient.postSales(order)
    }

    override suspend fun sendTokenToday(unro: Int): SendTokenToIndividualCustomer {
        return  retrofitService.sendTokenToday(unro)
    }

    override suspend fun CustomerInfoAsync(urno: Int): OutletAsyn {
        return retrofitService.isCustomerInfoAsync(urno)
    }

    override suspend fun updateIndividualCustomer(
        outletclassid: Int,
        outletlanguageid: Int,
        outlettypeid: Int,
        outletname: String,
        outletaddress: String,
        contactname: String,
        contactphone: String,
        latitude: Double,
        longitude: Double,
        urno: Int
    ) {
        return appdoa.updateIndividualCustomer(outletclassid, outletlanguageid, outlettypeid, outletname, outletaddress, contactname, contactphone, latitude, longitude, urno)
    }

    override suspend fun isCurrentMessage(msg: List<EntityAccuracy>) {
        return appdoa.isCurrentMessage(msg)
    }

    override suspend fun getDataAccuracy(): List<EntityAccuracy> {
        return appdoa.getDataAccuracy()
    }

    override suspend fun dataAccuracy(customercode: String): DataAccuracy {
        return retrofitClient.dataAccuracy(customercode)
    }



}