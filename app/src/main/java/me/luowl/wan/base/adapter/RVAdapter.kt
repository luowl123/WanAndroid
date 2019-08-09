package me.luowl.wan.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import me.luowl.wan.BR
import me.luowl.wan.R


/*
 *
 * Created by luowl
 * Date: 2019/7/23 
 * Desc：
 */

open class RVAdapter<T>(private var itemDatas: MutableList<T>, private val defaultLayout: Int, private val brId: Int) :
    RecyclerView.Adapter<RVAdapter.RVViewHolder>() {

    private var mNextLoadEnable = false
    private var mLoadMoreEnable = false
    private var mLoading = false
    private val loadMoreStateModel = LoadMoreState()
    private var loadMoreListener: LoadMoreListener? = null
    private var mEnableLoadMoreEndClick = false
    private var mPreLoadNumber = 1
    private var recyclerView: RecyclerView? = null

    fun setRequestLoadMoreListener(loadMoreListener: LoadMoreListener, recyclerView: RecyclerView) {
        this.loadMoreListener = loadMoreListener
        this.recyclerView = recyclerView
        mNextLoadEnable = true
        mLoadMoreEnable = true
        mLoading = false
    }

    fun loadMoreEnd() {
        mLoading = false
        mNextLoadEnable = false
        loadMoreStateModel.loadMoreEnd()
        notifyItemChanged(getLoadMoreViewPosition())
    }

    fun loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return
        }
        mLoading = false
        mNextLoadEnable = true
        loadMoreStateModel.loadMoreComplete()
        notifyItemChanged(getLoadMoreViewPosition())
    }

    fun loadMoreFail() {
        if (getLoadMoreViewCount() == 0) {
            return
        }
        mLoading = false
        loadMoreStateModel.loadMoreFail()
        notifyItemChanged(getLoadMoreViewPosition())
    }

    private fun autoLoadMore(position: Int) {
        if (getLoadMoreViewCount() == 0) {
            return
        }
        if (position < itemCount - mPreLoadNumber) {
            return
        }
        if (!loadMoreStateModel.none) {
            return
        }
        loadMoreStateModel.startLoading()
        if (!mLoading) {
            mLoading = true
            recyclerView?.let { it.post { loadMoreListener?.loadMore() } }
                ?: loadMoreListener?.loadMore()
        }
    }

    private val dataItemCount: Int
        get() = itemDatas.size

    /**
     * Load more view count
     *
     * @return 0 or 1
     */
    private fun getLoadMoreViewCount(): Int {
        if (loadMoreListener == null || !mLoadMoreEnable) {
            return 0
        }
//        if (!mNextLoadEnable) {
//            return 0
//        }
        if (dataItemCount == 0) {
            return 0
        }
        return 1
    }

    private fun getLoadMoreViewPosition(): Int {
        return getHeaderLayoutCount() + dataItemCount
    }

    override fun getItemCount(): Int {
        return dataItemCount + getLoadMoreViewCount() + getHeaderLayoutCount()
    }

    private var mHeaderLayout: LinearLayout? = null
    private var mHeaderDataBinding: ViewDataBinding? = null

    /**
     * Return root layout of header
     */

    fun getHeaderLayout(): LinearLayout? {
        return mHeaderLayout
    }

    /**
     * Append header to the rear of the mHeaderLayout.
     *
     * @param header
     */
    fun addHeaderView(header: View): Int {
        return addHeaderView(header, -1)
    }

    /**
     * Add header view to mHeaderLayout and set header view position in mHeaderLayout.
     * When index = -1 or index >= child count in mHeaderLayout,
     * the effect of this method is the same as that of [.addHeaderView].
     *
     * @param header
     * @param index  the position in mHeaderLayout of this header.
     * When index = -1 or index >= child count in mHeaderLayout,
     * the effect of this method is the same as that of [.addHeaderView].
     */
    fun addHeaderView(header: View, index: Int): Int {
        return addHeaderView(header, index, LinearLayout.VERTICAL)
    }

    /**
     * @param header
     * @param index
     * @param orientation
     */
    fun addHeaderView(header: View, index: Int, orientation: Int): Int {
        if (mHeaderLayout == null) {
            mHeaderDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(header.context),
                R.layout.recycler_view_header_view,
                null,
                false
            )
            mHeaderLayout = mHeaderDataBinding!!.root.findViewById(R.id.ll_header_view) as LinearLayout
//            mHeaderDataBinding=DataBindingUtil.bind(mHeaderLayout!!)
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout!!.orientation = LinearLayout.VERTICAL
                mHeaderLayout!!.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                mHeaderLayout!!.orientation = LinearLayout.HORIZONTAL
                mHeaderLayout!!.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }
        val childCount = mHeaderLayout!!.childCount
        var mIndex = index
        if (index < 0 || index > childCount) {
            mIndex = childCount
        }
        mHeaderLayout!!.addView(header, mIndex)
        if (mHeaderLayout!!.childCount == 1) {
            val position = getHeaderViewPosition()
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return mIndex
    }

    fun setHeaderView(header: View): Int {
        return setHeaderView(header, 0, LinearLayout.VERTICAL)
    }

    fun setHeaderView(header: View, index: Int): Int {
        return setHeaderView(header, index, LinearLayout.VERTICAL)
    }

    fun setHeaderView(header: View, index: Int, orientation: Int): Int {
        if (mHeaderLayout == null || mHeaderLayout!!.childCount <= index) {
            return addHeaderView(header, index, orientation)
        } else {
            mHeaderLayout?.run {
                removeViewAt(index)
                addView(header, index)
            }
            return index
        }
    }

    /**
     * remove header view from mHeaderLayout.
     * When the child count of mHeaderLayout is 0, mHeaderLayout will be set to null.
     *
     * @param header
     */
    fun removeHeaderView(header: View) {
        if (getHeaderLayoutCount() == 0) return

        mHeaderLayout?.run {
            removeView(header)
            if (childCount == 0) {
                val position = getHeaderViewPosition()
                if (position != -1) {
                    notifyItemRemoved(position)
                }
            }
        }

    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    fun getHeaderLayoutCount(): Int {
        if (mHeaderLayout == null || mHeaderLayout!!.childCount == 0)
            return 0
        return 1
    }

    private fun getHeaderViewPosition(): Int {
        return 0
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    private fun compatibilityDataSizeChanged(size: Int) {
        val dataSize = itemDatas.size
        if (dataSize == size) {
            notifyDataSetChanged()
        }
    }

    fun setData(itemDatas: MutableList<T>?) {
        if (itemDatas == null) {
            this.itemDatas = mutableListOf()
        } else {
            this.itemDatas = itemDatas
        }
        if (loadMoreListener != null) {
            mNextLoadEnable = true
            mLoadMoreEnable = true
            mLoading = false
            loadMoreStateModel.loadMoreComplete()
        }
        notifyDataSetChanged()
    }

    fun addData(itemDatas: MutableList<T>?) {
        if (itemDatas != null) {
            this.itemDatas = itemDatas
            notifyDataSetChanged()
        }
    }

    open fun getItemLayout(viewType: Int): Int {
        return defaultLayout
    }

    open fun addListener(binding: ViewDataBinding, itemData: T, position: Int) {}

    open fun getDefItemViewType(position: Int): Int {
        return TYPE_OTHER
    }

    override fun getItemViewType(position: Int): Int {
//        return if (position < dataItemCount) getDefItemViewType(position) else TYPE_LOAD_MORE
        val numHeaders = getHeaderLayoutCount()
        return if (position < numHeaders) {
            TYPE_HEADER_VIEW
        } else {
            val adjPosition = position - numHeaders
            val adapterCount = itemDatas.size
            if (adjPosition < adapterCount) {
                getDefItemViewType(adjPosition)
            } else {
                TYPE_LOAD_MORE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        when (viewType) {
            TYPE_LOAD_MORE -> {
                val banding: ViewDataBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.recycler_item_load_more,
                        parent, false
                    )
                return RVViewHolder(banding)
            }
            TYPE_HEADER_VIEW -> {
//                val banding: ViewDataBinding = DataBindingUtil.bind(mHeaderLayout!!)!!
                return RVViewHolder(mHeaderDataBinding!!)
            }
        }
        val banding: ViewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), getItemLayout(viewType), parent, false)
        return RVViewHolder(banding)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        autoLoadMore(position)
        when (getItemViewType(position)) {
            TYPE_LOAD_MORE -> {
                holder.binding.setVariable(BR.loadMoreStateModel, loadMoreStateModel)
                holder.binding.root.findViewById<View>(R.id.load_more_load_fail_view)?.setOnClickListener {
                    loadMoreStateModel.startLoading()
                    if (!mLoading) {
                        mLoading = true
                        recyclerView?.let { it.post { loadMoreListener?.loadMore() } }
                            ?: loadMoreListener?.loadMore()
                    }
                    notifyItemChanged(getLoadMoreViewPosition())
                }
                holder.binding.executePendingBindings()
            }
            TYPE_HEADER_VIEW -> {

            }
            else -> {
                val adjPosition = position - getHeaderLayoutCount()
                holder.binding.setVariable(brId, itemDatas[adjPosition])
                addListener(holder.binding, itemDatas[adjPosition], position)
                onBindDefViewHolder(holder, itemDatas[adjPosition], position)
                holder.binding.executePendingBindings()
            }
        }
    }

    open fun onBindDefViewHolder(viewHolder: RVViewHolder, data: T, position: Int) {

    }

    interface LoadMoreListener {
        fun loadMore()
    }

    class RVViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        const val TYPE_LOAD_MORE = 100
        const val TYPE_HEADER_VIEW = 101
        private const val TYPE_OTHER = 102
    }

}