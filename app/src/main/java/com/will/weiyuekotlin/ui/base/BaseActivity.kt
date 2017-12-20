package com.will.weiyuekotlin.ui.base

import android.app.Dialog
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.IntRange
import android.support.annotation.Nullable
import android.view.View
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.trello.rxlifecycle2.LifecycleTransformer
import com.will.weiyuekotlin.MyApp
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.ui.base.BaseContract.BaseView
import com.will.weiyuekotlin.ui.listener.IBase
import com.will.weiyuekotlin.utils.StatusBarUtil
import com.will.weiyuekotlin.utils.toast
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import javax.inject.Inject


/**
 * desc: .
 * author: Will .
 * date: 2017/11/27 .
 */
abstract class BaseActivity<T : BaseContract.BasePresenter> : SupportActivity(), IBase, BaseView, BGASwipeBackHelper.Delegate {
    private lateinit var mRootView: View
    lateinit var mLoadingDialog: Dialog
    lateinit var mSwipeBackHelper: BGASwipeBackHelper

    @Nullable
    private var mSimpleMultiStateView: SimpleMultiStateView? = null

    @Nullable
    @Inject
    @JvmField
    var mPresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initSwipeBackFinish()
        super.onCreate(savedInstanceState)
        mRootView = layoutInflater.inflate(getContentLayout(), null)
        setContentView(mRootView)
        mSimpleMultiStateView = getSimpleMultiStateView()
        bindView(mRootView, savedInstanceState)
        initInjector(MyApp.mApplicationComponent)
        attachView()
        initStateView()
        initData()
    }

    /**
     * 绑定 Presenter
     */
    private fun attachView() {
        mPresenter?.attachView(this)
    }

    /**
     * 初始化状态主布局
     */
    private fun initStateView() {
        mSimpleMultiStateView?.setEmptyResource(R.layout.view_empty)
                ?.setRetryResource(R.layout.view_retry)
                ?.setLoadingResource(R.layout.view_loading)
                ?.setNoNetResource(R.layout.view_nonet)
                ?.build()
                ?.setOnReLoadListener { onRetry() }
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private fun initSwipeBackFinish() {
        mSwipeBackHelper = BGASwipeBackHelper(this, this)
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(false)
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(false)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f)
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    override fun isSupportSwipeBack(): Boolean = true

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected fun setStatusBarColor(@ColorInt color: Int) {
        setStatusBarColor(color, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA)
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     * @param statusBarAlpha 透明度
     */
    fun setStatusBarColor(@ColorInt color: Int, @IntRange(from = 0, to = 255) statusBarAlpha: Int) {
        StatusBarUtil.setColorForSwipeBack(this, color, statusBarAlpha)
    }

    /**
     * 显示加载页面
     */
    override fun showLoading() {
        mSimpleMultiStateView?.showLoadingView()
    }

    /**
     * 显示内容页面
     */
    override fun showSuccess() {
        mSimpleMultiStateView?.showContent()
    }

    /**
     * 显示错误页面
     */
    override fun showError() {
        mSimpleMultiStateView?.showErrorView()
    }

    /**
     * 显示无网络页面
     */
    override fun showNoNet() {
        mSimpleMultiStateView?.showNoNetView()
    }

    /**
     * 加载失败重试
     */
    override fun onRetry() {}

    /**
     * 绑定生命周期
     */
    override fun <T> bindToLife(): LifecycleTransformer<T> = this.bindToLifecycle()

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    override fun onSwipeBackLayoutExecuted() { mSwipeBackHelper.swipeBackward() }

    override fun onSwipeBackLayoutSlide(slideOffset: Float) {}

    override fun onSwipeBackLayoutCancel() {}

    protected fun toast(message: String) { toast(this, message) }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }


}