package com.will.weiyuekotlin.ui.base

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import me.yokeyword.fragmentation.ExtraTransaction
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragmentDelegate
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 * desc: .
 * author: Will .
 * date: 2017/11/27 .
 */
open class SupportFragment : RxFragment(), ISupportFragment {

    private val mDelegate = SupportFragmentDelegate(this)
    private lateinit var _mActivity: FragmentActivity

    override fun getSupportDelegate(): SupportFragmentDelegate {
        return mDelegate
    }

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    override fun extraTransaction(): ExtraTransaction = mDelegate.extraTransaction()

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        mDelegate.onAttach(activity!!)
        _mActivity = mDelegate.activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDelegate.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mDelegate.onSaveInstanceState(outState!!)
    }

    override fun onResume() {
        super.onResume()
        mDelegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        mDelegate.onPause()
    }

    override fun onDestroyView() {
        mDelegate.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mDelegate.onDestroy()
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mDelegate.onHiddenChanged(hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mDelegate.setUserVisibleHint(isVisibleToUser)
    }

    /**
     * Called when the enter-animation end.
     * 入栈动画 结束时,回调
     */
    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        mDelegate.onEnterAnimationEnd(savedInstanceState)
    }


    /**
     * Lazy initial，Called when fragment is first called.
     *
     *
     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
     */
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        mDelegate.onLazyInitView(savedInstanceState)
    }

    /**
     * Called when the fragment is visible.
     * 当Fragment对用户可见时回调
     *
     *
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    override fun onSupportVisible() {
        mDelegate.onSupportVisible()
    }

    /**
     * Called when the fragment is invivible.
     *
     *
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    override fun onSupportInvisible() {
        mDelegate.onSupportInvisible()
    }

    /**
     * Return true if the fragment has been supportVisible.
     */
    override fun isSupportVisible(): Boolean = mDelegate.isSupportVisible

    /**
     * Set fragment animation with a higher priority than the ISupportActivity
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    override fun onCreateFragmentAnimator(): FragmentAnimator = mDelegate.onCreateFragmentAnimator()

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    override fun getFragmentAnimator(): FragmentAnimator = mDelegate.fragmentAnimator

    /**
     * 设置Fragment内的全局动画
     */
    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    override fun onBackPressedSupport(): Boolean = mDelegate.onBackPressedSupport()

    /**
     * 类似 [Activity.setResult]
     *
     * Similar to [Activity.setResult]
     *
     * @see .startForResult
     */
    override fun setFragmentResult(resultCode: Int, bundle: Bundle) {
        mDelegate.setFragmentResult(resultCode, bundle)
    }

    /**
     * 类似  [Activity.onActivityResult]
     *
     * Similar to [Activity.onActivityResult]
     *
     * @see .startForResult
     */
    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle) {
        mDelegate.onFragmentResult(requestCode, resultCode, data)
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
     * 类似 [Activity.onNewIntent]
     *
     * Similar to [Activity.onNewIntent]
     *
     * @see .start
     * @param args putNewBundle(Bundle newBundle)
     */
    override fun onNewBundle(args: Bundle) {
        mDelegate.onNewBundle(args)
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     *
     * @see .start
     */
    override fun putNewBundle(newBundle: Bundle) {
        mDelegate.putNewBundle(newBundle)
    }
}
