package com.will.weiyuekotlin

import android.os.Bundle
import android.view.View
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.ui.base.BaseActivity
import com.will.weiyuekotlin.ui.base.BaseContract
import com.will.weiyuekotlin.ui.base.SupportFragment
import com.will.weiyuekotlin.ui.jandan.JanDanFragment
import com.will.weiyuekotlin.ui.news.NewsFragment
import com.will.weiyuekotlin.ui.personal.PersonalFragment
import com.will.weiyuekotlin.ui.video.VideoFragment
import com.will.weiyuekotlin.utils.StatusBarUtil
import com.will.weiyuekotlin.widget.BottomBar
import com.will.weiyuekotlin.widget.BottomBarTab
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<BaseContract.BasePresenter>() {

    private val mFragments = arrayOfNulls<SupportFragment>(4)

    override fun getContentLayout(): Int = R.layout.activity_main

    override fun getSimpleMultiStateView(): SimpleMultiStateView? = null

    override fun initInjector(appComponent: ApplicationComponent) {}

    override fun bindView(view: View, savedInstanceState: Bundle?) {
        StatusBarUtil.setTranslucentForImageViewInFragment(this@MainActivity, 0, null)
        if (savedInstanceState == null) {
            mFragments[0] = NewsFragment.newInstance()
            mFragments[1] = VideoFragment.newInstance()
            mFragments[2] = JanDanFragment.newInstance()
            mFragments[3] = PersonalFragment.newInstance()

            supportDelegate.loadMultipleRootFragment(R.id.contentContainer, 0,
                    mFragments[0],
                    mFragments[1],
                    mFragments[2],
                    mFragments[3])
        } else {
            mFragments[0] = findFragment(NewsFragment::class.java)
            mFragments[1] = findFragment(VideoFragment::class.java)
            mFragments[2] = findFragment(JanDanFragment::class.java)
            mFragments[3] = findFragment(PersonalFragment::class.java)
        }

        mBootomBar.addItem(BottomBarTab(this, R.drawable.ic_news, "新闻"))
                .addItem(BottomBarTab(this, R.drawable.ic_video, "视频"))
                .addItem(BottomBarTab(this, R.drawable.ic_jiandan, "煎蛋"))
                .addItem(BottomBarTab(this, R.drawable.ic_my, "我的"))

        mBootomBar.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                supportDelegate.showHideFragment(mFragments[position], mFragments[prePosition])
            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabReselected(position: Int) {

            }
        })

    }

    override fun initData() {

    }

    override fun onBackPressedSupport() {
        if (JCVideoPlayer.backPress()) {
            return
        }
        super.onBackPressedSupport()
    }

    override fun onPause() {
        super.onPause()
        JCVideoPlayer.releaseAllVideos()
    }


}
