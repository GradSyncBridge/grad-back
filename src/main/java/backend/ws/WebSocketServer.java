package backend.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/websocket/{sid}")
@Profile("mq")
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    private static final AtomicInteger onlineSessionClientCount = new AtomicInteger(0);
    private static final ConcurrentHashMap<String, Session> onlineSessionClientMap = new ConcurrentHashMap<>();

    private String sid;
    private Session session;

    @OnOpen
    public void onOpen(@PathParam("sid") String sid, Session session) {
        log.info("Connection opened for sid: {}", sid);
        onlineSessionClientMap.put(sid, session);
        onlineSessionClientCount.incrementAndGet();
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid, Session session) {
        log.info("Connection closed for sid: {}", sid);
        onlineSessionClientMap.remove(sid);
        onlineSessionClientCount.decrementAndGet();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Received message from sid: {}: {}", sid, message);
        sendMessageToAll(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error occurred in session: {}", session.getId(), error);
    }

    public void sendMessageToAll(String message) {
        onlineSessionClientMap.forEach((onlineSid, toSession) -> {
            if (!sid.equalsIgnoreCase(onlineSid)) {
                sendMessage(onlineSid, message);
            }
        });
    }

    public void sendMessage(String toSid, String message) {
        Session toSession = onlineSessionClientMap.get(toSid);
        if (toSession == null) {
            log.warn("No session found for sid: {}", toSid);
            return;
        }
        toSession.getAsyncRemote().sendText(message);
    }

    public void sendMessageToAllQualified(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            MessageEntity messageEntity = objectMapper.readValue(message, MessageEntity.class);

            onlineSessionClientMap.forEach((onlineSid, toSession) -> {
                if(messageEntity.getReceiver() == Integer.parseInt(onlineSid))
                    sendMessage(onlineSid, message);
            });
        }catch (Exception e){
            log.error("Error occurred in sending.");
        }
    }

}

