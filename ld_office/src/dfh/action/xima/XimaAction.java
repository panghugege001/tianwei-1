package dfh.action.xima;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import dfh.model.Proposal;
import dfh.service.interfaces.IGameinfoService;
import dfh.service.interfaces.ProposalService;
import dfh.utils.DateUtil;
import dfh.utils.SpringFactoryHepler;

public class XimaAction extends ActionSupport implements Serializable,
		SessionAware, ServletRequestAware, ServletResponseAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5075493986597663281L;
	private Map<String, Object> session;
	private HttpServletRequest req;
	private HttpServletResponse res;
	private IGameinfoService ximaService;
	private boolean isExec=false;
	private String r;
	public String getR() {
		return r;
	}


	public void setR(String r) {
		this.r = r;
	}

	private ProposalService proposalService;
	private Logger log=Logger.getLogger(XimaAction.class);
	
	
	public ProposalService getProposalService() {
		return proposalService;
	}


	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}


	public String getXimaMessage(){
		if (ximaService!=null) {
			String message = ximaService.getMessage();
//			if (message==null) {
//				this.isExec=false;
//			}
			this.println(message);
			
		}else{
			this.println("反水尚未执行，没有任何消息产生");
		}
		
		return null;
	}
	
	

	public String addXimaProposal(){
		if (isExec) {
//			if (!isMonday()) {
//				return null;
//			}
			ximaService=(IGameinfoService) SpringFactoryHepler.getInstance("addXimaProposalService");
			new Thread(ximaService).start();
			this.println("正在提交洗码提案...");
		}else{
			this.println("请先执行检测任务");
		}
		return null;
	}
	
	public String execXimaProposal(){
		if (isExec) {
//			if (!isMonday()) {
//				return null;
//			}
			ximaService=(IGameinfoService) SpringFactoryHepler.getInstance("execXimaProposalService");
			new Thread(ximaService).start();
			this.println("正在执行洗码提案...");
		}else{
			this.println("请先执行检测任务");
		}
		return null;
	}
	
	public String updateXimaProposal(){
		if (isExec) {
//			if (!isMonday()) {
//				return null;
//			}
			ximaService=(IGameinfoService) SpringFactoryHepler.getInstance("updateXimaProposalService");
			new Thread(ximaService).start();
			this.println("正在更新Gameinfo table flag status，请稍候...");
		}else{
			this.println("请先执行检测任务");
		}
		return null;
	}
	
	public String checkNotExecProposal(){
//		if (!isMonday()) {
//			return null;
//		}
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(7*24+12));
		try {
			List<Proposal> proposalList = proposalService.getNotExecProposal(starttime, new Date());
			if (proposalList==null||proposalList.size()<=0) {
				this.isExec=true;
				this.println("ok,可以执行反水");
			}else{
				this.println("有未审核或执行的洗码提案，请处理后在执行系统反水");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			this.println(e.getMessage());
		}
		
		return null;
	}
	
	
	public String recoverStatus(){
		this.isExec=false;
		this.println("ok");
		return null;
	}
	
	
	private void println(String msg){
		PrintWriter out = null;
		try {
			out=res.getWriter();
			out.println(msg);
			out.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			if (out!=null) {
				out.close();
			}
		}
	}
	
	private boolean isMonday()
	{
		Calendar c=Calendar.getInstance();
		c.setTime(new Date());
		int week=c.get(Calendar.DAY_OF_WEEK)-1;
		if (week!=1) {
			this.println("今天不是周一，无法执行反水");
			return false;
		}else{
			return true;
		}
	}
	
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		session=arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		req=arg0;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		res=arg0;
	}

}
