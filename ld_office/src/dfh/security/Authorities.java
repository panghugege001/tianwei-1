package dfh.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.nnti.office.model.auth.Operator;

import dfh.utils.Constants;
import dfh.utils.DomOperator;

public class Authorities {
	private static Logger log = Logger.getLogger(Authorities.class);
	
	public static Map<String, String> getAuthorites(String authoritiesXml) {
		Map<String, String> authoritiesMap = new HashMap<String, String>();
		Document doc = DomOperator.string2Document(authoritiesXml);
		if (doc != null) {
			Element root = doc.getRootElement();
			if (root != null) {
				List eleLst = root.elements();
				Iterator it = eleLst.listIterator();
				while (it.hasNext()) {
					Element ele = (Element) it.next();
					// 如果是目录
					if ("folder".equals(ele.getName())) {
						Element folder = ele;
						Iterator<Element> functionsIt = ele.elements().listIterator();
						while (functionsIt.hasNext()) {
							Element function = functionsIt.next();
							if ("function".equals(function.getName())) {
								//URL
								authoritiesMap.put(function.attributeValue("href"), function.attributeValue("access"));
							}
							//如果有子标签
							if(function.elements().size()>0){
								Iterator<Element> btnIt=function.elements().listIterator();
								while (btnIt.hasNext()) {
									Element btn = btnIt.next();
									//控件显示与否
									authoritiesMap.put(btn.attributeValue("name"), btn.attributeValue("access"));
								}
							}
						}
					}
				}
			}
		} else {
			log.info("document parse failed:" + authoritiesXml);
		}
		return authoritiesMap;
	}
	
	
	public static boolean canAccess(HttpServletRequest req,String controlName){
		log.debug("excute canAccess");
		boolean canAccess=false;
		log.debug(req);
		try{
		HttpSession session=req.getSession();
		ServletContext application=session.getServletContext();
		Operator operator=(Operator) session.getAttribute(Constants.SESSION_OPERATORID);
		if(operator==null){
			log.debug("session operator is null");
			canAccess=false;
		}else{
			String authority=operator.getAuthority();
			Map<String, String> authoritiesMap = (Map<String, String>)application.getAttribute("authoritiesMap");
			String canAccessAuthority=authoritiesMap.get(controlName);
			log.debug("canAccessAuthority:"+canAccessAuthority);
			if(StringUtils.isEmpty(canAccessAuthority))
				canAccess=false;
			else{
				String[] access = canAccessAuthority.split(",");
				List accessList = Arrays.asList(access);
				if (accessList.contains(authority.toLowerCase())) {
					canAccess=true;
				}else{
					canAccess=false;
				}
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return canAccess;
	}
	
	public static boolean canSee(HttpServletRequest req,String controlName){
		log.debug("excute canAccess");
		boolean canAccess=false;
		log.debug(req);
		try{
		if(req==null){
			return false;
		}
		HttpSession session=req.getSession();
		ServletContext application=session.getServletContext();
		Operator operator=(Operator) session.getAttribute(Constants.SESSION_OPERATORID);
		if(operator==null){
			log.debug("session operator is null");
			canAccess=false;
		}else{
			String authority=operator.getAuthority();
			if(authority.equals("fnfh")|| authority.equals("fnfh_leader")|| authority.equals("finance_manager")||authority.equals("admin")||authority.equals("sale_manager")){
				canAccess=true;
			}else{
				canAccess=false;
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return canAccess;
	}

	public static String translateToHTMLCode(String authoritiesXml, String contextPath, String role) {
		String html = "";
		String newLine = "\n";
		Document doc = DomOperator.string2Document(authoritiesXml);
		if (doc != null) {
			Element root = doc.getRootElement();
			if (root != null) {
				List eleLst = root.elements();
				Iterator it = eleLst.listIterator();
				while (it.hasNext()) {
					Element ele = (Element) it.next();
					// 如果是目录
					if ("folder".equals(ele.getName())) {
						Element folder = ele;
						String folderStartHtml = "<div><img align=absMiddle  border=0 id=xingzheng onclick=swapimg(xingzheng," + folder.attributeValue("id") + ",openx) src=\"" + contextPath
								+ "/images/pics/tminus.gif\" style=\"CURSOR: hand\"><img align=absMiddle border=0 class=havechild id=openx src=\"" + contextPath
								+ "/images/pics/icon_folderopen.gif\">" + folder.attributeValue("name") + "</div><div class=off id=" + folder.attributeValue("id") + ">";
						html += (folderStartHtml + newLine);
						Iterator<Element> functionsIt = ele.elements().listIterator();
						while (functionsIt.hasNext()) {
							Element function = functionsIt.next();
							if ("function".equals(function.getName())) {
								String[] access = function.attributeValue("access").split(",");
								List accessList = Arrays.asList(access);
								if (function.attributeValue("access").equalsIgnoreCase("all") || accessList.contains(role.toLowerCase())) {
									String functionHtml = "<div><nobr><img align=absMiddle src=\"" + contextPath + "/images/pics/i.gif\"><img align=absMiddle src=\"" + contextPath
											+ "/images/pics/t.gif\"><img align=absMiddle alt=Folder src=\"" + contextPath + "/images/pics/icon_inbox.gif\"> <a class=folderlink href=\"" + contextPath
											+ function.attributeValue("href") + "\" target=\"mainFrame\">" + function.attributeValue("name") + "</a></div>";
									html += (functionHtml + newLine);
								}
							}
						}
						String folderEndHtml = ("</div>" + newLine);
						html += folderEndHtml;
					}
				}
			}
		} else {
			log.info("document parse failed:" + authoritiesXml);
		}
		return html;
	}
}
