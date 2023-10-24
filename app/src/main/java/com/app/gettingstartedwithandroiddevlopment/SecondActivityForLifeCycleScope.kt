package com.app.gettingstartedwithandroiddevlopment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SecondActivityForLifeCycleScope : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_for_life_cycle_scope)
    }
}