// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SingleTest.java

package test;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Ignore;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.model.Commissions;
import dfh.model.Creditlogs;
import dfh.model.Prize;
import dfh.model.Proposal;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.UserRole;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.NetpayService;
import dfh.service.interfaces.NotifyService;
import dfh.service.interfaces.OperatorService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.SynRecordsService;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.StringUtil;

@Ignore
public class SingleTest {
	private static Logger log = Logger.getLogger(SingleTest.class);

	private static FileSystemXmlApplicationContext ctx;
	static ProposalService proposalService;
	static NetpayService netpayService;
	static SeqService seqService;
	static TransferService transferService;
	static SynRecordsService synRecordsService;
	static NotifyService notifyService;
	static CustomerService customerService;
	static TransferDao transferDao;
	static UserDao userDao;
	static TradeDao tradeDao;
	static OperatorService operatorService;

	public static FileSystemXmlApplicationContext getContext() {
		return ctx;
	}

	public static SynRecordsService getSynRecordsService() {
		return synRecordsService;
	}

	public static void initSpring() {
		ctx = new FileSystemXmlApplicationContext("D:/Workspace/MyEclipse/Ea_office/WebRoot/WEB-INF/applicationContext.xml");
		proposalService = (ProposalService) getContext().getBean("proposalService");
		tradeDao = (TradeDao) getContext().getBean("tradeDao");
		userDao = (UserDao) getContext().getBean("userDao");

	}

	public static Map<String, Double> readXimaExcel(String filePath) throws BiffException, IOException {
		Map<String, Double> map = new HashMap<String, Double>();

		Workbook wb = Workbook.getWorkbook(new File(filePath));
		Sheet s = wb.getSheet(0);// 第1个sheet
		int row = s.getRows();// 总行数
		int col = s.getColumns();// 总列数
		System.out.println("rows:" + row);
		for (int i = 0; i < row; i++) {
			String loginname = s.getCell(0, i).getContents();
			String totalBetAmount = s.getCell(1, i).getContents();
			String validBetAmount = s.getCell(2, i).getContents();
			System.out.println("loginname:" + loginname + " totalBetAmount:" + totalBetAmount + " validBetAmount:" + validBetAmount);
			if (StringUtil.isEmpty(loginname))
				continue;
			else {
				map.put(StringUtil.trim(loginname), Double.parseDouble(StringUtil.trim(validBetAmount).replaceAll(",", "")));
			}
		}
		System.out.println("map size:" + map.size());
		return map;
	}

	public static void main(String args[]) {
		initSpring();
		
		tradeDao.changeCredit("wxlpcz", 264.00, CreditChangeType.COMMISSION.getCode(), null, "EA佣金额度补加");
//		userDao.checkExsitForCreateUser("张朝立", "127.0.0.1", "370051291@qq.com");
//		System.out.println(userDao.getAgentByWebsite("http://888.e68ph.net").getLoginname());;
		
//		Proposal p= proposalService.getLastSuccCashout("ylpa988888", DateUtil.parseDateForStandard("2010-07-23 00:58:20"));
//		System.out.println(p.getPno());
//		Users user = (Users) proposalService.get(Users.class, "elftest01");
//		System.out.println("current credit:"+user.getCredit());
//		tradeDao.changeCredit(user, 100.0, CreditChangeType.CHANGE_MANUAL.getCode(), null, null);
//		System.out.println("current credit:"+user.getCredit());
		

		// DetachedCriteria dc = DetachedCriteria.forClass(Commissions.class);
		// if (StringUtils.isNotEmpty(agent))
		// dc = dc.add(Restrictions.eq("id.loginname", agent));
		// dc = dc.add(Restrictions.eq("id.year", 2010));
		// dc = dc.add(Restrictions.eq("id.month", 6));
		// System.out.println(proposalService.findByCriteria(dc).size());
		// Page page = PageQuery.queryForPagenationWithStatistics(proposalService.getHibernateTemplate(), dc, 1, 20, "amount", null);

	}

