package com.mobile.mobiletradermtx.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

fun Customers.toSalesEntry() : CustomersList {
    return CustomersList(
        auto, dates, timeago, _id, employee_id, urno, customerno, outletname, latitude, longitude, sort, notice,
        outlet_waiver, cust_token, defaulttoken, seq, modes, rep_id, outletclassid, outletlanguageid, outlettypeid,
        outletaddress, contactphone, contactname, outlet_pic, repname, volumeclass, employee_code,
        customer_code, distance, duration, depotwaivers, spec, __v, th
    )
}


fun CustomersList.toCustomers() : Customers {
    return Customers(
        auto, dates, timeago, _id, employee_id, urno, customerno, outletname, latitude, longitude, sort, notice,
        outlet_waiver, cust_token, defaulttoken, seq, modes, rep_id, outletclassid, outletlanguageid, outlettypeid,
        outletaddress, contactphone, contactname, outlet_pic, repname, volumeclass, employee_code,
        customer_code, distance, duration, depotwaivers, spec, __v, th
    )
}

fun BasketLimit.toBasketLimit() : BasketLimitList {
    return BasketLimitList(
        auto, employee_id, product_id, product_code, qty, soq, order_sold, price, product_name, seperator, seperatorname,
        dates, pricing, inventory, orders, entry_time, controlpricing, controlinventory, controlorder,blimit
    )
}

fun BasketLimitList.toBasketToApi() : BasketLimit {
    return BasketLimit(
        auto, employee_id, product_id, product_code, qty, soq, order_sold, price, product_name, seperator, seperatorname,
        dates, pricing, inventory, orders, entry_time, controlpricing, controlinventory, controlorder,blimit
    )
}

data class IsAllCustomers(
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("entries")
    var entries: List<CustomersList>? = null
)

data class SalesEntryMapperInterface(
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("data")
    var data: List<BasketLimitList>? = null
)

@Parcelize
data class IsParcelable(
    var latitude: String? = null,
    var longitude: String? = null,
    var entry_time: String? = null,
    var entry_date: String? = null,
    var uii: String? = null,
    var remark: String? = null,
    var data:Customers? = null,
    var atime:String? = null
): Parcelable

fun UserSpinner.toSpinners() : UserSpinnerEntity {
    return UserSpinnerEntity(
        auto, id, name, sep
    )
}

data class SpinnerInterface(
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("data")
    var data: List<UserSpinnerEntity>? = null
)

fun MoneyAgent.toIsMoneyAgents() : IsMoneyAgent {
    return IsMoneyAgent(
        id, route_id, agenttype, agennama, phone,  address, lat, lng, distance, employee_id
    )
}

fun DataAccuracy.DataAccuracyConditions.toAccuracyEntity() : EntityAccuracy {
    return EntityAccuracy(
       id, _id, entry_date, remark, status
    )
}









