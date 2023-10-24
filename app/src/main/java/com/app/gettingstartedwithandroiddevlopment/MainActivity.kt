package com.app.gettingstartedwithandroiddevlopment

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.gettingstartedwithandroiddevlopment.ui.theme.GettingStartedWithAndroidDevlopmentTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


// Coroutine is similar to thread that is use execute the process or program as instructed
// only thing is coroutine runs inside the thread
// A thread can have n number of coroutines
// consider number of workers in construction site
// construction site is like a thread and the worker in it is like the coroutines
// blocking the thread will make all the coroutines in it to be blocked too
// but delaying the coroutine has no impact on the the thread it is running on


// Advantages of Coroutine
// Can be suspended then and there when we wanted
// Switching the context on which it runs is possible ie coroutine running in thread 1 can be switched to thread 2 or others

class MainActivity : AppCompatActivity() {
    val TAG="MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // GlobalScope is the simplest way  to start a coroutine but not the best way
        // because global scope provides the coroutine the life span until the application or the main thread is finished
        // which is not good the worker threads should be destroyed once their work is finished
        // Following the above thing will make our app that is working better even with limited resource
        GlobalScope.launch {
            // Log.d with Tag is used to log message in the LogCat
            // here we are printing the current thread in the coroutine
            // in thread we will use Thread.sleep to block the thread
            // but in coroutine we use delay func it block only the coroutine and not the entire thread
            delay(3000L)
            Log.d(TAG,"Couroutine from Thread ${Thread.currentThread().name}")

            // the time taken for making this two network call will sum-up of two network call delay as they used in the same coroutine
            val makingNetworkCall1=doNetworkCall1()
            val makingNetworkCall2=doNetworkCall2()
            Log.d(TAG,makingNetworkCall1)
            Log.d(TAG,makingNetworkCall2)
        }
        // here we printing from the main thread
        // this will clearly shows the difference between the coroutine and main thread
        // Important note to remember is when ever a main is destroyed then all the other sub threads and coroutines
        // of not only the main but coroutines of all the other thread as well as thread is destroyed
        // even though their work is not completed
        Log.d(TAG,"Main Thread ${Thread.currentThread().name}")

        // Coroutine Context
        // Coroutines are always started with context and the context describes on which thread our coroutine will be started
        // above we use globalScope.launch lets see the context in detail
        // we can pass Dispatchers.context as a parameter in the launch
        // there are four context in coroutine
        // Dispatchers.Main context will create coroutine in the main thread
        // Main is thread is only thread that is allowed to make changes in the UI
        //GlobalScope.launch(Dispatchers.Main){}
        // Dispatchers.IO context will create coroutine in the IO thread
        // It is used to make Network class and Database calls
        // lets use the below network call in IO dispatcher and try to change the context to main thread to change UI
        // This solves the problem that making change in UI can only be done main thread
        GlobalScope.launch(Dispatchers.IO){
            val newText=doNetworkCall1()
            val textView:TextView=findViewById(R.id.textView)
            withContext(Dispatchers.Main){
                textView.text=newText
            }
        }
        // Dispatchers.Default context will create coroutine in the Default Thread
        // Long running operation are carried in this thread because if we run this in main thread it may get blocked and the UI might get freeze
        //GlobalScope.launch(Dispatchers.Default){}
        // Dispatchers.Unconfined context will create coroutine in the Unconfined Thread
        // The Dispatchers.Unconfined coroutine dispatcher starts a coroutine in the caller thread, but only until the first suspension point.
        // After suspension it resumes the coroutine in the thread that is fully determined by the suspending function that was invoked.
        // In short Unconfined Thread is started in Call thread after first suspension it will resume on thread decided by suspension function
        //GlobalScope.launch(Dispatchers.Unconfined){}
        // Creating a New Thread with newSingleThreadContext by specifying the Thread Name
        //GlobalScope.launch(newSingleThreadContext("Thread 1")){}

        // Run Blocking
        // when we use delay in normal coroutine scope it wont block the main thread
        // but run blocking function will start a coroutine scope by stop the thread
        // by default it will start in main thread and it runs the coroutine scope by blocking the main thread
        Log.d(TAG,"Before Run blocking")
        runBlocking {
            // run blocking scope is not asynchronous
            // but if we create a launch coroutine scope inside of run block then it will be asynchronous
            // Below two launch launch scope will run asynchronous as both operation will take delay of seconds only not 4 sec
            launch(Dispatchers.IO) {
                delay(2000L)
                Log.d(TAG,"Finished IO coroutine 1")
            }
            launch(Dispatchers.IO) {
                delay(2000L)
                Log.d(TAG,"Finished IO coroutine 2")
            }
            // since the delay over here blocks the main thread itself the it is not asynchronous
            // simply we can say that run block is the suspend function that we can call upon the main thread
            Log.d(TAG,"Starting runblocking in Thread: ${Thread.currentThread().name}")
            Log.d(TAG,"Delaying the Thread: ${Thread.currentThread().name}")
            delay(4000L)
            Log.d(TAG,"Releasing the Thread ${Thread.currentThread().name}")
        }
        Log.d(TAG,"After Run blocking")
    }

    // we can create a custom suspend function delay is also an suspend function
    // suspend function only be called inside another suspend function or coroutine
    // delay function can only be called inside a coroutine or a suspend function
    // suspend fun is created with suspend keyword before fun keyword
    // if we call this two suspend function in a same coroutine they will add up total time taken for the network to be made will be six seconds
    suspend fun doNetworkCall1():String{
        delay(3000L)
        return "Network call 1 made"
    }
    suspend fun doNetworkCall2():String{
        delay(3000L)
        return "Network call 2 made"
    }

}
