package java.frontend.findfiesta;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * class that holds functionality for mod requests for mods to view
 */
public class ModRequestActivity extends AppCompatActivity {

    /**
     * array adapter to hold mod requests
     */
    ArrayAdapter<String> arrayAdapter;

    /**
     * listview to display the users requesting to be mods
     */
    private ListView modApplications;

    /**
     * button that takes mod back to previous page
     */
    private Button backButton;

    /**
     * keeps track of user ids of the mod applicants
     */
    private ArrayList<String> userID = new ArrayList<>(0);
    /**
     * keeps track of the username of the mod applicants
     */
    private ArrayList<String> applyingUser = new ArrayList<>(0);
    /**
     * keeps track of usertype of applying user
     */
    private ArrayList<Integer> applyingUserType = new ArrayList<>(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_modrequests);

        modApplications = findViewById(R.id.modApplicationsList);
        getModRequests(userID, applyingUser);
        modApplications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String items = (String) adapterView.getItemAtPosition(i);
                Actor.getInstance().setSecondId(userID.get(i));
                Actor.getInstance().setSecondUsername(applyingUser.get(i));
                Actor.getInstance().setSecondUserType(applyingUserType.get(i));
                Intent myIntent = new Intent(ModRequestActivity.this, OtherUserActivity.class).putExtra("userID", userID.get(i)).putExtra("modrequest", "true");
                startActivity(myIntent);

            }
        });

        backButton = findViewById(R.id.backbutton60);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ModRequestActivity.this, AccountActivity.class));
            }
        });

    }

    /**
     * method that lists all users that have applied to be mods (userType == 4)
     *
     * @param userID - id of the applicant
     * @param applyingUser - user that is applying
     */
    private void getModRequests(ArrayList<String> userID, ArrayList<String> applyingUser) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-040.class.las.iastate.edu:8080/actors",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Reponse", response.toString());
                        ArrayList<String> arrayList = new ArrayList<String>();
                        arrayAdapter = new ArrayAdapter<String>(ModRequestActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        modApplications.setAdapter(arrayAdapter);
                        int count = 0;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Integer ID = jsonObject.getInt("id");
                                String applied = jsonObject.getString("userName");
                                Integer userType = jsonObject.getInt("userType");
                                if (ID != null && userType==4) {
                                    if (i < response.length()){
                                        userID.add(ID.toString());
                                        applyingUser.add(applied.toString());
                                        arrayAdapter.add("" + applyingUser.get(count));
                                        applyingUserType.add(userType);
                                        count ++;
                                    }
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

}
