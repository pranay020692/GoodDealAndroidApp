package com.example.tonyhuang.gooddealapplication;

/**
 * Created by TonyHuang on 10/31/15.
 */

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class DealsTab extends ListFragment {
    static String Tag = "DealTab";
    //ViewPagerAdapter adapter;
    AppCompatActivity listener;
    TextView nameText,priceText,storeText;
    private ListView list;
    SetList listCallback;

    //@Override
    //public void onCreate(Bundle savedInstanceState) {

    //super.onCreate(savedInstanceState);
    //}
    //@Override
    /*public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.result_tab,container,false);

        /*nameText = (TextView)view.findViewById(R.id.textView4);
        nameText.setCursorVisible(true
        );*/

        list= (ListView)view.findViewById(R.id.list);  // List defined in XML ( See Below )
        //setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, presidents));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listCallback = (SetList) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SetList");
        }
    }

    public interface SetList {
        public void setlistview (CustomAdapter adapter);
    }



}
