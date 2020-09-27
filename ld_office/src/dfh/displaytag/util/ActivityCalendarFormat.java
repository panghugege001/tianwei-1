package dfh.displaytag.util;

import dfh.action.vo.ActivityCalendarVO;
import org.displaytag.decorator.TableDecorator;

/**
 * Created by wander on 2016/12/19.
 */
public class ActivityCalendarFormat extends TableDecorator {

    public String getFlag() {
        ActivityCalendarVO vo = (ActivityCalendarVO) this.getCurrentRowObject();
        if (vo.getFlag() == 1) {
            return "<span style='color:green;'>启用</span>";
        }
        return "<span style='color:red;'>禁用</span>";
    }

    public String getOperator() {
        String result = "";
        ActivityCalendarVO vo = (ActivityCalendarVO) this.getCurrentRowObject();
        if (vo.getFlag() == 1) {
            result += "<a href=JavaScript:switchFlag('" + vo.getId() + "','2'" + ")>禁用</a> | ";
        } else if (vo.getFlag() == 2) {
            result += "<a href=JavaScript:switchFlag('" + vo.getId() + "','1'" + ")>启用</a> | ";
        }
        result += "<a href=JavaScript:editInfo('" + vo.getId() + "')>编辑</a>";
        return result;
    }
}
