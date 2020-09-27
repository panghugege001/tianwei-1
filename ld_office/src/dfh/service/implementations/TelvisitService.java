package dfh.service.implementations;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.TelsivitVOForCount;
import dfh.action.vo.TelvisitVO;
import dfh.dao.ProposalDao;
import dfh.dao.TelvisitDao;
import dfh.dao.TelvisitremarkDao;
import dfh.dao.TelvisittaskDao;
import dfh.dao.UserDao;
import dfh.model.Proposal;
import dfh.model.Telvisit;
import dfh.model.Telvisitremark;
import dfh.model.Telvisittask;
import dfh.model.Users;
import dfh.service.interfaces.ITelvisitService;
import dfh.utils.DateUtil;

public class TelvisitService implements ITelvisitService {
	
	private TelvisitDao telvisitDao;
	private TelvisittaskDao telvisittaskDao;
	private TelvisitremarkDao telvisitremarkDao;
	private UserDao userDao;
	private ProposalDao proposalDao;
	private String msg;
	



	@Override
	public Telvisittask getVisitTask(int taskid) throws Exception {
		// TODO Auto-generated method stub
		return telvisittaskDao.getEntity(Telvisittask.class, new Integer(taskid));
	}

	

	@Override
	public boolean taskComplete(int taskid) throws Exception {
		// TODO Auto-generated method stub
		Telvisittask telvisittask = telvisittaskDao.getEntity(Telvisittask.class, new Integer(taskid));
		if (telvisittask.getTaskstatus().intValue()==1) {
			return true; // 已经完成
		}
		String hql="from Telvisit where taskid=? and execstatus!=1 and execstatus!=0";
		List<Telvisit> list = telvisitDao.findEntity(hql, new Integer(taskid));
		if (list==null||list.size()<=0) {
			// complete
			telvisittaskDao.taskComplete(taskid);
			return true;
		}
		return false;
	}






	@Override
	public List<Telvisitremark> getTelvisitRemark(int telvisitid, int pageno,
			int length) throws Exception {
		// TODO Auto-generated method stub
		int offset=(pageno-1)*length;
		return telvisitremarkDao.getTelvisitRemark(telvisitid, offset, length);
	}


	

	@Override
	public void endvisit(int visitid, int execstatus, String operator, String remark) throws Exception {
		// TODO Auto-generated method stub
		telvisitremarkDao.saveorupdate(new Telvisitremark(visitid, DateUtil.convertToTimestamp(new Date()), remark, operator,execstatus));
		telvisitDao.endvisit(visitid, execstatus);
	}


	
	@Override
	public List<Telvisitremark> getTelvisitRemark(int telvisitid)
			throws Exception {
		// TODO Auto-generated method stub
		String hql="from Telvisitremark where telvisitid=? order by addtime desc";
		return telvisitremarkDao.findEntity(hql, new Integer(telvisitid));
	}

	
	@Override
	public void startvisit(int visitid, String operator) throws Exception {
		// TODO Auto-generated method stub
		telvisitDao.startvisit(visitid, operator);
	}
	
	

	@Override
	public Integer getTaskDetailTotalCount(String loginname, String phone,
			String email, int islock, int visitResult, Date start, Date end,
			int taskid,String intro) throws Exception {
		// TODO Auto-generated method stub
		return telvisitDao.getTaskDetailListCount(loginname, phone, email, islock, visitResult, start, end, taskid,intro);
	}





	@Override
	public List<TelvisitVO> getTaskDetailList(String loginname, String phone,
			String email, int islock, int visitResult, Date start, Date end,
			int taskid,String intro, int pageno, int length) throws Exception{
		// TODO Auto-generated method stub
		int offset=(pageno-1)*length;
		Object object = telvisitDao.getTaskDetailList(loginname, phone, email, islock, visitResult, start, end, taskid,intro, offset, length);
		return parserSqlResult(object);
	}




