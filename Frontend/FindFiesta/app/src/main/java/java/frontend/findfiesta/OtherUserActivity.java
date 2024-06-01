package java.frontend.findfiesta;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * class that holds functionality for the other user page
 */
public class OtherUserActivity extends AppCompatActivity implements WebSocketListener1{

    /**
     * list adapater for the listview
     */
    private ListAdapter adapter;
    /**
     * shows the other users followers
     */
    private ListView followersList;
    /**
     * displays the other users username
     */
    private TextView otherUsernameText;

    /**
     * displays the other users profile photo
     */
    private ImageView otherProfilePhoto;
    /**
     * button that makes the current user follow the other user
     */
    private Button follow;
    /**
     * button that shows the followers of the other user
     */
    private Button otherUsersFollowersButton;
    /**
     * remove the other user as a follower of the current user
     */
    private Button removeFollowerButton;

    /**
     * button that unfollows the other user
     */
    private Button unfollowButton;
    /**
     * takes user back to previous screen
     */
    private Button backButton3;
    /**
     * approves other user to be a mod
     */
    private Button approveModButton;
    /**
     * declines other user to be a mod
     */
    private Button declineModButton;

    /**
     * button to report the user
     */
    private Button reportButton;

    /**
     * base url for profile picture of other user
     */
    private String image_url = "http://coms-309-040.class.las.iastate.edu:8080/profile_images/" + Actor.getSecondId();

    /**
     * url for current user to follow the other user
     */
    private String followUrl = "http://coms-309-040.class.las.iastate.edu:8080/" + Actor.getInstance().getCurrentId() + "/follow/" + Actor.getInstance().getSecondId();

    /**
     * url to shows followers of the other user
     */
    private String followersUrl = "http://coms-309-040.class.las.iastate.edu:8080/followers/" + Actor.getInstance().getSecondId();

    /**
     * image view for profile picture
     *
     */
    private CircleImageView profilePic;

