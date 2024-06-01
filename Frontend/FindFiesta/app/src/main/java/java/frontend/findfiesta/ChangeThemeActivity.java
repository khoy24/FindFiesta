package java.frontend.findfiesta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class manages the page with the option to change user theme
 */
public class ChangeThemeActivity extends AppCompatActivity implements WebSocketListener1{


    /**
     * goes back to the previous page
     */
    private Button backButton;
    /**
     * button to change theme to main
     */
    private Button mainThemeButton;
    /**
     * button to change theme to green
     */
    private Button greenThemeButton;
    /**
     * button to change theme to pink
     */
    private Button pinkThemeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_changetheme);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(ChangeThemeActivity.this);

//       find buttons by id
         backButton = findViewById(R.id.backButton11);
         mainThemeButton = findViewById(R.id.mainThemeButton);
         greenThemeButton = findViewById(R.id.greenThemeButton);
         pinkThemeButton = findViewById(R.id.pinkThemeButton);

         backButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(ChangeThemeActivity.this, SettingsActivity.class));
             }
         });
         // when clicked changes the theme back to main 
         mainThemeButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Actor.getInstance().setCurrentTheme(0);
                 changeTheme(0);
                 finish();
                 startActivity(getIntent());
             }
         });
        //  when clicked the theme changes to green
         greenThemeButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Actor.getInstance().setCurrentTheme(1);
                 changeTheme(1);
                 finish();
                 startActivity(getIntent());
             }
         });
         // when clicked the theme changes to pink
         pinkThemeButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Actor.getInstance().setCurrentTheme(2);
                 changeTheme(2);
                 finish();
                 startActivity(getIntent());
             }
         });



    }

    /**
     * method that changes the UI theme of the entire application
     */
    private void changeTheme(int themeId) {
        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("themeId", themeId);

//            this works with new username
//            Toast.makeText(ChangeThemeActivity.this, Actor.getCurrentTheme() + "" , Toast.LENGTH_LONG).show();

        } catch (Exception e){
            e.printStackTrace();
        }
        String url = "http://coms-309-040.class.las.iastate.edu:8080/actors/" + Actor.getCurrentId();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ChangeThemeActivity.this, "Account theme changed", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangeThemeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

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
       Log.d("WebSocket", message);

       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(ChangeThemeActivity.this, message + "", Toast.LENGTH_SHORT).show();
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
