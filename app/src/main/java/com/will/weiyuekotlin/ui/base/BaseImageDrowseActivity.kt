package com.will.weiyuekotlin.ui.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.github.chrisbanes.photoview.PhotoView
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.utils.ImageLoaderUtil
import com.will.weiyuekotlin.widget.SwipeBackLayout


/**
 * desc: 图片浏览页面父类.
 * author: Will .
 * date: 2017/12/11 .
 */

abstract class BaseImageDrowseActivity<T : BaseContract.BasePresenter> : BaseActivity<T>() {
    private lateinit var mainRelativeLayout: RelativeLayout
    private lateinit var mSwipeBackLayout: SwipeBackLayout
    private lateinit var mViewPager: ViewPager

    /**
     * 初始化布局透明度 和viewPager
     */
    protected fun initView(relativeLayout: RelativeLayout, swipeBackLayout: SwipeBackLayout, viewPager: ViewPager) {
        mainRelativeLayout = relativeLayout
        mSwipeBackLayout = swipeBackLayout
        mViewPager = viewPager

        mainRelativeLayout.background.alpha = 255
        mSwipeBackHelper.setSwipeBackEnable(true)
        mSwipeBackLayout.setDragDirectMode(SwipeBackLayout.DragDirectMode.VERTICAL)
        mSwipeBackLayout.setOnSwipeBackListener { fractionAnchor, _ ->
            mainRelativeLayout.background.alpha = 255 - Math.ceil((255 * fractionAnchor).toDouble()).toInt()
            onViewPositionChanged(fractionAnchor)
        }
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> mSwipeBackHelper.setSwipeBackEnable(true)
                    else -> mSwipeBackHelper.setSwipeBackEnable(false)
                }
                setTvInfo(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    private inner class ViewPagerAdapter(private val context: Context, private val ImgUrls: List<String>?) : PagerAdapter() {
        private lateinit var mPhotoView: PhotoView
        private lateinit var mProgressBar: ProgressBar

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(context).inflate(
                    R.layout.loadimage, null)
            mPhotoView = view.findViewById(R.id.photoview) as PhotoView
            mProgressBar = view.findViewById(R.id.loading) as ProgressBar
            mPhotoView.setOnPhotoTapListener { _, _, _ ->
                onPhotoTap()
            }
            mProgressBar.visibility = View.GONE
            ImageLoaderUtil.LoadImage(context, ImgUrls!![position],
                    object : DrawableImageViewTarget(mPhotoView) {

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            mProgressBar.visibility = View.GONE
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            super.onLoadCleared(placeholder)
                            mProgressBar.visibility = View.GONE
                        }

                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            super.onResourceReady(resource, transition)
                            mProgressBar.visibility = View.GONE

                        }
                    })

            container.addView(view)
            return view
        }

        override fun getCount(): Int = ImgUrls?.size ?: 0

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean = arg0 === arg1

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    /**
     * ViewPager 滑动时更新文本内容
     *
     * @param position 下标
     */
    open fun setTvInfo(position: Int) {}

    /**
     * 图片点击回调
     */
    open fun onPhotoTap() {}

    /**
     * 图片上下拖动时回调
     *
     * @param fractionAnchor relative to the anchor.
     */
    open fun onViewPositionChanged(fractionAnchor: Float) {}

    /**
     * 初始化ViewPager
     *
     * @param ImgUrls 图片列表
     */
    fun initViewPager(context: Context, ImgUrls: List<String>?) {
        mViewPager.adapter = ViewPagerAdapter(context, ImgUrls)
    }

    open fun setViewVisible(visible: Int, vararg view: View) {
        view.forEach {
            it.apply {// https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/apply.html
                alpha = 1f
                visibility = visible
            }
        }
    }

    /**
     * 修改 view 可见性伴随动画
     *
     * @param view  操作 view
     * @param isShow 是否可见
     */
    open fun changeViewAlpha(view: View, isShow: Boolean) {
        val alphaAnimation: AlphaAnimation = when {
            isShow -> AlphaAnimation(0f, 1f)
            else -> AlphaAnimation(1f, 0f)
        }
        alphaAnimation.fillAfter = true
        alphaAnimation.duration = 500
        view.startAnimation(alphaAnimation)
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = if (isShow) View.VISIBLE else View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}