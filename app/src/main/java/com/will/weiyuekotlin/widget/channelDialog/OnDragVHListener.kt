package com.will.weiyuekotlin.widget.channelDialog

/**
 * ViewHolder 被选中 以及 拖拽释放 触发监听器
 * Created by YoKeyword on 15/12/29.
 */
interface OnDragVHListener {
    /**
     * Item被选中时触发
     */
    fun onItemSelected()


    /**
     * Item在拖拽结束/滑动结束后触发
     */
    fun onItemFinish()
}
