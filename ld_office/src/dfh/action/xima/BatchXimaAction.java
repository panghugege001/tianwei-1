package dfh.action.xima;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dfh.utils.*;
import dfh.utils.bitGame.BitGameUtil;
import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import dfh.action.SubActionSupport;
import dfh.icbc.quart.fetch.FetchEBetAppJobService;
import dfh.icbc.quart.fetch.FetchNTwoJobService;
import dfh.model.AgData;
import dfh.model.AgProfit;
import dfh.model.BbinData;
import dfh.model.EaData;
import dfh.model.NTProfit;
import dfh.model.PlatformData;
import dfh.model.Proposal;
import dfh.model.PtData;
import dfh.model.PtDataNew;
import dfh.model.PtProfit;
import dfh.model.RecordMail;
import dfh.model.bean.Bean4Xima;
import dfh.model.bean.XimaDataVo;
import dfh.model.enums.ProposalType;
import dfh.model.enums.SystemLogType;
import dfh.service.implementations.AbstractBatchGameinfoServiceImpl;
import dfh.service.interfaces.GuestBookService;
import dfh.service.interfaces.IGameinfoService;
import dfh.service.interfaces.MGSService;
import dfh.service.interfaces.ProposalService;
import dfh.utils.synch.SynchrAgentVipUtil;
import dfh.utils.synch.SynchrPtCommissionsUtil;

