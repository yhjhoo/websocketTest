package com;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/clock")
public class WebSocketClock {
	static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

	private static Set<Session> allSessions;

	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@OnOpen
	public void showTime(Session session) {
		allSessions = session.getOpenSessions();

		// start the scheduler on the very first connection
		// to call sendTimeToAll every second
		if (allSessions.size() == 0) {
			timer.scheduleAtFixedRate(() -> sendTimeToAll(session), 0, 5, TimeUnit.SECONDS);
		}
	}

	private void sendTimeToAll(Session session) {
		allSessions = session.getOpenSessions();
		for (Session sess : allSessions) {
			try {
				sess.getBasicRemote().sendText("Local time: " + LocalTime.now().format(timeFormatter));
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		}
	}
}
