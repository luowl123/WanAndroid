package me.luowl.wan.ui

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import me.luowl.wan.R
import me.luowl.wan.databinding.ActivityMainBinding
import me.luowl.wan.ui.account.MineFragment
import me.luowl.wan.ui.home.HomeFragment
import me.luowl.wan.ui.project.ProjectFragment
import me.luowl.wan.util.GlobalUtil
import me.luowl.wan.util.logDebug


class MainActivity : AppCompatActivity() {
    private lateinit var mFragments: MutableList<Fragment>
    private var mCurrentTabIndex = 0
    private val tabViews:MutableList<View> =ArrayList()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = MainViewModel()
        binding.mainViewModel = mainViewModel
        removeOldFragment()
        initTabLayout()
        initFragments()
        initCurrentTab()
    }

    private fun removeOldFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val oldFragments = supportFragmentManager.fragments
        oldFragments.forEach {
            transaction.remove(it)
        }
        transaction.commit()
    }

    private fun initTabLayout() {
        val itemSize = NAV_TITLES.size
        for (position in 0 until itemSize) {
            val tabItem = binding.tabLayout.newTab()
            tabItem.text = NAV_TITLES[position]
            tabItem.setIcon(NAV_ICON_NORMAL[position])
            val tabView = createTabView(position)
            tabItem.customView = tabView
            binding.tabLayout.addTab(tabItem)
            tabViews.add(tabView)
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                switchFragment(mCurrentTabIndex, tab.position)
            }
        })
    }

    private fun initFragments() {
        mFragments = mutableListOf()
        mFragments.add(HomeFragment())
        mFragments.add(ProjectFragment())
        mFragments.add(FindFragment())
        mFragments.add(MineFragment())

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_change, mFragments[2]).hide(mFragments[2]).commit()
    }

    private fun initCurrentTab() {
        val temp = 0
        val selectTabItem = binding.tabLayout.getTabAt(temp)
        selectTabItem?.let {
            changeTabSelect(it)
            val transaction = supportFragmentManager.beginTransaction()
            val targetFragment = mFragments[temp]
            if (targetFragment.isAdded) {
                transaction.show(targetFragment).commit()
            } else {
                transaction.add(R.id.fl_change, targetFragment).show(targetFragment).commit()
            }
            mCurrentTabIndex = temp
            it.select()
        }
    }

    fun switchFragment(fromID: Int, toID: Int) {
        logDebug("switchFragment fromID:$fromID toID:$toID")
        if (fromID == toID || toID < 0 || toID >= NAV_TITLES.size) {
            return
        }
        val fromFragment = mFragments[fromID]
        val toFragment = mFragments[toID]
        val transaction = supportFragmentManager.beginTransaction()
        if (!toFragment.isAdded) {
            transaction.add(R.id.fl_change, toFragment)
                .show(toFragment)
                .hide(fromFragment)
                .commit()
        } else {
            transaction.show(toFragment).hide(fromFragment).commit()
        }
        mCurrentTabIndex = toID
        changeTabNormal(tab_layout.getTabAt(fromID)!!)
        changeTabSelect(tab_layout.getTabAt(mCurrentTabIndex)!!)

    }

    private fun createTabView(position: Int): View {
        val v = LayoutInflater.from(applicationContext).inflate(R.layout.nav_tab_item, null)
        val textView = v.findViewById(R.id.nav_text) as TextView
        val imageView = v.findViewById(R.id.nav_icon) as ImageView
        textView.text = NAV_TITLES[position]
        textView.setTextColor(ContextCompat.getColor(this, R.color.color_6))
        imageView.setImageResource(NAV_ICON_NORMAL[position])
        imageView.setColorFilter(ContextCompat.getColor(this, R.color.color_6))
        return v
    }

    private fun changeTabSelect(tab: TabLayout.Tab) {
        tabViews[tab.position].let {
            logDebug("changeTabSelect $mCurrentTabIndex")
            val imageView = it.findViewById(R.id.nav_icon) as ImageView
            val textView = it.findViewById(R.id.nav_text) as TextView
            textView.setTextColor(ContextCompat.getColor(this, R.color.appThemeColor))
            imageView.setImageResource(NAV_ICON_SELECTED[tab.position])
            imageView.setColorFilter(ContextCompat.getColor(this, R.color.appThemeColor))
        }
    }

    private fun changeTabNormal(tab: TabLayout.Tab) {
        tabViews[tab.position].let {
            logDebug("changeTabNormal $mCurrentTabIndex")
            val imageView = it.findViewById(R.id.nav_icon) as ImageView
            val textView = it.findViewById(R.id.nav_text) as TextView
            textView.setTextColor(ContextCompat.getColor(this, R.color.color_6))
            imageView.setImageResource(NAV_ICON_NORMAL[tab.position])
            imageView.setColorFilter(ContextCompat.getColor(this, R.color.color_6))
        }
    }

    fun openNavigationPage() {
        selectFindFragmentPage(2)
    }

    fun openKnowledgePage() {
        selectFindFragmentPage(1)
    }

    private fun selectFindFragmentPage(index: Int) {
        switchFragment(mCurrentTabIndex, 2)
        val tabAt = tab_layout.getTabAt(2)
        logDebug("tab_layout.getTabAt(2):$tabAt")
        tab_layout.selectTab(tabAt)
        val fragment = mFragments[2] as FindFragment
        if (fragment.isAdded) {
            fragment.setCurrentPage(index)
        } else {
            tab_layout.postDelayed({
                fragment.setCurrentPage(index)
            }, 300)
        }
    }

    private var mLastBackExit: Long = 0
    override fun onBackPressed() {
        if (SystemClock.elapsedRealtime() - mLastBackExit > 3000) {
            GlobalUtil.showToastShort("再按一次退出程序")
            mLastBackExit = SystemClock.elapsedRealtime()
            return
        }
        super.onBackPressed()
    }

    companion object {
        val NAV_TITLES = arrayOf("首页", "项目", "看看", "我的")

        val NAV_ICON_NORMAL = intArrayOf(
            R.mipmap.ic_nav_home_normal,
            R.mipmap.ic_nav_project_normal,
            R.mipmap.ic_nav_wx_chapters_normal,
            R.mipmap.ic_nav_mine_noraml
        )

        val NAV_ICON_SELECTED = intArrayOf(
            R.mipmap.ic_nav_home_selected,
            R.mipmap.ic_nav_project_selected,
            R.mipmap.ic_nav_wx_chapters_selected,
            R.mipmap.ic_nav_mine_selected
        )
    }

}
