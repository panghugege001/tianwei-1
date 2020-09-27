package dfh.listener;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dfh.service.interfaces.SynRecordsService;
import dfh.utils.Constants;

public class SynRecordsListener implements ServletContextListener {

	private static Logger log = Logger.getLogger(OfficeContextListener.class);
	private SynRecordsService synRecordsService;

	public void contextDestroyed(ServletContextEvent arg0) {
		log.debug("LoadConstantListener Destroyed");
	}

	public void contextInitialized(ServletContextEvent ctx) {
		log.info("add task of syn bet records");
		ApplicationContext springCtx = WebApplicationContextUtils.getWebApplicationContext(ctx.getServletContext());
		synRecordsService = (SynRecordsService) springCtx.getBean("synRecordsService");
		TimerTask taskSyn = new TimerTask() {
			@Override
			public void run() {
				synRecordsService.synBetRecords();
			}
		};
		Timer timer = new Timer();
		timer.schedule(taskSyn, Constants.SYN_BETRECORDS_DELAY.intValue() * 60 * 1000, Constants.SYN_BETRECORDS_DELAY.intValue() * 60 * 1000);
	}
}
