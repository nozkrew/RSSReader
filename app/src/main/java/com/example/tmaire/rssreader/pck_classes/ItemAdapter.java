package com.example.tmaire.rssreader.pck_classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tmaire.rssreader.R;

import java.util.ArrayList;


/**
 * Created by tmaire on 02/04/2015.
 */
public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        // Populate the data into the template view using the data object
        title.setText(cutString(item.getTitle()));
        description.setText(item.getDescription());
        // Return the completed view to render on screen
        return convertView;
    }

    public String cutString(String chaine){
        if(chaine.length() >= 40){
            chaine = chaine.substring(0, 40);
            return chaine.concat("...");
        }
        return chaine;
    }
}
