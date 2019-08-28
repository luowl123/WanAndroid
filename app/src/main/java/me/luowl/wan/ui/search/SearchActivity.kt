package me.luowl.wan.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_search.*
import me.luowl.wan.AppConfig
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseActivity
import me.luowl.wan.base.adapter.LoadMoreState
import me.luowl.wan.base.adapter.RVAdapter
import me.luowl.wan.data.model.ArticleData
import me.luowl.wan.data.model.SearchRecord
import me.luowl.wan.databinding.ActivitySearchBinding
import me.luowl.wan.databinding.RecyclerItemArticleBinding
import me.luowl.wan.databinding.RecyclerItemSearchRecordBinding
import me.luowl.wan.ui.webview.WebViewActivity
import me.luowl.wan.util.logDebug
import me.luowl.wan.widget.SimpleDividerItemDecoration


/*
 *
 * Created by luowl
 * Date: 2019/7/29 
 * Desc：
 */

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>() {

    //搜索历史纪录
    //搜索热词
    //

    override fun getViewModelClass(): Class<SearchViewModel> = SearchViewModel::class.java

    override fun getLayoutId() = R.layout.activity_search

    override fun initVariableId() = BR.viewModel

    private lateinit var searchView: SearchView
    private lateinit var recordAdapter: RVAdapter<SearchRecord>
    private lateinit var resultAdapter: RVAdapter<ArticleData>

    private val recordDecor by lazy {
        SimpleDividerItemDecoration(this)
    }

    private val resultDecor by lazy {
        SimpleDividerItemDecoration(this, 10f)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        setupToolbar()
        recycler_view.layoutManager = LinearLayoutManager(this)
        recordAdapter =
            object : RVAdapter<SearchRecord>(mutableListOf(), R.layout.recycler_item_search_record, BR.data) {
                override fun addListener(binding: ViewDataBinding, itemData: SearchRecord, position: Int) {
                    super.addListener(binding, itemData, position)
                    val recordBinding = binding as RecyclerItemSearchRecordBinding
                    recordBinding.root.setOnClickListener {
                        searchView.setQuery(itemData.keyword, false)
                        setUpSearchResult()
                        viewModel.doSearch(itemData.keyword)
                        hideSoftInput()
                    }
                    recordBinding.imgDelete.setOnClickListener {
                        viewModel.deleteRecord(itemData.keyword)
                    }
                }
            }

        resultAdapter =
            object : RVAdapter<ArticleData>(mutableListOf(), R.layout.recycler_item_article, BR.article) {
                override fun addListener(binding: ViewDataBinding, itemData: ArticleData, position: Int) {
                    val articleBinding = binding as RecyclerItemArticleBinding
                    binding.root.setOnClickListener {
                        WebViewActivity.startActivity(this@SearchActivity, itemData.title, itemData.link)
                    }
                    articleBinding.imgCollect.setOnClickListener {
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
        resultAdapter.setRequestLoadMoreListener(object : RVAdapter.LoadMoreListener {
            override fun loadMore() {
                viewModel.searchArticleByKey()
            }
        }, recycler_view)

        setUpSearchRecord()
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.dataList.observe(this, Observer {
            logDebug("dataList dataChange")
            resultAdapter.setData(viewModel.dataList.value)
        })

        viewModel.records.observe(this, Observer {
            logDebug("records dataChange")
            recordAdapter.setData(ArrayList(it))
        })

        viewModel.requestLoadDataState.observe(this, Observer {
            when (it) {
                LoadMoreState.STATE_LOAD_FAIL -> resultAdapter.loadMoreFail()
                LoadMoreState.STATE_LOAD_END -> resultAdapter.loadMoreEnd()
                LoadMoreState.STATE_LOAD_NONE -> resultAdapter.loadMoreComplete()
            }
        })
        initLoginChangeObservable()
    }

    override fun startLoadData() {
        super.startLoadData()
        viewModel.queryRecord()
    }

    private fun setUpSearchRecord() {
        logDebug("setUpSearchRecord")
        recycler_view.removeItemDecoration(resultDecor)
        recycler_view.addItemDecoration(recordDecor)
        recordAdapter.setData(mutableListOf())
        recycler_view.adapter = recordAdapter
    }

    private fun setUpSearchResult() {
        logDebug("setUpSearchResult")
        recycler_view.removeItemDecoration(recordDecor)
        recycler_view.addItemDecoration(resultDecor)
        resultAdapter.setData(mutableListOf())
        recycler_view.adapter = resultAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_view, menu)

        //找到searchView
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.isIconified = false//一开始处于展开状态
        searchView.onActionViewExpanded()// 当展开无输入内容的时候，没有关闭的图标
        searchView.queryHint = "搜索关键字以空格形式隔开"//设置默认无内容时的文字提示
        searchView.isSubmitButtonEnabled = false//显示提交按钮
//        searchView.setIconifiedByDefault(false)//默认为true在框内，设置false则在框外
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                setUpSearchResult()
                viewModel.doSearch(query)
                hideSoftInput()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //当输入框内容改变的时候回调
                logDebug("内容: $newText")
                if (newText.isEmpty()) {
                    setUpSearchRecord()
                    viewModel.queryRecord()
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun hideSoftInput(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }
}