public class BatchXimaAction extends SubActionSupport implements Serializable, SessionAware, ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5455508126698187561L;
	private static Logger log = Logger.getLogger(BatchXimaAction.class);
	private static final int BUFFER_SIZE = 16 * 1024;
	
	private GuestBookService guestBookService;

	private File myFile;
	private String contentType;
	private String fileName;
	private String excelFileName;
	private String errormsg;
	private Double rate;
	private String amount;
	private String remark;
	private String belong;
	private String depositbank;
	private String depositname;
	private String depositaccount;
	private String businessProposalType;
	private String pno;
	private String bankinfoid;
	private Double fee;
	private Double actualmoney;
	private boolean isExec = false;
	private String ids ;
	private Integer level ;
	private String executetime ;
	private HttpServletRequest req;
	private Map<String, Object> session;
	private HttpServletResponse res;
	private ProposalService proposalService;
	private AbstractBatchGameinfoServiceImpl batchGameinfoServiceImpl;
	private FetchNTwoJobService fetchNTwoJobService;
	private FetchEBetAppJobService fetchEBetAppJobService;
	private MGSService mgsService;


	
	private IGameinfoService ximaService;
	private String gamekind;
	public String getGamekind() {
		return gamekind;
	}

	public void setGamekind(String gamekind) {
		this.gamekind = gamekind;
	}

	private PBUtil pbUtil;

	public PBUtil getPbUtil() {
		return pbUtil;
	}

	public void setPbUtil(PBUtil pbUtil) {
		this.pbUtil = pbUtil;
	}

	/**
	 * BBIN系统洗码
	 * */
	public synchronized String addBbinBatchXimaProposal() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<13){
			GsonUtil.GsonObject("提示：请您每天的下午1点后提交") ;
			return SUCCESS;
		}


		/*String ximalogStr = "";
		if(gamekind.equals("3")) {
			ximalogStr =proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMABBINELE);
		}

		if(gamekind.equals("5")) {
			ximalogStr =proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMABBINVID);
		}

		if (ximalogStr!=null&&ximalogStr!="") {
			setErrormsg("提交失败: "+ximalogStr);
			return SUCCESS;
		}*/

		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			if(gamekind.equals("3")) {
				dc = dc.add(Restrictions.in("remark", new Object[] { "bbinvid系统洗码", "bbinvid系统洗码;执行:", "bbinvid系统洗码;executed:" }));
			}
			if(gamekind.equals("5")) {
				dc = dc.add(Restrictions.in("remark", new Object[] { "bbinele系统洗码", "bbinele系统洗码;执行:", "bbinele系统洗码;executed:" }));
			}

			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				String result = BBinUtils.getBbinXimaData(DateUtil.getchangedDate(-1)+" 00:00:00",DateUtil.getchangedDate(-1)+ " 23:59:59",gamekind);
				List sbaData = JSONArray.fromObject(result);
				if (sbaData == null || sbaData.size() <= 0 || sbaData.get(0) == null) {
					setErrormsg("无洗码数据，请核对下再试！");
					return SUCCESS;
				}
				String msg = proposalService.autoAddXimaBbinProposal(sbaData,gamekind);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} else {
				setErrormsg((gamekind.equals("3")?"bbinvid":"bbinele")+"系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}

	public synchronized String addNTwoBatchBaobiaoXimaProposal() {
		final String platform = "N2Live";
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(AgProfit.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", currentDateTwo.getTime().clone()));
			dc.add(Restrictions.like("remark", "%"+platform+"平台输赢值%"));
			List list = proposalService.findByCriteria(dc);
			if (list.isEmpty()) {

				if (null == fileName || fileName.equals("")) {

					setErrormsg("请先提交文件");

					return INPUT;
				}

				String filehouzhui = getExtention(fileName);

				if (!filehouzhui.equals(".xls")) {

					setErrormsg("文件格式必须是excel");

					return INPUT;
				}

				excelFileName = new Date().getTime() + getExtention(fileName);

				File uplodaFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));

				if (!uplodaFiles.exists()) {

					uplodaFiles.mkdir();
				}

				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);

				copy(myFile, file);

				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");

				try {
					List<Bean4Xima> dataList = this.batchGameinfoServiceImpl.excelToNTwoVo(file);
					if(dataList == null || dataList.size() == 0){
						setErrormsg("提交失败，没有获取到有效数据！");
						return SUCCESS;
					}

					boolean flag = true;
					List<Bean4Xima> tempList = null;
					for (int i = 0; i < dataList.size(); i++) {
						log.info("\n已添加处理"+i+"条数据");
						if(null == tempList || tempList.size() == 0){
							tempList = new ArrayList<Bean4Xima>() ;
						}
						tempList.add(dataList.get(i)) ;
						if(tempList.size() == 500 || i == dataList.size()-1){
							String msg = proposalService.addNTwoBatchBaobiaoXimaProposal(tempList);
							if(StringUtils.isNotBlank(msg)){
								flag = false ;
							}
							tempList = null ;
						}
					}
					if (flag) {
						setErrormsg("N2报表洗码数据提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: ");
						return SUCCESS;
					}
				} catch (Exception e) {
					e.printStackTrace();
					setErrormsg("提交失败: " + e);
				}
			} else {
				setErrormsg(platform + "系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	/**
	 * 开元棋牌系统洗码
	 * */
	public synchronized String addKyqpBatchXimaProposal() {
		String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAKYQP);
		if (ximalogStr!=null&&ximalogStr!="") {
			setErrormsg("提交失败: "+ximalogStr);
			return SUCCESS;
		}

		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "kyqp系统洗码", "kyqp系统洗码;执行:", "kyqp系统洗码;executed:" }));

			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				String startTime = DateUtil.getchangedDate(-1)+" 00:00:00";
				String endTime = DateUtil.getchangedDate(-1)+ " 23:59:59";
				String result = KYQPUtil.getKyqpXimaData(startTime,endTime);
				if (StringUtils.isEmpty(result)) {
					setErrormsg("无洗码数据，请核对下再试！");
					return SUCCESS;
				}
				List data = JSONArray.fromObject(result);
				String msg = proposalService.autoAddXimaKyqpProposal(data);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} else {
				setErrormsg("kyqp系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("系统异常，请联系技术");
		}
		return SUCCESS;
	}

	/**
	 * VR彩票系统洗码
	 * */
	public synchronized String addVRBatchXimaProposal() {

		String ximalogStr = "";
		if(gamekind.equals("1")) {
			ximalogStr =proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAVR);
		}

		if(gamekind.equals("2")) {
			ximalogStr =proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAVRLIVE);
		}

		if (ximalogStr!=null&&ximalogStr!="") {
			setErrormsg("提交失败: "+ximalogStr);
			return SUCCESS;
		}

		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			if(gamekind.equals("1")) {
				dc = dc.add(Restrictions.in("remark", new Object[] { "vr官方彩系统洗码", "vr官方彩系统洗码;执行:", "vr官方彩系统洗码;executed:" }));
			}
			if(gamekind.equals("2")) {
				dc = dc.add(Restrictions.in("remark", new Object[] { "vr彩系统洗码", "vr彩系统洗码;执行:", "vr彩系统洗码;executed:" }));
			}

			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				String startTime = DateUtil.getchangedDate(-1)+" 00:00:00";
				String endTime = DateUtil.getchangedDate(-1)+ " 23:59:59";
				String result = KYQPUtil.getVRXimaData(startTime,endTime,gamekind);
				if (StringUtils.isEmpty(result)) {
					setErrormsg("无洗码数据，请核对下再试！");
					return SUCCESS;
				}
				List data = JSONArray.fromObject(result);
				String msg = proposalService.autoAddXimaVRProposal(data,gamekind);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} else {
				setErrormsg((gamekind.equals("1")?"vr官方彩":"vr彩")+"系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("系统异常，请联系技术");
		}
		return SUCCESS;
	}

	/**
	 * FANYA系统洗码
	 * */
	public synchronized String addFanyaBatchXimaProposal() {
		String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAFANYA);
		if (ximalogStr!=null&&ximalogStr!="") {
			setErrormsg("提交失败: "+ximalogStr);
			return SUCCESS;
		}

		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "FANYA系统洗码", "FANYA系统洗码;执行:", "FANYA系统洗码;executed:" }));

			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				String startTime = DateUtil.getchangedDate(-1)+" 00:00:00";
				String endTime = DateUtil.getchangedDate(-1)+ " 23:59:59";
				String sql = "select user_name,SUM(bet_amount) bet,SUM(money) win from fanya_log where reward_at >=:startTime and reward_at<=:endTime GROUP BY user_name ";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("startTime", startTime);
				params.put("endTime", endTime);
				List data = proposalService.list(sql, params);
				if (data == null || data.size() <= 0 || data.get(0) == null) {
					setErrormsg("无洗码数据，请核对下再试！");
					return SUCCESS;
				}
				String msg = proposalService.autoAddXimaFanyaProposal(data);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} else {
				setErrormsg("FANYA系统洗码已经导入！！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("系统异常，请联系技术");
		}
		return SUCCESS;
	}

	/**
	 * BBIN系统洗码(最新的)
	 * */
	public synchronized String addBbinBatchXimaBaobiaoProposal() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<13){
			GsonUtil.GsonObject("提示：请您每天的下午1点后提交") ;
			return SUCCESS;
		}

		String ximalogStr = "";
		if(gamekind.equals("3")) {
			ximalogStr =proposalService.insertSystemLogs(getOperatorLoginname(), SystemLogType.XIMABBINVID);
		}

		if(gamekind.equals("5")) {
			ximalogStr =proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMABBINELE);
		}

		if (ximalogStr!=null&&ximalogStr!="") {
			setErrormsg("提交失败: "+ximalogStr);
			return SUCCESS;
		}

		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			if(gamekind.equals("3")) {
				dc = dc.add(Restrictions.in("remark", new Object[] { "bbinvid系统洗码", "bbinvid系统洗码;执行:", "bbinvid系统洗码;executed:" }));
			}
			if(gamekind.equals("5")) {
				dc = dc.add(Restrictions.in("remark", new Object[] { "bbinele系统洗码", "bbinele系统洗码;执行:", "bbinele系统洗码;executed:" }));
			}

			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {


				if (null == fileName || fileName.equals("")) {

					setErrormsg("请先提交文件");

					return INPUT;
				}

				String filehouzhui = getExtention(fileName);

				if (!filehouzhui.equals(".xls")) {

					setErrormsg("文件格式必须是excel");

					return INPUT;
				}

				excelFileName = new Date().getTime() + getExtention(fileName);

				File uplodaFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));

				if (!uplodaFiles.exists()) {

					uplodaFiles.mkdir();
				}

				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);

				copy(myFile, file);

				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");

				String msg = this.batchGameinfoServiceImpl.systemXimaForBBin(file,gamekind);

				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} else {
				setErrormsg((gamekind.equals("3")?"bbinvid":"bbinele")+"系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}


	public synchronized String addBatchXimaProposal() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			SystemLogType systemLogType = null;
			if (rate == 0.02) {
				dc = dc.add(Restrictions.in("remark", new Object[] { "ag系统洗码", "bbin系统洗码", "agin系统洗码" }));
				systemLogType=SystemLogType.XIMAAG;
				// dc.add(Restrictions.or(Restrictions.eq("remark", "ag系统洗码"),
				// Restrictions.eq("remark", "bbin系统洗码")));
			} else if (rate == 0.01) {
				dc = dc.add(Restrictions.eq("remark", "ea系统洗码"));
				systemLogType=SystemLogType.XIMAEA;
			} else if (rate == 0.03) {
				dc = dc.add(Restrictions.eq("remark", "keno系统洗码"));
			} else if (rate == 0.04) {
				dc = dc.add(Restrictions.eq("remark", "SB系统洗码"));
				systemLogType=SystemLogType.XIMASB;
			} else if (rate == 0.05) {
				dc = dc.add(Restrictions.eq("remark", "PT系统洗码"));
			} else if (rate == 0.06) {
				dc = dc.add(Restrictions.eq("remark", "keno2系统洗码"));
			}else if (rate == 0.07) {
				dc = dc.add(Restrictions.eq("remark", "ebet系统洗码"));
				systemLogType=SystemLogType.XIMAEBET;
			}else if (rate == 0.09) {
				dc = dc.add(Restrictions.eq("remark", "ttg系统洗码"));
				systemLogType=SystemLogType.XIMATTG;
			} else if (rate == 0.1){
				dc = dc.add(Restrictions.eq("remark", "GPI系统洗码"));
			} else if (rate == 0.11){
				dc = dc.add(Restrictions.eq("remark", "qt系统洗码"));
				systemLogType=SystemLogType.XIMAQT;
			}
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				if (null == fileName || fileName.equals("")) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				String filehouzhui = getExtention(fileName);

				if (!filehouzhui.equals(".xls")) {
					setErrormsg("文件格式必须是excel");
					return INPUT;
				}
				excelFileName = new Date().getTime() + getExtention(fileName);

				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
				copy(myFile, file);
				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
				try {
					
					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),systemLogType);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}
					
					String msg = null  ;
					if(rate == 0.06){
						batchGameinfoServiceImpl.systemXimaForKg(file);
					}else{
						msg = batchGameinfoServiceImpl.autoAddXimaProposal(file, rate);
					}
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if (rate == 0.02) {
					setErrormsg("AG和BBIN系统洗码已经导入！请不要再次操作！");
				} else if (rate == 0.01) {
					setErrormsg("EA系统洗码已经导入！请不要再次操作！");
				} else if (rate == 0.03) {
					setErrormsg("keno系统洗码已经导入！请不要再次操作！");
				} else if (rate == 0.04) {
					setErrormsg("SB系统洗码已经导入！请不要再次操作！");
				} else if (rate == 0.05) {
					setErrormsg("PT系统洗码已经导入！请不要再次操作！");
				}else if (rate == 0.06) {
					setErrormsg("keno2系统洗码已经导入！请不要再次操作！");
				}else if (rate == 0.07) {
					setErrormsg("ebet系统洗码已经导入！请不要再次操作！");
				}else if (rate == 0.09) {
					setErrormsg("ttg系统洗码已经导入！请不要再次操作！");
				}else if (rate == 0.1) {
					setErrormsg("GPI系统洗码已经导入！请不要再次操作！");
				}else if (rate == 0.11) {
					setErrormsg("QT系统洗码已经导入！请不要再次操作！");
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}

	/**
	 * SwFish系统洗码（报表）
	 * */
	public synchronized String addSwFishBatchXimaProposalExcel() {

		try {
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			/*if(hour<15){
				GsonUtil.GsonObject("提示：请您每天的下午3点后提交") ;
				return SUCCESS;
			}*/

			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "SWFISH系统洗码", "SWFISH系统洗码;执行:", "SWFISH系统洗码;executed:" }));

			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				if (null == fileName || fileName.equals("")) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				String filehouzhui = getExtention(fileName);

				if (!filehouzhui.equals(".xls")) {
					setErrormsg("文件格式必须是excel");
					return INPUT;
				}
				excelFileName = new Date().getTime() + getExtention(fileName);

				File uplodaFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));
				if(!uplodaFiles.exists()){
					uplodaFiles.mkdir();
				}
				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);

				copy(myFile, file);


				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
				try {
					List<Bean4Xima> dataList = this.batchGameinfoServiceImpl.excelToPtSkyVo(file);
					if(dataList == null || dataList.size() == 0){
						setErrormsg("提交失败，没有获取到有效数据！");
						return SUCCESS;
					}

					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMASWFISH);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}

					boolean flag = true;
					List<Bean4Xima> tempList = null;
					for (int i = 0; i < dataList.size(); i++) {
						log.info("\n已添加处理"+i+"条数据");
						if(null == tempList || tempList.size() == 0){
							tempList = new ArrayList<Bean4Xima>() ;
						}
						tempList.add(dataList.get(i)) ;
						if(tempList.size() == 500 || i == dataList.size()-1){
							String msg = proposalService.addSwFishBatchXimaProposal(tempList);
							if(StringUtils.isNotBlank(msg)){
								flag = false ;
							}
							tempList = null ;
						}
					}

					if (flag) {
						setErrormsg("SWFISH洗码数据提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: ");
						return SUCCESS;
					}
				} catch (Exception e) {
					e.printStackTrace();
					setErrormsg("提交失败: " + e);
				}
			} else {
				setErrormsg("SWFISH系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}


	public synchronized String addBatchXimaPtProposal() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "PT系统洗码", "PT系统洗码;执行:", "PT系统洗码;executed:" }));
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				try {
					SimpleDateFormat dfHH = new SimpleDateFormat("HH");
					Date date = new Date();
					Integer hh = Integer.parseInt(dfHH.format(date));
					if (hh < 3) {
						setErrormsg("只能在3点之后才能提交");
						return SUCCESS;
					}
                    //昨天12点
					Calendar c = Calendar.getInstance();
					c.setTime(DateUtil.now());
					c.add(Calendar.DAY_OF_MONTH, -1);
					c.set(Calendar.HOUR_OF_DAY, 12);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					Date start = c.getTime();
					c.add(Calendar.DAY_OF_MONTH, 1);
					c.set(Calendar.HOUR_OF_DAY, 12);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					Date end = c.getTime();
					DetachedCriteria d = DetachedCriteria.forClass(PtProfit.class);
					d.add(Restrictions.and(Restrictions.eq("starttime",start), Restrictions.eq("endtime", end)));
					List<PtProfit> ptProfit = proposalService.findByCriteria(d);
					if(ptProfit==null || ptProfit.size()<=0 ||ptProfit.get(0)==null){
						setErrormsg("获取数据失败");
						return SUCCESS;
					}
					String msg = proposalService.autoAddXimaPtProposal(ptProfit);
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {
				setErrormsg("PT系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	public synchronized String addNewPtBatchSixXimaProposal() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "NEWPT系统洗码", "NEWPT系统洗码;执行:", "NEWPT系统洗码;executed:" }));
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				try {
					SimpleDateFormat dfHH = new SimpleDateFormat("HH");
					Date date = new Date();
					Integer hh = Integer.parseInt(dfHH.format(date));
					if (hh < 3) {
						setErrormsg("只能在3点之后才能提交");
						return SUCCESS;
					}
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat formatterTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar cals = Calendar.getInstance();
					cals.setTime(date);
					cals.add(Calendar.DAY_OF_MONTH, -1);
					String dateTime = formatter.format(cals.getTime());
					Date start = formatterTwo.parse(dateTime+" 00:00:00");
					Date end = formatterTwo.parse(dateTime+" 23:59:59");
					DetachedCriteria d = DetachedCriteria.forClass(PtData.class);
					d.add(Restrictions.and(Restrictions.eq("starttime",start), Restrictions.eq("endtime", end)));
					List<PtData> ptData = proposalService.findByCriteria(d);
					if(ptData==null || ptData.size()<=0 ||ptData.get(0)==null){
						setErrormsg("获取数据失败");
						return SUCCESS;
					}
					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAPT);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}
					String msg = proposalService.autoAddXimaNewPtProposal(ptData);
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {
				setErrormsg("PT系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}

	
	public synchronized String addNTBatchXimaProposal() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "nt系统洗码", "nt系统洗码;执行:", "nt系统洗码;executed:" }));
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				try {
					SimpleDateFormat dfHH = new SimpleDateFormat("HH");
					Date date = new Date();
					Integer hh = Integer.parseInt(dfHH.format(date));
					if (hh < 3) {
						setErrormsg("只能在3点之后才能提交");
						return SUCCESS;
					}
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat formatterTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar cals = Calendar.getInstance();
					cals.setTime(date);
					cals.add(Calendar.DAY_OF_MONTH, -1);
					String dateTime = formatter.format(cals.getTime());
					Date start = formatterTwo.parse(dateTime+" 00:00:00");
					Date end = formatterTwo.parse(dateTime+" 23:59:59");
					DetachedCriteria d = DetachedCriteria.forClass(NTProfit.class);
					d.add(Restrictions.and(Restrictions.eq("startTime",start), Restrictions.eq("endTime", end)));
					List<NTProfit> ntData = proposalService.findByCriteria(d);
					if(ntData==null || ntData.size()<=0 ||ntData.get(0)==null){
						setErrormsg("获取数据失败");
						return SUCCESS;
					}
					
					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMANT);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}
					
					String msg = proposalService.autoAddXimaNTProposal(ntData);
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
				}
			} else {
				setErrormsg("NT系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	public synchronized String addNewNTBatchXimaProposal() {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Calendar currentDateTwo = new GregorianCalendar();
		currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
		currentDateTwo.set(Calendar.MINUTE, 59);
		currentDateTwo.set(Calendar.SECOND, 59);
		dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
		dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
		dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
		dc = dc.add(Restrictions.in("remark", new Object[] { "nt系统洗码", "nt系统洗码;执行:", "nt系统洗码;executed:" }));
		List list = proposalService.findByCriteria(dc);
		if (list.size() <= 0) {
			try {
				SimpleDateFormat dfHH = new SimpleDateFormat("HH");
				Date date = new Date();
				Integer hh = Integer.parseInt(dfHH.format(date));
				if (hh < 3) {
					setErrormsg("只能在3点之后才能提交");
					return SUCCESS;
				}
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cals = Calendar.getInstance();
				cals.setTime(new Date());
				cals.add(Calendar.DAY_OF_MONTH, -1);
				String dateTime = formatter.format(cals.getTime());
				List<Bean4Xima> betsList = SlotUtil.getBetsAmount(dateTime, SlotUtil.NT_PLATFORM);
				if (betsList != null && betsList.size()>0) {
					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMANT);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}
				} else {
					setErrormsg("NT洗码数据抓取为空");
					return SUCCESS;
				}
				boolean flag = true;
				List<Bean4Xima> ntDataTemp = null;
				for (int i = 0; i < betsList.size(); i++) {
					log.info("\nNT洗码已添加处理" + i + "条数据");
					if (null == ntDataTemp || ntDataTemp.size() == 0) {
						ntDataTemp = new ArrayList<Bean4Xima>();
					}
					Bean4Xima bean4Xima = betsList.get(i);
					bean4Xima.setProfit(Arith.sub(bean4Xima.getBetAmount(), bean4Xima.getProfit()));
					ntDataTemp.add(bean4Xima);
					if (ntDataTemp.size() == 500 || i == betsList.size() - 1) {
						String msg = proposalService.xima4NT(ntDataTemp);
						if (StringUtils.isNotBlank(msg)) {
							flag = false;
						}
						ntDataTemp = null;
					}
				}

				if (flag) {
					setErrormsg("NT洗码数据提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: ");
					return SUCCESS;
				}
			} catch (Exception e) {
				log.error("NT系统洗码异常：", e);
				setErrormsg("系统异常！");
			}
		} else {
			setErrormsg("NT系统洗码已经导入！请不要再次操作！");
		}
		return SUCCESS;
	}

	public synchronized String addEBetAppBatchXimaProposal() {
		final String platform = "EBetApp" ;
		try {
			if (getTodayCreateProfitList(platform).isEmpty()) {
				//尚未产生洗码Proposal , 进行产生
				if (isTimeBeforeLimited(3)) {
					setErrormsg("只能在3点之后才能提交");
					return SUCCESS;
				}
				List<PlatformData> profits = fetchEBetAppJobService.processData();
				if (profits == null || profits.isEmpty()) {
					setErrormsg("获取" +platform+"数据 , 昨天没有任何记录");
					return SUCCESS;
				}
				
				String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAEBETAPP);
				if (ximalogStr!=null&&ximalogStr!="") {
					setErrormsg("提交失败: "+ximalogStr);
					return SUCCESS;
				}
				
				String msg = proposalService.autoAddXimaEBetAppProposal(profits);
				if (msg == null) {
					setErrormsg("提交成功");
				} else {
					setErrormsg("提交失败: " + msg);
				}
				return SUCCESS;
			} else {
				setErrormsg(platform + "系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题! " + e1.getMessage());
		}
		return SUCCESS;
	}

	private List<AgProfit> getTodayCreateProfitList(String platformCode) {
		Date startDay = new DateTime().withTimeAtStartOfDay().toDate();
		Date endDay = new DateTime().plusDays(1).withTimeAtStartOfDay().minusSeconds(1).toDate();
		DetachedCriteria dc = DetachedCriteria.forClass(AgProfit.class);
		dc = dc.add(Restrictions.ge("createTime", startDay));
		dc = dc.add(Restrictions.le("createTime", endDay));
		dc.add(Restrictions.like("remark", "%"+ platformCode +"平台输赢值"));
		return proposalService.findByCriteria(dc);
	}

	/**
	 * 比对 时间是否为 limitedHour 之前
	 *
	 * @param limitedHour format HH 24小时制
	 * @return
	 */
	private boolean isTimeBeforeLimited(int limitedHour) {
		final SimpleDateFormat dfHH = new SimpleDateFormat("HH");
		Integer nowHH = Integer.parseInt(dfHH.format(new Date()));
		return nowHH < limitedHour;
	}

			
	public synchronized String addEaXimaProposal() {
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "ea系统洗码", "ea系统洗码;执行:", "ea系统洗码;executed:" }));
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				SimpleDateFormat dfHH = new SimpleDateFormat("HH");
				SimpleDateFormat dfMM = new SimpleDateFormat("mm");
				Date date = new Date();
				Integer hh = Integer.parseInt(dfHH.format(date))*60;
				Integer mm = Integer.parseInt(dfMM.format(date));
				if((hh+mm)<(13*60+30)){
					out.println("只能在13:30点之后才能提交");
					return null;
				}
				// 昨天12点
				Calendar c = Calendar.getInstance();
				c.setTime(DateUtil.now());
				c.add(Calendar.DAY_OF_MONTH, -1);
				c.set(Calendar.HOUR_OF_DAY, 12);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date start = c.getTime();
				c.add(Calendar.DAY_OF_MONTH, 1);
				c.set(Calendar.HOUR_OF_DAY, 12);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date end = c.getTime();
				DetachedCriteria d = DetachedCriteria.forClass(EaData.class);
				d.add(Restrictions.and(Restrictions.ge("starttime", start), Restrictions.lt("endtime", end)));
				List<EaData> eaData = proposalService.getHibernateTemplate().findByCriteria(d);
				if (eaData == null || eaData.size() <= 0 || eaData.get(0) == null) {
					out.println("获取数据失败");
					return null;
				}
				String msg = proposalService.autoAddXimaEaProposal(eaData);
				if (msg == null) {
					out.println("提交成功");
					return null;
				} else {
					out.println("提交失败: " + msg);
					return null;
				}
			} else {
				out.println("EA系统洗码已经导入！请不要再次操作！");
				return null;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			out.println("获取当天时间查询问题!" + e1.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	public synchronized String addAgXimaProposal() {
		if (isTimeBeforeLimited(13)) {
			setErrormsg("只能在13点之后才能提交");
			return SUCCESS;
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(AgProfit.class);
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Calendar currentDateTwo = new GregorianCalendar();
		currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
		currentDateTwo.set(Calendar.MINUTE, 59);
		currentDateTwo.set(Calendar.SECOND, 59);
		dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
		dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
		dc = dc.add(Restrictions.eq("platform", belong));
		
		List list = proposalService.findByCriteria(dc);
		if (list.size() > 0) {
			setErrormsg(belong+"系统洗码已经导入，请勿重复操作");
			return SUCCESS;
		}
		
		String sql = "select playName,SUM(validBetAmount)bet,SUM(netAmount)amount from agdata where platformType=:platformType and DATE(recalcuTime) =:startTime GROUP BY playName ";

		String startTime = DateUtil.fmtYYYY_MM_DD(new DateTime().minusDays(1).withTimeAtStartOfDay().toDate());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("platformType", belong);
		params.put("startTime", startTime);
		
		try {
			List betsList = proposalService.getListBySql(sql,params);
			if(betsList.size() ==0 ){
				setErrormsg("提交失败:没有可洗码的数据！");
				return SUCCESS;
			}
			String msg = proposalService.autoAddXimaAgProposal(betsList,belong);
			if (msg == null) {
				setErrormsg("提交成功");
			} else {
				setErrormsg("提交失败: " + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败: " + e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 捕鱼洗码
	 * @return
	 * */
	public synchronized String addFishXimaProposal() {
		
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			
			dc = dc.add(Restrictions.eq("remark", "aginfish系统洗码"));
			
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				if (null == fileName || fileName.equals("")) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				String filehouzhui = getExtention(fileName);

				if (!filehouzhui.equals(".xls")) {
					setErrormsg("文件格式必须是excel");
					return INPUT;
				}
				
				File uplodaFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));
				if(!uplodaFiles.exists()){
					uplodaFiles.mkdir();
				}
				excelFileName = new Date().getTime() + getExtention(fileName);

				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
				copy(myFile, file);
				
				
				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
				try {
					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAAGFISH);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}
					 String msg = this.batchGameinfoServiceImpl.systemXimaForGf(file);
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
					e.printStackTrace();
					setErrormsg("提交失败: " + e);
				}
			} else {
				setErrormsg("aginfish系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
		
	}
	
	public synchronized String addNewPtSkyBatchXimaProposal() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "PTSKY系统洗码", "PTSKY系统洗码;执行:", "PTSKY系统洗码;executed:" }));
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				try {
					SimpleDateFormat dfHH = new SimpleDateFormat("HH");
					Date date = new Date();
					Integer hh = Integer.parseInt(dfHH.format(date));
					if (hh < 3) {
						setErrormsg("只能在3点之后才能提交");
						return SUCCESS;
					}
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cals = Calendar.getInstance();
					cals.setTime(date);
					cals.add(Calendar.DAY_OF_MONTH, -1);
					String dateTime = formatter.format(cals.getTime());

					
					
					List<Bean4Xima> betsList = SlotUtil.getBetsAmount(dateTime);
					
					String ximalogStr = proposalService.insertSystemLogs(getOperatorLoginname(), SystemLogType.XIMAPTSKY);
					
					if (ximalogStr != null && ximalogStr != "") {
						setErrormsg("提交失败: " + ximalogStr);
						return SUCCESS;
					}
					
					//批量处理数据
					boolean flag = true ;
					List<Bean4Xima> ptDataTemp = null ;
					for (int i = 0; i < betsList.size(); i++) {
						if(null == ptDataTemp || ptDataTemp.size() == 0){
							ptDataTemp = new ArrayList<Bean4Xima>() ;
						}
						ptDataTemp.add(betsList.get(i)) ;
						if(ptDataTemp.size() == 500 || i == betsList.size()-1){
							String msg = proposalService.addNewPtSkyBatchXimaProposal(ptDataTemp);
							if(StringUtils.isNotBlank(msg)){
								flag = false ;
							}
							ptDataTemp = null ;
						}
					}
					
					if (flag) {
						
				
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: ");
						return SUCCESS;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {
				setErrormsg("PTSKY系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	/**
	 * PT SkyWind系统洗码（报表）
	 * */
	public synchronized String addPTSkyBatchXimaProposalExcel() {
		
		try {
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			/*if(hour<15){
				GsonUtil.GsonObject("提示：请您每天的下午3点后提交") ;
				return SUCCESS;
			}*/
			
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "PTSKY系统洗码", "PTSKY系统洗码;执行:", "PTSKY系统洗码;executed:" }));
			
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				if (null == fileName || fileName.equals("")) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				String filehouzhui = getExtention(fileName);
				
				if (!filehouzhui.equals(".xls")) {
					setErrormsg("文件格式必须是excel");
					return INPUT;
				}
				excelFileName = new Date().getTime() + getExtention(fileName);
				
				File uplodaFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));
				if(!uplodaFiles.exists()){
					uplodaFiles.mkdir();
				}
				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
				
				copy(myFile, file);
				
				
				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
				try {
					 List<Bean4Xima> dataList = this.batchGameinfoServiceImpl.excelToPtSkyVo(file);
					if(dataList == null || dataList.size() == 0){
						setErrormsg("提交失败，没有获取到有效数据！");
						return SUCCESS;
					}
					
					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAPTSKY);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}
					
					boolean flag = true;
					List<Bean4Xima> tempList = null;
					for (int i = 0; i < dataList.size(); i++) {
						log.info("\n已添加处理"+i+"条数据");
						if(null == tempList || tempList.size() == 0){
							tempList = new ArrayList<Bean4Xima>() ;
						}
						tempList.add(dataList.get(i)) ;
						if(tempList.size() == 500 || i == dataList.size()-1){
							String msg = proposalService.addNewPtSkyBatchXimaProposal(tempList);							if(StringUtils.isNotBlank(msg)){
								flag = false ;
							}
							tempList = null ;
						}
					}
					
					if (flag) {
						setErrormsg("ptsky洗码数据提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: ");
						return SUCCESS;
					}
				} catch (Exception e) {
					e.printStackTrace();
					setErrormsg("提交失败: " + e);
				}
			} else {
				setErrormsg("ptsky系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	/**
	 * ag老虎机洗码
	 * @return
	 * */
	public synchronized String addAginSlotXimaProposal() {
		
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			
			dc = dc.add(Restrictions.eq("remark", "aginslot系统洗码"));
			
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				if (null == fileName || fileName.equals("")) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				String filehouzhui = getExtention(fileName);

				if (!filehouzhui.equals(".xls")) {
					setErrormsg("文件格式必须是excel");
					return INPUT;
				}
				excelFileName = new Date().getTime() + getExtention(fileName);

				File uplodaFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));
				if(!uplodaFiles.exists()){
					uplodaFiles.mkdir();
				}
				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
				
				copy(myFile, file);
				
				
				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
				try {
					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAAGSLOT);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}
					 String msg = this.batchGameinfoServiceImpl.systemXimaForAgSlot(file);
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
					e.printStackTrace();
					setErrormsg("提交失败: " + e);
				}
			} else {
				setErrormsg("aginslot系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
		
	}
	
	/**
	 * ag老虎机洗码
	 * @return
	 * */
	public synchronized String addAutoAginSlotXimaProposal() {
			try {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				Calendar currentDate = new GregorianCalendar();
				currentDate.set(Calendar.HOUR_OF_DAY, 0);
				currentDate.set(Calendar.MINUTE, 0);
				currentDate.set(Calendar.SECOND, 0);
				Calendar currentDateTwo = new GregorianCalendar();
				currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
				currentDateTwo.set(Calendar.MINUTE, 59);
				currentDateTwo.set(Calendar.SECOND, 59);
				dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
				dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
				dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
				dc = dc.add(Restrictions.in("remark", new Object[] { "aginslot系统洗码", "aginslot系统洗码;执行:", "aginslot系统洗码;executed:" }));
				List list = proposalService.findByCriteria(dc);
				if (list.size() <= 0) {
					try {
						SimpleDateFormat dfHH = new SimpleDateFormat("HH");
						Date date = new Date();
						Integer hh = Integer.parseInt(dfHH.format(date));
						if (hh < 3) {
							setErrormsg("只能在3点之后才能提交");
							return SUCCESS;
						}
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat formatterTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Calendar cals = Calendar.getInstance();
						cals.setTime(date);
						cals.add(Calendar.DAY_OF_MONTH, -1);
						String dateTime = formatter.format(cals.getTime());
						Date start = formatterTwo.parse(dateTime+" 12:00:00");
						cals.add(Calendar.DAY_OF_MONTH, +1);
						dateTime = formatter.format(cals.getTime());
						Date end = formatterTwo.parse(dateTime+" 11:59:59");
						
					       log.info(".....aginslot 返水操作......."+start+"------"+end);
						List listdata =proposalService.sqlAutoQueryList(formatterTwo.format(start),formatterTwo.format(end));
						if(listdata==null || listdata.size()<=0 ||listdata.get(0)==null){
							setErrormsg("没用户下注，没记录");
							return SUCCESS;
						}
						
						String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAAGSLOT);
						if (ximalogStr!=null&&ximalogStr!="") {
							setErrormsg("提交失败: "+ximalogStr);
							return SUCCESS;
						}
						
						String msg = proposalService.autoAddXimaAgSlotProposal(listdata);
						if (msg == null) {
							setErrormsg("提交成功");
							return SUCCESS;
						} else {
							setErrormsg("提交失败: " + msg);
							return SUCCESS;
						}
					} catch (Exception e) {
					    e.printStackTrace();
					}
				} else {
					setErrormsg("AG aginslot系统洗码已经导入！请不要再次操作！");
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				setErrormsg("获取当天时间查询问题!");
			}
			return SUCCESS;
	}
	

	public synchronized String addBbinXimaProposal() {
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "bbin系统洗码", "bbin系统洗码;执行:", "bbin系统洗码;executed:" }));
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				SimpleDateFormat dfHH = new SimpleDateFormat("HH");
				SimpleDateFormat dfMM = new SimpleDateFormat("mm");
				Date date = new Date();
				Integer hh = Integer.parseInt(dfHH.format(date))*60;
				Integer mm = Integer.parseInt(dfMM.format(date));
				if((hh+mm)<(13*60+30)){
					out.println("只能在13:30点之后才能提交");
					return null;
				}
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH, -1);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date start = c.getTime();
				c.add(Calendar.DAY_OF_MONTH, 1);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date end = c.getTime();
				DetachedCriteria d = DetachedCriteria.forClass(BbinData.class);
				d.add(Restrictions.and(Restrictions.ge("startday", start), Restrictions.lt("endday", end)));
				List<BbinData> bbinData = proposalService.getHibernateTemplate().findByCriteria(d);
				if (bbinData == null || bbinData.size() <= 0 || bbinData.get(0) == null) {
					out.println("获取数据失败");
					return null;
				}
				/*
				 * String msg =
				 * proposalService.autoAddXimaBbinProposal(bbinData); if (msg ==
				 * null) { out.println("提交成功"); return null; } else {
				 * out.println("提交失败: " + msg); return null; }
				 */
				return null;
			} else {
				out.println("BBIN系统洗码已经导入！请不要再次操作！");
				return null;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			out.println("获取当天时间查询问题!" + e1.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	public synchronized String addAgTryRecord() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.eq("remark", "agtry系统洗码"));

			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				// System.out.println("rate: "+rate);
				if (null == fileName || fileName.equals("")) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				String filehouzhui = getExtention(fileName);

				if (!filehouzhui.equals(".xls")) {
					setErrormsg("文件格式必须是excel");
					return INPUT;
				}
				excelFileName = new Date().getTime() + getExtention(fileName);

				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
				copy(myFile, file);
				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
				try {
					String msg = batchGameinfoServiceImpl.addAgTry(file);
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {
				setErrormsg("agtry系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	
	
	public String addMail() {
		try {
			if (null == fileName || fileName.equals("")) {
				setErrormsg("请先提交文件");
				return INPUT;
			}
			String filehouzhui = getExtention(fileName);
			if (!filehouzhui.equals(".xls")) {
				setErrormsg("文件格式必须是excel");
				return INPUT;
			}
			excelFileName = new Date().getTime() + getExtention(fileName);

			File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
			copy(myFile, file);
			batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
			try {
				String msg = batchGameinfoServiceImpl.addMail(file);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;	
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	

	public String addPhone() {
		try {
			if (null == fileName || fileName.equals("")) {
				setErrormsg("请先提交文件");
				return INPUT;
			}
			String filehouzhui = getExtention(fileName);
			if (!filehouzhui.equals(".csv")) {
				setErrormsg("文件格式必须是csv");
				return INPUT;
			}
			excelFileName = new Date().getTime() + getExtention(fileName);

			File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
			copy(myFile, file);
			batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
			try {
				String msg = batchGameinfoServiceImpl.addPhone(file);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;	
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	public String downloadMailExcel() {
		try {
			List<RecordMail>list=new ArrayList<RecordMail>();
			DetachedCriteria dc = DetachedCriteria.forClass(RecordMail.class);
				dc = dc.add(Restrictions.eq("status",0));
				System.out.println(start);
				System.out.println(end);
			if (start != null) {
				dc = dc.add(Restrictions.ge("createtime", start));
			}
			if (end != null) {
				dc = dc.add(Restrictions.lt("createtime", end));
			}
			
			list=proposalService.findByCriteria(dc);
			
	 		OutputStream os = getResponse().getOutputStream();//取得输出流
	 		getResponse().setHeader("Content-Disposition","attachment;filename="+"mail.xls");
	 		getResponse().setContentType("application/msexcel");//定义输出类型
			XlsUtil.createExcel(os,list);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return null;
	}
	
	
	public String addAgentCustomer(){
		try {
				if (null == fileName || fileName.equals("")) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				String filehouzhui = getExtention(fileName);
				if (!filehouzhui.equals(".xls")) {
					setErrormsg("文件格式必须是excel");
					return INPUT;
				}
				excelFileName = new Date().getTime() + getExtention(fileName);
				File UploadFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));
				if(!UploadFiles.exists()){
					UploadFiles.createNewFile();
				}
				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
				copy(myFile, file);
				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
				try {
					String msg = batchGameinfoServiceImpl.addAgentPhone(file);
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	//导入pt8元优惠劵邮箱
	public String addPtCoupon(){
		try {
				if (null == fileName || fileName.equals("")) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				String filehouzhui = getExtention(fileName);
				if (!filehouzhui.equals(".xls")) {
					setErrormsg("文件格式必须是excel");
					return INPUT;
				}
				excelFileName = new Date().getTime() + getExtention(fileName);
				File UploadFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));
				if(!UploadFiles.exists()){
					UploadFiles.createNewFile() ;
				}
				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
				
				copy(myFile, file);
				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
				try {
					String msg = batchGameinfoServiceImpl.addPtCoupon(file,getOperatorLoginname
							());
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setErrormsg("导入pt8元优惠劵邮箱!");
		}
		return SUCCESS;
	}

	public String addAttachment() {
		// System.out.println("rate: "+rate);
		String filetemp = "";
		if (fileName != null) {
			excelFileName = new Date().getTime() + getExtention(fileName);

			File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
			copy(myFile, file);
			filetemp = file.getName();
		}
		if (businessProposalType == null || businessProposalType.equals("")) {
			setErrormsg("请选择事务类型");
			return INPUT;
		}
		if (amount == null || amount.equals("")) {
			setErrormsg("请填写金额");
			return INPUT;
		}

		/*
		 * if(belong==null ||belong.equals("")){ setErrormsg("请填写当属月份"); return
		 * INPUT; }
		 */

		try {

			String msg = proposalService.addBusiness(getOperatorLoginname(), depositname, depositaccount, depositbank, businessProposalType, Double.valueOf(amount), remark, StringUtils.trim(filetemp), belong, 5);

			if (msg == null)
				setErrormsg("提交成功");
			else
				setErrormsg("提交失败: " + msg);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;

	}

	public String addpesoAttachment() {
		// System.out.println("rate: "+rate);
		String filetemp = "";
		if (fileName != null) {
			excelFileName = new Date().getTime() + getExtention(fileName);

			File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
			copy(myFile, file);
			filetemp = file.getName();
		}
		if (businessProposalType == null || businessProposalType.equals("")) {
			setErrormsg("请选择事务类型");
			return INPUT;
		}
		if (amount == null || amount.equals("")) {
			setErrormsg("请填写金额");
			return INPUT;
		}

		/*
		 * if(belong==null ||belong.equals("")){ setErrormsg("请填写当属月份"); return
		 * INPUT; }
		 */

		try {

			String msg = proposalService.addBusiness(getOperatorLoginname(), depositname, depositaccount, depositbank, businessProposalType, Double.valueOf(amount), remark, StringUtils.trim(filetemp), belong, 6);

			if (msg == null)
				setErrormsg("提交成功");
			else
				setErrormsg("提交失败: " + msg);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;

	}

	public String excBusinessProposal() {
		// System.out.println("rate: "+rate);
		String filetemp = "";
		if (fileName != null) {
			excelFileName = new Date().getTime() + getExtention(fileName);

			File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
			copy(myFile, file);
			filetemp = file.getName();
		}

		if (bankinfoid.equals("0")) {
			setErrormsg("请选择支付帐号");
			return INPUT;
		}

		try {

			String msg = proposalService.excuteBusinessProposal(pno, getOperatorLoginname(), getIp(), StringUtils.trim(remark), bankinfoid, fee, actualmoney, StringUtils.trim(filetemp));

			if (msg == null)
				setErrormsg("提交成功");
			else
				setErrormsg("提交失败: " + msg);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;

	}

	public String addBatchProfitProposal() {
		// System.out.println("rate: "+rate);
		if (null == fileName || fileName.equals("")) {
			setErrormsg("请先提交文件");
			return INPUT;
		}
		String filehouzhui = getExtention(fileName);

		if (!filehouzhui.equals(".xls")) {
			setErrormsg("文件格式必须是excel");
			return INPUT;
		}
		excelFileName = new Date().getTime() + getExtention(fileName);

		File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
		copy(myFile, file);
		batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchProfitProposalService");
		try {
			String msg = batchGameinfoServiceImpl.autoAddXimaProposal(file, 0.0);
			if (msg == null) {
				setErrormsg("提交成功");
				return SUCCESS;
			} else {
				setErrormsg("提交失败: " + msg);
				return SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		setErrormsg("提交成功");
		return SUCCESS;
	}

	public String addBatchPrizeProposal() {
		// System.out.println("rate: "+rate);
		if (null == fileName || fileName.equals("")) {
			setErrormsg("请先提交文件");
			return INPUT;
		}
		String filehouzhui = getExtention(fileName);

		if (!filehouzhui.equals(".xls")) {
			setErrormsg("文件格式必须是excel");
			return INPUT;
		}
		excelFileName = new Date().getTime() + getExtention(fileName);

		File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
		copy(myFile, file);
		batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchPrizeProposalService");
		try {
			String msg = batchGameinfoServiceImpl.autoAddXimaProposal(file, 0.0);
			if (msg == null) {
				setErrormsg("提交成功");
				return SUCCESS;
			} else {
				setErrormsg("提交失败: " + msg);
				return SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		setErrormsg("提交成功");
		return SUCCESS;
	}

	public String excelsendMail() {
		// System.out.println("rate: "+rate);
		if (null == fileName || fileName.equals("")) {
			setErrormsg("请先提交文件");
			return INPUT;
		}
		String filehouzhui = getExtention(fileName);

		if (!filehouzhui.equals(".xls")) {
			setErrormsg("文件格式必须是excel");
			return INPUT;
		}
		excelFileName = new Date().getTime() + getExtention(fileName);

		File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
		copy(myFile, file);
		batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("excelsendMail");
		try {
			String msg = batchGameinfoServiceImpl.autoAddCommissions(file);
			if (msg == null) {
				setErrormsg("提交成功");
				return SUCCESS;
			} else {
				setErrormsg("提交失败: " + msg);
				return SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		setErrormsg("提交成功");
		return SUCCESS;
	}

	public synchronized String sendmessage() {
		ximaService = (IGameinfoService) SpringFactoryHepler.getInstance("ximasendMessageService");
		ximaService.run();
		this.println("发送成功");
		return null;
	}

	public String addCommissions() {
		String filehouzhui = getExtention(fileName);

		if (!filehouzhui.equals(".xls")) {
			setErrormsg("文件格式必须是excel");
			return INPUT;
		}
		excelFileName = new Date().getTime() + getExtention(fileName);

		File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
		copy(myFile, file);
		batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addCommissionsService");
		try {
			String msg = batchGameinfoServiceImpl.autoAddCommissions(file);
			if (msg == null) {
				setErrormsg("提交成功");
				return SUCCESS;
			} else {
				setErrormsg("提交失败: " + msg);
				return SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		setErrormsg("提交成功");
		return SUCCESS;
	}

	public String execXimaProposal() {
		//ximaService = (IGameinfoService) SpringFactoryHepler.getInstance("execBatchXimaProposalService");
		//ximaService.run();
		//this.println("正在执行洗码提案...");
		//return null;
		
		String exeReslut = SynchronizedUtil.getInstance().exeXima(proposalService);
		this.println(exeReslut);
		return INPUT;
	}

	public synchronized String execPrize() {
		ximaService = (IGameinfoService) SpringFactoryHepler.getInstance("execBatchExecPrizeService");
		ximaService.run();
		this.println("正在执行幸运抽奖提案...");
		return null;
	}

	public synchronized String execProfit() {
		ximaService = (IGameinfoService) SpringFactoryHepler.getInstance("execBatchExecProfitService");
		ximaService.run();
		this.println("正在执行负盈利反赠提案...");
		return null;
	}

	public synchronized String execCommissions() {
		ximaService = (IGameinfoService) SpringFactoryHepler.getInstance("execCommissionsService");
		ximaService.run();
		this.println("正在执行佣金记录...");
		return null;
	}
	
	public synchronized String addNTwoBatchXimaProposal() {
		final String platform = "N2Live";
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(AgProfit.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", currentDateTwo.getTime().clone()));
			dc.add(Restrictions.like("remark", "%"+platform+"平台输赢值%"));
			List list = proposalService.findByCriteria(dc);
			if (list.isEmpty()) {
				SimpleDateFormat dfHH = new SimpleDateFormat("HH");
				Date date = new Date();
				Integer hh = Integer.parseInt(dfHH.format(date));
				if (hh < 12) {
					setErrormsg("只能在12点之后才能提交");
					return SUCCESS;
				}
				List<PlatformData> profits = fetchNTwoJobService.processData();
				if (profits == null || profits.isEmpty()) {
					setErrormsg("获取" +platform+"数据 , 昨天没有任何记录");
					return SUCCESS;
				}
				
				String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMANN2LIVE);
				if (ximalogStr!=null&&ximalogStr!="") {
					setErrormsg("提交失败: "+ximalogStr);
					return SUCCESS;
				}
				
				
				String msg = proposalService.autoAddXimaNTwoProposal(profits);
				if (msg == null) {
					setErrormsg("提交成功");
				} else {
					setErrormsg("提交失败: " + msg);
				}
				return SUCCESS;
				
			} else {
				setErrormsg(platform + "系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	private static void copy(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}

	public String checkNotExecProposal() {

		Date starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1 * 24 + 12));
		try {
			List<Proposal> proposalList = proposalService.getNotExecProposal(starttime, new Date());
			if (proposalList == null || proposalList.size() <= 0) {
				this.isExec = true;
				this.println("ok,可以执行反水");
			} else {
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
	
public String addPtCommissions() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<17){
			GsonUtil.GsonObject("提示：请您每天的下午5点后提交") ;
			return null ;
		}
		batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addCommissionsService");
		try {
			String msg = batchGameinfoServiceImpl.addLiveGameCommissions(executetime);
			GsonUtil.GsonObject(msg) ;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return null ;
	}
	
	public String executePtCommissions(){
		if(StringUtils.isNotBlank(ids)){
			String msg = SynchrPtCommissionsUtil.getInstance().executePtCommission(proposalService, getOperatorLoginname(), ids);
			GsonUtil.GsonObject(msg);
		}else{
			GsonUtil.GsonObject("未选中需要执行的数据");
		}
		return null ;
	}
	
	public String executeAgentVip(){
		if(StringUtils.isNotBlank(ids)){
			String msg = SynchrAgentVipUtil.getInstance().executeAgentVip(proposalService, getOperatorLoginname(), ids , level);
			GsonUtil.GsonObject(msg);
		}else{
			GsonUtil.GsonObject("未选中需要执行的数据");
		}
		return null ;
	}
	
	
	/**
	 * 批量导入优惠
	 * @throws UnsupportedEncodingException 
	 */
	public void impPrivileges() throws UnsupportedEncodingException{
		this.getResponse().setContentType("text/html;charset=utf-8");  
		excelFileName = new Date().getTime() + getExtention(fileName);
		File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
		copy(myFile, file);
		try {
			String result = BatchImpPrivileges.impPrivileges(proposalService, file);
			println(result);
		} catch (Exception e) {
			e.printStackTrace();
			println("服务异常");
		} 
	}
	
	/**
	 * MWG系统洗码
	 * */
	public synchronized String addMWGBatchXimaProposal() {
		String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAMWG);
		if (ximalogStr!=null&&ximalogStr!="") {
			setErrormsg("提交失败: "+ximalogStr);
			return SUCCESS;
		}
		
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.eq("remark", "mwg系统洗码"));
			
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				String startTime = DateUtil.getchangedDate(-1)+" 00:00:00";
				String endTime = DateUtil.getchangedDate(-1)+ " 23:59:59";
				String result = MWGUtils.getMwgXimaData(startTime,endTime);
				List mwgData = JSONArray.fromObject(result);
				if (mwgData == null || mwgData.size() <= 0 || mwgData.get(0) == null) {
					setErrormsg("无洗码数据，请核对下再试！");
					return SUCCESS;
				}
				String msg = proposalService.autoAddXimaMwgProposal(mwgData);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} else {
				setErrormsg("mwg系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("系统异常，请联系技术");
		}
		return SUCCESS;
	}
	

	public synchronized String addDtBatchXimaProposal() {
		final String platform = "dt";
		try {
			if (getTodayCreateProfitList(platform).isEmpty()) {
	//			List<Object> list =DtUtil.getbet(null,DateUtil.getchangedDate(-1)+" 00:00:00",DateUtil.getTodayFormat());
				List<Object> list =DtUtil.getbetByDay(null,DateUtil.getchangedDate(-1),DateUtil.getchangedDate(-1));
				if (null ==list||list.size()<0) {
					setErrormsg("数据抓取失败");
					return INPUT;
				}
				List<NTProfit> dtData = new ArrayList<NTProfit>();
				for(Object obj : list){
					NTProfit nf = new NTProfit();
					Map<String,Object> map = (Map<String,Object>)obj;
					String loginname = map.get("playerName").toString();
					nf.setLoginname(loginname.toLowerCase());
					//String betWins = map.get("betWins")+""; 
					String betPrice = map.get("betPrice")+"";
					String prizeWins = map.get("prizeWins")+"";
					Double betCredit = Double.parseDouble(betPrice);// 投注额
					Double amount = -Double.parseDouble(prizeWins); // 记录的Amount为平台盈利
					nf.setBetCredit(betCredit);
					nf.setAmount(amount);
					dtData.add(nf);
				}
				
				String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMADT);
				if (ximalogStr!=null&&ximalogStr!="") {
					setErrormsg("提交失败: "+ximalogStr);
					return SUCCESS;
				}
				
				String msg = proposalService.autoAddXimaDtProposal(dtData);
				if (msg == null) {
					setErrormsg("提交成功");
				} else {
					setErrormsg("提交失败: " + msg);
				}
				return SUCCESS;
			}else{
				setErrormsg(platform + "系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		} 
		return SUCCESS;
	}
	
	/**
	 * MG 系统洗码
	 * @return
	 */
	public synchronized String addNewMGSBatchXimaProposal(){
		if (isTimeBeforeLimited(3)) {
			setErrormsg("只能在3点之后才能提交");
			return SUCCESS;
		}
		String countSql = "select count(*) from agprofit where createTime >= :startTime and createTime <= :endTime and platform = :platform";
		Map<String, Object> params = new HashMap<String, Object>();
		Date startTime = new DateTime().withTimeAtStartOfDay().toDate();
		Date endTime = new DateTime().plusDays(1).withTimeAtStartOfDay().minusSeconds(1).toDate();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("platform", "mg");
		if(proposalService.getCount(countSql, params) > 0){
			setErrormsg("MG系统洗码已经导入，请勿重复操作");
		}else{
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cals = Calendar.getInstance();
				cals.setTime(new Date());
				cals.add(Calendar.DAY_OF_MONTH, -1);
				String dateTime = formatter.format(cals.getTime());
				List<Bean4Xima> betsList = SlotUtil.getBetsAmount(dateTime, SlotUtil.MG_PLATFORM);
				if (betsList != null && betsList.size()>0) {
					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAMG);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}
				} else {
					setErrormsg("MG洗码数据抓取为空");
					return SUCCESS;
				}
				boolean flag = true;
				List<Bean4Xima> ptDataTemp = null;
				for (int i = 0; i < betsList.size(); i++) {
					log.info("\n已添加处理" + i + "条数据");
					if (null == ptDataTemp || ptDataTemp.size() == 0) {
						ptDataTemp = new ArrayList<Bean4Xima>();
					}
					Bean4Xima bean4Xima = betsList.get(i);
					bean4Xima.setProfit(Arith.sub(bean4Xima.getBetAmount(), bean4Xima.getProfit()));
					ptDataTemp.add(bean4Xima);
					if (ptDataTemp.size() == 500 || i == betsList.size() - 1) {
						String msg = proposalService.xima4MG(ptDataTemp);
						if (StringUtils.isNotBlank(msg)) {
							flag = false;
						}
						ptDataTemp = null;
					}
				}

				if (flag) {
					setErrormsg("MG洗码数据提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: ");
					return SUCCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("查询MG游戏数据错误");
			}
		}
		return SUCCESS;
	}
	
	public void setMyFileContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setMyFileFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public File getMyFile() {
		return myFile;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	private void println(String msg) {
		PrintWriter out = null;
		try {
			out = res.getWriter();
			out.println(msg);
			out.flush();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		session = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		req = arg0;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		res = arg0;
	}

	
	private Date start;
	private Date end;
	
	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBusinessProposalType() {
		return businessProposalType;
	}

	public void setBusinessProposalType(String businessProposalType) {
		this.businessProposalType = businessProposalType;
	}

	public String getDepositbank() {
		return depositbank;
	}

	public void setDepositbank(String depositbank) {
		this.depositbank = depositbank;
	}

	public String getDepositname() {
		return depositname;
	}

	public void setDepositname(String depositname) {
		this.depositname = depositname;
	}

	public String getDepositaccount() {
		return depositaccount;
	}

	public void setDepositaccount(String depositaccount) {
		this.depositaccount = depositaccount;
	}

	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public String getBankinfoid() {
		return bankinfoid;
	}

	public void setBankinfoid(String bankinfoid) {
		this.bankinfoid = bankinfoid;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Double getActualmoney() {
		return actualmoney;
	}

	public void setActualmoney(Double actualmoney) {
		this.actualmoney = actualmoney;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public GuestBookService getGuestBookService() {
		return guestBookService;
	}

	public void setGuestBookService(GuestBookService guestBookService) {
		this.guestBookService = guestBookService;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getExecutetime() {
		return executetime;
	}

	public void setExecutetime(String executetime) {
		this.executetime = executetime;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	public void setFetchNTwoJobService(FetchNTwoJobService fetchNTwoJobService) {
		this.fetchNTwoJobService = fetchNTwoJobService;
	}

	public void setFetchEBetAppJobService(FetchEBetAppJobService fetchEBetAppJobService) {
		this.fetchEBetAppJobService = fetchEBetAppJobService;
	}

	public MGSService getMgsService() {
		return mgsService;
	}

	public void setMgsService(MGSService mgsService) {
		this.mgsService = mgsService;
	}
	
	public synchronized String addNewDTBatchXimaProposal() {
		final String platform = "dt";
		try {

//			if (getTodayCreateProfitList(platform).isEmpty()) {
				// 获取玩家投注额
				SimpleDateFormat dfHH = new SimpleDateFormat("HH");
				Date date = new Date();
				Integer hh = Integer.parseInt(dfHH.format(date));
				if (hh < 3) {
					setErrormsg("只能在3点之后才能提交");
					return SUCCESS;
				}
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cals = Calendar.getInstance();
				cals.setTime(date);
				cals.add(Calendar.DAY_OF_MONTH, -1);
				String dateTime = formatter.format(cals.getTime());
				List<Bean4Xima> betsList = SlotUtil.getDTBetsAmount(dateTime);
				if (null == betsList || betsList.size() < 0) {
					setErrormsg("数据抓取失败");
					return INPUT;
				}

				List<NTProfit> dtData = new ArrayList<NTProfit>();
				for (Bean4Xima obj : betsList) {
					NTProfit nf = new NTProfit();
					String loginname = obj.getUserName();
					nf.setLoginname(loginname.toLowerCase());
					String betPrice = obj.getBetAmount() + "";
					String prizeWins = obj.getProfit() + "";
					Double betCredit = Double.parseDouble(betPrice);// 投注额
					Double amount = -Double.parseDouble(prizeWins); // 记录的Amount为平台盈利
					nf.setBetCredit(betCredit);
					nf.setAmount(obj.getBetAmount()-obj.getProfit());
					dtData.add(nf);
				}
//				String ximalogStr = proposalService.insertSystemLogs(getOperatorLoginname(), SystemLogType.XIMADT);
//				if (ximalogStr != null && ximalogStr != "") {
//					setErrormsg("提交失败: " + ximalogStr);
//					return SUCCESS;
//				}

				String msg = proposalService.autoAddXimaDtProposal(dtData);
				if (msg == null) {
					setErrormsg("提交成功");
				} else {
					setErrormsg("提交失败: " + msg);
				}

				return SUCCESS;
//			} else {
//				setErrormsg(platform + "系统洗码已经导入！请不要再次操作！");
//			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	/**
	 * PB系统洗码
	 * @return
	 */
	public synchronized String addPBBatchXimaProposal(){
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<13){
			GsonUtil.GsonObject("提示：请您每天的下午1点后提交") ;
			return SUCCESS;
		}
		try {
			String sdate = DateUtil.getchangedDate(-1)+" 00:00:00";
			String edate = DateUtil.getchangedDate(-1)+ " 23:59:59";
			//查询平博体育昨天所有投注记录
			List<Bean4Xima> betsList = pbUtil.getAllWagers(sdate,edate);
			if (betsList != null && betsList.size()>0) {
				String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAPB);
				if (ximalogStr!=null&&ximalogStr!="") {
					setErrormsg("提交失败: "+ximalogStr);
					return SUCCESS;
				}
			} else {
				setErrormsg("PB洗码数据抓取为空");
				return SUCCESS;
			}
			boolean flag = true;
			List<Bean4Xima> ptDataTemp = null;
			for (int i = 0; i < betsList.size(); i++) {
				log.info("\n已添加处理" + i + "条数据");
				if (null == ptDataTemp || ptDataTemp.size() == 0) {
					ptDataTemp = new ArrayList<Bean4Xima>();
				}
				Bean4Xima bean4Xima = betsList.get(i);
				ptDataTemp.add(bean4Xima);
				if (ptDataTemp.size() == 500 || i == betsList.size() - 1) {
					String msg = proposalService.PbXiMA(ptDataTemp);
					if (StringUtils.isNotBlank(msg)) {
						flag = false;
					}
					ptDataTemp = null;
				}
			}

			if (flag) {
				setErrormsg("PB洗码数据提交成功");
				return SUCCESS;
			} else {
				setErrormsg("提交失败: ");
				return SUCCESS;
			}
		} catch (Exception e) {
			log.error(e);
			setErrormsg("查询PB体育数据错误");
		}
		return SUCCESS;
	}
	
	
	/**
	 * PNG系统洗码（中心钱包V1版本）
	 * */
	public synchronized String addPNGBatchXimaProposal_V1() {

		if (isTimeBeforeLimited(3)) {
			setErrormsg("请在3点之后提交");
			return SUCCESS;
		}
		String countSql = "select count(1) from agprofit where createTime >= :startTime and createTime <= :endTime and platform = :platform";
		Map<String, Object> params = new HashMap<String, Object>();
		Date startTime = new DateTime().withTimeAtStartOfDay().toDate();
		Date endTime = new DateTime().plusDays(1).withTimeAtStartOfDay().minusSeconds(1).toDate();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("platform", "png");
		if(proposalService.getCount(countSql, params) > 0){
			setErrormsg("PNG系统洗码已经导入，请勿重复操作");
		}else{
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cals = Calendar.getInstance();
				cals.setTime(new Date());
				cals.add(Calendar.DAY_OF_MONTH, -1);
				String dateTime = formatter.format(cals.getTime());
				List<Bean4Xima> betsList = SlotUtil.getBetsAmount(dateTime, SlotUtil.PNG_PLATFORM);
				if (betsList != null && betsList.size()>0) {
					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAPNG);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}
				} else {
					setErrormsg("PNG洗码数据抓取为空");
					return SUCCESS;
				}
				boolean flag = true;
				List<Bean4Xima> ptDataTemp = null;
				for (int i = 0; i < betsList.size(); i++) {
					log.info("\n已添加处理" + i + "条数据");
					if (null == ptDataTemp || ptDataTemp.size() == 0) {
						ptDataTemp = new ArrayList<Bean4Xima>();
					}
					Bean4Xima bean4Xima = betsList.get(i);
					bean4Xima.setProfit(Arith.sub(bean4Xima.getBetAmount(), bean4Xima.getProfit()));
					ptDataTemp.add(bean4Xima);
					if (ptDataTemp.size() == 500 || i == betsList.size() - 1) {
						String msg = proposalService.xima4PNG(ptDataTemp);
						if (StringUtils.isNotBlank(msg)) {
							flag = false;
						}
						ptDataTemp = null;
					}
				}

				if (flag) {
					setErrormsg("PNG洗码数据提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: ");
					return SUCCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("查询PNG游戏数据错误");
			}
		}
		return SUCCESS;
	}
	
	/*
	 * QT系统洗码中心钱包版本
	 */
	public String addQTBatchXimaProposal_V1(){
		if (isTimeBeforeLimited(3)) {
			setErrormsg("请在3点之后提交");
			return SUCCESS;
		}
		String countSql = "select count(*) from agprofit where createTime >= :startTime and createTime <= :endTime and platform = :platform";
		Map<String, Object> params = new HashMap<String, Object>();
		Date startTime = new DateTime().withTimeAtStartOfDay().toDate();
		Date endTime = new DateTime().plusDays(1).withTimeAtStartOfDay().minusSeconds(1).toDate();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("platform", "qt");
		if(proposalService.getCount(countSql, params) > 0){
			setErrormsg("QT系统洗码已经导入，请勿重复操作");
		}else{
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cals = Calendar.getInstance();
				cals.setTime(new Date());
				cals.add(Calendar.DAY_OF_MONTH, -1);
				String dateTime = formatter.format(cals.getTime());
				List<Bean4Xima> betsList = SlotUtil.getBetsAmount(dateTime, SlotUtil.QT_PLATFORM);
				if (betsList != null && betsList.size()>0) {
					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAQT);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}
				} else {
					setErrormsg("QT洗码数据抓取为空");
					return SUCCESS;
				}
				boolean flag = true;
				List<Bean4Xima> ptDataTemp = null;
				for (int i = 0; i < betsList.size(); i++) {
					log.info("\n已添加处理" + i + "条数据");
					if (null == ptDataTemp || ptDataTemp.size() == 0) {
						ptDataTemp = new ArrayList<Bean4Xima>();
					}
					Bean4Xima bean4Xima = betsList.get(i);
					bean4Xima.setProfit(Arith.sub(bean4Xima.getBetAmount(), bean4Xima.getProfit()));
					ptDataTemp.add(bean4Xima);
					if (ptDataTemp.size() == 500 || i == betsList.size() - 1) {
						String msg = proposalService.xima4QT(ptDataTemp);
						if (StringUtils.isNotBlank(msg)) {
							flag = false;
						}
						ptDataTemp = null;
					}
				}

				if (flag) {
					setErrormsg("QT洗码数据提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: ");
					return SUCCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("查询QT游戏数据错误");
			}
		}
		return SUCCESS;
	}	
	
	
	
	
	
	/**
	 * hyg系统洗码（报表）
	 * */
	public synchronized String addDTFishBatchXimaProposalExcel() {
		
		try {
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			/*if(hour<15){
				GsonUtil.GsonObject("提示：请您每天的下午3点后提交") ;
				return SUCCESS;
			}*/
			
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "HYG系统洗码", "HYG系统洗码;执行:", "HYG系统洗码;executed:" }));
			
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				if (null == fileName || fileName.equals("")) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				String filehouzhui = getExtention(fileName);
				
				if (!filehouzhui.equals(".xls")) {
					setErrormsg("文件格式必须是excel");
					return INPUT;
				}
				excelFileName = new Date().getTime() + getExtention(fileName);
				
				File uplodaFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));
				if(!uplodaFiles.exists()){
					uplodaFiles.mkdir();
				}
				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
				
				copy(myFile, file);
				
				
				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
				try {
					 List<Bean4Xima> dataList = this.batchGameinfoServiceImpl.excelToDTFishVo(file);
					if(dataList == null || dataList.size() == 0){
						setErrormsg("提交失败，没有获取到有效数据！");
						return SUCCESS;
					}
					
//					String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMADTFISH);
//					if (ximalogStr!=null&&ximalogStr!="") {
//						setErrormsg("提交失败: "+ximalogStr);
//						return SUCCESS;
//					}
					
					boolean flag = true;
					List<Bean4Xima> tempList = null;
					for (int i = 0; i < dataList.size(); i++) {
						log.info("\n已添加处理"+i+"条数据");
						if(null == tempList || tempList.size() == 0){
							tempList = new ArrayList<Bean4Xima>() ;
						}
						tempList.add(dataList.get(i)) ;
						if(tempList.size() == 500 || i == dataList.size()-1){
							String msg = proposalService.addDTFishBatchXimaProposal(tempList);			
							if(StringUtils.isNotBlank(msg)){
								flag = false ;
							}
							tempList = null ;
						}
					}
					
					if (flag) {
						setErrormsg("HYG洗码数据提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: ");
						return SUCCESS;
					}
				} catch (Exception e) {
					e.printStackTrace();
					setErrormsg("提交失败: " + e);
				}
			} else {
				setErrormsg("HYG系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}

	/**
	 * 比特游戏系统洗码
	 * @return
	 */
	public synchronized String addBITBatchXimaProposal(){
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<8){
			GsonUtil.GsonObject("提示：请您每天的上午8点后提交") ;
			return SUCCESS;
		}
		try {
			String sdate = DateUtil.getchangedDate(-1)+" 00:00:00";
			String edate = DateUtil.getchangedDate(-1)+ " 23:59:59";
			//查询比特游戏昨天所有投注记录
			List<Bean4Xima> betsList = BitGameUtil.bitGetBetting(sdate,edate);

			if (betsList != null && betsList.size()>0) {
				String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMABIT);
				if (ximalogStr!=null&&ximalogStr!="") {
					setErrormsg("提交失败: "+ximalogStr);
					return SUCCESS;
				}
			} else {
				setErrormsg("比特游戏洗码数据抓取为空");
				return SUCCESS;
			}
			boolean flag = true;
			List<Bean4Xima> ptDataTemp = null;
			for (int i = 0; i < betsList.size(); i++) {
				log.info("\n已添加处理" + i + "条数据");
				if (null == ptDataTemp || ptDataTemp.size() == 0) {
					ptDataTemp = new ArrayList<Bean4Xima>();
				}
				Bean4Xima bean4Xima = betsList.get(i);
				ptDataTemp.add(bean4Xima);
				if (ptDataTemp.size() == 500 || i == betsList.size() - 1) {
					String msg = proposalService.BitXiMA(ptDataTemp);
					if (StringUtils.isNotBlank(msg)) {
						flag = false;
					}
					ptDataTemp = null;
				}
			}

			if (flag) {
				setErrormsg("比特游戏洗码数据提交成功");
				return SUCCESS;
			} else {
				setErrormsg("提交失败: ");
				return SUCCESS;
			}
		} catch (Exception e) {
			log.error(e);
			setErrormsg("查询比特游戏数据错误");
		}
		return SUCCESS;
	}

	//==============================以上为旧代码==================	
	
	// 查询今天是否有提交agprofit记录
	private Integer queryAgprofit(String platform) {
		String countSql = "select count(1) from agprofit where createTime >= :startTime and createTime <= :endTime and platform = :platform";
		Map<String, Object> params = new HashMap<String, Object>();
		Date startT = new DateTime().withTimeAtStartOfDay().toDate();
		Date endT = new DateTime().plusDays(1).withTimeAtStartOfDay().minusSeconds(1).toDate();
		params.put("startTime", startT);
		params.put("endTime", endT);
		params.put("platform", platform);
		return proposalService.getCount(countSql, params);
	}

	public synchronized String addNewPtBatchXimaProposalXXX() {
		try {
			Integer count = queryAgprofit("newpt");
			if (count <= 0) {
				try {
					SimpleDateFormat dfHH = new SimpleDateFormat("HH");
					Date date = new Date();
					Integer hh = Integer.parseInt(dfHH.format(date));
					if (hh < 3) {
						setErrormsg("只能在3点之后才能提交");
						return SUCCESS;
					}
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat formatterTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar cals = Calendar.getInstance();
					cals.setTime(date);
					cals.add(Calendar.DAY_OF_MONTH, -1);
					String dateTime = formatter.format(cals.getTime());
					Date start = formatterTwo.parse(dateTime + " 00:00:00");
					Date end = formatterTwo.parse(dateTime + " 23:59:59");
					DetachedCriteria d = DetachedCriteria.forClass(PtDataNew.class);
					d.add(Restrictions.and(Restrictions.eq("starttime", start), Restrictions.eq("endtime", end)));
					List<PtDataNew> ptData = proposalService.findByCriteria(d);
					if (ptData == null || ptData.size() <= 0 || ptData.get(0) == null) {
						setErrormsg("获取数据失败");
						return SUCCESS;
					}
					// 批量处理ptdata数据
					boolean flag = true;
					List<PtDataNew> ptDataTemp = null;
					for (int i = 0; i < ptData.size(); i++) {
						log.info("\n已添加处理" + i + "条数据");
						if (null == ptDataTemp || ptDataTemp.size() == 0) {
							ptDataTemp = new ArrayList<PtDataNew>();
						}
						ptDataTemp.add(ptData.get(i));
						if (ptDataTemp.size() == 500 || i == ptData.size() - 1) {
							String msg = proposalService.autoAddXimaNewPtProposalNewXXX(ptDataTemp);
							if (StringUtils.isNotBlank(msg)) {
								flag = false;
							}
							ptDataTemp = null;
						}
					}

					if (flag) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: ");
						return SUCCESS;
					}
				} catch (Exception e) {
				}
			} else {
				setErrormsg("PT系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	public String autoXima4QT(){
		try {
			Integer count = queryAgprofit("qt");
			if (count <= 0) {
				try {
					SimpleDateFormat dfHH = new SimpleDateFormat("HH");
					Date date = new Date();
					Integer hh = Integer.parseInt(dfHH.format(date));
					if (hh < 3) {
						setErrormsg("只能在3点之后才能提交");
						return SUCCESS;
					}
					String msg = proposalService.xima4QT();
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
				}
			} else {
				setErrormsg("QT系统洗码已经处理！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}
	
	/**
	 * ttg系统洗码
	 * @return
	 */
	public String autoXima4TTG(){
		try {
			Integer count = queryAgprofit("ttg");
			if (count <= 0) {
				try {
					SimpleDateFormat dfHH = new SimpleDateFormat("HH");
					Date date = new Date();
					Integer hh = Integer.parseInt(dfHH.format(date));
					if (hh < 3) {
						setErrormsg("只能在3点之后才能提交");
						return SUCCESS;
					}
					String msg = proposalService.xima4TTG();
					if (msg == null) {
						setErrormsg("提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: " + msg);
						return SUCCESS;
					}
				} catch (Exception e) {
				}
			} else {
				setErrormsg("TTG系统洗码已经处理！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("系统异常:"+e1.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * SBA系统洗码
	 * */
	public synchronized String addSBABatchXimaProposal() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<13){
			GsonUtil.GsonObject("提示：请您每天的下午1点后提交") ;
			return SUCCESS;
		}
		
		try {
			Integer count = queryAgprofit("sba");
			if (count <= 0) {
				String querysql = "select PlayerName , sum(Stake),sum(WinLoseAmount)  from sba_data_dy where WinLostDateTime >=:startTime and WinLostDateTime <=:endTime group by PlayerName";
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("startTime", DateUtil.getchangedDate(-1)+" 00:00:00");
				param.put("endTime", DateUtil.getchangedDate(-1)+ " 23:59:59");
				List sbaData = proposalService.getListBySql(querysql, param);
				
				if (sbaData == null || sbaData.size() <= 0 || sbaData.get(0) == null) {
					setErrormsg("无洗码数据，请核对下再试！");
					return SUCCESS;
				}
				String msg = proposalService.autoAddXimaSbaProposal(sbaData);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} else {
				setErrormsg("sba系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}	
	
	/**
	 * 761系统洗码
	 * */
	public synchronized String add761BatchXimaProposal() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<1){
			GsonUtil.GsonObject("提示：请您每天的1点后提交") ;
			return SUCCESS;
		}
		
		try {
			Integer count = queryAgprofit("761");
			if (count <= 0) {
				String querysql = "select acc ,SUM(realput)bet,SUM(chg) win from chess_data where ctime>=:startTime and ctime <=:endTime group by acc ";
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("startTime", DateUtil.getchangedDate(-1)+" 00:00:00");
				param.put("endTime", DateUtil.getchangedDate(-1)+ " 23:59:59");
				List data = proposalService.getListBySql(querysql, param);
				
				if (data == null || data.size() <= 0 || data.get(0) == null) {
					setErrormsg("无洗码数据，请核对下再试！");
					return SUCCESS;
				}
				String msg = proposalService.autoAddXima761Proposal(data);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} else {
				setErrormsg("761系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}	
	
	/**
	 * swslot系统洗码
	 * */
	public synchronized String addSwSBatchXimaProposal() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<1){
			GsonUtil.GsonObject("提示：请您每天的1点后提交") ;
			return SUCCESS;
		}
		
		try {
			Integer count = queryAgprofit("sw");
			if (count <= 0) {
				String querysql = "select playerCode,SUM(bet),SUM(revenue) from sw_data where ts>=:startTime and ts <=:endTime  and gamecode not in ('w_fufish-jp', 'sw_fufish_intw') GROUP BY playerCode";
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("startTime", DateUtil.getchangedDate(-1)+" 00:00:00");
				param.put("endTime", DateUtil.getchangedDate(-1)+ " 23:59:59");
				List data = proposalService.getListBySql(querysql, param);
				
				if (data == null || data.size() <= 0 || data.get(0) == null) {
					setErrormsg("无洗码数据，请核对下再试！");
					return SUCCESS;
				}
				String msg = proposalService.autoAddXimaSwSProposal(data);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} else {
				setErrormsg("SW系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}	
	/**
	 * swfish系统洗码
	 * */
	public synchronized String addSwFBatchXimaProposal() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<1){
			GsonUtil.GsonObject("提示：请您每天的1点后提交") ;
			return SUCCESS;
		}
		
		try {
			Integer count = queryAgprofit("swfish");
			if (count <= 0) {
				String querysql = "select playerCode,SUM(bet),SUM(revenue) from sw_data where ts>=:startTime and ts <=:endTime  and gamecode in ('w_fufish-jp', 'sw_fufish_intw') GROUP BY playerCode ";
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("startTime", DateUtil.getchangedDate(-1)+" 00:00:00");
				param.put("endTime", DateUtil.getchangedDate(-1)+ " 23:59:59");
				List data = proposalService.getListBySql(querysql, param);
				
				if (data == null || data.size() <= 0 || data.get(0) == null) {
					setErrormsg("无洗码数据，请核对下再试！");
					return SUCCESS;
				}
				String msg = proposalService.autoAddXimaSwFProposal(data);
				if (msg == null) {
					setErrormsg("提交成功");
					return SUCCESS;
				} else {
					setErrormsg("提交失败: " + msg);
					return SUCCESS;
				}
			} else {
				setErrormsg("SWFISH系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}	
	
	/**
	 * MG 系统洗码
	 * @return
	 */
	public String addMGSBatchXimaProposal(){
		if (isTimeBeforeLimited(1)) {
			setErrormsg("只能在1点之后才能提交");
			return SUCCESS;
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Calendar currentDateTwo = new GregorianCalendar();
		currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
		currentDateTwo.set(Calendar.MINUTE, 59);
		currentDateTwo.set(Calendar.SECOND, 59);
		dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
		dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
		dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
		dc = dc.add(Restrictions.in("remark", new Object[] { "MG系统洗码", "MG系统洗码;执行:", "MG系统洗码;executed:" }));
		
		List list = proposalService.findByCriteria(dc);
		if (list.size() > 0) {
			setErrormsg("MG系统洗码已经导入，请勿重复操作");
			return SUCCESS;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append("	loginname, ");
		sb.append("	SUM(bet) bet, ");
		sb.append("	SUM(win) win ");
		sb.append("FROM ");
		sb.append("	( ");
		sb.append("		SELECT  mbrCode loginname, SUM(amnt) bet, 0 win ");
		sb.append("		FROM mg_data ");
		sb.append("		WHERE ");
		sb.append("			transType = 'bet' ");
		sb.append("		AND transTime >=:startTime ");
		sb.append("		AND transTime <=:endTime ");
		sb.append("		AND transId NOT IN ( ");
		sb.append("			SELECT ");
		sb.append("				refTransId ");
		sb.append("			FROM ");
		sb.append("				mg_data ");
		sb.append("			WHERE ");
		sb.append("				transType = 'refund' ");
		sb.append("		) ");
		sb.append("		GROUP BY mbrCode ");
		sb.append("		UNION ALL ");
		sb.append("			SELECT mbrCode loginname,0 bet, SUM(amnt) win ");
		sb.append("			FROM ");
		sb.append("				mg_data ");
		sb.append("			WHERE ");
		sb.append("				transType = 'win' ");
		sb.append("			AND transTime >=:startTime ");
		sb.append("			AND transTime <=:endTime ");
		sb.append("			AND transId NOT IN ( ");
		sb.append("				SELECT ");
		sb.append("					refTransId ");
		sb.append("				FROM ");
		sb.append("					mg_data ");
		sb.append("				WHERE ");
		sb.append("					transType = 'refund' ");
		sb.append("			) ");
		sb.append("			GROUP BY ");
		sb.append("				mbrCode ");
		sb.append("	) t ");
		sb.append("GROUP BY ");
		sb.append("	t.loginname  ");

		String sql = sb.toString();

		String startTime = DateUtil.formatDateForStandard(new DateTime().minusDays(1).withTimeAtStartOfDay().toDate());
		String endTime = DateUtil.formatDateForStandard(new DateTime().withTimeAtStartOfDay().minusSeconds(1).toDate());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		List betsList;
		try {
			betsList = proposalService.getListBySql(sql,params);
			String msg = proposalService.xima4MG(betsList);
			if (msg == null) {
				setErrormsg("提交成功");
			} else {
				setErrormsg("提交失败: " + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败: " + e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * PNG系统洗码（报表）
	 * */
	public synchronized String addPNGBatchXimaProposal() {
		
		try {
			SimpleDateFormat dfHH = new SimpleDateFormat("HH");
			SimpleDateFormat dfMM = new SimpleDateFormat("mm");
			Date date = new Date();
			Integer hh = Integer.parseInt(dfHH.format(date))*60;
			Integer mm = Integer.parseInt(dfMM.format(date));
			/*if((hh+mm)<(10*60+30)){
				setErrormsg("提交失败: 只能在10:30点之后才能提交");//暂定每天10点半后提交.
				return SUCCESS;
			}*/
			
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			Calendar currentDate = new GregorianCalendar();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			Calendar currentDateTwo = new GregorianCalendar();
			currentDateTwo.set(Calendar.HOUR_OF_DAY, 23);
			currentDateTwo.set(Calendar.MINUTE, 59);
			currentDateTwo.set(Calendar.SECOND, 59);
			dc = dc.add(Restrictions.ge("createTime", (Date) currentDate.getTime().clone()));
			dc = dc.add(Restrictions.le("createTime", (Date) currentDateTwo.getTime().clone()));
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.in("remark", new Object[] { "png系统洗码", "png系统洗码;执行:", "png系统洗码;executed:" }));
			
			List list = proposalService.findByCriteria(dc);
			if (list.size() <= 0) {
				if (null == fileName || fileName.equals("")) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				String filehouzhui = getExtention(fileName);

				if (!filehouzhui.equals(".xls")) {
					setErrormsg("文件格式必须是excel");
					return INPUT;
				}
				excelFileName = new Date().getTime() + getExtention(fileName);

				File uplodaFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));
				if(!uplodaFiles.exists()){
					uplodaFiles.mkdir();
				}
				File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);

				copy(myFile, file);
				
				
				batchGameinfoServiceImpl = (AbstractBatchGameinfoServiceImpl) SpringFactoryHepler.getInstance("addBatchXimaProposalService");
				try {
					List<XimaDataVo> dataList = this.batchGameinfoServiceImpl.excelToPNGVo(file);
					if(dataList == null || dataList.size() == 0){
						setErrormsg("提交失败，没有获取到有效数据！");
						return SUCCESS;
					}
					
					/*String ximalogStr=proposalService.insertSystemLogs(getOperatorLoginname(),SystemLogType.XIMAPNG);
					if (ximalogStr!=null&&ximalogStr!="") {
						setErrormsg("提交失败: "+ximalogStr);
						return SUCCESS;
					}*/
					
					Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-8));
					Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 8);
					String remark = "png系统洗码";
					String plantform = "png";
					ProposalType proposalType = ProposalType.PNGSELFXIMA;
					boolean flag = true;
					List<XimaDataVo> tempList = null;
					for (int i = 0; i < dataList.size(); i++) {
						log.info("\n已添加处理"+i+"条数据");
						if(null == tempList || tempList.size() == 0){
							tempList = new ArrayList<XimaDataVo>() ;
						}
						tempList.add(dataList.get(i)) ;
						if(tempList.size() == 500 || i == dataList.size()-1){
							String msg = proposalService.systemXima4Common(tempList, starttime, endtime, plantform, remark, proposalType);
							if(StringUtils.isNotBlank(msg)){
								flag = false ;
							}
							tempList = null ;
						}
					}
					
					if (flag) {
						setErrormsg("PNG洗码数据提交成功");
						return SUCCESS;
					} else {
						setErrormsg("提交失败: ");
						return SUCCESS;
					}
				} catch (Exception e) {
					e.printStackTrace();
					setErrormsg("提交失败: " + e);
				}
			} else {
				setErrormsg("png系统洗码已经导入！请不要再次操作！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			setErrormsg("获取当天时间查询问题!");
		}
		return SUCCESS;
	}

}
