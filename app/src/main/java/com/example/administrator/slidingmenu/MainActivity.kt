package com.example.administrator.slidingmenu

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private var mTabLayout: TabLayout? = null
    private var mViewPager: ViewPager? = null

    private val mInflater: LayoutInflater? = null
    private val mTitleList = null//页卡标题集合
    private val view1: View? = null
    private val view2: View? = null
    private val view3: View? = null
    private val view4: View? = null
    private val view5: View? = null//页卡视图
    private val mViewList = null//页卡视图集合
    private var listTitles: MutableList<String>? = null
    private var fragments: MutableList<Fragment>? = null
    private var listTextViews: List<TextView>? = null

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewPager = findViewById<ViewPager>(R.id.vp_view)
        mTabLayout = findViewById<TabLayout>(R.id.tabs)

        initData()
    }

    private fun initData() {
        listTitles = ArrayList()
        fragments = ArrayList()
        listTextViews = ArrayList()

        listTitles!!.add("推荐")
        listTitles!!.add("热点")
        listTitles!!.add("视频")
        listTitles!!.add("北京")
        listTitles!!.add("社会")
        listTitles!!.add("娱乐")
        listTitles!!.add("问答")
        listTitles!!.add("图片")
        listTitles!!.add("科技")
        listTitles!!.add("汽车")
        listTitles!!.add("体育")
        listTitles!!.add("财经")
        listTitles!!.add("军事")
        listTitles!!.add("国际")
        for (i in listTitles!!.indices) {
            val fragment = ContentFragment.newInstance(listTitles!![i])
            fragments!!.add(fragment)

        }
        //mTabLayout.setTabMode(TabLayout.SCROLL_AXIS_HORIZONTAL);//设置tab模式，当前为系统默认模式
        for (i in listTitles!!.indices) {
            mTabLayout!!.addTab(mTabLayout!!.newTab().setText(listTitles!![i]))//添加tab选项
        }

        val mAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragments!![position]
            }

            override fun getCount(): Int {
                return fragments!!.size
            }

            //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
            override fun getPageTitle(position: Int): CharSequence? {
                return listTitles!![position]
            }
        }
        mViewPager!!.adapter = mAdapter

        mTabLayout!!.setupWithViewPager(mViewPager)//将TabLayout和ViewPager关联起来。
        mTabLayout!!.setTabsFromPagerAdapter(mAdapter)//给Tabs设置适配器

    }
}