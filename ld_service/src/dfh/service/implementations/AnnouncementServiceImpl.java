package dfh.service.implementations;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.AnnouncementVO;
import dfh.action.vo.QueryDataEuroVO;
import dfh.dao.AnnouncementDao;
import dfh.dao.BankinfoDao;
import dfh.dao.LogDao;
import dfh.dao.TradeDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Announcement;
import dfh.model.Const;
import dfh.model.GiftOrder;
import dfh.model.PayMerchant;
import dfh.model.Payorder;
import dfh.model.Payorderbillono;
import dfh.model.Users;
import dfh.model.enums.CommonGfbEnum;
import dfh.model.enums.CommonZfEnum;
import dfh.model.enums.CreditChangeType;
import dfh.service.interfaces.AnnouncementService;
import dfh.skydragon.webservice.model.WinPointInfo;
import dfh.utils.Arith;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.Page;
import dfh.utils.PagenationUtil;
import dfh.utils.StringUtil;

public class AnnouncementServiceImpl implements AnnouncementService {
	
	private static Logger log = Logger.getLogger(AnnouncementServiceImpl.class); 

	private LogDao logDao;
	private AnnouncementDao annDao;
	private TradeDao tradeDao;
	private BankinfoDao bankinfoDao;

	public AnnouncementDao getAnnDao() {
		return annDao;
	}


	public void setAnnDao(AnnouncementDao annDao) {
		this.annDao = annDao;
	}


