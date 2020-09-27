package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.AnnouncementVO;
import dfh.model.Const;
import dfh.model.Payorder;
import dfh.model.Users;
import dfh.model.enums.CreditChangeType;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.Page;

public interface AnnouncementService {

	
	// 分页用；查询所有
	List queryAll(int offset,int length);
	
	// index.jsp页面，公告栏显示的部分公告；倒序；
	List query();
	//公告的分页用 第一行 最后一行
	public List querybyPage(int first,int maxresult);
	
	// 分页用，返回总行数
	int totalCount();
	
	AnnouncementVO getAnnouncement(int aid);
	
	// 首页置顶公告
	List queryTopNews();
	
	
 List queryDataEuro();
	
	//在线存款返回成功页面处理
	
	String addPayorder(String billno,Double money,String loginname,String msg,String date, String type);
	
	String addPayorderZf(String billno, Double money,String loginname, String msg,String date,String merchant_code, String type);
	
	String addPayorderDianKaZf(String billno, Double money,String loginname, String msg,String date,String merchant_code, String type);
	
	 String addPayorderHaier(String billno, Double money,String loginname, String merchant_code, String type);
	 
	String addPayQwZf(String orderNo, Double OrdAmt,String payName, String loginname,String flag);
	
	/**
	 * 智付点卡V3.0 录入成功的订单
	 * @param billno    智付方生成的账单号
	 * @param money     金额
	 * @param loginname  用户名
	 * @param card_code  点卡类型
	 * @param merchant_code    商户号
	 * @param type   类型（回调/补单）
	 * @return
	 */
	String addPayorder4DCard(String billno, Double money, String loginname, String card_code, String merchant_code, String type);
	
	/**
	 * 支付宝 flag	区分第三方支付回调与平台自动补单  0：第三方支付  1：平台补单
	 */
	String addPayorderZfb(String outtradeno, Double money, String loginname, String tradeno, String date, String flag);
	
	 /**
     * 自动--- 汇付
     * @param orderNo
     * @param OrdAmt
     * @param loginname
     * @return
     */
    String addSaveOrderHf(String orderNo, Double OrdAmt, String loginname,String constID);
	
    String addSaveOrderHf(String orderNo, Double OrdAmt, String loginname);
    
    /**
     * 汇付的 动态支付
     * @param orderNo
     * @param OrdAmt
     * @param loginname
     * @param msg
     * @param flag
     * @param merchantcode
     * @return
     */
    String addPayorderHf1(String orderNo, Double OrdAmt, String loginname, String msg, String flag,String merchantcode);
    
    String addPayorderHf(String orderNo, Double OrdAmt, String loginname, String msg, String type);
    
	//刷新session用,获取新的users信息
	
	Users getUser(String loginname);

	Page selectWinPoint(Date startTime, Date endTime, Integer betTotal,
			String by, String order, Integer pageIndex, Integer size);
	
	String addSaveOrderHC(String orderNo, Double OrdAmt, String loginname);
	
	String addSaveOrderHCYmd(String orderNo, Double OrdAmt, String loginname,String payfly);
	
	public String updatePayorderHC(String orderNo, Double OrdAmt, String type) ;
	
	public String updatePayorderHCYmd(String orderNo, Double OrdAmt, String merNo, String type) ;
	
	String addSaveOrderBfb(String orderNo, Double OrdAmt, String loginname);

	String updatePayorderBfb(String orderNo, Double OrdAmt, String type);
	
	String addSaveOrderGfb(String orderNo, Double OrdAmt, String loginname, String code);
    
    String updatePayorderGfb(String orderNo, Double OrdAmt, String code, String type);
    
    /**
     * 
     * @param orderNo订单号
     * @param money订单金额
     * @param loginname玩家账号
     * @return
     */
	String addPayorderWeiXin(String orderNo, Double money, String loginname);
	
	  /**
     * 
     * @param orderNo订单号
     * @param money订单金额
     * @param loginname玩家账号
     * @return
     */
	String weixinpayreturn(String out_trade_no, String flag,String OrdAmt);
	
	   
    /**
     * 乐富微信支付回调
     * @param orderNo
     * @param OrdAmt
     * @param loginname
     * @param flag
     * @return
     */
	String addPayLfWxzf(String orderNo, Double OrdAmt,String loginname,String flag);
	
