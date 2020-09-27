package test;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import dfh.dao.UserDao;
import dfh.model.Users;
import dfh.utils.Configuration;
import dfh.utils.HttpClientHelper;
import dfh.utils.SpringFactoryHepler;
import dfh.utils.StringUtil;

public class SynMemberInfo2Bbs {
	
	

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		ApplicationContext ctx = new FileSystemXmlApplicationContext("D:\\Workspaces\\MyEclipse 8.x\\e68\\application\\Ea_office\\WebRoot\\WEB-INF\\applicationContext.xml");
		UserDao userdao=(UserDao) SpringFactoryHepler.getInstance("userDao");
		
		// 最后一次同步时间：2010-10-15；
		// 该功能仅在出现大量会员注册信息，系统没能自动同步到Bbs时使用，否则使用ea_office后台的会员同步信息功能即可
		
		java.text.SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start=null;
		Date end=null;
		try {
			start = sf.parse("2010-10-16 00:00:00");
			end=sf.parse("2010-10-16 16:00:00");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
		List userList = userdao.loadUsers(start,end);
		System.out.println(userList.size());
		if (userList!=null&&userList.size()>0) {
			System.out.println("开始同步会员信息到社区...");
			int size=userList.size();
			System.out.println("有 "+size+" 个会员，包括禁用会员");
			String synMemberInfoUrl=Configuration.getInstance().getValue("ea_bbs_register");
			for (int i = 0; i <size; i++) {
				Users user=(Users) userList.get(i);
				
					String username = user.getLoginname();

					String aliasName = user.getAliasName();
					if (aliasName==null||aliasName.trim().equals("")) {
						aliasName="";
					}
					int status=new HttpClientHelper().synRegister(synMemberInfoUrl, StringUtil.convertByteArrayToHexStr(aliasName.getBytes("gbk")), StringUtil.convertByteArrayToHexStr(username.getBytes("gbk")));
					System.out.println("同步会员："+user.getLoginname()+"..."+(status==200?"成功":"失败"));
				
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			System.out.println("error");
		}
		System.out.println("同步完成");
	}

}
