package java.frontend.findfiesta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
 * class that holds functionality for the my posts page
 */
public class MyPostsActivity extends AppCompatActivity implements WebSocketListener1{

    /**
     * gets posts of current actor
     */
    private String postURL = "http://coms-309-040.class.las.iastate.edu:8080/posts/actor/";
    /**
     * holds current actor id
     */
    private String userID = Actor.getCurrentId();
    /**
     * takes user back to previous screen
     */
    private Button backButton4;
    /**
     * listview that displays the users posts
     */
    private ListView postList;
    /**
     * array of post ids
     */
    private ArrayList<String> postID = new ArrayList<>(0);
    /**
     * array of post types
     */
    private ArrayList<String> postType = new ArrayList<>(0);
    /**
     * array of post titles
     */
    private ArrayList<String> postTitle = new ArrayList<>(0);

    /**
     * array adapter object for the arrays
     */
    ArrayAdapter<String> arrayAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_myposts);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(MyPostsActivity.this);

        backButton4 = findViewById(R.id.backButton4);

        postList = findViewById(R.id.postList);
        postList.setChoiceMode(ListView.CHOICE_MODE_NONE);
        makeJsonArrReq(postID, postTitle, postType);

        backButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyPostsActivity.this, AccountActivity.class));
            }

        });

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String items = (String) adapterView.getItemAtPosition(i);
                Intent myIntent = new Intent(MyPostsActivity.this, viewPostActivity.class).putExtra("postID", postID.get(i));
                startActivity(myIntent);
            }
        });

    }

    /**
     * gets the current users posts 
     */
    private void makeJsonArrReq(ArrayList<String> postID, ArrayList<String> postTitle, ArrayList<String> postType) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                postURL + userID,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        String TypeString = "";

                        ArrayList<String> arrayList = new ArrayList<String>();
                        arrayAdapter = new ArrayAdapter<String>(MyPostsActivity.this, android.R.layout.simple_list_item_1, arrayList);
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
               Toast.makeText(MyPostsActivity.this, message + "", Toast.LENGTH_SHORT).show();
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
