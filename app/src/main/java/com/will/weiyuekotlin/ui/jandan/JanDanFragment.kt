package com.will.weiyuekotlin.ui.jandan

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.bean.JdBaseBean
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.net.JanDanApi
import com.will.weiyuekotlin.ui.adapter.BoredPicAdapter
import com.will.weiyuekotlin.ui.adapter.FreshNewsAdapter
import com.will.weiyuekotlin.ui.adapter.JokesAdapter
import com.will.weiyuekotlin.ui.base.BaseContract
import com.will.weiyuekotlin.ui.base.BaseFragment
import com.will.weiyuekotlin.ui.base.BasePresenter
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import kotlinx.android.synthetic.main.fragment_jiandan.*
import java.util.*


/**
 * desc: 煎蛋 .
 * author: Will .
 * date: 2017/11/28 .
 */

class JanDanFragment : BaseFragment<BasePresenter<BaseContract.BaseView>>() {
    private var mJanDanPagerAdapter: JanDanPagerAdapter? = null

    companion object {
        fun newInstance(): JanDanFragment {
            val args = Bundle()
            val fragment = JanDanFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getSimpleMultiStateView(): SimpleMultiStateView? = null

    override fun initInjector(appComponent: ApplicationComponent) {}

    override fun getContentLayout(): Int = R.layout.fragment_jiandan

    override fun bindView(view: View, savedInstanceState: Bundle?) {}

    override fun initData() {
        val strings = ArrayList<String>()
        strings.add("新鲜事")
        strings.add("无聊图")
        strings.add("妹子图")
        strings.add("段子")
        mJanDanPagerAdapter = JanDanPagerAdapter(childFragmentManager, strings)
        viewpager.adapter = mJanDanPagerAdapter
        viewpager.offscreenPageLimit = 1
        viewpager.setCurrentItem(0, false)
        tablayout.setupWithViewPager(viewpager, true)
    }

    inner class JanDanPagerAdapter(fm: FragmentManager, private val titles: List<String>) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): JdDetailFragment? {
            when (position) {
                0 -> return JdDetailFragment.newInstance(JanDanApi.TYPE_FRESH, FreshNewsAdapter(activity, null) as BaseQuickAdapter<in JdBaseBean
                        , BaseViewHolder>)
                1 -> return JdDetailFragment.newInstance(JanDanApi.TYPE_BORED, BoredPicAdapter(activity, null) as BaseQuickAdapter<in JdBaseBean
                        , BaseViewHolder>)
                2 -> return JdDetailFragment.newInstance(JanDanApi.TYPE_GIRLS, BoredPicAdapter(activity, null) as BaseQuickAdapter<in JdBaseBean
                        , BaseViewHolder>)
                3 -> return JdDetailFragment.newInstance(JanDanApi.TYPE_Duan, JokesAdapter(null) as BaseQuickAdapter<in JdBaseBean
                        , BaseViewHolder>)
            }
            return null
        }

        override fun getPageTitle(position: Int): CharSequence = titles[position]

        override fun getCount(): Int = titles.size

        override fun getItemPosition(`object`: Any?): Int = PagerAdapter.POSITION_NONE

    }
}