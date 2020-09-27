package dfh.service.interfaces;

import java.util.List;

import dfh.action.vo.AnnouncementVO;
import dfh.model.Users;

public interface AnnouncementService {

	
	// 分页用；查询所有
	List queryAll(int offset,int length);
	
	// index.jsp页面，公告栏显示的部分公告；倒序；
	List query();
	
	// 分页用，返回总行数
	int totalCount();
	
	AnnouncementVO getAnnouncement(int aid);
	
	// 首页置顶公告
	List queryTopNews();
	
	//在线存款返回成功页面处理
	
	String addPayorder(String billno,Double money,String loginname,String msg,String date);
	
	//刷新session用,获取新的users信息
	
	Users getUser(String loginname);

}