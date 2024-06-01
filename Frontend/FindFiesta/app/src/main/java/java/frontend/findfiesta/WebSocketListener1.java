package java.frontend.findfiesta;

import org.java_websocket.handshake.ServerHandshake;

public interface WebSocketListener1 {

    /**
     * called when websocket connection is opened
     *
     * @param handshakedata - info about the server handshake
     */
    void onWebSocketOpen(ServerHandshake handshakedata);

    /**
     * called when message from websocket is received
     * @param message
     */
    void onWebSocketMessage(String message);

    /**
     * called when connection is closed (give a closed message?)
     * @param statusCode - status code (reason for the websocket closing)
     * @param reason - explanation for the closure (human-readable)
     * @param remote - indicates if the closer was initiated by the remote endpoint
     */
    void onWebSocketClose(int statusCode, String reason, boolean remote);

    /**
     * is called when the websocket communication has an error
     * @param e - exception description
     */
    void onWebSocketError(Exception e);

}
