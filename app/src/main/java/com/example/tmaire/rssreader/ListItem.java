package com.example.tmaire.rssreader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.tmaire.rssreader.pck_classes.Item;
import com.example.tmaire.rssreader.pck_classes.ItemAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ListItem extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        Log.i("RssReader", "Début activité ListItem");

        //Récupération de l'intent
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        try {
            Log.i("RssReader", "Début try");
            Log.i("RssReader", "Debut URL" + url);
            URL uri = new URL(url);

            Log.i("RssReader", "Stream");
            InputStream urlStream = uri.openStream();

            Log.i("RssReader", "Début Parser");
            XmlParser parser = new XmlParser();
            List<XmlParser.Entry> entries = parser.parse(urlStream);

            //Init d'une liste d'item
            ArrayList<Item> listItem = new ArrayList<Item>();

            for(XmlParser.Entry entry : entries){
                Item item = new Item(entry.title, entry.description, entry.link);
                listItem.add(item);
            }

            // Création itemAdapter
            ItemAdapter itemAdapter = new ItemAdapter(this, listItem);

            ListView list = (ListView) findViewById(R.id.listViewItem);
            list.setAdapter(itemAdapter);

            Log.i("RssReader", "Fin du Try");

        } catch (IOException e) {
            Log.i("RssReader", "Catch IOException");
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            Log.i("RssReader", "Catch XmlPullParser");
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
