package dfh.displaytag.util;

import java.util.Date;

import org.displaytag.decorator.TableDecorator;

import dfh.model.Telvisittask;

public class TelvisittaskFormat extends TableDecorator {

	private static String[] TASKSTATUS={"<font color=red>未完成</font>","完成","废弃"};
	private static String[] DELAY_STATUS={"","[延迟]"};
	
	public String getTaskstatus(){
		Telvisittask telvisittask=(Telvisittask) this.getCurrentRowObject();
		return TASKSTATUS[telvisittask.getTaskstatus().intValue()]+DELAY_STATUS[telvisittask.getIsdelay().intValue()];
	}
	
	public String getTaskStatusAddDelay(){
		Telvisittask telvisittask=(Telvisittask) this.getCurrentRowObject();
		if( telvisittask.getIsdelay().intValue() == 0 ){
			return TASKSTATUS[telvisittask.getTaskstatus().intValue()];
		}else{
			return TASKSTATUS[telvisittask.getTaskstatus().intValue()]+"[延迟："+telvisittask.getDelaytime()/60/3600+"H]";
		}
		
	}
	
	public String getCz(){
		Telvisittask telvisittask=(Telvisittask) this.getCurrentRowObject();
		if (telvisittask.getTaskstatus().intValue()==0) {
			if (telvisittask.getStarttime().before(new Date())) {
				return "<a href=\"/telvisit/getVisitTaskAll.do?taskid="+telvisittask.getId()+"&taskName="+telvisittask.getTaskname()+"\">执行任务</a>";
			}
			return "";
		}
		return "<a href=\"/telvisit/getVisitTaskAll.do?taskid="+telvisittask.getId()+"&taskName="+telvisittask.getTaskname()+"\">查看明细</a>"; 
	}
	
	public String getUnlock(){
		Telvisittask telvisittask=(Telvisittask) this.getCurrentRowObject();
		if (telvisittask.getTaskstatus().intValue()==0 && telvisittask.getStarttime().before(new Date())) {
				return "<a href=\"/telvisit/getVisitTaskAllForUnlock.do?taskid="+telvisittask.getId()+"&taskName="+telvisittask.getTaskname()+"\">解禁操作</a>";
		}
		return "<a href=\"/telvisit/getVisitTaskAllForNoUnlock.do?taskid="+telvisittask.getId()+"&taskName="+telvisittask.getTaskname()+"\">查看明细</a>"; 
	}
	
	public String getModify(){
		String action ="/telvisit/cancleTelVisitTask.do";
		Telvisittask telvisittask=(Telvisittask) this.getCurrentRowObject();
		if (telvisittask.getTaskstatus().intValue()==0) {
				return "<a href=\"/telvisit/modifyTelVisitTask.do?taskid="+telvisittask.getId()+"&taskName="+telvisittask.getTaskname()+"\">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;" 
						+"<a style=\"cursor: pointer\" onclick=\"submitForNewAction('"+action+"',"+telvisittask.getId()+");\""
						+">删除</a>";
		}else{
			return "";
		}
	}
}