	@Override
	public List<TelvisitVO> getTaskDetailList(int taskid, int pageno,
			int length) throws Exception {
		// TODO Auto-generated method stub
		int offset=(pageno-1)*length;
		Object object = telvisitDao.getTaskDetailList(taskid, offset, length);
		
		return parserSqlResult(object);
	}
	
	private List<TelvisitVO> parserSqlResult(Object object)throws Exception{
		if (object!=null) {
			List<TelvisitVO> users=new ArrayList<TelvisitVO>();
			if (object instanceof Object[]) {
				Object[] o=(Object[]) object;
				users.add(new TelvisitVO(((Number)o[0]).intValue(), ((Number)o[2]).intValue(), 
						((Number)o[3]).intValue(), ((Number)o[4]).intValue(),((Number)o[12]).intValue(), 
						String.valueOf(o[1]), String.valueOf(o[5]),String.valueOf(o[6]), 
						String.valueOf(o[7]), String.valueOf(o[8]), String.valueOf(o[9]), 
						(Date)o[10], (Date)o[11],String.valueOf(o[13]),String.valueOf(o[14])));
			}else{
				// list
				List list=(List) object;
				int size=list.size();
				for (int i = 0; i < size; i++) {
					Object[] o=(Object[]) list.get(i);
					if(o[12] == null){
						users.add(new TelvisitVO(((Number)o[0]).intValue(), ((Number)o[2]).intValue(), 
								((Number)o[3]).intValue(), ((Number)o[4]).intValue(),999, 
								String.valueOf(o[1]), String.valueOf(o[5]),String.valueOf(o[6]), 
								String.valueOf(o[7]), String.valueOf(o[8]), String.valueOf(o[9]), 
								(Date)o[10], (Date)o[11],String.valueOf(o[13]),String.valueOf(o[14])));
					}else{
						users.add(new TelvisitVO(((Number)o[0]).intValue(), ((Number)o[2]).intValue(), 
								((Number)o[3]).intValue(), ((Number)o[4]).intValue(),((Number)o[12]).intValue(), 
								String.valueOf(o[1]), String.valueOf(o[5]),String.valueOf(o[6]), 
								String.valueOf(o[7]), String.valueOf(o[8]), String.valueOf(o[9]), 
								(Date)o[10], (Date)o[11],String.valueOf(o[13]),String.valueOf(o[14])));
					}
				}
			}
			return users;
		}
		return null;
	}


	@Override
	public Integer getTaskDetailTotalCount(int taskid) throws Exception {
		String sql="from Telvisit where taskid=?";
		List<Telvisit> entity = telvisitDao.findEntity(sql, new Integer(taskid));
		if (entity==null||entity.size()<=0) {
			return 0;
		}
		return entity.size();
	}
	

	@Override
	public Integer getTaskTotalCount(String taskname,int taskstatus, Date start, Date end)throws Exception {
		// TODO Auto-generated method stub
		if (start==null||end==null) {
			this.msg="起始时间和结束时间，不可为空";
			return null;
		}
		
		if (taskname==null||taskname.trim().equals("")) {
			if (taskstatus==-1) {
				return telvisittaskDao.getTaskTotalCount(start, end);
			}
			return telvisittaskDao.getTaskTotalCount(taskstatus, start, end);
		}else{
			if (taskstatus==-1) {
				return telvisittaskDao.getTaskTotalCount(taskname, start, end);
			}
			return telvisittaskDao.getTaskTotalCount(taskname, taskstatus, start, end);
		}
	}


