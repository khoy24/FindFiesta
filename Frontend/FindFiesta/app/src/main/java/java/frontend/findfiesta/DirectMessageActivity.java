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

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;

public class DirectMessageActivity extends AppCompatActivity implements WebSocketListener2{

    /**
     * button that takes the user back to messages
     */
    private Button backtomsgbutton;
    /**
     * holds and displays the name of the sender
     */
    private TextView senderName;

    /**
     * sends the message to the websocket
     */
    private Button sendMsgBtn;
    /**
     * views the chat message history
     */
    private TextView msgChat;
    /**
     * text that can be sent as a message to the websocket
     */
    private EditText msgToSend;
    /**
     * holds the base message
     */
    private String message = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        WebSocketManager2.getInstance().disconnectWebSocket();
        super.onCreate(savedInstanceState);
        setTheme(Actor.getInstance().getCurrentTheme());
        setContentView(R.layout.activity_directmessage);

        //bottom of the page
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        WebSocketManager.getInstance().setWebSocketListener(DirectMessageActivity.this);
        msgChat = findViewById(R.id.directMessageView);
        msgChat.setText(Actor.getChatMessage());
        msgChat.setMovementMethod(new ScrollingMovementMethod());

        Bundle extras = getIntent().getExtras();
        try {
            String possibleDMLink = extras.getString("DMNEWlink");
            if (!possibleDMLink.isEmpty()){
                WebSocketManager.getInstance().sendMessage(possibleDMLink);
                WebSocketManager.getInstance().sendMessage("New chat");

            }
        } catch (Exception e) {
            //its null
        }



        msgToSend = findViewById(R.id.msgToSend);

        sendMsgBtn = findViewById(R.id.buttonToSend);
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //try catch to get errors
                try {
                    WebSocketManager.getInstance().sendMessage(msgToSend.getText().toString());
                    msgToSend.setText("");
                } catch (Exception e){
                    Toast.makeText(DirectMessageActivity.this, "" + e.getMessage().toString(), Toast.LENGTH_LONG);
                }
            }
        });


        backtomsgbutton = findViewById(R.id.backToMsgButton);
        backtomsgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actor.getInstance().setMessagesArrList(new ArrayList<String>());
                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(new Intent(DirectMessageActivity.this, MessagesActivity.class));
            }
        });

        senderName = findViewById(R.id.senderText);
        senderName.setText(Actor.getInstance().getSecondUsername());


    }


    @Override
    public void onWebSocketOpen2(ServerHandshake handshakedata) {
//        WebSocketManager2.getInstance().sendMessage(Actor.getCurrentUsername() + " connected to the chat.");
        WebSocketManager.getInstance().sendMessage(" connected to the chat.");
    }

    @Override
    public void onWebSocketMessage2(String message) {

        /*
        updates the chats thread with the new message
         */

        if (msgChat.getText().toString().equals(null)) {
            msgChat.setText(" \n" + /**Actor.getCurrentUsername() + ": " + */message);
        } else {
            String s = msgChat.getText().toString();
            msgChat.setText(s + "\n" + /**Actor.getCurrentUsername() + ": " + */message);
        }
//        runOnUiThread(() -> {
//            String s = msgChat.getText().toString();
//            msgChat.setText(s + "\n"+ /**Actor.getCurrentUsername() + ": " + */message);
//        });
        msgChat.scrollTo(0,msgChat.getLayout().getLineTop(msgChat.getLineCount()) - msgChat.getHeight());
    }
    @Override
    public void onWebSocketClose2(int statusCode, String reason, boolean remote) {
        WebSocketManager.getInstance().sendMessage("" + Actor.getCurrentUsername() + "disconnected from chat");
        WebSocketManager.getInstance().disconnectWebSocket();
    }

    @Override
    public void onWebSocketError2(Exception e) {
        Log.d("WEBSOCKETERROR", "onWebSocketError: " + e.getMessage());
    }


}
