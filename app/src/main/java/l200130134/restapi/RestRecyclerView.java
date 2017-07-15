package l200130134.restapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class RestRecyclerView extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final String URL = "https://l200130134.000webhostapp.com/dataku/json-return.php";
    private final String TAG = RestRecyclerView.class.getName();
    protected Context context;
    private RecyclerView.Adapter adapter;
    private List<MahasiswaList> listItems;
    private RecyclerView recyclerView;
    private ProgressDialog Dialog;
    private AlertDialog adsDialog;
    private SwipeRefreshLayout swipeLayout;
    private boolean openDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_main);
        context = getBaseContext();

        listItems = new ArrayList<>();

        //recycleView initialization
        recyclerView = (RecyclerView) findViewById(R.id.myRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        //swipe refresh initialization
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.colorPrimary);

        Dialog = new ProgressDialog(RestRecyclerView.this);

        if (listItems.size() == 0) {
            new LongOperation().execute(URL);
        }
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
                Intent intent = new Intent(context, RestSimple.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        if (listItems.size() == 0) {
            new LongOperation().execute(URL);
        }
        swipeLayout.setRefreshing(false);
    }

    private void errorDialog(String response) {
        LayoutInflater inflater = (LayoutInflater) RestRecyclerView.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog = inflater.inflate(R.layout.item_response_error_server, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(RestRecyclerView.this);
        builder.setTitle("Error Response :");
        builder.setView(dialog);

        TextView error = (TextView) dialog.findViewById(R.id.error_response);
        error.setText(response);

        adsDialog = builder.create();

        adsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                openDialog = false;
            }
        });

        if (!openDialog) {
            openDialog = true;
            adsDialog.show();
        }
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


        protected void onPostExecute(Void unused) {

            // Close progress dialog
            swipeLayout.setRefreshing(false);
            Dialog.dismiss();

            if (Error != null) {
                errorDialog(Error);

            } else {

                // Start Parse Response JSON Data
                try {

                    // Creates a new JSONObject with name/value mappings from the JSON string.
                    JSONObject jsonResponse = new JSONObject(Content);

                    // Returns the value mapped by name if it exists and is a JSONArray.
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

                        MahasiswaList list = new MahasiswaList(nim, nama, jenjang, PT, prodi);
                        listItems.add(list);
                    }

                    adapter = new ListAdapter(listItems, context);
                    recyclerView.setAdapter(adapter);
                    // End Parse Response JSON Data

                } catch (JSONException e) {
                    errorDialog(e.getMessage());
                }


            }
        }

    }

}
