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
import android.widget.ArrayAdapter;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Controls the create post activity page
 */
public class PostAddPictureActivity extends AppCompatActivity implements WebSocketListener1{

    /**
     * url for post images
     */
    private String imageURL = "http://coms-309-040.class.las.iastate.edu:8080/post_images/";
    /**
     * url for current users post
     */
    private String postURL = "http://coms-309-040.class.las.iastate.edu:8080/posts/actor/" + Actor.getCurrentId();
    /**
     * holds current user 
     */
    private Actor user = Actor.getInstance();
    /**
     * holds current user ID
     */
    private String userID = Actor.getCurrentId();
    /**
     * button for the camera
     */
    private Button CameraBtn;
    /**
     * button to choose from the camera roll
     */
    private Button CameraRollBtn;
    /**
     * skips adding an image
     */
    private Button SkipBtn;
    /**
     * button to confirm additions
     */
    private Button ConfirmBtn;
    /**
     * imageview that holds the image of the post
     */
    private ImageView postImage;
    /**
     * string that holds the current photo path
     */
    String currentPhotoPath;
    /**
     * holds the selected URI
     */
    Uri selectedUri;
    /**
     * holds current post ID
     */
    private String currentPostID = "0";
    /**
     * integer to choose the gallery 
     */
    private static final int REQUEST_GALLERY_ACCESS = 1;
    /**
     * integer to choose the camera
     */
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_post_add_picture);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(PostAddPictureActivity.this);

//      Navigation bar buttons end ///

        CameraBtn = findViewById(R.id.CameraBtn);
        CameraRollBtn = findViewById(R.id.CameraRollBtn);
        postImage = findViewById(R.id.PostImage);
        ConfirmBtn = findViewById(R.id.ConfirmBtn);
        SkipBtn = findViewById(R.id.SkipBtn);


        CameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                makeJsonArrReq();
                dispatchTakePictureIntent();
            }
        });

        CameraRollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                makeJsonArrReq();
                startActivityForResult(gallery, REQUEST_GALLERY_ACCESS);
            }
        });

        SkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getIntent().getStringExtra("postID").equals("-1")) {
                    Intent returnToPost = new Intent(PostAddPictureActivity.this, viewPostActivity.class).putExtra("postID", getIntent().getStringExtra("postID"));
                    startActivity(returnToPost);
                } else {
                startActivity(new Intent(PostAddPictureActivity.this, MainActivity.class));
            }}
        });

        ConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (postImage != null) {
                uploadImage();
                if (!getIntent().getStringExtra("postID").equals("-1")) {
                    Intent returnToPost = new Intent(PostAddPictureActivity.this, viewPostActivity.class).putExtra("postID", currentPostID);
                    startActivity(returnToPost);
                } else {
                    startActivity(new Intent(PostAddPictureActivity.this, MainActivity.class));
                }

            }
        });
    }

    /**
     * either requests photo from the gallery, or sends to the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE/** && resultCode == RESULT_OK*/) {

            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                postImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                selectedUri = contentUri;
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

            }
        }
        if (requestCode == REQUEST_GALLERY_ACCESS) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                selectedUri = contentUri;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri: " + imageFileName);
                postImage.setImageURI(contentUri);
                currentPhotoPath = contentUri.getPath();
            }
        }
    }

    /**
     * retrieves a file ext
     */
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    /**
     * creates an image file
     */
    private File createImageFile() throws IOException {
        //create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //use if not adding pic to camera roll

        File image = File.createTempFile(
                imageFileName, //prefix
                ".jpg", //suffix
                storageDir //directory
        );
        Log.d("storageDir", storageDir.getAbsolutePath());
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    /**
     * method that allows a picture to be taken
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {

        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * converts the image URI to bytes
     */
    private byte[] convertImageUriToBytes(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * uploads an image to post
     */
    private void uploadImage(){
        if (postImage!=null) {
            byte[] imageData = convertImageUriToBytes(selectedUri);
            if (getIntent().getStringExtra("postID").equals("-1")) {
            } else {
                currentPostID = getIntent().getStringExtra("postID");
                Intent viewPostIntent = new Intent(PostAddPictureActivity.this, viewPostActivity.class).putExtra("postID", currentPostID);
            }
            MultipartRequest multipartRequest = new MultipartRequest(
                    Request.Method.POST,
                    imageURL + currentPostID,
                    imageData,
                    response -> {
                        // Handle response
                        Toast.makeText(getApplicationContext(), response + "", Toast.LENGTH_LONG).show();
                        Log.d("Upload", "Response: " + response);
                    },
                    error -> {
                        // Handle error
                        Toast.makeText(getApplicationContext(), error.getMessage() + "", Toast.LENGTH_LONG).show();
                        Log.e("Upload", "Error: " + error.getMessage());
                    }
            );
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
        }
    }

    /**
     * requests array of posts wiht their ids
     */
    private void makeJsonArrReq() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                postURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int ID = jsonObject.getInt("id");
                                makeJsonObjReq(ID);

                                if (ID != 0) {
//                                    postID.add(ID.toString());
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

    /**
     * requests the post to add a picture to
     */
    private void makeJsonObjReq(int ID) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://coms-309-040.class.las.iastate.edu:8080/posts/" + ID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            Integer id = response.getInt("id");
                            currentPostID = id.toString();
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
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
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
               Toast.makeText(PostAddPictureActivity.this, message + "", Toast.LENGTH_SHORT).show();
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