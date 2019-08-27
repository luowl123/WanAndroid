package me.luowl.wan.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_home.*
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseFragment
import me.luowl.wan.base.adapter.LoadMoreState
import me.luowl.wan.base.adapter.RVAdapter
import me.luowl.wan.data.model.ArticleData
import me.luowl.wan.databinding.FragmentHomeBinding
import me.luowl.wan.databinding.RecyclerItemArticleBinding
import me.luowl.wan.ui.MainActivity
import me.luowl.wan.ui.search.SearchActivity
import me.luowl.wan.ui.webview.WebViewActivity
import me.luowl.wan.ui.wxarticle.ArticleListActivity
import me.luowl.wan.util.GlobalUtil
import me.luowl.wan.util.logDebug
import me.luowl.wan.widget.BannerView
import me.luowl.wan.widget.SimpleDividerItemDecoration

/**
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
class HomeFragment : BaseFragment<FragmentHomeBinding,HomeViewModel>() {

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initVariableId(): Int =BR.viewModel

    private lateinit var adapter: RVAdapter<ArticleData>
    private lateinit var banner: BannerView
    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        swipe_refresh_layout.setColorSchemeColors(resources.getColor(R.color.appThemeColor))
        search_view.setOnClickListener {
            val intent= Intent(context,SearchActivity::class.java)
            context?.startActivity(intent)
        }
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.addItemDecoration(SimpleDividerItemDecoration(context, 10f))
        adapter = object : RVAdapter<ArticleData>(mutableListOf(), R.layout.recycler_item_article, BR.article) {
            override fun addListener(binding: ViewDataBinding, itemData: ArticleData, position: Int) {
                val articleBinding=binding as RecyclerItemArticleBinding
                articleBinding.root.setOnClickListener {
                    context?.let {
                        WebViewActivity.startActivity(it, itemData.title, itemData.link)
                    }
                }
                articleBinding.imgCollect.setOnClickListener {
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
        val bannerView = LayoutInflater.from(context).inflate(R.layout.recycler_item_banner, recycler_view, false)
        banner = bannerView.findViewById(R.id.banner)
        banner.setOnSwitchRvBannerListener { position, imageView ->
            Glide.with(context!!).load(viewModel.bannerData.value!![position].imagePath).into(imageView)
        }
        banner.setOnSwitchRvBannerTitleListener { position, titleView ->
            titleView.text = viewModel.bannerData.value!![position].title
        }
        banner.setOnRvBannerClickListener { position ->
            context?.let {
                WebViewActivity.startActivity(
                    it,
                    viewModel.bannerData.value!![position].title,
                    viewModel.bannerData.value!![position].url
                )
            }
        }
        adapter.addHeaderView(bannerView)
        val entryView = LayoutInflater.from(context).inflate(R.layout.recycler_item_entry, recycler_view, false)
        setEntryViewEvent(entryView)
        adapter.addHeaderView(entryView)
        recycler_view.adapter = adapter
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.bannerData.observe(this, Observer {
            if (it.size > 0)
                banner.setRvBannerData(it)
        })
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

    private fun setEntryViewEvent(entryView: View) {
        entryView.findViewById<View>(R.id.tv_entry_day_problem).setOnClickListener {
            context?.let {
                ArticleListActivity.startActivity(it, 440, GlobalUtil.getString(R.string.text_one_day_one_problem))
            }
        }
        entryView.findViewById<View>(R.id.tv_entry_interview_guide).setOnClickListener {
            context?.let {
                ArticleListActivity.startActivity(it, 73, GlobalUtil.getString(R.string.text_interview_guide))
            }
        }
        entryView.findViewById<View>(R.id.tv_entry_knowledge_system).setOnClickListener {
            (activity as MainActivity).openKnowledgePage()
        }

        entryView.findViewById<View>(R.id.tv_entry_web_guide).setOnClickListener {
            (activity as MainActivity).openNavigationPage()
        }
    }
}