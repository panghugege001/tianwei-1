package dfh.utils;

import org.apache.log4j.Logger;

import dfh.utils.DtUtil.ErrorCode;

public class DtThread extends Thread{
	
	private static Logger log = Logger.getLogger(DtThread.class);
	private String loginname;
	private String password;
	
	public DtThread(String loginname,String password){
		this.loginname=loginname;
	    this.password=password;
	}
	
	public void run(){

	}
}
