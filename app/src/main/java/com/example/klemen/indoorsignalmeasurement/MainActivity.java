package com.example.klemen.indoorsignalmeasurement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.CallAudioState;
import android.telecom.TelecomManager;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthLte;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.apache.http.conn.*;
import org.apache.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;


import javax.net.ssl.HttpsURLConnection;

import static android.R.id.list;
import static android.widget.Toast.LENGTH_LONG;


import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;



public class MainActivity extends AppCompatActivity implements LocationListener {

    TelephonyManager telemanager;
    ConnectivityManager connManager;
    WifiManager wifiManager;
    GsmSignalStrengthListener gsmListener;
    GsmCellLocation cellLocation;
    List<CellInfo> cellInfoList;
    int cellSig, cellID, cellMcc, cellMnc, cellPci, cellTac = 0;
    public int mastertype = 0;

    public int lte_cell_id_to_send = 0;
    public int umts_cell_id_to_send = 0;
    public int gsm_cell_id_to_send = 0;

    public String lte_SS = "";
    public String umts_SS = "";
    public String gsm_SS = "";

    public String lte_SQ = "";
    public String umts_SQ = "";
    public String gsm_SQ = "";

    private final int CALL_REQUEST = 100;

    int mcc;
    int mnc;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Double latitude = 0.0;
    Double longitude = 0.0;
    public String[][] items;
    public String[] cells;

    public int numberOfRows = 100;
    //public int[] cellids = {496134,101};
    //public String[] cellnames = {"sneberje","fgf"};
    public int cellid = 101;
    //public ArrayList<String> crunchifyResult = new ArrayList<String>();
    public String krnekitest;
    public String[][] cellids = new String[100][2];
    String input_get = null;
    public RequestQueue SendDataQueue;

    //public String insertURL = getString(R.string.api_ip)+"/data";

    public String showURL = "http://192.168.1.100/readMeas.php";
    public GraphView graphView;

