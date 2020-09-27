package dfh.service.implementations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import dfh.dao.LogDao;
import dfh.service.interfaces.SynRecordsService;

public class SynRecordsServiceImpl extends UniversalServiceImpl implements SynRecordsService {

	private static Log log = LogFactory.getLog(SynRecordsServiceImpl.class);
	private LogDao logDao;
	private Integer synPeriod;

	public SynRecordsServiceImpl() {
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public Integer getSynPeriod() {
		return synPeriod;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public void setSynPeriod(Integer synPeriod) {
		this.synPeriod = synPeriod;
	}

	// update sun
	public void synBetRecords() {
/*
		log.info("start to syn bet records");
		Date start = null;
		DetachedCriteria dc = DetachedCriteria.forClass(BetRecords.class).setProjection(Projections.max("billTime"));
		List list = getHibernateTemplate().findByCriteria(dc);
		System.out.println(list.toString());
		if (list != null && list.size() > 0)
			start = (Date) list.get(0);
		if (start == null)
			start = new Date(0L); //
		List records = RemoteCaller.getBetRecord(DateUtil.getMontSecond(start, 1), DateUtil.get15MinutesAgo(), null, true);
		if (records == null || records.size() == 0) {
			log.info("no bet records to syn");
			return;
		} else {
			log.info("got " + records.size() + " 条记录");
			final Iterator it = records.listIterator();
			Object result = getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					int rows = 0;
					log.info("start to save the bet records");
					session.connection().setAutoCommit(false);
					try {
						while (it.hasNext()) {
							BetRecords record = (BetRecords) it.next();
							session.save(record);
							rows++;
							if (rows % 500 == 0)
								session.flush();
						}
					} catch (Exception e) {
						session.flush();
						session.clear();
						return rows - rows % 500;
					}
					session.flush();
					session.clear();
					return rows;
				}
			});
			String remark = "以" + DateUtil.formatDateForStandard(start) + "为起点,共获取" + records.size() + "条记录,同步" + result + "条记录";
			logDao.insertOperationLog("system", OperationLogType.SYN_BET_RECORDS, remark);
			log.info("finish syn the bet records");
			return;
		}
		*/

	}

}
