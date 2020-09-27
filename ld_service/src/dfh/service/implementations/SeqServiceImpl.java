// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SeqServiceImpl.java

package dfh.service.implementations;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.LockMode;

import dfh.model.Actionlogs;
import dfh.model.Seq;
import dfh.model.Userstatus;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.ProposalType;
import dfh.service.interfaces.SeqService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

// Referenced classes of package dfh.service.implementations:
//			UniversalServiceImpl

public class SeqServiceImpl extends UniversalServiceImpl implements SeqService {

	static final Integer NETPAY_DIGITS = Integer.valueOf(8);
	static final Integer PROPOSAL_DIGITS = Integer.valueOf(4);

	public SeqServiceImpl() {
	}

	// 得到在线支付维一订单号
	public String generateNetpayBillno() {
		String id = "";
		Seq seq = (Seq) get(Seq.class, "SEQ_NETPAYID", LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			if (seqvalue.toString().startsWith(DateUtil.getYYMMDD()))
				id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.valueOf(Integer.parseInt(seqvalue.substring(6)) + 1), NETPAY_DIGITS);
			else
				id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.valueOf(1), NETPAY_DIGITS);
			seq.setSeqValue(id);
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_NETPAYID");
			id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.valueOf(1), NETPAY_DIGITS);
			seq.setSeqValue(id);
			save(seq);
		}
		return Constants.NETPAYORDER + id;
	}
	
	public String generatePt8TransferID(Integer userId){
		String prefix = "100000";//共16位    6
		int idLength = userId.toString().length() ;
		int need = 10 - idLength ;
		String middle = "";
		if(need>0){
			for(int i=0 ; i< need ; i++){
				middle += "0";
			}
		}
		return prefix + middle + userId ;
	}
	
	// 用户登录AG平台单号 update sun 15位的转账编号
	public String generateLoginID(String loginname) {
		/*Seq seq = (Seq) get(Seq.class, "SEQ_LOGINID", LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqvalue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_LOGINID");
			seq.setSeqValue("200000000000000");
			save(seq);
		}*/
		Actionlogs actionlog = new Actionlogs();
		actionlog.setLoginname(loginname);
		actionlog.setCreatetime(DateUtil.now());
		actionlog.setAction(ActionLogType.LOGIN_AG_GAME.getCode());
		save(actionlog);
//		return seq.getSeqValue();
//		return DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(15).toLowerCase();
		return DateUtil.getYYMMDDHHmmssSSS4TransferNo()+new Random().nextInt(50);
	}
	
	// 用户登录Bbin平台单号 update sun 15位的转账编号
	public String generateLoginBbinID(String loginname) {
		/*Seq seq = (Seq) get(Seq.class, "SEQ_LOGINID", LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqvalue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_LOGINID");
			seq.setSeqValue("200000000000000");
			save(seq);
		}*/
		Actionlogs actionlog = new Actionlogs();
		actionlog.setLoginname(loginname);
		actionlog.setCreatetime(DateUtil.now());
		actionlog.setAction(ActionLogType.LOGIN_BBIN_GAME.getCode());
		save(actionlog);
//		return seq.getSeqValue();
		return DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(15).toLowerCase();
	}
	
	// 用户登录Keno平台单号 update sun 15位的转账编号
	public String generateLoginKenoID(String loginname) {
		/*Seq seq = (Seq) get(Seq.class, "SEQ_LOGINID", LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqvalue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_LOGINID");
			seq.setSeqValue("200000000000000");
			save(seq);
		}*/
		Actionlogs actionlog = new Actionlogs();
		actionlog.setLoginname(loginname);
		actionlog.setCreatetime(DateUtil.now());
		actionlog.setAction(ActionLogType.LOGIN_KENO_GAME.getCode());
		save(actionlog);
//		return seq.getSeqValue();
		return DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(15).toLowerCase();
	}

	// 用户转入转出单号 update sun 15位的转账编号
	public String generateTransferID() {
		/*Seq seq = (Seq) get(Seq.class, "SEQ_TRANSFERID", LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqvalue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_TRANSFERID");
			seq.setSeqValue("200000000000000");
			save(seq);
		}
		return seq.getSeqValue();*/
		return DateUtil.getYYMMDDHHmmssSSS4TransferNo() + RandomStringUtils.randomNumeric(4);
	}

	public String generateVisitorID() {
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_VISITORID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Integer.parseInt(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_VISITORID);
			seq.setSeqValue("1000000000");
			save(seq);
		}
		return seq.getSeqValue();
	}

	public String generateCommissionID() {
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_COMMISSIONID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Integer.parseInt(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_COMMISSIONID);
			seq.setSeqValue("100000000");
			save(seq);
		}
		return Constants.COMMISSIONORDER + seq.getSeqValue();
	}

	public String generateAgTryGameID() {
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_TRYGAMEID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_TRYGAMEID);
			seq.setSeqValue("10000001");
			save(seq);
		}
		return seq.getSeqValue();
	}
	
	// 获取智付订单号
	public String generateZhiFuOrderNoID(String loginname) {
		
		System.out.println("===================当前玩家用户名--->"+loginname+"==========================");   
		
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);    
		//如果等于null 生成数据
		int numsize = 4000000;  
		if(userstatus !=null){   
			String payorderValue = userstatus.getPayorderValue();   
			int  payordervalue = Integer.parseInt(null == payorderValue?"1400000":payorderValue);  
			if(payordervalue < numsize){
				payorderValue = String.valueOf(numsize);
			}
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);
			userstatus.setPayorderValue("4000000");  
			save(userstatus);
		}
		return userstatus.getPayorderValue();
	}
	
	public String generateZlxID() {
		/*Seq seq = (Seq) get(Seq.class, Constants.SEQ_STATION_LETTERS_ID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_STATION_LETTERS_ID);
			seq.setSeqValue("1");
			save(seq);
		}
		return seq.getSeqValue();*/
		return  DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}
	
	public String generateProposalPno(ProposalType type) {
		/*String id = "";
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_PROPOSALID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_PROPOSALID);
			seq.setSeqValue("1510400000");
			save(seq);
		}
		return  type.getCode()+seq.getSeqValue();*/
		return  type.getCode() + DateUtil.getYYMMDD() + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}
	
	public String generateYhjID() {
		/*Seq seq = (Seq) get(Seq.class, Constants.SEQ_COUPON_ID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_COUPON_ID);
			seq.setSeqValue("1");
			save(seq);
		}
		return seq.getSeqValue();*/
		return "yhj" + DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}
	
	// 获取海尔订单号
	public String generateHaierOrderNoID(String loginname) {
		Seq seq = (Seq) get(Seq.class, Constants.SEO_HAIERORDERNO, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEO_HAIERORDERNO);
			seq.setSeqValue("1");
			save(seq);
		}
		return "e68_"+loginname+"_"+seq.getSeqValue();
	}
	
	
	
	// 获取汇付订单号
	public String generateHfOrderNoID() {
		Seq seq = (Seq) get(Seq.class, Constants.SEO_HFORDERNO, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEO_HFORDERNO);
			seq.setSeqValue("1");
			save(seq);
		}
		return "e68_"+seq.getSeqValue();
	}
	
	// 获取汇潮订单号
	public String generateHCOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			int  payordervalue = Integer.parseInt(null == payorderValue?"1400000":payorderValue);
			if(payordervalue < numsize){
				payorderValue = String.valueOf(numsize);
			}  
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();   
			userstatus.setLoginname(loginname);
			userstatus.setPayorderValue("4000000");   
			save(userstatus);
		}
		return "e_"+loginname+"_"+userstatus.getPayorderValue();
	}
	
	// 获取汇潮一麻袋订单号
	public String generateHCYmdOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			int  payordervalue = Integer.parseInt(null == payorderValue?"4000000":payorderValue);
			if(payordervalue <= numsize){
				payorderValue = String.valueOf(numsize);
			}
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return loginname+"_"+userstatus.getPayorderValue();   
	}
	
	// 获取币付宝订单号
	public String generateBfbOrderNoID() {
		Seq seq = (Seq) get(Seq.class, Constants.SEO_BFBORDERNO, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEO_BFBORDERNO);
			seq.setSeqValue("1");
			save(seq);
		}
		return "e_"+seq.getSeqValue();
		
	}
	
	// 获取国付宝订单号
	public String generateGfbOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			int  payordervalue = Integer.parseInt(null == payorderValue?"1400000":payorderValue);
			if(payordervalue < numsize){
				payorderValue = String.valueOf(numsize);
			}
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return "e68_gfb_"+loginname+"_"+userstatus.getPayorderValue();
	}
	
	//获取支付宝订单号
	public String generateZfbOrderNoID(){
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_ZFBORDERNO, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_ZFBORDERNO);
			seq.setSeqValue("1");
			save(seq);
		}
		return "zfb_"+seq.getSeqValue();
	}
	
	//获取微信订单号
		public String generateWeiXinOrderNoID(){ 
			Seq seq = (Seq) get(Seq.class, Constants.SEQ_WEIXINORDERNO, LockMode.UPGRADE);
			if (seq != null) {
				String seqValue = seq.getSeqValue();
				seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
				update(seq);
			} else {
				seq = new Seq();
				seq.setSeqName(Constants.SEQ_WEIXINORDERNO);
				seq.setSeqValue("1");
				save(seq);
			}
			return seq.getSeqValue();
		}
		
		
		// 获取乐富微信支付订单号
		public String generateLfWxOrderNoID(String loginname) {
			Seq seq = (Seq) get(Seq.class, Constants.SEO_LFWXORDERNO, LockMode.UPGRADE);
			if (seq != null) {
				String seqValue = seq.getSeqValue();
				seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
				update(seq);
			} else {
				seq = new Seq();
				seq.setSeqName(Constants.SEO_LFWXORDERNO);
				seq.setSeqValue("1");
				save(seq);
			}
			return "lfwx_"+loginname+"_"+seq.getSeqValue();
		}

		// 获取乐富微信支付订单号
		public String generateXinBOrderNoID(String loginname) {
			Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
			//如果等于null 生成数据
			int numsize = 1400000;
			if(userstatus !=null){
				String payorderValue = userstatus.getPayorderValue();
				int  payordervalue = Integer.parseInt(null == payorderValue?"1400000":payorderValue);
				if(payordervalue <= numsize){
					payorderValue = String.valueOf(numsize);
				}
				userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
				update(userstatus);
			} else {
			    userstatus = new Userstatus();
				userstatus.setLoginname(loginname);
				userstatus.setPayorderValue("4000000");
				save(userstatus);
			}
			return "xbwx_"+loginname+"_"+userstatus.getPayorderValue();
		}
		
		public String generateKdZfOrderNoID(String loginname) {
			Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
			//如果等于null 生成数据
			int numsize = 4000000;
			if(userstatus !=null){
				String payorderValue = userstatus.getPayorderValue();
				int  payordervalue = Integer.parseInt(null == payorderValue?"1400000":payorderValue);
				if(payordervalue < numsize){
					payorderValue = String.valueOf(numsize);
				}
				userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
				update(userstatus);
			} else {
			    userstatus = new Userstatus();
				userstatus.setLoginname(loginname);
				userstatus.setPayorderValue("4000000");
				save(userstatus);
			}
			return "e68_kd_"+loginname+"_"+userstatus.getPayorderValue();
		}
		
	public String generateHhbZfOrderNoID(String loginname) {
		Seq seq = (Seq) get(Seq.class, Constants.SEO_HHBZFORDERNO, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEO_HHBZFORDERNO);
			seq.setSeqValue("1");
			save(seq);
		}
		return "e68_hhb_"+loginname+"_"+seq.getSeqValue();
	}
	
	
	//汇付宝微信支付
	public String generateHhbWxZfOrderNoID(String loginname) {
		Seq seq = (Seq) get(Seq.class, Constants.SEO_HHBWXZFORDERNO, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEO_HHBWXZFORDERNO);
			seq.setSeqValue("1");
			save(seq);
		}
		return "e68_hhbwx_"+loginname+"_"+seq.getSeqValue();
	}
	
	
	/****
	 * 获得聚宝支付宝订单号
	 */
	public String generateJubZfbOrderNoID(String loginname) {
		Seq seq = (Seq) get(Seq.class, Constants.SEO_JUBZFBORDERNO, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEO_JUBZFBORDERNO);
			seq.setSeqValue("1");
			save(seq);
		}
		return "e68_jub_"+loginname+"_"+seq.getSeqValue();
	}
	
	//迅联宝微信支付
	public String generateXlbOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			int  payordervalue = Integer.parseInt(null == payorderValue?"1400000":payorderValue);
			if(payordervalue < numsize){
				payorderValue = String.valueOf(numsize);
			}
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return "e68_xlb_"+loginname+"_"+userstatus.getPayorderValue();
	}
	
	
	
	//迅联宝网银支付
	public String generateXlbWyOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			int  payordervalue = Integer.parseInt(null == payorderValue?"1400000":payorderValue);
			if(payordervalue < numsize){
				payorderValue = String.valueOf(numsize);
			}
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return "e68_xlbwy_"+loginname+"_"+userstatus.getPayorderValue();
	}
	
	
	//优付支付宝 and 微信
	public String generateYfZfOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			if(payorderValue == null || payorderValue.equals("")){
				payorderValue = "4000000";
			}
			int  payordervalue = Integer.parseInt(payorderValue);
			if(payordervalue <= numsize){
				payorderValue = String.valueOf(numsize);
			}   
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);  
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return loginname+"_"+userstatus.getPayorderValue();
	}	

	
	
	
	/***
	 * 口袋微信支付
	 */
	public String generateKdWxZfOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			System.out.println(payorderValue+"-----payorderValue------");
			int  payordervalue = Integer.parseInt(null == payorderValue?"1800000":payorderValue);
			if(payordervalue < numsize){
				payorderValue = String.valueOf(numsize);
			}   
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);  
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return "e68_kdwx_"+loginname+"_"+userstatus.getPayorderValue();
	}
	
	/***
	 * 口袋微信支付2
	 */
	public String generateKdWxZfOrderNoID2(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			System.out.println(payorderValue+"-----payorderValue------");
			int  payordervalue = Integer.parseInt(null == payorderValue?"1800000":payorderValue);
			if(payordervalue < numsize){
				payorderValue = String.valueOf(numsize);
			}   
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);  
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return "e68_kdwx2_"+loginname+"_"+userstatus.getPayorderValue();
	}
	
	/*****
	 * 口袋微信支付1 and 口袋微信支付2 and 口袋微信支付3
	 */
	public String generateKdWxZfsOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 1400000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			int  payordervalue = Integer.parseInt(null == payorderValue?"1400000":payorderValue);
			if(payordervalue <= numsize){
				payorderValue = String.valueOf(numsize);
			}
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return loginname+"_"+userstatus.getPayorderValue();
	}
	
	// 获取新贝支付宝支付订单号
	public String generateXinBZfbOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 1400000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			int  payordervalue = Integer.parseInt(null == payorderValue?"1400000":payorderValue);
			if(payordervalue <= numsize){
				payorderValue = String.valueOf(numsize);
			}
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return "e68_xbzfb_"+loginname+"_"+userstatus.getPayorderValue();
	}
	
	//银宝支付宝
	public String generateYbZfbOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			if(payorderValue == null || payorderValue.equals("")){
				payorderValue = "4000000";
			}
			int  payordervalue = Integer.parseInt(payorderValue);
			if(payordervalue <= numsize){
				payorderValue = String.valueOf(numsize);
			}   
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);  
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return loginname+"_"+userstatus.getPayorderValue();
	}	
	
	//千网支付宝
	public String generateQwZfOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			if(payorderValue == null || payorderValue.equals("")){
				payorderValue = "4000000";
			}
			int  payordervalue = Integer.parseInt(payorderValue);
			if(payordervalue <= numsize){
				payorderValue = String.valueOf(numsize);
			}   
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);  
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return loginname+"_"+userstatus.getPayorderValue();
	}

	//迅联宝支付宝
	public String generateXlbZfbOrderNoID(String loginname) {
		Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
		//如果等于null 生成数据
		int numsize = 4000000;
		if(userstatus !=null){
			String payorderValue = userstatus.getPayorderValue();
			int  payordervalue = Integer.parseInt(payorderValue);
			if(payordervalue < numsize){
				payorderValue = String.valueOf(numsize);
			}
			userstatus.setPayorderValue(Long.parseLong(payorderValue)+1+"");
			update(userstatus);
		} else {
		    userstatus = new Userstatus();
			userstatus.setLoginname(loginname);
			userstatus.setPayorderValue("4000000");
			save(userstatus);
		}
		return "e68_xlbzfb_"+loginname+"_"+userstatus.getPayorderValue();
	}
}
