package java.frontend.findfiesta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * class that holds functionality for the my followers page
 */
public class MyFollowersActivity extends AppCompatActivity implements WebSocketListener1{

    /**
     * takes user back to previous page
     */
    private Button backButton5;
    /**
     * holds list of the current users followers
     */
    private ListView followersList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_myfollowers);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(MyFollowersActivity.this);

        backButton5 = findViewById(R.id.backButton5);
        backButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyFollowersActivity.this, AccountActivity.class));
            }

        });

        followersList = findViewById(R.id.myFollowersList);
        followersList.setChoiceMode(ListView.CHOICE_MODE_NONE);

        makeJsonArrReq();

    }

    /**
     * gets the followers of the current user and sets them to be displayed
     */
    private void makeJsonArrReq() {
//        ArrayList arr = new ArrayList();
        String followersUrl = "http://coms-309-040.class.las.iastate.edu:8080/followers/" + Actor.getCurrentId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                followersUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        ArrayList<String> arrayList = new ArrayList<String>();
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MyFollowersActivity.this, android.R.layout.simple_list_item_1, arrayList);
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
               Toast.makeText(MyFollowersActivity.this, message + "", Toast.LENGTH_SHORT).show();
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
