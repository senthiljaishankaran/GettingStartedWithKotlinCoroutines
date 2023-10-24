package com.app.gettingstartedwithandroiddevlopment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LifeCycleScopeAndViewModelScope : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle_scope_and_view_model_scope)

        val TAG = "LifeCycleScopeAndViewModelScope"
        val button: Button = findViewById(R.id.startActivityButton)

        // Global scope coroutines will have lifecycle of the Application
        // ie it will be destroyed only at time destroyin of the application
        // this might lead to memory leaks
        // as the activity the coroutine related with is destroyed then the coroutine must be destroyed too
        // or else the resource used by the activity will be in turn used by the coroutine which cannot be Garbage collected
        // this will lead it to the memory leaks so we should use the lifeCycleScope

        button.setOnClickListener {
            // this life cycle scope is similar when it is applied to fragments other than activities
            // view model scope is also similar to life cycle scope as it will live till the view model is not destroyed
            lifecycleScope.launch {
                while (true) {
                    // though it is a while with infinite loop once the activity is terminated this also terminated
                    // as it defined in life cycle scope
                    delay(1000L)
                    Log.d(TAG, "Still the coroutine is running....")
                }
            }
            // life cycle scope will be destroyed once the activity it is created in is destroyed
            GlobalScope.launch() {
                delay(5000L)
                Intent(
                    this@LifeCycleScopeAndViewModelScope,
                    SecondActivityForLifeCycleScope::class.java
                ).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }
}