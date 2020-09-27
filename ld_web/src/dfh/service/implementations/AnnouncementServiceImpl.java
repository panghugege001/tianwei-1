package dfh.service.implementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import dfh.action.vo.AnnouncementVO;
import dfh.dao.AnnouncementDao;
import dfh.dao.BankinfoDao;
import dfh.dao.LogDao;
import dfh.dao.TradeDao;
import dfh.model.Announcement;
import dfh.model.Payorder;
import dfh.model.Users;
import dfh.model.enums.CreditChangeType;
import dfh.service.interfaces.AnnouncementService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class AnnouncementServiceImpl implements AnnouncementService {

	private LogDao logDao;
	private AnnouncementDao annDao;
	private TradeDao tradeDao;
	private BankinfoDao bankinfoDao;

	public AnnouncementDao getAnnDao() {
		return annDao;
	}


	public void setAnnDao(AnnouncementDao annDao) {
		this.annDao = annDao;
	}


	public AnnouncementServiceImpl() {
	}

	
	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}



	public List query() {
		// TODO Auto-generated method stub
		Iterator it = annDao.query().iterator();
		List<AnnouncementVO> list=new ArrayList<AnnouncementVO>();
		while(it.hasNext()){
			Announcement anns=(Announcement) it.next();
			list.add(new AnnouncementVO( anns.getId(), DateUtil.fmtyyyy_MM_d(anns.getCreatetime()), StringUtil.subString(anns.getTitle(), 46)));
		}
		
		return list;
	}
	
	public List queryTopNews(){
		Iterator it = annDao.queryTopNews().iterator();
		List<AnnouncementVO> list=new ArrayList<AnnouncementVO>();
		while(it.hasNext()){
			Announcement anns=(Announcement) it.next();
			list.add(new AnnouncementVO( anns.getId(), DateUtil.fmtyyyy_MM_d(anns.getCreatetime()), StringUtil.subString(anns.getTitle(), 60)));
		}
		return list;
	}



	public List queryAll(int pageNumber, int length) {
		// TODO Auto-generated method stub
		int offset=(pageNumber-1)*length;
		List queryAll = annDao.queryAll(offset, length);
		List<AnnouncementVO> list=new ArrayList<AnnouncementVO>();
		if (queryAll==null) {
			return list;
		}
		Iterator it = queryAll.iterator();
		while(it.hasNext()){
			Announcement anns=(Announcement) it.next();
			list.add(new AnnouncementVO( anns.getId(), DateUtil.fmtyyyy_MM_d(anns.getCreatetime()), anns.getTitle()));
		}
		return list;
	}



	public int totalCount() {
		// TODO Auto-generated method stub
		return annDao.totalCount();
	}

	
	public AnnouncementVO getAnnouncement(int aid) {
		// TODO Auto-generated method stub
		Object o = annDao.get(Announcement.class, aid);
		if (o!=null) {
			Announcement ann= (Announcement)o;
			return new AnnouncementVO(ann.getId(), ann.getContent(), DateUtil.fmtyyyy_MM_d(ann.getCreatetime()), ann.getTitle());
			
		}
		return null;
	}
	
	@Override
	public String addPayorder(String billno, Double money,String loginname, String msg,String date) {
		String returnmsg=null;
		if(annDao.get(Payorder.class,billno)==null){
			try {
				Users users = (Users)annDao.get(Users.class,loginname);
				Payorder payorder=new Payorder();
				payorder.setBillno(billno);
				payorder.setPayPlatform("ips");
				payorder.setFlag(0);
				payorder.setNewaccount(Constants.FLAG_FALSE);
				payorder.setLoginname(loginname);
				payorder.setAliasName(users.getAccountName());
				payorder.setMoney(money);
				payorder.setPhone(users.getPhone());
				payorder.setEmail(users.getEmail());
				payorder.setCreateTime(DateUtil.now());
				payorder.setMsg("ips单号:"+msg);
				annDao.save(payorder);
				
				tradeDao.changeCredit(loginname, money, CreditChangeType.NETPAY.getCode(), billno, "ips单号:"+msg);
				
				bankinfoDao.changeAmountOnline(4, money,billno);
			}catch (Exception e) {
				e.printStackTrace();
			}	
			
		}else{
			returnmsg="此笔交易已经支付成功";
		}
		
		
		return returnmsg;
	}

	@Override
	public Users getUser(String loginname){
		Users users=(Users)annDao.get(Users.class,loginname);
		return users;
	}


	public TradeDao getTradeDao() {
		return tradeDao;
	}


	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}


	public BankinfoDao getBankinfoDao() {
		return bankinfoDao;
	}


	public void setBankinfoDao(BankinfoDao bankinfoDao) {
		this.bankinfoDao = bankinfoDao;
	}



}
