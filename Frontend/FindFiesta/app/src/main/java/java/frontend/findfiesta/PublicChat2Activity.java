package java.frontend.findfiesta;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.WebSocketListener;
import org.java_websocket.handshake.ServerHandshake;

/**
 * class that holds public chat functionality
 */
public class PublicChat2Activity extends AppCompatActivity implements WebSocketListener1 {

    /**
     * url for the public chat
     */
    private String serverURL = "ws://coms-309-040.class.las.iastate.edu:8080/chat2/" + Actor.getCurrentUsername();
    /**
     * button to take user back to previous page
     */
    private Button backBtn;
    /**
     * button that sends the message
     */
    private Button sendMsgBtn;
    /**
     * displays the message chat
     */
    private TextView msgChat;
    /**
     * text to be edited and sent to messages
     */
    private EditText msgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicchat2);
        WebSocketManager.getInstance().disconnectWebSocket();


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        WebSocketManager2.getInstance().connectWebSocket(serverURL);
        WebSocketManager2.getInstance().setWebSocketListener(PublicChat2Activity.this);

        backBtn = findViewById(R.id.backBtn90);
        sendMsgBtn = findViewById(R.id.sendMsgBtn);
        msgChat = findViewById(R.id.msgChat);
        msgText = findViewById(R.id.msgText);

        msgChat.setMovementMethod(new ScrollingMovementMethod());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PublicChat2Activity.this, MessagesActivity.class));
                WebSocketManager2.getInstance().disconnectWebSocket();
            }
        });

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String message = msgText.getText().toString();
                    WebSocketManager2.getInstance().sendMessage(message);
                    msgText.setText("");
                } catch (Exception e) {
                    Toast.makeText(PublicChat2Activity.this, "" + e.getMessage().toString(), Toast.LENGTH_LONG);
                }
            }
        });

    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        WebSocketManager2.getInstance().sendMessage(" connected to the chat.");
    }

    @Override
    public void onWebSocketMessage(String message) {
        if (msgChat.getText().toString().equals(null)) {
            msgChat.setText(" \n" + /**Actor.getCurrentUsername() + ": " + */message);
        } else {
            String s = msgChat.getText().toString();
            msgChat.setText(s + "\n" + /**Actor.getCurrentUsername() + ": " + */message);
        }
        msgChat.scrollTo(0,msgChat.getLayout().getLineTop(msgChat.getLineCount()) - msgChat.getHeight());
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason, boolean remote) {
        WebSocketManager2.getInstance().sendMessage(Actor.getCurrentUsername() + " disconnected from the chat");
    }

    @Override
    public void onWebSocketError(Exception e) {
        Log.d("WEBSOCKETERROR", "onWebSocketError: ");
    }
}
