package com.example.klemen.indoorsignalmeasurement;

import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.id.list;
import static android.R.id.message;

public class savedmeas extends AppCompatActivity {

    private RecordAdapter recordAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedmeas);


        GetSavedMeas();



    }



    private void GetSavedMeas(){

        recordAdapter= new RecordAdapter(this, new ArrayList<Record>());
        final ListView recordsView = (ListView) findViewById(R.id.records_view);

        recordsView.setAdapter(recordAdapter);

        final RequestQueue GetDataQueue = Volley.newRequestQueue(this);

        String user_id = getIntent().getStringExtra("user_id");

        final String url_get_data = getString(R.string.api_ip)+"data/"+user_id;

        Log.i("url_get",url_get_data);



            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {




                                JsonObjectRequest GetDataRequest = new JsonObjectRequest(Request.Method.GET, url_get_data,

                                        new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {

                                                    JSONArray jArray = response.getJSONArray("data");

                                                    JSONObject to_list_view = new JSONObject();

                                                    for (int i=0; i < 1; i++)
                                                    {
                                                        try {
                                                            JSONObject oneObject = jArray.getJSONObject(i);
                                                            //Log.i("saved_meas_1", oneObject.toString());

                                                            String datetime = oneObject.getString("datetime");
                                                            String cell_id = oneObject.getString("cell_id");
                                                            String SS = oneObject.getString("signal_strength");
                                                            String SQ = oneObject.getString("signal_quality");



                                                            try {

                                                                Integer datetime_int=Integer.parseInt(datetime);

                                                                Log.i("datetime", String.valueOf(datetime_int));

                                                                Date df = new java.util.Date(datetime_int);
                                                                String datetime_h = new SimpleDateFormat("dd MM hh:mm:ss").format(df);

                                                                to_list_view.put("datetime", datetime_h);
                                                                to_list_view.put("cell_id", cell_id);
                                                                to_list_view.put("SS", SS);
                                                                to_list_view.put("SQ", SQ);


                                                            } catch (JSONException e) {
                                                                // TODO Auto-generated catch block
                                                                e.printStackTrace();
                                                            }

                                                            String outPut = datetime + "-" +cell_id;

                                                            //listContents.add(outPut);

                                                        } catch (JSONException e) {
                                                            // Oops
                                                        }



                                                    }


                                                    Log.i("inside", to_list_view.toString());

                                                    Gson gson = new Gson();
                                                    Record record = gson.fromJson(to_list_view.toString(), Record.class);



                                                    recordAdapter.add(record);
                                                    recordsView.setSelection(recordAdapter.getCount() - 1);


                                                    //Log.i("inside", listContents.toString());
                                                    //out_list=listContents;



                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //This code is executed if there is an error.
                                    }

                                });

                                GetDataQueue.add(GetDataRequest);

                            } catch (Exception e) {
                                // TODO

                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 0, 10000); // ms







    }


}







