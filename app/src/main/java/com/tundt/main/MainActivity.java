package com.tundt.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    EditText txtName;

    String level;

    Const c;

    String data_name = "high_score.db";
    String data_path = "/databases/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication app = (MyApplication) getApplication();
        app.loadText();

        processCopy();
        Const.database = openOrCreateDatabase(data_name, MODE_PRIVATE, null);

        TextView txtMaster = (TextView) findViewById(R.id.txtMaster);
        txtMaster.setTypeface(Typeface.createFromAsset(getAssets(), "font/KG.ttf"));

        txtName = (EditText) findViewById(R.id.txtName);

        findViewById(R.id.btnPlay).setOnTouchListener(this);
        findViewById(R.id.btnHigh).setOnTouchListener(this);
        findViewById(R.id.btnAbout).setOnTouchListener(this);
        findViewById(R.id.btnGuide).setOnTouchListener(this);
        findViewById(R.id.btnQuit).setOnTouchListener(this);

        txtMaster.requestFocus();
    }

    public void processCopy() {
        File dbFile = getDatabasePath(data_name);

        if (!dbFile.exists()) {
            try {
                copyDatabase();
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Failed when copy", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void copyDatabase() {
        try {
            InputStream input;

            input = getAssets().open("database/" + data_name);

            String outputName = getDatabasePath();

            File f = new File(getApplicationInfo().dataDir + data_path);
            if (!f.exists()) f.mkdir();

            OutputStream output = new FileOutputStream(outputName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getDatabasePath() {
        return getApplicationInfo().dataDir + data_path + data_name;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ImageButton btnImg = (ImageButton) v;

            btnImg.setColorFilter(Color.argb(100, 0, 0, 0));
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            ImageButton btnImg = (ImageButton) v;
            btnImg.setColorFilter(Color.argb(0, 0, 0, 0));
            switch (v.getId()) {
                case R.id.btnPlay:
                    boolean usernameEnable = true;

                    String name = txtName.getText().toString().trim();

                    if(name == "") usernameEnable = false;

                    if(usernameEnable) {
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        Intent it = new Intent(MainActivity.this, GameActivity.class);
                        it.putExtra("name", txtName.getText().toString());
                        startActivity(it);
                    } else {
                        txtName.setError("Please type your username");
                    }
                    break;
                case R.id.btnHigh:
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Intent it2 = new Intent(MainActivity.this, HighActivity.class);
                    startActivity(it2);
                    break;
                case R.id.btnAbout:
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Intent it3 = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(it3);
                    break;
                case R.id.btnGuide:
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Intent it4 = new Intent(MainActivity.this, GuideActivity.class);
                    startActivity(it4);
                    break;
                case R.id.btnQuit:
                    finish();
                    break;
            }
        }
        return true;
    }
}
