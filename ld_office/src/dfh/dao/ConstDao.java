package dfh.dao;

import dfh.model.Const;

public class ConstDao extends UniversalDao{
	
	
	/**
	 * 邮箱是否开启
	 * @return
	 */
	public boolean isOpenEmail(){
		Const email = (Const) get(Const.class, "邮箱");
		if(null != email){
			if(email.getValue().equals("1")){
				return true ;
			}else{
				return false ;
			}
		}else{
			return true ;
		}
	}

}
