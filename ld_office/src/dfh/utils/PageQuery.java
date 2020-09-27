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
			Integer totalResults = 0;
			List list = hibernateTemplate.findByCriteria(criteria.setProjection(Projections.rowCount()));
			if (null != list && !list.isEmpty()) {
				totalResults = (Integer) list.get(0);
			}
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
	public static Page queryForPagenation_Friendintroduce(HibernateTemplate hibernateTemplate, DetachedCriteria criteria, Integer pageIndex, Integer size,String type, Order order) {
		if ((size == null) || (size.intValue() == 0))
			size = Page.PAGE_DEFAULT_SIZE;
		if (pageIndex == null)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		Page page = new Page();
		page.setSize(size);
		try {
			
			if(!type.equals("0")){
				ProjectionList pList = Projections.projectionList().add(Projections.rowCount());
				pList.add(Projections.sum("money"));
				
				List list = hibernateTemplate.findByCriteria(criteria.setProjection(pList));
				Object[] array = (Object[]) list.get(0);
				if(array[1] != null){
					page.setStatics1((Double) array[1]);
				}
			}
			
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
	
	@SuppressWarnings("rawtypes")
	public static Page queryForPagenation(HibernateTemplate hibernateTemplate,String recordSql, String totalSql, Integer pageIndex, Integer pageSize) {
		
		Page page = new Page();
		
		if (pageSize == null || pageSize.intValue() == 0) {
			
			pageSize = Page.PAGE_DEFAULT_SIZE;
		}
		page.setSize(pageSize);
		
		if (pageIndex == null) {
			
			pageIndex = Page.PAGE_BEGIN_INDEX;
		}
		
		try {
			Integer totalRecords = Integer.parseInt(String.valueOf(hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(totalSql.toString()).list().get(0)));
			log.info("totalRecords:" + totalRecords);
			
			Integer totalPages = PagenationUtil.computeTotalPages(totalRecords, pageSize);
			
			page.setTotalRecords(totalRecords);
			page.setTotalPages(totalPages);
			
			if (pageIndex > totalPages) {
				
				pageIndex = Page.PAGE_BEGIN_INDEX;
			}
			
			page.setPageNumber(pageIndex);
			
			List list = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(recordSql).list();
			
			page.setPageContents(list);
			
			if (null != list && !list.isEmpty()) {
				
				page.setNumberOfRecordsShown(Integer.valueOf(list.size()));
			} else {
				
				page.setNumberOfRecordsShown(0);
			}
		} catch (Exception e) {
			
			log.error("执行queryForPagenation方法发生异常，异常信息：" + e.getMessage());
		}
		
		return page;
	}
	

	/*public static Page queryForPagenationWithStatistics(HibernateTemplate hibernateTemplate, DetachedCriteria criteria, Integer pageIndex, Integer size, String staticsFiled1, String staticsFiled2,String staticsFiled3) {
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
//			System.out.println(list.size());
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
			page.setPageContents(hibernateTemplate.findByCriteria(criteria.setProjection(null), (pageIndex.intValue() - 1) * size.intValue(), size.intValue()));
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return page;
	}*/
	
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
//			System.out.println(list.size());
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
	
	public static Page queryForPagenationWithStatistics_ptdate(HibernateTemplate hibernateTemplate, DetachedCriteria criteria, Integer pageIndex, Integer size, String staticsFiled1, String staticsFiled2,String staticsFiled3,String staticsFiled4, Order order) {
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
			if (StringUtils.isNotEmpty(staticsFiled4))
				pList.add(Projections.sum(staticsFiled4));
			
			List list = hibernateTemplate.findByCriteria(criteria.setProjection(pList));
//			System.out.println(list.size());
			Object[] array = (Object[]) list.get(0);
			if (StringUtils.isNotEmpty(staticsFiled1))
				page.setStatics1((Double) array[1]);
			if (StringUtils.isNotEmpty(staticsFiled2))
				page.setStatics2((Double) array[2]);
			if (StringUtils.isNotEmpty(staticsFiled3))
				if(array[3] != null){
					page.setStatics3(Double.valueOf((String)array[3]));
				}
			
			if (StringUtils.isNotEmpty(staticsFiled4)){
				if(array[4] != null){
					page.setStatics4(Double.valueOf((String)array[4]));
				}
			}
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
	
	public static Page queryForPagenationWithStatistics(HibernateTemplate hibernateTemplate, DetachedCriteria criteria, Integer pageIndex, Integer size, String staticsFiled1, String staticsFiled2,String staticsFiled3,String staticsFiled4, Order order) {
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
			if (StringUtils.isNotEmpty(staticsFiled4))
				pList.add(Projections.sum(staticsFiled4));

			List list = hibernateTemplate.findByCriteria(criteria.setProjection(pList));
//			System.out.println(list.size());
			Object[] array = (Object[]) list.get(0);
			if (StringUtils.isNotEmpty(staticsFiled1))
				page.setStatics1((Double) array[1]);
			if (StringUtils.isNotEmpty(staticsFiled2))
				page.setStatics2((Double) array[2]);
			if (StringUtils.isNotEmpty(staticsFiled3))
				page.setStatics3((Double) array[3]);
			if (StringUtils.isNotEmpty(staticsFiled4))
				page.setStatics4((Double) array[4]);
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
	
	
	public static Page queryForPagenationWithStatistics(HibernateTemplate hibernateTemplate, DetachedCriteria criteria, Integer pageIndex, Integer size, String staticsFiled1, String staticsFiled2,String staticsFiled3,String staticsFiled4,String staticsFiled5, Order order) {
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
			if (StringUtils.isNotEmpty(staticsFiled4))
				pList.add(Projections.sum(staticsFiled4));
			if (StringUtils.isNotEmpty(staticsFiled5))
				pList.add(Projections.sum(staticsFiled5));

			List list = hibernateTemplate.findByCriteria(criteria.setProjection(pList));
//			System.out.println(list.size());
			Object[] array = (Object[]) list.get(0);
			if (StringUtils.isNotEmpty(staticsFiled1))
				page.setStatics1((Double) array[1]);
			if (StringUtils.isNotEmpty(staticsFiled2))
				page.setStatics2((Double) array[2]);
			if (StringUtils.isNotEmpty(staticsFiled3))
				page.setStatics3((Double) array[3]);
			if (StringUtils.isNotEmpty(staticsFiled4))
				page.setStatics4((Double) array[4]);
			if (StringUtils.isNotEmpty(staticsFiled5))
				page.setStatics5((Double) array[5]);
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

	public static Page queryForPagenationWithStatistics(HibernateTemplate hibernateTemplate, DetachedCriteria criteria, Integer pageIndex, Integer size, 
			String staticsFiled1, String staticsFiled2,String staticsFiled3,String staticsFiled4,String staticsFiled5, String staticsFiled6) {
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
			if (StringUtils.isNotEmpty(staticsFiled4))
				pList.add(Projections.sum(staticsFiled4));
			if (StringUtils.isNotEmpty(staticsFiled5))
				pList.add(Projections.sum(staticsFiled5));
			if (StringUtils.isNotEmpty(staticsFiled6))
				pList.add(Projections.sum(staticsFiled6));

			List list = hibernateTemplate.findByCriteria(criteria.setProjection(pList));
//			System.out.println(list.size());
			Object[] array = (Object[]) list.get(0);
			if (StringUtils.isNotEmpty(staticsFiled1))
				page.setStatics1((Double) array[1]);
			if (StringUtils.isNotEmpty(staticsFiled2))
				page.setStatics2((Double) array[2]);
			if (StringUtils.isNotEmpty(staticsFiled3))
				page.setStatics3((Double) array[3]);
			if (StringUtils.isNotEmpty(staticsFiled4))
				page.setStatics4((Double) array[4]);
			if (StringUtils.isNotEmpty(staticsFiled5))
				page.setStatics5((Double) array[5]);
			if (StringUtils.isNotEmpty(staticsFiled6))
				page.setStatics6((Double) array[6]);
			Integer totalResults = (Integer) array[0];
			int pages = PagenationUtil.computeTotalPages(totalResults, size).intValue();
			log.debug("totalResults:" + totalResults);
			page.setTotalRecords(totalResults);
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(hibernateTemplate.findByCriteria(criteria.setProjection(null), (pageIndex.intValue() - 1) * size.intValue(), size.intValue()));
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return page;
	}
}