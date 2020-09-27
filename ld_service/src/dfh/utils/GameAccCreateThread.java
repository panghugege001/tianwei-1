package dfh.utils;

import dfh.security.EncryptionUtil;

public class GameAccCreateThread extends Thread{
	
	private String loginname;
	private String password;
	
	public GameAccCreateThread(String loginname,String password){
		this.loginname=loginname;
	    this.password=password;
	}
	
	public void run(){
		try {
			//创建PT账号
			PtUtil.createPlayerName(loginname, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//创建SW账号
			SWUtil.createPlayer(loginname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//创建DT账号
			DtUtil.createPlayerName(loginname, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//创建沙巴体育账号
			ShaBaUtils.CreateMember(loginname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//创建mg账号
			//MGSUtil.createPlayerName(loginname, EncryptionUtil.encryptPassword(password));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
