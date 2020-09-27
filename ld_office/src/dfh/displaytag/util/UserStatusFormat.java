package dfh.displaytag.util;

import org.displaytag.decorator.TableDecorator;

import dfh.action.vo.UserStatusVO;

public class UserStatusFormat extends TableDecorator{
	
	public String getCz(){
		UserStatusVO userStatusVO = (UserStatusVO)this.getCurrentRowObject();
		Object page = this.getPageContext().getRequest().getAttribute("page");
		String pageno="1";
		if (page!=null) {
			pageno=String.valueOf(page);
		}
		if (userStatusVO.getMailflag().intValue()==0) {
				return "<a href=\"/userstatus/modifyUserMailFlag.do?mailflag=1&page="+pageno+"&loginname="+userStatusVO.getLoginname()+"\">关闭</a>";
		}
		return "<a href=\"/userstatus/modifyUserMailFlag.do?mailflag=0&page="+pageno+"&loginname="+userStatusVO.getLoginname()+"\">开通</a>"; 
	}
	
	public String getTouzhu(){
		UserStatusVO userStatusVO = (UserStatusVO)this.getCurrentRowObject();
		Object page = this.getPageContext().getRequest().getAttribute("page");
		String pageno="1";
		if (page!=null) {
			pageno=String.valueOf(page);
		}
		if (userStatusVO.getTouzhuflag().intValue()==1) {
				return "有限制<a class='mtTouzhuFlag' href='#' url=\"/userstatus/closeTouzhuFlag.do?touzhuflag=0&page="+pageno+"&loginname="+userStatusVO.getLoginname()+"\">(取消限制)</a>";
		}else{
				return "无限制<a class='mtTouzhuFlag' href='#' url=\"/userstatus/closeTouzhuFlag.do?touzhuflag=1&page="+pageno+"&loginname="+userStatusVO.getLoginname()+"\">(加上限制)</a>";
		}
	}
	
}
