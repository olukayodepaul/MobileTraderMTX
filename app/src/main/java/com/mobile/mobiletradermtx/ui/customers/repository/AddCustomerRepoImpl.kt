package com.mobile.mobiletradermtx.ui.customers.repository

import com.mobile.mobiletradermtx.datasource.AppDao
import com.mobile.mobiletradermtx.datasource.RetrofitService
import com.mobile.mobiletradermtx.datasource.RetrofitServices
import com.mobile.mobiletradermtx.dto.OutletUpdateResponse
import com.mobile.mobiletradermtx.dto.UserSpinnerEntity
import com.mobile.mobiletradermtx.dto.UserSpinnerResponse


class AddCustomerRepoImpl(
    private val retrofitClient: RetrofitService,
    private val appdoa: AppDao,
    private val retrofitServices: RetrofitServices

): AddCustomerRep {

    override suspend fun fetchSpinners(): UserSpinnerResponse {
        return retrofitClient.fetchSpinners()
    }

    override suspend fun fetchSpinnerFromLocalDb(): List<UserSpinnerEntity> {
        return appdoa.fetchSpinnerFromLocalDb()
    }

    override suspend fun addCustomer(cust: List<UserSpinnerEntity>) {
        return appdoa.addCustomer(cust)
    }

    override suspend fun createCustomers(
        tmid:Int, rep:Int, latitude:Double, longitude:Double, outletname:String, contactname:String, outletaddress:String, contactphone:String,
        outletclassid:Int, outletlanguage:Int, outlettypeid:Int
    ) : OutletUpdateResponse {
        return retrofitServices.mapOutlet(tmid, tmid, latitude, longitude, outletname, contactname, outletaddress, contactphone, outletclassid, outletlanguage, outlettypeid)
    }

    override suspend fun updateOutlet(
        tmid: Int, urno: Int, latitude: Double, longitude: Double, outletname: String, contactname: String,
        outletaddress: String, contactphone: String, outletclassid: Int, outletlanguage: Int,
        outlettypeid: Int
    ): OutletUpdateResponse {
        return retrofitServices.updateOutlet(tmid,urno,latitude,longitude,outletname,contactname,outletaddress,contactphone,outletclassid,outletlanguage,outlettypeid)
    }

}