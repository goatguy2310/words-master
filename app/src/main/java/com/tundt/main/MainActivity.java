package com.tundt.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    EditText txtName;

    String level;

    Const c;

    String score_data_name = "high_score.db";
    String note_data_name = "notes.db";
    String data_path = "/databases/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication app = (MyApplication) getApplication();
        app.loadText();

        processCopy();
        Const.scoreDatabase = openOrCreateDatabase(score_data_name, MODE_PRIVATE, null);
        Const.noteDatabase = openOrCreateDatabase(note_data_name, MODE_PRIVATE, null);

        TextView txtMaster = (TextView) findViewById(R.id.txtMaster);
        txtMaster.setTypeface(Typeface.createFromAsset(getAssets(), "font/KG.ttf"));

        txtName = (EditText) findViewById(R.id.txtName);

        findViewById(R.id.btnPlay).setOnTouchListener(this);
        findViewById(R.id.btnHigh).setOnTouchListener(this);
        findViewById(R.id.btnAbout).setOnTouchListener(this);
        findViewById(R.id.btnLearn).setOnTouchListener(this);
        findViewById(R.id.btnGuide).setOnTouchListener(this);
        findViewById(R.id.btnQuit).setOnTouchListener(this);

        txtMaster.requestFocus();
    }

    public void processCopy() {
        File dbFile = getDatabasePath(score_data_name);
        File dbNoteFile = getDatabasePath(note_data_name);

        if (!dbFile.exists()) {
            try {
                copyDatabase();
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Failed when copy", Toast.LENGTH_LONG).show();
            }
        }

        if (!dbNoteFile.exists()) {
            try {
                copyDatabaseForNote();
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Failed when copy", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void copyDatabase() {
        try {
            InputStream input;

            input = getAssets().open("database/" + score_data_name);

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

    public void copyDatabaseForNote() {
        try {
            InputStream input;

            input = getAssets().open("database/" + note_data_name);

            String outputName = getNoteDatabasePath();

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
        return getApplicationInfo().dataDir + data_path + score_data_name;
    }

    public String getNoteDatabasePath() {
        return getApplicationInfo().dataDir + data_path + note_data_name;
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

                    while(!usernameEnable) {
                        txtName.setError("Please enter your name");
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Intent it = new Intent(MainActivity.this, GameActivity.class);
                    it.putExtra("name", txtName.getText().toString());
                    startActivity(it);

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
                case R.id.btnLearn:
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Intent it4 = new Intent(MainActivity.this, NoteActivity.class);
                    startActivity(it4);
                    break;
                case R.id.btnGuide:
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Intent it5= new Intent(MainActivity.this, GuideActivity.class);
                    startActivity(it5);
                    break;
                case R.id.btnQuit:
                    finish();
                    break;
            }
        }
        return true;
    }
}
