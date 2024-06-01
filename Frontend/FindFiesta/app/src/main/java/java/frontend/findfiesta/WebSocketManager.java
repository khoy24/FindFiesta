package java.frontend.findfiesta;

import android.util.Log;

import org.java_websocket.WebSocketListener;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

/**
 * manages websocket connections
 *
 * connect as user, send messages, view convo (messages section)
 *
 * allows for centralized websocket handling because this instance ensures there is
 * just one websocketmanager throughout the apps lifecycle
 *
 */
public class WebSocketManager {

    /**
     * an instance of the websocket manager
     */
    private static WebSocketManager instance;
    
    private MyWebSocketClient2 webSocketClient;

    private WebSocketListener2 webSocketListener;


    // constructor
    WebSocketManager(){
    }

    /**
     * gets a synchronized instance of the websocketmanager, makes sure that only one instance
     * of websocketmanager exists throughout the app. Synchronization ensures thread safety when
     * accessing or creating the instance
     *
     * @return - returns synchronized instance of WebSocketManager
     */
    public static synchronized WebSocketManager getInstance(){
        if(instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    /**
     *  sets websocketlistener for this instance. Handles websocket events like received messages and errors
     * @param listener - the websocketlistener to be set for this websocketmanager
     */
    public void setWebSocketListener(WebSocketListener2 listener){
        this.webSocketListener = listener;
    }

    /**
     * removes current websocketlistener from this websocketmanager instance.
     * Disconnects the listener from handling websocket events.
     */
    public void removeWebSocketListener(){
        this.webSocketListener = null;
    }

    /**
     * initiates a websocket connection to server url.
     * Establsishes connection with the websocket server at the url.
     *
     * @param serverUrl - url of server to connect to
     */
    public void connectWebSocket(String serverUrl){
        try{
            URI serverUri = URI.create(serverUrl);
            webSocketClient = new MyWebSocketClient2(serverUri);
            webSocketClient.connect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * sends websocket message to connected websocket server.
     * Allows app to send a message to server through the established connection.
     *
     * @param message - msg to be sent to the server
     */
    public void sendMessage(String message){
        if (webSocketClient != null && webSocketClient.isOpen()){
            webSocketClient.send(message);
//            this.message += message;
        }
    }

    /**
     * disconnectes websocket connection, terminating communication with the websocket server
     */
    public void disconnectWebSocket(){
        if (webSocketClient != null){
            webSocketClient.close();
        }
    }

//    public String getMessage(){
//        return message;
//    }

    /**
     * inner class that represents a websocket client instance tailored for specific functionalities
     * within the websocketmanager. Encapsulates the websocketclient and provides custom behavior or
     * handling for websocket communication as needed by the application
     *
     */
    private class MyWebSocketClient2 extends WebSocketClient {

        private String message;

        private MyWebSocketClient2(URI serverUri){
            super(serverUri);
        }

        /**
         * called when connection is opened succesfully and a handshake has been completed
         * in the server. Invoked to handle when the websocket connection is ready to send
         * and receive messages
         *
         * @param handshakedata - server handshake object with details about the handshake with the server
         */
        public void onOpen(ServerHandshake handshakedata){
            Log.d("WebSocket", "Connected");
            if (webSocketListener != null) {
                webSocketListener.onWebSocketOpen2(handshakedata);
            }
        }

        /**
         * called when message is received from server. Invoked to handle incoming
         * websocket messages and allows the app to process and respond to msgs as needed
         *
         * @param message The UTF-8 decoded message that was received.(message received from server as a string)
         */
        public void onMessage(String message){
            Log.d("WebSocket", "Received message: " + message);
//            this.message += message;
            if (webSocketListener != null) {
                webSocketListener.onWebSocketMessage2(message);
            }
            Actor.getInstance().setChatMessage(message);
        }

        /**
         * Called when the WebSocket connection is closed, either due to a client request
         * or a server-initiated close. This method is invoked to handle the WebSocket
         * connection closure event and provides details about the closure, such as the
         * closing code, reason, and whether it was initiated remotely.
         *
         * @param statusCode   The WebSocket closing code indicating the reason for closure.
         * @param reason A human-readable explanation for the WebSocket connection closure.
         * @param remote A boolean flag indicating whether the closure was initiated remotely.
         *               'true' if initiated remotely, 'false' if initiated by the client.
         */
        public void onClose(int statusCode, String reason, boolean remote){
            Log.d("WebSocket", "Closed");
            if (webSocketListener != null) {
                webSocketListener.onWebSocketClose2(statusCode, reason, remote);
            }
        }

        /**
         * Called when an error occurs during WebSocket communication. This method is
         * invoked to handle WebSocket-related errors and allows the application to
         * respond to and log error details.
         *
         * @param e - The Exception representing the WebSocket communication error.
         */
        @Override
        public void onError(Exception e) {
            Log.d("WebSocket", "Error" + e.getMessage());
            if (webSocketListener != null) {
                webSocketListener.onWebSocketError2(e);
            }
        }
    }
}
