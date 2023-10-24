package com.app.gettingstartedwithandroiddevlopment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class AsyncAndWait : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_and_wait)

        val TAG="AsyncAndWait"

        // Jobs inside the Coroutine scope will be executed sequentially
        // lets see an sample for the sequential execution of the jobs
        GlobalScope.launch(Dispatchers.IO){
            var time= measureTimeMillis {
                var job1=networkCall1()
                var job2=networkCall2()
                Log.d(TAG,"Answer is $job1")
                Log.d(TAG,"Answer is $job2")
            }
            Log.d(TAG,"Request Took $time")
        }
        Log.d(TAG,"Coroutine Inside of Coroutine to do the job parallel")

        // Here the job will be executed in a  parallel manner
        // Because here we are using coroutine scope inside of another coroutine scope
        // that makes the job to be executed parallel
        GlobalScope.launch(Dispatchers.IO) {
            var time2= measureTimeMillis {
                var answer1:String?=null
                var answer2:String?=null
                var job3=launch{
                    answer1=networkCall1()
                }
                var job4=launch{
                    answer2=networkCall2()
                }
                // join function is used here to make the log statement wait until the job is done
                // or it will print the answer to the call as null
                job3.join()
                job4.join()
                Log.d(TAG,"Answer is $answer1")
                Log.d(TAG,"Answer is $answer2")
            }
            Log.d(TAG,"Request Took with async $time2")
        }

        // The Above method we follow to return the value using launch scope is not bad
        // but the best practice is to use the async and await
        // async will does the job of creating a new coroutine scope and return the value as Deferred
        // so it is recommended to use it whenever we are trying to return value from the coroutine
        GlobalScope.launch {
            // async method creates a new coroutine and where the await makes the coroutine wait until it gets the answer
            val answer1=async { networkCall1() }
            val answer2=async { networkCall2() }
            Log.d(TAG,"Answer is ${answer1.await()}")
            Log.d(TAG,"Answer is ${answer2.await()}")
        }
    }
    // Creating the suspend functions for the Async sample
    suspend fun networkCall1():String{
        delay(3000L)
        return "Answer for Call1"
    }
    suspend fun networkCall2():String{
        delay(3000L)
        return "Answer for Call2"
    }
}