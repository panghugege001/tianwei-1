package com.nnti.common.utils;



import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;


public class PhoneFeeUtil {

	private static Logger log = Logger.getLogger(PhoneFeeUtil.class);
	
	private static final String KEY = "e7f14944340880dcb373e1e64fe09e67";
//	private static final String OPEN_ID = "JHe772457a3b17879cf5c2a0aff2bd6ee6";
	public static final String onlineUrl="http://op.juhe.cn/ofpay/mobile/onlineorder?key=" + KEY + "&phoneno=!&cardnum=*&orderid=@&sign=$";
	
	
	public static boolean valiedMD5BySign(String sign, String sporder_id, String orderId){
		
		String md5 =DigestUtils.md5Hex(KEY + sporder_id + orderId);
		if(md5.equals(sign)){
			return true;
		}
		return false;
	}
	
	
	public static void main(String[] args) throws Exception {
//		System.out.println(onlineRecharge("15652309116",1,"10000000"));
    }

}
