package dfh.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Task;
import dfh.model.enums.LevelType;
import dfh.model.enums.TaskFlagType;
import dfh.utils.DateUtil;

public class TaskDao extends UniversalDao {

	private static Logger log = Logger.getLogger(TaskDao.class);

	public TaskDao() {
	}

	public void auditTask(String pno, String operator, String ip) {
		log.info((new StringBuilder("audit tasks for proposal:")).append(pno).toString());
		Task audit = getTask(pno, LevelType.AUDIT);
		if (audit == null) {
			throw new GenericDfhRuntimeException("找不到对应提案任务");
		} else {
			audit.setAgreeIp(ip);
			audit.setManager(operator);
			audit.setFlag(TaskFlagType.FINISHED.getCode());
			audit.setAgreeTime(DateUtil.now());
			update(audit);
			return;
		}
	}

	public void cancleTask(String pno, String operator, String ip) {
		log.info((new StringBuilder("cancle tasks for proposal:")).append(pno).toString());
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Task.class).add(Restrictions.eq("pno", pno));
		List list = findByCriteria(dCriteria);
		if (list.size() == 0)
			throw new GenericDfhRuntimeException("找不到对应提案任务");
		Task task;
		for (Iterator it = list.listIterator(); it.hasNext(); update(task)) {
			task = (Task) it.next();
			task.setAgreeIp(ip);
			task.setManager(operator);
			task.setFlag(TaskFlagType.CANCLED.getCode());
			task.setAgreeTime(DateUtil.now());
		}
	}

	public void excuteTask(String pno, String operator, String ip) {
		log.info((new StringBuilder("exucte tasks for proposal:")).append(pno).toString());
		Task excute = getTask(pno, LevelType.EXCUTE);
		if (excute == null) {
			throw new GenericDfhRuntimeException("找不到对应提案任务");
		} else {
			excute.setAgreeIp(ip);
			excute.setManager(operator);
			excute.setFlag(TaskFlagType.FINISHED.getCode());
			excute.setAgreeTime(DateUtil.now());
			update(excute);
			return;
		}
	}

	public void generateTasks(String pno, String operator) {
		log.info((new StringBuilder("generate tasks for proposal:")).append(pno).toString());
		Task audit = new Task(pno, LevelType.AUDIT.getCode(), TaskFlagType.SUBMITED.getCode(), DateUtil.now(), null, operator);
		save(audit);
		Task excute = new Task(pno, LevelType.EXCUTE.getCode(), TaskFlagType.SUBMITED.getCode(), DateUtil.now(), null, operator);
		save(excute);
	}

	public Task getTask(String pno, LevelType type) {
		if (pno == null || type == null)
			return null;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Task.class);
		dCriteria = dCriteria.add(Restrictions.eq("pno", pno)).add(Restrictions.eq("level", type.getCode()));
		List list = findByCriteria(dCriteria);
		if (list.size() == 0)
			return null;
		else
			return (Task) list.get(0);
	}

}
