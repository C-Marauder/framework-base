package com.xqy.androidx.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AppFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private lateinit var fragments:MutableList<Fragment>
    private lateinit var titles:MutableList<String>
    constructor(fragments:MutableList<Fragment>,fm: FragmentManager):this(fm){
        this.fragments = fragments
    }
    constructor(titles:MutableList<String>,fragments:MutableList<Fragment>,fm: FragmentManager):this(fm){
        this.fragments = fragments
        this.titles = titles
    }


    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return if (::titles.isInitialized){
            titles[position]
        }else{
            super.getPageTitle(position)

        }
    }
}