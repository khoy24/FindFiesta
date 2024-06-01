package java.frontend.findfiesta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * class that holds functionality for the user list page
 */
public class UserListActivity extends AppCompatActivity implements WebSocketListener1{

    /**
     * sends user back to previous page
     */
    private Button userListBackButton;
    /**
     * button that searches for user
     */
    private Button findUserButton;
    /**
     * allows user to type in a username to be searched for
     */
    private EditText searchUserText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_userlist);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(UserListActivity.this);

        userListBackButton = findViewById(R.id.userListBackButton);
        userListBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserListActivity.this, MainActivity.class));
            }
        });

        findUserButton = findViewById(R.id.findUserButton);
        findUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUserText = findViewById(R.id.searchUserText);
                getRequest(searchUserText.getText().toString());

            }
        });
    }

    /**
     * searches to see if a user with that username exists
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
                            Integer userType = object.getInt("userType");

                            Actor user = Actor.getInstance();
                            user.setSecondUsername(username);
                            user.setSecondEmail(email);
                            user.setSecondPassword(password);
                            user.setSecondId(id);
                            user.setSecondUserType(userType);

                            startActivity(new Intent(UserListActivity.this, OtherUserActivity.class).putExtra("prevActivity", "-1"));

                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
                            Toast.makeText(UserListActivity.this, "User does not exist", Toast.LENGTH_LONG).show();
                        }

                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserListActivity.this, "That didn't work!" + error.toString(), Toast.LENGTH_LONG);

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
               Toast.makeText(UserListActivity.this, message + "", Toast.LENGTH_SHORT).show();
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

