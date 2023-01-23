package com.atilsamancioglu.artbookhilttesting

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * 테스트에서 Hilt를 사용하기 위해선 따로 Test용 Hilt를 구성해야한다
 * */
class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        // className이 아닌 HiltTestApplication의 class name을 넣어서 반환하도록 한다
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