    /**
     * holds String info mod request 
     */
    private String fromModRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_otheruser);

        createChannel(this);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(OtherUserActivity.this);

        otherUsernameText = findViewById(R.id.otherUsernameText);
        otherUsernameText.setText("" + Actor.getInstance().getSecondUsername() + "" );

        approveModButton = findViewById(R.id.approveModButton);
        approveModButton.setVisibility(GONE);
        approveModButton.setEnabled(false);
        declineModButton = findViewById(R.id.declineModButton);
        declineModButton.setVisibility(GONE);
        declineModButton.setEnabled(false);


        String reportPage = getIntent().getStringExtra("previousReportPage");
        try{
            fromModRequest = getIntent().getStringExtra("modrequest");
        } catch (Exception e){

        }

        profilePic = findViewById(R.id.OtherProfilePicture);
        makeImageRequest();

        follow = findViewById(R.id.followUserButton);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Actor.getUserType()==1){
                    startActivity(new Intent(OtherUserActivity.this, CreateAccountActivity.class));
                } else {
                    try {
                        postRequest();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        followersList = findViewById(R.id.followersList);
        followersList.setChoiceMode(ListView.CHOICE_MODE_NONE);
//        makeJsonArrReq();

        otherUsersFollowersButton = findViewById(R.id.otherUsersFollowersButton);
        otherUsersFollowersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeJsonArrReq();
            }
        });
        followersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String followers = (String) adapterView.getItemAtPosition(i);
                adapter.notifyAll();
            }
        });

        unfollowButton = findViewById(R.id.unfollowButton);
        unfollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Actor.getUserType()==1){
                    startActivity(new Intent(OtherUserActivity.this, CreateAccountActivity.class));
                } else {
                    unfollowUser();
                }
            }
        });

        backButton3 = findViewById(R.id.backButton3);
        backButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fromModRequest!=null && fromModRequest.equals("true")){
                    startActivity(new Intent(OtherUserActivity.this, ModRequestActivity.class));
                } else {
                    if (reportPage != null){
                        startActivity(new Intent(OtherUserActivity.this, ViewReportActivity.class).putExtra("reportID", reportPage));
                    } else {
                        if (getIntent().getStringExtra("prevActivity").equals("-1")) {
                            startActivity(new Intent(OtherUserActivity.this, UserListActivity.class));
                        } else {
                            startActivity(new Intent(OtherUserActivity.this, myFollowingActivity.class));
                        }
                    }
                }




            }
        });

        removeFollowerButton = findViewById(R.id.removeFollowerButton);
        removeFollowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Actor.getUserType()==1){
                    startActivity(new Intent(OtherUserActivity.this, CreateAccountActivity.class));
                } else {
                    removeFollower();
                }
            }
        });

        reportButton = findViewById(R.id.reportUserButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                report the user
                try {
                    reportActor();
                    Intent reportComment = new Intent(OtherUserActivity.this, ReportCommentActivity.class);
                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                }
            }
        });

        if (Actor.getUserType()==3 && Actor.getSecondUserType()==4){
            approveModButton.setVisibility(VISIBLE);
            approveModButton.setEnabled(true);
            declineModButton.setVisibility(VISIBLE);
            declineModButton.setEnabled(true);
            reportButton.setVisibility(GONE);
            reportButton.setEnabled(false);
        }

        approveModButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveMod();
            }
        });

        declineModButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineMod();
            }
        });


    }

    /**
     * gets the other user object
     */
    private void postRequest() throws JSONException {


//        // Convert input to JSONObject
        JSONObject postBody = null;
        postBody = new JSONObject();
        postBody.put("userName", Actor.getInstance().getSecondUsername());
        postBody.put("password", Actor.getInstance().getSecondPassword());
        postBody.put("email", Actor.getInstance().getSecondEmail());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                followUrl,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(OtherUserActivity.this, "User " + Actor.getInstance().getSecondUsername() + " followed by " + Actor.getInstance().getCurrentUsername(), Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };


        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


    /**
     * gets a list of the other users followers
     */
    private void makeJsonArrReq() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                followersUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        ArrayList<String> arrayList = new ArrayList<String>();
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(OtherUserActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        followersList.setAdapter(arrayAdapter);
//
                        for (int i = 0; i < response.length(); i++) {
//
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String username = jsonObject.getString("userName");
                                arrayAdapter.add(username);
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
//                        Toast.makeText(OtherUserActivity.this,"" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);

    }


    /**
     * current user unfollows the other user
     */
    private void unfollowUser(){

        String unfollowUrl = "http://coms-309-040.class.las.iastate.edu:8080/" + Actor.getCurrentId() + "/unfollow/" + Actor.getSecondId();

        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("userName", Actor.getCurrentUsername());
            user.put("password", Actor.getCurrentPassword());
            user.put("email", Actor.getCurrentEmail());


        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                unfollowUrl,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(OtherUserActivity.this, error.getMessage().toString() + "", Toast.LENGTH_LONG).show();

                    }
                }
        ){
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }


    /**
     * current user removes the other user as a follower
     */
    private void removeFollower() {
        // Convert input to JSONObject
        String url = "http://coms-309-040.class.las.iastate.edu:8080/remove/" + Actor.getSecondId() + "/from/" + Actor.getCurrentId();
        JSONObject user  = null;
        try{

            user = new JSONObject();
            user.put("userName", Actor.getCurrentUsername());
            user.put("password", Actor.getCurrentPassword());
            user.put("email", Actor.getCurrentEmail());
            user.put("id", Actor.getCurrentId());


        } catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        tvResponse.setText(response.toString());
                        Toast.makeText(OtherUserActivity.this, "Follower Removed", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
        };


        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * gets profile picture of the other user
     */
    private void makeImageRequest() {
        ImageRequest imageRequest = new ImageRequest(
                image_url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        if (response!=null){
                            profilePic.setImageBitmap(response);
                        }
//                        profilePic.setImageBitmap(response);
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                        profilePic.setImageDrawable(Drawable.createFromPath("drawable/account_icon.png"));
                    }
                }
        );
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }

    /**
     * current user reports the other user
     */
    private void reportActor() throws JSONException {
        String reportActorURL =  "http://coms-309-040.class.las.iastate.edu:8080/" + Actor.getCurrentId() + "/reports_actor/" + Actor.getSecondId();
        // Convert input to JSONObject
        JSONObject postBody = null;
        postBody = new JSONObject();
        postBody.put("userName", Actor.getInstance().getSecondUsername());
        postBody.put("password", Actor.getInstance().getSecondPassword());
        postBody.put("email", Actor.getInstance().getSecondEmail());
        postBody.put("id", Actor.getSecondId());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                reportActorURL,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(OtherUserActivity.this, "User " + Actor.getSecondUsername() + " reported by " + Actor.getInstance().getCurrentUsername(), Toast.LENGTH_LONG).show();
                        getLastReport();
                        Intent commentintent = new Intent(OtherUserActivity.this, ReportCommentActivity.class);
                        startActivity(commentintent);
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(OtherUserActivity.this, error.getMessage() + "User " + Actor.getSecondUsername() + " reported by " + Actor.getInstance().getCurrentUsername(), Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * creates a notification channel
     */
    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "CHANNEL_ID", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("this is my channel");

                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * holds channel id
     */
    public static final String CHANNEL_ID = "CHANNEL_ID";

    /**
     * send notification to the current user
     */
    public static void sendNotification(Context context, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Find Fiesta")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManagerCompat.notify(0, builder.build());
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
//                    Manifest.permission.POST_NOTIFICATIONS}, 44);
                    Manifest.permission.POST_NOTIFICATIONS}, 44);
            notificationManagerCompat.notify(0, builder.build());
//            sendNotification(context, message);
        }
        notificationManagerCompat.notify(0, builder.build());
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
//        WebSocketManagerNotifs.getInstanceNotifs().sendMessage("Connected to webSocket");
    }

    @Override
    public void onWebSocketMessage(String message) {
////        Log.d("WebSocket", message);
//
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendNotification(OtherUserActivity.this, message);
//                Toast.makeText(OtherUserActivity.this, message + "", Toast.LENGTH_SHORT).show();
            }
        });
        //GET_SIGN_IN_DURATION //format

    }

    @Override
    public void onWebSocketClose(int statusCode, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception e) {

    }

    /**
     * gets the last report of user for upper level users
     */
    private void getLastReport() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-040.class.las.iastate.edu:8080/reports",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = response.getJSONObject(response.length()-1);
                            int ID = jsonObject.getInt("id");
                            Actor.getInstance().setReportId(ID);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    /**
     * approve the users request to be a moderator
     */
    private void approveMod() {
        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("userType", 2);

        } catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                "http://coms-309-040.class.las.iastate.edu:8080/actors/" + Actor.getSecondId(),
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(OtherUserActivity.this, "Application approved", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MODAPPROVE", error.getMessage() +"");
                    }
                }
        ){
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * declines the other users request to be a moderator
     */
    private void declineMod() {
        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("userType", 0);

        } catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                "http://coms-309-040.class.las.iastate.edu:8080/actors/" + Actor.getSecondId(),
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(OtherUserActivity.this, "Application Declined", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MODDECLINE", error.getMessage() +"");
                    }
                }
        ){
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

}

