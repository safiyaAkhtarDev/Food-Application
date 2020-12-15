package com.example.dindinnassigment.home

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.airbnb.mvrx.*
import com.andremion.counterfab.CounterFab
import com.example.dindinnassigment.R
import com.example.dindinnassigment.cart.CartItemViewModel
import com.example.dindinnassigment.cart.OrderSummaryFragment
import com.example.dindinnassigment.home.slider.SliderAdapter
import com.example.dindinnassigment.home.slider.SliderItem
import com.example.dindinnassigment.items.ItemsPOJO
import com.example.dindinnassigment.libs.AppBarStateChangeListener
import com.example.dindinnassigment.libs.Utils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeFragment : Fragment() {

    internal var view: View? = null
    var mToolbar: Toolbar? = null
    var mCollapsingToolbar: CollapsingToolbarLayout? = null
    var mAppBarLayout: AppBarLayout? = null
    var mTabLayout: TabLayout? = null
    var mToolbarTextView: TextView? = null
    var sliderView: SliderView? = null
    var mContainerView: View? = null
    var mViewPager: ViewPager? = null
    var mAdapterItems: ItemsViewPagerAdapter? = null
    var cart_fab: CounterFab? = null
    var linear_filter: LinearLayout? = null


    //Overriden method onCreateView
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.home_fragment, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews()
        setUpViews()
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = activity?.getSharedPreferences(
            context!!.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        ) ?: return
        val count = sharedPref.getInt(getString(R.string.cart_count), 0)
        cart_fab!!.count = count
    }


    @SuppressLint("UseRequireInsteadOfGet")
    private fun findViews() {
        cart_fab = view!!.findViewById(R.id.cart_fab)
        mToolbar = view!!.findViewById(R.id.toolbar)
        linear_filter = view!!.findViewById(R.id.linear_filter)
        mToolbarTextView = view!!.findViewById(R.id.toolbar_title)
        mCollapsingToolbar = view!!.findViewById(R.id.collapsing_toolbar)
        mAppBarLayout = view!!.findViewById(R.id.app_bar)
        mTabLayout = view!!.findViewById(R.id.tabLayout)
        sliderView = view!!.findViewById(R.id.imageSlider)
        mContainerView = view!!.findViewById(R.id.view_container)
        mViewPager = view!!.findViewById(R.id.viewPager)
    }


    private fun setUpViews() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar)

        if ((activity as AppCompatActivity?)!!.getSupportActionBar() != null) {
            (activity as AppCompatActivity?)!!.getSupportActionBar()!!
                .setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity?)!!.getSupportActionBar()!!
                .setDisplayShowHomeEnabled(true)
        }
        mCollapsingToolbar!!.setTitleEnabled(false)

        mCollapsingToolbar!!.requestLayout()
        if ((activity as AppCompatActivity?)!!.getSupportActionBar() != null) {
            val actionBar: ActionBar? = (activity as AppCompatActivity?)!!.getSupportActionBar()
            actionBar!!.setDisplayHomeAsUpEnabled(false)
        }

        cart_fab!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
//                send to cart fragment with animation
                val ordersummaryFragment = OrderSummaryFragment()
                val transaction = (activity as AppCompatActivity?)!!.supportFragmentManager
                    .beginTransaction()
                transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                transaction.replace(R.id.fragment_container, ordersummaryFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })
        // TODO : Hack for CollapsingToolbarLayout
        mToolbarTextView!!.setText("")

        actionBarResponsive()

        mAppBarLayout!!.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            public override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (mToolbarTextView != null) {
                    if (state == State.COLLAPSED) {
                        linear_filter!!.setAlpha(0f)
                        mToolbarTextView!!.setAlpha(1f)

                        cart_fab!!.setVisibility(View.VISIBLE)
                        mTabLayout!!.setVisibility(View.GONE)

                        mCollapsingToolbar!!.setContentScrimColor(
                            ContextCompat.getColor(context!!, R.color.white)
                        )
                    } else if (state == State.EXPANDED) {
                        mToolbarTextView!!.setAlpha(0f)
                        cart_fab!!.setVisibility(View.GONE)
                        mTabLayout!!.setVisibility(View.VISIBLE)
                        linear_filter!!.setAlpha(1f)
                        mCollapsingToolbar!!.setContentScrimColor(
                            ContextCompat
                                .getColor(context!!, android.R.color.white)
                        )
                    }
                }
            }

            public override fun onOffsetChanged(state: State?, offset: Float) {
                if (mToolbarTextView != null) {
                    if (state == State.IDLE) {
                        cart_fab!!.setVisibility(View.GONE)
                        mToolbarTextView!!.setAlpha(offset)

//                        linear_filter.setAlpha(offset);
                        mCollapsingToolbar!!.setContentScrimColor(
                            ArgbEvaluator()
                                .evaluate(
                                    offset, ContextCompat
                                        .getColor(context!!, android.R.color.white),
                                    ContextCompat.getColor(
                                        context!!,
                                        R.color.white
                                    )
                                ) as Int
                        )
                    }
                }
            }
        })

//        View pager for menu Items
        mAdapterItems =
            ItemsViewPagerAdapter(childFragmentManager)
        mViewPager!!.setOffscreenPageLimit(mAdapterItems!!.getCount())
        mViewPager!!.setAdapter(mAdapterItems)
        mTabLayout!!.setupWithViewPager(mViewPager)

//        Slider Images Adapter
        val adapter: SliderAdapter = SliderAdapter(requireContext())
        var sliderItem: SliderItem = SliderItem(R.mipmap.offer1)
        adapter.addItem(sliderItem)
        sliderItem = SliderItem(R.mipmap.offer2)
        adapter.addItem(sliderItem)
        sliderItem = SliderItem(R.mipmap.offer32)
        adapter.addItem(sliderItem)
        sliderView!!.setSliderAdapter(adapter)
        sliderView!!.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView!!.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView!!.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
        sliderView!!.setIndicatorSelectedColor(Color.WHITE)
        sliderView!!.setIndicatorUnselectedColor(Color.GRAY)
        sliderView!!.setScrollTimeInSec(4) //set scroll delay in seconds :
        sliderView!!.startAutoCycle()


        // If your CollapsingToolbarLayout too complex (e.g. ImageView into FrameLayout), then
        // your status bar may looks so buggy.
        // You can hotfix by this code when you need to use some 24.2.0's features,
        // or you can wait for Google fix this (24.2.1), or downgrade to 24.1.1.
        // The issue: http://goo.gl/FMWs37
        ViewCompat
            .setOnApplyWindowInsetsListener(
                (mContainerView)!!,
                object : OnApplyWindowInsetsListener {
                    public override fun onApplyWindowInsets(
                        v: View,
                        insets: WindowInsetsCompat
                    ): WindowInsetsCompat {
                        return insets.consumeSystemWindowInsets()
                    }
                })
    }

    private fun actionBarResponsive() {
        val actionBarHeight: Int = Utils.getActionBarHeightPixel(requireContext())
        val tabHeight: Int = Utils.getTabHeight(requireContext())
        if (actionBarHeight > 0) {
            mToolbar!!.getLayoutParams().height = 2
            mToolbar!!.requestLayout()
        }
    }
}