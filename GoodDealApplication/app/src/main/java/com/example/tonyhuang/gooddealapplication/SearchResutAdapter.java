package com.example.tonyhuang.gooddealapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by TonyHuang on 10/31/15.
 */
public class SearchResutAdapter extends ArrayAdapter {
    public SearchResutAdapter(Context context, int resource, ArrayList serchResult) {
        super(context, resource, serchResult);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        ResultList resultList = (ResultList) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.search_result_tab, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) view.findViewById(R.id.textView5);
        TextView price = (TextView) view.findViewById(R.id.textView7);
        TextView store = (TextView) view.findViewById(R.id.textView9);
        // Populate the data into the template view using the data object
        name.setText(resultList.name);
        price.setText(resultList.price);
        store.setText(resultList.store);
        // Return the completed view to render on screen
        return view;
    }
}
