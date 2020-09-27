package dfh.service.implementations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dfh.dao.MGSDao;
import dfh.model.notdb.MGPlaycheckVo;
import dfh.service.interfaces.MGSService;
import dfh.utils.Arith;

public class MGSServiceImpl implements MGSService {
	
	private MGSDao mgsDao;
	
	public MGSDao getMgsDao() {
		return mgsDao;
	}

	public void setMgsDao(MGSDao mgsDao) {
		this.mgsDao = mgsDao;
	}

	@Override
	public List<?> getListBySql(String sql, Map<String, Object> params) {
		return mgsDao.getListBySql(sql, params);
	}

	@Override
	public Object getOneObject(String sql, Map<String, Object> params) {
		return mgsDao.getOneObject(sql, params);
	}

	/**
	 * MG输赢信息
	 * @param mgsService 静态方法,所以传递service进来
	 * @param loginname 用户名
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param startIndex 开始index(分页)
	 * @param pageSize 每页行数(分页)
	 * @param orderBy 排序字段名称
	 * @param order 排序方向,asc 从小到到大,desc 从大到小
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String,Object> queryMgLogStaticsPage(String loginname, String startTime, String endTime,int startIndex,int pageSize,String orderBy,String order) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		//query total users
		StringBuilder countSql = new StringBuilder("select loginname, SUM(amount) as amount, actiontype from mglog where actiontime>=:startTime and actiontime<:endTime");
		if(StringUtils.isNotBlank(loginname)){
			countSql.append(" and loginname=:loginname");
		}
		countSql.append(" GROUP BY loginname, actiontype");
		Map<String, Object> countParams = new HashMap<String, Object>(); 
		countParams.put("loginname", loginname);
		countParams.put("startTime", startTime);
		countParams.put("endTime", endTime);
		List countPlayList = mgsDao.getListBySql(countSql.toString(), countParams);
		List<MGPlaycheckVo> countMgPlaycheckList = getMgPlayCheckList(countPlayList);
		resultMap.put("totalUser", countMgPlaycheckList.size());
		
		//query user page list
		StringBuilder sql = new StringBuilder("select loginname, SUM(amount) as amount, actiontype from mglog where actiontime>=:startTime and actiontime<:endTime");
		if(StringUtils.isNotBlank(loginname)){
			sql.append(" and loginname=:loginname");
		}
		if(orderBy.equals("loginname") && order.equals("desc")) {
			sql.append(" GROUP BY loginname, actiontype ORDER BY loginname desc limit :startIndex,:pageSize");	
		}else {
			sql.append(" GROUP BY loginname, actiontype ORDER BY loginname limit :startIndex,:pageSize");
		}
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("loginname", loginname);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("startIndex", startIndex);
		params.put("pageSize", pageSize * 2); //因为每个玩家统计之后都是只有bet 和 win的数据(by dean:product database)
		List playList = mgsDao.getListBySql(sql.toString(), params);
		List<MGPlaycheckVo> mgPlaycheckList = getMgPlayCheckList(playList);
        mgPlaycheckList = sortMgPlaycheckList(mgPlaycheckList,orderBy,order);
        
        resultMap.put("mgPlaycheckList", mgPlaycheckList);
        return resultMap;
	}
	
	/**
	 * get List<MGPlaycheckVo> from userList
	 * @param userList
	 * @return
	 */
	private List<MGPlaycheckVo> getMgPlayCheckList(List userList) {
        Map<String, MGPlaycheckVo> palyCheckVoMap = new HashMap<String, MGPlaycheckVo>();
        for (Object obj : userList) {
        	Object[] item = (Object[]) obj;
        	MGPlaycheckVo playVo;
        	String userName = item[0].toString();
        	if(palyCheckVoMap.containsKey(userName)){
        		playVo = palyCheckVoMap.get(userName);
        	}else{
        		playVo = new MGPlaycheckVo();
        		playVo.setLoginname(userName);
        		palyCheckVoMap.put(userName, playVo);
        	}
        	
        	String type = item[2].toString();
        	Double amount = Arith.div(Double.parseDouble(item[1].toString()), 100);
        	if(type.equalsIgnoreCase("bet")){
        		playVo.setBet(amount);
        	}else if(type.equalsIgnoreCase("refund")){
        		playVo.setRefund(amount);
        	}else if(type.equalsIgnoreCase("win")){
        		playVo.setWin(amount);
        	}else if(type.equalsIgnoreCase("progressivewin")){
        		playVo.setProgressivewin(amount);
        	}
        	playVo.setNet(Arith.add(playVo.getNet(), amount));
		}
        List<MGPlaycheckVo> mgPlaycheckList = new ArrayList<MGPlaycheckVo>();
        for (String key: palyCheckVoMap.keySet()) {
        	mgPlaycheckList.add(palyCheckVoMap.get(key));
		}
        return mgPlaycheckList;
	}
	
