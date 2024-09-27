package com.tundt.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tundt.main.AddActivity;
import com.tundt.main.Const;
import com.tundt.main.R;
import com.tundt.model.Note;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    Activity context;
    int resource;
    List<Note> objects;

    public NoteAdapter(Activity context, int resource, List<Note> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = this.context.getLayoutInflater().inflate(this.resource, null);

        final TextView txtWord = (TextView) row.findViewById(R.id.txtWordy);
        final TextView txtMeaning = (TextView) row.findViewById(R.id.txtMeaning);

        ImageButton btnEdit = (ImageButton) row.findViewById(R.id.btnEdit);
        ImageButton btnDelete = (ImageButton) row.findViewById(R.id.btnDelete);

        final Note note = this.objects.get(position);

        txtWord.setText(note.getWord());
        txtMeaning.setText(note.getMeaning());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, AddActivity.class);
                it.putExtra("note", objects.get(position));
                context.startActivity(it);
            }
        });

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setTitle("Deleting " + txtWord.getText());
                alertDialog.setMessage("Are you sure you want to delete " + txtWord.getText() + "? This action can't be undone!");
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Const.noteDatabase.delete("notes", "id=?", new String[] {String.valueOf(note.getId())});

                                objects.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        return row;
    }
}
