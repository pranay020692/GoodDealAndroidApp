package com.example.tonyhuang.gooddealapplication.fragments;

/**
 * Created by TonyHuang on 10/31/15.
 */

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tonyhuang.gooddealapplication.R;
import com.example.tonyhuang.gooddealapplication.adapters.CustomAdapter;
import com.example.tonyhuang.gooddealapplication.data.ProductsDataSource;
import com.example.tonyhuang.gooddealapplication.models.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class DealsTab extends Fragment {
    //static String Tag = "DealTab";
    //ViewPagerAdapter adapter;
    AppCompatActivity listener;
    TextView nameText, priceText, storeText;
    //public ListView list;
    //SetList listCallback;

    public ProductsDataSource productsDataSource;
    private ArrayList<Product> CustomListViewValuesArr = new ArrayList<Product>();
    private CustomAdapter adapter;
    private Resources res;
    private ListView list;
    String price;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        bundle = this.getArguments();
        String fromSearch = bundle.getString("list");
        if (fromSearch.equals("noList")) {
            view = inflater.inflate(R.layout.result_tab_nodata, container, false);
        } else if (fromSearch.equals("List")) {
            view = inflater.inflate(R.layout.result_tab, container, false);
            price = bundle.getString("price"); //enteredPrice
            list = (ListView) view.findViewById(R.id.list);  // List defined in XML ( See Below )
            res = getResources();
            setListData();
            adapter = new CustomAdapter(getActivity(), CustomListViewValuesArr, res, price);
            list.setAdapter(adapter);
        } else {
            view = inflater.inflate(R.layout.result_tab_nodata, container, false);
        }
        return view;
    }

    public void setListData() {
        productsDataSource = new ProductsDataSource(getActivity());
        try {
            productsDataSource.open();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        CustomListViewValuesArr = productsDataSource.getAllProducts();
        productsDataSource.close();
    }
}
