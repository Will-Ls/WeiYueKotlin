package com.will.weiyuekotlin.net

import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable

/**
 * desc:
 * author: Will .
 * date: 2017/9/2 .
 */
abstract class BaseObserver<T> : Observer<T> {

    abstract fun onSuccess(t: T?)

    abstract fun onFail(e: Throwable)

    override fun onSubscribe(@NonNull d: Disposable) {}

    override fun onNext(@NonNull t: T) {
        onSuccess(t)
    }

    override fun onError(@NonNull e: Throwable) {
        onFail(e)
    }

    override fun onComplete() {}
}
