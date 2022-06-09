package com.mobile.mobiletradermtx.ui.sales.repository

import com.mobile.mobiletradermtx.dto.*


interface SalesRepo {
    suspend fun getCustomer(employee_id: Int) : CustomersResponse
    suspend fun getCustomers() : List<CustomersList>
    suspend fun addCustomers(cust: List<CustomersList>)
    suspend fun postSales(order: OrderPosted): PostSalesResponse
    suspend fun sendTokenToday(unro: Int): SendTokenToIndividualCustomer
    suspend fun CustomerInfoAsync(urno: Int): OutletAsyn
    suspend fun updateIndividualCustomer(outletclassid:Int, outletlanguageid:Int, outlettypeid:Int, outletname:String, outletaddress:String, contactname:String, contactphone:String, latitude:Double, longitude:Double,urno:Int)

}