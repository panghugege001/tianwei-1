package dfh.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class PageQuery {
	private static Logger log = Logger.getLogger(PageQuery.class);

	public static Integer getNextIndex(boolean fprev, boolean prev, boolean next, boolean fnext, Integer index, Integer lastIndex) {
		Integer nextIndex = Page.PAGE_BEGIN_INDEX;
		if ((index == null) || (lastIndex == null)) {
			return nextIndex;
		}
		if (fnext)
			nextIndex = lastIndex;
		else if ((next) && (index.compareTo(lastIndex) < 0))
			nextIndex = Integer.valueOf(index.intValue() + 1);
		else if ((prev) && (index.intValue() - 1 > 0)) {
			nextIndex = Integer.valueOf(index.intValue() - 1);
		}

		return nextIndex;
	}

	public static Integer queryForCount(HibernateTemplate hibernateTemplate, DetachedCriteria criteria) {
		Integer count = Integer.valueOf(0);
		List list = hibernateTemplate.findByCriteria(criteria.setProjection(Projections.rowCount()));
		if (list.size() > 0)
			count = (Integer) list.get(0);
		return count;
	}

	public static Page queryForPagenation(HibernateTemplate hibernateTemplate, DetachedCriteria criteria, Integer pageIndex, Integer size, Order order) {
		if ((size == null) || (size.intValue() == 0))
			size = Page.PAGE_DEFAULT_SIZE;
		if (pageIndex == null)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		Page page = new Page();
		page.setSize(size);
		try {
			Integer totalResults = (Integer) hibernateTemplate.findByCriteria(criteria.setProjection(Projections.rowCount())).get(0);
			log.debug("totalResults:" + totalResults);
			int pages = PagenationUtil.computeTotalPages(totalResults, size).intValue();
			page.setTotalRecords(totalResults);
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			if(order != null){
				criteria.addOrder(order);
			}
			page.setPageContents(hibernateTemplate.findByCriteria(criteria.setProjection(null), (pageIndex.intValue() - 1) * size.intValue(), size.intValue()));
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	public static Page queryForPagenationWithStatistics(HibernateTemplate hibernateTemplate, DetachedCriteria criteria, Integer pageIndex, Integer size, String staticsFiled1, String staticsFiled2,String staticsFiled3, Order order) {
		if ((size == null) || (size.intValue() == 0))
			size = Page.PAGE_DEFAULT_SIZE;
		if (pageIndex == null)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		log.debug("pageIndex:" + pageIndex);
		Page page = new Page();
		page.setPageNumber(pageIndex);
		page.setSize(size);
		try {
			ProjectionList pList = Projections.projectionList().add(Projections.rowCount());

			if (StringUtils.isNotEmpty(staticsFiled1))
				pList.add(Projections.sum(staticsFiled1));
			if (StringUtils.isNotEmpty(staticsFiled2))
				pList.add(Projections.sum(staticsFiled2));
			if (StringUtils.isNotEmpty(staticsFiled3))
				pList.add(Projections.sum(staticsFiled3));

			List list = hibernateTemplate.findByCriteria(criteria.setProjection(pList));
			Object[] array = (Object[]) list.get(0);
			if (StringUtils.isNotEmpty(staticsFiled1))
				page.setStatics1((Double) array[1]);
			if (StringUtils.isNotEmpty(staticsFiled2))
				page.setStatics2((Double) array[2]);
			if (StringUtils.isNotEmpty(staticsFiled3))
				page.setStatics3((Double) array[3]);
			Integer totalResults = (Integer) array[0];
			int pages = PagenationUtil.computeTotalPages(totalResults, size).intValue();
			log.debug("totalResults:" + totalResults);
			page.setTotalRecords(totalResults);
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			if(order != null){
				criteria.addOrder(order);
			}
			page.setPageContents(hibernateTemplate.findByCriteria(criteria.setProjection(null), (pageIndex.intValue() - 1) * size.intValue(), size.intValue()));
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return page;
	}
	
	public static Page queryForPagenationForApp(HibernateTemplate hibernateTemplate, DetachedCriteria criteria, Integer pageIndex, Integer size, Order order) {
		if ((size == null) || (size.intValue() == 0))
			size = Page.PAGE_DEFAULT_SIZE;
		if (pageIndex == null)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		Page page = new Page();
		page.setSize(size);
		try {
			Integer totalResults = (Integer) hibernateTemplate.findByCriteria(criteria.setProjection(Projections.rowCount())).get(0);
			log.debug("totalResults:" + totalResults);
			int pages = PagenationUtil.computeTotalPages(totalResults, size).intValue();
			page.setTotalRecords(totalResults);
			page.setTotalPages(Integer.valueOf(pages));
/*			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;*/
			page.setPageNumber(pageIndex);
			if (order != null) {
				criteria.addOrder(order);
			}
			page.setPageContents(hibernateTemplate.findByCriteria(criteria.setProjection(null), (pageIndex.intValue() - 1) * size.intValue(), size.intValue()));
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}	
	
	public static Page queryForPagenationWithStatisticsForApp(HibernateTemplate hibernateTemplate, DetachedCriteria criteria, Integer pageIndex, Integer size, String staticsFiled1, String staticsFiled2,String staticsFiled3, Order order) {
		if ((size == null) || (size.intValue() == 0))
			size = Page.PAGE_DEFAULT_SIZE;
		if (pageIndex == null)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		log.debug("pageIndex:" + pageIndex);
		Page page = new Page();
		page.setPageNumber(pageIndex);
		page.setSize(size);
		try {
			ProjectionList pList = Projections.projectionList().add(Projections.rowCount());
			
			if (StringUtils.isNotEmpty(staticsFiled1))
				pList.add(Projections.sum(staticsFiled1));
			if (StringUtils.isNotEmpty(staticsFiled2))
				pList.add(Projections.sum(staticsFiled2));
			if (StringUtils.isNotEmpty(staticsFiled3))
				pList.add(Projections.sum(staticsFiled3));
			
			List list = hibernateTemplate.findByCriteria(criteria.setProjection(pList));
			//System.out.println(list.size());
			Object[] array = (Object[]) list.get(0);
			if (StringUtils.isNotEmpty(staticsFiled1))
				page.setStatics1((Double) array[1]);
			if (StringUtils.isNotEmpty(staticsFiled2))
				page.setStatics2((Double) array[2]);
			if (StringUtils.isNotEmpty(staticsFiled3))
				page.setStatics3((Double) array[3]);
			Integer totalResults = (Integer) array[0];
			int pages = PagenationUtil.computeTotalPages(totalResults, size).intValue();
			log.debug("totalResults:" + totalResults);
			page.setTotalRecords(totalResults);
			page.setTotalPages(Integer.valueOf(pages));
/*			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;*/
			page.setPageNumber(pageIndex);
			if (order != null) {
				criteria.addOrder(order);
			}
			page.setPageContents(hibernateTemplate.findByCriteria(criteria.setProjection(null), (pageIndex.intValue() - 1) * size.intValue(), size.intValue()));
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return page;
	}	
}