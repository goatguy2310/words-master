package com.tundt.main;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tundt.adapter.NoteAdapter;
import com.tundt.model.Note;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    ImageButton btnAdd;
    ListView lvNotes;
    ArrayList<Note> noteArr;
    NoteAdapter adapterNote;

    Button btnPlayGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        lvNotes = (ListView) findViewById(R.id.lvNotes);
        noteArr = new ArrayList<>();
        selectAll();
        adapterNote = new NoteAdapter(NoteActivity.this, R.layout.item_notes, noteArr);
        lvNotes.setAdapter(adapterNote);

        btnAdd = (ImageButton) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(NoteActivity.this, AddActivity.class);
                startActivity(it);
            }
        });

        btnPlayGame = (Button) findViewById(R.id.btnPlayGame);

        btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Intent it = new Intent(NoteActivity.this, GuessActivity.class);
                startActivity(it);
            }
        });
    }

    private void selectAll() {
        Cursor c = Const.noteDatabase.query("notes", null, null, null, null, null, null);
        noteArr.clear();
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String word = c.getString(1);
            String meaning = c.getString(2);
            noteArr.add(new Note(word, meaning, id));
        }
        c.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectAll();
        adapterNote.notifyDataSetChanged();
    }
}
