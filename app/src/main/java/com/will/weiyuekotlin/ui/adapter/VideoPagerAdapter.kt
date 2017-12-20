package com.will.weiyuekotlin.ui.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

import com.will.weiyuekotlin.bean.VideoChannelBean
import com.will.weiyuekotlin.ui.base.BaseFragment
import com.will.weiyuekotlin.ui.video.DetailFragment

/**
 * desc 视频 Adapter :
 * author: Will .
 * date: 2017/9/10 .
 */
class VideoPagerAdapter(fm: FragmentManager, private val videoChannelBean: VideoChannelBean?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): BaseFragment<*>
            = DetailFragment.newInstance("clientvideo_" + videoChannelBean!!.types!![position].id)

    override fun getPageTitle(position: Int): CharSequence = videoChannelBean!!.types!![position].name!!

    override fun getCount(): Int = videoChannelBean?.types?.size ?: 0

    override fun getItemPosition(`object`: Any?): Int = PagerAdapter.POSITION_NONE

}