    private LineGraphSeries<DataPoint> mSeries;
    private double graph2LastXValue = 5d;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cellLocation = new GsmCellLocation();
        gsmListener = new GsmSignalStrengthListener();
        telemanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telemanager.listen(gsmListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        TextView textView = (TextView) findViewById(R.id.coordinates);
        textView.setText("searching for satelites :)");


        ButtonClick2();

        //ButtonClickMakeCall();

        SavedMeas();


        GraphView graphView = (GraphView) findViewById(R.id.graph);
        mSeries = new LineGraphSeries<>();
        graphView.addSeries(mSeries);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(40);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(-40);
        graphView.getViewport().setMinY(-130);


        graphView.getViewport().setScrollable(true);

        GridLabelRenderer gridLabel = graphView.getGridLabelRenderer();

        gridLabel.setHorizontalAxisTitle("Seconds");
        gridLabel.setVerticalAxisTitle("[dBm]");

        gridLabel.setGridColor(Color.parseColor("#FFA726"));
        gridLabel.setVerticalAxisTitleColor(Color.parseColor("#FFA726"));
        gridLabel.setHorizontalAxisTitleColor(Color.parseColor("#FFA726"));
        gridLabel.setHorizontalLabelsColor(Color.parseColor("#FFA726"));
        gridLabel.setVerticalLabelsColor(Color.parseColor("#FFA726"));


        PusherOptions options = new PusherOptions();
        options.setCluster("eu");
        Pusher pusher = new Pusher("bff446cfed9e4cfc6570", options);

        Channel channel = pusher.subscribe("my-channel");

        channel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // TODO background code
                        callPhoneNumber();
                    }
                });
                    Log.i("push",data);
            }
        });

        pusher.connect();

        callAsynchronousTask_send_data();

        callAsynchronousTask_draw_graph();



    }

    public void callAsynchronousTask_send_data() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {

                            SendData();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000); // ms
    }


    public void callAsynchronousTask_draw_graph() {

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {

                            graph2LastXValue += 1d;

                            if(mastertype==1)
                            {mSeries.appendData(new DataPoint(graph2LastXValue, Integer.parseInt(gsm_SS)), true, 40);}

                            else if (mastertype == 2)
                            {mSeries.appendData(new DataPoint(graph2LastXValue, Integer.parseInt(umts_SS)), true, 40);}

                            else if (mastertype==3)
                            {mSeries.appendData(new DataPoint(graph2LastXValue, Integer.parseInt(lte_SS)), true, 40);}


                        } catch (Exception e) {
                            // TODO Auto-generated catch block

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 500); // ms
    }


    public void ButtonClick2() {

        Button info = (Button) findViewById(R.id.infobutton);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, info.class);
                startActivity(intent);

            }
        });
    }


    public void SavedMeas() {

        Button info = (Button) findViewById(R.id.savedmeasbutton);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_id = getIntent().getStringExtra("user_id");
                Intent intent = new Intent(MainActivity.this, savedmeas.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });


    }


    public void SendData(){

        final RequestQueue SendDataQueue = Volley.newRequestQueue(this);

        final String url_send_data = getString(R.string.api_ip)+"data";


        //get current time stamp
        Long tsLong = System.currentTimeMillis()/1000 + (2*60*60);

        String ts = tsLong.toString();

        //get current user_id

        String user_id = getIntent().getStringExtra("user_id");
        Log.i("cell", String.valueOf(cellID));

        Log.i("date", ts);
        // construct json object from username and password fields
        final JSONObject obj = new JSONObject();
        try {
            obj.put("datetime", ts);
            obj.put("user_id", user_id);

            if(mastertype==1)
            {
                if (Integer.parseInt(gsm_SS)<0)
                {
                    obj.put("rat","2g");
                    obj.put("cell_id", String.valueOf(gsm_cell_id_to_send));
                    obj.put("signal_strength", gsm_SS);
                    obj.put("signal_quality", 0);
                }
            }

            else if (mastertype==3)
            {
                if (Integer.parseInt(lte_SS)<0)
                {
                    obj.put("rat", "4g");
                    obj.put("cell_id", String.valueOf(lte_cell_id_to_send));
                    obj.put("signal_strength", lte_SS);
                    obj.put("signal_quality", lte_SQ);
                }

            }

            else
            {
                if (Integer.parseInt(umts_SS)<0)
                {
                    obj.put("rat", "3g");
                    obj.put("cell_id", String.valueOf(umts_cell_id_to_send));
                    obj.put("signal_strength", umts_SS);
                    obj.put("signal_quality", 0);
                }
            }

            obj.put("latitude", latitude.toString());
            obj.put("longitude", longitude.toString());


            Log.i("obj_", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest LoginRequest = new JsonObjectRequest(Request.Method.POST, url_send_data, obj,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String response_message=response.getString("message");
                            Log.i("response_json",response_message);
                            Log.i("response_json", Integer.toString(response_message.length()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(MainActivity.this,"Data not sent :(",
                   //     Toast.LENGTH_SHORT).show();
            }
        });


        SendDataQueue.add(LoginRequest);
    }


 /*   public void ButtonClickMakeCall(){
        Button savemeas = (Button) findViewById(R.id.save_meas);

        savemeas.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // TODO background code
                                                    callPhoneNumber();

                                                }
                                            });

                                        }
                                    }
        );

    }*/

    Handler call_handler=new Handler();


    public void callPhoneNumber() {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);

                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + "040929292"));
            startActivity(callIntent);

            SystemClock.sleep(20000);

            Class c = Class.forName(telemanager.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            Object telephonyService = m.invoke(telemanager); // Get the internal ITelephony object
            c = Class.forName(telephonyService.getClass().getName()); // Get its class
            m = c.getDeclaredMethod("endCall"); // Get the "endCall()" method
            m.setAccessible(true); // Make it accessible
            m.invoke(telephonyService); // invoke endCall()




        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == CALL_REQUEST)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
            else
            {
                Toast.makeText(MainActivity.this, "permission not", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub


        latitude = location.getLatitude();
        longitude = location.getLongitude();


        if(latitude == 0.0 && longitude == 0.0){
            TextView textView = (TextView) findViewById(R.id.coordinates);
            textView.setText("searching for satelites :)");

        }else{
            TextView textView = (TextView) findViewById(R.id.coordinates);
            textView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }
    @Override
    public void onProviderDisabled(String provider) {
        TextView textView2 = (TextView) findViewById(R.id.coordinates);
        textView2.setText("GPS disabled");

    }

    @Override
    public void onProviderEnabled(String provider) {
        TextView textView2 = (TextView) findViewById(R.id.coordinates);
        textView2.setText("GPS enabled");

    }





    private class GsmSignalStrengthListener extends PhoneStateListener {
        /* Get the Signal strength from the provider, each tiome there is a change */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            GetMobileData(String.valueOf(signalStrength.getGsmSignalStrength()));
            GetWifiData(String.valueOf(signalStrength.getGsmSignalStrength()));

            int signal_ASU = signalStrength.getGsmSignalStrength();
            int signal_dBm = - 113 + 2 * signal_ASU;
            int bit_error_rate = signalStrength.getGsmBitErrorRate();



            //SOSEDNJE CELICE
            List<NeighboringCellInfo> neighCell = null;
            neighCell = telemanager.getNeighboringCellInfo();
            for (int i = 0; i < neighCell.size(); i++)
            {
                NeighboringCellInfo thisCell = neighCell.get(i);
                int CID = thisCell.getCid();
                int RSSI = thisCell.getRssi();
                int RSSI_GSM = -113 + 2 * RSSI;
                int PSC = thisCell.getPsc();
                int NetworkTypeview = thisCell.getNetworkType();

                //TextView adj1 = (TextView) findViewById(R.id.adj1);
                //TextView adj2 = (TextView) findViewById(R.id.adj2);
                //TextView adj3 = (TextView) findViewById(R.id.adj3);
                //TextView adj4 = (TextView) findViewById(R.id.adj4);

                if(mastertype == 1){

                    //gsm neighbours
                    if(i == 0){
                        //adj1.setText("GSM | " + "Cell ID:" + CID + "  RSSI:" + RSSI_GSM + "dBm"  );
                    }
                    else if(i == 1){
                        //adj2.setText("GSM | " + "Cell ID:" + CID + "  RSSI:" + RSSI_GSM + "dBm"  );
                    }
                    else if(i == 2){
                        //adj3.setText("GSM | " + "Cell ID:" + CID + "  RSSI:" + RSSI_GSM + "dBm"  );
                    }
                    else if(i == 3){
                        //adj4.setText("GSM | " + "Cell ID:" + CID + "  RSSI:" + RSSI_GSM + "dBm"  );
                    }

                }else if (mastertype == 2){
                    //umts neighbours
                    if(i == 0){
                        //adj1.setText("UMTS | "+"RSSI:" + RSSI + "dBm" + "  PSC:" + PSC);
                    }
                    else if(i == 1){
                        //adj2.setText("UMTS | "+"RSSI:" + RSSI + "dBm" + "  PSC:" + PSC);
                    }
                    else if(i == 2){
                        //adj3.setText("UMTS | "+"RSSI:" + RSSI + "dBm" + "  PSC:" + PSC);
                    }
                    else if(i == 3){
                        //adj4.setText("UMTS | "+"RSSI:" + RSSI + "dBm" + "  PSC:" + PSC);
                    }
                }
            }


            //LAC, Cell ID in Scrambling Code dobimo tukaj
            GsmCellLocation cl = (GsmCellLocation) telemanager.getCellLocation();
            TextView cell_lac_view = (TextView)findViewById(R.id.cell_lac_psc);

            if(mastertype == 2){
                int lac_umts = cl.getLac();
                int utran_cid = cl.getCid();
                int cid_short = utran_cid % 65536;//modulo operation (ostanek pri deljenju)
                int rnc_id = utran_cid / 65536;
                umts_cell_id_to_send=cid_short;
                cell_lac_view.setText("Location Area Code: " + lac_umts + "\nCell ID: " + cid_short
                        +"\nRNC ID: " + rnc_id);
                TextView textView3 = (TextView)findViewById(R.id.textView3);
                textView3.setText("---UMTS info---");

            }else if (mastertype == 3){
                int tac_lte = cl.getLac();
                int lte_long_cid = cl.getCid(); //globalen LTE cell id
                int mrbts_id = lte_long_cid / 256;//MRBTS id
                int pci = cl.getPsc();
                lte_cell_id_to_send=lte_long_cid;
                //String lte_long_cid_string=null;


                cell_lac_view.setText("Tracking Area Code: " + tac_lte + "\nCell ID: " + lte_long_cid + "\nBase station ID: " + mrbts_id);
                TextView textView4 = (TextView)findViewById(R.id.textView3);
                textView4.setText("---LTE info---");
            }else{
                int lac = cl.getLac();
                int cid = cl.getCid();
                int psc = cl.getPsc();
                gsm_cell_id_to_send=cid;
                cell_lac_view.setText("Location Area Code: " + lac + "\nCell ID: " + cid);
                TextView textView5 = (TextView)findViewById(R.id.textView3);
                textView5.setText("---GSM info---");
            }


            String ltestr = signalStrength.toString();
            String[] parts = ltestr.split(" ");
            String lterssi = parts[8];
            String ltersrp = parts[9];
            String ltersrq = parts[10];
            //String ltecqi = parts[12];
            String lterssnr = parts[11];

            //EditText indoor_room = (EditText) findViewById(R.id.editText);

            // LineGraphSeries lineGraphSeries = (LineGraphSeries) findViewById(R.id.graph);

            // lineGraphSeries.appendData(Integer.parseInt(ltersrp),true,10);



            try {
                cellInfoList = telemanager.getAllCellInfo();
                for (CellInfo cellInfo : cellInfoList) {
                    if (cellInfo instanceof CellInfoLte) {
                        // cast to CellInfoLte and call all the CellInfoLte methods you need
                        // gets RSRP cell signal strength:
                        cellSig = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();

                        // Gets the LTE cell identity: (returns 28-bit Cell Identity, Integer.MAX_VALUE if unknown)
                        cellID = ((CellInfoLte) cellInfo).getCellIdentity().getCi();

                        // Gets the LTE MCC: (returns 3-digit Mobile Country Code, 0..999, Integer.MAX_VALUE if unknown)
                        cellMcc = ((CellInfoLte) cellInfo).getCellIdentity().getMcc();

                        // Gets theLTE MNC: (returns 2 or 3-digit Mobile Network Code, 0..999, Integer.MAX_VALUE if unknown)
                        cellMnc = ((CellInfoLte) cellInfo).getCellIdentity().getMnc();

                        // Gets the LTE PCI: (returns Physical Cell Id 0..503, Integer.MAX_VALUE if unknown)
                        cellPci = ((CellInfoLte) cellInfo).getCellIdentity().getPci();

                        // Gets the LTE TAC: (returns 16-bit Tracking Area Code, Integer.MAX_VALUE if unknown)
                        cellTac = ((CellInfoLte) cellInfo).getCellIdentity().getTac();
                    }
                }
            } catch (Exception e) {
                Log.d("SignalStrength", "+++++++++++++++++++++++++++++++ null array spot 3: " + e);
            }

            int ltersrq_int = Integer.parseInt(ltersrq);

            int PRB = 100; //ampak to je samo za LTE1800 (B = 20MHz)
            double ltesinr = ltersrq_int + 10*Math.log(PRB * 12);
            DecimalFormat df = new DecimalFormat("#.00");

            double x = 1.66;
            double sinr = 1/(1/(12*ltersrq_int)-x);

            int lterssi_dBm=0;
            int umtsrssi_dBm=0;
            int gsmrssi_dBm=0;

            Log.i("umts", String.valueOf(umtsrssi_dBm));

            lterssi_dBm = 140 - Integer.parseInt(lterssi);

            umtsrssi_dBm = signal_ASU -116;


            lte_SS=ltersrp;
            umts_SS=String.valueOf(umtsrssi_dBm);
            gsm_SS=String.valueOf(signal_dBm);

            lte_SQ=String.valueOf(ltersrq_int);


            if(mastertype == 3){
                TextView signalLTE = (TextView) findViewById(R.id.signal);
                signalLTE.setText("RSRP: " + ltersrp + " dBm"+
                        "\nRSRQ: " + ltersrq_int +  " dB");
            }else if(mastertype == 1){

                TextView signal = (TextView)findViewById(R.id.signal);
                signal.setText("RSSI:" + signal_dBm + " dBm"
                        );
            }else{
                TextView signalUMTS = (TextView) findViewById(R.id.signal);
                signalUMTS.setText("RSCP:" + umtsrssi_dBm +
                         " dBm");
            }
        }
    }


    public void GetData(View view){
        //WIFI
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        String wifiConn="disconnected";

        Log.i("wifi", String.valueOf(activeNetwork.getType()));

        //if ( activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ) {
            if ( activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ) {
                wifiConn = "connected";
                GetWifiData(wifiConn);

            } else {
                //TextView text_view_WIFI_SERVICE = (TextView) findViewById(R.id.text_view_WIFI_SERVICE);
                //text_view_WIFI_SERVICE.setText("");
            }
        }

    public void GetMobileData(String signalStrength){
        TelephonyManager telemanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        Integer networkType= telemanager.getNetworkType();
        String networkOperatorId = telemanager.getNetworkOperator();

        int mcc = Integer.parseInt(networkOperatorId.substring(0, 3));
        int mnc = Integer.parseInt(networkOperatorId.substring(3));

        String networkOperatorName = telemanager.getNetworkOperatorName();
        String simSerialNumber = telemanager.getSimSerialNumber();
        String deviceId = telemanager.getDeviceId();
        Integer dataState = telemanager.getDataState();
        String imsi = telemanager.getSubscriberId();
        String networkTypeMessage = "not connected";
        String dataStateMessageS = "unknown";
        Integer dataStateMessage = telemanager.getDataActivity();//TODO: implement DataState!

        //DATA ACTIVITY
        if(dataStateMessage == 1){
            dataStateMessageS = "Data connecting";
        }
        else if(dataStateMessage == 0){
            dataStateMessageS = "Data activity none";
        }
        else if(dataStateMessage == 2){
            dataStateMessageS = "Data connected";
        }
        else if (dataStateMessage == 3){
            dataStateMessageS = "Data suspended";
        }

        //NETWORK TYPE
        if(networkType == 1){
            networkTypeMessage = "GPRS";
            //TextView title = (TextView)findViewById(R.id.title);
            //title.setText("Displaying info for GSM");
            mastertype = 1;
        }
        else if (networkType == 2){
            networkTypeMessage = "EDGE";
            //TextView title = (TextView)findViewById(R.id.title);
            //title.setText("Displaying info for GSM");
            mastertype = 1;
        }
        else if (networkType == 3){
            networkTypeMessage = "UMTS";
            //TextView title = (TextView)findViewById(R.id.title);
            //title.setText("Displaying info for UMTS");
            mastertype = 2;
        }
        else if (networkType == 4){
            networkTypeMessage = "CDMA";
        }
        else if (networkType == 5){
            networkTypeMessage = "EVDO_0";
        }
        else if (networkType == 6){
            networkTypeMessage = "EVDO_A";
        }
        else if (networkType == 7){
            networkTypeMessage = "1xRTT";
        }
        else if (networkType == 8){
            networkTypeMessage = "HSDPA";
            //TextView title = (TextView)findViewById(R.id.title);
            //title.setText("Displaying info for UMTS");
            mastertype = 2;
        }
        else if (networkType == 9){
            networkTypeMessage = "HSUPA";
            //TextView title = (TextView)findViewById(R.id.title);
            //title.setText("Displaying info for UMTS");
            mastertype = 2;
        }
        else if (networkType == 10){
            networkTypeMessage = "HSPA";
            //TextView title = (TextView)findViewById(R.id.title);
            //title.setText("Displaying info for UMTS");
            mastertype = 2;
        }
        else if (networkType == 11){
            networkTypeMessage = "IDEN";
        }
        else if (networkType == 12){
            networkTypeMessage = "EVDO_B";
        }
        else if (networkType == 13){
            networkTypeMessage = "LTE";
            //TextView title = (TextView)findViewById(R.id.title);
            //title.setText("Displaying info for LTE");
            mastertype = 3;

        }
        else if (networkType == 14){
            networkTypeMessage = "EHRPD";
        }
        else if (networkType == 15){
            networkTypeMessage = "HSPA+";
            //TextView title = (TextView)findViewById(R.id.title);
            //title.setText("Displaying info for UMTS");
            mastertype = 2;
        }

        TextView text_view_TELEPHONY_SERVICE = (TextView) findViewById(R.id.text_view_TELEPHONY_SERVICE);
       /* text_view_TELEPHONY_SERVICE.setText("MNC: " + mnc + "\nMCC: " + mcc + "\nNetworkOperatorName: "
                + networkOperatorName + "\nIMEI: " + deviceId + "\nSimSerialNumber: " + simSerialNumber
                + "\nIMSI: " + imsi + "\nNetworkType:" +networkTypeMessage
                +"\nData State: " + dataStateMessageS);*/
    }

    public void GetWifiData(String wifiConn){

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Integer levelDBm = wifiInfo.getRssi();
        int level = WifiManager.calculateSignalLevel(levelDBm, numberOfLevels);
        String BSSID = wifiInfo.getBSSID();
        String SSID = wifiInfo.getSSID();
        Integer Frequency = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            Frequency = wifiInfo.getFrequency();
        }

        Integer Ip_raw = wifiInfo.getIpAddress();
        String Ip = Formatter.formatIpAddress(Ip_raw);

        Integer LinkSpeed = wifiInfo.getLinkSpeed();

        if(BSSID == null){
            //TextView text_view_WIFI_SERVICE = (TextView) findViewById(R.id.text_view_WIFI_SERVICE);
            //text_view_WIFI_SERVICE.setText("wifi not connected");
        }
        else{
            //TextView text_view_WIFI_SERVICE = (TextView) findViewById(R.id.text_view_WIFI_SERVICE);
            /*text_view_WIFI_SERVICE.setText("Wifi signal level: "
                    +level+" (0-5) " + "\nWifi RSSI: " +levelDBm+" dBm"+"\nBSSID: "+BSSID+ "\nSSID: "
                    + SSID + "\nFrequency: "+Frequency+" MHz"+"\nIp:"+Ip+"\nLinkSpeed: "
                    +LinkSpeed+"Mbps");*/
        }
    }
}
