package dfh.service.implementations;

import dfh.action.vo.ActivityCalendarVO;
import dfh.dao.ActivityCalendarDao;
import dfh.model.ActivityCalendar;
import dfh.service.interfaces.ActivityCalendarService;
import dfh.utils.DateUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wander on 2016/12/20.
 */
public class ActivityCalendarServiceImpl implements ActivityCalendarService {

    private ActivityCalendarDao activityCalendarDao;

    @Override
    public List<ActivityCalendarVO> queryTopActivity(Integer top) {

        Iterator it = activityCalendarDao.queryTopActivity(top).iterator();
        List<ActivityCalendarVO> list = new ArrayList<ActivityCalendarVO>();
        while (it.hasNext()) {
            ActivityCalendar vo = (ActivityCalendar) it.next();
            String activityDate = vo.getActivityDate() == null ? "长期有效" : DateUtil.fmtyyyy_MM_d(vo.getActivityDate());
            list.add(new ActivityCalendarVO(vo.getId(), vo.getName(), vo.getContent(), activityDate,
                    DateUtil.fmtDateForBetRecods(vo.getCreatetime()), vo.getCreator(), vo.getFlag()));
        }
        return list;
    }

    public void setActivityCalendarDao(ActivityCalendarDao activityCalendarDao) {
        this.activityCalendarDao = activityCalendarDao;
    }
}
