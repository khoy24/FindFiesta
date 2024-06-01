package java.frontend.findfiesta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * class that holds the report comment page functionality
 */
public class ReportCommentActivity extends AppCompatActivity {

    /**
     * takes user back to home page to view posts
     */
    private Button backButton70;

    /**
     *
     * edit text for inputting comments
     */
    private EditText reportComments;

    /**
     * button to send the comments
     */
    private Button sendComments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_reportcomment);

        backButton70 = findViewById(R.id.backButton70);
        backButton70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(ReportCommentActivity.this, MainActivity.class);
                startActivity(main);
            }
        });

        reportComments = findViewById(R.id.reportComments);

        sendComments = findViewById(R.id.sendCommentButton);
        sendComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putComments(reportComments.getText().toString());
                reportComments.getText().clear();
            }
        });


    }

    /**
     * method that allows user to edit report comments 
     */
    private void putComments(String comments) {
        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("userNotes", comments);

//            this works with new username
//            Toast.makeText(ChangeThemeActivity.this, Actor.getCurrentTheme() + "" , Toast.LENGTH_LONG).show();

        } catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(ReportCommentActivity.this, Actor.getReportId() + "", Toast.LENGTH_LONG).show();
        String url = "http://coms-309-040.class.las.iastate.edu:8080/reports/user_notes/" + String.valueOf(Actor.getReportId());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ReportCommentActivity.this, "Thank you for reporting!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReportCommentActivity.this, error.getMessage() + "", Toast.LENGTH_LONG).show();
                        Log.d("PUTCOMMENT", error.getMessage() + "");
                    }
                }
        ){

        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }





}

