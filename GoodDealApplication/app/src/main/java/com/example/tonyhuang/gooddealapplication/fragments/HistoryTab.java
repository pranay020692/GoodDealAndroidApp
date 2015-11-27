package com.example.tonyhuang.gooddealapplication.fragments;

/**
 * Created by TonyHuang on 10/29/15.
 */

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tonyhuang.gooddealapplication.R;
import com.example.tonyhuang.gooddealapplication.adapters.HistoryAdapter;
import com.example.tonyhuang.gooddealapplication.data.ProductsDataSource;
import com.example.tonyhuang.gooddealapplication.models.History;

import java.sql.SQLException;
import java.util.ArrayList;

public class HistoryTab extends Fragment {

    private ArrayList<History> histories;
    private ProductsDataSource productsDataSource;
    public  HistoryTab CustomListView = null;
    private ListView historyListView;
    private HistoryAdapter historyAdapter ;
    private Activity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_list,container,false);

        historyListView = (ListView) v.findViewById(R.id.list_for_history);

        productsDataSource = new ProductsDataSource(getActivity());
        CustomListView = this;
        try {
            productsDataSource.open();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        histories = productsDataSource.getAllHistory();
        productsDataSource.close();

        Resources res = getResources();

        historyAdapter=new HistoryAdapter(mActivity, histories, res );
        historyListView.setAdapter( historyAdapter );
        return v;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}