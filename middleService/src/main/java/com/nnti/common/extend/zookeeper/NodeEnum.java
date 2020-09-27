package com.nnti.common.extend.zookeeper;

public enum NodeEnum {
	
	USER_LOCK_FOR_UPDATE("ULFU"),
	//提款回调锁
	WITHDRAW_LOCK("WITHDRAW");
	
	private String nodePath;
	
	private NodeEnum(String nodePath) {
		this.nodePath = nodePath;
	}
	
	public String getNodePath() {
		return nodePath;
	}
	
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
}