package com.atilsamancioglu.artbookhilttesting

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/* Copyright 2019 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */

/**
 * liveData가 값을 갖고 있다면 즉시 반환하고
 * 새로운 값을 받을 때까지 liveData를 observe 한다
 * 이 때 설정한 시간(time)만큼 observe 하며
 * 해당 시간이 지나면 exception이 발생한다
 * */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
