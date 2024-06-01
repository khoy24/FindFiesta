package java.frontend.findfiesta;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.accounts.Account;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Class controls the page that holds and displays the account information
 */
public class AccountActivity extends AppCompatActivity implements WebSocketListener1{

    /**
     * image of the actors url
     */
    public static String URL_IMAGE = "http://coms-309-040.class.las.iastate.edu:8080/profile_images/" + Actor.getCurrentId();

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
     * takes user to their settings
     */
    private Button settingsButton;
    /**
     * shows list of users
     */
    private Button userlistButton;

    /**
     * shows users posts
     */
    private Button myPostsButton;

    /**
     * shows list of people user is following
     */
    private Button followingBtn;

    /**
     * greets the user with username and Hello!
     */
    private TextView greeting;

    /**
     * image view for profile picture
     *
     */
    private CircleImageView profilePic;

    /**
     * takes user to edit their profile picture
     */
    private Button editProfilePic;

    /**
     * reports button for admins and mods
     */
    private Button reportsButton;

    /**
     * button for admins to view mod requests
     */
    private Button modRequests;

    /**
     * button that shows all completed reports for admin to view only
     */
    private Button completedReportsButton;

    /**
     * button that normal accounts can use to apply to be a mod
     */
    private Button modApplyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_account);
        greeting = findViewById(R.id.GreetingText);
        greeting.setText("Hello \n" + Actor.getCurrentUsername());
        completedReportsButton = findViewById(R.id.completedReportsButton);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(AccountActivity.this);

        profilePic = findViewById(R.id.profilePicture);
        profilePic.setImageDrawable(Drawable.createFromPath("drawable/account_icon.png"));

        URL_IMAGE = "http://coms-309-040.class.las.iastate.edu:8080/profile_images/" + Actor.getCurrentId();
        /**
         * makes the image request for the profile picture
         */
        makeImageRequest();

        //profile pic
        editProfilePic = findViewById(R.id.editProfilePicButton);
        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, EditProfilePicActivity.class));
            }
        });


        //      Navigation Bar Buttons Begin //
        accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, AccountActivity.class));
            }
        });

        createPostButton = findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, CreatePostActivity.class));
            }
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
            }
        });

        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, MapsActivity.class));
            }
        });

        messagesButton = findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, MessagesActivity.class));
            }
        });

//      Navigation bar buttons end ///

//        UI Page buttons
        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(AccountActivity.this, SettingsActivity.class));
            }
        });
        userlistButton = findViewById(R.id.userListButton);
        userlistButton.setEnabled(true);
        userlistButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(AccountActivity.this, MyFollowersActivity.class));
            }
        });
        myPostsButton = findViewById(R.id.myPostsButton);
        myPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, MyPostsActivity.class));
            }
        });
        followingBtn = findViewById(R.id.followingBtn);
        followingBtn.setEnabled(true);
        followingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, myFollowingActivity.class));
            }
        });

//        reports button for admins and mods
        reportsButton = findViewById(R.id.reportsButton);
        if (Actor.getUserType()==0){
            reportsButton.setVisibility(GONE);
        }
        reportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reports = new Intent(AccountActivity.this, ReportsActivity.class).putExtra("completed", "false");
                startActivity(reports);
            }
        });

        completedReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, ReportsActivity.class).putExtra("completed", "true"));
            }
        });


//        mod requests button for admin
        modRequests = findViewById(R.id.modRequestsButton);
        modRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reports = new Intent(AccountActivity.this, ModRequestActivity.class);
                startActivity(reports);
            }
        });

        modApplyButton = findViewById(R.id.modApplyButton);
        modApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userTypeChange();
            }
        });

        if (Actor.getUserType()!=3){
            modRequests.setVisibility(GONE);
            completedReportsButton.setVisibility(GONE);
        }
        if (Actor.getUserType()==3 ){
            followingBtn.setVisibility(GONE);
            followingBtn.setEnabled(false);
            userlistButton.setVisibility(GONE);
            userlistButton.setEnabled(false);
            modApplyButton.setVisibility(GONE);
            modApplyButton.setEnabled(false);
        } else if (Actor.getUserType()==2 || Actor.getUserType()==4) {
            modApplyButton.setVisibility(GONE);
            modApplyButton.setEnabled(false);
        }
        if (Actor.getUserType()==0){
            modApplyButton.setVisibility(VISIBLE);
            modApplyButton.setEnabled(true);
        }
    }

    /**
     * Making image request
     * */
    private void makeImageRequest() {
        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        profilePic.setImageBitmap(response);

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
                    }
                }
        );
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
//        WebSocketManagerNotifs.getInstanceNotifs().sendMessage("Connected to webSocket");
    }

    @Override
    public void onWebSocketMessage(String message) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(AccountActivity.this, message + "", Toast.LENGTH_SHORT).show();
//            }
//        });
        //GET_SIGN_IN_DURATION //format

    }

    @Override
    public void onWebSocketClose(int statusCode, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception e) {

    }

    private void userTypeChange() {
        JSONObject user  = null;
        try{
            user = new JSONObject();
            user.put("userType", 4);

        } catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                "http://coms-309-040.class.las.iastate.edu:8080/actors/" + Actor.getCurrentId(),
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(AccountActivity.this, "Application submitted", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      Log.d("MODAPPLY", error.getMessage() +"");
                    }
                }
        ){
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
