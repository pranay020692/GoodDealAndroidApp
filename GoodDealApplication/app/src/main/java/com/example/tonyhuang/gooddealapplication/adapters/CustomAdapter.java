package com.example.tonyhuang.gooddealapplication.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyhuang.gooddealapplication.R;
import com.example.tonyhuang.gooddealapplication.activities.getStoresforProduct;
import com.example.tonyhuang.gooddealapplication.activities.searchActivity;
import com.example.tonyhuang.gooddealapplication.data.ProductsDataSource;
import com.example.tonyhuang.gooddealapplication.models.Product;

import java.io.InputStream;
import java.sql.SQLException;
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
    private static LayoutInflater inflater = null;
    public Resources res;
    Product tempValues = null;
    int i = 0;
    private double enteredPrice;
    private ProductsDataSource productsDataSource;

    public CustomAdapter(Activity activity, ArrayList<Product> products, Resources res, String enteredPrice) {
        this.activity = activity;
        this.products = products;
        this.res = res;
        this.enteredPrice = Double.parseDouble(enteredPrice);


        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        if (products.size() <= 0)
            return 1;
        return products.size();
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
        public TextView productPriceView;
        public TextView productGoodDealView;
        public ImageView productImageView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        productsDataSource = new ProductsDataSource(activity);
        if (convertView == null) {
            vi = inflater.inflate(R.layout.product_in_list_view, null);
            holder = new ViewHolder();
            holder.productNameView = (TextView) vi.findViewById(R.id.display_name_id);
            holder.productPriceView = (TextView) vi.findViewById(R.id.display_price_id);
            holder.productGoodDealView = (TextView) vi.findViewById(R.id.display_good_deal_id);
            holder.productImageView = (ImageView) vi.findViewById(R.id.avatarView);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();
        if (products.size() <= 0) {
            holder.productNameView.setText("No Data");
        } else {
            tempValues = null;
            tempValues = (Product) products.get(position);
            int loopCounter = 0;
            holder.productNameView.setText(tempValues.getProductName());
            holder.productPriceView.setText(tempValues.getProductPrice());
            final String avatarURL = tempValues.getProductImageUrl();
            new DownloadImageTask(holder.productImageView).execute(avatarURL);
            if (enteredPrice != 0.0) {
                compareAndSetText(holder);
            } else {
                holder.productGoodDealView.setVisibility(View.INVISIBLE);
            }
            try {
                productsDataSource.open();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            //vi.setOnClickListener(new OnItemClickListener(position));
            vi.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View arg) {
                    try {
                        productsDataSource.open();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(arg.getContext()).create();
                    alertDialog.setTitle(tempValues.getProductName());
                    alertDialog.setMessage(tempValues.getProductPrice());
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Wish list",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO: ADD TO WISHLIST
                                    productsDataSource.createWishList(tempValues.getProductName(), tempValues.getProductImageUrl(), tempValues.getProductPrice());
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "BUY",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (tempValues.getProductImageUrl().contains("BestBuy")) {
                                        searchActivity.gotoProduct(tempValues.getProductId(), activity);
                                        dialog.dismiss();
                                    } else {
                                        searchActivity.gotoProductWalmart(tempValues.getProductId(), activity);
                                        dialog.dismiss();
                                    }
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nearby Stores",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(arg.getContext(), "dsa", Toast.LENGTH_SHORT).show();
                                    showStores(tempValues.getProductId(), arg.getContext());
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
        }
        productsDataSource.close();
        return vi;
    }

    public void compareAndSetText(ViewHolder holder) {
        double productPrice = Double.parseDouble(tempValues.getProductPrice());
        double productRating = 0.0;
        if (!tempValues.getProductAvgRating().equals("null")) {
            productRating = Double.parseDouble(tempValues.getProductAvgRating());
        }
        double difference = productPrice - enteredPrice;
        holder.productGoodDealView.setTextColor(Color.WHITE);
        if (productPrice < 20.0) {
            if ((productPrice < enteredPrice) && (productPrice >= 4.0)) {
                holder.productGoodDealView.setText("Its a Good Deal !!");
                holder.productGoodDealView.setTextColor(Color.GREEN);
            } else if ((productPrice < enteredPrice) && (productRating == 0.0)) {
                holder.productGoodDealView.setText("Its a Fair Deal");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else if ((productPrice > enteredPrice) && (difference <= PERCENTAGE_POINT_1 * enteredPrice)) {
                holder.productGoodDealView.setText("Its a fair Deal");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else {
                holder.productGoodDealView.setText("Its a Bad Deal");
                holder.productGoodDealView.setTextColor(Color.RED);
            }
        }
        if ((productPrice >= 20.0) && (productPrice < 50.0)) {
            if ((productPrice < enteredPrice) && (productRating >= 4.0)) {
                holder.productGoodDealView.setText("Its a Good Deal !!");
                holder.productGoodDealView.setTextColor(Color.GREEN);
            } else if ((productPrice < enteredPrice) && (productRating == 0.0)) {
                holder.productGoodDealView.setText("Its a Fair Deal");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else if ((productPrice > enteredPrice) && (difference <= PERCENTAGE_POINT_2 * enteredPrice)) {
                holder.productGoodDealView.setText("Its a Fair Deal !!");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else {
                holder.productGoodDealView.setText("Its a Bad Deal");
                holder.productGoodDealView.setTextColor(Color.RED);
            }
        }
        if ((productPrice >= 40.0) && (productPrice < 120.0)) {
            if ((productPrice < enteredPrice) && (productRating >= 4.0)) {
                holder.productGoodDealView.setText("Its a Good Deal !!");
                holder.productGoodDealView.setTextColor(Color.GREEN);
            } else if ((productPrice < enteredPrice) && (productRating == 0.0)) {
                holder.productGoodDealView.setText("Its a Fair Deal");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else if ((productPrice > enteredPrice) && (difference <= PERCENTAGE_POINT_3 * enteredPrice)) {
                holder.productGoodDealView.setText("Its a Fair Deal !!");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else {
                holder.productGoodDealView.setText("Its a Bad Deal");
                holder.productGoodDealView.setTextColor(Color.RED);
            }
        }
        if ((productPrice >= 120.0) && (productPrice < 300.0)) {
            if ((productPrice < enteredPrice) && (productRating >= 4.0)) {
                holder.productGoodDealView.setText("Its a Good Deal !!");
                holder.productGoodDealView.setTextColor(Color.GREEN);
            } else if ((productPrice < enteredPrice) && (productRating == 0.0)) {
                holder.productGoodDealView.setText("Its a Fair Deal");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else if ((productPrice > enteredPrice) && (difference <= PERCENTAGE_POINT_4 * enteredPrice)) {
                holder.productGoodDealView.setText("Its a Fair Deal !!");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else {
                holder.productGoodDealView.setText("Its a Bad Deal");
                holder.productGoodDealView.setTextColor(Color.RED);
            }
        }
        if ((productPrice >= 300.0) && (productPrice < 600.0)) {
            if ((productPrice < enteredPrice) && (productRating >= 4.0)) {
                holder.productGoodDealView.setText("Its a Good Deal !!");
                holder.productGoodDealView.setTextColor(Color.GREEN);
            } else if ((productPrice < enteredPrice) && (productRating == 0.0)) {
                holder.productGoodDealView.setText("Its a Fair Deal");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else if ((productPrice > enteredPrice) && (difference <= PERCENTAGE_POINT_5 * enteredPrice)) {
                holder.productGoodDealView.setText("Its a Fair Deal !!");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else {
                holder.productGoodDealView.setText("Its a Bad Deal");
                holder.productGoodDealView.setTextColor(Color.RED);
            }
        }
        if ((productPrice >= 600.0) && (productPrice < 1000.0)) {
            if ((productPrice < enteredPrice) && (productRating >= 4.0)) {
                holder.productGoodDealView.setText("Its a Good Deal !!");
                holder.productGoodDealView.setTextColor(Color.GREEN);
            } else if ((productPrice < enteredPrice) && (productRating == 0.0)) {
                holder.productGoodDealView.setText("Its a Fair Deal");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else if ((productPrice > enteredPrice) && (difference <= PERCENTAGE_POINT_6 * enteredPrice)) {
                holder.productGoodDealView.setText("Its a Fair Deal !!");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else {
                holder.productGoodDealView.setText("Its a Bad Deal");
                holder.productGoodDealView.setTextColor(Color.RED);
            }
        }
        if (productPrice >= 1000.0) {
            if ((productPrice < enteredPrice) && (productRating >= 4.0)) {
                holder.productGoodDealView.setText("Its a Good Deal !!");
                holder.productGoodDealView.setTextColor(Color.GREEN);
            } else if ((productPrice < enteredPrice) && (productRating == 0.0)) {
                holder.productGoodDealView.setText("Its a Fair Deal");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else if ((productPrice > enteredPrice) && (difference <= PERCENTAGE_POINT_7 * enteredPrice)) {
                holder.productGoodDealView.setText("Its a Fair Deal !!");
                holder.productGoodDealView.setTextColor(Color.parseColor("#FFA500"));
            } else {
                holder.productGoodDealView.setText("Its a Bad Deal");
                holder.productGoodDealView.setTextColor(Color.RED);
            }
        }
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

    public void showStores(String skuid, Context a) {

        getStoresforProduct stores = new getStoresforProduct();
        /*LocationManager locationManager = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);    //default


        criteria.setCostAllowed(false);

        // get the best provider depending on the criteria
        String provider = locationManager.getBestProvider(criteria, false);

        // the last known location of this provider
        Location location = locationManager.getLastKnownLocation(provider);

        LocationListener mylistener = new MyLocationListener();
        if (location != null) {
            mylistener.onLocationChanged(location);
        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            a.startActivity(intent);
        }

        // location updates: at least 5 meter and 5000millsecs change
        locationManager.requestLocationUpdates(provider, 5000, 5, mylistener);
                                                                                */
        stores.getStores(skuid, 40.6928, -73.9903, a);
        //message+="https://www.google.co.id/maps/@"+latitude+","+longitude;
    }
}


