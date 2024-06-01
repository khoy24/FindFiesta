package java.frontend.findfiesta;

import android.Manifest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Controls the create post activity page
 */
public class CreatePostActivity extends AppCompatActivity implements WebSocketListener1{

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
     * stores the post title
     */
    private EditText titleEdit;
    /**
     * stores the post location
     */
    private EditText locationEdit;
    /**
     * stores the post notes
     */
    private EditText notesEdit;

    /**
     * a button that discards the post
     */
    private Button discButton;
    /**
     * submit post button
     */
    private Button submitButton;
    /**
     * check box when the item is lost
     */
    private CheckBox lostCB;
    /**
     * check box when the item is found
     */
    private CheckBox foundCB;
    /**
     * integer to keep track of whats checked on the checkbox
     */
    private int isChecked = 0;
    /**
     * base url for posts
     */
    private String url = "http://coms-309-040.class.las.iastate.edu:8080/posts/";
    /**
     * base url for images for the posts
     */
    private String imageURL = "http://coms-309-040.class.las.iastate.edu:8080/images";
    /**
     * stores the current user
     */
    private Actor user = Actor.getInstance();
    /**
     * stores the current user's id
     */
    private String userID = Actor.getCurrentId();
    /**
     * stores the current photo path
     */
    String currentPhotoPath;
    /**
     * stores the image URI
     */
    Uri selectedUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_createpost);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(CreatePostActivity.this);

        //      Navigation Bar Buttons Begin //
        accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, AccountActivity.class));
            }
        });

        createPostButton = findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, CreatePostActivity.class));
            }
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
            }
        });

        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, MapsActivity.class));
            }
        });

        messagesButton = findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, MessagesActivity.class));
            }
        });

//      Navigation bar buttons end ///


        titleEdit = findViewById(R.id.title);
        locationEdit = findViewById(R.id.location);
        notesEdit = findViewById(R.id.note);
        discButton = findViewById(R.id.discard);
        submitButton = findViewById(R.id.submit);
        lostCB = findViewById(R.id.lostCB);
        foundCB = findViewById(R.id.foundCB);

        url += userID;

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (lostCB.isChecked() && foundCB.isChecked()) {
                    Toast.makeText(CreatePostActivity.this, "only select either lost OR found", Toast.LENGTH_SHORT).show();
//                    isChecked = 1;
                } else if (foundCB.isChecked()) {
                    isChecked = 1;
                    postRequest();
                            Intent submitIntent = new Intent(CreatePostActivity.this, PostAddPictureActivity.class).putExtra("postID", "-1");
                            startActivity(submitIntent);
                } else if (lostCB.isChecked()) {
                    isChecked = 0;
                    postRequest();
                        Intent submitIntent = new Intent(CreatePostActivity.this, PostAddPictureActivity.class).putExtra("postID", "-1");
                        startActivity(submitIntent);
                } else {
                    Toast.makeText(CreatePostActivity.this, "you must select either lost OR found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        discButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
            }
        });

    }


    /**
     * posts the post to the server/app
     */
    private void postRequest() {
        JSONObject postBody = null;

        try {
            postBody = new JSONObject();

            postBody.put("title", titleEdit.getText().toString());
            postBody.put("type", isChecked);
            postBody.put("location", locationEdit.getText().toString());
            postBody.put("notes", notesEdit.getText().toString());

        }

        catch (Exception e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postBody,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CreatePostActivity.this, "Post Created", Toast.LENGTH_LONG).show();
                        return;
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "An error occured";
                            Toast.makeText(CreatePostActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreatePostActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ) {

        };
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
               Toast.makeText(CreatePostActivity.this, message + "", Toast.LENGTH_SHORT).show();
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

