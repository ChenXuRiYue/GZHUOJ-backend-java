package common.Handler;


import cn.hutool.core.collection.CollUtil;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WebSocketMessageHandler extends TextWebSocketHandler {
    private final ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, String> param = getQueryParam(session);
        String key = genKey(param.get("teamAccount"), param.get("contestNum"), session.getId());
        System.out.println(key);
        sessions.put(key, session);  // 用户连接时，添加会话到线程安全的 Map
    }

    private String genKey(String teamAccount, String contestNum, String sessionId) {
        return String.format("%s:%s:%s", teamAccount, contestNum, sessionId);
    }

    private Map<String, String> getQueryParam(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        Map<String, String> map = new HashMap<>();
        for (String s : query.split("&")) {
            String[] split = s.split("=");
            if(split.length > 1){
                map.put(split[0], split[1]);
            }
        }
        return map;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session.getId());  // 用户断开连接时移除
    }

    // 广播消息给所有连接的客户端
    // TODO 只给制定role发
    public void sendMessageToAll(Integer ContestNum, String message) {
        sessions.forEach((key, session) -> {
            if (session.isOpen()) {
                try {
                    String[] split = key.split(":");
                    if(split.length == 3 && Integer.parseInt(split[1]) == ContestNum){
                        session.sendMessage(new TextMessage(message));
                    }
                } catch (IOException e) {
                    e.printStackTrace();  // 实际生产环境可考虑更好的异常处理策略
                }
            }
        });
    }

    public void sendMessageToOne(String teamAccount, Integer ContestNum, String message) {
        System.out.println(sessions);
        sessions.forEach((key, session) -> {
            if (session.isOpen()) {
                try {
                    String[] split = key.split(":");
                    // FIXME 前端需传入正确的teamAccount
                    if(split.length == 3 && Integer.parseInt(split[1]) == ContestNum && Objects.equals(teamAccount, split[0])){
                        session.sendMessage(new TextMessage(message));
                    }
                } catch (IOException e) {
                    e.printStackTrace();  // 实际生产环境可考虑更好的异常处理策略
                }
            }
        });
    }
}
