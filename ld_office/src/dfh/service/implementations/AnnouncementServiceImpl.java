package dfh.service.implementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import dfh.action.vo.AnnouncementVO;
import dfh.dao.AnnouncementDao;
import dfh.dao.LogDao;
import dfh.model.Announcement;
import dfh.model.enums.AnnouncementType;
import dfh.model.enums.OperationLogType;
import dfh.service.interfaces.AnnouncementService;
import dfh.utils.DateUtil;

public class AnnouncementServiceImpl implements AnnouncementService {

	private LogDao logDao;
	private AnnouncementDao announcementDao;
	

	private Logger log=Logger.getLogger(AnnouncementServiceImpl.class);

	
	public AnnouncementServiceImpl() {
	}

	public void addAnnouncement(String type, String content, String operator,String title)throws Exception {
//		content=content.replaceAll("\\n", "<br/>");
		announcementDao.save(new Announcement(type,content, DateUtil.now(),title));
		logDao.insertOperationLog(operator, OperationLogType.ADD_ANNOUNCEMENT, null);
	}

	public void deleteAnnouncement(Integer id, String operator) {
		announcementDao.delete(Announcement.class, id);
		logDao.insertOperationLog(operator, OperationLogType.DEL_ANNOUNCEMENT, null);
	}

	public List getAllIndexAnnouncement(int pageNumber ,int rowCount)throws Exception {
		int offset=(pageNumber-1)*rowCount;
		try {
			List list = announcementDao.findAll(offset, rowCount);
			Iterator it = list.iterator();
			List recvList=new ArrayList();
			while(it.hasNext()){
				// 给过长标题和内容减少适当后续文字。
				Announcement ann=(Announcement) it.next();
				recvList.add(new AnnouncementVO(ann.getId(),ann.getType(),"",DateUtil.fmtDateForBetRecods(ann.getCreatetime()),ann.getTitle()));
			}
			return recvList;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
	public void modifyAnnouncement(Integer id, String type, String content,
			String operator, String title) {
		// TODO Auto-generated method stub
		Object o= announcementDao.get(Announcement.class, id);
		if(o!=null){
			Announcement announcement=(Announcement)o;
			announcement.setContent(content);
//			announcement.setCreatetime(DateUtil.now());
			announcement.setTitle(title);
			announcement.setType(type);
			
			announcementDao.saveOrUpdate(announcement);
			logDao.insertOperationLog(operator, OperationLogType.MODIFY_ANNOUNCEMENT, null);
		}
		
	}
	
	public Object queryAnnouncementById(Integer id) {
		// TODO Auto-generated method stub
		return announcementDao.get(Announcement.class, id);
	}
	
	public int getAnnouncementCount() throws Exception {
		// TODO Auto-generated method stub
		try {
			return announcementDao.getCount();
		} catch (Exception e) {
			// TODO: handle exception
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

	
	public AnnouncementDao getAnnouncementDao() {
		return announcementDao;
	}

	public void setAnnouncementDao(AnnouncementDao announcementDao) {
		this.announcementDao = announcementDao;
	}


	

	
}
