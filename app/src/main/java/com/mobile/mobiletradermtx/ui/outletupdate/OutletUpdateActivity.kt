package com.mobile.mobiletradermtx.ui.outletupdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.mobiletradermtx.databinding.ActivityOutletUpdateBinding


class OutletUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOutletUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutletUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}