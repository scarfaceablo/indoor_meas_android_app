package com.example.klemen.indoorsignalmeasurement;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static EditText username;
    private static EditText password;
    private static TextView attempt;
    private static Button login_button;
    private static Button register_button;

    int attempt_counter = 50;

    private String response_from_api = "false";

    //public String response_from_api = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login_button = (Button) findViewById(R.id.button_login);

        final RequestQueue LoginReqQueue = Volley.newRequestQueue(this);
        final String url = getString(R.string.api_ip)+"login";

        Log.i("string", url);

        username = (EditText)findViewById(R.id.editText_user);
        password = (EditText)findViewById(R.id.editText_password);
        attempt = (TextView)findViewById(R.id.textView_attempt);
        login_button = (Button)findViewById(R.id.button_login);
        register_button = (Button)findViewById(R.id.register_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // construct json object from username and password fields
                final JSONObject obj = new JSONObject();
                try {
                    obj.put("username", username.getText().toString());
                    obj.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest LoginRequest = new JsonObjectRequest(Request.Method.POST, url, obj,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String response_message=response.getString("message");
                                    String response_user_id=response.getString("user_id");

                                    Log.i("response_json",response_message);
                                    Log.i("response_json", Integer.toString(response_message.length()));

                                    if (response_message.equals("OK")){

                                        Log.i("response_if","equals");

                                        Toast.makeText(Login.this,"Username and password is correct",
                                                Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.putExtra("user_id", response_user_id);
                                        startActivity(intent);

                                    }else{
                                        Toast.makeText(Login.this,"Username and password NOT correct",
                                                Toast.LENGTH_SHORT).show();
                                    }


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


                LoginReqQueue.add(LoginRequest);
            }
        });


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });


    }


    }







