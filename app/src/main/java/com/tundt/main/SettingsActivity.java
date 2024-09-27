package com.tundt.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tundt.model.Note;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class SettingsActivity extends AppCompatActivity {

    TextView txtLevel;
    RadioGroup group;

    RadioButton rbOne;
    RadioButton rbTwo;
    RadioButton rbThree;

    CheckBox chkRemind;
    EditText txtRemind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.settings_title);

        setContentView(R.layout.activity_settings);
        txtLevel = (TextView) findViewById(R.id.txtLevel);
        rbOne = (RadioButton) findViewById(R.id.rbOne);
        rbTwo = (RadioButton) findViewById(R.id.rbTwo);
        rbThree = (RadioButton) findViewById(R.id.rbThree);

        group = (RadioGroup) findViewById(R.id.group);

        rbOne.setTypeface(Typeface.createFromAsset(getAssets(), "font/KG.ttf"));
        rbTwo.setTypeface(Typeface.createFromAsset(getAssets(), "font/KG.ttf"));
        rbThree.setTypeface(Typeface.createFromAsset(getAssets(), "font/KG.ttf"));

        String level = getSharedPreferences("level", MODE_PRIVATE).getString("level", "Easy");
        if (level.equals("Easy")) group.check(R.id.rbOne);
        else if (level.equals("Hard")) group.check(R.id.rbTwo);
        else if (level.equals("Custom (From word list)")) group.check(R.id.rbThree);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rbLevel = (RadioButton) group.findViewById(checkedId);
                getSharedPreferences("level", MODE_PRIVATE).edit().putString("level", rbLevel.getText().toString()).apply();
            }
        });

        txtRemind = findViewById(R.id.txtRemind);

        chkRemind = (CheckBox) findViewById(R.id.chkRemind);
        chkRemind.setChecked(getSharedPreferences("remind", MODE_PRIVATE).getBoolean("remind", false));

        chkRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    int interval;
                    if(!txtRemind.getText().toString().equals("")) {
                        interval = Integer.parseInt(txtRemind.getText().toString());
                    } else interval = 1;
                    Calendar ca = Calendar.getInstance();

                    Intent it = new Intent(SettingsActivity.this, NotificationReceiver.class);

                    PendingIntent pid = PendingIntent.getBroadcast(SettingsActivity.this, 100, it, PendingIntent.FLAG_UPDATE_CURRENT);

                    AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, ca.getTimeInMillis(), AlarmManager.INTERVAL_HOUR * interval, pid);
                } else {
                    Intent intentStop = new Intent(SettingsActivity.this, NotificationReceiver.class);
                    PendingIntent senderStop = PendingIntent.getBroadcast(SettingsActivity.this, 100, intentStop, 0);
                    AlarmManager alarmManagerStop = (AlarmManager) getSystemService(ALARM_SERVICE);

                    alarmManagerStop.cancel(senderStop);
                }
                getSharedPreferences("remind", MODE_PRIVATE).edit().putBoolean("remind", isChecked).apply();
            }
        });
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