	public AnnouncementServiceImpl() {
	}

	
	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}



	public List query() {
		// TODO Auto-generated method stub
		Iterator it = annDao.query().iterator();
		List<AnnouncementVO> list=new ArrayList<AnnouncementVO>();
		while(it.hasNext()){
			Announcement anns=(Announcement) it.next();
			list.add(new AnnouncementVO( anns.getId(),anns.getContent() ,DateUtil.fmtyyyy_MM_d(anns.getCreatetime()), StringUtil.subString(anns.getTitle(), 46)));
		}
		
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List querybyPage(int first,int maxresult) {
		// TODO Auto-generated method stub
		Iterator it = annDao.querybyPage(first,maxresult).iterator();
		List<AnnouncementVO> list=new ArrayList<AnnouncementVO>();
		while(it.hasNext()){
			Announcement anns=(Announcement) it.next();
			list.add(new AnnouncementVO( anns.getId(),anns.getContent() ,DateUtil.fmtyyyy_MM_d(anns.getCreatetime()), StringUtil.subString(anns.getTitle(), 46)));
		}
		
		return list;
	}
	
	public List queryTopNews(){
		Iterator it = annDao.queryTopNews().iterator();
		List<AnnouncementVO> list=new ArrayList<AnnouncementVO>();
		while(it.hasNext()){
			Announcement anns=(Announcement) it.next();
			list.add(new AnnouncementVO( anns.getId(), DateUtil.fmtyyyy_MM_d(anns.getCreatetime()), StringUtil.subString(anns.getTitle(), 60)));
		}
		return list;
	}



	public List queryAll(int pageNumber, int length) {
		// TODO Auto-generated method stub
		int offset=(pageNumber-1)*length;
		List queryAll = annDao.queryAll(offset, length);
		List<AnnouncementVO> list=new ArrayList<AnnouncementVO>();
		if (queryAll==null) {
			return list;
		}
		Iterator it = queryAll.iterator();
		while(it.hasNext()){
			Announcement anns=(Announcement) it.next();
			list.add(new AnnouncementVO( anns.getId(), DateUtil.fmtyyyy_MM_d(anns.getCreatetime()), anns.getTitle()));
		}
		return list;
	}



	public int totalCount() {
		// TODO Auto-generated method stub
		return annDao.totalCount();
	}

	
	public AnnouncementVO getAnnouncement(int aid) {
		// TODO Auto-generated method stub
		Object o = annDao.get(Announcement.class, aid);
		if (o!=null) {
			Announcement ann= (Announcement)o;
			return new AnnouncementVO(ann.getId(), ann.getContent(), DateUtil.fmtyyyy_MM_d(ann.getCreatetime()), ann.getTitle());
			
		}
		return null;
	}
	
	@Override
	public String addPayorder(String billno, Double money,String loginname, String msg,String date, String type) {
		String returnmsg=null;
		if(annDao.get(Payorder.class,billno)==null){
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("id", "环迅"));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
	
				// 后台是否关闭此 存款
				if (type.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "在线支付正在维护！";
				}
				Users users = (Users)annDao.get(Users.class,loginname);
				Payorder payorder=new Payorder();
				payorder.setBillno(billno);
				payorder.setPayPlatform("ips");
				payorder.setFlag(0);
				payorder.setNewaccount(Constants.FLAG_FALSE);
				payorder.setLoginname(loginname);
				payorder.setAliasName(users.getAccountName());
				payorder.setMoney(money);
				payorder.setPhone(users.getPhone());
				payorder.setEmail(users.getEmail());
				payorder.setCreateTime(DateUtil.now());
				payorder.setMsg("ips单号:"+msg);
				annDao.save(payorder);
				tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "ips单号:"+msg);
				bankinfoDao.changeAmountOnline(4, money,billno);
			
		}else{
			returnmsg="此笔交易已经支付成功";
		}
		
		
		return returnmsg;
	}
	
	 /**
     * @description 将xml字符串转换成map
     * @param xml
     * @return Map
     */
    public static Map readStringXmlOut(String xml) { 
    	Map map = new HashMap();
        Document doc = null;
        try {
            // 将字符串转为XML
            doc = DocumentHelper.parseText(xml); 
            // 获取根节点
            Element rootElt = doc.getRootElement(); 

            // 获取根节点下的子节点head
            Iterator iter = rootElt.elementIterator("response"); 
            // 遍历head节点
            while (iter.hasNext()) {

                Element recordEle = (Element) iter.next();
                // 拿到is_success节点下的子节点title值
                String is_success = recordEle.elementTextTrim("is_success");  
                map.put("is_success", is_success);
                
                // 拿到is_success节点下的子节点title值
                String sign_type = recordEle.elementTextTrim("sign_type");  
                map.put("sign_type", sign_type);
                
                // 拿到is_success节点下的子节点title值
                String sign = recordEle.elementTextTrim("sign");  
                map.put("sign", sign);
                 
                // 获取子节点head下的子节点script
                Iterator iters = recordEle.elementIterator("trade"); 
                // 遍历Header节点下的Response节点
                while (iters.hasNext()) {
                    Element itemEle = (Element) iters.next();
                    // 拿到head下的子节点script下的字节点username的值
                    String merchant_code = itemEle.elementTextTrim("merchant_code"); 
                    String order_amount = itemEle.elementTextTrim("order_amount");
                    String order_no = itemEle.elementTextTrim("order_no"); 
                    String order_time = itemEle.elementTextTrim("order_time");
                    String trade_no = itemEle.elementTextTrim("trade_no"); 
                    String trade_status = itemEle.elementTextTrim("trade_status");
                    String trade_time = itemEle.elementTextTrim("trade_time");

                    map.put("merchant_code", merchant_code);
                    map.put("merchant_code", merchant_code);
                    map.put("order_no", order_no);
                    map.put("order_time", order_time);
                    map.put("trade_no", trade_no);
                    map.put("trade_status", trade_status);
                    map.put("trade_time", trade_time); 
                }
            } 
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    
    /*******
     * 通用智付1 and 通用智付2
     */
	public String addPayorderZf(String billno, Double money,String loginname, String msg,String date,String merchant_code, String type) {
		String returnmsg=null;  
		if(loginname.contains("_flag_")){
			loginname = loginname.split("_flag_")[0] ;
		}
		if(annDao.get(Payorder.class,billno)==null){
				 
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("shopno", merchant_code));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
	
				// 后台是否关闭此 存款
				if (type.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "在线支付正在维护！";
				}
				CommonZfEnum cmzf = null;
				Users users = (Users)annDao.get(Users.class,loginname);
				Payorder payorder=new Payorder();
				payorder.setBillno(billno);
				if(merchant_code.equals("2030028802")){ 
					payorder.setPayPlatform("zf");
					payorder.setMsg("zf单号:"+msg);
				}else if(merchant_code.equals("2030000006")){
					payorder.setPayPlatform("zf1");
					payorder.setMsg("zf1单号:"+msg); 
				}else if(merchant_code.equals("2030020118")){ 
					payorder.setPayPlatform("zf2");
					payorder.setMsg("zf2单号:"+msg);
				}else if(merchant_code.equals("2030020119")){ 
					payorder.setPayPlatform("zf3");
					payorder.setMsg("zf3单号:"+msg);
				}else{
					cmzf = CommonZfEnum.getCommonZfByMerchantCode(merchant_code);
					if(cmzf != null){
						payorder.setPayPlatform(cmzf.getCode());
						payorder.setMsg(cmzf.getCode()+"单号:"+msg);
					}
				}
				payorder.setFlag(0);
				payorder.setNewaccount(Constants.FLAG_FALSE);
				payorder.setLoginname(loginname);
				payorder.setAliasName(users.getAccountName());
				payorder.setMoney(money);
				payorder.setPhone(users.getPhone());
				payorder.setEmail(users.getEmail());
				payorder.setCreateTime(DateUtil.now());
				annDao.save(payorder);
				//更新用户存款方式
				annDao.updateUserSqlIsCashinOnline(loginname) ;
				if(merchant_code.equals("2030028802")){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "zf单号:"+msg);
					bankinfoDao.changeZfAmountOnline(40, money,billno);
				}else if(merchant_code.equals("2030000006")){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "zf1单号:"+msg);
					bankinfoDao.changeZf1AmountOnline(41, money,billno);
				}else if(merchant_code.equals("2030020118")){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "zf2单号:"+msg);
					bankinfoDao.changeZfAmountOnlineTwo(42, money,billno);
				}else if(merchant_code.equals("2030020119")){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "zf3单号:"+msg);
					bankinfoDao.changeZfAmountOnlineTwo(43, money,billno);
				}else if(cmzf != null){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, cmzf.getCode()+"单号:"+msg);
					bankinfoDao.changeCommonZfAmountOnline(cmzf, money,billno);
				}
		}else{
			returnmsg="此笔交易已经支付成功";
		}
		
		
		return returnmsg;
	}
	
	public String addPayorderDianKaZf(String billno, Double money,String loginname, String msg,String date,String merchant_code, String type) {
		System.out.println("==============智付微信 and 支付点卡 回调webservice============\n");
		String returnmsg=null;  
		String [] args ;
		String diankaCode = null ;
		if(loginname.contains("_flag_")){
			args = loginname.split("_flag_") ;
			loginname = args[0];
			diankaCode = args[1];
		}
		if(annDao.get(Payorder.class,billno)==null){
				 
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("shopno", merchant_code));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
	
				// 后台是否关闭此 存款
				if (type.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "在线支付正在维护！";
				}
				System.out.println(merchant_code+"====="+diankaCode);
				
				Users users = (Users)annDao.get(Users.class,loginname);
				Payorder payorder=new Payorder();
				payorder.setBillno(billno);
				if(merchant_code.equals("2030028802")){
					payorder.setPayPlatform("dk");
					payorder.setMsg("zf点卡单号:"+msg+diankaCode);
				}else if(merchant_code.equals("2030000006")){
					payorder.setPayPlatform("dk1");
					payorder.setMsg("zf点卡单号:"+msg+diankaCode); 
				}else if(merchant_code.equals("2030020118")){ 
					payorder.setPayPlatform("dk2");
					payorder.setMsg("zf点卡单号:"+msg+diankaCode);   
				}else if(merchant_code.equals("2030020119")){
					payorder.setPayPlatform("dk3");
					payorder.setMsg("zf点卡单号:"+msg+diankaCode);
				}else if(merchant_code.equals("2000295555") && !diankaCode.equals("ZFWX")){     
					payorder.setPayPlatform("zfdk");   
					payorder.setMsg("zfdk点卡单号:"+msg+diankaCode);
				}else if(merchant_code.equals("2000295666") && diankaCode.equals("ZFWX")){   
					payorder.setPayPlatform("zfwx");
					payorder.setMsg("智付微信单号:"+msg+diankaCode);     
				}
				
				Double percent = null ;
				if(diankaCode.equals("YDSZX")){
					percent =  0.05;
				}else if(diankaCode.equals("DXGK")){
					percent =  0.05;
				}else if(diankaCode.equals("QBCZK")){
					percent =  0.14;
				}else if(diankaCode.equals("LTYKT")){
					percent =  0.05;
				}else if(diankaCode.equals("JWYKT")){
					percent =  0.16;
				}else if(diankaCode.equals("SDYKT")){
					percent =  0.13;
				}else if(diankaCode.equals("WMYKT")){
					percent =  0.14;
				}else if(diankaCode.equals("ZTYKT")){
					percent =  0.13;
				}else if(diankaCode.equals("WYYKT")){
					percent =  0.14;
				}else if(diankaCode.equals("SFYKT")){
					percent =  0.14;
				}else if(diankaCode.equals("SHYKT")){
					percent =  0.16;
				}else if(diankaCode.equals("JYYKT")){
					percent =  0.2;
				}else if(diankaCode.equals("THYKT")){
					percent =  0.17;       
				}else if(diankaCode.equals("GYYKT")){
					percent =  0.17;     
				}else if(diankaCode.equals("TXYKT")){
					percent =  0.18;
				}else if(diankaCode.equals("ZYYKT")){
					percent =  0.16;
				}else if(diankaCode.equals("TXYKTZX")){
					percent =  0.19;
				}else if(diankaCode.equals("ZFWX")){
					percent =  0.008;
				}
				Double oldMoney = money ;
				money=money*(1-percent);
				payorder.setFlag(0);
				payorder.setNewaccount(Constants.FLAG_FALSE);
				payorder.setLoginname(loginname);
				payorder.setAliasName(users.getAccountName());
				if(!diankaCode.equals("ZFWX")){
				    payorder.setMoney(money);
				}else{
					payorder.setMoney(oldMoney);
				}
				payorder.setPhone(users.getPhone());
				payorder.setEmail(users.getEmail());
				payorder.setCreateTime(DateUtil.now());
				annDao.save(payorder);
				//更新用户存款方式
				annDao.updateUserSqlIsCashinOnline(loginname) ;
				if(merchant_code.equals("2030028802")){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "zf单号:"+msg);
					bankinfoDao.changeZfDianKaAmountOnline(4000, money,billno);
				}else if(merchant_code.equals("2030000006")){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "zf1单号:"+msg);
					bankinfoDao.changeZfDianKaAmountOnline(4001, money,billno);
				}else if(merchant_code.equals("2030020118")){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "zf2单号:"+msg);
					bankinfoDao.changeZfDianKaAmountOnline(4002, money,billno);
				}else if(merchant_code.equals("2030020119")){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "zf3单号:"+msg);
					bankinfoDao.changeZfDianKaAmountOnline(4003, money,billno);
				}else if(merchant_code.equals("2000295666")){
					if(diankaCode.equals("ZFWX")){
						Double amount4user = Arith.mul(oldMoney, 0.992);
						tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), billno, "zfwx单号:"+msg);
						bankinfoDao.changeZfDianKaAmountOnline(413, money,billno);
					}else{
						tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "zfdk单号:"+msg);
						bankinfoDao.changeZfDianKaAmountOnline(4010, money,billno);
					}
				}
		}else{
			returnmsg="此笔交易已经支付成功";
		}   
		return returnmsg;
	}
	
	@Override
	public String addPayorder4DCard(String billno, Double money, String loginname, String card_code, String merchant_code, String type) {
		String returnmsg = null;  
		String diankaCode = card_code ;
		
		if(annDao.get(Payorder.class,billno)==null){
				 
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("shopno", merchant_code));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
	
				// 后台是否关闭此 存款
				if (type.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "在线支付正在维护！";
				}
				
				Users users = (Users)annDao.get(Users.class,loginname);
				Payorder payorder=new Payorder();
				payorder.setBillno(billno);
				if(merchant_code.equals("2000295548")){ 
					payorder.setPayPlatform("zfdk1");
					payorder.setMsg("zfdk1点卡单号:" + billno);
				}
				
				Double percent = null ;
				if(diankaCode.equals("YDSZX")){
					percent =  0.05;
				}else if(diankaCode.equals("DXGK")){
					percent =  0.05;
				}else if(diankaCode.equals("QBCZK")){
					percent =  0.14;
				}else if(diankaCode.equals("LTYKT")){
					percent =  0.05;
				}else if(diankaCode.equals("JWYKT")){
					percent =  0.16;
				}else if(diankaCode.equals("SDYKT")){
					percent =  0.13;
				}else if(diankaCode.equals("WMYKT")){
					percent =  0.14;
				}else if(diankaCode.equals("ZTYKT")){
					percent =  0.13;
				}else if(diankaCode.equals("WYYKT")){
					percent =  0.14;
				}else if(diankaCode.equals("SFYKT")){
					percent =  0.14;
				}else if(diankaCode.equals("SHYKT")){
					percent =  0.16;
				}else if(diankaCode.equals("JYYKT")){
					percent =  0.2;
				}else if(diankaCode.equals("THYKT")){
					percent =  0.17;
				}else if(diankaCode.equals("GYYKT")){
					percent =  0.17;
				}else if(diankaCode.equals("TXYKT")){
					percent =  0.18;
				}else if(diankaCode.equals("ZYYKT")){
					percent =  0.16;
				}else if(diankaCode.equals("TXYKTZX")){
					percent =  0.19;
				}else if(diankaCode.equals("ZFWX")){
					percent =  0.009;
				}
				Double oldMoney = money ;
				money=money*(1-percent);
				payorder.setFlag(0);
				payorder.setNewaccount(Constants.FLAG_FALSE);
				payorder.setLoginname(loginname);
				payorder.setAliasName(users.getAccountName());
				if(!diankaCode.equals("ZFWX")){
				    payorder.setMoney(money);
				}else{
					payorder.setMoney(oldMoney);
				}
				payorder.setPhone(users.getPhone());
				payorder.setEmail(users.getEmail());
				payorder.setCreateTime(DateUtil.now());
				annDao.save(payorder);
				//更新用户存款方式
				annDao.updateUserSqlIsCashinOnline(loginname) ;
				if(merchant_code.equals("2000295548")){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "zfdk1 2000295548");
					bankinfoDao.changeZfDianKaAmountOnline(4011, money,billno);
				}
		}else{
			returnmsg="此笔交易已经支付成功";
		}
		return returnmsg;
	}
	
	
	
	/*********
	 * 海尔支付
	 */
	public String addPayorderHaier(String billno, Double money,String loginname,String merchant_code, String type) {
		String returnmsg=null;  
		if(annDao.get(Payorder.class,billno)==null){
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("shopno", merchant_code));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
	
				// 后台是否关闭此 存款
				if (type.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "在线支付正在维护！";
				}
				
				Users users = (Users)annDao.get(Users.class,loginname);
				if(null == users || null == users.getAccountName()){
					System.out.println(loginname+"******************************查询信息为空****************************************");
				}
				Payorder payorder=new Payorder();
				payorder.setBillno(billno);
				if(merchant_code.equals("200002058681")){ 
					payorder.setPayPlatform("haier");
					payorder.setMsg("海尔支付单号:"+billno);
				}
				
				payorder.setFlag(0);
				payorder.setNewaccount(Constants.FLAG_FALSE);
				payorder.setLoginname(loginname);
				payorder.setAliasName(users.getAccountName());  
				payorder.setMoney(money);
				payorder.setPhone(users.getPhone());
				payorder.setEmail(users.getEmail());
				payorder.setCreateTime(DateUtil.now());
				annDao.save(payorder);
				//更新用户存款方式
				annDao.updateUserSqlIsCashinOnline(users.getLoginname());
				if(merchant_code.equals("200002058681")){
					tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "海尔支付单号:"+billno);
					bankinfoDao.changeHaierAmountOnline(472, money,billno);
				}
		}else{
			returnmsg="此笔交易已经支付成功";
		}
		return returnmsg;
	}
	
	
	
	/**
	 * @param flag 区分第三方支付回调与平台自动补单  0：第三方支付  1：平台补单
	 */
	public String addPayorderZfb(String outtradeno, Double money, String loginname, String tradeno, String date, String flag) {
		String returnmsg=null;
		if(annDao.get(Payorder.class, outtradeno)==null){
				
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("id", "支付宝"));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
	
				// 后台是否关闭此 存款
				if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "在线支付正在维护！";
				}
				Users users = (Users)annDao.get(Users.class, loginname);
				Payorder payorder=new Payorder();
				payorder.setBillno(outtradeno);
				payorder.setPayPlatform("zfb");
				payorder.setMsg("zfb单号:"+tradeno);
				payorder.setFlag(0);
				payorder.setNewaccount(Constants.FLAG_FALSE);
				payorder.setLoginname(loginname);
				payorder.setAliasName(users.getAccountName());
				payorder.setMoney(money);
				payorder.setPhone(users.getPhone());
				payorder.setEmail(users.getEmail());
				payorder.setCreateTime(DateUtil.now());
				annDao.save(payorder);
				//更新用户存款方式
				annDao.updateUserSqlIsCashinOnline(loginname) ;
				tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), outtradeno, "zfb单号:"+tradeno);
				bankinfoDao.changeZfbAmountOnline(60, money, outtradeno);
			
		}else{
			returnmsg="此笔交易已经支付成功";
		}

		return returnmsg;
	}
	
	public String addPayorderHf(String orderNo, Double OrdAmt, String loginname, String msg, String type) {
		DecimalFormat df = new DecimalFormat("0.00");
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if (payorder != null && payorder.getLoginname().equals(loginname) && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			if(payorder.getPayPlatform().equals("hf")){
				dc = dc.add(Restrictions.eq("id", "汇付"));
			}else{
				System.out.println("汇付自动补单---动态模式开始---");
				String merchantcode="";
				DetachedCriteria dc1 = DetachedCriteria.forClass(PayMerchant.class);
				dc1 = dc1.add(Restrictions.eq("constid", payorder.getPayPlatform()));
				List<PayMerchant> payMerchants = annDao.getHibernateTemplate().findByCriteria(dc1);
				if(null!=payMerchants&&payMerchants.size()>0){
					PayMerchant p= payMerchants.get(0);
					merchantcode=p.getMerchantcode();
				}else{
					return "沒有该支付方式！";
				}
				String str=addPayorderHf1(orderNo,OrdAmt,loginname,msg,"1",merchantcode);
				System.out.println("汇付自动补单---动态模式结束---");
				return str;
			}
			
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 后台是否关闭此 存款
			if (type.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			// 更新订单号
			payorder.setMsg("hf单号:" + msg);
			payorder.setType(0);
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(loginname) ;
			// 加入银行
			tradeDao.changeCredit(loginname, OrdAmt, CreditChangeType.NETPAY.getCode(), orderNo, "hf单号:" + msg);
			bankinfoDao.changeHfAmountOnline(44, OrdAmt, orderNo);
			return null;
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	/**
	 *   汇付-----动态支付  
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param msg
	 * @param flag
	 * @param merchantcode 商户号
	 * @return
	 */
	public String addPayorderHf1(String orderNo, Double OrdAmt, String loginname, String msg, String flag,String merchantcode) {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("汇付自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"loginname="+loginname+"msg==="+msg+"flag=="+flag+"merchantcode=="+merchantcode);
		//根据商户号 获取该商户号对应的  支付开关表的主键ID
		DetachedCriteria dcc = DetachedCriteria.forClass(PayMerchant.class);
		dcc = dcc.add(Restrictions.eq("merchantcode", merchantcode));
		List<PayMerchant> payMers = annDao.getHibernateTemplate().findByCriteria(dcc);
		PayMerchant payMer = null;
		if(null!=payMers&&payMers.size()>0){
			payMer =payMers.get(0);
		}else{
			System.out.println("根据商户号找不到对应商户!");
			return "根据商户号找不到对应商户！";
		}
		String onstid=payMer.getConstid();
		
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if (payorder != null && payorder.getLoginname().equals(loginname) && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", onstid));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			String str1=onstid+"单号:" + msg;
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(loginname) ;
			// 加入银行
			System.out.println("汇付自动回调changeCredit开始----");
			tradeDao.changeCredit(loginname, OrdAmt, CreditChangeType.NETPAY.getCode(), orderNo, str1);
			System.out.println("汇付自动回调changeCredit结束----");
			System.out.println("汇付自动回调changeHfAmountOnline开始----");
			int  type =payMer.getType();
			bankinfoDao.changeHfAmountOnline1(type, OrdAmt, orderNo);
			System.out.println("汇付自动回调changeHfAmountOnline结束----");
			System.out.println("汇付自动回调webService结束----");
			return null;
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	/**
	 * 汇潮没有提供他们那边的交易号， 以及用户姓名  （少了 loginname 、 msg参数）
	 * @param orderNo
	 * @param OrdAmt
	 * @return
	 */
	public String updatePayorderHC(String orderNo, Double OrdAmt, String type) {
		DecimalFormat df = new DecimalFormat("0.00");
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", "汇潮"));
			dc = dc.add(Restrictions.eq("value", "1"));  //该在线支付为开启状态
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 后台是否关闭此 存款
			if (type.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			// 更新订单号
			payorder.setMsg("hc单号:");
			payorder.setType(0);
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(payorder.getLoginname()) ;
			// 加入银行
			tradeDao.changeCredit(payorder.getLoginname(), OrdAmt, CreditChangeType.NETPAY.getCode(), orderNo, "hc单号:");
			bankinfoDao.updateHCAmountOnline(50, OrdAmt, orderNo);  //50代表汇潮（bankinfo表）
			
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(payorder.getLoginname());
			pb.setPayplatform(payorder.getPayPlatform());
			pb.setMoney(OrdAmt);
			pb.setRemark(DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			
			return null;
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	/**
	 * 汇潮一麻袋 网银 and 快捷支付
	 * @param orderNo
	 * @param OrdAmt
	 * @return
	 */
	public String updatePayorderHCYmd(String orderNo, Double OrdAmt, String merNo, String type) {
		DecimalFormat df = new DecimalFormat("0.00");
		int paysign=0;
		String hcsign="";
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("shopno", merNo)); 
			dc = dc.add(Restrictions.eq("value", "1"));  //该在线支付为开启状态
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			System.out.println("=========汇潮一麻袋在线支付回调=======type:"+type+"==merNo:"+merNo+"==paysign:"+paysign);
			// 后台是否关闭此 存款
			if (type.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			if(type.equals("1")){
				payorder.setIp("127.0.0.1");
			}
			//快捷支付
			if(merNo.equals(Constants.HC_MerNo)){
				payorder.setMsg("hc单号:");
				hcsign="hc单号;";
				paysign=50;
			}
			//网银支付
			else if(merNo.equals(Constants.MerNo)){
				payorder.setMsg("hcwy单号:");
				hcsign="hcwy单号;";
				paysign=51;  
			}
			else{
				return merNo+"该商户号无法匹配支付平台";
			}   
			System.out.println("==type:"+type+"==merNo:"+merNo+"==paysign:"+paysign);
			
			// 更新订单号
			payorder.setType(0);
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			tradeDao.updateUserSqlIsCashinOnline(payorder.getLoginname());
			// 加入银行
			tradeDao.changeCredit(payorder.getLoginname(), OrdAmt, CreditChangeType.NETPAY.getCode(), orderNo, hcsign);
			bankinfoDao.updateHCYmdAmountOnline(paysign, OrdAmt, orderNo);  //51代表汇潮网银 50代表汇潮快捷
			
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(payorder.getLoginname());
			pb.setPayplatform(payorder.getPayPlatform());
			pb.setMoney(OrdAmt);
			pb.setRemark(DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	public String addSaveOrderHC(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("hc");
		payorder.setMsg("汇潮未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	//汇潮一麻袋
	public String addSaveOrderHCYmd(String orderNo, Double OrdAmt, String loginname, String payfly) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		if(payfly.equals("hckj")){
			payorder.setBillno("e_hckjt_"+orderNo);
			payorder.setPayPlatform("hc");
			payorder.setMsg("汇潮快捷通未支付订单号:" + orderNo);
		}
		else if (payfly.equals("hcwy")){
			payorder.setBillno("e_hcymd_"+orderNo);
			payorder.setPayPlatform("hcymd");
			payorder.setMsg("汇潮网银未支付订单号:" + orderNo);
		}
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return payorder.getBillno();
	}
	
	public String addSaveOrderBfb(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("bfb");
		payorder.setMsg("币付宝未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	public String updatePayorderBfb(String orderNo, Double OrdAmt, String type) {
		DecimalFormat df = new DecimalFormat("0.00");
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try{
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", "币付宝"));
			dc = dc.add(Restrictions.eq("value", "1"));  //该在线支付为开启状态
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 后台是否关闭此 存款
			if (type.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			// 更新订单号
			payorder.setMsg("bfb单号:" + orderNo);
			payorder.setType(0);
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(payorder.getLoginname()) ;
			// 加入银行
			tradeDao.changeCredit(payorder.getLoginname(), OrdAmt, CreditChangeType.NETPAY.getCode(), orderNo, "bfb单号:");
			bankinfoDao.updateBfbAmountOnline(61, OrdAmt, orderNo);  //61代表币付宝（bankinfo表）
			
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(payorder.getLoginname());
			pb.setPayplatform(payorder.getPayPlatform()); 
			pb.setMoney(OrdAmt);
			pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+payorder.getLoginname());
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	public String addSaveOrderGfb(String orderNo, Double OrdAmt, String loginname, String code) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform(code);
		payorder.setMsg("国付宝未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	public String updatePayorderGfb(String orderNo, Double OrdAmt, String code, String type) {
		DecimalFormat df = new DecimalFormat("0.00");
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try{
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			//补单和正常回调的code参数不一样
			CommonGfbEnum cmgfb = "1".equals(type)?CommonGfbEnum.getCommonGfbByVirCardNoIn(code):CommonGfbEnum.getCommonGfb(code);
			dc = dc.add(Restrictions.eq("id", cmgfb.getText()));
			dc = dc.add(Restrictions.eq("value", "1"));  //该在线支付为开启状态
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 后台是否关闭此 存款
			if (type.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			// 更新订单号
			payorder.setMsg("gfb单号:" + orderNo);
			payorder.setType(0);
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(payorder.getLoginname()) ;
			// 加入银行
			tradeDao.changeCredit(payorder.getLoginname(), OrdAmt, CreditChangeType.NETPAY.getCode(), orderNo, "gfb单号:");
			bankinfoDao.updateGfbAmountOnline(cmgfb, OrdAmt, orderNo);
			
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(payorder.getLoginname());
			pb.setPayplatform(payorder.getPayPlatform());
			pb.setMoney(OrdAmt);
			pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+payorder.getLoginname());
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	public String addSaveOrderHf(String orderNo, Double OrdAmt, String loginname,String constID) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform(constID);
		payorder.setMsg(constID+"未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	public String addSaveOrderHf(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("hf");
		payorder.setMsg("汇付未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	@Override
	public Users getUser(String loginname){
		Users users=(Users)annDao.get(Users.class,loginname);
		return users;
	}


	public TradeDao getTradeDao() {
		return tradeDao;
	}


	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}


	public BankinfoDao getBankinfoDao() {
		return bankinfoDao;
	}


	public void setBankinfoDao(BankinfoDao bankinfoDao) {
		this.bankinfoDao = bankinfoDao;
	}


	@Override
	public Page selectWinPoint(Date startTime, Date endTime, Integer betTotal,
			String by, String order, Integer pageIndex, Integer size) {
		Session  session = annDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuffer sbf = new StringBuffer();
		sbf.append("select aloginname,level,amountTotal,betTotal,winPointTotal,agent from " +
				"	(select loginname as aloginname,-1*sum(amount) as amountTotal,sum(bettotal) as betTotal,-1*sum(amount)/sum(bettotal) as winPointTotal from agprofit " +
				"	where createTime>=? and createTime<=? ");

		sbf.append(" group by loginname )t " +
				"	left join users on users.loginname=t.aloginname where 1=1 and amountTotal>0 ");
		
		Map<String,Object> mm = new HashMap<String, Object>();
		switch (betTotal) {
		case 0:
			sbf.append(" and betTotal>=:gtBet");
			sbf.append(" and betTotal<:ltBet");
			mm.put("gtBet",50000 );
			mm.put("ltBet",500000 );
			break;
		case 1:
			sbf.append(" and betTotal>=:gtBet");
			sbf.append(" and betTotal<:ltBet");
			mm.put("gtBet",500000 );
			mm.put("ltBet",1000000 );
			break;
		case 2:
			sbf.append(" and betTotal>=:gtBet");
			sbf.append(" and betTotal<:ltBet");
			mm.put("gtBet",1000000 );
			mm.put("ltBet",1500000 );
			break;
		case 3:
			sbf.append(" and betTotal>=:gtBet");
			mm.put("gtBet",1500000 );
			break;
		default:
			break;
		}
		
		if(StringUtils.isNotEmpty(by)){
			sbf.append(" order by "+by+" "+order);
		}
		
		if ((size == null) || (size.intValue() == 0))
			size = Page.PAGE_DEFAULT_SIZE;
		if (pageIndex == null)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		Query query = session.createSQLQuery(sbf.toString()).setDate(0, startTime).setDate(1, endTime);
		
		if(!mm.isEmpty()){
			query = query.setProperties(mm);//直接将map参数传组query对像
		}
		List list = query.list();
		Double totalAmount = 0.00;
		Double totalBet = 0.00;
		for(Object obj : list){
			Object[]objarray = (Object[])obj;
			totalAmount +=(Double)objarray[2];
			totalBet +=(Double)objarray[3];
		}
		query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
		query.setMaxResults(size.intValue());
		List contentList = query.list();
		List<WinPointInfo> modelList = new ArrayList<WinPointInfo>();
		for(int i=0;i<contentList.size();i++){
			Object[]pgarray = (Object[])contentList.get(i);
			String loginname = (String)pgarray[0];
			if(StringUtils.isNotEmpty(loginname)){
				loginname = loginname.substring(0, loginname.length()-1)+"***";
			}
			WinPointInfo info = new WinPointInfo(loginname,(Double)pgarray[2],(Double)pgarray[3],(Double)pgarray[4],(pageIndex.intValue()-1)*10+i+1);
			modelList.add(info);
		}
		Page page = new Page();
		page.setPageNumber(pageIndex);
		page.setSize(size);
		page.setTotalRecords(list.size());
		int pages = PagenationUtil.computeTotalPages(list.size(), size).intValue();
		page.setTotalPages(Integer.valueOf(pages));
		if (pageIndex.intValue() > pages)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		page.setPageNumber(pageIndex);
		page.setPageContents(modelList);
		page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		page.setStatics1(totalAmount);
		page.setStatics2(totalBet);
		
		return page;
	}
	
	public String addPayorderWeiXin(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("weixinpay");
		payorder.setMsg("微信未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	
	/**
	 * 微信支付回调
	 * @param out_trade_no
	 * @param flag=0是自动回调
	 * @return
	 */
	public String weixinpayreturn(String out_trade_no, String flag,String OrdAmt) {
		System.out.println("微信支付webService回调开始----orderNo=="+out_trade_no+"flag==="+flag+"金额是："+OrdAmt);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, out_trade_no);
		if (payorder != null && payorder.getType() == 2 && payorder.getMoney().equals(Double.parseDouble(OrdAmt))) {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", "微信支付直连"));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			String str1="微信支付单号:" + out_trade_no+",微信支付自动回调";
			if(!flag.equals("0")){
				str1="微信支付单号:" + out_trade_no+",微信支付补单回调";	
			}
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(payorder.getLoginname()) ;
			// 加入银行
			System.out.println("微信自动回调changeCredit开始----");
			tradeDao.changeCredit(payorder.getLoginname(), payorder.getMoney(), CreditChangeType.NETPAY.getCode(), out_trade_no, str1);
			System.out.println("微信自动回调changeCredit结束----");
			System.out.println("微信自动回调changeWeiXinAmountOnline开始----");
			int  type =451;
			bankinfoDao.changeWeiXinAmountOnline(type, Double.parseDouble(OrdAmt), out_trade_no);
			System.out.println("微信自动回调weixinpayreturn结束----");
			System.out.println("微信自动回调webService结束----");
			return null;
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	
	
	
	@Override
	public String addPayLfWxzf(String orderNo, Double OrdAmt,
			String loginname, String flag) {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("乐富微信支付自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", "乐富微信"));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			String str1="乐富微信单号:" + orderNo;
			if(!loginname.equals(payorder.getLoginname())){
				System.out.println("乐富微信支付回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
				return "乐富微信支付回调失败：订单用户和实际用户不一致";
			}
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			if(flag.equals("1")){
				payorder.setIp("127.0.0.1");
			}
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式changeHfAmountOnline
			annDao.updateUserSqlIsCashinOnline(loginname) ;
			// 加入银行
			System.out.println("乐富微信支付自动回调changeCredit开始----");
			tradeDao.changeCredit(loginname, OrdAmt, CreditChangeType.NETPAY.getCode(), orderNo, str1);
			System.out.println("乐富微信支付回调changeCredit结束----");
			System.out.println("乐富微信支付回调changeLfAmountOnline开始----");
			bankinfoDao.changeLfwxzfAmountOnline(450, OrdAmt, orderNo);
			System.out.println("乐富微信支付回调changeLfAmountOnline结束----");
			System.out.println("乐富微信支付回调webService结束----");
			return null;
		} else {
			return "此笔交易已经支付成功";
		}
	}

	
	public String addSaveOrderLfwx(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("lfwx");
		payorder.setMsg("lfwx未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	
	@Override
	public String addSaveOrderXinBwx(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("xbwx");
		payorder.setMsg("xbwx未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	public String addSaveOrderKdZf(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("kdzf");
		payorder.setMsg("kdzf未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	@Override
	public String addSaveOrderKdWxZf(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("kdwxzf");
		payorder.setMsg("kdwxzf未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	
	/****
	 * 口袋微信支付2
	 */
	@Override
	public String addSaveOrderKdWxZf2(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("kdwxzf2");
		payorder.setMsg("kdwxzf2未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	/****
	 *  口袋微信支付1 and 口袋微信支付2 and 口袋微信支付3
	 */
	@Override
	public String addSaveOrderKdWxZfs(String orderNo, Double OrdAmt, String loginname,String payfly) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		if(payfly.equals("kdwxzf")){
			payorder.setBillno("e68_kdwx_"+orderNo);
			payorder.setPayPlatform("kdwxzf");
			payorder.setMsg("kdwxzf未支付订单号:" + orderNo);
		}
		else if (payfly.equals("kdwxzf2")){
			payorder.setBillno("e68_kdwx2_"+orderNo);
			payorder.setPayPlatform("kdwxzf2");
			payorder.setMsg("kdwxzf2未支付订单号:" + orderNo);
		}
		else if (payfly.equals("kdwxzf3")){
			payorder.setBillno("e68_kdwx3_"+orderNo);
			payorder.setPayPlatform("kdwxzf3");
			payorder.setMsg("kdwxzf3未支付订单号:" + orderNo);
		}
		else if (payfly.equals("kdzfb2")){
			payorder.setBillno("e68_kdzfb2_"+orderNo);
			payorder.setPayPlatform("kdzfb2");
			payorder.setMsg("kdzfb2未支付订单号:" + orderNo);
		}
		else if (payfly.equals("kdzfb")){
			payorder.setBillno("e68_kdzfb_"+orderNo);
			payorder.setPayPlatform("kdzfb");
			payorder.setMsg("kdzfb未支付订单号:" + orderNo);
		}
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return payorder.getBillno();
	}
	
	public String addSaveOrderHhbZf(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("hhbzf");
		payorder.setMsg("hhbzf未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	public String addSaveOrderHhbWxZf(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("hhbwxzf");
		payorder.setMsg("hhbwx未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	
	/****
	 * 聚宝支付宝支付
	 */
	public String addSaveOrderJubZfb(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("jubzfb");
		payorder.setMsg("jubzfb未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	
	public String addSaveOrderXlb(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("xlb");
		payorder.setMsg("xlb未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	
	
	public String addSaveOrderXlbWy(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("xlbwy");
		payorder.setMsg("xlbwy未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);    
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	/***
	 * 优付支付宝 and 微信
	 */
	public String addSaveOrderYfZf(String orderNo, Double OrdAmt, String loginname,String payfly) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		if(payfly.equals("yfzfb")){
			payorder.setBillno("e68_yfzfb_"+orderNo);
			payorder.setPayPlatform("yfzfb");
			payorder.setMsg("yfzfb未支付订单号:" + orderNo);
		}
		else if (payfly.equals("yfwx")){
			payorder.setBillno("e68_yfwx_"+orderNo);
			payorder.setPayPlatform("yfwx");
			payorder.setMsg("yfwx未支付订单号:" + orderNo);
		}
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);    
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return payorder.getBillno();
	}	
	
	/***
	 * 银宝支付宝
	 */
	public String addSaveOrderYbZfb(String orderNo, Double OrdAmt, String loginname,String payfly ) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		if(payfly.equals("ybzfb")){
			payorder.setBillno("e68_ybzfb_"+orderNo);
			payorder.setPayPlatform("ybzfb");
			payorder.setMsg("ybzfb未支付订单号:" + orderNo);
		}
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);    
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return payorder.getBillno();
	}	
	
	
	@Override
	public String addPayXbWxzf(String orderNo, Double OrdAmt, String loginname, String flag) {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("新贝微信支付自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("id", "新贝微信"));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
				// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
				if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "在线支付正在维护！";
				}
				String str1="新贝微信单号:" + orderNo;
				if(!loginname.equals(payorder.getLoginname())){
					System.out.println("新贝微信支付回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
					return "新贝微信支付回调失败：订单用户和实际用户不一致";
				}
				// 更新订单号
				payorder.setMsg(str1);
				payorder.setType(0);
				if(flag.equals("1")){
					payorder.setIp("127.0.0.1");
				}
				payorder.setReturnTime(DateUtil.now());
				annDao.update(payorder);
				// 更新用户存款方式changeHfAmountOnline
				annDao.updateUserSqlIsCashinOnline(loginname);
				// 加入银行
				System.out.println("新贝微信支付自动回调changeCredit开始----");
				Double amount4user = Arith.mul(OrdAmt, 0.992);
				tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1);
				System.out.println("新贝微信支付回调changeCredit结束----");
				System.out.println("新贝微信支付回调changeLfAmountOnline开始----");
				bankinfoDao.changeXbwxzfAmountOnline(460, OrdAmt, orderNo);
				System.out.println("新贝微信支付回调webService结束----");
				Payorderbillono pb =new Payorderbillono();
				pb.setBillno(orderNo);
				pb.setLoginname(loginname);
				pb.setPayplatform("xbwx"); 
				pb.setMoney(OrdAmt);
				pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
				annDao.save(pb);
				return null;
			} catch(Exception e){
				System.out.println("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	@Override
	public String addPayKdZf(String orderNo, Double OrdAmt, String loginname, String flag) {
		DecimalFormat df = new DecimalFormat("0.00");
		log.info("口袋支付自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("id", "口袋支付"));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
				// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
				if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "在线支付正在维护！";
				}
				String str1="口袋支付单号:" + orderNo;
				if(!loginname.equals(payorder.getLoginname())){
					log.info("口袋支付回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
					return "口袋支付回调失败：订单用户和实际用户不一致";
				}
				// 更新订单号
				payorder.setMsg(str1);
				payorder.setType(0);
				if(flag.equals("1")){
					payorder.setIp("127.0.0.1");
				}
				payorder.setReturnTime(DateUtil.now());
				annDao.update(payorder);
				// 更新用户存款方式changeHfAmountOnline
				annDao.updateUserSqlIsCashinOnline(loginname);
				// 加入银行
				Double amount4user = Arith.mul(OrdAmt, 0.991);
				tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1 + ";手续费0.9%");
				bankinfoDao.changeKdZfAmountOnline(470, OrdAmt, orderNo);
				Payorderbillono pb =new Payorderbillono();
				pb.setBillno(orderNo);
				pb.setLoginname(loginname);
				pb.setPayplatform("kdzf"); 
				pb.setMoney(OrdAmt);
				pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
				annDao.save(pb);
			} catch(Exception e){
				log.error("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
			return null;
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	@Override
	public String addPayHhbZf(String orderNo, Double OrdAmt, String loginname, String flag) {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("汇付宝支付自动回调webService开始--orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("id", "汇付宝支付"));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
				// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
				if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "汇付宝支付正在维护！";
				}
				String str1="汇付宝支付单号:" + orderNo;
				if(!loginname.equals(payorder.getLoginname())){
					System.out.println("汇付宝支付回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
					return "汇付宝支付回调失败：订单用户和实际用户不一致";
				}
				// 更新订单号
				payorder.setMsg(str1);
				payorder.setType(0);
				if(flag.equals("1")){
					payorder.setIp("127.0.0.1");
				}
				payorder.setReturnTime(DateUtil.now());
				annDao.update(payorder);
				// 更新用户存款方式changeHfAmountOnline
				annDao.updateUserSqlIsCashinOnline(loginname);
				// 加入银行
				System.out.println("口袋支付自动回调changeCredit开始----");
				tradeDao.changeCredit(loginname, OrdAmt, CreditChangeType.NETPAY.getCode(), orderNo, str1);
				bankinfoDao.changeHhbZfAmountOnline(480, OrdAmt, orderNo);
				System.out.println("口袋支付回调changeCredit结束----");
				
				Payorderbillono pb =new Payorderbillono();
				pb.setBillno(orderNo);
				pb.setLoginname(loginname);
				pb.setPayplatform("hhbzf"); 
				pb.setMoney(OrdAmt);
				pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
				annDao.save(pb);
				
				System.out.println("口袋支付回调webService结束----");
				return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}

	@Override
	public String addPayKdWxZf(String orderNo, Double OrdAmt, String loginname, String flag) {
		Boolean isPhone = false ;
		String phoneMsg = "";
		if(loginname.startsWith("wap_")){  //手机端过来的，费率不一样，做下区分
			isPhone = true ;
			loginname = loginname.replace("wap_", "");
			phoneMsg = "wap";
		}
		DecimalFormat df = new DecimalFormat("0.00");
		log.info("口袋微信支付自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("id", "口袋微信支付"));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
				// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
				if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "口袋微信支付正在维护！";
				}
				String str1= phoneMsg+"口袋微信支付单号:" + orderNo;
				if(!loginname.equals(payorder.getLoginname())){
					log.info("口袋微信支付回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
					return "口袋微信支付回调失败：订单用户和实际用户不一致";
				}
				// 更新订单号
				payorder.setMsg(str1);
				payorder.setType(0);
				if(flag.equals("1")){
					payorder.setIp("127.0.0.1");
				}
				payorder.setReturnTime(DateUtil.now());
				annDao.update(payorder);
				// 更新用户存款方式changeHfAmountOnline
				annDao.updateUserSqlIsCashinOnline(loginname);
				// 加入银行
				Double amount4user = Arith.mul(OrdAmt, 0.992);
				tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1 + ";费率0.8%");
				bankinfoDao.changeKdWxZfAmountOnline(471, OrdAmt, orderNo, isPhone);
				Payorderbillono pb =new Payorderbillono();
				pb.setBillno(orderNo);
				pb.setLoginname(loginname);
				pb.setPayplatform("kdwxzf"); 
				pb.setMoney(OrdAmt);
				pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
				annDao.save(pb);
				return null;
			} catch (Exception e) {
				log.error("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	

	/******
	 * 口袋微信支付2
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param flag
	 * @return
	 */
	@Override
	public String addPayKdWxZf2(String orderNo, Double OrdAmt, String loginname, String flag) {
		Boolean isPhone = false ;
		String phoneMsg = "";
		if(loginname.startsWith("wap_")){  //手机端过来的，费率不一样，做下区分
			isPhone = true ;
			loginname = loginname.replace("wap_", "");
			phoneMsg = "wap";
		}
		DecimalFormat df = new DecimalFormat("0.00");
		log.info("口袋微信2支付自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		log.info("webservice口袋微信2支付单号："+payorder.getBillno());
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		log.info("====处理玩家"+payorder.getLoginname()+"订单====");
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("id", "口袋微信支付2"));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
				// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
				if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "口袋微信2支付正在维护！";
				}
				//记录是补单程序补单到账，还是正常回调到账
				String str1 = ""; 
				str1 =flag.equals("1")?phoneMsg+"口袋微信2支付单号:" + orderNo+"--补单程序补单":phoneMsg+"口袋微信2支付单号:" + orderNo;
				
				if(!loginname.equals(payorder.getLoginname())){
					log.info("口袋微信2支付回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
					return "口袋微信2支付回调失败：订单用户和实际用户不一致";
				}
				log.info("即将执行更新payorder操作："+payorder.getBillno());
				// 更新订单号
				payorder.setMsg(str1);
				payorder.setType(0);
				if(flag.equals("1")){
					payorder.setIp("127.0.0.1");
				}
				payorder.setReturnTime(DateUtil.now());
				log.info(payorder.toString()+"==payorder对象");
				log.info(payorder.getBillno()+"---billno--"+payorder.getMsg()+"----MSg--"+payorder.getType()+"---type--"+payorder.getFlag()+"---flag---");
				annDao.update(payorder);
				log.info("执行修改payorder结束");
				
				System.out.println("\n\n\n**************************************************");
				log.info("更新存款方式开始："+loginname);
				// 更新用户存款方式changeHfAmountOnline
				annDao.updateUserSqlIsCashinOnline(loginname);
				log.info("更新存款方式结束："+loginname);
				// 加入银行
				Double amount4user = Arith.mul(OrdAmt, 0.992);
				tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo,str1 + ";费率0.8%");
				bankinfoDao.changeKdWxZfAmountOnline2(474, OrdAmt, orderNo , isPhone);
				log.info("=====即将执行订单唯一约束表保存操作=====");
				Payorderbillono pb =new Payorderbillono();
				pb.setBillno(orderNo);
				pb.setLoginname(loginname);
				pb.setPayplatform("kdwxzf2"); 
				pb.setMoney(OrdAmt);
				pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
				annDao.save(pb);
				log.info("=====执行订单唯一约束表保存操作结束====="+pb.getBillno()+"---"+pb.getMoney());
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	/******
	 * 口袋支付宝2 and 口袋微信支付1 and 口袋微信支付2 and 口袋微信支付3 回调业务处理
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param flag
	 * @return
	 */
	@Override
	public String addPayKdWxZfs(String orderNo, Double OrdAmt, String payName,String loginname, String flag) {
		Boolean isPhone = false ;
		String phoneMsg = "";
		String kdzfsign="";       //payPlatForm支付平台
		int paysign;                   //BankType标示和后台保持一致
		Double fee;                  //自身扣除费率
		Double payFee;          //支付平台费率
		
		if(loginname.startsWith("wap_")){  //手机端过来的，费率不一样，做下区分
			isPhone = true ;
			loginname = loginname.replace("wap_", "");
			phoneMsg = "wap";
		}
		DecimalFormat df = new DecimalFormat("0.00");
		log.info(payName+"支付自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"payName==="+payName+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		log.info("====处理玩家"+payorder.getLoginname()+"订单====");
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("id", payName));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
				// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
				if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return payName+"正在维护！";
				}
				// 口袋微信支付
				if(payName.equals("口袋微信支付")){
					if(isPhone){payFee=97.6;fee =0.976;}//手机端费率
					else{payFee=98.6;fee =0.992;} //电脑端费率
					kdzfsign="kdwxzf";
					paysign=471;
				}
				// 口袋微信支付2
				else if(payName.equals("口袋微信支付2")){
					if(isPhone){payFee=97.6;fee =0.976;}//手机端费率
					else{payFee=98.6;fee =0.992;} //电脑端费率
					kdzfsign="kdwxzf2";
					paysign=474;
				}
				// 口袋微信支付3
				else if(payName.equals("口袋微信支付3")){
					if(isPhone){payFee=97.6;fee =0.976;}//手机端费率
					else{payFee=98.6;fee =0.992;} //电脑端费率
					kdzfsign="kdwxzf3";
					paysign=478;
				}
				// 口袋支付宝2
				else if(payName.equals("口袋支付宝2")){
					if(isPhone){payFee=98.5;}//手机端费率
					else{payFee=98.5;} //电脑端费率
					kdzfsign="kdzfb2;";
					paysign=494;
					fee =0.991;
				}
				// 口袋支付宝
				else if(payName.equals("口袋支付宝")){
					if(isPhone){payFee=98.5;}//手机端费率
					else{payFee=98.5;} //电脑端费率
					kdzfsign="kdzfb;";
					paysign=470;
					fee =0.991;
				}
				else{
					log.info(payName+"回调失败：该笔订单无法匹配支付平台");
					return payName+"回调失败：该笔订单无法匹配支付平台";
				}   
				
				String str1=phoneMsg+payName+"单号:" + orderNo;
				if(!loginname.equals(payorder.getLoginname())){
					log.info(payName+"回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
					return payName+"回调失败：订单用户和实际用户不一致";
				}
				// 更新订单号
				payorder.setMsg(str1);
				payorder.setType(0);
				if(flag.equals("1")){
					payorder.setIp("127.0.0.1");
				}
				payorder.setReturnTime(DateUtil.now());
				annDao.update(payorder);
				// 更新用户存款方式
				annDao.updateUserSqlIsCashinOnline(loginname);  
				// 加入银行
				Double amount4user = Arith.mul(OrdAmt, fee);
				tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1);
				bankinfoDao.changeKdWxZfsAmountOnline(paysign, OrdAmt, orderNo, payFee);
				Payorderbillono pb =new Payorderbillono();
				pb.setBillno(orderNo);
				pb.setLoginname(loginname);
				pb.setPayplatform(kdzfsign); 
				pb.setMoney(OrdAmt);
				pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
				annDao.save(pb);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			log.info("===========此笔交易已经支付成功============");
			return "此笔交易已经支付成功";
		}
	}
	
	
	
	
	/****
	 * 汇付宝微信支付
	 */
	@Override
	public String addPayHhbWxZf(String orderNo, Double OrdAmt, String loginname, String flag) {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("汇付宝微信自动回调webService开始--orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("id", "汇付宝微信"));
				dc = dc.add(Restrictions.eq("value", "1"));
				List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
				// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
				if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
					return "汇付宝微信支付正在维护！";
				}
				String str1="汇付宝微信支付单号:" + orderNo;
				if(!loginname.equals(payorder.getLoginname())){
					System.out.println("汇付宝微信支付回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
					return "汇付宝微信支付回调失败：订单用户和实际用户不一致";
				}
				// 更新订单号
				payorder.setMsg(str1);
				payorder.setType(0);
				if(flag.equals("1")){
					payorder.setIp("127.0.0.1");
				}
				payorder.setReturnTime(DateUtil.now());
				annDao.update(payorder);
				// 更新用户存款方式changeHfAmountOnline 
				annDao.updateUserSqlIsCashinOnline(loginname);
				// 加入银行
				System.out.println("汇付宝微信自动回调changeCredit开始----");
				Double amount4user = Arith.mul(OrdAmt, 0.992);
				tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1);
				bankinfoDao.changeHhbWxZfAmountOnline(481, OrdAmt, orderNo);
				System.out.println("汇付宝微信回调changeCredit结束----");
				
				Payorderbillono pb =new Payorderbillono();
				pb.setBillno(orderNo);
				pb.setLoginname(loginname);
				pb.setPayplatform("hhbzf"); 
				pb.setMoney(OrdAmt);
				pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
				annDao.save(pb);
				
				System.out.println("汇付宝支付回调webService结束----");
				return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	/***
	 * 聚宝支付
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param flag
	 * @return
	 */
	@Override 
	public String addPayJubZfb(String orderNo, Double OrdAmt, String loginname, String flag) {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("聚宝支付宝自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", "聚宝支付宝"));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "聚宝支付宝正在维护！";
			}
			String str1="聚宝支付宝单号:" + orderNo;
			if(!loginname.equals(payorder.getLoginname())){
				System.out.println("聚宝支付宝回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
				return "聚宝支付宝回调失败：订单用户和实际用户不一致";
			}
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			if(flag.equals("1")){
				payorder.setIp("127.0.0.1");
			}
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式changeHfAmountOnline
			annDao.updateUserSqlIsCashinOnline(loginname);
			// 加入银行
			System.out.println("聚宝支付宝自动回调money change开始----");
			Double amount4user = Arith.mul(OrdAmt, 0.991);
			tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1);
			bankinfoDao.changeJubZfbAmountOnline(473, OrdAmt, orderNo);
			System.out.println("聚宝支付宝回调webService结束----");
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(loginname);
			pb.setPayplatform("jubzfb"); 
			pb.setMoney(OrdAmt);
			pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	
	/******
	 * 迅联宝微信支付
	 */
	@Override 
	public String addPayXlb(String orderNo, Double OrdAmt, String loginname, String flag) {
		Boolean isPhone = false ;
		String phoneMsg = "";
		if(loginname.startsWith("wap_")){  //手机端过来的，费率不一样，做下区分
			isPhone = true ;
			loginname = loginname.replace("wap_", "");
			phoneMsg = "wap";
		}
		
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("迅联宝自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", "迅联宝"));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			String str1=phoneMsg+"迅联宝单号:" + orderNo;
			if(!loginname.equals(payorder.getLoginname())){
				System.out.println("迅联宝回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
				return "迅联宝支付回调失败：订单用户和实际用户不一致";
			}
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			if(flag.equals("1")){
				payorder.setIp("127.0.0.1");
			}
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(loginname);
			// 加入银行
			System.out.println("迅联宝自动回调changeCredit开始----");
			Double amount4user = Arith.mul(OrdAmt, 0.992);
			tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1+";0.8%");
			System.out.println("迅联宝回调changeCredit结束----");
			System.out.println("迅联宝回调changeXlbAmountOnline开始----");
			bankinfoDao.changeXlbAmountOnline(485, OrdAmt, orderNo,isPhone);
			System.out.println("迅联宝回调webService结束----");
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(loginname);
			pb.setPayplatform("xlb");
			pb.setMoney(OrdAmt);
			pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	
	
	/******
	 * 迅联宝网银支付
	 */
	@Override 
	public String addPayXlbWy(String orderNo, Double OrdAmt, String loginname, String flag) {
		
		DecimalFormat df = new DecimalFormat("0.00");
		log.info("<<<迅联宝网银支付自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", "迅联宝网银"));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			String str1="迅联宝网银单号:" + orderNo;
			if(!loginname.equals(payorder.getLoginname())){
				log.info("<<<迅联宝网银支付回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
				return "迅联宝网银支付回调失败：订单用户和实际用户不一致";
			}
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			if(flag.equals("1")){
				payorder.setIp("127.0.0.1");
			}
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(loginname);
			// 加入银行
			log.info("<<<迅联宝网银支付自动回调changeCredit开始----");
			tradeDao.changeCredit(loginname, OrdAmt, CreditChangeType.NETPAY.getCode(), orderNo,"迅联宝网银支付单号:"+orderNo);
			log.info("<<<迅联宝网银支付回调changeCredit结束----");
			log.info("<<<迅联宝网银支付回调changeXlbAmountOnline开始----");
			bankinfoDao.changeXlbWyAmountOnline(486, OrdAmt, orderNo);
			log.info("<<<迅联宝网银支付回调webService结束----");
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(loginname);
			pb.setPayplatform("xlbWy");
			pb.setMoney(OrdAmt);
			pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
			} catch (Exception e) {
				log.info("<<<单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	
	/***
	 * 优付支付宝 and 微信
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param flag
	 * @return
	 */
	@Override 
	public String addPayYfZf(String orderNo, Double OrdAmt,String payName, String loginname, String flag) {
		DecimalFormat df = new DecimalFormat("0.00");
		Boolean isPhone = false ;
		String phoneMsg = "";  //手机端区分标示
		String kdwxsign="";     //payPlatForm支付平台
		int paysign;                 //BankType标示和后台保持一致
		Double fee;                //自身扣除费率
		Double payFee;          //支付平台费率
		if(loginname.startsWith("wap_")){  //手机端过来的，费率不一样，做下区分
			isPhone = true ;
			loginname = loginname.replace("wap_", "");
			phoneMsg = "wap";
		}
		
		System.out.println(payName+"支付自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"payName=="+payName+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", payName));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return  payName+"正在维护！";
			}
			//优付支付宝
			if(payName.equals("优付支付宝")){
				if(isPhone){payFee=98.2;}//手机端费率
				else{payFee=98.2;} //电脑端费率
				kdwxsign="yfzfb;";
				paysign=488;
				fee =0.991;
			}
			//优付微信
			else if(payName.equals("优付微信")){
				if(isPhone){payFee=99.0;} //手机端费率
				else{payFee=99.0;} //电脑端费率
				kdwxsign="yfwx;";
				paysign=492;
				fee =0.992;
			}
			else{
				log.info(payName+"回调失败：该笔订单无法匹配支付平台");
				return payName+"回调失败：该笔订单无法匹配支付平台";
			}
			String str1=phoneMsg+payName+"单号:" + orderNo;
			if(!loginname.equals(payorder.getLoginname())){
				System.out.println(payName+"回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
				return payName+"回调失败：订单用户和实际用户不一致";
			}
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			if(flag.equals("1")){
				payorder.setIp("127.0.0.1");
			}
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(loginname);
			// 加入银行
			System.out.println(payName+"自动回调money change开始----");
			Double amount4user = Arith.mul(OrdAmt, fee);
			tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1);
			bankinfoDao.changeYfZfAmountOnline(paysign, OrdAmt, orderNo,payFee);
			System.out.println(payName+"回调webService结束----");
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(loginname);
			pb.setPayplatform(kdwxsign); 
			pb.setMoney(OrdAmt);
			pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	

	
	
	
	@Override
	public List queryDataEuro() {
		Iterator it = annDao.queryDataEuro().iterator();
		List<QueryDataEuroVO> list=new ArrayList<QueryDataEuroVO>();
		while(it.hasNext()){
			GiftOrder giftOrder =(GiftOrder) it.next();
			list.add(new QueryDataEuroVO(StringUtil.replace(giftOrder.getLoginname(), StringUtil.substring(giftOrder.getLoginname(), 2,6), "****"), DateUtil.formatDateForStandard(giftOrder.getApplyDate())));
		}
		return list;
	}
	
	public String addSaveOrderXinBZfb(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("xbzfb");
		payorder.setMsg("xbzfb未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	/***
	 * 银宝支付宝
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param flag
	 * @return
	 */
	@Override 
	public String addPayYbZfb(String orderNo, Double OrdAmt,String payName, String loginname, String flag) {
		Boolean isPhone = false ;
		String phoneMsg = "";
		String kdwxsign="";
		int paysign = 0;
		if(loginname.startsWith("wap_")){  //手机端过来的，费率不一样，做下区分
			isPhone = true ;
			loginname = loginname.replace("wap_", "");
			phoneMsg = "wap";
		}
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("银宝支付宝自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname+"payName==="+payName);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", payName));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "银宝支付宝正在维护！";
			}
			// 口袋微信支付
			if(payName.equals("银宝支付宝")){
				kdwxsign="ybzfb;";
				paysign=491;
			}
			else{
				log.info(payName+"回调失败：该笔订单无法匹配支付平台");
				return payName+"回调失败：该笔订单无法匹配支付平台";
			}   
			
			String str1=phoneMsg+payName+"单号:" + orderNo;
			if(!loginname.equals(payorder.getLoginname())){
				System.out.println("银宝支付宝回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
				return "银宝支付宝回调失败：订单用户和实际用户不一致";
			}
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			if(flag.equals("1")){
				payorder.setIp("127.0.0.1");
			}
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(loginname);
			// 加入银行
			System.out.println("银宝支付宝自动回调money change开始----");
			Double amount4user = Arith.mul(OrdAmt, 0.991);
			tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1);
			bankinfoDao.changeYbZfbAmountOnline(paysign, OrdAmt, orderNo,isPhone);
			System.out.println("银宝支付宝回调webService结束----");
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(loginname);
			pb.setPayplatform(kdwxsign); 
			pb.setMoney(OrdAmt);
			pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	/***
	 * 新贝支付宝
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param flag
	 * @return
	 */
	@Override 
	public String addPayXbZfbzf(String orderNo, Double OrdAmt, String loginname, String flag) {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("新贝支付宝自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", "新贝支付宝"));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "新贝支付宝正在维护！";
			}
			String str1="新贝支付宝单号:" + orderNo;
			if(!loginname.equals(payorder.getLoginname())){
				System.out.println("新贝支付宝回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
				return "新贝支付宝回调失败：订单用户和实际用户不一致";
			}
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			if(flag.equals("1")){
				payorder.setIp("127.0.0.1");
			}
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(loginname);
			// 加入银行
			System.out.println("新贝支付宝自动回调money change开始----");
			Double amount4user = Arith.mul(OrdAmt, 0.991);
			tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1);
			bankinfoDao.changeXbZfbAmountOnline(489, OrdAmt, orderNo);
			System.out.println("新贝支付宝回调webService结束----");
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(loginname);
			pb.setPayplatform("xbzfb"); 
			pb.setMoney(OrdAmt);
			pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	/***
	 * 千网支付宝
	 */
	public String addSaveOrderQwZf(String orderNo, Double OrdAmt, String loginname,String payfly ) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		if(payfly.equals("qwzfb")){
			payorder.setBillno("e68_qwzfb_"+orderNo);
			payorder.setPayPlatform("qwzfb");
			payorder.setMsg("qwzfb未支付订单号:" + orderNo);
		}
		else if (payfly.equals("qwwx")){
			payorder.setBillno("e68_qwwx_"+orderNo);
			payorder.setPayPlatform("qwwx");
			payorder.setMsg("qwwx未支付订单号:" + orderNo);
		}
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);    
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return payorder.getBillno();
	}
	
	/***
	 * 千网支付宝 and 微信
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param flag
	 * @return
	 */
	@Override 
	public String addPayQwZf(String orderNo, Double OrdAmt,String payName, String loginname, String flag) {
		DecimalFormat df = new DecimalFormat("0.00");
		Boolean isPhone = false ;
		String phoneMsg = "";  //手机端区分标示
		String kdwxsign="";     //payPlatForm支付平台
		int paysign;                 //和后台BankType标示保持一致
		Double fee;                //自身扣除费率
		Double payFee;          //支付平台费率
		if(loginname.startsWith("wap_")){  //手机端过来的，费率不一样，做下区分
			isPhone = true ;
			loginname = loginname.replace("wap_", "");
			phoneMsg = "wap";
		}
		
		System.out.println(payName+"支付自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"payName=="+payName+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id",payName));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return  payName+"正在维护！";
			}
			//千网支付宝
			if(payName.equals("千网支付宝")){
				if(isPhone){payFee=98.2;}//手机端费率
				else{payFee=98.2;} //电脑端费率
				kdwxsign="qwzfb;";
				paysign=493;
				fee =0.991;
			}
			//千网微信
			else if(payName.equals("千网微信")){
				if(isPhone){payFee=99.0;} //手机端费率
				else{payFee=99.0;} //电脑端费率
				kdwxsign="qwwx;";
				paysign=495;
				fee =0.992;
			}
			else{
				log.info(payName+"回调失败：该笔订单无法匹配支付平台");
				return payName+"回调失败：该笔订单无法匹配支付平台";
			}   
			
			String str1=phoneMsg+payName+"单号:" + orderNo;
			if(!loginname.equals(payorder.getLoginname())){
				System.out.println(payName+"回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
				return  payName+"回调失败：订单用户和实际用户不一致";
			}
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			if(flag.equals("1")){
				payorder.setIp("127.0.0.1");
			}
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(loginname);
			// 加入银行
			System.out.println(payName+"自动回调money change开始----");
			Double amount4user = Arith.mul(OrdAmt, fee);
			tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1);
			bankinfoDao.changeQwZfAmountOnline(paysign, OrdAmt, orderNo,payFee);
			System.out.println(payName+"回调webService结束----");
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(loginname);
			pb.setPayplatform(kdwxsign); 
			pb.setMoney(OrdAmt);
			pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
	
	public String addSaveOrderXlbZfb(String orderNo, Double OrdAmt, String loginname) {
		Users users = (Users) annDao.get(Users.class, loginname);
		if(users==null){
			return null;
		}
		Payorder payorder = new Payorder();
		payorder.setBillno(orderNo);
		payorder.setPayPlatform("xlbzfb");
		payorder.setMsg("xlbzfb未支付订单号:" + orderNo);
		payorder.setFlag(0);
		payorder.setType(2);
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(OrdAmt);
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setCreateTime(DateUtil.now());
		annDao.save(payorder);
		return "ok";
	}
	
	/******
	 * 迅联宝支付宝
	 */
	@Override 
	public String addPayXlbZfb(String orderNo, Double OrdAmt, String loginname, String flag) {
		Boolean isPhone = false ;
		String phoneMsg = "";
		if(loginname.startsWith("wap_")){  //手机端过来的，费率不一样，做下区分
			isPhone = true ;
			loginname = loginname.replace("wap_", "");
			phoneMsg = "wap";
		}    
		
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("迅联宝支付宝自动回调webService开始----orderNo=="+orderNo+"OrdAmt==="+OrdAmt+"flag=="+flag+"loginname=="+loginname);
		Payorder payorder = (Payorder) annDao.get(Payorder.class, orderNo);
		if(null == payorder){
			return "不存在此订单"+orderNo;
		}
		if (payorder != null  && payorder.getType() == 2 && df.format(payorder.getMoney()).equals(df.format(OrdAmt))) {
			try {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.eq("id", "迅联宝支付宝"));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> constPay = annDao.getHibernateTemplate().findByCriteria(dc);
			// 对于来自第三方支付回调，并且后台关闭此 存款方式的，不处理
			if (flag.equals("0") && (constPay == null || constPay.size() <= 0 || constPay.get(0) == null)) {
				return "在线支付正在维护！";
			}
			String str1=phoneMsg+"迅联宝支付宝单号:" + orderNo;
			if(!loginname.equals(payorder.getLoginname())){
				System.out.println("迅联宝支付宝回调失败：订单用户和实际用户不一致-----订单号："+orderNo+",loginname:"+loginname);
				return "迅联宝支付宝支付回调失败：订单用户和实际用户不一致";
			}
			// 更新订单号
			payorder.setMsg(str1);
			payorder.setType(0);
			if(flag.equals("1")){
				payorder.setIp("127.0.0.1");
			}
			payorder.setReturnTime(DateUtil.now());
			annDao.update(payorder);
			// 更新用户存款方式
			annDao.updateUserSqlIsCashinOnline(loginname);
			// 加入银行
			System.out.println("迅联宝支付宝自动回调changeCredit开始----");
			Double amount4user = Arith.mul(OrdAmt, 0.991);
			tradeDao.changeCredit(loginname, amount4user, CreditChangeType.NETPAY.getCode(), orderNo, str1+";0.8%");
			System.out.println("迅联宝支付宝回调changeCredit结束----");
			System.out.println("迅联宝支付宝回调changeXlbAmountOnline开始----");
			bankinfoDao.changeXlbZfbAmountOnline(497, OrdAmt, orderNo,isPhone);
			System.out.println("迅联宝支付宝回调webService结束----");
			Payorderbillono pb =new Payorderbillono();
			pb.setBillno(orderNo);
			pb.setLoginname(loginname);
			pb.setPayplatform("xlbzfb");
			pb.setMoney(OrdAmt);
			pb.setRemark("插入时间："+DateUtil.formatDateForStandard(new Date()));
			annDao.save(pb);
			return null;
			} catch (Exception e) {
				System.out.println("单号重复,失败:"+payorder+";"+loginname);
				throw new GenericDfhRuntimeException("单号重复,失败");
			}
		} else {
			return "此笔交易已经支付成功";
		}
	}
}
