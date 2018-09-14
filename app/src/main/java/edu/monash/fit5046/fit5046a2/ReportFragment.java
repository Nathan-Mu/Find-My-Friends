package edu.monash.fit5046.fit5046a2;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by nathan on 5/5/17.
 */

public class ReportFragment extends Fragment {

    private Button bnPie, bnBar;
    View vReport;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);

        this.getActivity().setTitle("Report");

        bnPie = (Button) vReport.findViewById(R.id.bnPie);
        bnBar = (Button) vReport.findViewById(R.id.bnBar);

        bnPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        return RestClient.getFUnits();
                    }

                    @Override
                    protected void onPostExecute(String jsonString) {
                        JSONArray jsonArray = JSONReader.toJSONArray(jsonString);
                        String[] units = new String[jsonArray.length()];
                        int[] frequencies = new int[jsonArray.length()];
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                units[i] = JSONReader.getStringFromJSONObject(json, "funit");
                                frequencies[i] = JSONReader.getIntegerFromJSONObject(json, "frequency");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent newIntent = new Intent(vReport.getContext(), PieChartActivity.class);
                        newIntent.putExtra("units", units);
                        newIntent.putExtra("frequencies", frequencies);
                        startActivity(newIntent);
                    }
                }.execute();
            }
        });

        return vReport;
    }
}
