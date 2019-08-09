package me.luowl.wan.ui

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_find.*
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseFragment
import me.luowl.wan.base.adapter.CommonFragmentStatePagerAdapter
import me.luowl.wan.databinding.FragmentFindBinding
import me.luowl.wan.ui.architecture.ArchitectureFragment
import me.luowl.wan.ui.navigation.NavigationFragment
import me.luowl.wan.ui.wxarticle.WXArticleChaptersFragment
import me.luowl.wan.util.GlobalUtil
import me.luowl.wan.util.logDebug

/*
 *
 * Created by luowl
 * Date: 2019/7/26 
 * Desc：
 */

class FindFragment : BaseFragment<FragmentFindBinding, FindViewModel>() {

    override fun getViewModelClass(): Class<FindViewModel> = FindViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_find

    override fun initVariableId() = BR.viewModel

    var currentIndex = 0
    val tabs: ArrayList<TextView> = ArrayList()

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        val titles = listOf(
            GlobalUtil.getString(R.string.chapter),
            GlobalUtil.getString(R.string.system),
            GlobalUtil.getString(R.string.navigation)
        )
        tabs.addAll(listOf(tv_item_wx_chapters, tv_item_system, tv_item_guide))
        val fragments = ArrayList<Fragment>()
        fragments.add(WXArticleChaptersFragment())
        fragments.add(ArchitectureFragment())
        fragments.add(NavigationFragment())
        setItemSelected(tabs[currentIndex])
        view_pager.adapter = CommonFragmentStatePagerAdapter(childFragmentManager, titles, fragments)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                logDebug("select $position")
                setItemUnSelected(tabs[currentIndex])
                setItemSelected(tabs[position])
                currentIndex = position
            }
        })
        setItemClickEvent()
    }

    private fun setItemClickEvent() {
        tv_item_wx_chapters.setOnClickListener {
            view_pager.currentItem = 0
        }
        tv_item_system.setOnClickListener {
            view_pager.currentItem = 1
        }
        tv_item_guide.setOnClickListener {
            view_pager.currentItem = 2
        }
    }

    fun setItemSelected(textView: TextView) {
        textView.setTextColor(GlobalUtil.getColor(R.color.white))
    }

    fun setItemUnSelected(textView: TextView) {
        textView.setTextColor(GlobalUtil.getColor(R.color.half_white))
    }

    fun setCurrentPage(index:Int){
        view_pager.currentItem = index
    }
}