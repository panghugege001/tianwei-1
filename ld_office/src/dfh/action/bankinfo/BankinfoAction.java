package dfh.action.bankinfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.office.model.auth.Operator;
import com.opensymphony.xwork2.Action;

import dfh.action.SubActionSupport;
import dfh.model.Bankinfo;
import dfh.model.Const;
import dfh.model.FPayorder;
import dfh.model.MerchantPay;
import dfh.model.PayMerchant;
import dfh.model.commons.Response;
import dfh.model.notdb.BankVo;
import dfh.service.interfaces.IBankinfoService;
import dfh.service.interfaces.OperatorService;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;
import dfh.utils.MyWebUtils;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.RemoteUtils;

public class BankinfoAction extends SubActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1182716425426537063L;
	private Logger log=Logger.getLogger(BankinfoAction.class);
	private String username;
	private Integer type;
	private String bankname;
	private Integer bankInfoType;
	private String remark;
	private String errormsg;
	private Integer size;
	private Integer pageIndex;
	private String jobPno;
	private Integer id;
	private Integer useable;
	private String vpnname;
	private String vpnpassword;
	private String accountno;
	private String userrole;
	private Integer isshow;
	private String loginname;
	private String password;
	private String bankcard;
	private String usb;
	private String realname;
	private String remoteip;
	private String istransfer;
	
	//远程启动服务器
	private String remotetype;
	private String jobip;
	
	private String samebank;
	private String crossbank;
	private Double transfermoney;
	private Double fee;
	private Integer autopay;;
	private String switchtype;
	private String zfbImgCode;
	private Integer scanAccount;
	private List<Bankinfo> bankinfos = new ArrayList<Bankinfo>();
    private String orderNo;
    private String pno;
	
	private String redirect_url;
	
	private static ResourceBundle rb = ResourceBundle.getBundle("config");
	
	private IBankinfoService bankinfoService;
	private OperatorService operatorService;
	
	private Date start;
	private Date end;
	private Integer paytype;
	private Integer flag;
	
	private Double depositMin;
	private Double depositMax;
	
	public double getDepositMin() {
		return depositMin;
	}

	public void setDepositMin(double depositMin) {
		this.depositMin = depositMin;
	}

	public double getDepositMax() {
		return depositMax;
	}

	public void setDepositMax(double depositMax) {
		this.depositMax = depositMax;
	}
	
	public String addbankinfo(){
		if(null == type || type==0){
			setErrormsg("账户性质不可为空");
			return INPUT;
		}
		if(null == bankInfoType){
			setErrormsg("账户类型不可为空");
			return INPUT;
		}
		if(StringUtils.isBlank(bankname)){
			setErrormsg("银行名称不可为空");
			return INPUT;
		}
		if(type == 2 && "1".equals(autopay)){
			//查询是否有自动付款的支付账户
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc.add(Restrictions.eq("type", type));
			dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.eq("autopay", 1));
			List<?> banks = bankinfoService.getHibernateTemplate().findByCriteria(dc);
			if(null != banks && banks.size()>=2){
				setErrormsg("存在两个自动付款的支付账户，请修改");
				return INPUT;
			}
		}
		try {
			String result = bankinfoService.addbankinfo(id,username, type, bankname, remark,vpnname,vpnpassword,accountno,loginname,password,bankInfoType,bankcard,usb,realname,remoteip,samebank,crossbank,transfermoney,autopay,userrole,fee,zfbImgCode,scanAccount,paytype,depositMin,depositMax);
			if(null == result){
				setErrormsg("提交成功");
			}else{
				setErrormsg(result);
			}
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
	}
	
	public String addbankinfoTwo(){
		if(username==null||username.equals("")){
			setErrormsg("账户名称不可为空");
			return INPUT;
		}
		if(null == type || type==0){
			setErrormsg("账户性质不可为空");
			return INPUT;
		}
		if(null == bankInfoType){
			setErrormsg("账户类型不可为空");
			return INPUT;
		}
		if(StringUtils.isBlank(bankname)){
			setErrormsg("银行名称不可为空");
			return INPUT;
		}
		if (depositMin==null) {
			depositMin=0.00;
		}
		if (depositMax==null) {
			depositMax=0.00;
		}
		/*if(type == 2 && autopay == 1){
			//查询是否有自动付款的支付账户
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc.add(Restrictions.eq("type", type));
			dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.eq("autopay", 1));
			List<Bankinfo> banks = (List<Bankinfo>)bankinfoService.getHibernateTemplate().findByCriteria(dc);
			if(null != banks && banks.size()>=2){
				setErrormsg("存在两个自动付款的支付账户，请修改");
				return INPUT;
			}
		}*/
		try {
			String result = bankinfoService.addbankinfo(id,username, type, bankname, remark,vpnname,vpnpassword,accountno,loginname,password,bankInfoType,bankcard,usb,realname,remoteip,samebank,crossbank,transfermoney,autopay,userrole,fee,zfbImgCode,scanAccount,paytype,depositMin,depositMax);
			Bankinfo bankinfo= (Bankinfo)bankinfoService.get(Bankinfo.class, id);
			userrole=bankinfo.getUserrole();
			if(userrole==null){
				userrole="";
			}
			if(null == result){
				setErrormsg("提交成功");
			}else{
				setErrormsg(result);
			}
			return INPUT;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return INPUT;
	}
	
	public String changeBankSwitch(){
		try {
			if(null != id && StringUtils.isNotBlank(switchtype)){
				bankinfoService.updateBankSwitch(id, switchtype);
				GsonUtil.GsonObject("更新成功");
			}else{
				GsonUtil.GsonObject("ID和switchtype不能为空");
			}
		} catch (Exception e) {
			GsonUtil.GsonObject(e.getMessage());
			e.printStackTrace();
		}
		return null ;
	}
	
	public String querybankinfo(){
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("username", username));
		}
		if (StringUtils.isNotEmpty(bankname)) {
			dc.add(Restrictions.eq("bankname", bankname));
		}
		if (bankInfoType != null) {
			dc.add(Restrictions.eq("banktype", bankInfoType));
		}
		if (scanAccount!=null){
			dc.add(Restrictions.eq("scanAccount", scanAccount));
		}
		if (StringUtils.isNotEmpty(vpnpassword)) {
			dc.add(Restrictions.like("vpnpassword", vpnpassword));
		}
		if (type != 0) {
			dc.add(Restrictions.eq("type", type));
		}
		if (null != useable && useable != 2) {
			dc.add(Restrictions.eq("useable", useable));
		}

		if (start != null&& useable == -1) {
		    dc = dc.add(Restrictions.ge("updatetime", start));
		}
		if (end != null&& useable == -1) {
             dc = dc.add(Restrictions.lt("updatetime", end));	
             dc.addOrder(Order.desc("updatetime"));
		}

		
		Page page = PageQuery.queryForPagenation(bankinfoService
				.getHibernateTemplate(), dc, pageIndex, size,null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	
	/**
	 * 显示区间信息-客服权限 只能看
	 * @return
	 */
	public String showbankinfo(){
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		if (StringUtils.isNotEmpty(username)){
			dc.add(Restrictions.eq("username", username));
		}
		if (StringUtils.isNotEmpty(bankname)){
			dc.add(Restrictions.eq("vpnpassword", bankname));
		}
		
			dc.add(Restrictions.eq("type",1 ));
			dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.eq("isshow", 1));
			dc.addOrder(Order.desc("id"));
			
		Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, size, null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	
	/**
	 *  秒付宝
	 * @return
	 */
	public String queryFlashPayOrder(){
		DetachedCriteria dc = DetachedCriteria.forClass(FPayorder.class);
		if (StringUtils.isNotEmpty(orderNo)) {
			dc.add(Restrictions.eq("billno", orderNo));
			Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, size ,null);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (StringUtils.isNotEmpty(pno)) {
			dc.add(Restrictions.eq("pno", pno));
			Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, size , null);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (StringUtils.isNotEmpty(username)){
			dc.add(Restrictions.eq("loginname", username));
		}
		if (null != flag){
			dc.add(Restrictions.eq("flag", flag));
		}
		if (start != null){
			dc = dc.add(Restrictions.ge("createTime", start));
		}
		if (end != null){
			dc = dc.add(Restrictions.lt("createTime", end));
		}
		dc.addOrder(Order.desc("createTime"));
		Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, size ,null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	
	
	public String queryAlipayCards() {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.in("bankname", new String[] { "招商银行", "兴业银行","平安银行","民生银行","华夏银行","中信银行","广发银行","桂林银行","邮政储蓄银行","农业银行","建设银行","青岛银行","微信","通联转账"}));
		dc.add(Restrictions.eq("useable", 0));
		dc.add(Restrictions.eq("type", 1));
//		dc.addOrder(Order.desc("updatetime"));
		Order o = Order.desc("updatetime");
		Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, 50, o);
		GsonUtil.GsonList(page.getPageContents());
		return null ;
	}
	
	
	 public String queryBankinfo() {
	        DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
	        dc.add(Restrictions.in("bankname", new String[] { "云闪付"}));
	        dc.add(Restrictions.eq("useable", 0));
	        dc.add(Restrictions.eq("type", 1));
	        Order o = Order.desc("updatetime");
	        Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, 100, o);
	        GsonUtil.GsonList(page.getPageContents());
	        return null;
	    }
	
	
	
	public String querypaymerchant(){
		DetachedCriteria dc = DetachedCriteria.forClass(PayMerchant.class);
		Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, 50, null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String queryQrcodeBankinfo() {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.in("bankname", new String[] { bankname }));
		dc.add(Restrictions.eq("useable", 0));
		dc.add(Restrictions.eq("type", 1));
		Order o = Order.desc("updatetime");
		Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, 100, o);
		GsonUtil.GsonList(page.getPageContents());
		return null;
	}

	public String queryTlyBankinfo() {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.in("vpnpassword", new String[] { bankname }));
		dc.add(Restrictions.eq("useable", 0));
		dc.add(Restrictions.eq("type", 1));
		dc.add(Restrictions.eq("paytype", type));
		Order o = Order.desc("updatetime");
		Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, 100, o);
		GsonUtil.GsonList(page.getPageContents());
		return null;
	}

	public String querySelfCollectInfo() {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.in("bankname", new String[] { "微信二维码收款", "支付宝二维码收款" }));
		dc.add(Restrictions.eq("useable", 0));
		dc.add(Restrictions.eq("type", 1));
		dc.add(Restrictions.eq("paytype", type));
		Order o = Order.desc("updatetime");
		Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, 100, o);
		GsonUtil.GsonList(page.getPageContents());
		return null;
	}
	
	
	
	public String querybankinfostatus(){
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		if (StringUtils.isNotEmpty(username)){
			dc.add(Restrictions.eq("username", username));
		}
		if (StringUtils.isNotEmpty(bankname)){
			dc.add(Restrictions.eq("bankname", bankname));
		}
		if (StringUtils.isNotEmpty(vpnpassword)) {
			dc.add(Restrictions.like("vpnpassword", vpnpassword));
		}
		if (bankInfoType!=null){
			dc.add(Restrictions.eq("banktype", bankInfoType));
		}
		if(type != 0){
			dc.add(Restrictions.eq("type", type));
		}
		if(useable!=2){
			dc.add(Restrictions.eq("useable", useable));
		}
		//dc.add(Restrictions.eq("type", 1));
		Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, size, null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	@SuppressWarnings("unchecked")
	public String querymanagemsbankswitch(){
		DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
		/*Page page = PageQuery.queryForPagenation(bankinfoService.getHibernateTemplate(), dc, pageIndex, 50);
		getRequest().setAttribute("page", page);*/
		List<Const> list = operatorService.findByCriteria(dc);
		getRequest().setAttribute("list", list);
		return INPUT;
	}
	
	public String canclebankinfo() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
//			getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out=null;
		try {
			String msg = bankinfoService.cancle(getOperatorLoginname(),Integer.parseInt(jobPno),useable);
			out=this.getResponse().getWriter();
			if (msg == null)
				out.println("操作成功");
			else
				out.println("操作失败:" + msg);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("操作失败:" + e.getMessage());
			out.flush();
		}
		finally{
			if(out!=null){
				out.close();
			}
		}
//		return INPUT;
		return null;
	}
	
	public String switchmsbank() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
