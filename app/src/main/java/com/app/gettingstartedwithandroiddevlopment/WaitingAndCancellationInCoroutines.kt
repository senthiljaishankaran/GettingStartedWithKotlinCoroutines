package com.app.gettingstartedwithandroiddevlopment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

class WaitingAndCancellationInCoroutines : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_and_cancellation_in_coroutines)

        val TAG="WaitingAndCancellationInCoroutines"

        // waiting for a coroutine to finish its job is or stopping the thread untill a coroutine finishes its job is done
        // by using the join() function it it makes the thread to wait for the given job to be finished
        // sample for join function
        // Here we create a coroutine and assigning it to a variable
        // And we use the run Blocking coroutine scope to use the coroutine with join function making the thread to wait till
        // the job is completed
        var job= GlobalScope.launch(Dispatchers.Default) {
            repeat(5) {
                Log.d(TAG, "Coroutine is Working....")
                delay(1000L)
            }
        }

        // We are using the run blocking to block the main thread because run blocking coroutine scope is the only scope
        // that blocks the thread using the suspend function
        runBlocking {
            job.join()
            Log.d(TAG,"Main thread is continuing...")
        }

        // Cancelling a Job cancel function is used to cancel the job when it is running
        // since this is also a suspend type function we have to use the run blocking scope
        var job2=GlobalScope.launch(Dispatchers.Default) {
            repeat(5) {
                Log.d(TAG, "Coroutine is Working....")
                delay(1000L)
            }
        }
        // using run block to cancel the job after an delay fo two seconds
        // here the log will have two logs from the job and then the cancel suspend function stop the job
        runBlocking {
            delay(2000L)
            job2.cancel()
            Log.d(TAG,"Job is Cancelled...")
        }

        // cancel job is a co-operative function
        // which means the job it is suspending must have a delay or wait time so that the suspend can take
        // effort on the cancel function that is being implemented in the job above we have a delay function
        // lets see sample example of job that dont have the delay function
        // lets have a sample recursive fibonacci series to take as an example

        var job3=GlobalScope.launch(Dispatchers.Default){
            Log.d(TAG,"Starting the Complex Calculation....")
            for(item in 30..40){
                // if this if condition is not there then the coroutine would not have stopped working
                // it checks the condition of active for coroutine for each for loop condition
                // if it is not active it will stop the calculation process
                if(isActive){
                    Log.d(TAG,"Result for $item is :${fib(item)}")
                }
            }
            Log.d(TAG,"Ending the Complex Calculation....")
        }

        // Here the job would keep on going even after cancel function is called in the program
        // this is because the coroutine does not have enough time to recognise or adopt cancel function
        // using a if statement to check the active status of the coroutine might clear the problem in above condition
        // lets implement the if function to check active status of the coroutine
        runBlocking {
            delay(2000L)
            job3.cancel()
            Log.d(TAG,"Job has been Cancelled")
        }

        // there is another suspend function called with TimeOut it will stop the coroutine after the specified time
        // regardless what ever the job is carried out by the coroutine the with time out function wil stop the job
        // It will be perfect replacement for the run blocking function and we can use it within the global scope itself
        var job4=GlobalScope.launch(Dispatchers.Default){
                Log.d(TAG,"Starting the Complex Calculation....")
                withTimeout(5000L){
                    for(item in 30..40){
                        if(isActive){
                            Log.d(TAG,"Result for $item is :${fib(item)}")
                        }
                    }
                }
            Log.d(TAG,"Ending the Complex Calculation....")
        }
    }
    fun fib(n:Int):Long {
        return if(n==0)0
        else if(n==1)1
        else fib(n-1)+fib(n-2)
    }
}