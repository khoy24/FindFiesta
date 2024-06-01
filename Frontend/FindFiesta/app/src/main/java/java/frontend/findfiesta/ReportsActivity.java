package java.frontend.findfiesta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * class that holds the functionality for the reports page
 */
public class ReportsActivity extends AppCompatActivity {

    /**
     * listview that displays the reports
     */
    private ListView reportsList;
    /**
     * holds list of the report IDs
     */
    private ArrayList<String> reportID = new ArrayList<>(0);
    /**
     * holds list of the reported usernames
     */
    private ArrayList<String> reportedUser = new ArrayList<>(0);

    /**
     * takes user back to previous page
     */
    private Button backButton;

    /**
     * array adapter for all arrarys
     */
    ArrayAdapter<String> arrayAdapter;

    /**
     * completed report
     */
    private Object completed = null;

    /**
     * base url for getting all reports
     */
    private String reportsURL = "http://coms-309-040.class.las.iastate.edu:8080/reports";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_reports);

        Bundle extras = getIntent().getExtras();

        try {
            completed = extras.get("completed");
            if (completed.toString().equals("false")){
                reportsURL = reportsURL + "/incomplete";
            } else if (completed.toString().equals("true")){
                reportsURL = reportsURL + "/complete";
            }
        } catch (Exception e){

        }


        getReports(reportID, reportedUser);

        reportsList = findViewById(R.id.reportsList);

        backButton = findViewById(R.id.backButton50);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent account = new Intent(ReportsActivity.this, AccountActivity.class);
                startActivity(account);
            }
        });

        reportsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String items = (String) adapterView.getItemAtPosition(i);
                if (completed != null){
                    if (completed.toString().equals("true")){
                        Intent myIntent = new Intent(ReportsActivity.this, ViewCompletedReportActivity.class).putExtra("reportID", reportID.get(i)).putExtra("completed", "true");
                        startActivity(myIntent);
                    } else if (completed.toString().equals("false")){
                        Intent myIntent = new Intent(ReportsActivity.this, ViewReportActivity.class).putExtra("reportID", reportID.get(i)).putExtra("completed", "false");
                        startActivity(myIntent);
                    }
                }

            }
        });


    }


    /**
     * method that lists all incompleted (or completed) reports in order from first to last made
     *
     * @param reportID - id of the report
     * @param reportedUser - user that was reported
     */
    private void getReports(ArrayList<String> reportID, ArrayList<String> reportedUser) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                reportsURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        String TypeString = "";

                        ArrayList<String> arrayList = new ArrayList<String>();
                        arrayAdapter = new ArrayAdapter<String>(ReportsActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        reportsList.setAdapter(arrayAdapter);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Integer ID = jsonObject.getInt("id");
                                JSONObject reported = jsonObject.getJSONObject("reported");
                                String repUser = reported.getString("userName");

                                if (ID != null) {
                                    reportID.add(ID.toString());
                                    reportedUser.add(repUser.toString());

                                    arrayAdapter.add("" + reportedUser.get(i));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

}

