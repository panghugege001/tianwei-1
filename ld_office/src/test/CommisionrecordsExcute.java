package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.model.Commissionrecords;
import dfh.model.CommissionrecordsId;
import dfh.model.Commissions;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.model.enums.CommisionType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.service.interfaces.AgentService;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.NetpayService;
import dfh.service.interfaces.NotifyService;
import dfh.service.interfaces.OperatorService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.TransferService;
import dfh.utils.DateUtil;
import dfh.utils.NumericUtil;
import dfh.utils.StringUtil;

//需要增加 代理用户名校对
public class CommisionrecordsExcute {
	private static Logger log = Logger.getLogger(CommisionrecordsExcute.class);

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
	static AgentService agentService;

	static String operator = "system";

	private static Map<String, String> changeAgentNameMap = new HashMap<String, String>();
	static {
		changeAgentNameMap.put("fanzhenqiang01", "fanzhenqia");
		changeAgentNameMap.put("daeiou14579", "daeiou1457");
		changeAgentNameMap.put("aaaaa115", "aaa1115");
	}

	public static FileSystemXmlApplicationContext getContext() {
		return ctx;
	}

	public static void initSpring() {

		ctx = new FileSystemXmlApplicationContext("D:/Workspace/MyEclipse/Ea_office/WebRoot/WEB-INF/applicationContext.xml");

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
		agentService = (AgentService) getContext().getBean("agentService");
	}

