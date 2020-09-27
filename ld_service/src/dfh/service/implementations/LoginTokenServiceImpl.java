package dfh.service.implementations;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria; 
import org.hibernate.criterion.Restrictions;

import dfh.dao.TokenInfoDao;
import dfh.model.TokenInfo;
import dfh.service.interfaces.LoginTokenService;
import dfh.utils.StringUtil;
import dfh.utils.validator.TokenValidator;
import dfh.utils.validator.TokenValidatorFactory;

public class LoginTokenServiceImpl extends UniversalServiceImpl implements LoginTokenService {
	
	TokenInfoDao tokenInfoDao;
	TokenValidatorFactory validatorFactory;

	@Override
	public String createLoginToken(String platformCode, String loginname) {
		TokenInfo tokenInfo = TokenInfo.Builder.aTokenInfo().withLoginname(loginname)
				.withPlatform(platformCode.toLowerCase()).withTimestamp(new Date()).build();
		Long id = getIdByUser(platformCode, loginname);
		if (id != null) {
			tokenInfo.setId(id);
		}
		tokenInfoDao.getHibernateTemplate().saveOrUpdate(tokenInfo);
		return tokenInfo.getToken();
	}

	 @Override
		public boolean isValidToken(String platform, String token,String loginname) {
			//验证是否存在DB
	        DetachedCriteria query = DetachedCriteria.forClass(TokenInfo.class);
	        query.add(Restrictions.eq("token", token));

	        if (StringUtil.isNotBlank(loginname)) {
	        	query.add(Restrictions.eq("loginname", loginname));
			}
	        
	        List<TokenInfo> list = tokenInfoDao.getHibernateTemplate().findByCriteria(query);
	        if (list == null || list.isEmpty()) {
	            return false;
	        }
	        //个自验证
	        TokenValidator validator = validatorFactory.getvalidator(platform);
	        TokenInfo tokenInfo = list.get(0);
	        return validator.isValid(tokenInfo);
		}

	@Override
	public TokenInfo getInfo(String token) {
		DetachedCriteria query = DetachedCriteria.forClass(TokenInfo.class);
		query.add(Restrictions.eq("token", token));
		List<TokenInfo> list = tokenInfoDao.getHibernateTemplate().findByCriteria(query);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	public TokenInfo getInfo(String platformCode, String loginname) {
		DetachedCriteria query = DetachedCriteria.forClass(TokenInfo.class);
		query.add(Restrictions.eq("platform", platformCode.toLowerCase()));
		query.add(Restrictions.eq("loginname", loginname.toLowerCase()));
		List<TokenInfo> list = tokenInfoDao.getHibernateTemplate().findByCriteria(query);
		return list == null || list.isEmpty() ? null : list.get(0);
	}
	
	@Override
    public TokenInfo proccessTokenInfoWithValid(String platformCode, String loginname) {
        TokenInfo tokenInfo = this.getInfo(platformCode, loginname);
        if(tokenInfo != null){
        	 //个自验证
            TokenValidator validator = validatorFactory.getvalidator(platformCode);
            if(validator.isValidWeb(tokenInfo)){
            	return tokenInfo;
            }
        } else {
        	tokenInfo = new TokenInfo();
        	tokenInfo.setLoginname(loginname);
        	tokenInfo.setPlatform(platformCode);
        }
        
        TokenInfo newtokenInfo = TokenInfo.Builder.aTokenInfo()
                .withLoginname(loginname)
                .withPlatform(platformCode.toLowerCase())
                .withTimestamp(new Date())
                .build();

        tokenInfo.setTimestamp(newtokenInfo.getTimestamp());
        tokenInfo.setToken(newtokenInfo.getToken());
        
        tokenInfoDao.getHibernateTemplate().saveOrUpdate(tokenInfo);
        return tokenInfo;
    }
	
	public Long getIdByUser(String platformCode, String loginname) {
		List<Long> list = tokenInfoDao.getHibernateTemplate().find(
				"SELECT id FROM TokenInfo as t where t.platform=? and t.loginname=?",
				new String[] { platformCode, loginname });
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	public void setTokenInfoDao(TokenInfoDao tokenInfoDao) {
		this.tokenInfoDao = tokenInfoDao;
	}
	
	public void setValidatorFactory(TokenValidatorFactory validatorFactory) {
	   this.validatorFactory = validatorFactory;
	}
}
