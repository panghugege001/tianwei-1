package dfh.service.implementations;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.dao.TradeDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Commissionrecords;
import dfh.model.Commissions;
import dfh.model.CommissionsId;
import dfh.model.Users;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.CommisionType;
import dfh.model.enums.CreditChangeType;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.AgentService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class AgentServiceImpl extends CustomerServiceImpl implements AgentService {

	private static Logger log = Logger.getLogger(AgentServiceImpl.class);

	public void addCommisionRecords(String loginname) {

	}

	public void excuteCommisionRecords(String loginname, Integer year, Integer month) {
		Commissions commission = (Commissions) get(Commissions.class, new CommissionsId(loginname, year, month));
		if (commission.getFlag().intValue() == CommisionType.INIT.getCode()) {
			getTradeDao().changeCredit(commission.getId().getLoginname(), commission.getAmount(), CreditChangeType.COMMISSION.getCode(), null, "代理佣金结算");
			commission.setFlag(CommisionType.EXCUTED.getCode());
			save(commission);
			log.info("已结算" + commission.getId().toString());
		} else {
			log.warn(commission.getId().toString() + "状态已被结算，跳过记录");
		}
	}

}
