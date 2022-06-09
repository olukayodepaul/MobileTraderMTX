package com.mobile.mobiletradermtx.util

import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nex3z.notificationbadge.NotificationBadge

object FirebaseDatabases {

    fun setOrderBadge(employeeId:Int? = null, db: FirebaseDatabase? = null, notificationBadge: NotificationBadge? = null ) {
        val references = db!!.getReference("/message/customer/$employeeId")
        references.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    notificationBadge!!.isVisible = true
                    notificationBadge.setText(p0.childrenCount.toString())
                }else{
                    notificationBadge!!.isVisible = false
                }
            }
        })
    }

}