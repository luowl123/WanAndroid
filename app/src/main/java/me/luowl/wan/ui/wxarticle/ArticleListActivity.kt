package me.luowl.wan.ui.wxarticle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_wx_article_list.*
import me.luowl.wan.AppConfig
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseActivity
import me.luowl.wan.base.adapter.LoadMoreState
import me.luowl.wan.base.adapter.RVAdapter
import me.luowl.wan.data.model.ArticleData
import me.luowl.wan.databinding.ActivityWxArticleListBinding
import me.luowl.wan.databinding.RecyclerItemWxArticleBinding
import me.luowl.wan.ui.webview.WebViewActivity
import me.luowl.wan.util.logDebug
import me.luowl.wan.widget.SimpleDividerItemDecoration

/*
 *
 * Created by luowl
 * Date: 2019/7/25 
 * Desc：
 */

class ArticleListActivity : BaseActivity<ActivityWxArticleListBinding, ArticleListViewModel>() {

    override fun getViewModelClass(): Class<ArticleListViewModel> = ArticleListViewModel::class.java

    override fun getLayoutId() = R.layout.activity_wx_article_list

    override fun initVariableId() = BR.viewModel

    private lateinit var adapter: RVAdapter<ArticleData>

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        setupToolbar()
        title = intent.getStringExtra(EXTRA_CHAPTER_NAME)
        viewModel.chapterId = intent.getLongExtra(EXTRA_CHAPTER_ID, 0L)
        swipe_refresh_layout.setColorSchemeColors(resources.getColor(R.color.appThemeColor))
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(SimpleDividerItemDecoration(this, 10f))
        adapter = object : RVAdapter<ArticleData>(mutableListOf(), R.layout.recycler_item_wx_article, BR.article) {
            override fun addListener(binding: ViewDataBinding, itemData: ArticleData, position: Int) {
                val itemBinding= binding as RecyclerItemWxArticleBinding
                itemBinding.root.setOnClickListener {
                    WebViewActivity.startActivity(this@ArticleListActivity, itemData.title, itemData.link)
                }
                itemBinding.imgCollect.setOnClickListener {
                    if (!AppConfig.isLogin()) {
                        showLoginDialog()
                        return@setOnClickListener
                    }
                    if (itemData.collect) {
                        viewModel.cancelCollectArticle(itemData.id)
                    } else {
                        viewModel.collectArticle(itemData.id)
                    }
                    itemData.collect = !itemData.collect
                    notifyItemChanged(position)
                }
            }
        }
        adapter.setRequestLoadMoreListener(object : RVAdapter.LoadMoreListener {
            override fun loadMore() {
                viewModel.getData()
            }
        }, recycler_view)
        recycler_view.adapter = adapter
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
        initLoginChangeObservable()
    }

    override fun startLoadData() {
        super.startLoadData()
        viewModel.dataList.value?.run {
            if (isEmpty()) {
                viewModel.getData()
            }
        }
    }

    private lateinit var searchView: SearchView
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_view, menu)

        //找到searchView
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
//        searchView.onActionViewExpanded()// 当展开无输入内容的时候，没有关闭的图标
//        searchView.queryHint = "搜索关键字以空格形式隔开"//设置默认无内容时的文字提示
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.keyword = query
                viewModel.reset()
                viewModel.getData()
                hideSoftInput()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //当输入框内容改变的时候回调
                logDebug("内容: $newText")
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val EXTRA_CHAPTER_ID = "chapterId"
        const val EXTRA_CHAPTER_NAME = "chapterName"

        fun startActivity(context: Context, chapterId: Long, chapterName: String) {
            val intent = Intent(context, ArticleListActivity::class.java).apply {
                putExtra(EXTRA_CHAPTER_ID, chapterId)
                putExtra(EXTRA_CHAPTER_NAME, chapterName)
            }
            context.startActivity(intent)
        }
    }

    private fun hideSoftInput(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }

}