package dfh.displaytag.util;

import org.displaytag.decorator.TableDecorator;

import dfh.model.Telvisitremark;

public class TelvisitRemarkFormat extends TableDecorator {
	
	private static String[] EXECSTATUS_MSG={"失败","成功","未访问","已访问"};
	
	public String getExecstatus(){
		Telvisitremark remark=(Telvisitremark) this.getCurrentRowObject();
		return EXECSTATUS_MSG[remark.getExecstatus().intValue()];
	}

}
