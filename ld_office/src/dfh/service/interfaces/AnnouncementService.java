package dfh.service.interfaces;

import java.util.List;

import dfh.model.enums.AnnouncementType;

public interface AnnouncementService {

	void addAnnouncement(String type, String content, String operator,String title)throws Exception;

	void deleteAnnouncement(Integer id, String operator);

	List getAllIndexAnnouncement(int pageNumber,int rowCount) throws Exception;
	
	void modifyAnnouncement(Integer id,String type,String content,String operator,String title)throws Exception;
	
	Object queryAnnouncementById(Integer id);
	
	int getAnnouncementCount() throws Exception;

}