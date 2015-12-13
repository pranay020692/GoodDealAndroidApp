package com.example.tonyhuang.gooddealapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyhuang.gooddealapplication.R;
import com.example.tonyhuang.gooddealapplication.models.WishList;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by puneet on 11/26/15.
 */

public class WishListAdapter extends BaseAdapter implements View.OnClickListener {


    private Activity activity;
    private ArrayList<WishList> wishList;
    private static LayoutInflater inflater = null;
    public Resources res;
    private WishList tempValues = null;
    private int sequenceNumber = 0;


    int i = 0;


    public WishListAdapter(Activity activity, ArrayList<WishList> wishList, Resources res) {


        this.activity = activity;
        this.wishList = wishList;
        this.res = res;


        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {

        if (wishList.size() <= 0)
            return 1;
        return wishList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class ViewHolder {

        public TextView productNameView;
        public TextView productSequenceView;
        public ImageView imageView;
        public TextView priceView;

    }


    public View getView(int position, View convertView, ViewGroup parent) {


        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {


            vi = inflater.inflate(R.layout.wishlist_tab, null);


            holder = new ViewHolder();
            holder.productNameView = (TextView) vi.findViewById(R.id.product_name_view);
            //holder.productSequenceView = (TextView) vi.findViewById(R.id.sequence_number_wishlist);
            holder.imageView = (ImageView) vi.findViewById(R.id.product_image_view);
            holder.priceView = (TextView) vi.findViewById(R.id.product_price_view);
            //final String avatarURL = "http://images.bestbuy.com/BestBuy_US/images/products/" + tempValues.getProductId().substring(0, 4) + "/" + tempValues.getProductId() + "_s.gif";
            //new DownloadImageTask(holder.imageView).execute(avatarURL);


            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (wishList.size() <= 0) {
            holder.productNameView.setText("No Wish List");
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.priceView.setVisibility(View.INVISIBLE);

        } else {

            tempValues = null;
            tempValues = (WishList) wishList.get(position);


            holder.productNameView.setText(tempValues.getName());
            //String str = tempValues.getImageUrl();
            //holder.imageView.setImageURI(Uri.parse(tempValues.getImageUrl()));
            //holder.productSequenceView.setText(String.valueOf(sequenceNumber));
            holder.priceView.setText("$ " + tempValues.getPrice());
            final String avatarURL = tempValues.getImageUrl();
            new DownloadImageTask(holder.imageView).execute(avatarURL);


            // vi.setOnClickListener(new OnItemClickListener( position ));
        }

        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
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
