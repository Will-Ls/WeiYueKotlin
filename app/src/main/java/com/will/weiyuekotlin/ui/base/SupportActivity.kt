package com.will.weiyuekotlin.ui.base

import android.os.Bundle
import android.view.MotionEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import me.yokeyword.fragmentation.*
import me.yokeyword.fragmentation.anim.FragmentAnimator


/**
 * desc: .
 * author: Will .
 * date: 2017/11/27 .
 */
open abstract class SupportActivity : RxAppCompatActivity(), ISupportActivity {
    private val mDelegate = SupportActivityDelegate(this)

    override fun getSupportDelegate(): SupportActivityDelegate = mDelegate

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    override fun extraTransaction(): ExtraTransaction = mDelegate.extraTransaction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDelegate.onPostCreate(savedInstanceState)
    }

    override fun onDestroy() {
        mDelegate.onDestroy()
        super.onDestroy()
    }

    /**
     * Note： return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean = mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev)

    /**
     * 不建议复写该方法,请使用 [.onBackPressedSupport] 代替
     */
    override fun onBackPressed() {
        mDelegate.onBackPressed()
    }

    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    override fun onBackPressedSupport() {
        mDelegate.onBackPressedSupport()
    }

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    override fun getFragmentAnimator(): FragmentAnimator = mDelegate.fragmentAnimator

    /**
     * Set all fragments animation.
     * 设置Fragment内的全局动画
     */
    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    /**
     * Set all fragments animation.
     * 构建Fragment转场动画
     *
     *
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     *
     * @return FragmentAnimator对象
     */
    override fun onCreateFragmentAnimator(): FragmentAnimator = mDelegate.onCreateFragmentAnimator()


    /**
     * 获取栈内的fragment对象
     */
    fun <T : ISupportFragment> findFragment(fragmentClass: Class<T>): T = SupportHelper.findFragment(supportFragmentManager, fragmentClass)

}

