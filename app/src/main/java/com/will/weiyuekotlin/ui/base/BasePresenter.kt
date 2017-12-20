package com.will.weiyuekotlin.ui.base


/**
 * desc: .
 * author: Will .
 * date: 2017/11/27 .
 */
abstract class BasePresenter<T> : BaseContract.BasePresenter {

    protected var mView: T? = null

    /**
     * 绑定 view
     */
    override fun attachView(view: BaseContract.BaseView) {
        this.mView = view as T
    }

    /**
     * 解绑 view
     */
    override fun detachView() {
        mView?.let {
            mView = null
        }
    }
}