package me.luowl.wan.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/*
 *
 * Created by luowl
 * Date: 2019/7/25 
 * Desc：
 */

class CommonFragmentStatePagerAdapter(
    fm: FragmentManager,
    private val titles: List<String>,
    private val fragments: List<Fragment>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}