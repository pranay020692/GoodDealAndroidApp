package com.example.tonyhuang.gooddealapplication;

/**
 * Created by Pranay on 10/27/2015.
 */

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ProductsDataSource productsDataSource;
    private CustomAdapter adapter;
    private searchActivity CustomListView = null;
    private ArrayList<Product> CustomListViewValuesArr = new ArrayList<Product>();
    URL url;
    HttpURLConnection urlConnection = null;
    private ListView list;
    private String enteredName;
    private String enteredPrice;
    private Resources res;

    //For Page viewer
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter pageadapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Search", "Deals", "History", "Wish List"};
    int Numboftabs = 4;
    Button compareBtn, barcodeBtn;
    Bundle bundle;

    private String myString = "search";


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
        /*res =getResources();
        list= (ListView)findViewById( R.id.list );  // List defined in XML ( See Below )
        */
        CustomListView = this;
        Intent searchIntent = getIntent();
        enteredName = searchIntent.getStringExtra("entered_name");
        enteredPrice = searchIntent.getStringExtra("entered_price");
        makeSearch(enteredName);

        // compareBtn = (Button) findViewById(R.id.button);
        /*compareBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startResultListActivity(view);
            }
        });*/
        //startResultListActivity();
        //makeSearch("iphone");
        //setContentView(R.layout.activity_main2);

        //toolbar = (Toolbar) findViewById(R.id.tool_bar);
        //setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        //pageadapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, "search");

        // Assigning ViewPager View and setting the adapter
        //pager = (ViewPager) findViewById(R.id.pager);
        //pager.setAdapter(pageadapter);
        //pager.setCurrentItem(1);

        // Assiging the Sliding Tab Layout View
        //tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        //tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        //tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
        //    @Override
        //    public int getIndicatorColor(int position) {
        //        return getResources().getColor(R.color.tabsScrollColor);
        //    }
        //});

        // Setting the ViewPager For the SlidingTabsLayout
        //tabs.setViewPager(pager);

        // DealsTab dealstab = (DealsTab)getSupportFragmentManager().findFragmentById(R.id.searchTab);

    }


    public String getMyData() {
        return myString;
    }

    public void makeSearch(String keyword) {
        //String urlstring = "http://api.bestbuy.com/beta/products/mostViewed?apiKey=6ru583b35stg5q4mzr23nntx";
        String urlstring = "http://api.bestbuy.com/v1/products(longDescription=" + keyword + "*%7Csku=7619002)?show=sku,name,customerReviewAverage,salePrice&pageSize=15&page=5&apiKey=6ru583b35stg5q4mzr23nntx&format=json";
        //http://api.bestbuy.com/v1/products(longDescription=iphone)?show=sku,name&pageSize=15&page=1&apiKey=6ru583b35stg5q4mzr23nntx&format=json
        new CallAPI().execute(urlstring);
    }
    //"http://api.bestbuy.com/beta/products/mostViewed?apiKey=6ru583b35stg5q4mzr23nntx");

    private void response(String responseData) {
        TextView productInfo = (TextView) findViewById(R.id.textView);
        ArrayList<String> productsList = new ArrayList();

        //productInfo.setText(responseData);
        try {
            productsDataSource.deleteAllProducts();

            productsList = getDataFromJson(responseData);// List of pairs containing productid and name
            //String simple = productsList.get(1);  // get the first pair in the array
            // productInfo.setText(simple);// Display the name of first item in the pair

            // The reason we are closing the database here is because we onPause is not called everytime.
            // So, we need to close it here since we are opening everytime in onResume
            productsDataSource.close();
            toolbar = (Toolbar) findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar);

            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            pageadapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, "search");

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
            productInfo.setText(e.getMessage());// set productInfo toast or message
        }

        /*setListData();
        adapter=new CustomAdapter( this, CustomListViewValuesArr, res, enteredPrice);
        list.setAdapter(adapter);*/
    }

    public ArrayList getDataFromJson(String jString) throws JSONException {
        //ArrayList<Pair> productsList = new ArrayList();
        ArrayList<String> productInfoSingleString = new ArrayList();
        JSONObject myjson = new JSONObject(jString);
        String String_that_should_be_array = myjson.getString("products");
        JSONArray myjsonarray = new JSONArray(String_that_should_be_array);

        for (int i = 0; i < myjsonarray.length(); i++) {
            JSONObject tempJSONobj = myjsonarray.getJSONObject(i);
            //products.put((tempJSONobj.get("productInfoSingleString").toString()),(tempJSONobj.get("name").toString()));
            //names.add(tempJSONobj.get("name").toString());
            //productInfoSingleString.add(tempJSONobj.get("productInfoSingleString").toString());
            String productId = tempJSONobj.get("sku").toString();
            String productName = tempJSONobj.get("name").toString();
            String productRating = tempJSONobj.get("customerReviewAverage").toString();
            String productPrice = tempJSONobj.get("salePrice").toString();
            //This is where we are creating an entry into our SQLITE table
            productsDataSource.createProduct(productId, productName, productRating, productPrice);
            //Pair<String, String> idNamePair = new Pair<>((productId),(productName+","+productPrice+","+productRating));
            //productsList.add(idNamePair);
            productInfoSingleString.add(productId + productName + productRating + productPrice);
        }
        //return productsList;
        return productInfoSingleString;
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
            response(stream_url);
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

    public void onItemClick(int mPosition) {
        Product tempValues = (Product) CustomListViewValuesArr.get(mPosition);
        Toast.makeText(this, tempValues.getProductName() + tempValues.getProductPrice(), Toast.LENGTH_SHORT).show();
    }

    public void setListData() {
        CustomListViewValuesArr = productsDataSource.getAllProducts();
    }
}