	@Override
	public List<Telvisittask> getTaskList(String taskname, int taskstatus,
			Date start, Date end, int pageno, int length) throws Exception {
		// TODO Auto-generated method stub
		if (start==null||end==null) {
			this.msg="起始时间和结束时间，不可为空";
			return null;
		}
		int offset=(pageno-1)*length;
		if (taskname==null||taskname.trim().equals("")) {
			if (taskstatus==-1) {
				return telvisittaskDao.getTaskList(start, end, offset, length);
			}
			return telvisittaskDao.getTaskList(taskstatus, start, end, offset, length);
		}else{
			if (taskstatus==-1) {
				return telvisittaskDao.getTaskList(taskname, start, end, offset, length);
			}
			return telvisittaskDao.getTaskList(taskname, taskstatus, start, end, offset, length);
		}
	}
	

	
	@Override
	public boolean generateTask(String taskName, Date start, Date end,
			String isCashin,String level, String agentURL, int loginInterval, Date taskStartTime,Date taskEndTime)
			throws Exception {
		// TODO Auto-generated method stub
		if (start==null||end==null) {
			this.msg="会员注册时间范围不可为空";
			return false;
		}
		Date now=new Date();
		if (taskStartTime.before(now)||taskEndTime.before(now)) {
			this.msg="任务的起始时间和结束时间，不能小于当前日期";
			return false;
		}
		
		if (taskStartTime==null||taskEndTime==null) {
			this.msg="任务周期的起始时间和结束时间，不可为空";
			return false;
		}
		Session session = telvisittaskDao.getSessionFactory().getCurrentSession();
		StringBuffer sbf = new StringBuffer();
		sbf.append("select loginname from users where flag=0 and phone!='' and createtime>=? and createtime<=? " +
				" and role='MONEY_CUSTOMER' and warnflag!=4 and 1=1 ");
		Map<String,Object> mm = new HashMap<String, Object>();
		if(null!=level && !"".equals(level)){
			sbf.append(" and level =:lel");
			mm.put("lel",level );
		}
		if(agentURL!=null&&!agentURL.equals("")){
			sbf.append(" and referWebsite =:rw");
			mm.put("rw",agentURL );
		}
		if(isCashin!=null&&!isCashin.equals("")){
			sbf.append(" and isCashin =:ic");
			mm.put("ic",isCashin );
		}
		if(loginInterval>0){
			sbf.append(" and (UNIX_TIMESTAMP()-UNIX_TIMESTAMP(lastLoginTime))/60/60/24 >=:li");
			mm.put("li",loginInterval );
		}
		
		Query query = session.createSQLQuery(sbf.toString())
		.setTimestamp(0, start).setTimestamp(1, end);
		if(!mm.isEmpty()){
			query = query.setProperties(mm);//直接将map参数传组query对像
		}
		List<String> userslist = query.list();
		if (userslist==null||userslist.size()<=0) {
			this.msg="没有符合条件的会员";
			return false;
		}
		
		Telvisittask telvisittask = new Telvisittask(taskName, sbf.toString(), 0, new Long(0), DateUtil.convertToTimestamp(taskStartTime), DateUtil.convertToTimestamp(taskEndTime),DateUtil.getCurrentTimestamp(),0);
		telvisittaskDao.saveorupdate(telvisittask);
		
		int userSize = userslist.size();
		List<Telvisit> telvisitEntities=new ArrayList<Telvisit>();
		for (int i = 0; i < userSize; i++) {
			String loginname = (String)userslist.get(i);
			telvisitEntities.add(new Telvisit(loginname, DateUtil.getCurrentTimestamp(),telvisittask.getId(), 0, 2));
		}
		
		telvisitDao.saveAll(telvisitEntities);
		return true;

	}
	
