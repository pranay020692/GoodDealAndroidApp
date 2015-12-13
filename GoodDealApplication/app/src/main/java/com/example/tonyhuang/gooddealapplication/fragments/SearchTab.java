package com.example.tonyhuang.gooddealapplication.fragments;

/**
 * Created by TonyHuang on 10/29/15.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tonyhuang.gooddealapplication.R;
import com.example.tonyhuang.gooddealapplication.activities.BarCodeScanner;
import com.example.tonyhuang.gooddealapplication.activities.searchActivity;

public class SearchTab extends Fragment {

    FragmentManager fragmentManager;

    private String enteredNameString;
    private String enteredPriceString;


    EditText enteredNameView;
    EditText enteredPriceView;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tab, container, false);

        enteredNameView = (EditText) view.findViewById(R.id.entered_product_name_id);
        enteredPriceView = (EditText) view.findViewById(R.id.entered_product_price_id);

        enteredNameString = enteredPriceView.getText().toString();
        enteredPriceString = enteredPriceView.getText().toString();

        context = view.getContext();
        view.findViewById(R.id.compare_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent searchIntent = new Intent(getActivity(), searchActivity.class);
                searchIntent.putExtra("entered_name", enteredNameView.getText().toString());
                searchIntent.putExtra("entered_price", enteredPriceView.getText().toString());
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
