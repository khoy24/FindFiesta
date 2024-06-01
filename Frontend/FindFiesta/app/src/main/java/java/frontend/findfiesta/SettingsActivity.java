package java.frontend.findfiesta;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * class that holds the functionality for the settings page
 */
public class SettingsActivity extends AppCompatActivity implements WebSocketListener1{

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
     * button that takes user back to previous page
     */
    private Button backButton6;
    /**
     * button that deletes the current users account
     */
    private Button deleteAccountButton;
    /**
     * button to change current users password
     */
    private Button changePasswordButton;
    /**
     * button to change current users username
     */
    private Button changeUsernameButton;
    /**
     * button to go to the change theme page
     */
    private Button changeThemeButton;

    /**
     * logs the current user out
     */
    private Button logoutButton;
    /**
     * allows user to type in new desired username
     */
    private EditText newUsername;
    /**
     * allows user to type in new desired password
     */
    private EditText newPassword;
    /**
     * holds current id of the actor
     */
    private String currentId = Actor.getInstance().getCurrentId();
    /**
     * base url that gets the current actor
     */
    private String url = "http://coms-309-040.class.las.iastate.edu:8080/actors/" + currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_settings);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(SettingsActivity.this);

        newUsername = findViewById(R.id.changeUsername);
        newPassword = findViewById(R.id.changePassword);

        backButton6 = findViewById(R.id.backButton6);
        backButton6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AccountActivity.class));
            }
        });



//        changing buttons begin
        changeUsernameButton = findViewById(R.id.usernameChangeButton);
        changeUsernameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!newUsername.getText().toString().isEmpty()){
                    Actor.getInstance().setUserName(newUsername.getText().toString());
                    changeUsername();
                }
            }
        });

        changePasswordButton = findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!newPassword.getText().toString().isEmpty()){
                    Actor.getInstance().setPassword(newPassword.getText().toString());
                    //                password criteria
                    boolean passHasNumber = false;
                    String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
                    for (int i = 0; i < numbers.length; i++){
                        String check = numbers[i];
                        if (newPassword.getText().toString().contains(check)){
                            passHasNumber = true;
                        }
                    }
                    if (!passHasNumber){
                        newPassword.setTextColor(Color.RED);
                        Toast.makeText(SettingsActivity.this, "Password must contain at least 1 number", Toast.LENGTH_LONG).show();
                    } else {
                        newPassword.setTextColor(Color.BLACK);
                        Actor.getInstance().setPassword(newPassword.getText().toString());
                        changePassword();
                    }
                }
            }
        });

        deleteAccountButton = findViewById(R.id.deleteAccountButton);
        deleteAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteAccount();
                Toast.makeText(SettingsActivity.this, "account should be deleted now", Toast.LENGTH_LONG).show();

//                logs the user out
                Actor.getInstance().setPassword(null);
                Actor.getInstance().setUserName(null);
                Actor.getInstance().setEmail(null);
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });

        changeThemeButton = findViewById(R.id.changeThemeButton);
        changeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, ChangeThemeActivity.class));
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actor.getInstance().setEmail(null);
                Actor.getInstance().setPassword(null);
                Actor.getInstance().setUserName(null);
                Actor.getInstance().setSecondUsername(null);
                Actor.getInstance().setSecondPassword(null);
                Actor.getInstance().setId(null);
                Actor.getInstance().setSecondId(null);
                Actor.getInstance().setSecondEmail(null);
                Actor.getInstance().setCurrentTheme(0);
                Actor.getInstance().setMessagesArrList(new ArrayList<>());
                Actor.getInstance().setUserType(0);
                WebSocketManager2.getInstance().disconnectWebSocket();
                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });


    }

    /**
     * method that deletes the current users account completely
     */
    private void deleteAccount() {
        // Convert input to JSONObject
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
                        Toast.makeText(SettingsActivity.this, "Account deleted", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SettingsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                        tvResponse.setText(error.getMessage());
                    }
                }
        ){

        };


        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * method that changes the username of the current user
     */
    private void changeUsername() {
        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("userName", newUsername.getText().toString());
            user.put("password", Actor.getCurrentPassword());
            user.put("email", Actor.getCurrentEmail());

        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SettingsActivity.this, "Account username changed", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SettingsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
        ){

        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


    /**
     * method that changes the current users password
     */
    private void changePassword() {
        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("password", Actor.getCurrentPassword());

        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SettingsActivity.this, "Account password changed", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SettingsActivity.this, error.getMessage() + "", Toast.LENGTH_LONG).show();

                    }
                }
        ){
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
       WebSocketManagerNotifs.getInstanceNotifs().sendMessage("Connected to webSocket");
    }

    @Override
    public void onWebSocketMessage(String message) {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(SettingsActivity.this, message + "", Toast.LENGTH_SHORT).show();
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
