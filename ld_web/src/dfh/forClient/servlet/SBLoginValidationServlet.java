package dfh.forClient.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.remote.DocumentParser;
import dfh.remote.bean.SportBookLoginValidationBean;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.SportBookUtils;

public class SBLoginValidationServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(SBLoginValidationServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("receive 188Bet SportsBook login validate request from "+req.getRemoteAddr());
		Enumeration en = req.getParameterNames();
		if(en.hasMoreElements()){
			String  encryptxml = en.nextElement().toString().replaceAll(" ", "+")+"=";
			log.info("encryptxml:"+encryptxml);
			if (StringUtils.isNotEmpty(encryptxml)) {
				String xml = SportBookUtils.getDecryptAESXML(encryptxml);
				System.out.println("188Bet:"+xml);
				SportBookLoginValidationBean  sbvb = DocumentParser.parseSportBookLoginValidationBean(xml);
				if(sbvb!=null && sbvb.getToken()!=null && !"".equals(sbvb.getToken()) &&
						sbvb.getLoginName()!=null && !"".equals(sbvb.getLoginName())){
						
						String responseXML = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
								+ "UserWebService", false), AxisUtil.NAMESPACE, "gameSportBookLoginValidation", 
								new Object[] {sbvb.getToken(),sbvb.getLoginName()}, String.class);
						log.info("out xml:"+responseXML);
						resp.getWriter().write(SportBookUtils.getEncryptAESXML(responseXML));
						resp.getWriter().flush();
					}
			}
		}
	}
	

	@Override
	public void init() throws ServletException {
	}
}
