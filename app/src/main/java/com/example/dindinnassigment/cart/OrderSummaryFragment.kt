package com.example.dindinnassigment.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.andremion.counterfab.CounterFab
import com.example.dindinnassigment.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class OrderSummaryFragment : Fragment() {
    var mToolbar: Toolbar? = null
    var mAppBarLayout: AppBarLayout? = null
    var mTabLayout: TabLayout? = null
    var mContainerView: View? = null
    var mViewPager: ViewPager? = null
    var mAdapter: ViewPagerAdapter? = null
    var cart_fab: CounterFab? = null
    var img_back: AppCompatImageView? = null
    var fab_payment: FloatingActionButton? = null
    internal var view: View? = null


    //Overriden method onCreateView
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.order_summary_fragment, container, false)
        findViews()
        setUpViews()

        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun findViews() {
        cart_fab = view!!.findViewById(R.id.cart_fab)
        mToolbar = view!!.findViewById(R.id.toolbar)
        img_back = view!!.findViewById(R.id.img_back)
        fab_payment = view!!.findViewById(R.id.fab_payment)
        mAppBarLayout = view!!.findViewById(R.id.app_bar)
        mTabLayout = view!!.findViewById(R.id.tabLayout)
        mContainerView = view!!.findViewById(R.id.view_container)
        mViewPager = view!!.findViewById(R.id.viewPager)
    }


    private fun setUpViews() {

        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar)
        if ((activity as AppCompatActivity?)!!.supportActionBar != null) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        if ((activity as AppCompatActivity?)!!.supportActionBar != null) {
            val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
            actionBar!!.setDisplayHomeAsUpEnabled(false)
        }
        mAdapter = ViewPagerAdapter(
            childFragmentManager
        )
        mViewPager!!.offscreenPageLimit = mAdapter!!.count
        mViewPager!!.adapter = mAdapter
        mTabLayout!!.setupWithViewPager(mViewPager)
        img_back!!.setOnClickListener(View.OnClickListener {
            (activity as AppCompatActivity?)!!.onBackPressed()
        })

        fab_payment!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Toast.makeText(context!!, "send for payment", Toast.LENGTH_SHORT).show()
            }
        })
    }


    class ViewPagerAdapter(fragmentManager: FragmentManager?) : FragmentPagerAdapter(
        (fragmentManager)!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                return CartFragment()
            } else if (position == 1) {
                return OrdersFragment()
            } else return if (position == 2) {
                return InfornationFragment()
            } else {
                return CartFragment()
            }
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            if (position == 0) {
                return "Cart"
            } else if (position == 1) {
                return "Orders"
            } else return if (position == 2) {
                "Information"
            } else {
                "Cart"
            }
        }
    }

}