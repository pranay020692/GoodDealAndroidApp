package com.example.tonyhuang.gooddealapplication;

/**
 * Created by Pranay on 10/27/2015.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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

    ProductsDataSource productsDataSource;
    URL url;
    HttpURLConnection urlConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productsDataSource = new ProductsDataSource(this);
        try {
            productsDataSource.open();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        makeSearch("iphone");
    }


    public void makeSearch(String keyword) {

        //String urlstring = "http://api.bestbuy.com/beta/products/mostViewed?apiKey=6ru583b35stg5q4mzr23nntx";
        String urlstring = "http://api.bestbuy.com/v1/products(longDescription="+keyword+"*%7Csku=7619002)?show=sku,name,customerReviewAverage,salePrice&pageSize=15&page=5&apiKey=6ru583b35stg5q4mzr23nntx&format=json";
        //http://api.bestbuy.com/v1/products(longDescription=iphone)?show=sku,name&pageSize=15&page=1&apiKey=6ru583b35stg5q4mzr23nntx&format=json
        new CallAPI().execute(urlstring);
    }
    //"http://api.bestbuy.com/beta/products/mostViewed?apiKey=6ru583b35stg5q4mzr23nntx");

    private void response(String responseData){
        TextView productInfo = (TextView) findViewById(R.id.textView);
        ArrayList<String> productsList = new ArrayList();
        //productInfo.setText(responseData);
        try {
            productsList = getDataFromJson(responseData);// List of pairs containing productid and name
            String simple = productsList.get(1);  // get the first pair in the array
            productInfo.setText(simple);// Display the name of first item in the pair
        }
        catch (JSONException e) {
            productInfo.setText(e.getMessage());// set productInfo toast or message
        }

    }

    public  ArrayList getDataFromJson(String jString) throws JSONException {

        //ArrayList<Pair> productsList = new ArrayList();
        ArrayList<String> productInfoSingleString = new ArrayList();
        JSONObject myjson = new JSONObject(jString);
        String String_that_should_be_array = myjson.getString("products");
        JSONArray myjsonarray = new JSONArray(String_that_should_be_array);

        for(int i = 0; i < myjsonarray.length(); i++) {
            JSONObject tempJSONobj = myjsonarray.getJSONObject(i);
            //products.put((tempJSONobj.get("productInfoSingleString").toString()),(tempJSONobj.get("name").toString()));
            //names.add(tempJSONobj.get("name").toString());
            //productInfoSingleString.add(tempJSONobj.get("productInfoSingleString").toString());
            String productId = tempJSONobj.get("sku").toString();
            String productName = tempJSONobj.get("name").toString();
            String productRating = tempJSONobj.get("customerReviewAverage").toString();
            String productPrice = tempJSONobj.get("salePrice").toString();
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
            response(stream_url);
        }
    }

    @Override
    protected void onResume() {
        try {
            productsDataSource.open();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        productsDataSource.close();
        super.onPause();
    }
}