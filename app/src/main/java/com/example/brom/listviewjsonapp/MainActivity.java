package com.example.brom.listviewjsonapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


// Create a new class, Mountain, that can hold your JSON data

// Create a ListView as in "Assignment 1 - Toast and ListView"

// Retrieve data from Internet service using AsyncTask and the included networking code

// Parse the retrieved JSON and update the ListView adapter

// Implement a "refresh" functionality using Android's menu system


public class MainActivity extends AppCompatActivity {
    private String[] mountainNames = {"Matterhorn", "Mont Blanc", "Denali"};
    private String[] mountainLocations = {"Alps", "Alps", "Alaska"};
    private int[] mountainHeights = {4478, 4808, 6190};
    private ArrayList<String> listData;
    private ArrayList<Mountain> waqarsBerg = new ArrayList<>();
    private ArrayAdapter<Mountain> adapter;
    ListView my_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

           /* Mountain m = new Mountain ("R2");
            Mountain m2 = new Mountain("Fuji","Japan",3776);
            TextView tv = findViewById(R.id.list_item_textview);
            tv.setText(m2.info());*/

        //listData = new ArrayList<>(Arrays.asList(mountainNames));
            /*for (int i=0; i<mountainNames.length; i++){
                waqarsBerg.add(new org.brohede.marcus.listviewapp.Mountain(mountainNames[i],mountainLocations[i],mountainHeights[i]));
                String[] waq = new String[] {"All","is","well!"};
                Toast.makeText(getApplicationContext(), Arrays.toString(waq), Toast.LENGTH_SHORT).show();
                Log.d("EMIL", Arrays.toString(waq));
                Button b =(Button) findViewById(R.id.toasterButton);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText myEdtBox =(EditText) findViewById(R.id.editText);
                        Toast.makeText(getApplicationContext(), myEdtBox.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
                */
        adapter = new ArrayAdapter<Mountain>(this, R.layout.list_item_textview, R.id.list_item_textview);
        my_listview = (ListView) findViewById(R.id.my_listview);

                my_listview.setAdapter(adapter);
                my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override

                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        Toast.makeText(getApplicationContext(),adapter.getItem(position).info(), Toast.LENGTH_SHORT).show();
                    }
                });


        //Log.d("WAQAR",waqarsBerg.get(0).getName());

        new FetchData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            Log.d("Waqar_debug", "Refresh pressed!");
            new FetchData().execute();
            return true;


        } else if (id == R.id.action_settings) {
            Log.d("Waqar_debug", "Settings has been pressed");
            return true;
        } else {
            return false;
        }


    }

    private class FetchData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // These two variables need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a Java string.
            String jsonStr = null;

            try {
                Log.d("a18waqje", "TRY");
                // Construct the URL for the Internet service
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    Log.d("a18waqje", "inputStream == null");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    Log.d("a18waqje", "buffer.length() == 0");
                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;
            } catch (Exception e) {
                Log.e("a18waqje", "IOException:" + e.getMessage());
                // If the code didn't successfully get the weather data, there's no point in
                // attempting to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.d("a18waqje", "Network error. Closing streamd" + e.getMessage());
                    }
                }
                Log.d("a18waqje", "Finally");

            }
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            if (o != null) {
                Log.d("a18waqje", o);
            } else {
                Log.d("a18waqje", "Null was received");
            }

            // This code executes after we have received our data. The String object o holds
            // the un-parsed JSON string or is null if we had an IOException during the fetch.

            // Implement a parsing code that loops through the entire JSON and creates objects
            // of our newly created Mountain class.


            try
            {
                // For loop
                JSONArray mountains = new JSONArray(o);
                Log.d("a18waqje", mountains.get(0).toString());
                //JSONObject obj =  mountains.getJSONObject(0);
                //Log.d( "Mountain",obj.getString("ID"));
                //Log.d( "Mountain",""+mountains.length());
                //String id = obj.getString("ID");

                adapter.clear();

                //JSONArray aProperty = obj.getJSONArray("properties");
                for (int i = 0; i < mountains.length(); i++) {
                    Log.d("a18waqje", "" + mountains.length());

                    JSONObject ajProperty = mountains.getJSONObject(i);

                    String name = (String) ajProperty.get("name");
                    Log.d("a18waqje", name);
                    String location = ajProperty.getString("location");
                    int height = ajProperty.getInt("size");
                    Log.d("a18waqje", name + "," + location + "," + height);
                    adapter.add(new Mountain(name, location, height));
                    Log.d("a18waqje", ajProperty.getString("name"));


/*
                    my_listview.setAdapter(adapter);
                    my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override

                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                            Toast.makeText(getApplicationContext(),waqarsBerg.get(position).info(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    */




/*
                    ArrayAdapter<Mountain> adapter=new ArrayAdapter<Mountain>(getApplicationContext(),R.layout.list_item_textview,R.id.list_item_textview,waqarsBerg);

                    ListView my_listview=(ListView) findViewById(R.id.my_listview);

                    my_listview.setAdapter(adapter);
  */
                }
            }

            // Skapa Mountain obj för varje varv
            // Lägg till nya Mountain oibj i ArrayAdapter


            catch (Exception e) {
                Log.e("a18waqje", "E:" + e.getMessage());
            }

        }
    }
}

