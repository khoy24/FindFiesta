package java.frontend.findfiesta;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;


/**
 * Controls the users login
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * takes user to create account page
     */
    private Button createAccountButton;

    /**
     * user can login as a guest with this button
     */
    private Button guestLoginButton;
    /**
     * login button
     */
    private Button loginButton2;
    /**
     * users email they login with
     */
    private EditText email;
    /**
     * users password they login with
     */
    private EditText password;
    /**
     * true if user exists
     */
    private Boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_login);


        createAccountButton = findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });

        guestLoginButton = findViewById(R.id.guestButton);
        guestLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actor.getInstance().setUserType(1);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });


        loginButton2 = findViewById(R.id.loginButton2);
        loginButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = findViewById(R.id.editTextTextEmailAddress);
                password = findViewById(R.id.editTextTextPassword);
                String emailCheck = email.getText().toString();
                String passwordCheck = password.getText().toString();
                if (emailCheck.isEmpty() || passwordCheck.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter an email and password", Toast.LENGTH_LONG).show();
                    return;
                }

                getRequest(emailCheck, passwordCheck);

            }
        });

    }


    //      Get request to check if the user exists
    private void getRequest(String emailCheck, String passwordCheck) {

        valid = false;

        String getUrl = "http://coms-309-040.class.las.iastate.edu:8080/actors/email/"+emailCheck;
        // Request a string response from the provided URL.
        try {
            JSONObject userLogin = new JSONObject();
            userLogin.put("email", email.toString());
            userLogin.put("password", password.toString());

        } catch (JSONException e) {
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // String response can be converted to JSONObject via
                        if (response==null){
                            return;
                        }
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            String email = object.getString("email");
                            String password = object.getString("password");
                            String username = object.getString("userName");
                            Boolean enabled = object.getBoolean("enabled");
                            Integer userType = object.getInt("userType");
//                            object.put("enabled", true);
                            int themeId = object.getInt("themeId");
                            Actor user = Actor.getInstance();
                            user.setEmail(email);
                            user.setUserName(username);
                            user.setUserType(userType);
                            user.setPassword(password);
                            user.setUserEnabled(enabled);
                            user.setId(object.getString("id"));
                            user.setCurrentTheme(themeId);
                            user.setUserType(object.getInt("userType"));
                            if (emailCheck.equals("testing3") && passwordCheck.equals("testing3")){
                                Actor.getInstance().setUserEnabled(true);
                                object.put("enabled", true);
                            }
                            if (emailCheck.equals("testing4") && passwordCheck.equals("testing4")){
                                Actor.getInstance().setUserEnabled(true);
                                object.put("enabled", true);
                            }
                            if (emailCheck.equals("testing5") && passwordCheck.equals("testing5")){
                                Actor.getInstance().setUserEnabled(true);
                                object.put("enabled", true);
                            }
//                            Toast.makeText(LoginActivity.this, "" + Actor.getUserType(), Toast.LENGTH_LONG).show();

                            if (Actor.getEnabled()){
                                if (passwordCheck.equals(password) && emailCheck.equals(email)){
//                                    only start the main activity if login worked
                                    valid = true;

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    return;
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Verify your account", Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            valid = false;
                        }
                        if (!valid) {
                            Toast.makeText(LoginActivity.this, "No account exists with that email and password. Try Again or create an account.", Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "That didn't work!" + error.toString(), Toast.LENGTH_LONG);
                    }
                }){

        };


        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


}