	@Override
	public boolean generateTaskforXima(String taskName,Date taskStartTime,Date taskEndTime)throws Exception{
		Date now=new Date();
		
		if (taskStartTime==null||taskEndTime==null) {
			this.msg="任务周期的起始时间和结束时间，不可为空";
			return false;
		}
		
		if (taskStartTime.before(now)||taskEndTime.before(now)) {
			this.msg="任务的起始时间和结束时间，不能小于当前日期";
			return false;
		}
		String sql="from Proposal where ";
		Calendar calendar = Calendar.getInstance();
		//calendar.add(Calendar.DATE,-calendar.get(Calendar.DAY_OF_WEEK)+1);    //得到当前日期所在周的第一天
		calendar.setTime(DateUtil.getToday());
		 StringBuffer taskcondition=new StringBuffer();
		taskcondition.append(" type=507 and flag=2 ");
		taskcondition.append(" and createtime >='"+DateUtil.formatDateForStandard(calendar.getTime())+"'");
		System.out.println(sql+taskcondition.toString());
		List<Proposal> proposals = proposalDao.findEntities(sql+taskcondition.toString());
		System.out.println(proposals.size());
		if (proposals==null||proposals.size()<=0) {
			this.msg="最近没有系统洗码的会员";
			return false;
		}
		Telvisittask telvisittask = new Telvisittask(taskName, taskcondition.toString(), 0, new Long(0), DateUtil.convertToTimestamp(taskStartTime), DateUtil.convertToTimestamp(taskEndTime),DateUtil.getCurrentTimestamp(),0);
		telvisittaskDao.saveorupdate(telvisittask);
		int proposalSize = proposals.size();
		List<Telvisit> telvisitEntities=new ArrayList<Telvisit>();
		for (int i = 0; i < proposalSize; i++) {
			Proposal proposal = proposals.get(i);
			telvisitEntities.add(new Telvisit(proposal.getLoginname(), DateUtil.getCurrentTimestamp(),telvisittask.getId(), 0, 2));
		}
		telvisitDao.saveAll(telvisitEntities);
		return true;
	};
	
	//getter and setter :
	public UserDao getUserDao() {
		return userDao;
	}


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


	public TelvisitDao getTelvisitDao() {
		return telvisitDao;
	}

	public void setTelvisitDao(TelvisitDao telvisitDao) {
		this.telvisitDao = telvisitDao;
	}

	public TelvisittaskDao getTelvisittaskDao() {
		return telvisittaskDao;
	}

	public void setTelvisittaskDao(TelvisittaskDao telvisittaskDao) {
		this.telvisittaskDao = telvisittaskDao;
	}


