package java.frontend.findfiesta;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.frontend.findfiesta.databinding.ActivityMapsBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class that holds and displays google maps and its display of the lost/found items locations
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, WebSocketListener1 {

    /**
     * holds the google map 
     */
    private GoogleMap mMap;
    /**
     * the binding for the google map
     */
    ActivityMapsBinding binding;
    /**
     * button that goes to the home screen
     */
    private Button homeButton;
    /**
     * button that goes to the account screen
     */
    private Button accountButton;
    /**
     * button that goes to the messages screen
     */
    private Button messagesButton;
    /**
     * button that goes to the map screen
     */
    private Button mapButton;
    /**
     * button that goes to the create post screen
     */
    private Button createPostButton;

    /**
     * holds the base url for posts for that actor
     */
    private String postURL = "http://coms-309-040.class.las.iastate.edu:8080/posts/actor/";
    /**
     * holds base url for all posts with /
     */
    private String URL = "http://coms-309-040.class.las.iastate.edu:8080/posts/";
    /**
     * holds base url for all posts
     */
    private String allPostsURL = "http://coms-309-040.class.las.iastate.edu:8080/posts";

    /**
     * holds the map search bar
     */
    SearchView mapsearch;
    /**
     * holds the map fragment
     */
    SupportMapFragment mapFragment;
    /**
     * holds client to show whether or not the permissions grant location
     */
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebSocketManagerNotifs.getInstanceNotifs().connectWebSocket("ws://coms-309-040.class.las.iastate.edu:8080/activity/" + Actor.getCurrentId());
        WebSocketManagerNotifs.getInstanceNotifs().setWebSocketListener(MapsActivity.this);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapsearch = findViewById(R.id.mapsearch);
        client = LocationServices.getFusedLocationProviderClient(this);

//        check location permissions
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//           when permission granted:
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        Actor.getInstance().setCurrentMap(mMap);

        mapsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = mapsearch.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addressList.size()>0){
                        Address address = addressList.get(0);
                        LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
                        if (latlng != null){
                            mMap.addMarker(new MarkerOptions().position(latlng).title(location));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 11));
                            Actor.getInstance().setCurrentMap(mMap);
                        }
                    }
                    else {
                        Toast.makeText(MapsActivity.this, "Not a valid location", Toast.LENGTH_LONG).show();
                    }
                }

                makeLocationsReq();
                return false;

            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });

        mapFragment.getMapAsync(this);

        //      Navigation Bar Buttons Begin //
        accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Actor.getUserType()==1){
                    startActivity(new Intent(MapsActivity.this, CreateAccountActivity.class));
                } else {
                    startActivity(new Intent(MapsActivity.this, AccountActivity.class));
                }
            }
        });

        createPostButton = findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Actor.getUserType()==1){
                    startActivity(new Intent(MapsActivity.this, CreateAccountActivity.class));
                } else {
                    startActivity(new Intent(MapsActivity.this, CreatePostActivity.class));
                }
            }
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
            }
        });

        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, MapsActivity.class));
            }
        });

        messagesButton = findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Actor.getUserType()==1){
                    startActivity(new Intent(MapsActivity.this, CreateAccountActivity.class));
                } else {
                    startActivity(new Intent(MapsActivity.this, MessagesActivity.class));
                }
            }
        });

//      Navigation bar buttons end ///

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker for Iowa State University and move the camera
        LatLng ISU = new LatLng(42.03, -93.65);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ISU, 50));
        mMap.addMarker(new MarkerOptions().position(ISU).title("Marker at Iowa State University"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ISU));
        makeLocationsReq();

//      enables the zoom in and zoom out buttons
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Actor.getInstance().setCurrentMap(mMap);

    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//           when permission granted
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return;
        }

        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location){

                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                            //create marker options
                            MarkerOptions options = new MarkerOptions().position(latlng).title("I am here");
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));
                            //optional: add a marker on the map
                            mMap.addMarker(options);
                            Actor.getInstance().setCurrentMap(mMap);

                        }
                    });
                }
            }
        });
    }


    /**
     * if location permission granted, receive the location
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 4) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }


   /**
    * method that gets all of the locations from all of the posts
    */
    private void makeLocationsReq() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                allPostsURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());
                        ArrayList<Address> locations = new ArrayList<Address>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
//                                requests data for each object i in the posts list
                                JSONObject jsonObject = response.getJSONObject(i);
                                Integer ID = jsonObject.getInt("id");
                                makeJsonObjReq(ID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Actor.getInstance().setCurrentMap(mMap);
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
     * gets objects from the array of posts and sets locations as markers
     */
    private void makeJsonObjReq(int ID) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL + ID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
//                          get location
                            String location = response.getString("location");
                            String item = response.getString("title");
                            List<Address> addressList = null;

                            if (location != null || !location.equals("")) {
                                Geocoder geocoder = new Geocoder(MapsActivity.this);
                                try {
                                    addressList = geocoder.getFromLocationName(location, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                int i = 0;
//                                for (i = 0, i < addressList.size(); i++){
                                if (addressList != null){
                                    if (addressList.size()>0){
                                        Address address = addressList.get(i);
                                        LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
                                        if (new MarkerOptions().position(latlng).title(location)!=null){
                                            mMap.addMarker(new MarkerOptions().position(latlng).title(item));
                                            Actor.getInstance().setCurrentMap(mMap);
                                        }

                                    }
                                }
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
               Toast.makeText(MapsActivity.this, message + "", Toast.LENGTH_SHORT).show();
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
}
