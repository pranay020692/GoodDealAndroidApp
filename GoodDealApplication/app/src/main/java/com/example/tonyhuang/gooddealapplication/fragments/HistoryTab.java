package com.example.tonyhuang.gooddealapplication.fragments;

/**
 * Created by TonyHuang on 10/29/15.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyhuang.gooddealapplication.models.History;
import com.example.tonyhuang.gooddealapplication.R;

import java.util.List;

public class HistoryTab extends Fragment {

    List<History> histories;
    //ProductsDataSource productsDataSource;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_tab,container,false);
        /*
        try {
            productsDataSource.open();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        histories = productsDataSource.getAllHistory();*/
        return v;

    }
}