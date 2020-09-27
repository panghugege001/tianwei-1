package dfh.displaytag.util;

import java.util.Calendar;

import org.displaytag.decorator.TableDecorator;

import com.nnti.office.model.auth.Operator;

import dfh.action.vo.TelvisitVO;
import dfh.utils.Constants;

public class TelvisitFormat extends TableDecorator {
	
	private static String[] EXECSTATUS_MSG={"失败","成功","未访问","已访问"};
	
	public String getExecstatus(){
		TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
		return EXECSTATUS_MSG[telvisit.getExecstatus()];
	}
	
	public String getIslock(){
		TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
		if (telvisit.getIslock()==1) {
			return "<font color=red>是</font>";
		}
		return "否";
	}
	public String getQq(){
		try{
			TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
			if(telvisit.getQq()!=null && !"".equals(telvisit.getQq()) && telvisit.getLastlogintime()!=null){
				long ency = 0;
				long l = telvisit.getLastlogintime().getTime();
				long d =  Calendar.getInstance().getTimeInMillis();
				long days = (d-l)/1000/60/60/24;
				if(days>90){
					if("a".equals(telvisit.getIntro())){
						ency  = Long.parseLong(telvisit.getQq())+1111111111+121322;
					}else if("b".equals(telvisit.getIntro())){
						ency  = Long.parseLong(telvisit.getQq())+1222222221+221433;
					}else if("c".equals(telvisit.getIntro())){
						ency  = Long.parseLong(telvisit.getQq())+1333333331+331543;
					}else if("d".equals(telvisit.getIntro())){
						ency  = Long.parseLong(telvisit.getQq())+1444444441+451654;
					}else if("e".equals(telvisit.getIntro())){
						ency  = Long.parseLong(telvisit.getQq())+1555555551+571725;
					}
					return "<a  href='javascript:getEncy(\""+telvisit.getIntro()+"\",\""+ency+"\");'>获取QQ</a>";
				}else{
					return "无法获取";
				}
			}else{
				return "无法获取";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "无法获取";
		}
	}
	public String getPhone(){
		TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
		String phone = telvisit.getPhone();
		if (phone != null && !"".equals(phone)) {
			try{
				long l = Long.parseLong(phone);
				String p = "1" + String.valueOf(l);
				l = Long.parseLong(p) * 11 + 123456;
				phone = dfh.security.EncryptionUtil.encryptBASE64(phone.getBytes());
				return "<a name='_call' href='http://192.168.0.8:12121/bridge/callctrl?callee="+l+"&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803' target='_blank'>80X呼叫</a>&nbsp;&nbsp;" +
					   "<a name='_call2' href='http://192.168.0.160/cc/api/webdial/?call="+phone+"&encrypt=yes&prefix=&exten=8003' target='_blank'>800X呼叫</a>";
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return "无";
	}
	
	public String getEmail(){
		TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
		String loginname = telvisit.getLoginname();
		if(loginname!=null &&!"".equals(loginname)){
			return "<a  href='/office/functions/pemail.jsp?loginname="+loginname+"' target='_blank'>发送邮箱</a>";
		}
		return "无邮箱";
	}
	
	public String getLoginname(){
		TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
		String taskName=(String) this.getPageContext().getRequest().getAttribute("taskName");
		return "<a title='点击查看访问结果' href=\"/telvisit/getRemarks.do?taskid="+telvisit.getTaskid()+"&visitid="+telvisit.getId()+"&taskName="+taskName+"&loginname="+telvisit.getLoginname()+"\">"+telvisit.getLoginname()+"</a>";
	}
	
	public String getLoginnameforunlock(){
		TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
		String taskName=(String) this.getPageContext().getRequest().getAttribute("taskName");
		return "<a title='点击查看访问结果' href=\"/telvisit/getRemarksForLock.do?taskid="+telvisit.getTaskid()+"&visitid="+telvisit.getId()+"&taskName="+taskName+"&loginname="+telvisit.getLoginname()+"\">"+telvisit.getLoginname()+"</a>";
	}
	
	public String getLoginnameforsee(){
		TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
		String taskName=(String) this.getPageContext().getRequest().getAttribute("taskName");
		return "<a title='点击查看访问结果' href=\"/telvisit/getRemarksForSee.do?taskid="+telvisit.getTaskid()+"&visitid="+telvisit.getId()+"&taskName="+taskName+"&loginname="+telvisit.getLoginname()+"\">"+telvisit.getLoginname()+"</a>";
	}
	
	public String getCz(){
		TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
		
		if (telvisit.getExecstatus()!=1&&telvisit.getExecstatus()!=0) {
			Object page = this.getPageContext().getRequest().getAttribute("page");
			String taskName=(String) this.getPageContext().getRequest().getAttribute("taskName");
			String intro=(String) this.getPageContext().getRequest().getAttribute("intro");
			String pageno="1";
			if (page!=null) {
				pageno=String.valueOf(page);
			}
			if (telvisit.getIslock()==0) {
				return "<a href=\"/telvisit/startvisit.do?intro="+intro+"&taskid="+telvisit.getTaskid()+"&visitid="+telvisit.getId()+"&page="+pageno+"&taskName="+taskName+"\">开始访问</a>";
			}
			Operator operator = (Operator) this.getPageContext().getSession().getAttribute(Constants.SESSION_OPERATORID);
			if (operator!=null) {
				String ename=operator.getUsername();
				if (ename.equals(telvisit.getLocker())) {
					return "<a href=\"/telvisit/endvisit.do?intro="+intro+"&taskid="+telvisit.getTaskid()+"&visitid="+telvisit.getId()+"&page="+pageno+"&taskName="+taskName+"&loginname="+telvisit.getLoginname()+"\"><font color=blue>结束访问</font></a>";
				}
				return telvisit.getLocker()+" 正在回访...";
			}
			
		}
		return ""; 
	}
	
//	public String getUnlock(){
//		
//		TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
//		String action ="/telvisit/unlockTelVisit.do";
//		if (telvisit.getIslock()==1) {
//			return "<a style=\"cursor: pointer\" onclick=\"submitForNewAction('"+action+"',"+telvisit.getId()+");\""
//			+">解锁</a>";
//		}else{
//			return "";
//		}
//		
//	}
	
	public String getUnlock(){
		
		TelvisitVO telvisit=(TelvisitVO) this.getCurrentRowObject();
		String action ="/telvisit/unlockTelVisit.do";
		if (telvisit.getIslock()==1) {
			Object page = this.getPageContext().getRequest().getAttribute("page");
			String taskName=(String) this.getPageContext().getRequest().getAttribute("taskName");
			String pageno="1";
			if (page!=null) {
				pageno=String.valueOf(page);
			}
			return "<a href=\"/telvisit/unlockTelVisit.do?taskid="+telvisit.getTaskid()+"&id="+telvisit.getId()+"&page="+pageno+"&taskName="+taskName+"\">解锁</a>";
			//return "<a style=\"cursor: pointer\" onclick=\"submitForNewAction('"+action+"',"+telvisit.getId()+");\""
			//+">解锁</a>";
		}else{
			return "";
		}
		
	}
}
