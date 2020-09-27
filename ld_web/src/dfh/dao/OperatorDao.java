package dfh.dao;

import org.apache.log4j.Logger;

import dfh.model.Operator;

// Referenced classes of package dfh.dao:
//			UniversalDao

public class OperatorDao extends UniversalDao {

	private static Logger log = Logger.getLogger(OperatorDao.class);

	public OperatorDao() {
	}

	public String checkOperator(Operator operator) {
		String msg = null;
		try {
			if (operator == null) {
				msg = "该操作员不存在";
				log.warn(msg);
			} else if (operator.getEnabled().equals("false")) {
				msg = "该操作员已经被禁用";
				log.warn(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return msg;
	}

}