	public ProposalDao getProposalDao() {
		return proposalDao;
	}



	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}



	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return msg;
	}

	public TelvisitremarkDao getTelvisitremarkDao() {
		return telvisitremarkDao;
	}

	public void setTelvisitremarkDao(TelvisitremarkDao telvisitremarkDao) {
		this.telvisitremarkDao = telvisitremarkDao;
	}



	@Override
	public String updateTelvisittask(int taskid, String taskName, Date start,
			Date end) throws Exception {
		String message="";
		if (start==null||end==null) {
			message ="任务周期的起始时间和结束时间，不可为空";
			return message;
		}
		Date now=new Date();
		if (start.before(now)||end.before(now)) {
			message = "任务的起始时间和结束时间，不能小于当前日期";
			return message;
		}
		if (end.before(start)) {
			message = "任务的结束时间不能小于起始日期";
			return message;
		}
		
		Telvisittask telvisittask = telvisittaskDao.loadEntity(Telvisittask.class, taskid);
		telvisittask.setTaskname(taskName);
		telvisittask.setStarttime(DateUtil.convertToTimestamp(start));
		telvisittask.setEndtime(DateUtil.convertToTimestamp(end));
		telvisittaskDao.saveorupdate(telvisittask);
		return message;
	}



	@Override
	public String cancleTask(int taskid,String locker) throws Exception {
		String message=null;
		Telvisittask telvisittask = telvisittaskDao.loadEntity(Telvisittask.class, taskid);
		telvisittask.setTaskstatus(2);
		telvisittaskDao.saveorupdate(telvisittask);
		String sql = "select telvisit.id,telvisit.execstatus,telvisit.loginname,telvisit.addtime,telvisit.taskid,"
			+"telvisit.islock,telvisit.locker from Telvisit telvisit where taskid = "+taskid;
		List  telvisits = telvisitDao.findEntity(sql);
		for(int i=0;i<telvisits.size();i++){
			Object[] objects = ((Object[])telvisits.get(i));
			if((Integer)objects[1]!= 1){
				Telvisit telvisit = new Telvisit();
				telvisit.setExecstatus(0);
				telvisit.setIslock(1);
				telvisit.setAddtime((Timestamp)objects[3]);
				telvisit.setId((Integer)objects[0]);
				telvisit.setLocker(locker);
				telvisit.setLoginname((String)objects[2]);
				telvisit.setTaskid(taskid);
				telvisitDao.saveorupdate(telvisit);
				
			}
		}
		
		return message;
	}



	@Override
	public String unlockTelVisit(int taskid) throws Exception {
		String msg = null;
		Telvisit telvisit  = telvisitDao.getEntity(Telvisit.class, taskid);
		if(null == telvisit){
			msg = "解锁失败";
		}else{
			telvisit.setIslock(0);
			telvisitDao.saveorupdate(telvisit);
		}
		
		return msg;
	}



	@Override
	public Integer getTelVisitRemarkTotalCount(String taskname, int taskstatus,
			Date start, Date end, String operator, int execstatus)
			throws Exception {
		// TODO Auto-generated method stub
//		if (start==null||end==null) {
//			this.msg="起始时间和结束时间，不可为空";
//			return null;
//		}
		StringBuilder sql = new StringBuilder();
		if(StringUtils.isNotEmpty(operator)){
			sql.append("select  distinct telvisittask.taskname ");
			sql.append(" from  telvisitremark ");
			sql.append(" left join  telvisit");
			sql.append(" on telvisitremark.telvisitid = telvisit.id ");
			sql.append(" left join  telvisittask ");
			sql.append(" on telvisittask.id=telvisit.taskid ");
			sql.append(" where telvisittask.taskstatus != 6 ");
			
			if(StringUtils.isNotEmpty(taskname)){
				sql.append(" and telvisittask.taskname like '%"+taskname+"%' ");
			}
			if(taskstatus != -1){
				sql.append(" and telvisittask.taskstatus="+taskstatus+"  ");
			}
			if(execstatus != -1){
				sql.append(" and  telvisitremark.execstatus="+execstatus+"  ");
			}
			
			sql.append("  and telvisitremark.operator = '"+operator+"' ");
			
			if (start != null ){
				String startstr = start.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and telvisittask.addtime > '"+formatStr2+"' ");
			}
			if (end != null ){
				String endstr = end.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and telvisittask.addtime < '"+formatStr2+"' ");
			}
		}else{
			sql.append("select  distinct telvisittask.taskname ");
			sql.append(" from  telvisittask left join  telvisit  ");
			sql.append(" on telvisittask.id=telvisit.taskid ");
			sql.append(" left join  telvisitremark");
			sql.append(" on telvisitremark.telvisitid = telvisit.id ");
			sql.append(" where telvisittask.taskstatus != 6 ");
			
			if(StringUtils.isNotEmpty(taskname)){
				sql.append(" and telvisittask.taskname like '%"+taskname+"%' ");
			}
			if(taskstatus != -1){
				sql.append(" and telvisittask.taskstatus="+taskstatus+"  ");
			}
			if(execstatus != -1){
				sql.append(" and  telvisitremark.execstatus="+execstatus+"  ");
			}
			
			if (start != null ){
				String startstr = start.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and telvisittask.addtime > '"+formatStr2+"' ");
			}
			if (end != null ){
				String endstr = end.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and telvisittask.addtime < '"+formatStr2+"' ");
			}
		}
		
		
		
		
		return telvisittaskDao.getTaskTotalCountByLocalSql(sql.toString());
		
	}



	@Override
	public List getTelVisitRemarkTotalListForCount(String taskname,
			int taskstatus, Date start, Date end, String operator,
			int execstatus,int pageno,int length) throws Exception {
		
		int offset=(pageno-1)*length;
		
		StringBuilder sql = new StringBuilder();
		if(StringUtils.isNotEmpty(operator)){
			sql.append("select  distinct telvisittask.id,telvisittask.taskname,telvisittask.taskstatus,telvisittask.addtime ");
			sql.append(" from  telvisitremark ");
			sql.append(" left join  telvisit");
			sql.append(" on telvisitremark.telvisitid = telvisit.id ");
			sql.append(" left join  telvisittask ");
			sql.append(" on telvisittask.id=telvisit.taskid ");
			sql.append(" where telvisittask.taskstatus != 6 ");
			//			sql.append(" from Telvisittask telvisittask left join Telvisit telvisit  ");
//			sql.append(" on telvisittask.id=telvisit.taskid ");
//			sql.append(" left join Telvisitremark telvisitremark");
//			sql.append(" on telvisitremark.telvisitid = telvisit.id ");
//			sql.append(" where telvisittask.taskstatus != 6 ");
			
			if(StringUtils.isNotEmpty(taskname)){
				sql.append(" and telvisittask.taskname like '%"+taskname+"%' ");
			}
			if(taskstatus != -1){
				sql.append(" and telvisittask.taskstatus="+taskstatus+"  ");
			}
			if(execstatus != -1){
				sql.append(" and  telvisitremark.execstatus="+execstatus+"  ");
			}
			sql.append("  and telvisitremark.operator = '"+operator+"' ");
			if (start != null ){
				String startstr = start.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and telvisittask.addtime > '"+formatStr2+"' ");
			}
			if (end != null ){
				String endstr = end.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and telvisittask.addtime < '"+formatStr2+"' ");
			}
			sql.append(" order by telvisittask.addtime ");
			List list = telvisittaskDao.getListByLocalSql(sql.toString(),offset,length);
			
			
			//return null;
			return getTelsivitVOForCountListWithOperator(list,execstatus,operator);
		}else{
			sql.append("select  distinct telvisittask.id,telvisittask.taskname,telvisittask.taskstatus,telvisittask.addtime ");
			sql.append(" from  telvisittask left join  telvisit  ");
			sql.append(" on telvisittask.id=telvisit.taskid ");
			sql.append(" left join  telvisitremark");
			sql.append(" on telvisitremark.telvisitid = telvisit.id ");
			sql.append(" where telvisittask.taskstatus != 6 ");
			
			if(StringUtils.isNotEmpty(taskname)){
				sql.append(" and telvisittask.taskname like '%"+taskname+"%' ");
			}
			if(taskstatus != -1){
				sql.append(" and telvisittask.taskstatus="+taskstatus+"  ");
			}
			if(execstatus != -1){
				sql.append(" and  telvisitremark.execstatus="+execstatus+"  ");
			}
			if (start != null ){
				String startstr = start.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and telvisittask.addtime > '"+formatStr2+"' ");
			}
			if (end != null ){
				String endstr = end.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and telvisittask.addtime < '"+formatStr2+"' ");
			}
			sql.append(" order by telvisittask.addtime ");
			List list = telvisittaskDao.getListByLocalSql(sql.toString(),offset,length);
			
			//return null;
			return getTelsivitVOForCountList(list,execstatus);
		}
		
		
		
		
	}
	
	private List<TelsivitVOForCount> getTelsivitVOForCountList(List list,int execstatus){
		if(null==list){
			return null;
		}else{
			List<TelsivitVOForCount> telsivitVOForCounts = new ArrayList<TelsivitVOForCount>();
			
			for(int i=0;i<list.size();i++){
				Object[] o=(Object[]) list.get(i);
				telsivitVOForCounts.add(new TelsivitVOForCount((Integer)o[0], (String)o[1], (Integer)o[2], 
						(Date)o[3],telvisitremarkDao.getNumberCount((Integer)o[0], execstatus),
						telvisitremarkDao.getFailNumberCount((Integer)o[0], execstatus),
						telvisitremarkDao.getSuccessNumberCount((Integer)o[0], execstatus),""));
			}
			return telsivitVOForCounts;
		}
		
	}
	
	private List<TelsivitVOForCount> getTelsivitVOForCountListWithOperator(List list,int execstatus,String operator){
		if(null==list){
			return null;
		}else{
			List<TelsivitVOForCount> telsivitVOForCounts = new ArrayList<TelsivitVOForCount>();
			
			for(int i=0;i<list.size();i++){
				Object[] o=(Object[]) list.get(i);
				telsivitVOForCounts.add(new TelsivitVOForCount((Integer)o[0], (String)o[1], (Integer)o[2], 
						(Date)o[3],telvisitremarkDao.getNumberCountWithOperator((Integer)o[0], execstatus,operator),
						telvisitremarkDao.getFailNumberCountWithOperator((Integer)o[0], execstatus,operator),
						telvisitremarkDao.getSuccessNumberCountWithOperator((Integer)o[0], execstatus,operator),operator));
			}
			return telsivitVOForCounts;
		}
		
	}



	@Override
	public Integer getCountByOperator(int taskid) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select count(operator) from telvisitremark ");
		sql.append(" left join  telvisit");
		sql.append(" on telvisitremark.telvisitid = telvisit.id ");
		sql.append(" left join  telvisittask ");
		sql.append(" on telvisittask.id=telvisit.taskid ");
		sql.append(" where telvisittask.id = "+taskid+" ");
		sql.append(" group by operator ");
		
		List numbers = telvisittaskDao.getListByLocalSql(sql.toString());
		
