package edu.monash.fit5046.fit5046a2;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;

import static android.R.attr.bitmap;

/**
 * Created by nathan on 25/4/17.
 */

public class MainFragment extends Fragment {
    View vMain;
    private TextView tvDescription, tvTemp, tvWelcome, tvDate, tvTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);

        this.getActivity().setTitle("Monash Friend Finder");

        tvDescription = (TextView) vMain.findViewById(R.id.tvDescription);
        tvTemp = (TextView) vMain.findViewById(R.id.tvTemp);
        tvWelcome = (TextView) vMain.findViewById(R.id.tvWelcome);
        tvDate = (TextView) vMain.findViewById(R.id.tvDate);
        tvTime = (TextView) vMain.findViewById(R.id.tvTime);

        Intent intent = getActivity().getIntent();
        Student student = intent.getParcelableExtra("user");

        tvWelcome.setText("Welcome, " + student.getFName() + " " + student.getLName() + "!");

        tvDate.setText("Date: " + Time.toString(Time.getCurrentDate(), "dd/MM/yyyy"));
        tvTime.setText("Time: " + Time.toString(Time.getCurrentTime(), "HH:mm"));

        SearchTemp searchTemp = new SearchTemp();
        searchTemp.execute(new String[] {"-37.8702", "145.0645"});

        return vMain;
    }



    private class SearchTemp extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String urlString = "http://api.openweathermap.org/data/2.5/weather?lat=" + params[0] + "&lon=" + params[1] + "&appid=2672bd6b5040bb0e93829ef6c78e7024";
            //initialise
            URL url = null;
            HttpURLConnection conn = null;
            String textResult = "";
            String[] result = null;
            //Making HTTP request
            try {
                urlString += "";
                url = new URL(urlString);
                //open the connection
                conn = (HttpURLConnection) url.openConnection();
                //set the timeout
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                //set the connection method to GET
                conn.setRequestMethod("GET");
                //add http headers to set your response type to json
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                //Read the response
                Scanner inStream = new Scanner(conn.getInputStream());
                //read the input stream and store it as string
                while (inStream.hasNextLine()) {
                    textResult += inStream.nextLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return textResult;
        }

        @Override
        protected void onPostExecute(String textResult)
        {
            String[] result = null;
            try
            {
                JSONObject json = new JSONObject(textResult);
                    String description = json.getJSONArray("weather").getJSONObject(0).getString("main");
                    String tempK = json.getJSONObject("main").getString("temp");
                    DecimalFormat df =new DecimalFormat("#.0");
                    String tempT = df.format(Double.valueOf(tempK) - 273.15);
                    tvDescription.setText("Weather: " + description);
                    tvTemp.setText("Temperature: " + tempT + "℃");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
