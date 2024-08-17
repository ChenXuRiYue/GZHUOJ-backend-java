package com.gzhuoj.judgeserver.judge;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    private ObjectMapper objectMapper = new ObjectMapper();
    public WebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
    }

    @SneakyThrows
    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        objectMapper.readTree(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from server");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void sendBinaryData(byte[] data) {
        send(ByteBuffer.wrap(data));
    }
}
