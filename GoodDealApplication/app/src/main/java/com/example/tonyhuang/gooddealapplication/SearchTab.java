package com.example.tonyhuang.gooddealapplication;

/**
 * Created by TonyHuang on 10/29/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SearchTab extends Fragment {

    private String enteredNameString ;
    private String enteredPriceString;

    private EditText enteredNameView;
    private EditText enteredPriceView;
    private Button compareButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.search_tab,container,false);
        enteredNameView = (EditText)v.findViewById(R.id.entered_product_name_id);
        enteredPriceView = (EditText) v.findViewById(R.id.entered_product_price_id);
        compareButton = (Button) v.findViewById(R.id.compare_button_id);


        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity(), searchActivity.class);
                searchIntent.putExtra("entered_name", enteredNameView.getText().toString());
                searchIntent.putExtra("entered_price", enteredPriceView.getText().toString());
                startActivity(searchIntent);
            }
        });

        return v;
    }


}
