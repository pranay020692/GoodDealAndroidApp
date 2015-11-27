package com.example.tonyhuang.gooddealapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tonyhuang.gooddealapplication.R;
import com.example.tonyhuang.gooddealapplication.models.WishList;

import java.util.ArrayList;

/**
 * Created by puneet on 11/26/15.
 */

public class WishListAdapter extends BaseAdapter implements View.OnClickListener {


    private Activity activity;
    private ArrayList<WishList> wishList;
    private static LayoutInflater inflater=null;
    public Resources res;
    private WishList tempValues = null;


    int i=0;


    public WishListAdapter(Activity activity, ArrayList<WishList> wishList,Resources res) {


        this.activity = activity;
        this.wishList = wishList;
        this.res = res;


        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {

        if(wishList.size()<=0)
            return 1;
        return wishList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView productNameView ;
        public TextView productSequenceView;

    }


    public View getView(int position, View convertView, ViewGroup parent) {


        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){


            vi = inflater.inflate(R.layout.wishlist_tab, null);


            holder = new ViewHolder();
            holder.productNameView= (TextView) vi.findViewById(R.id.product_name_wishlist);
            holder.productSequenceView = (TextView) vi.findViewById(R.id.sequence_number_wishlist);



            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(wishList.size()<=0)
        {
            holder.productNameView.setText("No Data");

        }
        else
        {

            tempValues=null;
            tempValues = ( WishList) wishList.get( position );


            holder.productNameView.setText(tempValues.getName());
            holder.productSequenceView.setText(String.valueOf(tempValues.getId()));



            // vi.setOnClickListener(new OnItemClickListener( position ));
        }

        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

/*
    private class OnItemClickListener  implements View.OnClickListener {

        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {

        }
    }
    */
}
