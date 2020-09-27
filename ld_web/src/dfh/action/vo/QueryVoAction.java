package dfh.action.vo;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;





import dfh.action.SubActionSupport;
import dfh.utils.AxisUtil;

public class QueryVoAction extends SubActionSupport {

	private static Logger log = Logger.getLogger(QueryVoAction.class);
	private Integer pagesize;
	private Integer begin;//开始页数
	private Integer end;//结束页数
	private Integer totle;//总条数
	private List<AnnouncementVO> lists=null;

	
	
	public String queryVoByPage(){
//		PrintWriter out =null;
		try {
			lists= AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "querybyPage", new Object[] {begin,end}, AnnouncementVO.class);
//		out = this.getResponse().getWriter();
//		StringBuffer sb = new StringBuffer("");
//		if(null!=lists&&lists.size()>0){
//			for(AnnouncementVO an :lists){
//			sb.append("<tr><td><div class=\"user_info\" style=\"width:880px;\"> <h3>");
//			sb.append(an.getTitle());
//			sb.append("<span class=\"t\">");
//			sb.append(an.getCreatetime());
//			sb.append("</span></h3>  <div class=\"gg-info\"> <div class=\"sjx\"></div>");
//			sb.append(an.getContent());
//			sb.append("</div></div></td></tr>");
//			}
//		}
//		out.write(sb.toString());
//		out.flush();
			getRequest().setAttribute("list", lists);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
//			if(null!=out){
//				out.close();
//			}
		}
		
		return INPUT;
	}
	public String queryVoTotleNo(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			totle= AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "totalNewsCount", new Object[] {}, Integer.class);
			out.print(totle);
			out.flush();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(null!=out){
				out.close();
			}
		}
		
		return null;
	}
	public Integer getPagesize() {
		return pagesize;
	}
	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}
	public Integer getBegin() {
		return begin;
	}
	public void setBegin(Integer begin) {
		this.begin = begin;
	}
	public Integer getEnd() {
		return end;
	}
	public void setEnd(Integer end) {
		this.end = end;
	}
	public Integer getTotle() {
		return totle;
	}
	public void setTotle(Integer totle) {
		this.totle = totle;
	}
	public List<AnnouncementVO> getLists() {
		return lists;
	}
	public void setLists(List<AnnouncementVO> lists) {
		this.lists = lists;
	}
	
}
