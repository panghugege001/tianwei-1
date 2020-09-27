package dfh.utils.sendemail.com.focussend;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.focussend.FocusEmail;
import com.focussend.FocusReceiver;
import com.focussend.FocusReport;
import com.focussend.FocusSendWebServiceSoapProxy;
import com.focussend.FocusTask;
import com.focussend.FocusUser;

public class WebServiceCaller {

	public static void main(String[] args) {
		WebServiceCaller ws = new WebServiceCaller();
		String str=ws.sendEmail("啊啊啊啊", "密码找回", "2870490746@qq.com");
		System.out.println("str==="+str);
		if(str.equals("success")){
			
		}
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	
	public String sendEmail(String nr,String zt,String sjr){
		// TODO 自动生成方法存根
		FocusSendWebServiceSoapProxy uid = new FocusSendWebServiceSoapProxy();
		FocusUser fUser = new FocusUser();
		// 平台的登陆名
		fUser.setEmail("bolt88888@163.com");
		// 平台的登陆密码,为了保证安全,传输过程中用sha1加密
		String pwd = "aa888888";
		byte[] encodedPwdByte = null;
		String encodedPwd = null;
		try {
			MessageDigest alg = java.security.MessageDigest
					.getInstance("SHA-1");
			alg.update(pwd.getBytes());
			encodedPwdByte = alg.digest();
			encodedPwd = bytes2Hex(encodedPwdByte);
			fUser.setPassword(encodedPwd);
		} catch (NoSuchAlgorithmException ex) {
		}

		// 要发送的内容
		FocusEmail fEmail = new FocusEmail();
		fEmail.setBody(nr);
		fEmail.setIsBodyHtml(false);
		// 一次发送作为一个任务,存储一些其他发送信息
	//	FocusTask fTask = new FocusTask();
		// 主题
	//	fTask.setSubject(zt);
		// 任务名,建议保存在数据库中,可以任务名为后期查询报告的标示;任务名不得重复
	//	Random rand = new Random();
	//	fTask.setTaskName(new Date().toString()+rand.nextInt(10000));
		// 回复邮箱
	//	fTask.setReplyEmail("jatisme@gmail.com");
		// 发件人姓名
	//	fTask.setSenderName("E68娱乐城");
		// 发件人邮箱,强烈要求如果没做spf解析的话,用我们提供的邮箱后缀;如果做了spf解析,可以用你们的
	//	fTask.setSenderEmail(sjr);
		// 发送时间,时间设置在将来即为定时发送
	//	fTask.setSendDate(Calendar.getInstance());
		// 本次发送的收件人,可以为多个
//		ArrayList list = new ArrayList();
		FocusReceiver receiver = new FocusReceiver();
		receiver.setEmail(sjr);
//		list.add(receiver);
		
/*
		receiver = new FocusReceiver();
		receiver.setEmail("zsddfwn@163.com");
		list.add(receiver);

		receiver = new FocusReceiver();
		receiver.setEmail("2ffg01377@qq.com");
		list.add(receiver);*/
		//int size = list.size();
		String result = null;
		try {
			// 批量发送，当然也可以用来发一封，当FocusReceiver[]数组的长度为1时即为发送1封
			 //result= uid.batchSend(fUser, fEmail, fTask,(FocusReceiver[])
			 //list.toArray(new FocusReceiver[size]));
			// 单封发送
			//result = uid.sendOne(fUser, fEmail, zt,receiver);
			// 获取整个任务的发送概况
			// FocusReport report=uid.getReportByName(fUser, "test");
			// 获取单个联系人的发送结果
			// FocusReceiverAction action= uid.getReceiverAction(fUser,
			// "java webservice caller", "296201377@qq.com");
			// 获取所有联系人的发送结果
			// FocusReceiverAction[] actionArr= uid.getAllReceiverAction(fUser,
			// "java webservice caller", 0, 10);
			//int a = 3;
			 result= uid.sendOneWithSender(fUser, fEmail, zt, receiver, "E68娱乐城", "cs@mk.e68668.com");
		} catch (RemoteException exp) {
			return "false";
		}
		return result;
		
	}
}
