package dfh.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import dfh.model.AgTryGame;
import dfh.utils.DateUtil;

public class AgTryGameDao extends HibernateDaoSupport {

	/**
	 * 电话号码验证
	 * 
	 * @param agTryGame
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AgTryGame agPhoneVerification(AgTryGame agTryGame) {
		if (agTryGame.getAgPhone() != null) {
			Criteria criteria = getSession().createCriteria(AgTryGame.class);
			criteria.add(Restrictions.eq("agPhone", agTryGame.getAgPhone()));
			List<AgTryGame> list = criteria.list();
			if (list.size() > 0) {
				return (AgTryGame) list.get(0);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Ip验证
	 * @param agTryGame
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer getIpCount(AgTryGame agTryGame) {
		Criteria criteria = getSession().createCriteria(AgTryGame.class);
		criteria.add(Restrictions.eq("agIp", agTryGame.getAgIp()));
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	/**
	 * 试玩账号保存
	 * 
	 * @param agTryGame
	 */
	public AgTryGame agSave(AgTryGame agTryGame) {
		this.getHibernateTemplate().save(agTryGame);
		return agTryGame;
	}

	/**
	 * 游戏登录验证
	 * 
	 * @param agTryGame
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AgTryGame agLogin(AgTryGame agTryGame ,String ip) {
		if (agTryGame==null) {
			String agvalidate =DateUtil.getYYMMDDHHmmssSSS();
			AgTryGame atGame=new AgTryGame();
			atGame.setAgName("kt_" +agvalidate.substring(5, agvalidate.length()));
			atGame.setAgPassword("kt_"+agvalidate.substring(5, agvalidate.length()));
			atGame.setAgRegDate(new Date());
			atGame.setAgIsLogin(1);
			atGame.setAgIp(ip);
			getHibernateTemplate().save(atGame);
			return atGame;
		}else{
		     AgTryGame tryGame=(AgTryGame) getHibernateTemplate().get(AgTryGame.class, agTryGame.getAgName());
		  if (tryGame!=null) {
			  tryGame.setAgIsLogin(tryGame.getAgIsLogin()+1);
		      getHibernateTemplate().update(tryGame);
			return tryGame;
	   	  }
		}
		return null;
		
	}
	
	public void update(AgTryGame agTryGame){
		this.getHibernateTemplate().merge(agTryGame);
	}

}
