package com.example.networking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Mountain> mountainArrayList;
    ArrayAdapter<Mountain> mountainArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mountainArrayList = new ArrayList<>();
        mountainArrayAdapter = new ArrayAdapter<>(this,R.layout.mytextview,R.id.myTextView,mountainArrayList);


        final ListView myListView = findViewById(R.id.myListView);
        myListView.setAdapter(mountainArrayAdapter);


        // Öppnar en snackbar vid klick på något i listan
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int x, long id) {
                // Det item som klickats på skriver ut den förbestämda information funktionen info() håller
                String showInfo = mountainArrayList.get(x).info();
                Snackbar.make(myListView, showInfo, Snackbar.LENGTH_LONG).show();
            }
        });

        // Laddar ner datan
        new JsonTask().execute ("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");
    }


    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONArray myJSONArray = new JSONArray(json);
                for (int i = 0; i < myJSONArray.length(); i++) {
                    // JSON-objekt som Java
                    JSONObject myJSONObject = myJSONArray.getJSONObject(i);

                    // Hämtar ut beståndsdelarna
                    String name = myJSONObject.getString("name");
                    String location = myJSONObject.getString("location");
                    int cost = myJSONObject.getInt("cost");

                    // Skickar beståndsdelarna till konstruktorn
                    Mountain newMountain = new Mountain(name, location, cost);
                    mountainArrayList.add(newMountain);
                }
            } catch (JSONException e) {
                Log.e("brom","E:"+e.getMessage());
            }
            Log.d("TAG", json);
            mountainArrayAdapter.notifyDataSetChanged();
        }
    }
}