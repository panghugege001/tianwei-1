package dfh.service.interfaces;

import dfh.action.vo.ActivityCalendarVO;

import java.util.List;

/**
 * Created by wander on 2016/12/20.
 */
public interface ActivityCalendarService {

    /**
     * 活动日历
     * @return list
     */
    List<ActivityCalendarVO> queryTopActivity(Integer top);

}
