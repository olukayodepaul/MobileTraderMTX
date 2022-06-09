package com.mobile.mobiletradermtx.ui.customers.repository

import com.mobile.mobiletradermtx.dto.OutletUpdateResponse
import com.mobile.mobiletradermtx.dto.UserSpinnerEntity
import com.mobile.mobiletradermtx.dto.UserSpinnerResponse


interface AddCustomerRep {
    suspend fun fetchSpinners(): UserSpinnerResponse
    suspend fun fetchSpinnerFromLocalDb() : List<UserSpinnerEntity>
    suspend fun addCustomer(cust: List<UserSpinnerEntity>)
    suspend fun createCustomers(
        tmid:Int, rep:Int, latitude:Double, longitude:Double, outletname:String, contactname:String, outletaddress:String, contactphone:String,
        outletclassid:Int, outletlanguage:Int, outlettypeid:Int
    ): OutletUpdateResponse

    suspend fun updateOutlet(tmid: Int, urno: Int, latitude: Double, longitude: Double, outletname: String, contactname: String,
                 outletaddress: String, contactphone: String, outletclassid: Int, outletlanguage: Int,
                 outlettypeid: Int): OutletUpdateResponse

}