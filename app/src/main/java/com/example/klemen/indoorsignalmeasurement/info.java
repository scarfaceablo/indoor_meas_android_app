package com.example.klemen.indoorsignalmeasurement;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import static java.lang.Math.log10;

public class info extends AppCompatActivity {



    public double frequency_1 = 0.0;
    public double height_ant_BS_1 = 0.0;
    public double ant_corr_1 = 0.0;
    public double dist_BS_M_1 = 0.0;


    public double ResultPath1 = 0.0;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        TextView freq_input = (TextView)findViewById(R.id.frequency);

        TextView ant_corr = (TextView)findViewById(R.id.ant_corr_factor);

        TextView height_ant_BS = (TextView)findViewById(R.id.height_ant_BS);

        TextView ResultPath = (TextView)findViewById(R.id.Result);

        TextView dist_BS_M = (TextView)findViewById(R.id.dist_BS_MS);












       // frequency_1 = Double.parseDouble(freq_input_edit.getText().toString());
        //height_ant_BS_1 = Double.parseDouble(height_ant_BS_edit.getText().toString());
       // ant_corr_1 = Double.parseDouble(ant_corr_edit.getText().toString());
       // dist_BS_M_1 = Double.parseDouble(dist_BS_M_edit.getText().toString());

        ButtonClick2();


    }





    public void ButtonClick2(){

        Button Result = (Button) findViewById(R.id.Calculatepathloss);
        Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {







                EditText freq_input_edit = (EditText) findViewById(R.id.editText2);
                EditText height_ant_BS_edit = (EditText) findViewById(R.id.editText3);
                EditText ant_corr_edit = (EditText) findViewById(R.id.editText4);
                EditText dist_BS_M_edit = (EditText) findViewById(R.id.editText5);


                frequency_1 = 0;
                try {
                    frequency_1 = Double.parseDouble(freq_input_edit.getText().toString().replace(",", ""));
                } catch (NumberFormatException e) {
                    // EditText EtPotential does not contain a valid double
                }
                height_ant_BS_1 = 0;
                try {
                    height_ant_BS_1 = Double.parseDouble(height_ant_BS_edit.getText().toString().replace(",", ""));
                } catch (NumberFormatException e) {
                    // EditText EtPotential does not contain a valid double
                }
                ant_corr_1 = 0;
                try {
                    ant_corr_1 = Double.parseDouble(ant_corr_edit.getText().toString().replace(",", ""));
                } catch (NumberFormatException e) {
                    // EditText EtPotential does not contain a valid double
                }
                dist_BS_M_1 = 0;
                try {
                    dist_BS_M_1 = Double.parseDouble(dist_BS_M_edit.getText().toString().replace(",", ""));
                } catch (NumberFormatException e) {
                    // EditText EtPotential does not contain a valid double
                }

                ResultPath1 = 69.55+26.16*log10(frequency_1)-13.82*log10(height_ant_BS_1) - ant_corr_1 + (44.9-6.55*log10(height_ant_BS_1))*log10(dist_BS_M_1);
                TextView ResultPath = (TextView)findViewById(R.id.Result);

                DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###");
                ResultPath.setText(REAL_FORMATTER.format(ResultPath1));

                Log.i("info_res",Double.toString(ResultPath1));
                Log.i("info_res",Double.toString(frequency_1));
                Log.i("info_res",Double.toString(height_ant_BS_1));
                Log.i("info_res",Double.toString(dist_BS_M_1));
                Log.i("info_res",Double.toString(ant_corr_1));





            }
        });


    }
}
