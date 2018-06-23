package com.example.klemen.indoorsignalmeasurement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class splashScreen extends AppCompatActivity {

    private Thread mSplashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView splash_image = (ImageView) findViewById(R.id.splash_img);
        TextView splash_text = (TextView) findViewById(R.id.splash_text);



        mSplashThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    synchronized (this)
                    {
                        wait(10);
                    }
                }
                catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
                finish();

                Intent intent = new Intent("com.example.klemen.indoorsignalmeasurement.Login");

                startActivity(intent);

                //startActivity(new Intent(getApplicationContext(),Login.class));
            }
        };
        mSplashThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if (evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized (mSplashThread)
            {
                mSplashThread.notifyAll();
            }
        }
        return true;
    }
}







