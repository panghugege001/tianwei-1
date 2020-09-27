package dfh.listener;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import dfh.model.enums.UserRole;
import dfh.utils.IPSeeker;

public class OfficeContextListener implements ServletContextListener {

	private Logger log;

	public OfficeContextListener() {
		log = Logger.getLogger(OfficeContextListener.class);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		log.debug("LoadConstantListener Destroyed");
	}

	public void contextInitialized(ServletContextEvent ctx) {
		log.debug("contextInitialized");
		log.info("load IPseeker...");
		IPSeeker ipSeeker = new IPSeeker(ctx.getServletContext().getRealPath("/commons/QQWry.Dat"));
		ctx.getServletContext().setAttribute("ipSeeker", ipSeeker);
	}

	private List getUsersExceptFree() {
		List list = new ArrayList();
		list.add(UserRole.MONEY_CUSTOMER);
		list.add(UserRole.AGENT);
		// list.add(UserRole.PARTNER);
		return list;
	}

}
