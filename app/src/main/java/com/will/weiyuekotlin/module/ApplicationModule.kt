package com.will.weiyuekotlin.module

import android.content.Context
import com.will.weiyuekotlin.MyApp

import dagger.Module
import dagger.Provides

/**
 * desc:
 * author: Will .
 * date: 2017/9/2 .
 */
@Module
class ApplicationModule(private val mContext: Context) {

    @Provides
    internal fun provideApplication(): MyApp {
        return mContext.applicationContext as MyApp
    }

    @Provides
    internal fun provideContext(): Context {
        return mContext
    }
}
