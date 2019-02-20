package com.tundt.main;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.tundt.adapter.HighAdapter;
import com.tundt.model.HighScore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class HighActivity extends AppCompatActivity {

    ListView lvHigh;
    ArrayList<HighScore> highArr;
    HighAdapter adapterHigh;

    String data_name = "high_score.db";
    String data_path = "/databases/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.hi_title);

        setContentView(R.layout.activity_high);
        addControls();
        addEvents();
        showScores();
    }

    public void addControls() {
        lvHigh = (ListView) findViewById(R.id.lvHigh);
        highArr = new ArrayList<>();
        adapterHigh = new HighAdapter(HighActivity.this, R.layout.high_item, highArr);
        lvHigh.setAdapter(adapterHigh);
    }

    public void addEvents() {
        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(HighActivity.this).create();
                alertDialog.setTitle(getString(R.string.clear));
                alertDialog.setMessage(getString(R.string.clear_msg));
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Const.database = openOrCreateDatabase(data_name, MODE_PRIVATE, null);
                                ContentValues value = new ContentValues();
                                value.put("score", 0);
                                value.put("name", "-");
                                Const.database.update("scores", value, null, null);
                                showScores();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
            }
        });
    }

    public void showScores() {
        Const.database = openOrCreateDatabase(data_name, MODE_PRIVATE, null);
        Cursor cursor = Const.database.query("scores", null, null, null, null, null, null);
        highArr.clear();
        while(cursor.moveToNext()) {
            String place = cursor.getString(0);
            String name = cursor.getString(1);
            int score = cursor.getInt(2);
            byte[] data = cursor.getBlob(3);

            Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);

            highArr.add(new HighScore(place, name, score, image));
        }
        cursor.close();
        adapterHigh.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
