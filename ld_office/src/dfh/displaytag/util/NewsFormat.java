package dfh.displaytag.util;

import org.displaytag.decorator.TableDecorator;

import dfh.action.vo.AnnouncementVO;
import dfh.model.enums.AnnouncementType;

public class NewsFormat extends TableDecorator {
	
	
	public String getType(){
		AnnouncementVO newsVo=(AnnouncementVO) this.getCurrentRowObject();
		return AnnouncementType.getText(newsVo.getType());
	}

}
