package dfh.service.implementations;

import dfh.action.vo.ActivityCalendarVO;
import dfh.dao.ActivityCalendarDao;
import dfh.dao.LogDao;
import dfh.model.ActivityCalendar;
import dfh.model.enums.OperationLogType;
import dfh.service.interfaces.ActivityCalendarService;
import dfh.utils.DateUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActivityCalendarServiceImpl implements ActivityCalendarService {

    private LogDao logDao;
    private ActivityCalendarDao activityCalendarDao;

    private Logger log = Logger.getLogger(ActivityCalendarServiceImpl.class);

    public ActivityCalendarServiceImpl() {
    }

    public void addActivityCalendar(ActivityCalendar activityCalendar) throws Exception {
        activityCalendarDao.save(activityCalendar);
        logDao.insertOperationLog(activityCalendar.getCreator(), OperationLogType.ADD_ACT_CALENDAR, null);
    }
    
    public List getAllIndexActivityCalendar(int pageNumber, int rowCount) throws Exception {
        int offset = (pageNumber - 1) * rowCount;
        try {
            List list = activityCalendarDao.findAll(offset, rowCount);
            Iterator it = list.iterator();
            List recvList = new ArrayList();
            while (it.hasNext()) {
                // 给过长标题和内容减少适当后续文字。
                ActivityCalendar ann = (ActivityCalendar) it.next();
                String activityDate = ann.getActivityDate() == null ? "长期有效" : DateUtil.fmtYYYY_MM_DD(ann.getActivityDate());
                recvList.add(new ActivityCalendarVO(
                        ann.getId(), ann.getName(), ann.getContent(), activityDate,
                        DateUtil.fmtDateForBetRecods(ann.getCreatetime()), ann.getCreator(), ann.getFlag(),ann.getOrderBy()));
            }
            return recvList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public void modifyActivityCalendar(ActivityCalendar vo) {
        Object o = activityCalendarDao.get(ActivityCalendar.class, vo.getId());
        if (o != null) {
            ActivityCalendar activityCalendar = (ActivityCalendar) o;
            activityCalendar.setName(vo.getName());
            activityCalendar.setContent(vo.getContent());
            activityCalendar.setActivityDate(vo.getActivityDate());
            activityCalendar.setCreator(vo.getCreator());
            activityCalendar.setFlag(vo.getFlag());

            activityCalendarDao.saveOrUpdate(activityCalendar);
            logDao.insertOperationLog(vo.getCreator(), OperationLogType.MODIFY_ACT_CALENDAR, null);
        }
    }

    public void switchActivityCalendar(Integer id, String operator, Integer flag) {
        Object o = activityCalendarDao.get(ActivityCalendar.class, id);
        if (o != null) {
            ActivityCalendar activityCalendar = (ActivityCalendar) o;
            activityCalendar.setFlag(flag);
            activityCalendarDao.saveOrUpdate(activityCalendar);
            logDao.insertOperationLog(operator, OperationLogType.MODIFY_ACT_CALENDAR, null);
        }
    }

    public Object queryActivityCalendarById(Integer id) {
        return activityCalendarDao.get(ActivityCalendar.class, id);
    }

    public int getActivityCalendarCount() throws Exception {
        try {
            return activityCalendarDao.getCount();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public LogDao getLogDao() {
        return logDao;
    }

    public void setLogDao(LogDao logDao) {
        this.logDao = logDao;
    }

    public ActivityCalendarDao getActivityCalendarDao() {
        return activityCalendarDao;
    }

    public void setActivityCalendarDao(ActivityCalendarDao activityCalendarDao) {
        this.activityCalendarDao = activityCalendarDao;
    }
}
