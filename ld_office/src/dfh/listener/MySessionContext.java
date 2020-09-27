package dfh.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class MySessionContext {
	private static Map<String, HttpSession> context = new HashMap<String, HttpSession>();
	public static synchronized void putSession(String id, HttpSession session) {
		context.put(id, session);
	}

	public static synchronized void removeSession(HttpSession session) {
		context.remove(session.getId());
	}

	public static Iterator<HttpSession> iterator() {
		return context.values().iterator();
	}

}
