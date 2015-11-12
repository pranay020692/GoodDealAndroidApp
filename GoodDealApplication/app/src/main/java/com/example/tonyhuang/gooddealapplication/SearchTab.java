package com.example.tonyhuang.gooddealapplication;

/**
 * Created by TonyHuang on 10/29/15.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SearchTab extends Fragment {

    private String enteredNameString ;
    private String enteredPriceString;

    EditText enteredNameView;
    EditText enteredPriceView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.search_tab,container,false);
        enteredNameView = (EditText)v.findViewById(R.id.entered_product_name_id);
        enteredPriceView = (EditText) v.findViewById(R.id.entered_product_price_id);

        enteredNameString = enteredPriceView.getText().toString();
        enteredPriceString = enteredPriceView.getText().toString();
        return v;
    }
}
