/*package com.example.klemen.indoorsignalmeasurement;

import android.content.Intent;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_old extends AppCompatActivity {

    private static EditText username;
    private static EditText password;
    private static TextView attempt;
    private static Button login_button;

    int attempt_counter=50;

    private String response_from_api = "false";

    //public String response_from_api = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton();
    }

    public void LoginButton(){
        username = (EditText)findViewById(R.id.editText_user);
        password = (EditText)findViewById(R.id.editText_password);
        attempt = (TextView)findViewById(R.id.textView_attempt);
        login_button = (Button)findViewById(R.id.button_login);


        final RequestQueue LoginRequestQueue = Volley.newRequestQueue(this);

        String username_from_app = "test";
        String password_from_app = "test123";



        //http://35.187.122.139:5111/login?username=test&password=test123


        attempt.setText(Integer.toString(attempt_counter));

        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String username_from_app = username.getText().toString();
                        String password_from_app = password.getText().toString();

                        String url = "http://35.187.122.139:5222/login";

                        Log.i("response_from",url);

                        StringRequest AuthLogin = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //This code is executed if the server responds, whether or not the response contains data.
                                //The String 'response' contains the server's response.
                                //response_from_api = Integer.valueOf(response);
                                Log.i("response_raw_len",Integer.toString(response.trim().length()));
                                Log.i("response_raw",response);

                                response_from_api=response;

                                Log.i("response_raw_len",Integer.toString(response_from_api.trim().length()));
                                Log.i("response_raw",response_from_api);

                            }
                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //This code is executed if there is an error.
                            }
                        });

                        LoginRequestQueue.add(AuthLogin);

                        //Log.i("response_from_serverlen", Integer.toString(response_from_api.length()));

                        //Log.i("response_from_api_str",response_from_api);

                        //Log.i("response_check_length", Integer.toString("auth_success".length()))


                        if (response_from_api.trim().length() == 4)
                        {
                            Toast.makeText(Login.this,"Username and password is correct",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        }

                        else {
                            Toast.makeText(Login.this,"Username and password is NOT correct",
                                    Toast.LENGTH_SHORT).show();
                            attempt_counter--;
                            attempt.setText(Integer.toString(attempt_counter));
                            if(attempt_counter==0)
                                login_button.setEnabled(false);
                        }


                    }
                }
        );
    }

}

*/