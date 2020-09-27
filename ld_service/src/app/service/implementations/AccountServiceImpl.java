package app.service.implementations;

import app.dao.BaseDao;
import app.model.vo.AccountVO;
import app.model.vo.PtCommissionsVo;
import app.service.interfaces.IAccountService;
import dfh.model.PtCommissions;
import dfh.utils.DateUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceImpl implements IAccountService {

    private Logger log = Logger.getLogger(LosePromoServiceImpl.class);
    private BaseDao baseDao;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Page queryDayCommissionPageList(AccountVO vo) {

        Integer pageIndex = vo.getPageIndex();
        Integer pageSize = vo.getPageSize();
        log.info("queryDayCommissionPageList方法的参数为：" + vo);

        DetachedCriteria dc = DetachedCriteria.forClass(PtCommissions.class);
        dc.add(Restrictions.eq("id.agent", vo.getAgent()));
        if(vo.getCommissionStartDate() != null){
        	dc.add(Restrictions.gt("createTime", DateUtil.parseDateForStandard(vo.getCommissionStartDate() + " 00:00:00")));
        }
        if(vo.getCommissionEndDate() != null){
        	dc.add(Restrictions.lt("createTime", DateUtil.parseDateForStandard(vo.getCommissionEndDate() + " 00:00:00" )));
        }
        Page page = PageQuery.queryForPagenation(baseDao.getHibernateTemplate(), dc, pageIndex, pageSize, null);
        List<PtCommissions> list = page.getPageContents();
        List<PtCommissionsVo> listRes = new ArrayList<PtCommissionsVo>();
        if(list != null){
        	
        	for (PtCommissions p : list) {
        		PtCommissionsVo tempVo = new PtCommissionsVo();
        		tempVo.setAmount(p.getAmount() + "");
        		tempVo.setCreatedate(sf.format(p.getId().getCreatedate()));
        		tempVo.setPlatform("slotmachine".equals(p.getId().getPlatform()) ? "老虎机佣金":"其他佣金");
        		listRes.add(tempVo);
        	}
        	
        }
        page.setPageContents(listRes);
        return page;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
