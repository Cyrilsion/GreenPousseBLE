package com.example.greenpousse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.greenpousse.models.Dechet;
import com.example.R;

import java.util.ArrayList;


public class ListAdapter extends ArrayAdapter<Dechet> {

   private Integer cross = R.drawable.cross;
   private  Integer tick = R.drawable.tick;
   private Integer notFound = R.drawable.pagenotfound;
   private ArrayList<Dechet> dechets;

    public ListAdapter(Context context, ArrayList<Dechet> dechets) {
        super(context, R.layout.simpledechetview, dechets);
        this.dechets = dechets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;

        if(rowView == null) rowView = inflater.inflate(R.layout.simpledechetview, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        textView.setText(dechets.get(position).getNom());
        if (dechets.get(position).isCompostable()) imageView.setImageResource(tick);
        else if(dechets.get(position).getId() == -1) imageView.setImageResource(notFound);
        else imageView.setImageResource(cross);


        return rowView;
    }
}