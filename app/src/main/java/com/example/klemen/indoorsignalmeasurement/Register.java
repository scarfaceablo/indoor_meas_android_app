package com.example.klemen.indoorsignalmeasurement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText username = (EditText)findViewById(R.id.username_input);
        final EditText password = (EditText)findViewById(R.id.password_input);
        final EditText password_again = (EditText)findViewById(R.id.password_input_2);
        Button register_button = (Button)findViewById(R.id.register_button);

        final RequestQueue RegisterQueue = Volley.newRequestQueue(this);

        final String url_create_user = getString(R.string.api_ip)+"user";

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // construct json object from username and password fields
                final JSONObject obj = new JSONObject();
                try {
                    obj.put("username", username.getText().toString());

                    Log.i("pass1",password.getText().toString());
                    Log.i("pass2",password_again.getText().toString());

                    if (password.getText().toString().equals(password_again.getText().toString())){
                        obj.put("password", password.getText().toString());
                    }
                    else{
                        Toast.makeText(Register.this,"Passwords do not match",
                                Toast.LENGTH_SHORT).show();
                        password.setText("");
                        password_again.setText("");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest RegisterRequest = new JsonObjectRequest(Request.Method.POST, url_create_user, obj,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String response_message=response.getString("message");
                                    Toast.makeText(Register.this,response_message,
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);

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


                RegisterQueue.add(RegisterRequest);
            }
        });



    }
}
