package com.example.tonyhuang.gooddealapplication.fragments;

/**
 * Created by TonyHuang on 10/29/15.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tonyhuang.gooddealapplication.R;
import com.example.tonyhuang.gooddealapplication.activities.BarCodeScanner;
import com.example.tonyhuang.gooddealapplication.activities.searchActivity;
import com.example.tonyhuang.gooddealapplication.data.ProductsDataSource;

import java.sql.SQLException;

public class SearchTab extends Fragment {
    private String enteredNameString = "";
    private String enteredPriceString = "";
    private ProductsDataSource productsDataSource;
    EditText enteredPriceView;
    Context context;
    AutoCompleteTextView autocompletetextview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tab, container, false);
        productsDataSource = new ProductsDataSource(getActivity());
        try {
            productsDataSource.open();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        autocompletetextview = (AutoCompleteTextView) view.findViewById(R.id.entered_product_name_id);
        enteredPriceView = (EditText) view.findViewById(R.id.entered_product_price_id);
        if (productsDataSource.getAllHistory().size() > 0) {
            String[] arr = new String[productsDataSource.getAllHistory().size()];
            for (int i = 0; i < productsDataSource.getAllHistory().size(); i++) {
                arr[i] = productsDataSource.getAllHistory().get(i).getName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, arr);
            autocompletetextview.setAdapter(adapter);
        }
        context = view.getContext();
        view.findViewById(R.id.compare_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredNameString = autocompletetextview.getText().toString();
                enteredPriceString = enteredPriceView.getText().toString();
                if(enteredPriceString.equals("")){
                    enteredPriceString = "0.0";
                }
                Intent searchIntent = new Intent(getActivity(), searchActivity.class);
                searchIntent.putExtra("entered_name", String.valueOf(enteredNameString));
                searchIntent.putExtra("entered_price",  String.valueOf(enteredPriceString));
                startActivity(searchIntent);
            }
        });

        view.findViewById(R.id.button_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanIntent = new Intent(getActivity(), BarCodeScanner.class);
                startActivity(scanIntent);
                Toast.makeText(getContext(), "scan", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
