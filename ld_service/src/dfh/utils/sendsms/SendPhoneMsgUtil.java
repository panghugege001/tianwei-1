package dfh.utils.sendsms;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shcm.bean.SendResult;
import com.shcm.send.DataApi;
import com.shcm.send.OpenApi;

//时代互联
public class SendPhoneMsgUtil extends Thread{  
	

	private String phone;
	private String msg;
	
	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}

	private static Log log = LogFactory.getLog(SendPhoneMsgUtil.class);
	private static String sOpenUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
	private static String sDataUrl = "http://smsapi.c123.cn/DataPlatform/DataApi";
	
	// 接口帐号
	private static final String account = "1001@501113300015";
	
	// 接口密钥
	private static final String authkey = "257D62C5E1F23A2DC8D5110386BF42BF";
	
	// 通道组编号
	private static final int cgid = 7738;
	
	// 默认使用的签名编号(未指定签名编号时传此值到服务器)
	private static final int csid = 0;
	public void run(){
		String str="";
		// 发送参数
		OpenApi.initialzeAccount(sOpenUrl, account, authkey, cgid, csid);
		// 状态及回复参数
		DataApi.initialzeAccount(sDataUrl, account, authkey);
		List<SendResult> listItem =OpenApi.sendOnce(phone, msg, 0, 0, null);
		if(null!=listItem&&listItem.size()>0){
		SendResult t = listItem.get(0);
		if(t.getResult()<1){
			str=t.getResult()+"";
		}else {
			str="发送成功";
		}
		}else{
			str="发送失败！";
		}
	}

	public static String callfour(String phone, String msg){
		String str="";
		// 发送参数
		OpenApi.initialzeAccount(sOpenUrl, account, authkey, cgid, csid);
		// 状态及回复参数
		DataApi.initialzeAccount(sDataUrl, account, authkey);
		List<SendResult> listItem =OpenApi.sendOnce(phone, msg, 0, 0, null);
		if(null!=listItem&&listItem.size()>0){
			SendResult t = listItem.get(0);
			if(t.getResult()<1){
				str=t.getResult()+"";
			}else {
				str="发送成功";
			}
		}else{
			str="发送失败！";
		}
		return str;
	}
	
	
	
	public static void main(String[] args) {
		SendPhoneMsgUtil smu = new SendPhoneMsgUtil();
		smu.setPhone("18111111111");
		smu.setMsg("13876");
		smu.start();
		
	}

}
