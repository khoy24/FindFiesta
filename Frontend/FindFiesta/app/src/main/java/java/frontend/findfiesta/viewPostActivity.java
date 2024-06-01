package java.frontend.findfiesta;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * class that holds functionality for view post page
 */
public class viewPostActivity  extends AppCompatActivity implements WebSocketListener1{

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
     * displays whether the item was marked as lost or found
     */
    private TextView displayLostOrFound;
    /**
     * displays the title of the post
     */
    private TextView titleDisplay;
    /**
     * displays the location of the post
     */
    private TextView locationDisplay;
    /**
     * displays any notes about the post
     */
    private TextView notesDisplay;
    /**
     * button to change post details if its the current users post
     */
    private Button changeDtlBtn;
    /**
     * button to delete the post if its the current users post
     */
    private Button deletePostBtn;
    /**
     * button to confirm changes 
     */
    private Button confirmChanDtlBtn;
    /**
     * button to cancel changes
     */
    private Button cancelChanDtlBtn;
    /**
     * allows user to input what they want to change the title to
     */
    private EditText changeTitle;
     /**
     * allows user to input what they want to change the location to
     */
    private EditText changeLocation;
     /**
     * allows user to input what they want to change the notes to
     */
    private EditText changeNotes;

    /**
     * takes user back to previous page
     */
    private Button backButton;

    /**
     * button to comment on post
     */
    private Button commentBtn;
    /**
     * displays comments as a list
     */
    private ListView commentList;
    /**
     * button to confirm comment 
     */
    private Button confirmCmtBtn;
    /**
     * button to cancel comment
     */
    private Button cancelCmtBtn;
    /**
     * allows user to input text to change the comment to
     */
    private EditText commentText;
    /**
     * adapter for all arrays
     */
    ArrayAdapter<String> arrayAdapter;
    /**
     * arraylist that holds post content
     */
    private ArrayList<String> contentList = new ArrayList<>(0);
    /**
     * arraylist holding the comment ids
     */
    private ArrayList<String> commentID = new ArrayList<>(0);

    /**
     * base url for posts
     */
    private String URL = "http://coms-309-040.class.las.iastate.edu:8080/posts"; //+ getIntent().getStringExtra("postID");
    //    private String postID = getIntent().getStringExtra("postID");
    /**
     * base url for post images 
     */
    private String ImageURL = "http://coms-309-040.class.las.iastate.edu:8080/post_images/";

    /**
     * displays post images
     */
    private ImageView postImage;
    /**
     * boolean to evaluate whether or not its edited
     */
    private boolean isEdit;
    /**
     * holds second id of post as String
     */
    private String postID2;
    /**
     * boolean that stores whether or not image is being removed
     */
    Boolean removeImageBool = false;
    /**
     * button to remove image
     */
    private Button removeImage;
    /**
     * boolean thats true if the current post was posted by the current user
     */
    private boolean isUsersPost;

    /**
     * button for reporting other users posts
     */
    private Button reportPostButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_post_view);

        createChannel(this);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(viewPostActivity.this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        String reportPage = getIntent().getStringExtra("previousReportPage");
        String postID = getIntent().getStringExtra("postID");
        postID2 = postID;
        //nav bar begins
        accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewPostActivity.this, AccountActivity.class));
//                titleDisplay.setText(postID);
            }
        });

        createPostButton = findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewPostActivity.this, CreatePostActivity.class));
            }
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewPostActivity.this, MainActivity.class));
            }
        });

        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewPostActivity.this, MapsActivity.class));
            }
        });

        messagesButton = findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewPostActivity.this, MessagesActivity.class));
            }
        });
        //nav bar ends
        backButton = findViewById(R.id.backButton100);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reportPage!=null){
                    startActivity(new Intent(viewPostActivity.this, ViewReportActivity.class).putExtra("reportID", reportPage));
                } else {
                    startActivity(new Intent(viewPostActivity.this, MainActivity.class));
                }
            }
        });

        displayLostOrFound = findViewById(R.id.displayLostOrFound);
        titleDisplay = findViewById(R.id.titleDisplay);
        locationDisplay = findViewById(R.id.locationDisplay);
        notesDisplay = findViewById(R.id.notesDisplay);

        changeDtlBtn = findViewById(R.id.changeDetailsBtn);
        deletePostBtn = findViewById(R.id.deletePostBtn);
        confirmChanDtlBtn = findViewById(R.id.confirmChanDtlBtn);

