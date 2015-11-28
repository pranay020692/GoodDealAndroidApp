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

//<<<<<<< Updated upstream
//=======
//>>>>>>> Stashed changes

public class SearchTab extends Fragment {
    //Button compareBtn, barcodeBtn;

   /* public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compareBtn = (Button) findViewById(R.id.button);
        compareBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startResultListActivity(view);
            }
        });
    }*/
   FragmentManager fragmentManager;

    private String enteredNameString ;
    private String enteredPriceString;

/*<<<<<<< Updated upstream
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


=======*/
    EditText enteredNameView;
    EditText enteredPriceView;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.search_tab,container,false);
//<<<<<<< Updated upstream
        enteredNameView = (EditText)view.findViewById(R.id.entered_product_name_id);
        enteredPriceView = (EditText)view.findViewById(R.id.entered_product_price_id);

        enteredNameString = enteredPriceView.getText().toString();
        enteredPriceString = enteredPriceView.getText().toString();
//=======
        context = view.getContext();
        view.findViewById(R.id.compare_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*//Log.i("DealsTab", "This is a test");
                DealsTab dealsTab = new DealsTab();
                //FragmentManager fragmentManager = getFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                //fragmentTransaction.hide(SearchTab.this);
               // fragmentTransaction.add(R.id.searchTab, dealsTab);
                fragmentTransaction.replace(R.id.mainLayout, dealsTab);
                fragmentTransaction.addToBackStack(null);
                //getFragmentManager().executePendingTransactions();
                fragmentTransaction.commit();*/

                //startResultListActivity(view);
                //Intent intent = new Intent(context, SearchForResultActivity.class);
                //intent.putExtra("trip", trip);
                //startActivity(intent);

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
                Toast.makeText( getContext() , "scan", Toast.LENGTH_SHORT).show();
            }
        });

//>>>>>>> Stashed changes
        return view;
    }


}