	 /**
	  * 乐富微信支付
	  * @param orderNo
	  * @param OrdAmt
	  * @param loginname
	  * @return
	  */
    String addSaveOrderLfwx(String orderNo, Double OrdAmt, String loginname);
    
    /**
	 * 新贝微信回调
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param flag
	 * @return
	 */
	String addPayXbWxzf(String orderNo, Double OrdAmt,String loginname,String flag);
	
    
    /**
     * 新贝微信支付
     * @param orderNo
     * @param OrdAmt
     * @param loginname
     * @return
     */
    String addSaveOrderXinBwx(String orderNo, Double OrdAmt, String loginname);
    
    String addSaveOrderKdZf(String orderNo, Double OrdAmt, String loginname);
    
    String addSaveOrderHhbZf(String orderNo, Double OrdAmt, String loginname);
    
    String addSaveOrderHhbWxZf(String orderNo, Double OrdAmt, String loginname);
    
    
    String addSaveOrderJubZfb(String orderNo, Double OrdAmt, String loginname);
    
    String addSaveOrderXlb(String orderNo, Double OrdAmt, String loginname);
    
    String addSaveOrderXlbWy(String orderNo, Double OrdAmt, String loginname);
    
    String addSaveOrderXlbZfb(String orderNo, Double OrdAmt, String loginname);
    
    String addPayXlbZfb(String orderNo, Double OrdAmt,String loginname,String flag);
    
    String addSaveOrderYfZf(String orderNo, Double OrdAmt,String loginname,String payfly);
    
    String addSaveOrderYbZfb(String orderNo, Double OrdAmt,String loginname,String payfly);
    
    String addPayKdZf(String orderNo, Double OrdAmt,String loginname,String flag);
    
    String addPayHhbZf(String orderNo, Double OrdAmt, String loginname, String flag);
    
	
	String addPayKdWxZf(String orderNo, Double OrdAmt, String loginname, String flag);
	
    /**
     * 口袋微信2支付回调处理
     * @param orderNo
     * @param OrdAmt
     * @param loginname
     * @param flag
     * @return
     */
    String addPayKdWxZf2(String orderNo, Double OrdAmt, String loginname, String flag);
	
    /****
     * 口袋微信支付1 and 口袋微信支付2 and 口袋微信支付3 回调处理
     * @param orderNo
     * @param OrdAmt
     * @param loginname
     * @param flag
     * @return
     */
    String addPayKdWxZfs(String orderNo, Double OrdAmt,String payName, String loginname, String flag);
    
	String addSaveOrderKdWxZf(String orderNo, Double OrdAmt, String loginname);
	
    /****
     * 口袋微信支付2
     * @param orderNo
     * @param OrdAmt
     * @param loginname
     * @return
     */
    String addSaveOrderKdWxZf2(String orderNo, Double OrdAmt, String loginname);
	
    /****
     *  口袋微信支付1 and 口袋微信支付2 and 口袋微信支付3
     * @param orderNo
     * @param OrdAmt
     * @param loginname
     * @return
     */
    String addSaveOrderKdWxZfs(String orderNo, Double OrdAmt, String loginname,String payfly);
    
    String addPayHhbWxZf(String orderNo, Double OrdAmt, String loginname, String flag);
    
    /***
     * 聚宝支付宝
     * @param orderNo
     * @param OrdAmt
     * @param loginname
     * @param flag
     * @return
     */
    String addPayJubZfb(String orderNo, Double OrdAmt, String loginname, String flag);
    
    
    String addPayXlb(String orderNo, Double OrdAmt,String loginname,String flag);
    
    String addPayXlbWy(String orderNo, Double OrdAmt,String loginname,String flag);
    
    String addPayYfZf(String orderNo, Double OrdAmt,String payName,String loginname,String flag);
    
    String addPayYbZfb(String orderNo, Double OrdAmt,String payName, String loginname,String flag);
    
    /*****
     * 新贝支付宝支付
     * @param orderNo
     * @param OrdAmt
     * @param loginname
     * @return
     */
    String addSaveOrderXinBZfb(String orderNo, Double OrdAmt, String loginname);
    
    String addPayXbZfbzf(String orderNo, Double OrdAmt,String loginname,String flag);
    
    String addSaveOrderQwZf(String orderNo, Double OrdAmt,String loginname,String payfly);
}