//        confirmDelBtn = findViewById(R.id.confirmDelBtn);
        cancelChanDtlBtn = findViewById(R.id.cancelChanDtlBtn);
//        cancelDelBtn = findViewById(R.id.cancelDelBtn);

        changeTitle = findViewById(R.id.changeTitle);
        changeLocation = findViewById(R.id.changeLocation);
        changeNotes = findViewById(R.id.changeNotes);

        commentBtn = findViewById(R.id.commentBtn);
        commentList = findViewById(R.id.commentList);

        confirmCmtBtn = findViewById(R.id.confirmCmtBtn);
        cancelCmtBtn = findViewById(R.id.cancelCmtBtn);
        commentText = findViewById(R.id.commentText);

        postImage = findViewById(R.id.postImage);

        removeImage = findViewById(R.id.removeImage);

        removeImage.setEnabled(false);
        removeImage.setVisibility(View.GONE);

        reportPostButton = findViewById(R.id.reportPostButton);
//        reportPostButton.setVisibility(View.GONE);
        reportPostButton.setEnabled(false);
        reportPostButton.setVisibility(View.GONE);

        confirmChanDtlBtn.setEnabled(false);
        cancelChanDtlBtn.setEnabled(false);
        changeTitle.setEnabled(false);
        changeLocation.setEnabled(false);
        changeNotes.setEnabled(false);
        confirmCmtBtn.setEnabled(false);
        cancelCmtBtn.setEnabled(false);
        commentText.setEnabled(false);
        postImage.setEnabled(false);
        reportPostButton.setEnabled(false);
        confirmChanDtlBtn.setVisibility(INVISIBLE);
        cancelChanDtlBtn.setVisibility(INVISIBLE);
        changeTitle.setVisibility(INVISIBLE);
        changeLocation.setVisibility(INVISIBLE);
        changeNotes.setVisibility(INVISIBLE);
        confirmCmtBtn.setVisibility(INVISIBLE);
        cancelCmtBtn.setVisibility(INVISIBLE);
        commentText.setVisibility(INVISIBLE);
        reportPostButton.setVisibility(View.GONE);

        URL += "/";
        URL += postID;
        ImageURL += postID;
        notesDisplay.setText(URL);

        makeJsonObjReq();
        makeJsonArrReq();

        makeJsonArrReq(contentList, postID);

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePostBtn.setVisibility(View.GONE);
                deletePostBtn.setEnabled(false);
                changeDtlBtn.setVisibility(View.GONE);
                changeDtlBtn.setEnabled(false);
                confirmCmtBtn.setVisibility(VISIBLE);
                confirmCmtBtn.setEnabled(true);
                cancelCmtBtn.setVisibility(VISIBLE);
                cancelCmtBtn.setEnabled(true);
                commentText.setVisibility(VISIBLE);
                commentText.setEnabled(true);
                commentBtn.setVisibility(View.GONE);
                commentBtn.setEnabled(false);
                reportPostButton.setEnabled(false);
                titleDisplay.setVisibility(View.GONE);
                notesDisplay.setVisibility(View.GONE);
                locationDisplay.setVisibility(View.GONE);
                postImage.setVisibility(View.GONE);
                displayLostOrFound.setVisibility(View.GONE);
                reportPostButton.setVisibility(View.GONE);
            }
        });



        confirmCmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentPost(postID);
                startActivity(new Intent(viewPostActivity.this, viewPostActivity.class).putExtra("postID", postID));
            }
        });

        cancelCmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCmtBtn.setEnabled(false);
                confirmCmtBtn.setVisibility(View.GONE);
                cancelCmtBtn.setEnabled(false);
                cancelCmtBtn.setVisibility(View.GONE);
                deletePostBtn.setVisibility(VISIBLE);
                deletePostBtn.setEnabled(true);
                changeDtlBtn.setVisibility(VISIBLE);
                changeDtlBtn.setEnabled(true);
                commentText.setEnabled(false);
                commentText.setVisibility(INVISIBLE);
                commentBtn.setEnabled(true);
                commentBtn.setVisibility(VISIBLE);
                titleDisplay.setVisibility(VISIBLE);
                locationDisplay.setVisibility(VISIBLE);
                notesDisplay.setVisibility(VISIBLE);
                postImage.setVisibility(VISIBLE);
                reportPostButton.setEnabled(false);
                reportPostButton.setVisibility(View.GONE);
            }
        });


        changeDtlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDtlBtn.setVisibility(View.GONE);
                deletePostBtn.setVisibility(View.GONE);
                confirmChanDtlBtn.setVisibility(VISIBLE);
                cancelChanDtlBtn.setVisibility(VISIBLE);
                confirmChanDtlBtn.setEnabled(true);
                cancelChanDtlBtn.setEnabled(true);
                changeTitle.setVisibility(VISIBLE);
                changeTitle.setEnabled(false);
                changeLocation.setVisibility(VISIBLE);
                changeNotes.setVisibility(VISIBLE);
                titleDisplay.setVisibility(View.GONE);
                locationDisplay.setVisibility(View.GONE);
                notesDisplay.setVisibility(View.GONE);
                changeTitle.setEnabled(true);
                changeLocation.setEnabled(true);
                changeNotes.setEnabled(true);
                isEdit = true;
                commentBtn.setVisibility(View.GONE);
                commentBtn.setEnabled(false);
                postImage.setEnabled(true);
                removeImage.setVisibility(VISIBLE);
                removeImage.setEnabled(true);
                reportPostButton.setEnabled(false);
                reportPostButton.setVisibility(View.GONE);

                changeTitle.setText("");
                changeLocation.setText("");
                changeNotes.setText("");
            }
        });

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTitle.setText("working");
                Intent changePicIntent = new Intent(viewPostActivity.this, PostAddPictureActivity.class).putExtra("postID", postID);
                startActivity(changePicIntent);



            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!removeImageBool) {
                    removeImageBool = true;
                    removeImage.setText("Removing");
                } else {
                    removeImageBool = false;
                    removeImage.setText("remove image");
                }
            }
        });

        confirmChanDtlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    makeJsonObjUpdate(changeTitle.getText().toString(), changeLocation.getText().toString(), changeNotes.getText().toString());
                    makeJsonObjReq();
                    if (removeImageBool) {
                        deletePostImage();
                    }
                    startActivity(new Intent(viewPostActivity.this, viewPostActivity.class).putExtra("postID", postID));
                } else {
                    if (postImage != null) {
                        deletePostImage();
                    }
                    deletePost();
                    startActivity(new Intent(viewPostActivity.this, MainActivity.class));
                }
            }
        });

        cancelChanDtlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    changeNotes.setEnabled(false);
                    changeLocation.setEnabled(false);
                    changeTitle.setEnabled(false);
                    changeNotes.setVisibility(INVISIBLE);
                    changeLocation.setVisibility(INVISIBLE);
                    changeTitle.setVisibility(INVISIBLE);
                    titleDisplay.setVisibility(VISIBLE);
                    locationDisplay.setVisibility(VISIBLE);
                    notesDisplay.setVisibility(VISIBLE);
                    changeDtlBtn.setVisibility(VISIBLE);
                    commentBtn.setVisibility(VISIBLE);
                    commentBtn.setEnabled(true);
                    changeDtlBtn.setEnabled(true);
                    deletePostBtn.setVisibility(VISIBLE);
                    deletePostBtn.setEnabled(true);
                    confirmChanDtlBtn.setVisibility(View.GONE);
                    confirmChanDtlBtn.setEnabled(false);
                    cancelChanDtlBtn.setVisibility(View.GONE);
                    cancelChanDtlBtn.setEnabled(false);
                    isEdit = false;
                    postImage.setEnabled(false);
                    reportPostButton.setEnabled(false);
                    reportPostButton.setVisibility(View.GONE);
                } else {
                    deletePostBtn.setEnabled(true);
                    deletePostBtn.setVisibility(VISIBLE);
                    changeDtlBtn.setEnabled(true);
                    changeDtlBtn.setVisibility(VISIBLE);
                    commentBtn.setEnabled(true);
                    commentBtn.setVisibility(VISIBLE);
                    cancelChanDtlBtn.setEnabled(false);
                    cancelChanDtlBtn.setVisibility(View.GONE);
                    confirmChanDtlBtn.setEnabled(false);
                    confirmChanDtlBtn.setVisibility(View.GONE);
                    reportPostButton.setVisibility(View.GONE);
                }
            }
        });

        deletePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDtlBtn.setVisibility(View.GONE);
                changeDtlBtn.setEnabled(false);
                deletePostBtn.setVisibility(View.GONE);
                deletePostBtn.setEnabled(false);
                confirmChanDtlBtn.setVisibility(VISIBLE);
                confirmChanDtlBtn.setEnabled(true);
                cancelChanDtlBtn.setVisibility(VISIBLE);
                cancelChanDtlBtn.setEnabled(true);
                commentBtn.setVisibility(View.GONE);
                commentBtn.setEnabled(false);
                reportPostButton.setEnabled(false);
                reportPostButton.setVisibility(View.GONE);
            }
        });

        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String items = (String) adapterView.getItemAtPosition(i);
                Intent myIntent = new Intent(viewPostActivity.this, viewCommentActivity.class).putExtra("commentID", commentID.get(i)).putExtra("postID", postID);
                startActivity(myIntent);
            }
        });
        makeImageRequest();

        if (!isUsersPost) {
            changeDtlBtn.setVisibility(View.GONE);
            changeDtlBtn.setEnabled(false);
            deletePostBtn.setVisibility(View.GONE);
            deletePostBtn.setEnabled(false);
            if (Actor.getUserType()==0 || Actor.getUserType()==4){
                reportPostButton.setVisibility(View.VISIBLE);
                reportPostButton.setEnabled(true);
            }
        }
