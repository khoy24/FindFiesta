package java.frontend.findfiesta;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;



/**
 * class that contains the create account functionality
 */
public class CreateAccountActivity extends AppCompatActivity{
    /**
     * button takes user to the login page
     */
    private Button toLoginButton;
    //    private Button createAccount;
    /**
     * Enter new accounts username
     */
    private EditText usernameEdit;
    /**
     * enter new accounts email
     */
    private EditText emailEdit;
    /**
     * enter new accounts password
     */
    private EditText passwordEdit;

    /**
     * URL to retrieve data and post new user
     */
    private String url = "http://coms-309-040.class.las.iastate.edu:8080/actors";

    /**
     * button that creates the account
     */
    private Button createAccount;

    /**
     * boolean representing whether the user exists already or not
     */
    private boolean userExists;
    /**
     * boolean representing if a user with that email already exists
     */
    private boolean emailExists;

//    private boolean exists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_createaccount);

        usernameEdit = findViewById(R.id.userName);
        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password);
        createAccount = findViewById(R.id.CreateAccountButton2);

        toLoginButton = findViewById(R.id.toLoginButton);
        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
            }
        });

        // creates account when clikced if all criteria is met
        createAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                //no spaces in username
                Boolean hasSpace = false;
                for (int i = 0; i < usernameEdit.getText().toString().length(); i++) {
                    if (usernameEdit.getText().toString().charAt(i) == ' ') {
                        hasSpace = true;
                    }
                }
                if (!hasSpace) {
//                    postRequest();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "username cannot contain a space", Toast.LENGTH_LONG).show();
                    return;
                }

//                only call post request once the email is verified.

                //=password criteria
                boolean passHasNumber = false;
                String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
                for (int i = 0; i < numbers.length; i++){
                    String check = numbers[i];
                    if (passwordEdit.getText().toString().contains(check)){
                        passHasNumber = true;
                    }
                }
                if (!passHasNumber){
                    passwordEdit.setTextColor(Color.RED);
                    Toast.makeText(CreateAccountActivity.this, "Password must contain at least 1 number", Toast.LENGTH_LONG).show();
                } else {
                    passwordEdit.setTextColor(Color.BLACK);
                    if (!usernameEdit.getText().toString().isEmpty() && !emailEdit.getText().toString().isEmpty() && !passwordEdit.getText().toString().isEmpty()){
                        //if username is a duplicate say so
                        usernameExists(usernameEdit.getText().toString());
                        // if email exists already say so
                        emailExists(emailEdit.getText().toString());
//                    Toast.makeText(CreateAccountActivity.this, "" + emailExists, Toast.LENGTH_LONG).show();

//                    Toast.makeText(CreateAccountActivity.this, "" + userExists, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CreateAccountActivity.this, "Must fill in all fields", Toast.LENGTH_LONG).show();
                    }
                }




            }
        });
    }
    /**
     * Creates the new user on the server
     */
    private void postRequest() {
        // Convert input to JSONObject
        JSONObject postBody = null;
        try{

            postBody = new JSONObject();
            postBody.put("userName", usernameEdit.getText().toString());
            postBody.put("password", passwordEdit.getText().toString());
            postBody.put("email", emailEdit.getText().toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(CreateAccountActivity.this, "Account created", Toast.LENGTH_LONG).show();
//                        return false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (!userExists && !emailExists){
                            Toast.makeText(CreateAccountActivity.this, "Verify your account to login", Toast.LENGTH_LONG).show();
                        }
//                        Toast.makeText(CreateAccountActivity.this, "Verify your account to login", Toast.LENGTH_LONG).show();

                    }
                }
        ){

        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


    /**
     * username cannot contain spaces and also cannot be a duplicate username
     */
    private void usernameExists(String username) {
//        exists = false;
        String getUrl = "http://coms-309-040.class.las.iastate.edu:8080/actors/username/" + username;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // String response can be converted to JSONObject via
                        if (response.isEmpty()){
//                            exists = false;
                            userExists = false;

                            //then call email exists next
//                            emailExists(usernameEdit.getText().toString());
                            return;
                        } else {
                            Toast.makeText(CreateAccountActivity.this, "Username already exists. Try again.", Toast.LENGTH_LONG).show();
                            userExists = true;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(CreateAccountActivity.this, "That didn't work!" + error.toString(), Toast.LENGTH_LONG);
                    }
                }){
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    /**
     * method that checks if there is already an account with that email existing
     */
    private void emailExists(String emailCheck) {

        String getUrl = "http://coms-309-040.class.las.iastate.edu:8080/actors/email/"+emailCheck;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        emailExists = false;
                        if (response.isEmpty()) {
                            //if email also doesn't exist then call postRequest
                            postRequest();
                        } else {
                            Toast.makeText(CreateAccountActivity.this, "Email already exists. Try again.", Toast.LENGTH_LONG).show();
                            emailExists = true;
                        }

                }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateAccountActivity.this, "That didn't work!" + error.toString(), Toast.LENGTH_LONG);
                    }
                }){
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}


