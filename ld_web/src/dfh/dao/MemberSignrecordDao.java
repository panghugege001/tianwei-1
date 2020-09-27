package dfh.dao;


import dfh.model.MemberSignrecord;

public class MemberSignrecordDao extends UniversalDao {
	
	public void login(MemberSignrecord member){
		this.saveOrUpdate(member);
	}
	
	public boolean islogined(String username){
		MemberSignrecord o = (MemberSignrecord) this.get(MemberSignrecord.class, username);
		if (o==null) {
			return false;
		}
		return o.getFlag().intValue()==1?false:true;
	}
	
	public void logout(MemberSignrecord member){
		this.saveOrUpdate(member);
	}

}