	public static void main(String args[]) {
		initSpring();

		try {
			Map<String, Double> cmMap = readCommisionRecordsExcel("d:/elf/9月ea平台佣金报表.xls");
			Map<String, Double> yhMap = new HashMap<String, Double>();

			// checkNames(cmMap);

			// 提交
			// autoAddCommisionRecords(2010, 9, cmMap, yhMap);
			// System.out.println("提交完毕");

//			 checkAtLast(2010, 9);
			// 执行
//			excuteCommisionRecords(2010, 8);
//			System.out.println("执行完毕");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取佣金记录，都已乘了30%
	 * 
	 * @param filePath
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public static Map<String, Double> readCommisionRecordsExcel(String filePath) throws BiffException, IOException {
		Map<String, Double> map = new HashMap<String, Double>();

		Workbook wb = Workbook.getWorkbook(new File(filePath));
		Sheet s = wb.getSheet(0);// 第1个sheet
		int row = s.getRows();// 总行数
		@SuppressWarnings("unused")
		int col = s.getColumns();// 总列数
		System.out.println("rows:" + row);
		// 从第二行开始
		for (int i = 1; i < row; i++) {
			String loginname = s.getCell(0, i).getContents();
			// System.out.println("head:" + StringUtil.trim(s.getCell(1, i).getContents()) + ";"
			// + StringUtil.trim(s.getCell(1, i).getContents()).replaceAll(",", ""));
			Double lose30Amount = Double.parseDouble(StringUtil.trim(s.getCell(1, i).getContents()).replaceAll(",", ""));
			System.out.println("loginname:" + loginname + " lose30Amount:" + lose30Amount);
			if (StringUtil.isEmpty(loginname))
				continue;
			else {
				if (changeAgentNameMap.containsKey(loginname))
					loginname = changeAgentNameMap.get(loginname);

				map.put(StringUtil.lowerCase(StringUtil.trim(loginname)), lose30Amount);
			}
		}
		System.out.println("map size:" + map.size());
		return map;
	}

	/**
	 * 读取旧平台的优惠记录
	 * 
	 * @param filePath
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public static Map<String, Double> readYhExcel(String filePath) throws Exception {
		Map<String, Double> map = new HashMap<String, Double>();

		Parser htmlParser = new Parser();

		htmlParser.setEncoding("GBK");
		htmlParser.setInputHTML(IOUtils.toString(new FileInputStream(filePath), "GBK"));
		NodeFilter filter = new NodeClassFilter(TableTag.class);
		NodeList nodeList = htmlParser.parse(filter);
		Node table = nodeList.elements().nextNode();
		NodeIterator trIt = table.getChildren().elements();
		while (trIt.hasMoreNodes()) {
			Node tr = trIt.nextNode();
			if (tr.getChildren() != null)
				System.out.println("tdList size:" + tr.getChildren().size());

			if (tr.getChildren() == null || tr.getChildren().size() != 11)
				continue;
			NodeList tdList = tr.getChildren();

			int i = 0;
			i++;
			String loginname = StringUtils.trim(tdList.elementAt(i).toPlainTextString()).replace("-", "");
			i++;
			i++;
			Double amount = Double.parseDouble(StringUtils.trim(tdList.elementAt(i).toPlainTextString()));
			i++;
			i++;
			i++;
			i++;
			String remark = StringUtils.trim(tdList.elementAt(i).toPlainTextString());
			i++;
			i++;
			i++;
			i++;

			System.out.println("[amount=" + amount + ", loginname=" + loginname + ", remark=" + remark + "]");
			if (StringUtils.contains(remark, "返水")) {
				String key = "洗码_" + loginname;
				map.put(key, amount);
			} else if (StringUtils.contains(remark, "首存")) {
				String key = "首存_" + loginname;
				map.put(key, amount);
			} else {
				String key = "其他_" + loginname;
				Double lastAmount = map.get(key);
				if (lastAmount != null)
					amount = amount + lastAmount;
				map.put(key, amount);
			}
		}
		return map;
	}

	public static void autoAddCommisionRecords(int year, int month, Map<String, Double> cmMap, Map<String, Double> yhMap) {
		Date startTime = DateUtil.getDate(year, month, 01, 12);
		Calendar temp = Calendar.getInstance();
		temp.setTime(startTime);
		temp.add(Calendar.MONTH, 1);
		Date endTime = temp.getTime();
		StringBuffer output = new StringBuffer();

		// 查询数据库的所有洗码
		List ximaList = operatorService.findByCriteria(DetachedCriteria.forClass(Proposal.class).add(
				Restrictions.eq("type", ProposalType.XIMA.getCode())).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode())).add(
				Restrictions.ge("createTime", startTime)).add(Restrictions.lt("createTime", endTime)).setProjection(
				Projections.projectionList().add(Projections.groupProperty("loginname")).add(Projections.sum("amount"))));
		Iterator ximaListIt = ximaList.listIterator();
		while (ximaListIt.hasNext()) {
			Object[] o = (Object[]) ximaListIt.next();
			String loginname = (String) o[0];
			Double amount = (Double) o[1];
			String key = "洗码_" + loginname;
			yhMap.put(key, toDouble(yhMap.get(key)) + toDouble(amount));
		}

		// 查询数据库的所有首存优惠
		List firstDepositList = operatorService.findByCriteria(DetachedCriteria.forClass(Proposal.class)//.add(Restrictions.eq("type", ProposalType.CONCESSIONS.getCode()))
				.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode())).add(Restrictions.ge("createTime", startTime)).add(
				Restrictions.lt("createTime", endTime)).setProjection(
				Projections.projectionList().add(Projections.groupProperty("loginname")).add(Projections.sum("amount"))));
		Iterator firstDepositListIt = firstDepositList.listIterator();
		while (firstDepositListIt.hasNext()) {
			Object[] o = (Object[]) firstDepositListIt.next();
			String loginname = (String) o[0];
			Double amount = (Double) o[1];
			String key = "首存_" + loginname;
			yhMap.put(key, toDouble(yhMap.get(key)) + toDouble(amount));
		}

		// 查询数据库的所有其他优惠
		List otherList = operatorService.findByCriteria(DetachedCriteria.forClass(Proposal.class).add(
				Restrictions.in("type", new Integer[] { ProposalType.PRIZE.getCode(), //ProposalType.BANKTRANSFERCONS.getCode(),
						ProposalType.OFFER.getCode() })).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode())).add(
				Restrictions.ge("createTime", startTime)).add(Restrictions.lt("createTime", endTime)).setProjection(
				Projections.projectionList().add(Projections.groupProperty("loginname")).add(Projections.sum("amount"))));
		Iterator otherListIt = otherList.listIterator();
		while (otherListIt.hasNext()) {
			Object[] o = (Object[]) otherListIt.next();
			String loginname = (String) o[0];
			Double amount = (Double) o[1];
			String key = "其他_" + loginname;
			yhMap.put(key, toDouble(yhMap.get(key)) + toDouble(amount));
		}

		// 查找所有的代理
		List userList = operatorService.findByCriteria(DetachedCriteria.forClass(Users.class).setProjection(
				Projections.projectionList().add(Projections.property("agent")).add(Projections.property("loginname")).add(
						Projections.property("role"))));
		Map<String, List<String>> userMap = new HashMap<String, List<String>>();
		Iterator userListit = userList.listIterator();
		while (userListit.hasNext()) {
			Object[] o = (Object[]) userListit.next();
			String agent = (String) o[0];
			agent = StringUtils.lowerCase(agent);
			if (StringUtils.isEmpty(agent))
				continue;
			String loginname = (String) o[1];
			List<String> users = userMap.get(agent);
			if (users == null) {
				users = new ArrayList<String>();
			}
			String role = (String) o[2];
			if (StringUtils.equals(role, UserRole.MONEY_CUSTOMER.getCode()))
				users.add(loginname);

			userMap.put(agent, users);
		}

		// to delete
		int total = userMap.size();
		Iterator<String> agentsIt = userMap.keySet().iterator();
		int i = 0;
		while (agentsIt.hasNext()) {
			String agent = (String) agentsIt.next();
			double eaProfitAmount, sumTotalReduceAmount = 0.0;
			eaProfitAmount = toDouble(cmMap.get(StringUtil.lowerCase(agent).replaceFirst("a_", "")));

			List<String> usersList = userMap.get(agent);
			// 获取所有下级会员
			Iterator<String> usersIt = usersList.listIterator();

			int subCount = 0;
			while (usersIt.hasNext()) {
				subCount++;
				String username = (String) usersIt.next();
				// 计算每个会员优惠，输赢值
				Double cashinAmount = 0.0, cashoutAmount = 0.0, ximaAmount = 0.0, otherAmount = 0.0, totalReduceAmount = 0.0, firstDepositAmount = 0.0;

				ximaAmount = toDouble(yhMap.get("洗码_" + username));
				firstDepositAmount = toDouble(yhMap.get("首存_" + username));
				otherAmount = toDouble(yhMap.get("其他_" + username));

				sumTotalReduceAmount += firstDepositAmount * 0.3 + ximaAmount + otherAmount;

				Commissionrecords c = new Commissionrecords(new CommissionrecordsId(username, year, month), agent, cashinAmount,
						cashoutAmount, firstDepositAmount, ximaAmount, otherAmount);
				output
						.append("INSERT INTO elf.commissionrecords(`loginname`, `parent`,`cashinAmount`, `cashoutAmount`,`firstDepositAmount`, `ximaAmount`, `otherAmount`, `year`, `month`)VALUES('"
								+ username
								+ "', '"
								+ agent
								+ "', 0.00, 0.00, "
								+ NumericUtil.formatDouble(firstDepositAmount)
								+ ", "
								+ NumericUtil.formatDouble(ximaAmount)
								+ ", "
								+ NumericUtil.formatDouble(otherAmount)
								+ ", "
								+ year
								+ ", "
								+ month + ");\n");
				// operatorService.save(c);
			}
			// if (StringUtil.equalsIgnoreCase(agent, "BCL888")) {
			// System.out.println(agent + ";sumTotalReduceAmount:" + sumTotalReduceAmount + ":" + usersList.toString());
			// }
			i++;
			if (StringUtil.equals("a_haiyan", agent)) {
				// 代理 的 haiyan 要单独 计算一下
				// [11:56:24] richard8699: 优惠其他的客人是100% 他就承担30%
				sumTotalReduceAmount = 0.3 * sumTotalReduceAmount;
				output.append("INSERT INTO elf.commissions VALUES('" + agent + "'," + year + "," + month + ","
						+ CommisionType.INIT.getCode() + "," + subCount + "," + eaProfitAmount + ","
						+ (eaProfitAmount - sumTotalReduceAmount) + ", '" + DateUtil.formatDateForStandard(DateUtil.now())
						+ "', null, null);\n");
			} else
				output.append("INSERT INTO elf.commissions VALUES('" + agent + "'," + year + "," + month + ","
						+ CommisionType.INIT.getCode() + "," + subCount + "," + eaProfitAmount + ","
						+ (eaProfitAmount - sumTotalReduceAmount) + ",  '" + DateUtil.formatDateForStandard(DateUtil.now())
						+ "', null, null);\n");
			System.err.println("完成" + i + "/" + total);
		}
		try {
			IOUtils.write(output.toString(), new FileOutputStream("d:/commissionrecords.sql"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static Double toDouble(Double num) {
		if (num == null)
			return 0.0;
		return num;
	}

	public static void check(int year, int month, Map<String, Double> cmMap) {
		DetachedCriteria dc = DetachedCriteria.forClass(Commissions.class).add(Restrictions.eq("id.year", year)).add(
				Restrictions.eq("id.month", month)).add(Restrictions.eq("flag", CommisionType.EXCUTED.getCode()));
		List<Commissions> list = agentService.findByCriteria(dc);
		int count = list.size();
		Iterator<Commissions> it = list.listIterator();
		while (it.hasNext()) {
			Commissions commission = (Commissions) it.next();
			Double eaProfitAmount = cmMap.get(StringUtil.lowerCase(commission.getId().getLoginname()).replaceFirst("a_", ""));
			if (eaProfitAmount == null)
				continue;
			if (eaProfitAmount.doubleValue() != commission.getEaProfitAmount().doubleValue()) {
				System.err.println(commission.getId().getLoginname() + ";EXCEL " + eaProfitAmount + ";DB "
						+ commission.getAmount().doubleValue());
				commission.setEaProfitAmount(eaProfitAmount);
				commission.setAmount(commission.getAmount() + eaProfitAmount);
				operatorService.update(commission);
				operatorService.changeCreditManual(commission.getId().getLoginname(), eaProfitAmount, "EA报表额度未加上，补加额度", "admin");
			}
		}
	}

	public static void checkCommissions(int year, int month) {
		Map<String, Commissions> cmMap = new HashMap<String, Commissions>();
		Map<String, List<Commissionrecords>> crMap = new HashMap<String, List<Commissionrecords>>();
		DetachedCriteria dc = DetachedCriteria.forClass(Commissions.class).add(Restrictions.eq("id.year", year)).add(
				Restrictions.eq("id.month", month));
		Iterator<Commissions> it = agentService.findByCriteria(dc).listIterator();
		while (it.hasNext()) {
			Commissions cm = (Commissions) it.next();
			cmMap.put(StringUtils.lowerCase(cm.getId().getLoginname()), cm);
		}

		DetachedCriteria dc2 = DetachedCriteria.forClass(Commissionrecords.class).add(Restrictions.eq("id.year", year)).add(
				Restrictions.eq("id.month", month));
		Iterator<Commissionrecords> it2 = agentService.findByCriteria(dc2).listIterator();
		while (it2.hasNext()) {
			Commissionrecords cr = (Commissionrecords) it2.next();
			String agent = StringUtils.lowerCase(cr.getParent());
			if (crMap.get(agent) == null) {
				List<Commissionrecords> list = new ArrayList<Commissionrecords>();
				list.add(cr);
				crMap.put(agent, list);
			} else {
				List<Commissionrecords> list = crMap.get(agent);
				list.add(cr);
				crMap.put(agent, list);
			}
		}

		System.out.println(cmMap.size() + ";" + crMap.size());
		Iterator<String> agentIt = cmMap.keySet().iterator();
		while (agentIt.hasNext()) {
			String agent = agentIt.next();
			Commissions cm = cmMap.get(agent);
			List<Commissionrecords> list = crMap.get(agent);

			if (cm.getSubCount().intValue() != list.size()) {
				// System.err.println(agent);
			}

			Iterator<Commissionrecords> crIt = list.listIterator();
			Double ximaAmount = 0.0, firstDepositAmount = 0.0, otherAmount = 0.0;
			while (crIt.hasNext()) {
				Commissionrecords cr = (Commissionrecords) crIt.next();
				ximaAmount += cr.getXimaAmount();
				firstDepositAmount += cr.getFirstDepositAmount();
				otherAmount += cr.getOtherAmount();
			}
			Double amount = cm.getEaProfitAmount() - firstDepositAmount * 0.3 - ximaAmount - otherAmount;

			if (cm.getAmount().intValue() != amount.intValue()) {
				System.err.println(agent + ";" + amount + ";" + list.size());
			}
		}

	}

	public static void excuteCommisionRecords(int year, int month) {
		DetachedCriteria dc = DetachedCriteria.forClass(Commissions.class).add(Restrictions.eq("id.year", year)).add(
				Restrictions.eq("id.month", month)).add(Restrictions.eq("flag", CommisionType.INIT.getCode()));
		List<Commissions> list = agentService.findByCriteria(dc);
		int count = list.size();
		Iterator<Commissions> it = list.listIterator();
		int i = 0;
		while (it.hasNext()) {
			Commissions commission = (Commissions) it.next();
			agentService.excuteCommisionRecords(commission.getId().getLoginname(), commission.getId().getYear(), commission.getId()
					.getMonth());
			i++;
			System.err.println("完成 " + i + "/" + count);
		}
	}

	public static void checkNames(Map<String, Double> cmMap) {
		Iterator<String> agentIt = cmMap.keySet().iterator();
		List<String> list = new ArrayList<String>();
		while (agentIt.hasNext()) {
			String agent = (String) agentIt.next();
			if (agentService.get(Users.class, agent) == null) {
				if (agentService.get(Users.class, "a_" + agent) == null) {
					list.add(agent);
					System.err.println(agent);
				}
			}

		}
		System.err.println(Arrays.toString(list.toArray()));
	}

	public static void checkAtLast(int year, int month) {
		Map<String, Double> cmrMap = new HashMap<String, Double>();
		DetachedCriteria dcCmm = DetachedCriteria.forClass(Commissions.class).add(Restrictions.eq("id.year", year)).add(
				Restrictions.eq("id.month", month));
		DetachedCriteria dcCmr = DetachedCriteria.forClass(Commissionrecords.class).add(Restrictions.eq("id.year", year)).add(
				Restrictions.eq("id.month", month));
		List<Commissions> listCmm = agentService.findByCriteria(dcCmm);
		List<Commissionrecords> listCmr = agentService.findByCriteria(dcCmr);
		Iterator<Commissionrecords> it = listCmr.listIterator();
		while (it.hasNext()) {
			Commissionrecords cmr = (Commissionrecords) it.next();
			Double amount = cmrMap.get(cmr.getParent());
			if (amount == null)
				cmrMap.put(cmr.getParent(), cmr.getFirstDepositAmount() * 0.3 + cmr.getXimaAmount() + cmr.getOtherAmount());
			else
				cmrMap.put(cmr.getParent(), cmr.getFirstDepositAmount() * 0.3 + cmr.getXimaAmount() + cmr.getOtherAmount() + amount);
		}

		Iterator<Commissions> it2 = listCmm.listIterator();
		while (it2.hasNext()) {
			Commissions cmm = (Commissions) it2.next();
			Double cmrTotalAmount = cmrMap.get(cmm.getId().getLoginname());
			cmrTotalAmount = cmrTotalAmount == null ? 0.0 : cmrTotalAmount;
			if (cmm.getId().getLoginname().equals("a_haiyan")) {
				cmrTotalAmount = cmrTotalAmount * 0.3;
				// System.out.println(cmm.getId().getLoginname()+";"+cmm.getAmount().intValue()+";"+(cmm.getEaProfitAmount()+";"+cmrTotalAmount));
			}
			if (cmm.getAmount().intValue() != (int) (cmm.getEaProfitAmount() - cmrTotalAmount)) {
				System.err.println(cmm.getId().getLoginname() + ";" + cmm.getAmount() + ";" + (cmm.getEaProfitAmount() - cmrTotalAmount));
			}
			if (cmm.getEaProfitAmount().intValue() == 0 && cmrTotalAmount.intValue() != 0) {
				System.err.println(cmm.getId().getLoginname());
			}

		}
	}
}
