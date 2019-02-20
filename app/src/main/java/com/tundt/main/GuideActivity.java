package com.tundt.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tundt.adapter.GuideAdapter;
import com.tundt.model.Guider;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    ListView lvHelp;
    ArrayList<Guider> guideArr;
    GuideAdapter adapterGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.how_title);

        setContentView(R.layout.activity_guide);
        addControls();
        addEvents();
    }

    public void addControls() {
        lvHelp = (ListView) findViewById(R.id.lvHelp);
        guideArr = new ArrayList<>();
        adapterGuide = new GuideAdapter(GuideActivity.this, R.layout.help_item, guideArr);
        lvHelp.setAdapter(adapterGuide);
    }

    public void addEvents() {
        guideArr.add(new Guider("The Grid contains characters that you can tap. When you tap a character, the character will move from the grid to the Text Field", R.drawable.guide1));
        guideArr.add(new Guider("If the word in the Text Field makes sense, tap the Submit button, you'll get score and the word in the Text Field will disappear. Otherwise, you won't. Tap the Delete button to move last character back to the Grid.", R.drawable.guide2));
        guideArr.add(new Guider("If your word is correct, the timer will be reset and you can start for a new word.", R.drawable.guide3));
        guideArr.add(new Guider("Wrong words will receive nothing. Time is the most important thing. If it's overtime, you'll lose.", R.drawable.guide4));
        guideArr.add(new Guider("You're not alone. Power-ups will along your way. There're +Time and +Character into the Grid when your score is more than 50 (Easy) or 75 (Hard) and your word is or more than 3 characters (Hard) and you're lucky, too (Change level in settings).", R.drawable.guide5));
        guideArr.add(new Guider("Your name and score will be saved once you slipped into the Hall of Fame (Or just the High Score page). Easy will have less point than Hard. That's all.", R.drawable.diamond));
        adapterGuide.notifyDataSetChanged();
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
