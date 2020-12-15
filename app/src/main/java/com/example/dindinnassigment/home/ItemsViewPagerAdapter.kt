package com.example.dindinnassigment.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.dindinnassigment.items.DrinksFragment
import com.example.dindinnassigment.items.ItemsFragment
import com.example.dindinnassigment.items.SushiFragment

class ItemsViewPagerAdapter constructor(fragmentManager: FragmentManager?) : FragmentPagerAdapter(
    (fragmentManager)!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return ItemsFragment()
        } else if (position == 1) {
            return SushiFragment()
        } else if (position == 2) {
            return DrinksFragment()
        } else {
            return ItemsFragment()
        }
    }

    public override fun getCount(): Int {
        return 3
    }

    public override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0) {
            return "Pizza"
        } else if (position == 1) {
            return "Sushi"
        } else if (position == 2) {
            return "Drinks"
        } else {
            return "Pizza"
        }
    }
}