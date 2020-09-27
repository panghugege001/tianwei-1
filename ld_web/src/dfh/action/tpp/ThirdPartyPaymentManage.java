package dfh.action.tpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dfh.model.Const;
import dfh.model.PayMerchant;
import dfh.model.Users;
import dfh.model.enums.VipLevel;
import dfh.utils.AlipayUtil;
import dfh.utils.AxisUtil;

/**
 * 第三方線上支付管理
 * 提供統一的接口給Action使用
 * @author clare
 *
 */
public class ThirdPartyPaymentManage {
	private static ThirdPartyPaymentManage manage;
	
	//user.getLevel() >= VipLevel.COMMON.getCode().intValue()
	private Map<String,ThirdPartyPayment> tpps1;
	//user.getLevel() < VipLevel.COMMON.getCode().intValue()
	private Map<String,ThirdPartyPayment> tpps2;
	
	ThirdPartyPaymentManage(){
		tpps1 = new HashMap<String,ThirdPartyPayment>();
		tpps1.put("智付2",new ZFThirdPartyPayment2("智付2","在线支付页面","https://pay.dinpay.com/gateway?input_charset=UTF-8"));
		tpps1.put("智付3",new ZFThirdPartyPayment3("智付3","在线支付页面","https://pay.dinpay.com/gateway?input_charset=UTF-8"));
		tpps1.put("汇付",new HFThirdPartyPayment("汇付","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps1.put("汇潮",new HCThirdPartyPayment("汇潮","在线支付页面","http://www.gzhuipingkeji.com/ymdpay.jsp")); 
		tpps1.put("汇潮网银",new HCYmdThirdPartyPayment("汇潮网银","在线支付页面","http://www.zhkangtsmy.com/ymdpay.jsp"));
		tpps1.put("环迅",new IPShirdPartyPayment("环迅","在线支付页面","http://www.haoleyigo.com/redirect.jsp"));
		tpps1.put("智付1",new ZFThirdPartyPayment1("智付1","在线支付页面","https://pay.dinpay.com/gateway?input_charset=UTF-8"));
		tpps1.put("汇付1",new HFThirdPartyPayment("汇付1","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps1.put("汇付2",new HFThirdPartyPayment("汇付2","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps1.put("汇付3",new HFThirdPartyPayment("汇付3","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps1.put("汇付4",new HFThirdPartyPayment("汇付4","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps1.put("汇付5",new HFThirdPartyPayment("汇付5","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps1.put("汇付6",new HFThirdPartyPayment("汇付6","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps1.put("汇付7",new HFThirdPartyPayment("汇付7","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
//		tpps1.put("智付点卡", new ZFDKThirdPartyPayment("智付点卡", "点卡支付", "http://pay.kalunrantu.cn/paybank.asp"));
		tpps1.put("支付宝",new ZFBThirdPartyPayment("支付宝","支付宝","https://mapi.alipay.com/gateway.do?_input_charset="+AlipayUtil.Config.input_charset));
		tpps1.put("币付宝",new BFBThirdPartyPayment("币付宝","在线支付页面","http://ligeh888.cn/paybank.asp"));
		tpps1.put("通用智付1",new CZFThirdPartyPayment1("通用智付1","在线支付页面","http://www.anyuanxinkeji.com/zfpay.jsp"));
		tpps1.put("通用智付2",new CZFThirdPartyPayment2("通用智付2","在线支付页面","http://www.zhkangtsmy.com/zfpay.jsp"));
		tpps1.put("国付宝1",new GFBThirdPartyPayment("国付宝1","在线支付页面","http://pay.fumeihedu.cn/paybank.asp"));
		tpps1.put("乐富微信",new LFWXThirdPartyPayment("乐富微信","微信支付","https://pay.lefu8.com/gateway/trade.htm"));
		tpps1.put("新贝微信",new XBWXThirdPartyPayment("新贝微信","微信支付","http://gaoguangya.com/xbPaybank.jsp"));
		//tpps1.put("口袋支付宝",new KDZFBThirdPartyPayment("口袋支付宝","支付宝在线支付","http://Api.Duqee.Com/pay/Bank.aspx"));
		tpps1.put("口袋微信支付",new KDWXThirdPartyPayment("口袋微信支付","极速微信","https://api.duqee.com/pay/KDBank.aspx"));
		tpps1.put("口袋微信支付2",new KDWXThirdPartyPayment2("口袋微信支付2","极速微信","https://api.duqee.com/pay/KDBank.aspx"));
		//tpps1.put("口袋微信支付3",new KDWX3ThirdPartyPayment("口袋微信支付3","极速微信","http://Api.Duqee.Com/pay/Bank.aspx"));
		tpps1.put("口袋支付宝2",new KDZFB2ThirdPartyPayment("口袋支付宝2","支付宝在线支付","http://www.zhdingtsmy.com/kdzf.jsp"));
		tpps1.put("汇付宝微信",new HFBWXThirdPartyPayment("汇付宝微信","微信支付","https://pay.heepay.com/Payment/Index.aspx"));
		tpps1.put("迅联宝",new XLBWXThirdPartyPayment("迅联宝","微信支付","mobile/weixinqr/xlbwx.jsp"));
		tpps1.put("海尔支付",new CommonHaierThirdPartyPayment("海尔支付","在线支付页面","http://pay.mengruoling.cn/paybankHaier.asp"));
		tpps1.put("迅联宝网银",new XLBWYThirdPartyPayment("迅联宝网银","在线支付页面","http://www.glubanyunshang.com/xlbWy.jsp"));
		tpps1.put("优付支付宝",new YFZFBThirdPartyPayment("优付支付宝","支付宝在线支付","http://www.glubanyunshang.com/yfzf.jsp")); 
		tpps1.put("新贝支付宝",new XBZFBThirdPartyPayment("新贝支付宝","支付宝在线支付","http://www.yuxinbaokeji.com/xbzfbPaybank.jsp"));
		tpps1.put("银宝支付宝",new YBZFBThirdPartyPayment("银宝支付宝","支付宝在线支付","http://www.xinyisifuzhuangmy.com/ybpay.jsp")); 
		//如果要更改[银宝支付宝url] 记得前往 TPPManage.js 的 that.pay 把里面特别判断是否为银宝支付宝的url更改掉
		tpps1.put("优付微信",new YFWXThirdPartyPayment("优付微信","微信支付","http://www.glubanyunshang.com/yfzf.jsp"));
		tpps1.put("千网支付宝",new QWZFBThirdPartyPayment("千网支付宝","支付宝在线支付","http://apika.10001000.com/chargebank.aspx"));
		tpps1.put("千网微信",new QWWXThirdPartyPayment("千网微信","微信支付","http://apika.10001000.com/chargebank.aspx"));
		tpps1.put("迅联宝支付宝",new XLBZFBThirdPartyPayment("迅联宝支付宝","支付宝在线支付","mobile/zfbqr/xlbzfb.jsp")); 
		
		
		tpps2 = new HashMap<String,ThirdPartyPayment>();
		tpps2.put("智付1",new ZFThirdPartyPayment1("智付1","在线支付页面","https://pay.dinpay.com/gateway?input_charset=UTF-8"));
		tpps2.put("汇付",new HFThirdPartyPayment("汇付","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps2.put("汇潮",new HCThirdPartyPayment("汇潮","在线支付页面","http://www.gzhuipingkeji.com/ymdpay.jsp"));
		tpps2.put("汇潮网银",new HCYmdThirdPartyPayment("汇潮网银","在线支付页面","http://www.zhkangtsmy.com/ymdpay.jsp"));
		tpps2.put("环迅",new IPShirdPartyPayment("环迅","在线支付页面","http://www.haoleyigo.com/redirect.jsp"));
		tpps2.put("汇付1",new HFThirdPartyPayment("汇付1","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps2.put("汇付2",new HFThirdPartyPayment("汇付2","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps2.put("汇付3",new HFThirdPartyPayment("汇付3","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps2.put("汇付4",new HFThirdPartyPayment("汇付4","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do")); 
		tpps2.put("汇付5",new HFThirdPartyPayment("汇付5","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps2.put("汇付6",new HFThirdPartyPayment("汇付6","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
		tpps2.put("汇付7",new HFThirdPartyPayment("汇付7","在线支付页面","http://mas.chinapnr.com/gar/RecvMerchant.do"));
//		tpps2.put("智付点卡",new ZFDKThirdPartyPayment("智付点卡","支付点卡","https://pay.dinpay.com/gateway"));
		tpps2.put("支付宝",new ZFBThirdPartyPayment("支付宝","支付宝","https://mapi.alipay.com/gateway.do?_input_charset="+AlipayUtil.Config.input_charset));
		tpps2.put("币付宝",new BFBThirdPartyPayment("币付宝","在线支付页面","http://ligeh888.cn/paybank.asp"));
		tpps2.put("通用智付1",new CZFThirdPartyPayment1("通用智付1","在线支付页面","http://www.anyuanxinkeji.com/zfpay.jsp"));
		tpps2.put("通用智付2",new CZFThirdPartyPayment2("通用智付2","在线支付页面","http://www.zhkangtsmy.com/zfpay.jsp"));
		tpps2.put("国付宝1",new GFBThirdPartyPayment("国付宝1","在线支付页面","http://pay.fumeihedu.cn/paybank.asp"));
		tpps2.put("乐富微信",new LFWXThirdPartyPayment("乐富微信","微信支付","https://pay.lefu8.com/gateway/trade.htm"));
		tpps2.put("新贝微信",new XBWXThirdPartyPayment("新贝微信","微信支付","http://gaoguangya.com/xbPaybank.jsp"));
		//tpps2.put("口袋支付宝",new KDZFBThirdPartyPayment("口袋支付宝","支付宝在线支付","http://Api.Duqee.Com/pay/Bank.aspx"));
		tpps2.put("口袋微信支付",new KDWXThirdPartyPayment("口袋微信支付","极速微信","https://api.duqee.com/pay/KDBank.aspx"));
		tpps2.put("口袋微信支付2",new KDWXThirdPartyPayment2("口袋微信支付2","极速微信","https://api.duqee.com/pay/KDBank.aspx"));
		tpps2.put("口袋支付宝2",new KDZFB2ThirdPartyPayment("口袋支付宝2","支付宝在线支付","http://www.zhdingtsmy.com/kdzf.jsp"));
		//tpps2.put("口袋微信支付3",new KDWX3ThirdPartyPayment("口袋微信支付3","极速微信","http://Api.Duqee.Com/pay/Bank.aspx"));
		tpps2.put("汇付宝微信",new HFBWXThirdPartyPayment("汇付宝微信","微信支付","https://pay.heepay.com/Payment/Index.aspx"));
		tpps2.put("迅联宝",new XLBWXThirdPartyPayment("迅联宝","微信支付","mobile/weixinqr/xlbwx.jsp"));
		tpps2.put("海尔支付",new CommonHaierThirdPartyPayment("海尔支付","在线支付页面","http://pay.mengruoling.cn/paybankHaier.asp"));
		tpps2.put("迅联宝网银",new XLBWYThirdPartyPayment("迅联宝网银","在线支付页面","http://www.glubanyunshang.com/xlbWy.jsp"));
		tpps2.put("优付支付宝",new YFZFBThirdPartyPayment("优付支付宝","支付宝在线支付","http://www.glubanyunshang.com/yfzf.jsp")); 
		tpps2.put("新贝支付宝",new XBZFBThirdPartyPayment("新贝支付宝","支付宝在线支付","http://www.yuxinbaokeji.com/xbzfbPaybank.jsp"));
		tpps2.put("银宝支付宝",new YBZFBThirdPartyPayment("银宝支付宝","支付宝在线支付","http://www.xinyisifuzhuangmy.com/ybpay.jsp")); 
		//如果要更改[银宝支付宝url] 记得前往 TPPManage.js 的 that.pay 把里面特别判断是否为银宝支付宝的url更改掉
		tpps2.put("优付微信",new YFWXThirdPartyPayment("优付微信","微信支付","http://www.glubanyunshang.com/yfzf.jsp"));
		tpps2.put("千网支付宝",new QWZFBThirdPartyPayment("千网支付宝","支付宝在线支付","http://apika.10001000.com/chargebank.aspx"));
		tpps2.put("千网微信",new QWWXThirdPartyPayment("千网微信","微信支付","http://apika.10001000.com/chargebank.aspx"));
		tpps2.put("迅联宝支付宝",new XLBZFBThirdPartyPayment("迅联宝支付宝","支付宝在线支付","mobile/zfbqr/xlbzfb.jsp")); 
	}
	
	public static ThirdPartyPaymentManage getInstance(){
		if(manage==null){
			initialize();
		}
		return manage;
	}

	//避免出现执行绪不安全
	private synchronized static void initialize(){
		if(manage ==null){
			manage = new ThirdPartyPaymentManage();
		}
	}
	
	private boolean tppsContainsKey(String id,boolean vip){
		if(vip){
			return tpps1.containsKey(id);
		}else{
			return tpps2.containsKey(id);
		}
	}
	
	//避免出现执行绪不安全
	private ThirdPartyPayment getTpp(String id,boolean vip){
		try {
			if(vip){
				return tpps1.get(id).clone();
			}else{
				return tpps2.get(id).clone();
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tpps1.get(id);
	}
	
	/**
	 * 根據支付方式取得對應的第三方線上支付
	 * @param user
	 * @param payId
	 * @return
	 */
	public ThirdPartyPayment getThirdPartyPayment(Users user,String payId){
		try{
			List<Const> consts = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDepositSwitch", new Object[] { 1, 50 }, Const.class);
			List<PayMerchant> payMers = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectPayMerchant", new Object[] {"1",""}, PayMerchant.class);
			
			//判斷VIP
			boolean vip = user.getLevel() >= VipLevel.COMMON.getCode().intValue();
			
			for (Const constPay : consts){
				if(constPay.getId().equals(payId)&&tppsContainsKey(constPay.getId(),vip)){
					
					ThirdPartyPayment tpp = getTpp(constPay.getId(),vip);
					tpp.setWork("1".equals(constPay.getValue()));
//					tpp.setWork(true);
					return tpp;
				}
			}
			
			for (PayMerchant payMerchant : payMers){
				if(payMerchant.getConstid().equals(payId)&&tppsContainsKey(payMerchant.getConstid(),vip)){
					ThirdPartyPayment tpp = getTpp(payMerchant.getConstid(),vip);
					tpp.setWork(true);
					return tpp;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 取得目前可使用的第三方支付清單
	 * @param user
	 * @return
	 */
	public List<Map<String,String>> getWorkThirdPartyPayments(Users user){
		List<Map<String,String>> payList = new ArrayList<Map<String,String>>();
		
		try{
			List<Const> consts = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDepositSwitch", new Object[] { 1, 50 }, Const.class);
			List<PayMerchant> payMers = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectPayMerchant", new Object[] {"1",""}, PayMerchant.class);
			
			//判斷VIP
			boolean vip = user.getLevel() >= VipLevel.COMMON.getCode().intValue();
			
			Map<String,String> m = null;
			for (Const constPay : consts){
				if(tppsContainsKey(constPay.getId(),vip)&&"1".equals(constPay.getValue())){
//				if(tppsContainsKey(constPay.getId(),vip)){
					m = new HashMap<String,String>();
					m.put("value", constPay.getId());
					m.put("name",getTpp(constPay.getId(),vip).getName());
					payList.add(m);
				}
			}
			
			if(null!=payMers&&payMers.size()>0){
				for (PayMerchant paymerchant : payMers){
					if(tppsContainsKey(paymerchant.getConstid(),vip)){
						m = new HashMap<String,String>();
						m.put("value", paymerchant.getConstid());
						m.put("name",getTpp(paymerchant.getConstid(),vip).getName());
						payList.add(m);
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return payList;
	}
	
}
