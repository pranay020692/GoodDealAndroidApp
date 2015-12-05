package com.example.tonyhuang.gooddealapplication.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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


/**
 * Created by Pranay on 11/24/2015.
 */

public class ProductNameFromBarcode extends AppCompatActivity {

    private URL url;
    private HttpURLConnection urlConnection = null;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scan);

        //final Button button = (Button) findViewById(R.id.buttonProductname);
        //getProductname("barcode");

    }*/


    public void getProductname(String barcode) {

        String urlstring = "https://api.outpan.com/v2/products/" + barcode + "?apikey=e47ac17279175aec04b3aacb0296746f";
        new CallAPI().execute(urlstring);
    }

    private void response(String responseData) {
        //TextView productInfo = (TextView) findViewById(R.id.textView);

        try {
            String productName = getDataFromJson(responseData);
            Toast.makeText(ProductNameFromBarcode.this, productName, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            //productInfo.setText(e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public static String getDataFromJson(String jString) throws JSONException {


        JSONObject myjson = new JSONObject(jString);
        String productName = myjson.get("name").toString();
        return productName;

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