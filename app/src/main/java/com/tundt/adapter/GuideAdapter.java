package com.tundt.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tundt.main.R;
import com.tundt.model.Guider;

import java.util.List;

/**
 * Created by admin on 6/26/2017.
 */

public class GuideAdapter extends ArrayAdapter<Guider>{

    Activity context;
    int resource;
    List<Guider> objects;

    public GuideAdapter(Activity context, int resource, List<Guider> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = this.context.getLayoutInflater().inflate(this.resource, null);

        TextView txtHelp = (TextView) row.findViewById(R.id.txtHelp);
        ImageView imgHelp = (ImageView) row.findViewById(R.id.imgHelp);

        final Guider guider = this.objects.get(position);
        txtHelp.setText(guider.getText());
        if(guider.getResource() != -1) imgHelp.setImageResource(guider.getResource());

        return row;
    }
}
