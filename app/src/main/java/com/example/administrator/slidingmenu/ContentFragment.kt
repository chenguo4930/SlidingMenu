package com.example.administrator.slidingmenu

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.contentfragment.*

/**
 * @author ChengGuo
 * @date 2018/10/30
 */
class ContentFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.contentfragment, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_content.run {
            text = arguments!!.getString(KEY)
            setTextColor(Color.BLUE)
        }
        initRV()
    }

    private fun initRV() {
        val dataList = ArrayList<TestData>()
        for (i in 0..30) {
            dataList.add(TestData(i.toString()))
        }

        recycler_view.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = RvAdapter(activity!!, dataList)
        }
    }

    companion object {
        private const val KEY = "title"

        /**
         * fragment静态传值
         */
        fun newInstance(str: String): ContentFragment {
            val fragment = ContentFragment()
            val bundle = Bundle()
            bundle.putString(KEY, str)
            fragment.arguments = bundle
            return fragment
        }
    }
}