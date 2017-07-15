package l200130134.restapi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class RestSimple extends AppCompatActivity {

    private final String URL = "https://l200130134.000webhostapp.com/dataku/json-return.php";
    private final String TAG = RestSimple.class.getName();
    private Context context;
    private TextView uiUpdate, jsonParsed;
    private EditText serverText;
    private ProgressDialog Dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_main);
        context = getBaseContext();

        // Required initialization
        uiUpdate = (TextView) findViewById(R.id.output);
        jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        Dialog = new ProgressDialog(RestSimple.this);

        final Button GetServerData = (Button) findViewById(R.id.GetServerData);

        GetServerData.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Use AsyncTask execute Method To Prevent ANR Problem
                new LongOperation().execute(URL);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_renew:
                RestSimple.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Class with extends AsyncTask class
    private class LongOperation extends AsyncTask<String, Void, Void> {

        // Required initialization
        private String data = "";
        private String Content;
        private String Error = null;

        protected void onPreExecute() {
            // Start Progress Dialog (Message)
            Dialog.setMessage("Please wait..");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            // Make Post Call To Web Server
            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);
                System.out.println("URL : " + url);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                outputStreamWriter.write(data);
                outputStreamWriter.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    stringBuilder.append(line).append("\n");
                }

                // Append Server Response To Content String
                Content = stringBuilder.toString();
            } catch (Exception e) {
                Log.e(TAG, "Error at : " + e.getMessage());
                Error = e.getMessage();
            } finally {
                try {
                    if (reader != null) reader.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error at : " + e.getMessage());
                    Error = e.getMessage();
                }
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(Void unused) {

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {
                uiUpdate.setText("Error in : " + Error);
            } else {

                // Show Response Json On Screen (activity)
                uiUpdate.setText(Content);

                // Start Parse Response JSON Data

                String OutputData = "";

                try {

                    // Creates a new JSONObject with name/value mappings from the JSON string.
                    JSONObject jsonResponse = new JSONObject(Content);

                    // Returns the value mapped by name if it exists and is a JSONArray.
                    // Returns null otherwise.
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("Data");

                    // Process each JSON Node

                    int lengthJsonArr = jsonMainNode.length();

                    for (int i = 0; i < lengthJsonArr; i++) {
                        // Get Object for each JSON node.
                        JSONObject childNode = jsonMainNode.getJSONObject(i);

                        // Fetch node values
                        String nim = childNode.optString("nim");
                        String nama = childNode.optString("nama");
                        String jenjang = childNode.optString("jenjang");
                        String PT = childNode.optString("PT");
                        String prodi = childNode.optString("prodi");


                        OutputData +=
                                "NIM 		    : " + nim + " \n "
                                        + "Nama 		: " + nama + " \n "
                                        + "Jenjang 		: " + jenjang + " \n "
                                        + "PT 			: " + PT + " \n "
                                        + "Prodi 		: " + prodi + " \n "
                                        + "----------------------------------------------------------\n";

                        //Log.i("JSON parse", song_name);
                    }
                    // End Parse Response JSON Data

                    //Show Parsed Output on screen (activity)
                    jsonParsed.setText(OutputData);


                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }

    }

}