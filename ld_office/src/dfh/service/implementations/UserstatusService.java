package dfh.service.implementations;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import dfh.action.vo.UserStatusVO;
import dfh.dao.UserstatusDao;
import dfh.model.Userstatus;
import dfh.service.interfaces.IUserstatusService;

public class UserstatusService implements IUserstatusService{
	
	private UserstatusDao userstatusDao;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public UserstatusDao getUserstatusDao() {
		return userstatusDao;
	}

	public void setUserstatusDao(UserstatusDao userstatusDao) {
		this.userstatusDao = userstatusDao;
	}

	@Override
	public Integer getUserstatusCount(String loginname, String mail,
			String phone, Integer mailflag, Date start, Date end) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(users.loginname) from users ");
		sql.append(" left join userstatus ");
		sql.append(" on users.loginname = userstatus.loginname ");
		sql.append(" where users.loginname is not null");
		if(StringUtils.isNotEmpty(loginname)){
			sql.append(" and users.loginname  like '%"+loginname+"%' ");
		}
		if(StringUtils.isNotEmpty(mail)){
			sql.append(" and users.email  like '%"+mail+"%' ");
		}
		if(StringUtils.isNotEmpty(phone)){
			sql.append(" and users.phone  like '%"+phone+"%' ");
		}
		if(mailflag != null&& mailflag ==0){
			sql.append(" and  userstatus.mailflag="+mailflag+"  ");
		}
		if(!StringUtils.isNotEmpty(loginname)&&!StringUtils.isNotEmpty(mail)&&!StringUtils.isNotEmpty(phone)){
			if (start != null ){
				if (start != null ){
					String startstr = start.toString();
					SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Date date = (Date) sdf.parse(startstr);
					String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					sql.append(" and users.createtime > '"+formatStr2+"' ");
				}
				if (end != null ){
					String endstr = end.toString();
					SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Date date = (Date) sdf.parse(endstr);
					String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					sql.append(" and users.createtime < '"+formatStr2+"' ");
				}
			}
		}
		List numbers = userstatusDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		if(numbers.size() !=0 ){
			return ((BigInteger )numbers.get(0)).intValue();
		}else{
			return 0;
		}
	}

	@Override
	public List getUserstatusList(String loginname, String mail, String phone,
			Integer mailflag, Date start, Date end, int pageno, int length)
			throws Exception {
		int offset=(pageno-1)*length;
		StringBuilder sql = new StringBuilder();
		sql.append(" select users.loginname,users.email,users.phone,users.createtime,userstatus.mailflag,userstatus.touzhuflag,userstatus.remark from users ");
		sql.append(" left join userstatus ");
		sql.append(" on users.loginname = userstatus.loginname ");
		sql.append(" where users.loginname is not null");
		if(StringUtils.isNotEmpty(loginname)){
			sql.append(" and users.loginname  like '%"+loginname+"%' ");
		}
		if(StringUtils.isNotEmpty(mail)){
			sql.append(" and users.email  like '%"+mail+"%' ");
		}
		if(StringUtils.isNotEmpty(phone)){
			sql.append(" and users.phone  like '%"+phone+"%' ");
		}
		if(mailflag != null&& mailflag ==0){
			sql.append(" and  userstatus.mailflag="+mailflag+"  ");
		}
		if(StringUtils.isEmpty(loginname)&&StringUtils.isEmpty(mail)&&StringUtils.isEmpty(phone)){
			if (start != null ){
				if (start != null ){
					String startstr = start.toString();
					SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Date date = (Date) sdf.parse(startstr);
					String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					sql.append(" and users.createtime > '"+formatStr2+"' ");
				}
				if (end != null ){
					String endstr = end.toString();
					SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Date date = (Date) sdf.parse(endstr);
					String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					sql.append(" and users.createtime < '"+formatStr2+"' ");
				}
			}
		}
		List numbers = userstatusDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).setFirstResult(offset).setMaxResults(length).list();
		
		return getUserStatusVOs(numbers);
	}
	
	public List<UserStatusVO> getUserStatusVOs(List list){
		if(null==list){
			return null;
		}else{
			List<UserStatusVO> userStatusVOs = new ArrayList<UserStatusVO>();
			for(int i=0;i<list.size();i++){
				Object[] o=(Object[]) list.get(i);
				UserStatusVO userStatusVO = new UserStatusVO();
				userStatusVO.setLoginname((String)o[0]);
				userStatusVO.setEmail((String)o[1]);
				userStatusVO.setPhone((String)o[2]);
				userStatusVO.setCreatetime((Timestamp)o[3]);
				if(o[4] == null){
					userStatusVO.setMailflag(1);
				}else{
					userStatusVO.setMailflag((Integer)o[4]);
				}
				if(o[5] == null){
					userStatusVO.setTouzhuflag(0);
				}else{
					userStatusVO.setTouzhuflag((Integer)o[5]);
				}
				userStatusVO.setRemark((String)o[6]);
				userStatusVOs.add(userStatusVO);
			}
			return userStatusVOs;
		}
		
	}

	@Override
	public String modifyUserMailFlag(String loginname, String mailflag)
			throws Exception {
		String msg = null;
		Integer flag= Integer.parseInt(mailflag);
		Userstatus userstatus = userstatusDao.getEntity(Userstatus.class, loginname);
		if(flag ==1){
			if(null != userstatus){
				userstatus.setMailflag(flag);
				userstatusDao.saveorupdate(userstatus);
			}
		}if(flag == 0){
			if(null != userstatus){
				userstatus.setMailflag(flag);
				userstatusDao.saveorupdate(userstatus);
			}else{
				userstatusDao.saveorupdate(new Userstatus(loginname, 0, 0));
			}
		}
		return msg;
	}
	
	@Override
	public String closeTouzhuFlag(String loginname, String touzhuflag,String remark)
			throws Exception {
		String msg = null;
		Integer flag= Integer.parseInt(touzhuflag);
		Userstatus userstatus = userstatusDao.getEntity(Userstatus.class, loginname);
			if(null != userstatus){
				userstatus.setTouzhuflag(flag);
				userstatus.setRemark(remark);
				userstatusDao.saveorupdate(userstatus);
			}else{
			    userstatus = new Userstatus();
			    userstatus.setLoginname(loginname);
			    userstatus.setCashinwrong(0);
			    userstatus.setMailflag(0);
			    userstatus.setTouzhuflag(flag);
			    userstatus.setRemark(remark);
			    userstatusDao.saveorupdate(userstatus);
			   }
		return msg;
	}
	
}
