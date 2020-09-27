package dfh.service.interfaces;

import java.util.List;

import dfh.model.LotteryItem;
import dfh.utils.Page;

public interface IUserLotteryService {
	Page queryUserLotteryRecordPage(String loginname, int pageIndex,int size);
	void winningLottery(String loginname,String itemName)throws Exception;
	LotteryItem getPrize();
}
