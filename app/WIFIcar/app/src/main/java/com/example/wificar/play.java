package com.example.wificar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class play extends AppCompatActivity implements View.OnClickListener {

    String ip;
    private EditText select_play;
    private EditText M1,S1,F1,N1,T,D;
    private SeekBar seekBar;
    private Handler handler;
    TextView textView1,textView2;
    private ToggleButton toggleButton1,toggleButton2,toggleButton3;
    private final int PLAY = 1;
    private final int LAST = 2;
    private final int NEXT = 3;
    private final int OK = 4;
    private final int set_volue = 5;
    private final int beep = 6;
    private final int TIMESET = 7;
    private final int LIGHT = 8;
    private final int TALKTIME = 9;
    private final int CLEAN = 10;
    private final int DISTIME= 11;
    String volueVal;
    String number;
    String num;
    String shi;
    String fen;
    String time1 , delay1;
    String flag;
    int current = 1;
    private ToggleButton toggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.last).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.update).setOnClickListener(this);
        findViewById(R.id.timeSet).setOnClickListener(this);
        findViewById(R.id.light).setOnClickListener(this);
        findViewById(R.id.talkTime).setOnClickListener(this);
        findViewById(R.id.disTime).setOnClickListener(this);

        textView1 = findViewById(R.id.PT);
        textView2 = findViewById(R.id.VT);

        toggleButton1 = findViewById(R.id.light);
        toggleButton2 = findViewById(R.id.talkTime);
        toggleButton = findViewById(R.id.play);
        toggleButton3 = findViewById(R.id.disTime);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ip = bundle.getString("IP", "");
        Toast.makeText(play.this, "获取IP:" + ip, Toast.LENGTH_SHORT).show();
        select_play = findViewById(R.id.number);
        seekBar = findViewById(R.id.volue);
        HandlerThread handlerThread = new HandlerThread("Http");
        handlerThread.start();
        handler = new play.HttpHandler(handlerThread.getLooper());
        M1 = findViewById(R.id.number1);
        S1 = findViewById(R.id.shi);
        N1 = findViewById(R.id.flag);
        F1 = findViewById(R.id.fen);
        T = findViewById(R.id.time);
        D = findViewById(R.id.delay);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volueVal = String.valueOf(seekBar.getProgress());
                handler.sendEmptyMessage(set_volue);

            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                handler.sendEmptyMessage(PLAY);
                break;
            case R.id.last:
                handler.sendEmptyMessage(LAST);
                break;
            case R.id.next:
                handler.sendEmptyMessage(NEXT);
                break;
            case R.id.ok:
                handler.sendEmptyMessage(OK);
                break;
            case R.id.update:
                handler.sendEmptyMessage(beep);
                break;
            case R.id.timeSet:
                handler.sendEmptyMessage(TIMESET);
                break;
            case R.id.light:
                handler.sendEmptyMessage(LIGHT);
                break;
            case R.id.talkTime:
                handler.sendEmptyMessage(TALKTIME);
                break;
            case R.id.clear:
                handler.sendEmptyMessage(CLEAN);
                break;
            case R.id.disTime:
                handler.sendEmptyMessage(DISTIME);
                break;

            default:
                break;
        }

    }

    class HttpHandler extends Handler {
        public HttpHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                    case PLAY:
                        plays();
                        break;
                    case LAST:
                        last();
                        break;
                    case NEXT:
                        next();
                        break;
                    case OK:
                        ok();
                        break;
                    case set_volue:
                        set();
                        break;
                    case beep:
                        update();
                        break;
                    case TIMESET:
                        Timeset();
                        break;
                    case LIGHT:
                        light();
                        break;
                    case TALKTIME:
                        talktime();
                        break;
                    case CLEAN:
                        clean();
                        break;
                    case DISTIME:
                        distime();
                        break;
                    default:
                        break;
            }
        }

        private void distime() {
            if(toggleButton3.isChecked()){
                String registerUrl = "http://" + ip + "/control?distime=1" ;
                textView1.setText("打开时间显示！");
                Toast.makeText(play.this, "打开时间显示！", Toast.LENGTH_SHORT).show();
                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {


                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {
                String registerUrl = "http://" + ip + "/control?distime=0" ;
                textView1.setText("关闭时间显示！");
                Toast.makeText(play.this, "关闭时间显示！", Toast.LENGTH_SHORT).show();

                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {


                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        private void clean() {
            String registerUrl = "http://" + ip + "/control?clean=1" ;
            textView1.setText("闹钟清空");
            Toast.makeText(play.this, "闹钟清空", Toast.LENGTH_SHORT).show();
            try {
                URL url = new URL(registerUrl);
                final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(1000 * 5);
                httpURLConnection.setReadTimeout(1000 * 5);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                final StringBuilder buffer = new StringBuilder();
                int code = httpURLConnection.getResponseCode();
                if (code == 200) {
                    httpURLConnection.disconnect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        buffer.append(line);
                    }
                    runOnUiThread(() -> {


                    });
                }
                httpURLConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void talktime() {
            if(toggleButton2.isChecked()){
                String registerUrl = "http://" + ip + "/control?talktime=1" ;
                textView1.setText("打开整点报时");
                Toast.makeText(play.this, "开启报时！", Toast.LENGTH_SHORT).show();
                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {


                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                textView1.setText("关闭整点报时");
                String registerUrl = "http://" + ip + "/control?talktime=0" ;
                Toast.makeText(play.this, "关闭成功！", Toast.LENGTH_SHORT).show();
                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {


                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void light() {
            if(toggleButton1.isChecked()){
                textView1.setText("打开卡片灯光");
                String registerUrl = "http://" + ip + "/control?light=1" ;
                Toast.makeText(play.this, "开灯成功！", Toast.LENGTH_SHORT).show();
                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {


                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {
                textView1.setText("关闭卡片灯光");
                String registerUrl = "http://" + ip + "/control?light=0" ;
                Toast.makeText(play.this, "关灯成功！", Toast.LENGTH_SHORT).show();
                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {


                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        private void Timeset() {

            time1 = T.getText().toString();
            delay1 = D.getText().toString();
            textView1.setText("打开卡片灯光");
            if((time1!=null)&&(delay1!=null)) {
                String registerUrl = "http://" + ip + "/delaydis?delaydis=A" + time1 + "A" + delay1;
                Toast.makeText(play.this, "设置成功！" + volueVal, Toast.LENGTH_SHORT).show();
                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {


                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                textView1.setText("不能发送空数据！");
            }

        }

        private void update() {
            num = M1.getText().toString();
            flag = N1.getText().toString();
            shi = S1.getText().toString();
            fen = F1.getText().toString();
            if((num!=null)&&(flag!=null)&&(shi!=null)&&(fen!=null)) {
                String registerUrl = "http://" + ip + "/settime?setting=A" + num + "A" + flag + "A" + shi + "A" + fen;
                Toast.makeText(play.this, "设置成功！", Toast.LENGTH_SHORT).show();
                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {


                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else
            {
                textView1.setText("不能发送空数据！");
            }
        }

        private void set() {
            String registerUrl = "http://" + ip + "/volue?volue="+volueVal ;
            textView2.setText("音量："+volueVal);
            Toast.makeText(play.this, "音量设置为："+volueVal, Toast.LENGTH_SHORT).show();
            try {
                URL url = new URL(registerUrl);
                final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(1000 * 5);
                httpURLConnection.setReadTimeout(1000 * 5);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                final StringBuilder buffer = new StringBuilder();
                int code = httpURLConnection.getResponseCode();
                if (code == 200) {
                    httpURLConnection.disconnect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        buffer.append(line);
                    }
                    runOnUiThread(() -> {


                    });
                }
                httpURLConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void plays() {
            if(toggleButton.isChecked()) {
                toggleButton.setBackgroundResource(R.drawable.play);
                String registerUrl = "http://" + ip + "/control?play=1";
                textView1.setText("当前文件位置："+current);
                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {

                            // Toast.makeText(MainActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{

                toggleButton.setBackgroundResource(R.drawable.pause);
                String registerUrl = "http://" + ip + "/control?play=0";
                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {

                            // Toast.makeText(MainActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        private void last() {
            String registerUrl = "http://"+ip+"/control?last=1";
            current = current-1;
            textView1.setText("当前文件位置："+current);
            if(current<0){
                current=0;
            }
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

        private void next() {
            String registerUrl = "http://"+ip+"/control?next=1";
            current = current+1;
            textView1.setText("当前文件位置："+current);
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

        private void ok() {
            number =  select_play.getText().toString();
            String registerUrl = "http://"+ip+"/select?ok="+number;
            current=Integer.parseInt(number);
            textView1.setText("当前文件位置："+current);
            if(number!=null) {
                try {
                    URL url = new URL(registerUrl);
                    final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(1000 * 5);
                    httpURLConnection.setReadTimeout(1000 * 5);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    final StringBuilder buffer = new StringBuilder();
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        httpURLConnection.disconnect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        runOnUiThread(() -> {

                            // Toast.makeText(MainActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
                        });
                    }
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else
            {
                textView1.setText("不能发送空数据！");
            }
        }


    }
}