package java.frontend.findfiesta;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * class that holds the functionality for the my following page 
 */
public class myFollowingActivity extends AppCompatActivity implements WebSocketListener1{

    /**
     * takes user back to previous page
     */
    private Button backBtn;
    /**
     * displays the list of users the current user is following
     */
    private ListView myFollowingList;
    /**
     * displays the list of recommended users to follow
     */
    private ListView recFollowList;
    /**
     * shows recommended followers section label
     */
    private TextView recFollowText;

    /**
     * arraylist to hold followers
     */
    private ArrayList<String> arrayList = new ArrayList<String>();

    /**
     * arraylist to hold recommended followers
     */
    private ArrayList<String> arrayList2 = new ArrayList<String>();

    /**
     * holds followers ids
     */
    private ArrayList<Integer> idArray = new ArrayList<Integer>();
    /**
     * array adapter for following
     */
    ArrayAdapter<String> arrayAdapter;
    /**
     * array adapter for recommended following
     */
    ArrayAdapter<String> arrayAdapter2;

    /**
     * link to follow a user
     */
    private String followURL = "http://coms-309-040.class.las.iastate.edu:8080/" + Actor.getInstance().getCurrentId() + "/follow/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_myfollowing);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(myFollowingActivity.this);

        backBtn = findViewById(R.id.backButton);
        myFollowingList = findViewById(R.id.myFollowingList);
        recFollowList = findViewById(R.id.recFollowList);
        recFollowText = findViewById(R.id.recFollowText);

        myFollowingList.setChoiceMode(ListView.CHOICE_MODE_NONE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(myFollowingActivity.this, AccountActivity.class));
            }
        });


        recFollowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                getRequest(arrayList2.get(position).toString());
                }
        });

        makeJsonArrReq();
        makeJsonArrReq2();
    }

    /**
     * gets the users that the user is following list
     */
    private void makeJsonArrReq() {
//        ArrayList arr = new ArrayList();
        String followersUrl = "http://coms-309-040.class.las.iastate.edu:8080/following/" + Actor.getCurrentId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                followersUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        arrayAdapter = new ArrayAdapter<String>(myFollowingActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        myFollowingList.setAdapter(arrayAdapter);
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
     * gets the recommended followers 
     */
    private void makeJsonArrReq2() {
//        ArrayList arr = new ArrayList();
        String followersUrl = "http://coms-309-040.class.las.iastate.edu:8080/recommended_followers/" + Actor.getCurrentId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                followersUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        arrayAdapter2 = new ArrayAdapter<String>(myFollowingActivity.this, android.R.layout.simple_list_item_1, arrayList2);
                        recFollowList.setAdapter(arrayAdapter2);

                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String username = jsonObject.getString("userName");
                                Integer ID = jsonObject.getInt("id");
                                arrayAdapter2.add(username);
                                idArray.add(ID);
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

    /**
     * gets the actor requested by usernmae
     */
    private void getRequest(String username) {

        String getUrl = "http://coms-309-040.class.las.iastate.edu:8080/actors/username/"+ username;
        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response==null){
                            return;
                        }
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            String email = object.getString("email");
                            String password = object.getString("password");
                            String username = object.getString("userName");
                            String id = object.getString("id");
                            Actor user = Actor.getInstance();
//                            user.setEmail(email);
                            user.setSecondUsername(username);
                            user.setSecondEmail(email);
                            user.setSecondPassword(password);
                            user.setSecondId(id);
                            startActivity(new Intent(myFollowingActivity.this, OtherUserActivity.class).putExtra("prevActivity", "1"));

                        } catch (JSONException e) {
                            Toast.makeText(myFollowingActivity.this, "User does not exist", Toast.LENGTH_LONG).show();
                        }

                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(myFollowingActivity.this, "That didn't work!" + error.toString(), Toast.LENGTH_LONG);

                    }
                }){

        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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
               Toast.makeText(myFollowingActivity.this, message + "", Toast.LENGTH_SHORT).show();
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
