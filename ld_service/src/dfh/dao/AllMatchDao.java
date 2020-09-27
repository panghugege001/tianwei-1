package dfh.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dfh.model.Concert;
import dfh.model.SlotsMatchWeekly;
import dfh.utils.DateUtil;
import dfh.utils.Page;
import dfh.utils.PagenationUtil;
import dfh.utils.StringUtil;

public class AllMatchDao extends HibernateDaoSupport{
	
	public AllMatchDao() {
	}
	
	public Page getSlotsMatchPage(String sql,int pageIndex,int size,String count){
		Page page = new Page();
		SQLQuery query1 = getSession().createSQLQuery(sql);
		query1.setFirstResult((pageIndex - 1) * size);
		query1.setMaxResults(size);
		List list = query1.list();
		List<SlotsMatchWeekly> listSlots=new ArrayList<SlotsMatchWeekly>();
		int z = (pageIndex - 1) * size+1;
		for(Object obj : list){
			SlotsMatchWeekly smw = new SlotsMatchWeekly();
			Object[]objarray = (Object[])obj;
			String loginname=(String)objarray[0];
			if(!StringUtil.isEmpty(loginname)){
				Integer cut = ((Double)Math.floor(loginname.length()/2)).intValue();
				StringBuffer sbs = new StringBuffer();
				sbs.append(loginname.substring(0, cut));
				for (int i = 0; i < cut; i++) {
					sbs.append("*");
				}
				smw.setLoginname(sbs.toString());
			}
			smw.setStartTime((Timestamp)objarray[1]);
			smw.setEndTime((Timestamp)objarray[2]);
			smw.setGetTime((Timestamp)objarray[3]);
			smw.setShowGetTime(DateUtil.formatDateForStandard(smw.getGetTime())); //webservice不支持date的时分秒，单独转换成String形式
			smw.setWin((Double)objarray[4]);
			smw.setPlatform(objarray[5].toString());
			smw.setRankings(z);
			listSlots.add(smw);
			z++;
		}
		page.setPageNumber(pageIndex);
		page.setSize(size);
		page.setTotalRecords(Integer.parseInt(count));
		int pages = PagenationUtil.computeTotalPages(Integer.parseInt(count), size).intValue();
		page.setTotalPages(Integer.valueOf(pages));
		if (pageIndex > pages)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		page.setPageNumber(pageIndex);
		page.setPageContents(listSlots);
		page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		return page;
	}
	
	public Page getConcertPage(String sql,int pageIndex,int size,String count) throws Exception{
		Page page = new Page();
		SQLQuery query1 = getSession().createSQLQuery(sql);
		query1.setFirstResult((pageIndex - 1) * size);
		query1.setMaxResults(size);
		List list = query1.list();
		List<Concert> listSlots=new ArrayList<Concert>();
		for(Object obj : list){
			Concert concert = new Concert();
			Object[]objarray = (Object[])obj;
			String loginname=(String)objarray[0];
			if(!StringUtil.isEmpty(loginname)){
				Integer cut = ((Double)Math.floor(loginname.length()/2)).intValue();
				StringBuffer sbs = new StringBuffer();
				sbs.append(loginname.substring(0, cut));
				for (int i = 0; i < cut; i++) {
					sbs.append("*");
				}
				concert.setLoginname(sbs.toString());
			}
			concert.setStartTime((Timestamp)objarray[1]);
			concert.setEndTime((Timestamp)objarray[2]);
			concert.setBet((Double)objarray[3]);
			listSlots.add(concert);
			//z++;
		}
		page.setPageNumber(pageIndex);
		page.setSize(size);
		page.setTotalRecords(Integer.parseInt(count));
		int pages = PagenationUtil.computeTotalPages(Integer.parseInt(count), size).intValue();
		page.setTotalPages(Integer.valueOf(pages));
		if (pageIndex > pages)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		page.setPageNumber(pageIndex);
		page.setPageContents(listSlots);
		page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		return page;
	}
}
