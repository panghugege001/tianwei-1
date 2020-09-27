package app.service.interfaces;

import java.util.List;

import dfh.utils.Page;
import app.model.vo.AnnouncementForAppVO;
import app.model.vo.OnlinePayRecordVO;

public interface ISystemAnnouncementService {

	Page queryIndexTopList(OnlinePayRecordVO vo);
	
}