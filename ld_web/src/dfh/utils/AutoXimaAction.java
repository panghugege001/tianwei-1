package dfh.utils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.axis2.AxisFault;

import dfh.action.vo.AutoXima;
import dfh.service.interfaces.IGGameinfoService;

public class AutoXimaAction implements Serializable {
	private static final String NAMESPACE="http://webservice.skydragon.dfh";
	/**
	 * 
	 */
	private static final long serialVersionUID = -7447598460090205790L;
	private IGGameinfoService gameinfoService;
	
	public IGGameinfoService getGameinfoService() {
		return gameinfoService;
	}

	public void setGameinfoService(IGGameinfoService gameinfoService) {
		this.gameinfoService = gameinfoService;
	}

	public String getXimaEndTime(String loginname){
		String date = "";
		try {
			date = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), NAMESPACE, "getXimaEndTime", new Object[] {
					loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		}
		if(null!=date && !"".equals(date)){
			return date;
		}else{
			return "failed";
		}
	}
	
	public String getXimaEndPtTime(String loginname){
		String date = "";
		try {
			date = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), NAMESPACE, "getXimaEndPtTime", new Object[] {
					loginname}, String.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		}
		if(null!=date && !"".equals(date)){
			return date;
		}else{
			return "failed";
		}
	}
	
	public AutoXima getAutoXimaObject(String _endTime,String _startTime,String _loginname){
		//return new AutoXima("自助反水程序升级，暂停使用，启用日期，请留意主页公告");
		if (_endTime.trim().equals("")||_startTime.trim().equals("")||_loginname.trim().equals("")) {
			return new AutoXima("所有栏目，都为必填项！\n请重新选择[截止时间]，让系统自动帮您填好必填的栏目内容");
		}
		java.text.SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date end=null;
		Date start=null;
		try {
			end=sf.parse(_endTime);
			start=sf.parse(_startTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new AutoXima("日期格式有误，请重新输入");
		}
		
		if (end.getTime()>new Date().getTime()) {
			end=new Date();
		}
		if(end.getTime()<start.getTime()){
			return new AutoXima("截止时间必须大于起始时间");
		}
		
//		end=new Date(end.getTime()-60*30*1000); // 得到30分钟前的时间
		AutoXima autoXima = null;
		try {
			autoXima = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), NAMESPACE, "getAutoXimaObject", new Object[] {
				_endTime, _startTime, _loginname}, AutoXima.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return autoXima = new AutoXima("本次自助洗码异常，请稍后重试！");
		}
		if(null==autoXima){
			autoXima = new AutoXima("本次自助洗码异常，请稍后重试！");
		}
		return autoXima;
	}
	
	public AutoXima getAutoXimaPtObject(String _loginname){
		AutoXima autoXima = null;
		try {
			autoXima = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), NAMESPACE, "getAutoXimaPtObject", new Object[] {
				_loginname}, AutoXima.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return autoXima = new AutoXima("本次自助洗码异常，请稍后重试！");
		}
		if(null==autoXima){
			autoXima = new AutoXima("本次自助洗码异常，请稍后重试！");
		}
		return autoXima;
	}
	
	
}
