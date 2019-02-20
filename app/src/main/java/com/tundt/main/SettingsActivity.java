package com.tundt.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    TextView txtLevel;
    RadioGroup group;

    RadioButton rbOne;
    RadioButton rbTwo;
    RadioButton rbThree;

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
        if(level.equals("Easy")) group.check(R.id.rbOne);
        else if(level.equals("Hard")) group.check(R.id.rbTwo);
        else if(level.equals("Expert")) group.check(R.id.rbThree);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rbLevel = (RadioButton) group.findViewById(checkedId);
                getSharedPreferences("level", MODE_PRIVATE).edit().putString("level", rbLevel.getText().toString()).apply();
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
