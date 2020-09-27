package dfh.service.implementations;

import dfh.dao.MemberSignrecordDao;
import dfh.model.MemberSignrecord;
import dfh.service.interfaces.IMemberSignrecord;

public class MemberSignrecordImpl implements IMemberSignrecord {
	
	private MemberSignrecordDao memberDao;

	public MemberSignrecordDao getMemberDao() {
		return memberDao;
	}

	public void setMemberDao(MemberSignrecordDao memberDao) {
		this.memberDao = memberDao;
	}

	@Override
	public boolean isLogined(String username) throws Exception {
		// TODO Auto-generated method stub
		
		return memberDao.islogined(username);
	}

	@Override
	public void login(String username) throws Exception {
		// TODO Auto-generated method stub
		this.memberDao.login(new MemberSignrecord(username,0));
	}

	@Override
	public void logout(String username) throws Exception {
		// TODO Auto-generated method stub
		this.memberDao.login(new MemberSignrecord(username,1));
	}

	

}
