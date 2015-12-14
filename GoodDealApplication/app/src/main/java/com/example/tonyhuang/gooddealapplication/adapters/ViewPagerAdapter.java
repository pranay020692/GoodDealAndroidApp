package com.example.tonyhuang.gooddealapplication.adapters;

/**
 * Created by TonyHuang on 10/29/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tonyhuang.gooddealapplication.fragments.DealsTab;
import com.example.tonyhuang.gooddealapplication.fragments.HistoryTab;
import com.example.tonyhuang.gooddealapplication.fragments.SearchTab;
import com.example.tonyhuang.gooddealapplication.fragments.WishListTab;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private Bundle bundle;

    private CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private String where;
    private String price;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, String where) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.where = where;

    }

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, String where, String price) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.where = where;
        this.price = price;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) // if the position is 0 we are returning the First tab
        {
            SearchTab searchTab = new SearchTab();
            return searchTab;
        } else if (position == 1)          // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            bundle = new Bundle();
            if (where.equals("main")) {
                bundle.putString("list", "noList");
            } else if (where.equals("search")) {
                bundle.putString("list", "List");
                bundle.putString("price", price);
            }
            DealsTab dealsTab = new DealsTab();
            dealsTab.setArguments(bundle);
            return dealsTab;
        } else if (position == 2) {
            HistoryTab historyTab = new HistoryTab();
            return historyTab;
        } else {
            WishListTab wishListTab = new WishListTab();
            return wishListTab;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}