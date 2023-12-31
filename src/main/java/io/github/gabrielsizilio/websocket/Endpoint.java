
package io.github.gabrielsizilio.websocket;

import jakarta.websocket.EncodeException;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.util.List;

@ServerEndpoint("/horario")
public class Endpoint {

    private static final Logger logger = Logger.getLogger("Endpoint HORÁRIO MUNDIAL");
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();

    public static void send(List<String> horas) throws EncodeException {
        String msg = String.format(" %s / %s / %s / %s / %s",
                horas.get(0), horas.get(1), horas.get(2), horas.get(3), horas.get(4));

        try {
            for (Session session : queue) {
                session.getBasicRemote().sendText(msg);
            }
        } catch (IOException e) {
            logger.log(Level.INFO, e.toString());
        }
    }

    @OnOpen
    public void openConnection(Session session) {
        queue.add(session);
        logger.log(Level.INFO, "Connection opened.");
    }

    @OnClose
    public void closedConnection(Session session) {
        queue.remove(session);
        logger.log(Level.INFO, "Connection closed.");
    }

    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, t.toString());
        logger.log(Level.INFO, "Connection error.");
    }

}
