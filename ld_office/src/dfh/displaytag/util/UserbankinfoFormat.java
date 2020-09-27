package dfh.displaytag.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.displaytag.decorator.TableDecorator;

import dfh.model.Userbankinfo;

public class UserbankinfoFormat extends TableDecorator {
	
	
	public String getAddtime(){
		Userbankinfo userbankinfo=(Userbankinfo) this.getCurrentRowObject();
		java.text.SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(userbankinfo.getAddtime().getTime());
		return sf.format(date);
	}
	
	public String getFlag(){
		Userbankinfo userbankinfo=(Userbankinfo) this.getCurrentRowObject();
		if (userbankinfo.getFlag().intValue()==1) {
			return "<font color='red'>停用</font>";
		}else{
			return "正常";
		}
	}
	
	public String getCustomscript(){
		Userbankinfo userbankinfo=(Userbankinfo) this.getCurrentRowObject();
		if (userbankinfo.getFlag().intValue()==1) {
			// 停用
			return "";
		}else{
			return "<a href=\"JavaScript:unbanding('"+userbankinfo.getId().intValue()+"')\">解除绑定</a>";
		}
	}

}
