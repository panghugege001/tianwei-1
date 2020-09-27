package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Ignore;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.model.Creditlogs;
import dfh.model.Prize;
import dfh.model.Proposal;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.Xima;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.IGameinfoService;
import dfh.service.interfaces.NetpayService;
import dfh.service.interfaces.NotifyService;
import dfh.service.interfaces.OperatorService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.SynRecordsService;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.SpringFactoryHepler;
import dfh.utils.StringUtil;

@Ignore
public class XimaExcute {
	private static Logger log = Logger.getLogger(XimaExcute.class);

	private static FileSystemXmlApplicationContext ctx;
	static ProposalService proposalService;
	static NetpayService netpayService;
	static SeqService seqService;
	static TransferService transferService;
	static NotifyService notifyService;
	static CustomerService customerService;
	static TransferDao transferDao;
	static UserDao userDao;
	static TradeDao tradeDao;
	static OperatorService operatorService;
	static SeqDao seqDao;
	static TaskDao taskDao;

	static String operator = "system";

	public static FileSystemXmlApplicationContext getContext() {
		return ctx;
	}

	public static void initSpring() {

		ctx = new FileSystemXmlApplicationContext("D:\\Workspaces\\MyEclipse 8.x\\e68\\application\\Ea_office\\WebRoot\\WEB-INF\\applicationContext.xml");

		proposalService = (ProposalService) getContext().getBean("proposalService");
		netpayService = (NetpayService) getContext().getBean("netpayService");
		seqService = (SeqService) getContext().getBean("seqService");
		transferService = (TransferService) getContext().getBean("transferService");
		getContext().getBean("transferService");
		notifyService = (NotifyService) getContext().getBean("notifyService");
		customerService = (CustomerService) getContext().getBean("customerService");
		transferDao = (TransferDao) getContext().getBean("transferDao");
		userDao = (UserDao) getContext().getBean("userDao");
		operatorService = (OperatorService) getContext().getBean("operatorService");
		tradeDao = (TradeDao) getContext().getBean("tradeDao");
		seqDao = (SeqDao) getContext().getBean("seqDao");
		taskDao = (TaskDao) getContext().getBean("taskDao");
	}
	
	
	

	public static Map<String, Double> readXimaExcel(String filePath) throws BiffException, IOException {
		Map<String, Double> map = new HashMap<String, Double>();

		Workbook wb = Workbook.getWorkbook(new File(filePath));
		Sheet s = wb.getSheet(0);// 第1个sheet
		int row = s.getRows();// 总行数
		int col = s.getColumns();// 总列数
		System.out.println("rows:" + row);
		// 从第二行开始
		for (int i = 1; i < row; i++) {
			String loginname = s.getCell(0, i).getContents();
			String totalBetAmount = s.getCell(1, i).getContents();
			String validBetAmount = s.getCell(2, i).getContents();
			loginname = loginname.replaceFirst("ELF01_", "");
			System.out.print(i+":");
			System.out.println("loginname:" + loginname + " totalBetAmount:" + totalBetAmount + " validBetAmount:" + validBetAmount);
			if (StringUtil.isEmpty(loginname))
				continue;
			else {
				if (map.containsKey(StringUtil.trim(loginname))) {
					System.out.println(i+"================"+StringUtil.trim(loginname));
				}
				map.put(StringUtil.trim(loginname), Double.parseDouble(StringUtil.trim(validBetAmount).replaceAll(",", "")));
			}
		}
		return map;
	}

	public static void main(String args[]) {
		initSpring();

		Date startTime = DateUtil.getDate(2010, 11 , 15, 12);
		Date endTime = DateUtil.getDate(2010, 11, 22, 12);
		System.out.println(startTime.toLocaleString());
		System.out.println(endTime.toLocaleString());

		try {
			Map<String, Double> map = readXimaExcel("D:\\2010.11.15~11.22投注返水技术版.xls");

			// 提交
//			 autoAddXimaProposal(map, startTime, endTime);
//			 System.out.println("提交完毕");

			// 执行
//			excuteAutoXimaProposal();
//			System.out.println("执行完毕");
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void autoAddXimaProposal(Map<String, Double> map, Date startTime, Date endTime) {
		Iterator<String> it = map.keySet().iterator();

		List list = proposalService.findByCriteria(DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("type", ProposalType.XIMA.getCode())).add(
				Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())).add(Restrictions.eq("generateType", Constants.GENERATE_AUTO)));
		List loginnameList = new ArrayList();
		Iterator<Proposal> listIt = list.listIterator();
		while (listIt.hasNext()) {
			Proposal p = (Proposal) listIt.next();
			loginnameList.add(p.getLoginname());
		}
		System.out.println("result size:" + loginnameList.size());

		while (it.hasNext()) {
			String loginname = (String) it.next();
			if (loginnameList.contains(loginname)) {
				System.err.println(loginname + "已提交");
				continue;
			}
			Users user = (Users) proposalService.get(Users.class, loginname);
			if (user == null) {
				System.err.println("用户不存在:" + loginname);
				break;
			}
			Double validBetAmount = map.get(loginname);
			Double rate = userDao.getXimaRate(validBetAmount);
			log.info("会员:" + loginname + ";有效投注额:" + validBetAmount + ";洗码率:" + rate);
			String pno = seqDao.generateProposalPno(ProposalType.XIMA);
			Double tryCredit = Double.valueOf(Math.abs(validBetAmount) * Math.abs(rate));
			if (tryCredit > 50000.0)
				tryCredit = 50000.0;
			String remark = "自动结算洗码";
			Xima xima = new Xima(pno, user.getRole(), loginname, "网银支付", validBetAmount, tryCredit, DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), rate, remark);
			Proposal proposal = new Proposal(pno, operator, DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, tryCredit,user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			taskDao.generateTasks(pno, operator);
			proposalService.save(xima);
			proposalService.save(proposal);

		}
	}

	public static void excuteAutoXimaProposal() {
		List list = proposalService.findByCriteria(DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("type", ProposalType.XIMA.getCode())).add(
				Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())).add(Restrictions.eq("generateType", Constants.GENERATE_AUTO)));
		System.out.println("result size:" + list.size());
		Iterator<Proposal> it = list.listIterator();
		while (it.hasNext()) {
			Proposal p = (Proposal) it.next();
			proposalService.excute(p.getPno(), operator, "112.213.99.193", "ok","",0.0);
		}
	}

}