	/**
	 * sort List<MGPlaycheckVo>
	 * @param mgPlaycheckList
	 * @param orderBy
	 * @param order
	 * @return
	 */
	private List<MGPlaycheckVo> sortMgPlaycheckList (List<MGPlaycheckVo> mgPlaycheckList,String orderBy,String order) {
		Comparator<MGPlaycheckVo> comparator = null;
		if(orderBy.equals("loginname") && order.equals("desc")) {
			comparator =  new Comparator<MGPlaycheckVo>() {
				public int compare(MGPlaycheckVo mgPlaycheckVo1, MGPlaycheckVo mgPlaycheckVo2) {
					return mgPlaycheckVo2.getLoginname().compareTo(mgPlaycheckVo1.getLoginname());
				}
			};
		}else if(orderBy.equals("bet") && order.equals("asc")) {
			comparator =  new Comparator<MGPlaycheckVo>() {
				public int compare(MGPlaycheckVo mgPlaycheckVo1, MGPlaycheckVo mgPlaycheckVo2) {
					return mgPlaycheckVo1.getBet().compareTo(mgPlaycheckVo2.getBet());
				}
			};
		}else if(orderBy.equals("bet") && order.equals("desc")) {
			comparator =  new Comparator<MGPlaycheckVo>() {
				public int compare(MGPlaycheckVo mgPlaycheckVo1, MGPlaycheckVo mgPlaycheckVo2) {
					return mgPlaycheckVo2.getBet().compareTo(mgPlaycheckVo1.getBet());
				}
			};
		}else if(orderBy.equals("win") && order.equals("asc")) {
			comparator =  new Comparator<MGPlaycheckVo>() {
				public int compare(MGPlaycheckVo mgPlaycheckVo1, MGPlaycheckVo mgPlaycheckVo2) {
					return mgPlaycheckVo1.getWin().compareTo(mgPlaycheckVo2.getWin());
				}
			};
		}else if(orderBy.equals("win") && order.equals("desc")) {
			comparator =  new Comparator<MGPlaycheckVo>() {
				public int compare(MGPlaycheckVo mgPlaycheckVo1, MGPlaycheckVo mgPlaycheckVo2) {
					return mgPlaycheckVo2.getWin().compareTo(mgPlaycheckVo1.getWin());
				}
			};
		}else if(orderBy.equals("net") && order.equals("asc")) {
			comparator =  new Comparator<MGPlaycheckVo>() {
				public int compare(MGPlaycheckVo mgPlaycheckVo1, MGPlaycheckVo mgPlaycheckVo2) {
					return mgPlaycheckVo1.getNet().compareTo(mgPlaycheckVo2.getNet());
				}
			};
		}else if(orderBy.equals("net") && order.equals("desc")) {
			comparator =  new Comparator<MGPlaycheckVo>() {
				public int compare(MGPlaycheckVo mgPlaycheckVo1, MGPlaycheckVo mgPlaycheckVo2) {
					return mgPlaycheckVo2.getNet().compareTo(mgPlaycheckVo1.getNet());
				}
			};
		}else { //default login asc
			comparator =  new Comparator<MGPlaycheckVo>() {
	            public int compare(MGPlaycheckVo mgPlaycheckVo1, MGPlaycheckVo mgPlaycheckVo2) {
	                return mgPlaycheckVo1.getLoginname().compareTo(mgPlaycheckVo2.getLoginname());
	            }
	        };
		}
        Collections.sort(mgPlaycheckList, comparator);
        return mgPlaycheckList;
	}
}
