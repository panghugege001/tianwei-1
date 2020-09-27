package dfh.service.implementations;

import java.util.Date;
import dfh.dao.AgTryGameDao;
import dfh.model.AgTryGame;
import dfh.service.interfaces.AgTryGameService;
import dfh.service.interfaces.NotifyService;
import dfh.service.interfaces.SeqService;

public class AgTryGameServiceImpl implements AgTryGameService {

	public AgTryGameDao agTryGameDao;

	public SeqService seqService;

	private NotifyService notifyService;

	public AgTryGame agPhoneVerification(AgTryGame agTryGame) {
		return agTryGameDao.agPhoneVerification(agTryGame);
	}

	public AgTryGame agSave(AgTryGame agTryGame) {
		String password = agTryGame.getAgPassword();
		agTryGame.setAgName("kt_" + seqService.generateAgTryGameID());
		agTryGame.setAgRegDate(new Date());
		agTryGame.setAgIsLogin(0);
		agTryGame.setAgPassword(password);
		AgTryGame game = agTryGameDao.agSave(agTryGame);
		if (game != null) {
			notifyService.sendSms(game.getAgPhone(), "密码:" + password);
			return game;
		}
		return null;
	}

	public AgTryGame agLogin(AgTryGame agTryGame,String ip) {
		// TODO Auto-generated method stub
		return agTryGameDao.agLogin(agTryGame,ip);
	}

	public Integer getIpCount(AgTryGame agTryGame) {
		// TODO Auto-generated method stub
		return agTryGameDao.getIpCount(agTryGame);
	}

	public AgTryGameDao getAgTryGameDao() {
		return agTryGameDao;
	}

	public void setAgTryGameDao(AgTryGameDao agTryGameDao) {
		this.agTryGameDao = agTryGameDao;
	}

	public SeqService getSeqService() {
		return seqService;
	}

	public void setSeqService(SeqService seqService) {
		this.seqService = seqService;
	}

	public NotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

}
