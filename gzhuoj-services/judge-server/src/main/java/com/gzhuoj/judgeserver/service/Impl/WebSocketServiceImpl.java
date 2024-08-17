package com.gzhuoj.judgeserver.service.Impl;

import com.gzhuoj.judgeserver.judge.WebSocketClient;
import com.gzhuoj.judgeserver.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final WebSocketClient webSocketClient;

    @Override
    public void sendMessage(byte[] data) {
        webSocketClient.sendBinaryData(data);
    }
}