//        so that admins can delete all posts
        if (Actor.getUserType()==3){
            deletePostBtn.setVisibility(VISIBLE);
            deletePostBtn.setEnabled(true);
        }

        reportPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    reportPost(postID);
                    Intent reportComments = new Intent(viewPostActivity.this, ReportCommentActivity.class);
                    startActivity(reportComments);
                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * creates a notification channel 
     */
    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "CHANNEL_ID", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("this is my channel");

                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * holds channel id as String
     */
    public static final String CHANNEL_ID = "CHANNEL_ID";

    /**
     * method that sends the notification to the device
     */
    public static void sendNotification(Context context, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Find Fiesta")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManagerCompat.notify(0, builder.build());
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
//                    Manifest.permission.POST_NOTIFICATIONS}, 44);
                    Manifest.permission.POST_NOTIFICATIONS}, 44);
            notificationManagerCompat.notify(0, builder.build());
//            sendNotification(context, message);
        }
        notificationManagerCompat.notify(0, builder.build());
    }

    /**
     * gets the requested post from the server
     */
    private void makeJsonObjReq() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            String title = response.getString("title");
                            String location = response.getString("location");
                            String notes = response.getString("notes");
                            Boolean type = response.getBoolean("type");

                            titleDisplay.setText(title);
                            locationDisplay.setText("Location: " + location);
                            notesDisplay.setText("Additional notes: " + notes);

                            if (type) {
                                displayLostOrFound.setText("Found: ");
                            } else {
                                displayLostOrFound.setText("Lost: ");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * changes the post as requested
     */
    private void makeJsonObjUpdate(String title, String location, String notes) {
        JSONObject post = null;
        try {
            post = new JSONObject();
            if (!title.isEmpty()) {
                post.put("title", title);
            }
            if (!location.isEmpty()) {
                post.put("location", location);
            }
            if (!notes.isEmpty()) {
                post.put("notes", notes);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(viewPostActivity.this, "Post updated", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "An error occured";
                            Toast.makeText(viewPostActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(viewPostActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * method that deletes the image from the post
     */
    private void deletePostImage() {
        JSONObject post = null;
        try {
            post = new JSONObject();
            post.put("image", postImage);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                ImageURL,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(viewPostActivity.this, "Image deleted", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(viewPostActivity.this, error.getMessage() + "" , Toast.LENGTH_LONG).show();
                        Log.d("deleteError", String.valueOf(error));
                    }
                }
        ) {

        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * method that deletes the post completely
     */
    private void deletePost() {
        JSONObject post = null;
        try {
            post = new JSONObject();

            post.put("title", titleDisplay.getText().toString());
            post.put("location", locationDisplay.getText().toString());
            post.put("notes", notesDisplay.getText().toString());
            if (displayLostOrFound.getText().toString().equals("Lost: ")) {
                post.put("type", false);
            } else {
                post.put("type", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                URL,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(viewPostActivity.this, "Post deleted", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(viewPostActivity.this, error.getMessage() + "" , Toast.LENGTH_LONG).show();
                        Log.d("deleteError", String.valueOf(error));
                    }
                }
        ) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * method that allows the current user to comment on a post
     */
    private void commentPost(String postID) {
        JSONObject postBody = null;

        try {
            postBody = new JSONObject();

            //postBody.put("content",  commentText.getText().toString());
            postBody.put("content", Actor.getCurrentUsername() + ": " + commentText.getText().toString());
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://coms-309-040.class.las.iastate.edu:8080/comments/" + postID + "/" + Actor.getCurrentId().toString(),
                postBody,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(viewPostActivity.this, "Comment Created", Toast.LENGTH_LONG).show();
                        return;
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "An error occured";
//                            Toast.makeText(viewPostActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        } else {

//                            Toast.makeText(viewPostActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * method that gets a list of comments on the current post 
     */
    private void makeJsonArrReq(ArrayList<String> contentList, String postID) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-040.class.las.iastate.edu:8080/comments/post/" + postID,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        ArrayList<String> arrayList = new ArrayList<String>();
                        arrayAdapter = new ArrayAdapter<String>(viewPostActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        commentList.setAdapter(arrayAdapter);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String content = jsonObject.getString("content");
                                Integer ID = jsonObject.getInt("id");
                                commentID.add(ID.toString());
                                arrayAdapter.add(content);
                                makeJsonObjReq(ID);
                                contentList.add(content);
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

        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    /**
     * method that gets comment by id from server
     */
    private void makeJsonObjReq(Integer ID) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://coms-309-040.class.las.iastate.edu:8080/comments/" + ID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            String title = response.getString("title");

                        } catch (JSONException e) {
                            e.printStackTrace();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * method that requests an image 
     */
    private void makeImageRequest() {

        ImageRequest imageRequest = new ImageRequest(
                ImageURL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        postImage.setImageBitmap(response);
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
                        Log.e("Volley Error", error.toString());
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
        Log.d("WebSocket", message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendNotification(viewPostActivity.this, message);
//                Toast.makeText(viewPostActivity.this, message + "", Toast.LENGTH_SHORT).show();
            }
        });
        //GET_SIGN_IN_DURATION //format

    }

    @Override
    public void onWebSocketClose(int statusCode, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception e) {

    }

    /**
     * requests post considering whether its the current users post
     */
    private void makeJsonArrReq() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-040.class.las.iastate.edu:8080/posts/actor/" + Actor.getCurrentId(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Integer ID = jsonObject.getInt("id");

                                if (postID2.equals(ID.toString())) {
                                    isUsersPost = true;
                                    changeDtlBtn.setVisibility(VISIBLE);
                                    changeDtlBtn.setEnabled(true);
                                    deletePostBtn.setVisibility(VISIBLE);
                                    deletePostBtn.setEnabled(true);
                                    break;
                                } else {

                                    changeDtlBtn.setVisibility(View.GONE);
                                    changeDtlBtn.setEnabled(false);
                                    deletePostBtn.setVisibility(View.GONE);
                                    deletePostBtn.setEnabled(false);
                                }
                            } catch (JSONException e) {
//                                e.printStackTrace();
                            }

                        }
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    /**
     * button that allows users to report posts
     */
    private void reportPost(String currentpostid) throws JSONException {

        String reportActorURL =  "http://coms-309-040.class.las.iastate.edu:8080/" + Actor.getCurrentId() + "/reports_post/" + currentpostid;
        // Convert input to JSONObject
        JSONObject postBody = null;
        postBody = new JSONObject();
        postBody.put("userName", Actor.getInstance().getSecondUsername());
        postBody.put("password", Actor.getInstance().getSecondPassword());
        postBody.put("email", Actor.getInstance().getSecondEmail());
        postBody.put("id", Actor.getSecondId());


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                reportActorURL,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(viewPostActivity.this, "Post " + currentpostid + " reported by " + Actor.getInstance().getCurrentUsername(), Toast.LENGTH_LONG).show();
                        getLastReport();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VIEWPOSTID", error.getMessage() + "");
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * method that gets the last report made 
     */
    private void getLastReport() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-040.class.las.iastate.edu:8080/reports",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = response.getJSONObject(response.length()-1);
                            int ID = jsonObject.getInt("id");
                            Actor.getInstance().setReportId(ID);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

}