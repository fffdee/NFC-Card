package com.example.wificar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ModeActivity extends AppCompatActivity implements View.OnClickListener {

    String ip = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ip = bundle.getString("IP","");

        findViewById(R.id.M1).setOnClickListener(this);
        findViewById(R.id.M2).setOnClickListener(this);
        findViewById(R.id.M3).setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.M1) {
            Intent intent = new Intent(ModeActivity.this, play.class);
            Bundle bundle = new Bundle();
            bundle.putString("IP", ip);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(v.getId()==R.id.M2) {
            Intent intent = new Intent(ModeActivity.this, notify.class);
            Bundle bundle = new Bundle();
            bundle.putString("IP", ip);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(v.getId()==R.id.M3) {
            Intent intent = new Intent(ModeActivity.this, w2812.class);
            Bundle bundle = new Bundle();
            bundle.putString("IP", ip);
            intent.putExtras(bundle);
            startActivity(intent);
        }


    }
}