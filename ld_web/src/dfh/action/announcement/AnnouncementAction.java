package dfh.action.announcement;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import dfh.action.vo.AnnouncementVO;
import dfh.service.interfaces.AnnouncementService;
import dfh.utils.AxisUtil;
import org.apache.axis2.AxisFault;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AnnouncementAction extends ActionSupport implements ServletRequestAware{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5767135076740065325L;
	private List list;
	private int currPageNo=1;	// 当前页码
	private int totalRowNo;		// 总行数
	private int totalPageNo;  	// 总页数；丢在后台计算。
	private int rowNo=30; 		// 每页显示的最大行数
	private int aid;
	private HttpServletRequest request;
	
	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public int getTotalPageNo() {
		return totalPageNo;
	}

	public void setTotalPageNo(int totalPageNo) {
		this.totalPageNo = totalPageNo;
	}

	public int getCurrPageNo() {
		return currPageNo;
	}

	public void setCurrPageNo(int currPageNo) {
		this.currPageNo = currPageNo;
	}

	public int getTotalRowNo() {
		return totalRowNo;
	}

	public void setTotalRowNo(int totalRowNo) {
		this.totalRowNo = totalRowNo;
	}

	
	public List getList() {
		return list;
	}

	private AnnouncementService announcementService;

	public AnnouncementService getAnnouncementService() {
		return announcementService;
	}

	public void setAnnouncementService(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}

	// web page query Announcement; top 8
	public String getAnnouncements(){
		list = announcementService.query();
		return Action.SUCCESS;
	}
	
	public String findAll(){
		try{
			if(currPageNo==0)
				currPageNo=1;
		}catch(Exception e){
			currPageNo=1;
		}
		try {
			this.totalRowNo= (Integer) AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "totalNewsCount",
						new Object[] {}, Integer.class);// 得到总行
			
			this.totalPageNo=(totalRowNo%this.rowNo==0?0:1)+this.totalRowNo/this.rowNo; // 得到最大页数
			list=AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "findAll",
					new Object[] {this.currPageNo, this.rowNo}, AnnouncementVO.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	public String getAnnouncement(){
		
		AnnouncementVO ann = null;
		try {
			ann = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getAnnouncement",
					new Object[] {aid}, AnnouncementVO.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.request.setAttribute("ann", ann);
		return Action.SUCCESS;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		request=arg0;
	}

}
