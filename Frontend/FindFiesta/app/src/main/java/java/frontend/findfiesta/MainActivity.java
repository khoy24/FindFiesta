package java.frontend.findfiesta;

import android.Manifest;
import android.app.Activity;
import android.app.LauncherActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * holds the functionality for the main activity (home page) which displays posts
 */
public class MainActivity extends AppCompatActivity implements WebSocketListener1 {

    /**
     * takes user to home page
     */
    private Button homeButton;
    /**
     * takes user to account page
     */
    private Button accountButton;
    /**
     * takes user to account page
     */
    private Button messagesButton;
    /**
     * takes user to map page
     */
    private Button mapButton;
    /**
     * takes user to create post page
     */
    private Button createPostButton;
    /**
     * takes users to search for users
     */
    private Button findUsersButton;
    /**
     * welcomes users to home page
     */
    private TextView welcomeMessage;

    /**
     * temporary postman url for testing
     */
    private String url = "https://1dae91c3-3647-4ee1-9d59-7a26d977acc8.mock.pstmn.io/AccountData/GetData";
    /**
     * posts base url from server
     */
    private String URL = "http://coms-309-040.class.las.iastate.edu:8080/posts/";

    /**
     * holds ID of current user
     */
    private String userID = Actor.getCurrentId();
    /**
     * list of posts base url from server
     */
    private String postURL = "http://coms-309-040.class.las.iastate.edu:8080/posts";
    /**
     * websocket url 
     */
    private String webSocketURL = "ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId();

    /**
     * shows a list of posts
     */
    private ListView postList;

    /**
     * holds ids of the posts
     */
    private ArrayList<String> postID = new ArrayList<>(0);
    /**
     * holds the types of the posts
     */
    ArrayList<String> postType = new ArrayList<>(0);
    /**
     * holds the titles of the posts
     */
    ArrayList<String> postTitle = new ArrayList<>(0);
    /**
     * array adapter for organizing and displaying posts
     */
    ArrayAdapter<String> arrayAdapter;

    /**
     * holds a list of messages
     */
    private ArrayList<String> messagesArrList = Actor.getMessagesArrList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_main);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket(webSocketURL);
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(MainActivity.this);

        welcomeMessage = findViewById(R.id.welcomeMessage);
        if (Actor.getUserType() == 1) {
            welcomeMessage.setText("Hello Guest");
        } else {
            welcomeMessage.setText("Hello " + Actor.getCurrentUsername());
        }

//      Navigation Bar Buttons Begin //
        accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Actor.getUserType() == 1) {
                    startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, AccountActivity.class));
                }
            }
        });

        createPostButton = findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Actor.getUserType() == 1) {
                    startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, CreatePostActivity.class));
                }
            }
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        welcomeMessage = findViewById(R.id.welcomeMessage);
        messagesButton = findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Actor.getUserType() == 1) {
                    startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, MessagesActivity.class));
                }
            }
        });

//      Navigation bar buttons end ///


        findUsersButton = findViewById(R.id.findUsersButton);
        findUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserListActivity.class));
            }
        });

        postList = findViewById(R.id.postList);
        postList.setChoiceMode(ListView.CHOICE_MODE_NONE);
        makeJsonArrReq(postID, postTitle, postType);

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String items = (String) adapterView.getItemAtPosition(i);
                Intent myIntent = new Intent(MainActivity.this, viewPostActivity.class).putExtra("postID", postID.get(i) + "");
                startActivity(myIntent);
            }
        });
    }


    /**
     * requests the array for displaying all of the posts
     */
    private void makeJsonArrReq(ArrayList<String> postID, ArrayList<String> postTitle, ArrayList<String> postType) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                postURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        String TypeString = "";

                        ArrayList<String> arrayList = new ArrayList<String>();
                        arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        postList.setAdapter(arrayAdapter);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Integer ID = jsonObject.getInt("id");
                                String Title = jsonObject.getString("title");
                                Boolean Type = jsonObject.getBoolean("type");
                                if (Type) {
                                    TypeString = "Found: ";
                                } else {
                                    TypeString = "Lost: ";
                                }

                                if (ID != null) {
                                    postID.add(ID.toString());
                                    postTitle.add(Title.toString());
                                    postType.add(TypeString.toString());

                                    arrayAdapter.add("" + postType.get(i) + postTitle.get(i));
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

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
       WebSocketManagerNotifs.getInstanceNotifs().sendMessage("Connected to webSocket");
    }

    @Override
    public void onWebSocketMessage(String message) {
       Log.d("WebSocket", message);

       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(MainActivity.this, message + "", Toast.LENGTH_SHORT).show();
//                sendNotification(MainActivity.this, message);


           }
       });

    }

    @Override
    public void onWebSocketClose(int statusCode, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception e) {

    }
}