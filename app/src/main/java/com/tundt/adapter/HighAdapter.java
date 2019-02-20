package com.tundt.adapter;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tundt.main.R;
import com.tundt.model.HighScore;

import java.util.List;

/**
 * Created by admin on 6/13/2017.
 */

public class HighAdapter extends ArrayAdapter<HighScore> {

    Activity context;
    int resource;
    List<HighScore> objects;

    public HighAdapter(Activity context, int resource, List<HighScore> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = this.context.getLayoutInflater().inflate(this.resource, null);

        TextView txtPlace = (TextView) row.findViewById(R.id.txtPlace);
        TextView txtName = (TextView) row.findViewById(R.id.txtName);
        TextView txtScore = (TextView) row.findViewById(R.id.txtHiScore);

        ImageView imgPlace = (ImageView) row.findViewById(R.id.imgPlace);

        final HighScore score = this.objects.get(position);

        txtPlace.setText(score.getPlace());
        txtName.setText(score.getName());
        txtScore.setText("" + score.getScore());

        imgPlace.setImageBitmap(score.getImage());

        return row;
    }
}
