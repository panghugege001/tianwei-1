package dfh.service.interfaces;

import dfh.model.ActivityCalendar;

import java.util.List;

public interface ActivityCalendarService {

    void addActivityCalendar(ActivityCalendar activityCalendar) throws Exception;

    void switchActivityCalendar(Integer id, String operator, Integer flag);

    List getAllIndexActivityCalendar(int pageNumber, int rowCount) throws Exception;

    void modifyActivityCalendar(ActivityCalendar activityCalendar) throws Exception;

    Object queryActivityCalendarById(Integer id);

    int getActivityCalendarCount() throws Exception;

}