	public static void repairTransfer(long transferId) {
		Transfer transfer = (Transfer) proposalService.get(Transfer.class, transferId);
		if (transfer != null) {
			Users user = (Users) proposalService.get(Users.class, transfer.getLoginname());
			String reply = null;
			// String reply =
			// RemoteCaller.transferIn(transfer.getId().toString(),
			// transfer.getLoginname(), transfer.getCredit().longValue(),
			// user.getMd5str());
			if (reply == null) {
				transfer.setFlag(Constants.FLAG_TRUE);
				transfer.setRemark("转入成功[重新检查]");
				proposalService.update(transfer);
			}

		}
	}

	public static void setSynRecordsService(SynRecordsService synRecordsService) {
		synRecordsService = synRecordsService;
	}

	public static void testAddCashin() {
	}

	public static void testAddNewAccount() {
	}

	public static void testAddPayorder() {
		try {
			String msg = netpayService.addNetpayOrder(seqService.generateNetpayBillno(), "fmartin1", "123123", "yd", true, null, Double.valueOf(1000D), "martin", "130123123", "martin", "不知道",
					"127.0.0.1");
			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testAddTransfer() {
		// transferDao.addTransfer(2000003666, "ftest", 83.0, 83.0, true, true,
		// "测试");
	}

	// public static void testAggregateQuery(String loginname, Date start, Date
	// end) {
	// DetachedCriteria dcProposal =
	// DetachedCriteria.forClass(Proposal.class).add(Restrictions.between("createTime",
	// start, end)).add(Restrictions.eq("loginname", loginname)).add(
	// Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
	// dcProposal.setProjection(Projections.projectionList().add(Projections.groupProperty("type")).add(Projections.sum("amount")));
	// List resultProposal =
	// proposalService.getHibernateTemplate().findByCriteria(dcProposal);
	// System.out.println(resultProposal.size());
	// for (int j = 0; j < resultProposal.size(); j++) {
	// Object[] array = (Object[]) resultProposal.get(j);
	// for (int i = 0; i < array.length; i++) {
	// System.out.println(array[i]);
	// }
	// }
	//
	// DetachedCriteria dcPayOrder =
	// DetachedCriteria.forClass(PayOrder.class).add(Restrictions.between("createTime",
	// start, end)).add(Restrictions.eq("loginname", loginname)).add(
	// Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode()));
	// dcPayOrder.setProjection(Projections.sum("money"));
	// List resultPayOrder =
	// proposalService.getHibernateTemplate().findByCriteria(dcPayOrder);
	//
	// // 有效投注额
	// DetachedCriteria dcBetRecord =
	// DetachedCriteria.forClass(BetRecords.class).add(Restrictions.between("wagersDate",
	// start, end)).add(Restrictions.eq("userName", loginname)).add(
	// Restrictions.ne("payoff", new Double(0)));
	// dcBetRecord.setProjection(Projections.projectionList().add(Projections.sum("betAmount")).add(Projections.sum("payoff")));
	// List resultBetRecord =
	// proposalService.getHibernateTemplate().findByCriteria(dcBetRecord);
	// Object[] arrayBetRecord = (Object[]) resultBetRecord.get(0);
	// System.out.println(arrayBetRecord[0]);
	// System.out.println(arrayBetRecord[1]);
	//
	// dcBetRecord.add(Restrictions.eq("gameType",
	// GameType.LONGHUMEN.getCode())).add(Restrictions.sqlRestriction(" payoff=betAmount/2 "));
	// dcBetRecord.setProjection(Projections.projectionList().add(Projections.sum("betAmount")).add(Projections.sum("payoff")));
	// List resultBetRecord2 =
	// proposalService.getHibernateTemplate().findByCriteria(dcBetRecord);
	// Object[] arrayBetRecord2 = (Object[]) resultBetRecord2.get(0);
	// System.out.println(arrayBetRecord2[0]);
	// System.out.println(arrayBetRecord2[1]);
	// }

	public static void testAudit() {
		try {
			String msg = proposalService.audit("5020911040006", "admin", "127.0.0.1", null);
			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testCancle() {
		try {
			String msg = proposalService.cancle("5030911040003", "admin", "127.0.0.1", "测试撤销提案");
			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testExcute() {
		try {
			String msg = proposalService.excute("5010911100001", "admin", "127.0.0.1", null,"",0.0);
			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public static void testExcutePayorder() {
	// try {
	// String msg = netpayService.excuteNetpayOrder("09110500000021",
	// Double.valueOf(1000D), Boolean.valueOf(true), "成功", "我怎么知道");
	// System.out.println(msg);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static void testGenerateXima() {
		// System.out.println(operatorService.generateXima("fkingk",
		// DateUtil.now(), "admin"));
	}

	public static void testGetLastTransfer() {
		System.out.println(transferDao.getLastTransfer("ttest", Constants.IN).getId());
	}

	public static void testMatchStart() {
		DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.like("remark", "referenceNo:0", MatchMode.START));
		System.out.println(operatorService.findByCriteria(dc).size());
	}

	public static void testSendSms(String phoneNo, String msg) {
		System.out.println(notifyService.sendSms(phoneNo, msg));
	}

	public static void testSynBetRecord() {
		synRecordsService.synBetRecords();
	}

	public static void testTransfer() {
		String user = "ftest";
		// try {
		// for (int i = 0; i < 5; i++) {
		// transferService.transferOut(seqService.generateTransferID(),
		// user);
		// transferService.transferOut(seqService.generateTransferID(),
		// user);
		// Thread.sleep(2000);
		// transferService.transferIn(seqService.generateTransferID(),
		// user);
		// transferService.transferIn(seqService.generateTransferID(),
		// user);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	public static void testQueryUserAnalyze() {
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class).setProjection(
				Projections.projectionList().add(Projections.groupProperty("role")).add(Projections.groupProperty("level")).add(Projections.rowCount()));
		List list = operatorService.findByCriteria(dc);
		if (list != null) {
			Iterator<Object[]> it = list.listIterator();
			while (it.hasNext()) {
				Object[] obj = it.next();
				String role = obj[0].toString();
				String level = obj[1].toString();
				String rows = obj[2].toString();
				System.out.println(role + " " + level + " " + rows);
			}
		}
	}

	public static void testMe() {
		for (int i = 0; i < 10; i++) {
			System.out.println(seqService.generateNetpayBillno());
		}

	}

	// public static void addPrize(Date start, Date end) {
	// // 如果截止洗码时间在一小时内，先同步
	// try {
	// // 获取这段时间内所有的参与投注的会员
	// log.info("get the user who join in the bet");
	// DetachedCriteria dc = DetachedCriteria.forClass(BetRecords.class);
	// dc = dc.add(Restrictions.ge("wagersDate",
	// start)).add(Restrictions.lt("wagersDate",
	// end)).setProjection(Projections.groupProperty("userName"));
	// final List<String> userList = operatorService.findByCriteria(dc);
	// if (userList == null || userList.size() == 0) {
	// log.warn("no user to add prize");
	// return;
	// }
	//
	// // 开始自动化工作记录
	// log.info("start to add prize");
	// Iterator<String> it = userList.listIterator();
	// while (it.hasNext()) {
	// String loginname = it.next();
	// Double validBetAmount = userDao.getValidBetAmount(loginname, start, end);
	// operatorService.addPrize(start, end, loginname, validBetAmount);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static void excutePrize() {
		// 如果截止洗码时间在一小时内，先同步
		try {
			// 获取这段时间内所有的参与投注的会员
			log.info("find all the prize to excute");
			DetachedCriteria dc = DetachedCriteria.forClass(Prize.class);
			dc = dc.add(Restrictions.eq("flag", Constants.FLAG_FALSE)).setProjection(Projections.property("id"));
			List<Integer> prizeList = operatorService.findByCriteria(dc);
			if (prizeList == null || prizeList.size() == 0) {
				log.warn("no prize to excute");
				return;
			}

			// 开始自动化工作记录
			log.info("start to excute prize");
			Iterator<Integer> it = prizeList.listIterator();
			while (it.hasNext()) {
				Integer id = it.next();
				log.info("excute prize :" + id);
				// operatorService.excutePrize(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SingleTest() {
	}

	// public static void testSendSms(String phoneNo, String msg)
	// {
	// System.out.println(customerService.queryValidBetAmount("", start, end));
	// }
}
