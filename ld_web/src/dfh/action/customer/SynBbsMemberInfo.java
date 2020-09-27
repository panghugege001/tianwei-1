package dfh.action.customer;

import bbs.client.Client;
import dfh.utils.AxisUtil;

public class SynBbsMemberInfo extends Thread{
	
	public String username;
	public String password;
	public SynBbsMemberInfo(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public void run(){
		try {
			/*改为直接操作库表
			 * Client uc = new Client();
			String $returns = uc.uc_user_register(username, password, "ccc3c123@abc.com");
			int $uid = Integer.parseInt($returns);
			if ($uid <= 0) {
				if ($uid == -1) {
					System.out.print("用户名不合法");
				} else if ($uid == -2) {
					System.out.print("包含要允许注册的词语");
				} else if ($uid == -3) {
					System.out.print("用户名已经存在");
				} else if ($uid == -4) {
					System.out.print("Email 格式有误");
				} else if ($uid == -5) {
					System.out.print("Email 不允许注册");
				} else if ($uid == -6) {
					System.out.print("该 Email 已经被注册");
				} else {
					System.out.print("未定义");
				}
			} else {
				System.out.println("OK:" + $returns);
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "synBbsMemberInfo", new Object[] { username, password }, String.class);
			}*/
			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "synBbsMemberInfo", new Object[] { username, password }, String.class);
			System.out.println("SynBbsMemberInfo:" + msg);
		} catch (Exception e) {
			// 这里的异常需要捕获，不能让ea_bbs的登录状态影响注册流程
			System.out.println(e.toString());
		}
	}

}
