package java.frontend.findfiesta;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * class that holds functionality for the view reports page
 */
public class ViewReportActivity extends AppCompatActivity{

    /**
     * button that takes user back to previous page
     */
    private Button backButton;
    /**
     * displays the reported user's username
     */
    private TextView reportedUserText;
    /**
     * holds the reported user's username
     */
    public String usernameOfReported;
    /**
     * displays the reports comments
     */
    private TextView reportComments;
    /**
     * holds the id of the reported post
     */
    private String reportedPostId;
    /**
     * button that sends user to view a reported post
     */
    private Button viewReportedPostButton;
    /**
     * button that sends user to view a reported account
     */
    private Button viewReportedAccountButton;
    /**
     * button that deletes a report
     */
    private Button deleteReport;
    /**
     * button that disables an account
     */
    private Button disableAccountButton;

    /**
     * url to delete a report
     */
    private String deleteURL = "http://coms-309-040.class.las.iastate.edu:8080/reports/delete/";
    /**
     * url to mark report as complete
     */
    private String completeReportURL;

    /**
     * base url for moderator notes
     */
    private String modNotesURL = "http://coms-309-040.class.las.iastate.edu:8080/reports/mod_notes/";

    /**
     * url to disable accounts
     */
    private String disableAccountURL = "http://coms-309-040.class.las.iastate.edu:8080/actors/disable/";
    /**
     * allows moderators to make comments on reports
     */
    private EditText moderatorNotes;
    /**
     * button to submit moderator notes
     */
    private Button submitModNotes;
    /**
     * button to delete the reported post
     */
    private Button deleteReportedPostButton;
    /**
     * holds the report's id as an object
     */
    private Object reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_viewreports);
        Bundle extras = getIntent().getExtras();
        try {
            reportId = extras.get("reportID");
            getReportByID(reportId);
//        links for CRUDL
            deleteURL = deleteURL + reportId.toString();
            completeReportURL = "http://coms-309-040.class.las.iastate.edu:8080/reports/" + Actor.getCurrentId().toString() + "/complete/";
            completeReportURL = completeReportURL + reportId.toString();
            modNotesURL = modNotesURL + reportId.toString();

            Toast.makeText(ViewReportActivity.this, "" + reportId, Toast.LENGTH_LONG).show();

        } catch (Exception e){

        }

        reportedUserText = findViewById(R.id.reportedUserText);
        reportComments = findViewById(R.id.reportcommentsText);



        backButton = findViewById(R.id.backButton90);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewReportActivity.this, ReportsActivity.class).putExtra("completed", "false"));

            }
        });


        viewReportedAccountButton = findViewById(R.id.viewReportedAccountButton);
//        view account page with that id
        viewReportedAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewReportActivity.this, OtherUserActivity.class).putExtra("previousReportPage", reportId.toString()));
            }
        });

        viewReportedPostButton = findViewById(R.id.viewReportedPostButton);
        viewReportedPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                view post page with that id
                startActivity(new Intent(ViewReportActivity.this, viewPostActivity.class).putExtra("postID", reportedPostId).putExtra("previousReportPage", reportId.toString()));
            }
        });

        deleteReport = findViewById(R.id.deleteReportButton);
        deleteReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeReport();
                deleteReport();
            }
        });

        moderatorNotes = findViewById(R.id.modNotes);

        submitModNotes = findViewById(R.id.submitModNotesButton);
        submitModNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modComments(moderatorNotes.getText().toString());
            }
        });

        deleteReportedPostButton = findViewById(R.id.deleteReportedPostButton);
        deleteReportedPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeReport();
                deleteReportedPost();
            }
        });

        disableAccountButton = findViewById(R.id.disableAccountButton);
        disableAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableAccount();
                completeReport();
            }
        });

    }

    /**
     * method that retrieves a report by its ID
     */
    private void getReportByID(Object reportId) {

        reportId = reportId.toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://coms-309-040.class.las.iastate.edu:8080/reports/" + reportId,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Reponse", response.toString());

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = response;
                            Integer ID = jsonObject.getInt("id");
                            String comments = jsonObject.getString("userNotes");
                            JSONObject reported = jsonObject.getJSONObject("reported");
                            String repUser = reported.getString("userName");
                            String repUserEmail = reported.getString("email");
                            String repUserPassword = reported.getString("password");
                            String repUserID = reported.getString("id");
                            disableAccountURL = disableAccountURL + repUserID;
                            Actor.getInstance().setSecondUsername(repUser);
                            Actor.getInstance().setSecondEmail(repUserEmail);
                            Actor.getInstance().setSecondPassword(repUserPassword);
                            Actor.getInstance().setSecondId(repUserID);

                            try {
                                JSONObject post = jsonObject.getJSONObject("post");
                                if (post!=null){
                                    String postID = post.getString("id");
                                    reportedPostId = postID;
                                }
                            } catch (Exception e){
                                viewReportedPostButton.setVisibility(GONE);
                                deleteReportedPostButton.setVisibility(GONE);
                            }
                            usernameOfReported = repUser;
                            reportedUserText.setText(repUser + "");
//                            will say no comments instead of null
                            if (!comments.equals("null")){
                                reportComments.setText(comments);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * method that deletes a report completely
     */
    private void deleteReport() {
        JSONObject report = null;
        try {
            report = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                deleteURL,
                report,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ViewReportActivity.this, "Report deleted", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("deleteReportError", String.valueOf(error));
                    }
                }
        ) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * method that allows moderators to make comments 
     */
    private void modComments(String comments) {
        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("moderatorNotes", comments);
        } catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                modNotesURL,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ViewReportActivity.this, "comments updated", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MODCOMMENT", error.getMessage() + "");
                    }
                }
        ){
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * method to mark a report as completed
     */
    private void completeReport() {
        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("moderator", Actor.getCurrentId());
        } catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                completeReportURL,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ViewReportActivity.this, "report completed", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("COMPLETEREPORT", error.getMessage() + "");
                    }
                }
        ){
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * method to delete a reported post 
     */
    private void deleteReportedPost() {
        JSONObject post = null;
        try {
            post = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                "http://coms-309-040.class.las.iastate.edu:8080/posts/" + reportedPostId,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ViewReportActivity.this, "Post deleted", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("deleteError", String.valueOf(error));
                    }
                }
        ) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * method to disable a reported account
     */
    private void disableAccount() {
        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("enabled", false);
        } catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                disableAccountURL,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ViewReportActivity.this, "account disabled", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DISABLEACCOUNT", error.getMessage() + "");
                    }
                }
        ){
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


}

