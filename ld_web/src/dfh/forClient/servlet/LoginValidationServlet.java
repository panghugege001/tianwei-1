package dfh.forClient.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.remote.DocumentParser;
import dfh.remote.bean.LoginValidationBean;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;

public class LoginValidationServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(LoginValidationServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("receive login validate request from "+req.getRemoteAddr());
		String xml = IOUtils.toString(req.getInputStream());
		log.info("in xml:"+xml);
		if (StringUtils.isNotEmpty(xml)) {
			LoginValidationBean loginValidationBean = DocumentParser.parseLoginValidationBean(xml);
			if (loginValidationBean != null) {
				String responseXML = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "gameLoginValidation", 
						new Object[] {loginValidationBean.getId(),loginValidationBean.getUserid(),loginValidationBean.getPwd()}, String.class);
				log.info("out xml:"+responseXML);
				resp.setContentType("text/xml; charset=" + Constants.ENCODING);
				resp.getWriter().write(responseXML);
				resp.getWriter().flush();
			}
		}
	}
	

	@Override
	public void init() throws ServletException {
	}
}
