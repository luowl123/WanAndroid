package me.luowl.wan.widget

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.luowl.wan.R
import me.luowl.wan.base.adapter.RVAdapter
import me.luowl.wan.util.GlobalUtil

/**
 *
 * Created by luowl@iPanel.cn
 * Date: 2019/7/23
 * Desc：
 */
class SimpleDividerItemDecoration(
    context: Context?, private val dividerWidth: Float = 0.5f,
    val color: Int = GlobalUtil.getColor(R.color.app_divider),
    private val firstTop: Float = 0f, private val firstLeft: Float = 0f, private val lastRight: Float = 0f
) : RVDividerItemDecoration(context) {
    override fun getDivider(parent: RecyclerView, itemPosition: Int): RVDivider {
        when (parent.adapter?.getItemViewType(itemPosition)) {
            RVAdapter.TYPE_LOAD_MORE -> return RVDividerBuilder().create()
        }
        if (parent.layoutManager is GridLayoutManager) {
            val spanCount = getSpanCount(parent)
            val rvDividerBuilder = RVDividerBuilder()
            if (itemPosition < spanCount) {
                rvDividerBuilder.setTopSideLine(true, color, firstTop, 0f, 0f)
            }
            return when (itemPosition % spanCount) {
                0 ->
                    //每一行第一个填充右边
                    rvDividerBuilder
                        .setLeftSideLine(true, color, firstLeft, 0f, 0f)
                        .setRightSideLine(true, color, dividerWidth / 2, 0f, 0f)
                        .setBottomSideLine(true, color, dividerWidth, 0f, 0f)
                spanCount - 1 ->
                    //第行最后一个填充左边
                    rvDividerBuilder
                        .setLeftSideLine(true, color, dividerWidth / 2, 0f, 0f)
                        .setRightSideLine(true, color, lastRight, 0f, 0f)
                        .setBottomSideLine(true, color, dividerWidth, 0f, 0f)
                else -> rvDividerBuilder
                    .setLeftSideLine(true, color, dividerWidth / 2, 0f, 0f)
                    .setRightSideLine(true, color, dividerWidth / 2, 0f, 0f)
                    .setBottomSideLine(true, color, dividerWidth, 0f, 0f)
            }.create()
        }
        if (parent.layoutManager is LinearLayoutManager) {
            val rvDividerBuilder = RVDividerBuilder()
                .setBottomSideLine(true, color, dividerWidth, 0f, 0f)
            if (itemPosition == 0)
                rvDividerBuilder.setTopSideLine(true, color, firstTop, 0f, 0f)
            rvDividerBuilder.setLeftSideLine(true, color, firstLeft, 0f, 0f)
            rvDividerBuilder.setRightSideLine(true, color, lastRight, 0f, 0f)
            return rvDividerBuilder.create()
        }
        return RVDividerBuilder().create()
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return (layoutManager as? GridLayoutManager)?.spanCount ?: 1
    }
}