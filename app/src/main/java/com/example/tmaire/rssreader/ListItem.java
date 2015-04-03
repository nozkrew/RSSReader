package com.example.tmaire.rssreader;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tmaire.rssreader.pck_classes.Item;
import com.example.tmaire.rssreader.pck_classes.ItemAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

        new InputStreamTask().execute(intent.getStringExtra("url"));

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

    private class InputStreamTask extends AsyncTask<String, Void, InputStream>{
        @Override
        protected InputStream doInBackground(String... url){
            try {
                InputStream input = new URL(url[0]).openStream();
                Log.i("RssReader", "Debut Try InputStreamTask doInBackground");
                return input;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(InputStream input){
            new RecupeXmlTask().execute(input);
        }
    }

    private class RecupeXmlTask extends AsyncTask<InputStream, Void, List<XmlParser.Entry>>{

        @Override
        protected List<XmlParser.Entry> doInBackground(InputStream... input){
            XmlParser parser = new XmlParser();
            try {
                Log.i("RssReader", "Debut Try RecupeXmlTask doInBackground");
                Log.i("RssReader", "input : " + input[0]);
                if(input[0] != null){
                    Log.i("RssReader", "Début du If");
                    List<XmlParser.Entry> entries = parser.parse(input[0]);
                    Log.i("RssReader", "Entries : " + entries);
                    return entries;
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<XmlParser.Entry> entries){
            if(entries != null) {
                ArrayList<Item> listItem = new ArrayList<Item>();
                for (XmlParser.Entry entry : entries) {
                    Item item = new Item(entry.title, entry.description, entry.link);
                    listItem.add(item);
                }

                ItemAdapter itemAdapter = new ItemAdapter(ListItem.this, listItem);

                final ListView listView = (ListView) findViewById(R.id.listViewItem);
                listView.setAdapter(itemAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View child, int position, long id) {
                        Intent intentBrowser = new Intent(Intent.ACTION_VIEW);
                        Item item = (Item) listView.getItemAtPosition(position);
                        intentBrowser.setData(Uri.parse(item.getLink()));
                        startActivity(intentBrowser);
                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(), "Erreur URL", Toast.LENGTH_LONG).show();
            }
        }
    }
}
