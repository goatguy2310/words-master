package com.tundt.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tundt.main.R;

import java.util.List;

/**
 * Created by admin on 6/20/2017.
 */

public class TextAdapter extends ArrayAdapter<String>{
    @NonNull Activity context;
    @LayoutRes int resource;
    @NonNull List<String> objects;

    public TextAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = this.context.getLayoutInflater().inflate(this.resource, parent, false);
        TextView txtItem = (TextView) row.findViewById(R.id.txtItem);
        txtItem.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "font/KG.ttf"));
        txtItem.setText(objects.get(position));
        return row;
    }
}
