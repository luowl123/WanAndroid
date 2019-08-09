package me.luowl.wan.ui.account

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseActivity
import me.luowl.wan.base.adapter.LoadMoreState
import me.luowl.wan.base.adapter.RVAdapter
import me.luowl.wan.data.model.ArticleData
import me.luowl.wan.databinding.ActivityCollectionBinding
import me.luowl.wan.ui.webview.WebViewActivity
import me.luowl.wan.util.GlobalUtil
import me.luowl.wan.util.logDebug
import me.luowl.wan.widget.SimpleDividerItemDecoration

/*
 *
 * Created by luowl
 * Date: 2019/8/6 
 * Desc：
 */

class CollectionActivity : BaseActivity<ActivityCollectionBinding, CollectionViewModel>() {

    private lateinit var adapter: RVAdapter<ArticleData>

    override fun getViewModelClass(): Class<CollectionViewModel> = CollectionViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_collection

    override fun initVariableId(): Int = BR.viewModel

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        setupToolbar()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(SimpleDividerItemDecoration(this, 1f))
        adapter =
            object : RVAdapter<ArticleData>(mutableListOf(), R.layout.recycler_item_collection_article, BR.article) {
                override fun addListener(binding: ViewDataBinding, itemData: ArticleData, position: Int) {
                    binding.root.setOnClickListener {
                        WebViewActivity.startActivity(this@CollectionActivity, itemData.title, itemData.link)
                    }
                    binding.root.setOnLongClickListener {
                        showCancelDialog(itemData, position)
                        true
                    }
                }
            }
        adapter.setRequestLoadMoreListener(object : RVAdapter.LoadMoreListener {
            override fun loadMore() {
                viewModel.getData()
            }
        }, binding.recyclerView)
        binding.recyclerView.adapter = adapter
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.dataList.observe(this, Observer {
            logDebug("dataChange")
            adapter.setData(viewModel.dataList.value)
        })
        viewModel.requestLoadDataState.observe(this, Observer {
            when (it) {
                LoadMoreState.STATE_LOAD_FAIL -> adapter.loadMoreFail()
                LoadMoreState.STATE_LOAD_END -> adapter.loadMoreEnd()
                LoadMoreState.STATE_LOAD_NONE -> adapter.loadMoreComplete()
            }
        })
    }

    override fun startLoadData() {
        super.startLoadData()
        viewModel.dataList.value?.run {
            if (isEmpty()) {
                viewModel.getData()
            }
        }
    }

    fun showCancelDialog(itemData: ArticleData, position: Int) {
        val dialog = Dialog(this)
        val contentView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_cannel_collection, null)
        dialog.setContentView(contentView)
        val displayMetrics = resources.displayMetrics
        val params = dialog.window!!.attributes
        params.width = (displayMetrics.widthPixels * 0.8).toInt()
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        dialog.show()
        contentView.setOnClickListener {
            viewModel.cancelCollectArticle(itemData.id, itemData.originId)
            dialog.dismiss()
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, CollectionActivity::class.java))
        }
    }
}