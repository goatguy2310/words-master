package com.tundt.main;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tundt.model.Note;

public class AddActivity extends AppCompatActivity {

    Button btnSave;

    EditText txtEditWord;
    EditText txtEditMeaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        txtEditWord = (EditText) findViewById(R.id.txtEditWord);
        txtEditMeaning = (EditText) findViewById(R.id.txtEditMeaning);

        Note note = null;

        Intent it = getIntent();
        if(it.hasExtra("note")) {
            note = (Note) it.getSerializableExtra("note");
            txtEditWord.setText(note.getWord());
            txtEditMeaning.setText(note.getMeaning());
        }

        final Note nt = note;

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nt == null) {
                    ContentValues row = new ContentValues();
                    row.put("word", txtEditWord.getText().toString());
                    row.put("meaning", txtEditMeaning.getText().toString());
                    Const.noteDatabase.insert("notes", null, row);

                    finish();
                } else {
                    ContentValues row = new ContentValues();
                    row.put("word", txtEditWord.getText().toString());
                    row.put("meaning", txtEditMeaning.getText().toString());
                    Const.noteDatabase.update("notes", row, "id=?", new String[] {String.valueOf(nt.getId())});

                    finish();
                }
            }
        });
    }
}
