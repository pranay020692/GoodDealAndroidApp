package com.example.tonyhuang.gooddealapplication.activities;

/**
 * Created by Pranay on 11/27/2015.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.tonyhuang.gooddealapplication.R;

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
import java.util.ArrayList;


public class getStoresforProduct extends AppCompatActivity {

    URL url;
    HttpURLConnection urlConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //LocListener l = new LocListener();
        //getStores("",l.getLat(),l.getLon());
        getStores("6461052", 44.882942, -93.2775);
    }

    //Gets the nearby stores
    public void getStores(String sku, double lat, double lon) {

        String urlstring = "http://api.bestbuy.com/v1/stores(area(" + lat + "," + lon + ",10))+products(sku=" + sku + ")?apiKey=6ru583b35stg5q4mzr23nntx&format=json&show=storeId,address,city,region,name";
        new CallAPI().execute(urlstring);
    }
    //"http://api.bestbuy.com/beta/products/mostViewed?apiKey=6ru583b35stg5q4mzr23nntx");

    private void response(String responseData) {
        //TextView productInfo = (TextView) findViewById(R.id.textView);
        ArrayList<String> locationList = new ArrayList();

        try {
            locationList = getDataFromJson(responseData);// List of pairs containing productid and name

            StringBuilder builder = new StringBuilder();
            /*for (String details : locationList) {
                builder.append(details + "\n");
            }
            productInfo.setText(builder.toString());*/
            for (String simple : locationList) {
                String a = locationList.get(1);  // get the first pair in the array
                //locationInfo.setText(a);// Display the name of first item in the pair
            }
        } catch (JSONException e) {
            /// locationInfo.setText(e.getMessage());// set productInfo toast or message
        }

    }

    public static ArrayList getDataFromJson(String jString) throws JSONException {

        ArrayList<Pair> locationsArrayList = new ArrayList();
        //ArrayList<String> locationsArrayList = new ArrayList();
        JSONObject myjson = new JSONObject(jString);
        String String_that_should_be_array = myjson.getString("stores");
        JSONArray myjsonarray = new JSONArray(String_that_should_be_array);

        for (int i = 0; i < myjsonarray.length(); i++) {
            JSONObject tempJSONobj = myjsonarray.getJSONObject(i);

            String address = tempJSONobj.get("address").toString();
            String city = tempJSONobj.get("city").toString();
            String region = tempJSONobj.get("region").toString();
            String name = tempJSONobj.get("name").toString();

            Pair<String, String> nameaddressPair = new Pair<>((name + ", " + city + ", " + region), (address));
            locationsArrayList.add(nameaddressPair);
        }
        return locationsArrayList;
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
            String a = json;
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
}