//			getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out=null;
		try {
			String msg = bankinfoService.switchmsBank(jobPno,useable);
			out=this.getResponse().getWriter();
			if (msg == null)
				out.println("操作成功");
			else
				out.println("操作失败:" + msg);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("操作失败:" + e.getMessage());
			out.flush();
		}
		finally{
			if(out!=null){
				out.close();
			}
		}
//		return INPUT;
		return null;
	}
	
	public String remotebankinfo()  {
		PrintWriter out = null;
		try {
			out=this.getResponse().getWriter();
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			String name = this.getOperatorLoginname();
			if (name==null) {
				out.println("请登录后在尝试操作" );
				return null;
			}
			Bankinfo bankinfo= (Bankinfo)bankinfoService.get(Bankinfo.class, Integer.parseInt(jobPno));
			if(bankinfo==null){
				out.println("您要操作的银行不存在" );
				return null;
			}
			if(StringUtils.isBlank(jobip)){
				out.println("请先设置你要远程操作的IP" );
				return null;
			}
			if(remotetype.equals("open")) 
			{
				String processCount =RemoteUtils.getRemoteStatus("count",jobip);
				if(Integer.parseInt(processCount)>=1)
				{
					out.println("返回消息：程序已开启，请先关闭" );
					return null;
				}
			}
			if(remotetype.equals("restart")) 
			{
				String processCount =RemoteUtils.getRemoteStatus("count",jobip);
				if(Integer.parseInt(processCount)>1)
				{
					out.println("返回消息：开启的程序已超过2个，请先关闭" );
					return null;
				}
			}
			String result =RemoteUtils.getRemoteStatus(remotetype,jobip);
			if(result.equals("success"))
			{
			out.println("返回消息：操作成功,程序正在运行，请稍等10秒" );
			}
			else{
				out.println("返回消息：操作异常");
			}
			
		} catch (Exception e) {
			//System.out.println("返回消息:" + e.getMessage());
			//out.println("返回消息:" + e.getMessage());
			out.println("返回消息：操作异常");
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			out.flush();
		}
		return null;
	}
	
	public String remotebankstatus(){
		String name = this.getOperatorLoginname();
		if (name==null) {
			GsonUtil.GsonObject("请登录后在尝试操作");
			return null;
		}
		Bankinfo bankinfo= (Bankinfo)bankinfoService.get(Bankinfo.class, Integer.parseInt(jobPno));
		if(bankinfo==null){
			GsonUtil.GsonObject("您要操作的银行不存在" );
			return null;
		}
		if(bankinfo.getIsactive() == 0){
			bankinfo.setIsactive(1);
		}else if(bankinfo.getIsactive() == 1){
			bankinfo.setIsactive(0);
		}
		bankinfoService.updateBankInfo(bankinfo);
		GsonUtil.GsonObject("修改成功" );
		return null ;
	}
	
	public void gtDepositAmount() {
		PrintWriter out = null;
		String msg = null;
		msg ="";
		try {
			// type=2 支付账户 useable = 0 启用
			if (!bankinfoService.gtDepositAmount(1, 0).equals("")) {
				msg = msg + "存款银行超过5000："
						+ bankinfoService.gtDepositAmount(1, 0) + "<br/>";
			}
			if (!bankinfoService.gtDepositTime(1, 0).equals("")) {
				msg = msg + "存款银行超时3分钟：" + bankinfoService.gtDepositTime(1, 0)
						+ "<br/>";
			}
			if (!bankinfoService.gtWithdrawTime(2, 0).equals("")) {
				msg = msg + "支付,中转银行超时15秒：" + bankinfoService.gtWithdrawTime(2, 0)
						+ "<br/>";
			}
			out = getResponse().getWriter();
			out.print(msg);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.print(msg);
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	public String searchforeditbankinfo(){
		String name = this.getOperatorLoginname();
		if (name==null) {
			this.errormsg="请登录后在尝试操作";
			return "logout";
		}
		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		Bankinfo bankinfo= (Bankinfo)bankinfoService.get(Bankinfo.class, Integer.parseInt(jobPno));
		if(bankinfo==null){
			this.setErrormsg("您要编辑的记录不存在");
			return Action.ERROR;
		}
		this.setBankname(bankinfo.getBankname());
		this.setUsername(bankinfo.getUsername());
		this.setRemark(bankinfo.getRemark());
		this.setId(bankinfo.getId());
		this.setType(bankinfo.getType());
		this.setVpnname(bankinfo.getVpnname());
		this.setVpnpassword(bankinfo.getVpnpassword());
		this.setAccountno(bankinfo.getAccountno());
		this.setLoginname(bankinfo.getLoginname());
		this.setPassword(bankinfo.getPassword());
		
		this.setSamebank(bankinfo.getSamebank());
		this.setCrossbank(bankinfo.getCrossbank());
		this.setTransfermoney(bankinfo.getTransfermoney());
		this.setAutopay(bankinfo.getAutopay());
		this.setFee(bankinfo.getFee());
		this.setPaytype(bankinfo.getPaytype());
		if(!operator.getAuthority().equals("boss") && null != bankinfo.getBankcard() && bankinfo.getBankcard().length()>3){
			this.setBankcard("****************"+bankinfo.getBankcard().substring(bankinfo.getBankcard().length()-3 , bankinfo.getBankcard().length()));
		}else{
			this.setBankcard(bankinfo.getBankcard());
		}
		this.setUsb(bankinfo.getUsb());
		this.setRealname(bankinfo.getRealname());
		this.setBankInfoType(bankinfo.getBanktype());
		this.setRemoteip(bankinfo.getRemoteip());
		this.setZfbImgCode(bankinfo.getZfbImgCode());
		this.setScanAccount(bankinfo.getScanAccount());
		this.setDepositMin(bankinfo.getDepositMax());
		this.setDepositMax(bankinfo.getDepositMin());
		userrole=bankinfo.getUserrole();
		if(userrole==null){
			userrole="";
		}
		return "input";
	}
	
	public String updageuserrole(){
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			String name = this.getOperatorLoginname();
			if (name==null) {
				out.println("请登录后在尝试操作" );
				out.flush();
				return null;
			}
			Bankinfo bankinfo= (Bankinfo)bankinfoService.get(Bankinfo.class, Integer.parseInt(jobPno));
			if(bankinfo==null){
				out.println("您要编辑的记录不存在！" );
				out.flush();
				return null;
			}
			String userroleOld=bankinfo.getUserrole();
			if(userroleOld==null){
				userroleOld="";
			}
			if(userroleOld.contains(userrole)){
				userroleOld=userroleOld.replace(userrole, "");
				bankinfo.setUserrole(userroleOld);
			}else{
				bankinfo.setUserrole(userroleOld+userrole+"");
			}
			bankinfoService.updateUserrole(String.valueOf(bankinfo.getUserrole()),Integer.parseInt(String.valueOf(bankinfo.getId())));
			out=this.getResponse().getWriter();
			out.println("返回消息:成功！" );
			out.flush();
		} catch (Exception e) {
			out.println("返回消息:" + e.getMessage());
			out.flush();
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
		return null;
	}
	
	public String updageNewShow(){
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			String name = this.getOperatorLoginname();
			if (name==null) {
				out.println("请登录后在尝试操作" );
				out.flush();
				return null;
			}
			Bankinfo bankinfo= (Bankinfo)bankinfoService.get(Bankinfo.class, Integer.parseInt(jobPno));
			if(bankinfo==null){
				out.println("您要编辑的记录不存在！" );
				out.flush();
				return null;
			}
			Integer isshow=bankinfo.getIsshow();
			if(isshow==null){
				isshow=0;
			}
			if(isshow==1){
				bankinfo.setIsshow(0);
			}else{
				bankinfo.setIsshow(1);
			}
			bankinfoService.updateIsshow(getOperatorLoginname(),bankinfo.getIsshow(),bankinfo.getId(),bankinfo.getUsername());
			out=this.getResponse().getWriter();
			out.println("返回消息:成功！" );
			out.flush();
		} catch (Exception e) {
			out.println("返回消息:" + e.getMessage());
			out.flush();
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
		return null;
	}
	
	public String updageNewTransfer(){
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			String name = this.getOperatorLoginname();
			if (name==null) {
				out.println("请登录后在尝试操作" );
				out.flush();
				return null;
			}
			Bankinfo bankinfo= (Bankinfo)bankinfoService.get(Bankinfo.class, Integer.parseInt(jobPno));
			if(bankinfo==null){
				out.println("您要编辑的记录不存在！" );
				out.flush();
				return null;
			}
			Integer istransfer=bankinfo.getIstransfer();
			if(istransfer==null){
				istransfer=0;
			}
			if(istransfer==1){
				bankinfo.setIstransfer(0);
			}else{
				bankinfo.setIstransfer(1);
			}
			bankinfoService.updateIstransfer(getOperatorLoginname(),bankinfo.getIstransfer(),bankinfo.getId(),bankinfo.getUsername());
			out=this.getResponse().getWriter();
			out.println("返回消息:成功！" );
			out.flush();
		} catch (Exception e) {
			out.println("返回消息:" + e.getMessage());
			out.flush();
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
		return null;
	}
	
	public String getSaveBankAccount(){
		try {
			bankinfos = bankinfoService.getBankinfo(1);
			Set bankTypeMap = new HashSet();
			
			for (int i = 0;i < bankinfos.size(); i++) {
				bankTypeMap.add(bankinfos.get(i).getBanktype());
			}
			List<BankVo> bankTypelist = new ArrayList<BankVo>();
			Iterator bankTypeIter = bankTypeMap.iterator();
			BankVo bankVo = null;
			while (bankTypeIter.hasNext()) {
				bankVo = new BankVo();
				bankVo.setType(Integer.parseInt(bankTypeIter.next().toString()));
				bankTypelist.add(bankVo);
			}
			
			Set typeMap = null;
			Iterator typeIter = null; 
			BankVo typeBbankVo = null;
			List<BankVo> typeList = null;
			int bankType1 = 0;
			int bankType2 = 0;
			for (int k = 0;k < bankTypelist.size(); k++) { 
				typeMap = new HashSet();
				typeList = new ArrayList<BankVo>();
				for (int j = 0; j < bankinfos.size(); j++) { 
					 bankType1 = bankinfos.get(j).getBanktype();
					 bankType2 = bankTypelist.get(k).getType(); 
					 if (bankType1 == bankType2) {
						 typeMap.add(bankinfos.get(j).getType()+"");
					 }
				}
				typeIter = typeMap.iterator();
				
				while (typeIter.hasNext()) {
					typeBbankVo = new BankVo();
					typeBbankVo.setType(Integer.parseInt(typeIter.next().toString()));
					typeList.add(typeBbankVo);
				} 
				bankTypelist.get(k).setItem(typeList); 
			}
			List<BankVo> bankList = null;
			BankVo bankv = null;
			List<BankVo> bankListVo = null;
			int bank1 = 0;
			int bank2 = 0;
			for (int i = 0;i < bankTypelist.size(); i++) {
				if (bankTypelist != null && bankTypelist.get(i).getItem() != null){
					bankList = bankTypelist.get(i).getItem();
					for (int j = 0;j < bankList.size(); j++) {
						bankListVo = new ArrayList<BankVo>();
						for (int k = 0; k < bankinfos.size(); k++) { 
							bankType1 = bankinfos.get(k).getBanktype();
							bankType2 = bankTypelist.get(i).getType();
							bank1 = bankinfos.get(k).getType();
							bank2 = bankList.get(j).getType();
							
							 if (bank1 == bank2  && bankType1 == bankType2) {
								 bankv = new BankVo();
								 bankv.setId(bankinfos.get(k).getId());
								 bankv.setType(bankinfos.get(k).getType());
								 bankv.setName(bankinfos.get(k).getRemark());
								 bankListVo.add(bankv);
							 }
						}
						bankList.get(j).setItem(bankListVo);
					}
				}
			}
			getRequest().setAttribute("bankTypeList", bankTypelist);
		} catch (Exception e) {
			e.printStackTrace();	
		}
		return SUCCESS;
	}
	
	public String getDefrayBankAccount(){
		try {
			bankinfos = bankinfoService.getBankinfo(2);
		} catch (Exception e) {
			e.printStackTrace();	
		}
		return SUCCESS;
	}
	
	public String getBusinessBankAccount(){
		try {
			bankinfos = bankinfoService.getBusinessBankinfo();
		} catch (Exception e) {
			e.printStackTrace();	
		}
		return SUCCESS;
	}
	
	public String getAllBankAccount(){
		try {
			bankinfos = bankinfoService.getAllBankinfo();
		} catch (Exception e) {
			e.printStackTrace();	
		}
		return SUCCESS;
	}
	
	public String getCashinBankAccount(){
		try {
			bankinfos = bankinfoService.getCashinBankinfo();
			Set bankTypeMap = new HashSet();
			
			for (int i = 0;i < bankinfos.size(); i++) {
				bankTypeMap.add(bankinfos.get(i).getBanktype());
			}
			List<BankVo> bankTypelist = new ArrayList<BankVo>();
			Iterator bankTypeIter = bankTypeMap.iterator();
			BankVo bankVo = null;
			while (bankTypeIter.hasNext()) {
				bankVo = new BankVo();
				bankVo.setType(Integer.parseInt(bankTypeIter.next().toString()));
				bankTypelist.add(bankVo);
			}
			
			Set typeMap = null;
			Iterator typeIter = null; 
			BankVo typeBbankVo = null;
			List<BankVo> typeList = null;
			int bankType1 = 0;
			int bankType2 = 0;
			for (int k = 0;k < bankTypelist.size(); k++) { 
				typeMap = new HashSet();
				typeList = new ArrayList<BankVo>();
				for (int j = 0; j < bankinfos.size(); j++) { 
					 bankType1 = bankinfos.get(j).getBanktype();
					 bankType2 = bankTypelist.get(k).getType(); 
					 if (bankType1 == bankType2) {
						 typeMap.add(bankinfos.get(j).getType()+"");
					 }
				}
				typeIter = typeMap.iterator();
				
				while (typeIter.hasNext()) {
					typeBbankVo = new BankVo();
					typeBbankVo.setType(Integer.parseInt(typeIter.next().toString()));
					typeList.add(typeBbankVo);
				} 
				bankTypelist.get(k).setItem(typeList); 
			}
			List<BankVo> bankList = null;
			BankVo bankv = null;
			List<BankVo> bankListVo = null;
			int bank1 = 0;
			int bank2 = 0;
			for (int i = 0;i < bankTypelist.size(); i++) {
				if (bankTypelist != null && bankTypelist.get(i).getItem() != null){
					bankList = bankTypelist.get(i).getItem();
					for (int j = 0;j < bankList.size(); j++) {
						bankListVo = new ArrayList<BankVo>();
						for (int k = 0; k < bankinfos.size(); k++) { 
							bankType1 = bankinfos.get(k).getBanktype();
							bankType2 = bankTypelist.get(i).getType();
							bank1 = bankinfos.get(k).getType();
							bank2 = bankList.get(j).getType();
							
							 if (bank1 == bank2  && bankType1 == bankType2) {
								 bankv = new BankVo();
								 bankv.setId(bankinfos.get(k).getId());
								 bankv.setType(bankinfos.get(k).getType());
								 bankv.setName(bankinfos.get(k).getRemark());
								 bankListVo.add(bankv);
							 }
						}
						bankList.get(j).setItem(bankListVo);
					}
				}
			}
			getRequest().setAttribute("bankTypeList2", bankTypelist);
		} catch (Exception e) {
			e.printStackTrace();	
		}
		return SUCCESS;
	}
	
	
	private String merid;
	private String constid;
	private String merchantcode;
	private String note;
	/**
	 * 新增或者修改商户号
	 * @return
	 */
	public String updatepaymerchant(){
		PayMerchant paymer=null;
		if(!StringUtils.isEmpty(merid)){
		merid=merid.replace("ids", "");
		paymer=(PayMerchant) bankinfoService.get(PayMerchant.class, merid);
		}
		if(null==paymer||paymer.getId()==null){
			paymer = new PayMerchant();
			paymer.setCreatetime(new Date());
		}
		paymer.setConstid(constid);
		paymer.setMerchantcode(merchantcode);
		paymer.setNote(note);
		paymer.setUpdatetime(new Date());
		String a="";
		if(null!=paymer.getId()){
		bankinfoService.updatePayMerchant(paymer);
		}else {
			a=bankinfoService.savePayMerchant(paymer);
		}
		PrintWriter out=null;
		try {
			out =this.getResponse().getWriter();
			out.println("操作成功##"+a);
			out.flush();
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			out.flush();
		}
		
		return null;
	}
	
	public void deletepaymerchant(){
		merid=merid.replace("ids", "");
		bankinfoService.deletePayMerchant(PayMerchant.class, Integer.parseInt(merid));
		PrintWriter out=null;
		try {
			out =this.getResponse().getWriter();
			out.println("删除成功");
			out.flush();
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			out.flush();
		}
	}
	
    private ObjectMapper JSON = new ObjectMapper();

    public String merchantpay_main() {
        Operator cus = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
        if (cus != null) {
            getRequest().setAttribute("loginname", cus.getUsername());
        }
        return INPUT;
    }

    public String merchantpay_finance() {
        Operator cus = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
        if (cus != null) {
            getRequest().setAttribute("loginname", cus.getUsername());
        }
        return INPUT;
    }
	
    public String merchantpay_query() {
        String result = "";
        Operator cus = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
        if (cus != null) {
            String url = rb.getString("middleservice.url.pay") + "merchantPay/query";
            try {
                Map param = getRequest().getParameterMap();
                result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(param));
                log.info("接收结果：" + result);
                GsonUtil.GsonObject(result);
            } catch (Exception e) {
                log.error("调用接口异常：", e);
            }
        }
        return null;
    }

    public String merchantpay_add_update() {
        String result = "";
        Operator cus = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
        if (cus != null) {
            String url = rb.getString("middleservice.url.pay") + "merchantPay/add_update";
            try {
            	Map param = MyWebUtils.getRequestParameters(getRequest());
				result = MyWebUtils.getHttpContentByParam(url, getListNamevaluepair(param));
                log.info("接收结果：" + result);
            } catch (Exception e) {
                log.error("调用接口异常：", e);
            }
            getRequest().setAttribute("loginname", cus.getUsername());
        }
        GsonUtil.GsonObject(result);
        return null;
    }

    public static List<NameValuePair> getListNamevaluepair(Map<String, String[]> map) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return formparams;
    }
    
    public String merchantpay_edit() {
        String result = "";
        Operator cus = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
        MerchantPay vo = new MerchantPay();
        if (cus != null) {
            String url = rb.getString("middleservice.url.pay") + "merchantPay/get";
            try {
                Map param = getRequest().getParameterMap();
                result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(param));
                log.info("接收结果：" + result);

                vo = JSON.readValue(result, MerchantPay.class);
            } catch (Exception e) {
                log.error("调用接口异常：", e);
            }
            getRequest().setAttribute("loginname", cus.getUsername());
        }
        getRequest().setAttribute("vo", vo);

        return SUCCESS;
    }

    public String supplement_main() {
        String result = "";
        Operator cus = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
        List<MerchantPay> merchantPays = new ArrayList<>();
        if (cus != null) {
            String url = rb.getString("middleservice.url.pay") + "merchantPay/getPayWay";
            try {
                result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair("useable", "1"));
                log.info("接收结果：" + result);

                merchantPays = JSON.readValue(result, new TypeReference<List<MerchantPay>>() {
                });
            } catch (Exception e) {
                log.error("调用接口异常：", e);
            }
            getRequest().setAttribute("loginname", cus.getUsername());
        }
        getRequest().setAttribute("pays", merchantPays);
        return INPUT;
    }

    public String supplement_save() {
        String result = "";
        Response response;
        Operator cus = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
        if (cus != null) {
            String url = rb.getString("middleservice.url.pay") + "payOrder/supplement";
            try {
                Map param = getRequest().getParameterMap();
                result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(param));
                log.info("接收结果：" + result);

                response = JSON.readValue(result, Response.class);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                log.error("调用接口异常：", e);
            }
            getRequest().setAttribute("loginname", cus.getUsername());
        }
        return null;
    }
	
	public String getMerid() {
		return merid;
	}

	public void setMerid(String merid) {
		this.merid = merid;
	}

	public String getConstid() {
		return constid;
	}

	public void setConstid(String constid) {
		this.constid = constid;
	}

	public String getMerchantcode() {
		return merchantcode;
	}

	public void setMerchantcode(String merchantcode) {
		this.merchantcode = merchantcode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public IBankinfoService getBankinfoService() {
		return bankinfoService;
	}
	public void setBankinfoService(IBankinfoService bankinfoService) {
		this.bankinfoService = bankinfoService;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getJobPno() {
		return jobPno;
	}

	public void setJobPno(String jobPno) {
		this.jobPno = jobPno;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Bankinfo> getBankinfos() {
		return bankinfos;
	}

	public void setBankinfos(List<Bankinfo> bankinfos) {
		this.bankinfos = bankinfos;
	}

	public Integer getUseable() {
		return useable;
	}

	public void setUseable(Integer useable) {
		this.useable = useable;
	}

	public String getVpnname() {
		return vpnname;
	}

	public void setVpnname(String vpnname) {
		this.vpnname = vpnname;
	}

	public String getVpnpassword() {
		return vpnpassword;
	}

	public void setVpnpassword(String vpnpassword) {
		this.vpnpassword = vpnpassword;
	}

	public String getAccountno() {
		return accountno;
	}

	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}

	public String getUserrole() {
		return userrole;
	}

	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}

	public Integer getIsshow() {
		return isshow;
	}

	public void setIsshow(Integer isshow) {
		this.isshow = isshow;
	}


	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getBankInfoType() {
		return bankInfoType;
	}

	public void setBankInfoType(Integer bankInfoType) {
		this.bankInfoType = bankInfoType;
	}

	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getUsb() {
		return usb;
	}

	public void setUsb(String usb) {
		this.usb = usb;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getRemotetype() {
		return remotetype;
	}

	public void setRemotetype(String remotetype) {
		this.remotetype = remotetype;
	}

	public String getJobip() {
		return jobip;
	}

	public void setJobip(String jobip) {
		this.jobip = jobip;
	}

	public String getRemoteip() {
		return remoteip;
	}

	public void setRemoteip(String remoteip) {
		this.remoteip = remoteip;
	}

	public String getIstransfer() {
		return istransfer;
	}

	public void setIstransfer(String istransfer) {
		this.istransfer = istransfer;
	}

	public OperatorService getOperatorService() {
		return operatorService;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}

	public String getSamebank() {
		return samebank;
	}

	public void setSamebank(String samebank) {
		this.samebank = samebank;
	}

	public String getCrossbank() {
		return crossbank;
	}

	public void setCrossbank(String crossbank) {
		this.crossbank = crossbank;
	}

	public Double getTransfermoney() {
		return transfermoney;
	}

	public void setTransfermoney(Double transfermoney) {
		this.transfermoney = transfermoney;
	}

	public Integer getAutopay() {
		return autopay;
	}

	public void setAutopay(Integer autopay) {
		this.autopay = autopay;
	}

	public String getSwitchtype() {
		return switchtype;
	}

	public void setSwitchtype(String switchtype) {
		this.switchtype = switchtype;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getZfbImgCode() {
		return zfbImgCode;
	}

	public void setZfbImgCode(String zfbImgCode) {
		this.zfbImgCode = zfbImgCode;
	}

	public Integer getScanAccount() {
		return scanAccount;
	}

	public void setScanAccount(Integer scanAccount) {
		this.scanAccount = scanAccount;
	}
	
    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }
    
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
    
	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}
	
	
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	

	
	
}
