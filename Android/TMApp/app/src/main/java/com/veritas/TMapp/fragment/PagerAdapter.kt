package com.veritas.TMapp.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
// 뷰페이저의 페이지뷰를 생성하기 위해 사용되는 어댑터 클래스
class PagerAdapter(manager: FragmentManager): FragmentPagerAdapter(manager)
{
    private val fragmentList = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment = fragmentList[position]
    override fun getCount(): Int = titleList.size
    override fun getPageTitle(position: Int): CharSequence = titleList[position]

    fun addFragment(fragment: Fragment, title: String)
    {
        fragmentList.add(fragment)
        titleList.add(title)
    }

}