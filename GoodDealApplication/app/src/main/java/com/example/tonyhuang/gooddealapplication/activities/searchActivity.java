package com.example.tonyhuang.gooddealapplication.activities;

/**
 * Created by Pranay on 10/27/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonyhuang.gooddealapplication.R;
import com.example.tonyhuang.gooddealapplication.adapters.CustomAdapter;
import com.example.tonyhuang.gooddealapplication.adapters.ViewPagerAdapter;
import com.example.tonyhuang.gooddealapplication.data.ProductsDataSource;
import com.example.tonyhuang.gooddealapplication.models.Product;
import com.example.tonyhuang.gooddealapplication.ui.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;

public class searchActivity extends AppCompatActivity {
    //For Search
    private double percent = 0.3;
    private ProductsDataSource productsDataSource;
    private CustomAdapter adapter;
    private searchActivity CustomListView = null;
    private ArrayList<Product> CustomListViewValuesArr = new ArrayList<Product>();
    private URL url;
    private HttpURLConnection urlConnection = null;
    private ListView list;
    private String enteredName;
    private String enteredPrice;
    private Resources res;

    //For Page viewer
    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter pageadapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Search", "Deals", "History", "Wish List"};
    private int Numboftabs = 4;
    private Button compareBtn, barcodeBtn;
    private String myString = "search";
    private int responseCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productsDataSource = new ProductsDataSource(this);
        try {
            productsDataSource.open();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        CustomListView = this;
        Intent searchIntent = getIntent();
        enteredName = searchIntent.getStringExtra("entered_name");
        enteredPrice = searchIntent.getStringExtra("entered_price");
        productsDataSource.createHistory(enteredName, enteredPrice);
        if (isNetworkAvailable()) {
            String enteredNameTemp = enteredName.replace(" ", "&search="); // takes space as multiple search string
            makeSearch(enteredNameTemp);
        } else {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getMyData() {
        return myString;
    }

    public void makeSearch(String keyword) {
        String urlstring;
        if (enteredPrice.equals("0.0")) {
            urlstring = "http://api.bestbuy.com/v1/products(search=" + keyword + ")?show=sku,name,customerReviewAverage,salePrice,image&pageSize=5&page=10&apiKey=6ru583b35stg5q4mzr23nntx&format=json";
        } else {
            int min = (int) (Integer.valueOf(enteredPrice) - Integer.valueOf(enteredPrice) * percent);
            int max = (int) (Integer.valueOf(enteredPrice) + Integer.valueOf(enteredPrice) * percent);
            urlstring = "http://api.bestbuy.com/v1/products(search=" + keyword + "&salePrice>" + String.valueOf(min) + "&salePrice<" + String.valueOf(max) + ")?show=sku,name,customerReviewAverage,salePrice,image&pageSize=5&apiKey=6ru583b35stg5q4mzr23nntx&format=json";
        }
        new CallAPI().execute(urlstring);
    }

    public void makeSearchWalmart(String keyword) {
        String urlstring;
        if (enteredPrice.equals("0.0")) {
            urlstring = "http://api.walmartlabs.com/v1/search?numItems=5&apiKey=vhvu3qsshkyv5cpxrrtr36ur&query=" + keyword;
        } else {
            int min = (int) (Integer.valueOf(enteredPrice) - Integer.valueOf(enteredPrice) * percent);
            int max = (int) (Integer.valueOf(enteredPrice) + Integer.valueOf(enteredPrice) * percent);
            urlstring = "http://api.walmartlabs.com/v1/search?numItems=5&apiKey=vhvu3qsshkyv5cpxrrtr36ur&query=" + keyword + "&facet=on&facet.range=price:[" + String.valueOf(min) + "%20TO%20" + String.valueOf(max) + "]";
        }
        new CallAPIWalmart().execute(urlstring);
    }
    //"http://api.bestbuy.com/beta/products/mostViewed?apiKey=6ru583b35stg5q4mzr23nntx");

    private void response(String responseData, String where) {
        //TextView productInfo = (TextView) findViewById(R.id.textView);
        ArrayList<String> productsList = new ArrayList();
        try {
            if (responseCount == 0) {
                productsDataSource.deleteAllProducts();
                responseCount++;
            }
            productsList = getDataFromJson(responseData, where);// List of pairs containing productid and name

            toolbar = (Toolbar) findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar);

            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            pageadapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, "search", enteredPrice);
            //pageadapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, "search");

            // Assigning ViewPager View and setting the adapter
            pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(pageadapter);
            pager.setCurrentItem(1);

            // Assiging the Sliding Tab Layout View
            tabs = (SlidingTabLayout) findViewById(R.id.tabs);
            tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

            // Setting Custom Color for the Scroll bar indicator of the Tab View
            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.tabsScrollColor);
                }
            });

            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            //productInfo.setText(e.getMessage());// set productInfo toast or message
        }
    }

    public ArrayList getDataFromJson(String jString, String where) throws JSONException {
        if (where.equals("Bestbuy")) {
            ArrayList<String> productInfoSingleString = new ArrayList();
            JSONObject myjson = new JSONObject(jString);
            String String_that_should_be_array = myjson.getString("products");
            JSONArray myjsonarray = new JSONArray(String_that_should_be_array);
            for (int i = 0; i < myjsonarray.length(); i++) {
                JSONObject tempJSONobj = myjsonarray.getJSONObject(i);
                String productId = tempJSONobj.get("sku").toString();
                String productName = tempJSONobj.get("name").toString();
                String productRating = tempJSONobj.get("customerReviewAverage").toString();
                String productPrice = tempJSONobj.get("salePrice").toString();
                String productImageURL = tempJSONobj.get("image").toString();

                //This is where we are creating an entry into our SQLITE table
                productsDataSource.createProduct(productId, productName, productRating, productPrice, productImageURL);
                productInfoSingleString.add(productId + productName + productRating + productPrice);
            }
            return productInfoSingleString;
        } else {
            ArrayList<String> productInfoSingleString = new ArrayList();
            JSONObject myjson = new JSONObject(jString);
            String String_that_should_be_array = myjson.getString("items");
            JSONArray myjsonarray = new JSONArray(String_that_should_be_array);
            for (int i = 0; i < myjsonarray.length(); i++) {
                JSONObject tempJSONobj = myjsonarray.getJSONObject(i);
                String productId = tempJSONobj.get("itemId").toString();
                String productName = tempJSONobj.get("name").toString();
                String productRating;
                try {
                    productRating = tempJSONobj.get("customerRating").toString();
                } catch (JSONException e) {
                    productRating = "0";
                }
                String productPrice = tempJSONobj.get("salePrice").toString();
                String productImageURL = tempJSONobj.get("thumbnailImage").toString();

                //This is where we are creating an entry into our SQLITE table
                productsDataSource.createProduct(productId, productName, productRating, productPrice, productImageURL);
                productInfoSingleString.add(productId + productName + productRating + productPrice);

            }
            return productInfoSingleString;
        }
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            InputStream in = null;
            int resCode = -1;
            try {
                URL url = new URL(params[0]);
                URLConnection urlConn = url.openConnection();

                if (!(urlConn instanceof HttpURLConnection)) {
                    throw new IOException("URL is not an Http URL");
                }
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String json = convertStreamToString(in);
            return json;

        }

        public String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

            return sb.toString();
        }

        protected void onPostExecute(String stream_url) {
            super.onPostExecute(stream_url);
            response(stream_url, "Bestbuy");
            String enteredNameTemp = enteredName.replace(" ", "%20");
            makeSearchWalmart(enteredNameTemp);

            //makeSearcheBay(enteredNameTemp);
        }
    }

    private class CallAPIWalmart extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            InputStream in = null;
            int resCode = -1;
            try {
                URL url = new URL(params[0]);
                URLConnection urlConn = url.openConnection();

                if (!(urlConn instanceof HttpURLConnection)) {
                    throw new IOException("URL is not an Http URL");
                }
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String json = convertStreamToString(in);
            return json;

        }


        public String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

            return sb.toString();
        }

        protected void onPostExecute(String stream_url) {
            super.onPostExecute(stream_url);
            response(stream_url, "Walmart");
        }
    }




    @Override
    protected void onResume() {
        try {
            productsDataSource.open();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        productsDataSource.close();
        super.onPause();
    }

    /*public void onItemClick(int mPosition) {
        Product tempValues = (Product)CustomListViewValuesArr.get(mPosition);//TODO: Crash fix when a product is clicked
        Toast.makeText(this, tempValues.getProductName() + tempValues.getProductPrice(), Toast.LENGTH_SHORT).show();
    }*/

    public void setListData() {
        CustomListViewValuesArr = productsDataSource.getAllProducts();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //http://www.walmart.com/ip/Straight-Talk-Apple-iPhone-5S-4G-LTE-16GB-Prepaid-Smartphone/33152936
    public static void gotoProduct(String sku, Activity activity) {
        String URL = "http://www.bestbuy.com/site/searchpage.jsp?st=+" + sku + "&_dyncharset=UTF-8&id=pcat17071";
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        activity.startActivity(browse);
    }

    public static void gotoProductWalmart(String sku, Activity activity) {
        String URL = "http://www.walmart.com/ip/" + sku;
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        activity.startActivity(browse);
    }


    public void makeSearcheBay(String keyword) {


        String urlstring =  "http://sandbox.api.ebaycommercenetwork.com/publisher/3.0/json/GeneralSearch?apiKey=78b0db8a-0ee1-4939-a2f9-d3cd95ec0fcc&trackingId=7000610&visitorUserAgent&visitorIPAddress&keyword="+keyword;

        new CallAPIeBay().execute(urlstring);
    }


    public void responseeBay(String responseData){
        TextView productInfo = (TextView) findViewById(R.id.textView);
        ArrayList<String> productsList = new ArrayList();
        //productInfo.setText(responseData);
        try {
            productsList = getDataFromJsoneBay(responseData);// List of pairs containing productid and name
            String simple = productsList.get(1);  // get the first pair in the array
            productInfo.setText(simple);// Display the name of first item in the pair
        }
        catch (JSONException e) {
            productInfo.setText(e.getMessage());// set productInfo toast or message
        }

    }

    public  ArrayList getDataFromJsoneBay(String jString) throws JSONException {


        ArrayList<String> productInfoSingleString = new ArrayList();
        JSONObject myjson = new JSONObject(jString);
        String String_that_should_be_array = myjson.getString("categories");
        JSONObject jsonCategory = new JSONObject(String_that_should_be_array);
        String categoryArrayString = jsonCategory.getString("category");
        JSONArray categoryObjectArray = new JSONArray(categoryArrayString);

        String item0 =  categoryObjectArray.getJSONObject(0).getString("items");
        JSONObject itemObject = new JSONObject(item0);

        JSONArray itemArray = itemObject.getJSONArray("item");
        //String items = item0.getString("items");

        for(int loopCounter = 0; loopCounter < 5; loopCounter++) {

            JSONObject oneItem = itemArray.getJSONObject(loopCounter).getJSONObject("product");

            String itemName = oneItem.getString("name");
            String itemPrice = oneItem.getJSONObject("maxPrice").getString("value");

            String itemImageUrl = oneItem.getJSONObject("images").getJSONArray("image").getJSONObject(0).getString("sourceURL");

            String itemRating = null;
            if (oneItem.getJSONObject("rating").getString("reviewCount").equals("0")) {
                itemRating = "null";
            } else {
                itemRating = oneItem.getJSONObject("rating").getString("rating");
            }

            String itemId = oneItem.getString("id");
            productsDataSource.createProduct(itemId,itemName, itemRating, itemPrice, itemImageUrl);

        }
        return productInfoSingleString;
    }

    private class CallAPIeBay extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            InputStream in = null;
            int resCode = -1;
            try {
                URL url = new URL(params[0]);
                URLConnection urlConn = url.openConnection();

                if (!(urlConn instanceof HttpURLConnection)) {
                    throw new IOException("URL is not an Http URL");
                }
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");

                //httpConn.connect();
                resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String json = convertStreamToString(in);
            return json;

        }


        public String convertStreamToString(InputStream is){
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

            return sb.toString();
        }
        protected void onPostExecute(String stream_url) {
            super.onPostExecute(stream_url);
            responseeBay(stream_url);
        }

    }

    public static void gotoProducteBay(String sku, Activity activity) {
        String URL = "http://www.walmart.com/ip/" + sku;
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        activity.startActivity(browse);
    }
}

