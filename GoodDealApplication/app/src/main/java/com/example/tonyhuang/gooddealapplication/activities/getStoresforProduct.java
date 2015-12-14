package com.example.tonyhuang.gooddealapplication.activities;

/**
 * Created by Pranay on 11/27/2015.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

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

    // getStores("6461052", 44.882942, -93.2775);

    //Gets the nearby stores
    public void getStores(String sku, double lat, double lon, Context context) {

        String urlstring = "http://api.bestbuy.com/v1/stores(area(" + lat + "," + lon + ",10))+products(sku=" + sku + ")?apiKey=6ru583b35stg5q4mzr23nntx&format=json&show=storeId,address,city,region,name";
        CallAPI a = new CallAPI(context);
        new CallAPI(context).execute(urlstring);
    }
    //"http://api.bestbuy.com/beta/products/mostViewed?apiKey=6ru583b35stg5q4mzr23nntx");

    private void response(String responseData, final Context context) {

        try {
            //TextView productInfo = (TextView) findViewById(R.id.textView);
            //ArrayList<Pair> locationList = new ArrayList();
            ArrayList<String> CombLocationList = new ArrayList();
            final ArrayList<Pair> locationList = getDataFromJson(responseData);
            //String a = "ssd";
            String locationString = "";
            for (Pair s : locationList) {
                locationString += s.first.toString() + "\n";
                locationString += s.second.toString() + "\n";
                CombLocationList.add(locationString);
                locationString = "";
            }

            //locationList.get(i).first.toString() + " ," + locationList.get(i).second.toString();
            final CharSequence[] charSequencelocations = CombLocationList.toArray(new CharSequence[CombLocationList.size()]);


            /*locationList = getDataFromJson(responseData);// List of pairs containing productid and name
            //Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Nearby Stores:-");
            alertDialog.setMessage(locationString);
            alertDialog.show();*/

            //final CharSequence[] items={"One","two","three"};

            AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
            builder3.setTitle("Nearby Stores:-").setItems(charSequencelocations, new DialogInterface.OnClickListener() {


                public void onClick(DialogInterface dialog, int which) {

                    // TODO Auto-generated method stub

                    //Toast.makeText(context, "U clicked "+charSequencelocations[which], Toast.LENGTH_LONG).show();

                    String address = locationList.get(which).first.toString() + "," + locationList.get(which).second.toString();
                    address.replaceAll("\\s+", "");


                    //String map = "https://www.google.com/maps/dir/Best%20Buy%20Mobile%20-%20Union%20Square,%20New%20York,%20NY,2%20Union%20Square";
                    // where check is the address string

                    //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                    //startActivity(i);

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + address));
                    context.startActivity(intent);


                }

            });

            builder3.show();


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

    public class CallAPI extends AsyncTask<String, String, String> {


        private Context mContext;

        public CallAPI(Context context) {
            mContext = context;
        }

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
            response(stream_url, mContext);
        }
    }

}
