package com.will.weiyuekotlin.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

import org.litepal.annotation.Column
import org.litepal.crud.DataSupport

import java.io.Serializable

/**
 * desc: .
 * author: Will .
 * date: 2017/9/3 .
 */
class Channel : DataSupport(), Serializable, MultiItemEntity {
    companion object {
        val TYPE_MY = 1
        val TYPE_OTHER = 2
        val TYPE_MY_CHANNEL = 3
        val TYPE_OTHER_CHANNEL = 4
    }

    @Column(ignore = true)
    var viewType: Int = 0

    var channelId: String? = null
    var channelName: String? = null
    /**
     * 0 可移除，1不可移除
     */
    var channelType: Int = 0

    /**
     * 0 未选中 1 选中
     */
    var isChannelSelect: Boolean = false

    override fun getItemType(): Int {
        return viewType
    }

}
