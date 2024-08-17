package com.gzhuoj.judgeserver.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 连接建立后
        System.out.println("Connection established");
        session.sendMessage(new TextMessage("Welcome to the WebSocket server!"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 接收到客户端消息时
        System.out.println("Received message: " + message.getPayload());
        session.sendMessage(new TextMessage("Server response: " + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 连接关闭后
        System.out.println("Connection closed: " + status);
    }
}
