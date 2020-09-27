package dfh.service.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.dao.SlaveDao;
import dfh.dao.UniversalDao;
import dfh.model.LotteryItem;
import dfh.model.SignAmount;
import dfh.model.UserLotteryRecord;
import dfh.service.interfaces.IUserLotteryService;
import dfh.utils.DateUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;

public class UserLotteryServiceImpl implements IUserLotteryService{
	private static Logger log = Logger.getLogger(UserLotteryServiceImpl.class);
	private UniversalDao universalDao;
	private SlaveDao slaveDao;

	public UniversalDao getUniversalDao() {
		return universalDao;
	}

	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}

	public SlaveDao getSlaveDao() {
		return slaveDao;
	}

	public void setSlaveDao(SlaveDao slaveDao) {
		this.slaveDao = slaveDao;
	}
	
	public Page queryUserLotteryRecordPage(String loginname, int pageIndex,int size){
		DetachedCriteria dc = DetachedCriteria.forClass(UserLotteryRecord.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
//		dc = dc.addOrder(Order.desc("winningDate"));
		Order o = Order.desc("winningDate");
		int totalRecord = PageQuery.queryForCount(slaveDao.getHibernateTemplate(), dc);
		if(totalRecord>0){
			Page page = PageQuery.queryForPagenation(slaveDao.getHibernateTemplate(), dc, pageIndex, size, o);
			return page;
		}else{
			return new Page();
		}
	}
	
	public void winningLottery(String loginname,String itemName)throws Exception{
		DetachedCriteria dc = DetachedCriteria.forClass(SignAmount.class);
		dc.add(Restrictions.eq("username", loginname));
		List<SignAmount> list = universalDao.findByCriteria(dc);
		SignAmount signAmount=null;
		if(null!=list&&list.size()>0){
			signAmount = list.get(0);
		}else{
			throw new Exception("领取奖项延迟，请稍后再试");
		}
		//检查签到日期
		if(signAmount.getContinuesigncount()>0){
			log.info("已达到抽奖条件");
			//连续签到日数归0
			signAmount.setContinuesigncount(0);
			universalDao.update(signAmount);
			//记录获得奖项
			UserLotteryRecord userLotteryRecord = new UserLotteryRecord();
			userLotteryRecord.setItemName(itemName);
			userLotteryRecord.setIsReceive(0);
			userLotteryRecord.setLoginname(loginname);
			userLotteryRecord.setWinningDate(DateUtil.getCurrentTimestamp());
			universalDao.save(userLotteryRecord);
		}else{
			throw new Exception("资格不符!");
		}
	}

	@Override
	public LotteryItem getPrize() {
		DetachedCriteria dc = DetachedCriteria.forClass(LotteryItem.class);
		List<LotteryItem> lotteryItems = slaveDao.findByCriteria(dc);
		int randomInt = new Random().nextInt(1000) + 1;   //限制到小数点下一位
		int count = 0;
		LotteryItem prize = null;
		for(LotteryItem item : lotteryItems){
			count+=item.getPercent()*10;
			if(count>=randomInt){
				prize = item;
				break;
			}
		}
		return prize;
	}
	
	public static void main(String[] argv){
		int[] prizePool = {0,0,0,0,0,0,0,0};
		List<LotteryItem> lotteryItems = new ArrayList<LotteryItem>();
		lotteryItems.add(new LotteryItem(1l,"1","iphone7",0.0,1));
		lotteryItems.add(new LotteryItem(2l,"2","8元",50.0,1));
		lotteryItems.add(new LotteryItem(3l,"3","100%优惠券",10.0,1));
		lotteryItems.add(new LotteryItem(4l,"4","88元",10.0,1));
		lotteryItems.add(new LotteryItem(5l,"5","18元",10.0,1));
		lotteryItems.add(new LotteryItem(6l,"6","ipadpro",0.0,1));
		lotteryItems.add(new LotteryItem(7l,"7","88%优惠券",10.0,1));
		lotteryItems.add(new LotteryItem(8l,"8","188元",10.0,1));
		for(int i=0; i<1000; i++){
			int randomInt = new Random().nextInt(1000) + 1;   //限制到小数点下一位
			int count = 0;
			LotteryItem prize = null;
			for(LotteryItem item : lotteryItems){
				count+=item.getPercent()*10;
				if(count>=randomInt){
					prize = item;
					break;
				}
			}
			int type = Integer.parseInt(prize.getType());
			prizePool[type-1]+=1;
		}
		for(int i=0;i<prizePool.length;i++){
			System.out.println("第"+(i+1)+"项=="+prizePool[i]);			
		}
//		for(int i=1; i<=100; i++){
//			Random r = new Random();
//			int randomInt = r.nextInt(100) + 1;
//			System.out.println(randomInt);
//		}
	}
}
