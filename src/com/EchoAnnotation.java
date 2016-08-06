package com;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/websocket/echoAnnotation")
public class EchoAnnotation {
	
//	@OnOpen
//	public void onOpen1() {
//		System.out.println("11" );
//	}
	
	private static Set<String> list = new HashSet<>();
	
	@OnOpen
	public void onOpen(Session session) {
		Set<Session> test = session.getOpenSessions();
		
//		System.out.println(test.size() );
		
		list.add(session.getId() );
		System.out.println("----------------------------------");
		System.out.println(list.size() );
		for(String id : list){
			System.out.println(id);
		}
	}
	
	@OnClose
    public void close(Session session) {
		
	}
	
	@OnMessage
    public void echoTextMessage1(Session session, String msg, boolean last) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(msg, last);
            }
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }

    @OnMessage
    public void echoBinaryMessage(Session session, ByteBuffer bb,
            boolean last) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendBinary(bb, last);
            }
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }

    /**
     * Process a received pong. This is a NO-OP.
     *
     * @param pm    Ignored.
     */
    @OnMessage
    public void echoPongMessage(PongMessage pm) {
        // NO-OP
    }
}
