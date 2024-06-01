package java.frontend.findfiesta;

import static android.os.Looper.prepare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * class that holds all functionality for the messages page
 */
public class MessagesActivity extends AppCompatActivity implements WebSocketListener2{

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
     * button that takes user to the public chat
     */
    private Button publicChat2Button;
    /**
     * button that takes the user to create a new dm with a user
     */
    private Button newMessageButton;

    /**
     * listview to display messages
     */
    private ListView messagesList;
    /**
     * shows all messages 
     */
    private ArrayList<String> messagesArrList;
    /**
     * holds integers of the new chats
     */
    private ArrayList<Integer> chatArrList;
    /**
     * array adapter for the arrays of messages
     */
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_messages);

        Actor.getInstance().setMessagesArrList(new ArrayList<String>());
        Actor.getInstance().setChatArrList(new ArrayList<Integer>());

        String url = "ws://coms-309-040.class.las.iastate.edu:8080/chat1/" + Actor.getCurrentId();
        //        String serverUrl = serverURL;
        WebSocketManager.getInstance().connectWebSocket(url);
        WebSocketManager.getInstance().setWebSocketListener(MessagesActivity.this);


        //      Navigation Bar Buttons Begin //
        accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(new Intent(MessagesActivity.this, AccountActivity.class));
            }
        });

        createPostButton = findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(new Intent(MessagesActivity.this, CreatePostActivity.class));
            }
        });

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(new Intent(MessagesActivity.this, MainActivity.class));
            }
        });

        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(new Intent(MessagesActivity.this, MapsActivity.class));
            }
        });

        messagesButton = findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MessagesActivity.this, MessagesActivity.class));
            }
        });
//      Navigation bar buttons end ///


        publicChat2Button = findViewById(R.id.publicChat2Button);
        publicChat2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(new Intent(MessagesActivity.this, PublicChat2Activity.class));
            }
        });
        /*
        new message button sends screen to new message activity
         */
        newMessageButton = findViewById(R.id.newMessageButton);
        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessagesActivity.this, NewMessageActivity.class));
            }
        });
//
        // Update TextView with the received message
        messagesList = findViewById(R.id.messagesList);
        messagesArrList = Actor.getMessagesArrList();
        chatArrList = Actor.getChatArrList();
        messagesList.setChoiceMode(ListView.CHOICE_MODE_NONE);
        arrayAdapter = new ArrayAdapter<String>(MessagesActivity.this, android.R.layout.simple_list_item_1, messagesArrList);
        messagesList.setAdapter(arrayAdapter);
//
//        makeJsonArrReq(messagesArrList);
        messagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String items = (String) adapterView.getItemAtPosition(i);
//              Sets the second actor.
                Actor.getInstance().setSecondUsername(messagesArrList.get(i).toString());
                int chatRoomId = Actor.getChatArrList().get(i);
//                send message to the websocket
                WebSocketManager.getInstance().sendMessage("JOIN_CHAT_ROOM:" + chatRoomId);
                Intent myIntent = new Intent(MessagesActivity.this, DirectMessageActivity.class).putExtra("messagesArrListParticipant2", messagesArrList.get(i));
                startActivity(myIntent);
            }
        });


    }

    @Override
    public void onWebSocketOpen2(ServerHandshake handshakedata) {
        String baseMessage = "JOIN_CHAT_ROOM:" ;
//        Toast.makeText(MessagesActivity.this, "Connected", Toast.LENGTH_LONG).show();
        String url= "ws://coms-309-040.class.las.iastate.edu:8080/chat1/" + Actor.getCurrentId();
        JSONObject participant2 = new JSONObject();
//        participant2.getJSONObject("participant2").getString("userName");
    }

    /**
     * executes on websocket message. Websocket sends a message with all chats in JSON array
     * format instantly. This parses that and then sets the messagesArrList to contain
     * all users that the current user has conversations with.
     * @param message
     */
    @Override
    public void onWebSocketMessage2(String message) {
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {

                                  //parse data from message
                                  JSONArray jsonArray = null;
                                  try {
                                      jsonArray = new JSONArray(message);
                                  } catch (JSONException e) {
//                                      returns instead of throwing an error to enter another chat room
                                      return;
//                                      throw new RuntimeException(e);
                                  }

                                  // Access data elements
                                  for (int i = 0; i < jsonArray.length(); i++) {
                                      JSONObject item = null;
                                      try {
                                          item = jsonArray.getJSONObject(i);
                                      } catch (JSONException e) {
                                          throw new RuntimeException(e);
                                      }

                                      System.out.println("Received message:");
                                      try {
                                          System.out.println("ID: " + item.getInt("id"));
                                          chatArrList.add(item.getInt("id"));
                                          System.out.println("ROOM ID: " + item.getInt("id"));
                                          Log.d("CHATLIST", chatArrList.get(i) + " : ");
                                      } catch (JSONException e) {
                                          throw new RuntimeException(e);
                                      }

                                      JSONObject participant1 = null;
                                      try {
                                          participant1 = item.getJSONObject("participant1");
                                          System.out.println("  ID: " + participant1.getInt("id"));
                                          System.out.println("  Username: " + participant1.getString("userName"));
                                          System.out.println("  Email: " + participant1.getString("email"));
                                          String sender = participant1.getString("userName");

                                      } catch (JSONException e) {
//                                          throw new RuntimeException(e);
                                      }
                                  Log.d("MESSAGESLIST", messagesList + "");
                                  JSONObject participant2 = null;
                                  try {
                                      participant2 = item.getJSONObject("participant2");
                                      System.out.println("  ID: " + participant2.getInt("id"));
                                      System.out.println("  Username: " + participant2.getString("userName"));
                                      System.out.println("  Email: " + participant2.getString("email"));

                                      try {
                                          JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                id of the chat room
                                          String id = jsonObject.getString("id");
                                          String sender = participant2.getString("userName");
                                          if (!sender.equals(Actor.getCurrentUsername())){
                                              synchronized (messagesArrList) {
                                                  if (!sender.isEmpty()) {
                                                      messagesArrList.add(sender.toString());
                                                      Log.d("ADDTOLIST", messagesArrList.get(i) + " : ");
                                                      // Notify all threads waiting on messagesArrList
//                                                  messagesArrList.notifyAll();
                                                      Log.d("MessagesArrList", "Current list: " + messagesArrList.toString());
                                                  }
                                              }
                                          }
                                          // Notify the adapter that the dataset has changed
                                          runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  arrayAdapter.notifyDataSetChanged();
                                                  Log.d("Adapter", "Dataset changed");
                                              }
                                          });

                                      } catch (JSONException e) {
//                                          e.printStackTrace();
                                      }
//                }
                                  } catch (JSONException e) {
//                                      throw new RuntimeException(e);
                                  }
                              }
            }

        });

//
    }

    @Override
    public void onWebSocketClose2(int statusCode, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError2(Exception e) {

    }

}
