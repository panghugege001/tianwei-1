package dfh.displaytag.util;

import org.displaytag.decorator.TableDecorator;

import dfh.action.vo.TelsivitVOForCount;

public class TelvisitThreeFormat extends TableDecorator{
	
private static String[] TASKSTATUS_MSG={"未完成","完成","废弃"};

private static int countsum=0;
private static int success=0;
private static int fail = 0;
	
	
//	public String finishRow() { 
//		TelsivitVOForCount telsivitVOForCount=(TelsivitVOForCount) this.getCurrentRowObject();
//		System.out.println(countsum+"  "+success+"  "+fail);
//		countsum += telsivitVOForCount.getSum();
//		success += telsivitVOForCount.getSuccess();
//		fail += telsivitVOForCount.getFail();
//		if(this.isLastRow()){
//			return "本页面统计 &nbsp;&nbsp; 总拨打的数目是：<font color='#FF0000'>"+countsum+"</font>"
//			+"&nbsp;&nbsp;&nbsp;&nbsp;成功次数是：<font color='#FF0000'"+success+"</font>"
//			+"&nbsp;&nbsp;&nbsp;&nbsp;失败次数是：<font color='#FF0000'"+fail+"</font>";
//		}else{
//			return "";
//		}
//	}


	public String getTaskstatus(){
		TelsivitVOForCount telsivitVOForCount=(TelsivitVOForCount) this.getCurrentRowObject();
		return TASKSTATUS_MSG[telsivitVOForCount.getTaskstatus()];
	}
	
	public String getTaskname(){
		TelsivitVOForCount telsivitVOForCount=(TelsivitVOForCount) this.getCurrentRowObject();
		return "<a href=\"/telvisit/getCountNumberByOperator.do?taskid="+telsivitVOForCount.getId()+"&taskName="+telsivitVOForCount.getTaskname()+"\">"+telsivitVOForCount.getTaskname()+"</a>"; 
	}
	public String getTasknamein(){
		TelsivitVOForCount telsivitVOForCount=(TelsivitVOForCount) this.getCurrentRowObject();
		return telsivitVOForCount.getTaskname();
	}
	

}