//		for(int i=0;i<numbers.size();i++){
//			numberCount += ((BigInteger)numbers.get(i)).intValue();
//		}
		//System.out.println(numbers.size() +"       ++");
		return numbers.size();
	}



	@Override
	public List<TelsivitVOForCount> getCountDetailsByOperator(Telvisittask telvisittask,int pageno,int length) {
		
		List<TelsivitVOForCount> telsivitVOForCounts = new ArrayList<TelsivitVOForCount>();
		StringBuilder sql = new StringBuilder();
		
		sql.append("select operator,count(operator) from telvisitremark ");
		sql.append(" left join  telvisit");
		sql.append(" on telvisitremark.telvisitid = telvisit.id ");
		sql.append(" left join  telvisittask ");
		sql.append(" on telvisittask.id=telvisit.taskid ");
		sql.append(" where telvisittask.id = "+telvisittask.getId()+" ");
		sql.append(" group by operator ");
		sql.append(" order by count(operator)desc");
		List numbers = telvisittaskDao.getListByLocalSql(sql.toString());
		for(int i=0;i<numbers.size();i++){
			Object[] o=(Object[]) numbers.get(i);
			telsivitVOForCounts.add(new TelsivitVOForCount(telvisittask.getId(), telvisittask.getTaskname(), 
					telvisittask.getTaskstatus(), telvisittask.getAddtime(), ((BigInteger)o[1]).intValue(), 
					this.getFailSuccessNumberByOperator(telvisittask.getId(), (String)o[0], 0), 
					this.getFailSuccessNumberByOperator(telvisittask.getId(), (String)o[0], 1), (String)o[0]));
		}
		return telsivitVOForCounts;
	}
	
	public Integer getFailSuccessNumberByOperator(int taskid,String operator,int execstatus){
		StringBuilder sql = new StringBuilder();
		sql.append("select count(operator) from telvisitremark ");
		sql.append(" left join  telvisit");
		sql.append(" on telvisitremark.telvisitid = telvisit.id ");
		sql.append(" left join  telvisittask ");
		sql.append(" on telvisittask.id=telvisit.taskid ");
		sql.append(" where telvisittask.id = "+taskid+" and telvisitremark.operator='"+operator+"' ");
		sql.append(" and telvisitremark.execstatus = "+ execstatus);
		sql.append(" group by operator ");
		
		List numbers = telvisittaskDao.getListByLocalSql(sql.toString());

		if(numbers.size() !=0 ){
			return ((BigInteger )numbers.get(0)).intValue();
		}else{
			return 0;
		}
		
	}
	
	












}
