package dfh.displaytag.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.displaytag.decorator.TableDecorator;

import dfh.action.vo.OperatorVO;
import dfh.utils.RoleTable;

public class EmployeeFormat extends TableDecorator {
	
	private static SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
	private Logger log=Logger.getLogger(EmployeeFormat.class);
	
	public String getLastLoginTime(){
		OperatorVO vo=(OperatorVO) this.getCurrentRowObject();
		try {
			return vo.getLastLoginTime().equals("")?"":sf.format(sf.parse(vo.getLastLoginTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("格式化'员工最后登录时间'时，发生异常", e);
			return "";
		}
	}
	
	public String getCreateTime(){
		OperatorVO vo=(OperatorVO) this.getCurrentRowObject();
		try {
			return sf.format(sf.parse(vo.getCreateTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("格式化'员工账号创建时间'时，发生异常", e);
			return "";
		}
	}
	
	public String getEnabled(){
		OperatorVO vo=(OperatorVO) this.getCurrentRowObject();
		return vo.getEnabled().equals("0")?"正常":"<font color='red'>禁用</font>";
	}
	
	public String getAuthority(){
		OperatorVO vo=(OperatorVO) this.getCurrentRowObject();
		return RoleTable.getDepartmentName(vo.getAuthority());
	}
	

}
