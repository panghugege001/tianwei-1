package com.nnti.common.extend.zookeeper;

import com.nnti.common.utils.ConfigUtil;

public class GenerateNodePath {
	
	public static String brandPrefix = ConfigUtil.getValue("brand.prefix");
	
	public static String generateUserLockForUpdate(String loginname) {
		loginname = loginname.replace("wap_", "");  //针对在线支付前缀的处理
		return "/" + brandPrefix + "/" + NodeEnum.USER_LOCK_FOR_UPDATE.getNodePath() + "/_" + loginname.substring(0, 1) + "/" + loginname;
	}
	
	public static String generateWithdrawNofifyLock(String loginname) {
		
		return "/" + brandPrefix + "/" + NodeEnum.WITHDRAW_LOCK.getNodePath()  + "/_" + loginname.substring(0, 1) + "/" + loginname;
	}
}