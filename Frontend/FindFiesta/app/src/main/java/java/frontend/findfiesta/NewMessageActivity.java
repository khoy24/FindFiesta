package java.frontend.findfiesta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.java_websocket.WebSocketListener;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * class that holds the functionality for the new messages activity
 */
public class NewMessageActivity extends AppCompatActivity implements WebSocketListener1 {

    /**
     * when clicked searches for user
     */
    private Button findUserButton;
    /**
     * takes user back to previous screen
     */
    private Button backButton20;
    /**
     * text space to type in username to search
     */
    private EditText searchUserText;
    /**
     * holds the chat ID as a string
     */
    private String chatID;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_newmessage);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(NewMessageActivity.this);

        findUserButton = findViewById(R.id.findUserButton2);
        findUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUserText = findViewById(R.id.userSearch);
                getRequest(searchUserText.getText().toString());

            }
        });

        backButton20 = findViewById(R.id.backButton20);
        backButton20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewMessageActivity.this, MessagesActivity.class));
            }
        });

    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            //    Toast.makeText(NewMessageActivity.this, message + "", Toast.LENGTH_SHORT).show();
            }
        });

        chatID = message;
        //you'll receive the message from searching and it returns the chatroom number, so join that chatroom
        WebSocketManager.getInstance().sendMessage("JOIN_CHAT_ROOM:" + message);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception e) {

    }

    /**
     * Checks if a user exists under the given username
     * @param username
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
                            user.setSecondUsername(username);
                            user.setSecondEmail(email);
                            user.setSecondPassword(password);
                            user.setSecondId(id);

                            WebSocketManager.getInstance().sendMessage("CREATE_NEW_CHAT_ROOM:" + Actor.getCurrentId() + "-" + Actor.getSecondId());
                            Toast.makeText(NewMessageActivity.this, "New chat created!", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            Toast.makeText(NewMessageActivity.this, "User does not exist", Toast.LENGTH_LONG).show();
                        }

                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewMessageActivity.this, "That didn't work!" + error.toString(), Toast.LENGTH_LONG);

                    }
                }){

        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }



}
