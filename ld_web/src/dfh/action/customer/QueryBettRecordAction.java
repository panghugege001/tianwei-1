package dfh.action.customer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import dfh.action.SubActionSupport;
import dfh.model.Users;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.ILogin;
import dfh.service.interfaces.IMemberSignrecord;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;
import dfh.utils.SendPhoneMsgUtil;
import dfh.utils.Touclick;
/**
 * 查询
 * @author jad
 *
 */
public class QueryBettRecordAction extends SubActionSupport {
	private static Logger log = Logger.getLogger(QueryBettRecordAction.class);
	
	private String account;
	
	private String producttype;

	private String keyword;
	

	public String queryBettRecord(){
		HttpServletRequest request=getRequest();
		HttpSession chksession = request.getSession(true);
		Users user = (Users) chksession.getValue("customer");
		 account=user.getLoginname();
		 producttype="e68";//l8 e68 qy8
		 keyword =EncryptionUtil.encrypt(account+"ninetynine99");
		return SUCCESS;
	}


	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}


	public String getProducttype() {
		return producttype;
	}


	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}


	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	 
	

}
