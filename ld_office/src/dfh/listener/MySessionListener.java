package dfh.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MySessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent se) {
		MySessionContext.putSession(se.getSession().getId(), se.getSession());
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		MySessionContext.removeSession(se.getSession());

	}

}
