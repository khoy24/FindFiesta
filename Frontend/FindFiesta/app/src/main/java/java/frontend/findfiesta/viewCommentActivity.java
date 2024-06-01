package java.frontend.findfiesta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * class that holds the view comment page functionality
 */
public class viewCommentActivity extends AppCompatActivity implements WebSocketListener1{

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
     * displays comment text
     */
    private TextView commentText;
    /**
     * allows user to type in a comment
     */
    private EditText commentEdit;
    /**
     * button to change comment
     */
    private Button editComment;
    /**
     * button that deletes comment
     */
    private Button deleteComment;
    /**
     * button to confirm changes 
     */
    private Button confirmBtn;
    /**
     * button to cancel changes
     */
    private Button cancelBtn;
    /**
     * boolean for if wanting to edit comment
     */
    private boolean editCommentBool;
    /**
     * boolean for if wanting to delete comment
     */
    private boolean deleteCommentBool;
    /**
     * button to take user back to previous page
     */
    private Button backBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_view_comment);

        String postID = getIntent().getStringExtra("postID");

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(viewCommentActivity.this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        String commentID = getIntent().getStringExtra("commentID");
        String commentURL = "http://coms-309-040.class.las.iastate.edu:8080/comments/" + commentID;

        // nav bar begins 
        accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewCommentActivity.this, AccountActivity.class));
            }
        });

        createPostButton = findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewCommentActivity.this, CreatePostActivity.class));
            }
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewCommentActivity.this, MainActivity.class));
            }
        });

        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewCommentActivity.this, MapsActivity.class));
            }
        });

        messagesButton = findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewCommentActivity.this, MessagesActivity.class));
            }
        });
        //nav bar ends
        commentText = findViewById(R.id.commentText);
        commentEdit = findViewById(R.id.commentEdit);
        editComment = findViewById(R.id.editComment);
        deleteComment = findViewById(R.id.deleteComment);
        confirmBtn = findViewById(R.id.confirmBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        backBtn = findViewById(R.id.backBtn);

        commentEdit.setVisibility(View.GONE);
        commentEdit.setEnabled(false);
        confirmBtn.setVisibility(View.GONE);
        confirmBtn.setEnabled(false);
        cancelBtn.setVisibility(View.GONE);
        cancelBtn.setEnabled(false);


        makeJsonObjReq(commentID);


        editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editComment.setEnabled(false);
                editComment.setVisibility(View.GONE);
                deleteComment.setEnabled(false);
                deleteComment.setVisibility(View.GONE);
                confirmBtn.setVisibility(View.VISIBLE);
                confirmBtn.setEnabled(true);
                cancelBtn.setVisibility(View.VISIBLE);
                cancelBtn.setEnabled(true);
                editCommentBool = true;
                commentEdit.setVisibility(View.VISIBLE);
                commentEdit.setEnabled(true);
                commentText.setVisibility(View.GONE);
            }
        });

        deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editComment.setEnabled(false);
                editComment.setVisibility(View.GONE);
                deleteComment.setEnabled(false);
                deleteComment.setVisibility(View.GONE);
                confirmBtn.setVisibility(View.VISIBLE);
                confirmBtn.setEnabled(true);
                cancelBtn.setVisibility(View.VISIBLE);
                cancelBtn.setEnabled(true);
                deleteCommentBool = true;
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCommentBool) {
                    cancelBtn.setEnabled(false);
                    cancelBtn.setVisibility(View.GONE);
                    confirmBtn.setEnabled(false);
                    confirmBtn.setVisibility(View.GONE);
                    deleteComment.setVisibility(View.VISIBLE);
                    deleteComment.setEnabled(true);
                    editComment.setVisibility(View.VISIBLE);
                    editComment.setEnabled(true);
                    editCommentBool = false;
                    commentEdit.setVisibility(View.GONE);
                    commentEdit.setEnabled(false);
                    if (commentEdit.length() > 0) {
                        makeJsonObjUpdate(commentEdit.getText().toString(), commentID);
                    }
                    startActivity(new Intent(viewCommentActivity.this, viewCommentActivity.class).putExtra("commentID", commentID).putExtra("postID", postID));
                } else {
                    editCommentBool = false;
                    deleteCommentMethod(commentURL);
                    startActivity(new Intent(viewCommentActivity.this, viewPostActivity.class).putExtra("postID", postID));
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBtn.setEnabled(false);
                cancelBtn.setVisibility(View.GONE);
                confirmBtn.setEnabled(false);
                confirmBtn.setVisibility(View.GONE);
                deleteComment.setVisibility(View.VISIBLE);
                deleteComment.setEnabled(true);
                editComment.setVisibility(View.VISIBLE);
                editComment.setEnabled(true);
                editCommentBool = false;
                commentEdit.setVisibility(View.GONE);
                commentEdit.setEnabled(false);
                commentText.setVisibility(View.VISIBLE);
                deleteCommentBool = false;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewCommentActivity.this, viewPostActivity.class).putExtra("postID", postID));
            }
        });

    }



    /**
     * gets the requested comment 
     */
    private void makeJsonObjReq(String commentID) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://coms-309-040.class.las.iastate.edu:8080/comments/" + commentID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {

                            String content = response.getString("content");
                            commentText.setText(content);

                            Integer commenterID = response.getJSONObject("actor").getInt("id");
                            if (!commenterID.toString().equals(Actor.getCurrentId())) {
                                deleteComment.setVisibility(View.GONE);
                                deleteComment.setEnabled(false);
                                editComment.setVisibility(View.GONE);
                                editComment.setEnabled(false);
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
     * edits the comment
     */
    private void makeJsonObjUpdate(String commentEdit, String commentID) {
        JSONObject post = null;
        try {
            post = new JSONObject();
            if (commentEdit.length() > 0) {
                post.put("content", Actor.getCurrentUsername() + ": " + commentEdit);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                "http://coms-309-040.class.las.iastate.edu:8080/comments/" + commentID,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(viewCommentActivity.this, "Comment updated", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "An error occured";
//                            Toast.makeText(viewCommentActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        } else {
//                            Toast.makeText(viewCommentActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
     * method that completely deletes the comment
     */
    private void deleteCommentMethod(String commentURL) {
        JSONObject post = null;
        try {
            post = new JSONObject();
            post.put("content", commentText.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                commentURL,
                post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(viewCommentActivity.this, "Post deleted", Toast.LENGTH_LONG).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(viewCommentActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
               Toast.makeText(viewCommentActivity.this, message + "", Toast.LENGTH_SHORT).show();
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
