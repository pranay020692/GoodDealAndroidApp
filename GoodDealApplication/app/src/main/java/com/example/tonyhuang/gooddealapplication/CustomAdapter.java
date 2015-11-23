package com.example.tonyhuang.gooddealapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by puneet on 11/12/15.
 */
    public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

        private static double PERCENTAGE_POINT_1 = 0.1;
        private static double PERCENTAGE_POINT_2 = 0.05;
        private static double PERCENTAGE_POINT_3 = 0.04;
        private static double PERCENTAGE_POINT_4 = 0.03;
        private static double PERCENTAGE_POINT_5 = 0.02;
        private static double PERCENTAGE_POINT_6 = 0.015;
        private static double PERCENTAGE_POINT_7 = 0.01;
        private Activity activity;
        private ArrayList<Product> products;
        private static LayoutInflater inflater=null;
        public Resources res;
        Product tempValues=null;
        int i=0;
        private double enteredPrice;

        public CustomAdapter(Activity activity, ArrayList<Product> products,Resources res, String enteredPrice) {


            this.activity = activity;
            this.products = products;
            this.res = res;
            this.enteredPrice = Double.parseDouble(enteredPrice);


            inflater = ( LayoutInflater )activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public int getCount() {

            if(products.size()<=0)
                return 1;
            return products.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        /********* Create a holder Class to contain inflated xml file elements *********/
        public static class ViewHolder{

            public TextView productNameView;
            public TextView productPriceView;
            public TextView productGoodDealView;

        }


        public View getView(int position, View convertView, ViewGroup parent) {

            View vi = convertView;
            ViewHolder holder;

            if(convertView==null){


                vi = inflater.inflate(R.layout.product_in_list_view, null);


                holder = new ViewHolder();
                holder.productNameView = (TextView) vi.findViewById(R.id.display_name_id);
                holder.productPriceView=(TextView)vi.findViewById(R.id.display_price_id);
                holder.productGoodDealView = (TextView) vi.findViewById(R.id.display_good_deal_id);



                vi.setTag( holder );
            }
            else
                holder=(ViewHolder)vi.getTag();

            if(products.size()<=0)
            {
                holder.productNameView.setText("No Data");

            }
            else
            {

                tempValues=null;
                tempValues = ( Product ) products.get( position );


                int loopCounter = 0;

                holder.productNameView.setText(tempValues.getProductName());
                holder.productPriceView.setText(tempValues.getProductPrice());


                compareAndSetText(holder);

                vi.setOnClickListener(new OnItemClickListener(position));
            }
            return vi;
        }

    public void compareAndSetText(ViewHolder holder){

        double productPrice = Double.parseDouble(tempValues.getProductPrice());

        double difference = enteredPrice - productPrice;

        if(productPrice < 20.0){
            if((productPrice < enteredPrice) && (difference <= PERCENTAGE_POINT_1*productPrice)){
                holder.productGoodDealView.setText("Its a Good Deal !!");
            }
            else {
                holder.productGoodDealView.setText("Its a Bad Deal");
            }

        }

        if((productPrice >= 20.0) && (productPrice < 50.0)){
            if((productPrice < enteredPrice) && (difference <= PERCENTAGE_POINT_2*productPrice)){
                holder.productGoodDealView.setText("Its a Good Deal !!");
            }
            else {
                holder.productGoodDealView.setText("Its a Bad Deal");
            }

        }

        if((productPrice >= 40.0) && (productPrice < 120.0)){
            if((productPrice < enteredPrice) && (difference <= PERCENTAGE_POINT_3*productPrice)){
                holder.productGoodDealView.setText("Its a Good Deal !!");
            }
            else {
                holder.productGoodDealView.setText("Its a Bad Deal");
            }

        }

        if((productPrice >= 120.0) && (productPrice < 300.0)){
            if((productPrice < enteredPrice) && (difference <= PERCENTAGE_POINT_4*productPrice)){
                holder.productGoodDealView.setText("Its a Good Deal !!");
            }
            else {
                holder.productGoodDealView.setText("Its a Bad Deal");
            }

        }

        if((productPrice >= 300.0) && (productPrice < 600.0)){
            if((productPrice < enteredPrice) && (difference <= PERCENTAGE_POINT_5*productPrice)){
                holder.productGoodDealView.setText("Its a Good Deal !!");
            }
            else {
                holder.productGoodDealView.setText("Its a Bad Deal");
            }

        }

        if((productPrice >= 600.0) && (productPrice < 1000.0)){
            if((productPrice < enteredPrice) && (difference <= PERCENTAGE_POINT_6*productPrice)){
                holder.productGoodDealView.setText("Its a Good Deal !!");
            }
            else {
                holder.productGoodDealView.setText("Its a Bad Deal");
            }

        }

        if(productPrice >= 1000.0){
            if((productPrice < enteredPrice) && (difference <= PERCENTAGE_POINT_7*productPrice)){
                holder.productGoodDealView.setText("Its a Good Deal !!");
            }
            else {
                holder.productGoodDealView.setText("Its a Bad Deal");
            }

        }

    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }


    private class OnItemClickListener  implements View.OnClickListener {

        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            searchActivity viewTripActivity = (searchActivity)activity;


            viewTripActivity.onItemClick(mPosition);
        }
    }
}
