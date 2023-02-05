package com.example.wificar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.util.Calendar.*;
import static java.util.Calendar.HOUR_OF_DAY;

public class notify extends AppCompatActivity implements View.OnClickListener {

    String ip;
    private EditText M1,S1,F1,N1;

    String num;
    String shi;
    String fen;
    String hour , minute;
    String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ip = bundle.getString("IP", "");
        Toast.makeText(notify.this, "获取IP:" + ip, Toast.LENGTH_SHORT).show();
        M1 = findViewById(R.id.number1);
        S1 = findViewById(R.id.shi);
        N1 = findViewById(R.id.flag);
        F1 = findViewById(R.id.fen);


        findViewById(R.id.update).setOnClickListener(this);
        findViewById(R.id.timeup).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.update){

                num = M1.getText().toString();
                shi = S1.getText().toString();
                fen = F1.getText().toString();
                flag = N1.getText().toString();
                String registerUrl = "http://"+ip+"/settime?setting="+num+flag+shi+fen;
                try
                {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200)
                    {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null)
                        {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {

                            // Toast.makeText(MainActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            Toast.makeText(notify.this, "设置成功！", Toast.LENGTH_SHORT).show();
            }




        if(v.getId()==R.id.timeup){
            Calendar calendar = getInstance();

             hour = String.valueOf(calendar.get(HOUR_OF_DAY));

             minute = String.valueOf(calendar.get(MINUTE));

            String registerUrl = "http://"+ip+"/control?updata=1";
            try
            {
                URL url = new URL(registerUrl);
                final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(1000 * 5);
                httpURLConnection.setReadTimeout(1000 * 5);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                final StringBuilder buffer = new StringBuilder();
                int code = httpURLConnection.getResponseCode();
                if (code == 200)
                {
                    httpURLConnection.disconnect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        buffer.append(line);
                    }
                    runOnUiThread(() -> {

                        // Toast.makeText(MainActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
                    });
                }
                httpURLConnection.disconnect();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}