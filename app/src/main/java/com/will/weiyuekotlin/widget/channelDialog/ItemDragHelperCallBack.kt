package com.will.weiyuekotlin.widget.channelDialog

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper


class ItemDragHelperCallBack(private var onChannelListener: OnChannelListener?) : ItemTouchHelper.Callback() {

    fun setOnChannelDragListener(onChannelDragListener: OnChannelListener) {
        this.onChannelListener = onChannelDragListener
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val manager = recyclerView.layoutManager
        val dragFlags = when (manager) {
            is GridLayoutManager, is StaggeredGridLayoutManager -> //监听上下左右拖动
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            else -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        // 不同Type之间不可移动
        if (viewHolder.itemViewType != target.itemViewType) return false
        onChannelListener?.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // 不在闲置状态
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is OnDragVHListener) {
                val itemViewHolder = viewHolder as OnDragVHListener?
                itemViewHolder!!.onItemSelected()
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is OnDragVHListener) {
            val itemViewHolder = viewHolder as OnDragVHListener
            itemViewHolder.onItemFinish()
        }
        super.clearView(recyclerView, viewHolder)
    }

    //不需要长按拖动，因为我们的标题和 频道推荐 是不需要拖动的，所以手动控制
    override fun isLongPressDragEnabled(): Boolean = false

    //不需要侧滑
    override fun isItemViewSwipeEnabled(): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}
