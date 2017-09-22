package com.example.perfectbnb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    double longitute,lat;
    TextView txtName;
    TextView txtTemp;
    Button historyButton;
    Handler handler;
    String cityName,temp;
    DBHelper dbHelper;
    // Progress Dialog
    private ProgressDialog pDialog;



    private static final String TAG_NAME = "name";
    private static final String APIKEY = "33d7cdeb47601e7eb9fa84b4b1f9f410";
   private ArrayList<Location> historyArrayList = new ArrayList<>();
    private DownImageLoader _DownImageLoader;
    private ImageView weatherIcon;
    private String icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent i = getIntent();
        txtName = (TextView) findViewById(R.id.city_name_text_view);
        txtTemp = (TextView) findViewById(R.id.temp_text_view);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);

        historyButton = (Button)findViewById(R.id.history_button);
        longitute = i.getDoubleExtra("long",0.00);
        lat = i.getDoubleExtra("lat",0.00);
        String url_product_detials2 = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+longitute+"&appid="+APIKEY+"&units=metric";
        new GetWeaetherDetails().execute(url_product_detials2);
        dbHelper = new DBHelper(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                txtName.setText(cityName);
                txtTemp.setText(temp);

            }

        };
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Database History Printed On Console",Toast.LENGTH_LONG).show();

                historyArrayList = dbHelper.getAllNotes();
            }
        });
    }

    class GetWeaetherDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ResultActivity.this);
            pDialog.setMessage("Loading Weather details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(final String... params) {
            final HttpURLConnection[] connection = {null};
            final BufferedReader[] reader = {null};
            // updating UI from Background Thread



                    try {
                    URL url = new URL(params[0]);
                    connection[0] = (HttpURLConnection) url.openConnection();
                        connection[0].connect();


                        // Building Parameters
                        InputStream stream = connection[0].getInputStream();
                        reader[0] = new BufferedReader(new InputStreamReader(stream));
                        StringBuffer buffer = new StringBuffer();
                        String line = "";
                        while ((line = reader[0].readLine()) != null ){
                            buffer.append(line);
                        }
                        String  result = buffer.toString();
                        JSONObject parentObject = new JSONObject(result);
                         cityName = parentObject.getString("name");
                        JSONObject parentObject2 = parentObject.getJSONObject("main");
                        temp= parentObject2.getString("temp");
                        JSONArray jsonArray = parentObject.getJSONArray("weather");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        icon= jsonObject.getString("icon");

                        return temp;

                    }
                    catch (MalformedURLException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if(connection[0] != null){
                            connection[0].disconnect();
                        }
                        if (reader[0] != null){
                            try {
                                reader[0].close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
            txtName.setText(cityName);
            txtTemp.setText(temp + "\u2103");
            Location location = new Location(cityName,longitute+"",lat+"");
            String urlWeatherIcon = "http://openweathermap.org/img/w/"+icon+".png";
            _DownImageLoader = new DownImageLoader(ResultActivity.this);
            weatherIcon.setTag(urlWeatherIcon);
            _DownImageLoader.DisplayImage(urlWeatherIcon, ResultActivity.this , weatherIcon);
            dbHelper.insertNote(location);
            System.out.println("Result Activity, Temp::"+temp);
        }
    }

}
