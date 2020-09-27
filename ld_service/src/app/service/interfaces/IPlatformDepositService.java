package app.service.interfaces;

import app.model.vo.PlatformDepositVO;

public interface IPlatformDepositService {

	// 首存优惠
	PlatformDepositVO firstDeposit(PlatformDepositVO vo) throws Exception;
	
	// 次存优惠
	PlatformDepositVO timesDeposit(PlatformDepositVO vo) throws Exception;
	
	// 限时优惠
	PlatformDepositVO limitedTime(PlatformDepositVO vo) throws Exception;
	
}