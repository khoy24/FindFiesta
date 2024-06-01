package java.frontend.findfiesta;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * class that holds functionality for the completed reports page
 */
public class ViewCompletedReportActivity extends AppCompatActivity {

    /**
     * url to delete a report
     */
    private String deleteURL = "http://coms-309-040.class.las.iastate.edu:8080/reports/delete/";

    /**
     * displays username of reported user
     */
    private TextView reportedUserText;

    /**
     * holds reported post id as a string (easier for url)
     */
    private String reportedPostId;

    /**
     * displays report comments
     */
    private TextView reportComments;

    /**
     * displays moderator comments
     */
    private TextView modComments;
    /**
     * displays who reviewed the report
     */
    private TextView reviewedByText;
    /**
     * button to delete the report
     */
    private Button deleteCompletedReportButton;
    /**
     * takes user back to previous page
     */
    private Button backButton;
    /**
     * holds report id
     */
    private Object reportId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_viewcompletedreport);
        Bundle extras = getIntent().getExtras();
        try {
            reportId = extras.get("reportID");
            getReportByID(reportId);
            deleteURL = deleteURL + reportId.toString();
        } catch (Exception e){

        }

        reportedUserText = findViewById(R.id.reportedUserText2);
        reportComments = findViewById(R.id.reportcommentsText2);
        modComments = findViewById(R.id.moderatorComments);
        reviewedByText = findViewById(R.id.reviewedByText);

        deleteCompletedReportButton = findViewById(R.id.deleteCompletedReportButton);
        deleteCompletedReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteReport();
            }
        });

        backButton = findViewById(R.id.backButton101);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewCompletedReportActivity.this, ReportsActivity.class).putExtra("completed", "true"));
            }
        });


    }

    /**
     * retrieves report from the server by ID
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
                            String modcomments = jsonObject.getString("moderatorNotes");
                            JSONObject reported = jsonObject.getJSONObject("reported");
                            JSONObject moderator = jsonObject.getJSONObject("moderator");
                            String moderatorUsername = moderator.getString("userName");
                            String repUser = reported.getString("userName");
                            String repUserEmail = reported.getString("email");
                            String repUserPassword = reported.getString("password");
                            String repUserID = reported.getString("id");
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
                            }
                            reportedUserText.setText(repUser + "");
                            reviewedByText.setText("Reviewed by: " + moderatorUsername);

//                            will say no comments instead of null
                            if (!comments.equals("null")){
                                reportComments.setText(comments);
                            }
                            if (!modcomments.equals("null")){
                                modComments.setText(modcomments);
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
     * method that deletes the report completely
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
                        Toast.makeText(ViewCompletedReportActivity.this, "Report deleted", Toast.LENGTH_LONG).show();
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
}
