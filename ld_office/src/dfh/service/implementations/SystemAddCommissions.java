package dfh.service.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.dao.ProposalDao;
import dfh.model.AgProfit;
import dfh.model.Cashin;
import dfh.model.Commissionrecords;
import dfh.model.CommissionrecordsId;
import dfh.model.Commissions;
import dfh.model.CommissionsId;
import dfh.model.Payorder;
import dfh.model.Proposal;
import dfh.model.PtCommissionRecords;
import dfh.model.PtCommissions;
import dfh.model.PtCommissionsId;
import dfh.model.PtProfit;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.bean.Bean4Xima;
import dfh.model.bean.XimaDataVo;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class SystemAddCommissions extends AbstractBatchGameinfoServiceImpl  {
	
	private Logger log=Logger.getLogger(SystemAddCommissions.class);
	private ProposalDao proposalDao;
	private Sheet sheet = null;
	private Workbook wb = null;
	private InputStream stream = null;
	
	@Override
	public String autoAddXimaProposal(File file,Double rate) throws Exception {
		return null;
	}

	@Override
	public List<Bean4Xima> excelToNTwoVo(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excuteAutoXimaProposal() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateXimaStatus() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void closeFile() {
		try {
			wb.close();
			// wb_writeable.close();
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件关闭------------>error");
		}
	}
	
	private boolean isNullCurRow(int row) {
		int cols = sheet.getColumns();// 列数
		for (int i = 0; i < cols; i++) {
			String cellValue = this.getStringValue(row, i); // 当前单元格的值
			if (cellValue != null && !"".equals(cellValue)) {
				return true;
			}
		}
		return false;
	}
	
	public String getStringValue(int rows, int cols) {
		Cell c = sheet.getCell(cols, rows);
		
		String s = c.getContents();
		if (c.getType() == CellType.LABEL) {
			LabelCell labelc00 = (LabelCell) c;
			s = labelc00.getString();
		}
		
		return s;
	}
	
	public double getNumberValue(int rows, int cols) {
		Cell c = sheet.getCell(cols, rows);
		
		NumberCell nc = (NumberCell) c;
		double s = nc.getValue();
		if (c.getType() == CellType.NUMBER) {
			NumberCell labelc00 = (NumberCell) c;
			s = labelc00.getValue();
		}
		// System.out.println(s);
		return s;
	}
	
	public ProposalDao getProposalDao() {
		return proposalDao;
	}

	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}

	public static void main(String[] args) {
		
	}

	
	@Override
	public String autoAddCommissions(File file) throws Exception {
		// TODO Auto-generated method stub
		String msg=null;
		stream = new FileInputStream(file.toString());
		wb = Workbook.getWorkbook(stream);
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		
		int rows = sheet.getRows(); // 行数
		int cols = sheet.getColumns();// 列数
		
		Calendar calendar=Calendar.getInstance();
		try {
			for (int i = 0; i < rows; i++) {
				String string=this.getStringValue(i, 0);
				String stringEd=this.getStringValue(i, 1);
				Users user = (Users) this.getUserDao().get(Users.class,string,LockMode.UPGRADE);
				if (user==null) {
					log.info("代理："+string+"，不存在");
					continue;
				}
				Integer year=calendar.get(Calendar.YEAR);
				Integer month=calendar.get(Calendar.MONTH);
				if(month==0){
					year=year-1;
					month=12;
				}
				CommissionsId commissionsId=new CommissionsId(string, year, month);
				Commissions commissions=new Commissions();
				commissions.setId(commissionsId);
				commissions.setCreateTime(DateUtil.now());
				commissions.setFlag(1);
				commissions.setEaProfitAmount(Double.parseDouble(stringEd)); //ea报表利润
				//commissions.setActiveuser(Integer.parseInt(this.getStringValue(i, 2))); //活跃会员数

				DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
				dc.add(Restrictions.eq("agent", string));
				List<Users> users=this.getTaskDao().findByCriteria(dc);
				commissions.setSubCount(users.size());
				Double amount=0.00;
				Double agamount=0.00;
				
				Integer activeUser = 0;
				Double betNum = 0.00 ; //投注额
				Double sixlotterySum = 0.0 ; 
				Double sloatmachine = 0.0 ;
				Double liveall = 0.0 ;
				for (Users users2 : users) {
					Double sixlottery = 0.0 ; //六合彩佣金
					String loginname=users2.getLoginname();
//					System.out.println(users2.getLoginname());
					DetachedCriteria proposaldc = DetachedCriteria.forClass(Proposal.class);
					DetachedCriteria proposalSavedc = DetachedCriteria.forClass(Proposal.class);
					DetachedCriteria payorderdc = DetachedCriteria.forClass(Payorder.class);
					DetachedCriteria agprofitdc = DetachedCriteria.forClass(AgProfit.class);
					DetachedCriteria agprofitBetdc = DetachedCriteria.forClass(AgProfit.class);
					proposaldc.add(Restrictions.eq("loginname", loginname));
					payorderdc.add(Restrictions.eq("loginname", loginname));
					proposalSavedc.add(Restrictions.eq("loginname", loginname));
					agprofitdc.add(Restrictions.eq("loginname", loginname));
					agprofitBetdc.add(Restrictions.eq("loginname", loginname));
					
					payorderdc.add(Restrictions.eq("flag", 0));
					proposalSavedc.add(Restrictions.eq("flag", 2));
//					proposaldc.add(Restrictions.eq("flag", 2));
					Calendar calendar2=Calendar.getInstance();
					calendar2.add(Calendar.MONTH, -1); 
					calendar2.set(Calendar.DAY_OF_MONTH,1);
					calendar2.set(Calendar.HOUR_OF_DAY, 0);
					calendar2.set(Calendar.MINUTE, 0);
					calendar2.set(Calendar.SECOND, 0);
					//calendar2.add(Calendar.DAY_OF_MONTH, -1);
					
					proposaldc.add(Restrictions.ge("createTime",calendar2.getTime() ));
					payorderdc.add(Restrictions.ge("createTime",calendar2.getTime() ));
					proposalSavedc.add(Restrictions.ge("createTime",calendar2.getTime() ));
					agprofitdc.add(Restrictions.ge("createTime",calendar2.getTime() ));
					agprofitBetdc.add(Restrictions.ge("createTime",calendar2.getTime() ));
					//System.out.println(calendar2.getTime());
					calendar2.add(Calendar.MONTH, 1); 
					calendar2.set(Calendar.HOUR_OF_DAY, 23);
					calendar2.set(Calendar.MINUTE, 59);
					calendar2.set(Calendar.SECOND, 59);
					calendar2.add(Calendar.DAY_OF_MONTH, -1);
					proposaldc.add(Restrictions.le("createTime",calendar2.getTime() ));
					payorderdc.add(Restrictions.le("createTime",calendar2.getTime() ));
					proposalSavedc.add(Restrictions.le("createTime",calendar2.getTime() ));
					agprofitdc.add(Restrictions.le("createTime",calendar2.getTime() ));
					agprofitBetdc.add(Restrictions.le("createTime",calendar2.getTime() ));
					/********************************************************/
					/**
					 * 计算代理下线活跃会员人数
					 */
					
					proposalSavedc.add(Restrictions.eq("type", 502));
					proposalSavedc.setProjection(Projections.sum("amount"));
					List saveList = this.getTaskDao().findByCriteria(proposalSavedc);
					agprofitBetdc.setProjection(Projections.sum("bettotal"));
					List betList = this.getTaskDao().findByCriteria(agprofitBetdc);
					payorderdc.setProjection(Projections.sum("money"));
					List payorderList = this.getTaskDao().findByCriteria(payorderdc);
					
					if((saveList!=null && !saveList.isEmpty() && null!=saveList.get(0) 
							|| payorderList!=null && !payorderList.isEmpty() && null!=payorderList.get(0))
							&& betList!=null && !betList.isEmpty() && null!=betList.get(0)){
						Double dpay = 0.00;
						if(null!=payorderList.get(0)){
							dpay = (Double)payorderList.get(0);
						}
						Double dsave = 0.00;
						if(null!=saveList.get(0)){
							dsave = (Double)saveList.get(0);
						}
						Double dbet = (Double)betList.get(0);
						//当月累积存款金额大于等于500元，且当月活跃投注额大于等于1000元，即为活跃会员
						if(500<=dsave+dpay && 300<=dbet){
							activeUser++;
						}
					}
					/********************************************************/
					//System.out.println(calendar2.getTime());
//					proposaldc.add(Restrictions.ne("type", 502));
					proposaldc.add(Restrictions.ne("type", 503));
					//System.out.println(proposaldc.toString());
					List<Proposal> proposals=this.getTaskDao().findByCriteria(proposaldc);
					List<AgProfit> agProfits=this.getTaskDao().findByCriteria(agprofitdc);
					CommissionrecordsId commissionrecordsId=new CommissionrecordsId(loginname, year, month);
					Commissionrecords commissionrecords=new Commissionrecords();
					commissionrecords.setId(commissionrecordsId);
					commissionrecords.setParent(string);
					commissionrecords.setCashinAmount(0.0);
					commissionrecords.setCashoutAmount(0.0);
					commissionrecords.setRemark("");
					commissionrecords.setXimaAmount(0.0);
					commissionrecords.setFirstDepositAmount(0.0);
					commissionrecords.setOtherAmount(0.0);
					commissionrecords.setAgAmount(0.0);
					for (Proposal proposal : proposals) {
						if(proposal.getFlag() == 2){
							//去掉六合彩的各种优惠以及反水
							/*if(null != proposal.getRemark()){
								if(proposal.getRemark().toLowerCase().contains("sixlottery") || proposal.getRemark().contains("六合彩") || proposal.getType()==619){
									continue ;
								}
							}*/
							if(proposal.getType()==505){
								commissionrecords.setFirstDepositAmount(commissionrecords.getFirstDepositAmount()+proposal.getAmount());
								amount+=proposal.getAmount();
							}else if (proposal.getType()==507 ||proposal.getType()==517 ||proposal.getType()==611||proposal.getType()==612||proposal.getType()==613||proposal.getType()==614||proposal.getType()==615||proposal.getType()==616||proposal.getType()==617||proposal.getType()==618 ||proposal.getType()==619 ) {
								commissionrecords.setXimaAmount(commissionrecords.getXimaAmount()+proposal.getAmount());
								amount+=proposal.getAmount();
							}else if (proposal.getType()==509 || proposal.getType()==506|| proposal.getType()==518|| proposal.getType()==513) {
								commissionrecords.setOtherAmount(commissionrecords.getOtherAmount()+proposal.getAmount());
								amount+=proposal.getAmount();
							}else if (proposal.getType()==531 || proposal.getType()==532|| proposal.getType()==533|| proposal.getType()==534|| proposal.getType()==535 || proposal.getType()==536 || proposal.getType()==537 || proposal.getType()==571 || proposal.getType()==572 || proposal.getType()==573 || proposal.getType()==581 || proposal.getType()==582 || proposal.getType()==583 || proposal.getType()==584|| proposal.getType()==590 || proposal.getType()==591 || proposal.getType()==592 || proposal.getType()==701 || proposal.getType() == 771 || proposal.getType() == 772 || proposal.getType() == 773 || proposal.getType() == 774 || proposal.getType() == 775 || proposal.getType() == 776 || proposal.getType() == 777 || proposal.getType() == 778|| proposal.getType() == 779) {
								if(proposal.getExecuteTime()!=null){
									//时间格式
									SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd"); 
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
									//执行时间
									Date executeTime = format.parse(fmat.format(proposal.getExecuteTime())+" 00:00:00");
									//本月第一天
									Calendar c = Calendar.getInstance();    
							        c.add(Calendar.MONTH, 0);
							        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
							        Date firstMonday = format.parse(format.format(c.getTime())+" 00:00:00");
							        //时间相见
							        long timeOld=executeTime.getTime();
							        long timeNow=firstMonday.getTime();
									long d=(timeNow-timeOld)/(1000*60*60*24);//化为天
							        if(d>0){
//							        	System.out.println(proposal.getLoginname()+"*******"+format.format(proposal.getExecuteTime()));
							        	commissionrecords.setOtherAmount(commissionrecords.getOtherAmount()+proposal.getGifTamount());
										amount+=proposal.getGifTamount();
							        }
								}
							}else if(proposal.getType()==502){
								//去掉秒存的优惠0.5%
								//FIXME
								DetachedCriteria cashinDc = DetachedCriteria.forClass(Cashin.class);
								cashinDc.add(Restrictions.eq("pno", proposal.getPno()));
								List<Cashin> cashIns = this.getTaskDao().findByCriteria(cashinDc) ;
								if(null != cashIns && cashIns.size()==1){
									Cashin in = cashIns.get(0);
									commissionrecords.setOtherAmount(commissionrecords.getOtherAmount()+in.getFee());
									amount += in.getFee(); 
								}
								
							}
						}
						if(proposal.getType() == 519 && (proposal.getFlag() == 0 || proposal.getFlag() == 1 || proposal.getFlag() == 2)){
							commissionrecords.setOtherAmount(commissionrecords.getOtherAmount()+proposal.getAmount());
							amount+=proposal.getAmount();
						}
					}
					
					for(AgProfit agProfit: agProfits){
						/*if(agProfit.getPlatform().equals("agin")||agProfit.getPlatform().equals("ag")||agProfit.getPlatform().equals("bbin")||agProfit.getPlatform().equals("keno")||agProfit.getPlatform().equals("keno2")||agProfit.getPlatform().equals("pt")||agProfit.getPlatform().equals("sb")||agProfit.getPlatform().equals("newpt")||agProfit.getPlatform().equals("ebet")){
							commissionrecords.setAgAmount(commissionrecords.getAgAmount()+agProfit.getAmount());
							agamount+=agProfit.getAmount();
						}
						//六合彩是按照投注额来计算佣金，不管输赢
						if(agProfit.getPlatform().equals("sixlottery")){
							sixlottery = sixlottery + agProfit.getBettotal() ;
						}*/
						if(!agProfit.getPlatform().equals("ea")){
							if(agProfit.getPlatform().equals("newpt") || agProfit.getPlatform().equals("ttg")|| agProfit.getPlatform().equals("gpi")){
								commissionrecords.setAgAmount(commissionrecords.getAgAmount()+agProfit.getAmount());
								agamount+=agProfit.getAmount();
								sloatmachine += agProfit.getAmount();
							}else{
								commissionrecords.setAgAmount(commissionrecords.getAgAmount()+agProfit.getAmount());
								agamount+=agProfit.getAmount();
								liveall += agProfit.getAmount();
							}
						}
					}
					commissionrecords.setSixLotteryBet(sixlottery);
					sixlotterySum += sixlottery ;
					this.getProposalService().save(commissionrecords);
				}
				commissions.setActiveuser(activeUser);
				/*Double commissionAmount = 0.00 + Math.round((sumplatform - amount) * 30 / 100);
				commissions.setCrate(0.3);*/
				
				
				Double sumplatform = 0.00;
				Double commissionAmount = 0.00;
				sumplatform = commissions.getEaProfitAmount()+agamount;
				
				liveall += commissions.getEaProfitAmount();
				commissions.setRemark(commissions.getRemark()+"(slotmachine:"+sloatmachine+",liveall:"+liveall+")");
				if(sloatmachine>0){
					sumplatform = sumplatform - sloatmachine*0.15 ;
				}
				if(liveall>0){
					sumplatform = sumplatform - liveall*0.1 ;
				}
				/**
				 * 佣金比例分配
				 * 合作伙伴(代理)等级 	当月总输赢(所有平台) 	当月最少活跃会员数 	佣金比例%
						第一级 			1-30万(包括30万) 		5个或5个以上 		30%
						第二级 			30万以上-60万(包括60万) 	15个或15个以上 	35%
						第三级 			60万以上-90万(包括90万) 	30个或30个以上 	40%
						第四级 			90万以上 				80个或80个以上 	45%
				 */
				if(((sumplatform - amount) <= 0.0 && activeUser>0) || ((sumplatform - amount)>0.0 && activeUser==0) || ((sumplatform - amount)<=0.0 && activeUser==0)){
					commissionAmount = 0.00 + Math.round((sumplatform - amount) * 30 / 100);
					commissions.setCrate(0.3);
				}
				if (activeUser >= 1 && activeUser < 15) {
					commissionAmount = 0.00 + Math.round((sumplatform - amount) * 30 / 100);
					commissions.setCrate(0.3);
				}
				if (activeUser >= 15 && activeUser < 40) {
					if ((sumplatform - amount) > 0 && (sumplatform - amount) <= 300000) {
						commissionAmount = 0.00 + Math.round((sumplatform - amount) * 30 / 100);
						commissions.setCrate(0.3);
					} else if ((sumplatform - amount) > 300000) {
						commissionAmount = 0.00 + Math.round((sumplatform - amount) * 35 / 100);
						commissions.setCrate(0.35);
					}
				}
				if (activeUser >= 40 && activeUser < 80) {
					if ((sumplatform - amount) > 0 && (sumplatform - amount) <= 300000) {
						commissionAmount = 0.00 + Math.round((sumplatform - amount) * 30 / 100);
						commissions.setCrate(0.3);
					} else if ((sumplatform - amount) > 300000 && (sumplatform - amount) <= 1000000) {
						commissionAmount = 0.00 + Math.round((sumplatform - amount) * 35 / 100);
						commissions.setCrate(0.35);
					} else if ((sumplatform - amount) > 1000000) {
						commissionAmount = 0.00 + Math.round((sumplatform - amount) * 40 / 100);
						commissions.setCrate(0.4);
					}
				}
				if (activeUser >= 80) {
					if ((sumplatform - amount) > 0 && (sumplatform - amount) <= 300000) {
						commissionAmount = 0.00 + Math.round((sumplatform - amount) * 30 / 100);
						commissions.setCrate(0.3);
					} else if ((sumplatform - amount) > 300000 && (sumplatform - amount) <= 1000000) {
						commissionAmount = 0.00 + Math.round((sumplatform - amount) * 35 / 100);
						commissions.setCrate(0.35);
					} else if ((sumplatform - amount) > 1000000 && (sumplatform - amount) <= 2000000) {
						commissionAmount = 0.00 + Math.round((sumplatform - amount) * 40 / 100);
						commissions.setCrate(0.4);
					} else if ((sumplatform - amount) > 2000000) {
						commissionAmount = 0.00 + Math.round((sumplatform - amount) * 45 / 100);
						commissions.setCrate(0.45);
					}
				}
				commissions.setAmount(commissionAmount+sixlotterySum*0.001);  //总额洗码*0.1%
				java.text.DecimalFormat   df = new   java.text.DecimalFormat("#.##");
				betNum = Double.valueOf(df.format(betNum));
				commissions.setRemark(df.format(betNum)); //remark用来存放投注额度
				this.getProposalService().save(commissions);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			this.closeFile();
		}
		
		return msg;
	}
	
	public String addLiveGameCommissions(String executetime) {
		//输赢记录是查询今天执行的
		String date1 = DateUtil.getchangedDateStr(1, executetime); //所在天的第二天
		Date dateNow = DateUtil.parseDateForYYYYmmDD(executetime) ;
		Date dateBefore = DateUtil.parseDateForYYYYmmDD(DateUtil.getchangedDateStr(-1, executetime)) ;
		
		String sql = "select agent ,platform  ,sum(amount) profitall ,sum(bettotal) betall from agprofit"
				+ " where agent != '' and createTime>='"+executetime+"' and createTime<='"+date1+"' group by agent , platform " ;
		
		Session session = proposalDao.getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(sql);
		List list = null;
		try {
			list = query.list() ;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("server error", e);
		}finally {
			session.close();
		}
		if(null == list || list.size() == 0){
			return "今天没有产生新的输赢数据--》" ;
		}
		//查询当天没有输赢记录但是提案里面有前一天的系统洗码
		//漏掉的老虎机系统洗码
//		String sqlSlot = "select p.agent ,'slotmachine' platform ,0 profitall ,0 betall from proposal p  where p.agent!='' and p.agent is not null  and p.createTime>= '"+DateUtil.getchangedDateStr(-1, executetime)+"' and p.createTime<= '"+executetime+"' and (p.type=701 or (p.type=507 and p.remark in('ptother系统洗码;执行:','PTTIGER系统洗码;执行:','ttg系统洗码;执行:','GPI系统洗码;执行:')))  and p.agent not in(select a.agent from agprofit a where a.createTime>='"+executetime+"' and a.createTime<='"+date1+"' and a.platform in('newpt','gpi','ttg')) group by p.agent;" ;
		//String sqlSlot = "select p.agent ,'slotmachine' platform ,0 profitall ,0 betall from proposal p  where p.agent!='' and p.agent is not null  and p.createTime>= '"+DateUtil.getchangedDateStr(-1, executetime)+"' and p.createTime<= '"+executetime+"' and (p.type in(505,506,508,509,513,616,622,623,624.625,560,518,519,520,536,401,402,403,404,405,406,407,408,409,410,411,412,413,414,419,420,421,422,423,424,425,426,427,428,429,590,591,598,599,701,702,703,704,705,706,707,708,709,710,711,712,555,390,391) or (p.type=507 and p.remark in('ptother系统洗码;执行:','PTTIGER系统洗码;执行:','ttg系统洗码;执行:','GPI系统洗码;执行:','qt系统洗码;执行:','nt系统洗码;执行:','dt系统洗码;执行:')))  and p.agent not in(select a.agent from agprofit a where a.createTime>='"+executetime+"' and a.createTime<='"+date1+"' and a.platform in('newpt','gpi','ttg','qt','nt','aginslot','dt','aginfish')) group by p.agent;" ;
		String sqlSlot = "select p.agent ,'slotmachine' platform ,0 profitall ,0 betall from proposal p  where p.agent!='' and p.agent is not null  and p.createTime>= '"+DateUtil.getchangedDateStr(-1, executetime)+"' and p.createTime<= '"+executetime+"' group by p.agent;" ;
		
		Session slotSession = proposalDao.getSessionFactory().openSession();
		SQLQuery querySlot = slotSession.createSQLQuery(sqlSlot);
		List listSlot = null;
		try {
			listSlot = querySlot.list() ;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("server error", e);
		}finally {
			slotSession.close();
		}
		if(null != listSlot && listSlot.size() > 0){
			list.addAll(listSlot);
		}
		//漏掉的真人系统洗码
//		String sqlLive = "select p.agent ,'liveall' platform ,0 profitall ,0 betall  from proposal p  where p.type=507 and p.agent!='' and p.agent is not null  and p.createTime>= '"+DateUtil.getchangedDateStr(-1, executetime)+"' and p.createTime<= '"+executetime+"' and p.remark not in('ptother系统洗码;执行:','PTTIGER系统洗码;执行:','ttg系统洗码;执行:','GPI系统洗码;执行:','qt系统洗码;执行:','nt系统洗码;执行:') and p.agent not in(select a.agent from agprofit a where a.createTime>='"+executetime+"' and a.createTime<='"+date1+"' and a.platform in('ea','ag','agin','bbin','keno','keno2','sb','sixlottery','ebet','jc','n2live','ebetapp')) group by p.agent;" ;
		String sqlLive = "select p.agent ,'liveall' platform ,0 profitall ,0 betall  from proposal p  where p.type in (507,743,744,745) and p.agent!='' and p.agent is not null  and p.createTime>= '"+DateUtil.getchangedDateStr(-1, executetime)+"' and p.createTime<= '"+executetime+"' group by p.agent;" ;
		Session liveSession = proposalDao.getSessionFactory().openSession(); 
		SQLQuery queryLive = liveSession.createSQLQuery(sqlLive);
		List listLive = null;
		try {
			listLive = queryLive.list() ;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("server error", e);
		}finally {
			liveSession.close();
		}
		if(null != listLive && listLive.size() > 0){
			list.addAll(listLive);
		}
		//漏掉的体育系统洗码
		String sqlSports = "select p.agent ,'sports' platform ,0 profitall ,0 betall  from proposal p  where p.type=507 and p.agent != '' and p.agent is not null  and p.createTime>= '"+DateUtil.getchangedDateStr(-1, executetime)+"' and p.createTime<= '"+executetime+"' group by p.agent;" ;
		SQLQuery querySports = proposalDao.getSessionFactory().openSession().createSQLQuery(sqlSports);
		List listSports = querySports.list() ;
		if(null != listSports || listSports.size() > 0){
			list.addAll(listSports);
		}
		
		//漏掉的彩票系统洗码（暂时没反水，以后给反水记得打开此代码）
		/*String sqllottery = "select p.agent ,'lottery' platform ,0 profitall ,0 betall  from proposal p  where p.type=507 and p.agent = != '' and p.agent is not null  and p.createTime>= '"+DateUtil.getchangedDateStr(-1, executetime)+"' and p.createTime<= '"+executetime+"' group by p.agent;" ;
		SQLQuery querylottery = proposalDao.getSessionFactory().openSession().createSQLQuery(sqlLive);
		List listlottery = querylottery.list() ;
		if(null != listlottery || listlottery.size() > 0){
			list.addAll(listlottery);
		}*/

		//漏掉的PT奖池
		String starttime = DateUtil.getchangedDateStr(-1, executetime);
        String newptSql = "SELECT u.agent, SUM(a.PROGRESSIVE_FEE)" +
                "	FROM" +
                "	( " +
                "	SELECT LCASE(SUBSTRING(pt.PLAYERNAME,3)) loginname, SUM(pt.PROGRESSIVE_FEE) PROGRESSIVE_FEE " +
                "	FROM pt_data_new pt " +
                "	WHERE  date_format(pt.STARTTIME,'%Y-%m-%d') = '" + starttime + "'" +
                "	GROUP BY LCASE(SUBSTRING(pt.PLAYERNAME,3))" +
                "	) a, users u" +
                "	WHERE a.loginname = u.loginname" +
                "	GROUP BY u.agent";
		
		Session newPtSession = proposalDao.getSessionFactory().openSession();
		List<Object> newPtLists = null;
		try {
			SQLQuery newPtQuery = newPtSession.createSQLQuery(newptSql);
			newPtLists = newPtQuery.list();
		}catch(Exception e) {
			e.printStackTrace();
			log.error("server error", e);
		}finally {
			newPtSession.close();
		}
		DetachedCriteria proposaldc = DetachedCriteria.forClass(Proposal.class);
		proposaldc.add(Restrictions.isNotNull("agent"));
		proposaldc.add(Restrictions.gt("executeTime", dateBefore)) ;
		proposaldc.add(Restrictions.lt("executeTime", dateNow)) ;
		proposaldc.add(Restrictions.ne("type", 503));
		List<Proposal> proposals = proposalDao.findByCriteria(proposaldc);
		
		PtCommissions ptCommissions = null;
		List<PtCommissions> commLists = new ArrayList<PtCommissions>();
		
		Map<String, Double> pfMap = proposalDao.getPlatformFee();//各平台对应费率
		Map<String,Integer> AgentTypeMap = proposalDao.getAgentType(); //各代理对应代理类型
		
		for (Object object : list) {
			Object[] objects = (Object[])object;
			log.info("代理账号：" + objects[0] );
			String agent = objects[0].toString();
			String platform = objects[1].toString() ;
			
			ptCommissions  = new PtCommissions();
			//佣金
			PtCommissionRecords commissionRecord = new PtCommissionRecords(new PtCommissionsId(agent , dateBefore , platform)) ;
			commissionRecord.setProfitall(Double.parseDouble(objects[2].toString()));
			commissionRecord.setBetall(Double.parseDouble(objects[3].toString()));
			
			List<String> slotMachines = Arrays.asList(new String[]{"newpt" , "ttg","gpi","qt","nt","aginslot","dt","aginfish","mg","png","mwg","ptsky","swfish","hyg","761","bbinele","kyqp","yoplay","aginfish"});
			List<String> liveCasino = Arrays.asList(new String[]{"ea","ag","agin","bbin","keno","keno2","sixlottery","ebet","jc","n2live","ebetapp","bbinvid","vrlive"});
			List<String> sportsCasino = Arrays.asList(new String[]{"sba","pb","fanya"});//体育
			List<String> lotteryCasino = Arrays.asList(new String[]{"og","vr","bit"});//彩票
			
			//内部代理seo 按实际平台收取
			//判断是否seo
			Double platformfee = 0.0;
			Integer type = AgentTypeMap.get(agent);
			if(type!=null &&type == 1){
				Double fee = pfMap.get(platform);
				if(fee != null){
					platformfee = Double.parseDouble(objects[2].toString())*fee;
				}else{
					return platform+"平台不存在，无法计算平台费，请联系技术添加！";
				}
			}else{
				if(slotMachines.contains(platform)|| platform.equals("slotmachine")){
					platformfee = Double.parseDouble(objects[2].toString())*0.15;
				}else if(liveCasino.contains(platform)|| platform.equals("liveall")){
					platformfee = Double.parseDouble(objects[2].toString())*0.1;
				}else if(sportsCasino.contains(platform) || platform.equals("sports")){
					platformfee = Double.parseDouble(objects[2].toString())*0.1;
				}else if(lotteryCasino.contains(platform)|| platform.equals("lottery")){
					platformfee = Double.parseDouble(objects[2].toString())*0.1;
				}
			}
			ptCommissions.setPlatformfee(platformfee);
			
			PtCommissionsId id  = null ; 
			if(slotMachines.contains(platform) || platform.equals("slotmachine")){
				id  = new PtCommissionsId(agent , dateBefore , "slotmachine");
			}else if(liveCasino.contains(platform) || platform.equals("liveall")){
				id  = new PtCommissionsId(agent , dateBefore , "liveall");
			}else if(sportsCasino.contains(platform) || platform.equals("sports")){
				id  = new PtCommissionsId(agent , dateBefore , "sports");
			}else if(lotteryCasino.contains(platform) || platform.equals("lottery")){
				id  = new PtCommissionsId(agent , dateBefore , "lottery");
			}else {
				log.info(platform+"没有被添加上！！！");
				return null;
			}
			
			ptCommissions.setId(id);
			ptCommissions.setProfitall(Double.parseDouble(objects[2].toString()));
			ptCommissions.setBetall(Double.parseDouble(objects[3].toString()));
			ptCommissions.setFlag(0);
			ptCommissions.setCreateTime(DateUtil.convertToTimestamp(new Date()));
			
			Double couponfee = 0.0 ;
			Double ximafee = 0.0 ;
			Proposal proposal = null ;
			for (int i = 0; i < proposals.size(); i++) {
				proposal = proposals.get(i) ;
				if (proposal.getAgent().equals(agent)) {
					if(id.getPlatform().equals("slotmachine")){  //老虎机
						//优惠
						if(proposal.getFlag() == 2){  
							if(proposal.getType() == 512 ||proposal.getType() == 590 || proposal.getType() == 591 || proposal.getType() == 598 || proposal.getType() == 599|| proposal.getType() == 600 || proposal.getType() == 701 || proposal.getType() == 702 || proposal.getType() == 703|| proposal.getType() == 704|| proposal.getType() == 705|| proposal.getType() == 706|| proposal.getType() == 707|| proposal.getType() == 708|| proposal.getType() == 709 || proposal.getType() == 710|| proposal.getType() == 711|| proposal.getType() == 712 || proposal.getType() == 571 
									|| proposal.getType() == 572 || proposal.getType() == 573 || proposal.getType() == 574 || proposal.getType() == 575 
									|| proposal.getType() == 401 || proposal.getType() == 402 || proposal.getType() == 403 || proposal.getType() == 404	|| proposal.getType() == 405|| proposal.getType() == 406|| proposal.getType() == 407|| proposal.getType() == 408|| proposal.getType() == 409|| proposal.getType() == 410|| proposal.getType() == 411|| proposal.getType() == 412|| proposal.getType() == 413|| proposal.getType() == 414|| proposal.getType() == 419
									|| proposal.getType() == 422 || proposal.getType() == 423 || proposal.getType() == 424 || proposal.getType() == 425 || proposal.getType() == 426 || proposal.getType() == 427 || proposal.getType() == 428 || proposal.getType() == 429
									|| proposal.getType() == 730 || proposal.getType() == 731 || proposal.getType() == 732 || proposal.getType() == 733 || proposal.getType() == 734 || proposal.getType() == 735
									|| proposal.getType() == 771 || proposal.getType() == 772 || proposal.getType() == 773 || proposal.getType() == 774 || proposal.getType() == 775 || proposal.getType() == 776 || proposal.getType() == 777 || proposal.getType() == 778 || proposal.getType() == 779
									|| proposal.getType() == 430 || proposal.getType() == 431 || proposal.getType() == 432 || proposal.getType() == 433 || proposal.getType() == 434 || proposal.getType() == 435 || proposal.getType() == 436 || proposal.getType() == 437
									|| proposal.getType() == 791 || proposal.getType() == 319 || proposal.getType() == 101 || proposal.getType() == 792 || proposal.getType() == 793 || proposal.getType() == 222){ //优惠
								couponfee += proposal.getGifTamount() ;
							}
							//反水
							else if( proposal.getType() == 616 || proposal.getType() == 617 || proposal.getType() == 622 || proposal.getType() == 623 ||proposal.getType() == 624 ||proposal.getType() == 625 || proposal.getType() == 628 || proposal.getType() == 629 || proposal.getType() == 630 ||proposal.getType() == 632 ||proposal.getType() == 632 ||
									(proposal.getType() == 507 && (proposal.getRemark().equals("ptother系统洗码;执行:") || proposal.getRemark().equals("PTTIGER系统洗码;执行:") || proposal.getRemark().toLowerCase().contains("bbinele")||proposal.getRemark().toLowerCase().contains("ttg") || proposal.getRemark().toLowerCase().contains("gpi")|| proposal.getRemark().toLowerCase().contains("qt")|| proposal.getRemark().toLowerCase().contains("nt") || proposal.getRemark().toLowerCase().contains("mg")|| proposal.getRemark().toLowerCase().contains("dt")|| proposal.getRemark().toLowerCase().contains("aginslot")|| proposal.getRemark().toLowerCase().contains("png")|| proposal.getRemark().toLowerCase().contains("ptsky")|| proposal.getRemark().toLowerCase().contains("mwg")||proposal.getRemark().toLowerCase().contains("bbinele")|| proposal.getRemark().toLowerCase().contains("swfish")|| proposal.getRemark().toLowerCase().contains("hyg")))){  //洗码
								ximafee += proposal.getAmount();
							}
						}
						
						if(((proposal.getType() == 519 ||  proposal.getType() == 520 || proposal.getType() == 560) && (proposal.getFlag() == 0 || proposal.getFlag() == 1 || proposal.getFlag() == 2)) || proposal.getType() == 420|| proposal.getType() == 442 || proposal.getType() == 421|| proposal.getType() == 555|| proposal.getType() == 390 || proposal.getType() == 391|| proposal.getType() == 499 || proposal.getType() == 513){
							couponfee += proposal.getAmount() ;
						}
					}else if(id.getPlatform().equals("liveall")){
						if(proposal.getFlag() == 2){
							// 优惠
							if (proposal.getType() == 537 || proposal.getType() == 531|| proposal.getType() == 532|| proposal.getType() == 533|| proposal.getType() == 534|| proposal.getType() == 535
									|| proposal.getType() == 592 || proposal.getType() == 593 || proposal.getType() == 594|| proposal.getType() == 595 || proposal.getType() == 596 || proposal.getType() == 597
							|| proposal.getType() == 743|| proposal.getType() == 744|| proposal.getType() == 745) { // 优惠
								couponfee += proposal.getGifTamount();
							}
							// 反水
							else if (proposal.getType() == 517  ||proposal.getType() == 611  ||proposal.getType() == 612 ||proposal.getType() == 613 ||proposal.getType() == 614 ||proposal.getType() == 615 ||proposal.getType() == 618||proposal.getType() == 619||proposal.getType() == 620||proposal.getType() == 621
									|| (proposal.getType() == 507 && (proposal.getRemark().equals("agin系统洗码;执行:")||proposal.getRemark().equals("bbinvid系统洗码;执行:") || proposal.getRemark().equals("n2live系统洗码;执行:")||proposal.getRemark().equals("ebetapp系统洗码;执行:")||proposal.getRemark().equals("ea系统洗码;执行:")))) { // 洗码
								ximafee += proposal.getAmount();
							}
						}
					}else if(id.getPlatform().equals("sports")){
						if (proposal.getType() == 507 && (proposal.getRemark().equals("sba系统洗码;执行:") || proposal.getRemark().equals("PB系统洗码;执行:")|| proposal.getRemark().equals("fanya系统洗码;执行:"))) { // 洗码
							ximafee += proposal.getAmount();
						}
					}else if(id.getPlatform().equals("lottery")){
						if (proposal.getType() == 507 && proposal.getRemark().equals("og系统洗码;执行:")) { // 洗码
							ximafee += proposal.getAmount();
						}
					}
				}
			}
			commissionRecord.setCouponfee(couponfee);
			commissionRecord.setXimafee(ximafee);
			commissionRecord.setCreateTime(DateUtil.convertToTimestamp(new Date()));
			try {
				proposalDao.save(commissionRecord);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
			ptCommissions.setCouponfee(couponfee);
			ptCommissions.setXimafee(ximafee);
			
			Double percent = 0.3;
			
			Userstatus userstatus = (Userstatus) proposalDao.get(Userstatus.class, agent);
						
			if (null != userstatus) {
				if (StringUtils.isNotBlank(userstatus.getCommission()) && id.getPlatform().equals("slotmachine")) {
				
					percent = Double.parseDouble(userstatus.getCommission());
				}
				if (userstatus.getLiverate() !=null && id.getPlatform().equals("liveall")) {
					
					percent = userstatus.getLiverate();
				}
				if (userstatus.getSportsrate()!=null && id.getPlatform().equals("sports")) {
					
					percent = userstatus.getSportsrate();
				}
				if (userstatus.getLotteryrate()!=null && id.getPlatform().equals("lottery")) {
					
					percent = userstatus.getLotteryrate();
				}
			}
						
			ptCommissions.setPercent(percent); //佣金比例
			
			ptCommissions.setHistoryfee(queryAgentCommissionsAmountHistory(agent));
			
			commLists.add(ptCommissions) ;
		}
		maps = new HashMap<String, String>() ;
		convertAndSaveCommissions(commLists , "slotmachine" , proposals , newPtLists);
		convertAndSaveCommissions(commLists , "liveall" , proposals , newPtLists);
		convertAndSaveCommissions(commLists , "sports" , proposals , newPtLists);
		convertAndSaveCommissions(commLists , "lottery" , proposals , newPtLists);

		return "执行成功" ;
	}
	
	private  Map<String , String>  maps = null ;
	
	
	//查询历史佣金总额
	public Double queryAgentCommissionsAmountHistory(String agent){
		Double amount = 0.0 ;
		DetachedCriteria dc = DetachedCriteria.forClass(PtCommissions.class);
		dc.add(Restrictions.eq("id.agent", agent));
		dc.setProjection(Projections.sum("amount"));
		List sum = proposalDao.findByCriteria(dc) ;
		
		if(null != sum && !sum.isEmpty() && sum.size() ==1 && sum.get(0)!=null){
			amount += Double.valueOf(sum.get(0).toString());
		}
		DetachedCriteria dc1 = DetachedCriteria.forClass(Commissions.class);
		dc1.add(Restrictions.eq("id.loginname", agent));
		dc1.setProjection(Projections.sum("amount"));
		List sum1 = proposalDao.findByCriteria(dc1) ;
		if(null != sum1 && !sum1.isEmpty() && sum1.size() ==1 && sum1.get(0)!=null){
			amount += Double.valueOf(sum1.get(0).toString());
		}
		return amount ;
	}
	@Override
	public String systemXimaForBBin(File file, String gamekind) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * @param commLists
	 * @param platform  slotmachine / liveall
	 */
	@Override
	public void convertAndSaveCommissions(List<PtCommissions> commLists , String platform , List<Proposal> proposals,List<Object> newPtLists){
		List<PtCommissions> lists = new ArrayList<PtCommissions>();
		
		for (PtCommissions ptCommissions : commLists) {
			if(!ptCommissions.getId().getPlatform().equals(platform)){
				continue ;
			}
			String agent = ptCommissions.getId().getAgent() ;
			//判断lists是否有该代理
			PtCommissions old = null ;
			for (PtCommissions comm : lists) {
				if(agent.equals(comm.getId().getAgent()) && ptCommissions.getId().getPlatform().equals(platform)){
					old = comm ;
				}
			}
			if(null != old){
				lists.remove(old) ;
				old.setProfitall(old.getProfitall()+ptCommissions.getProfitall());
				old.setBetall(old.getBetall()+ptCommissions.getBetall());
				old.setCouponfee(old.getCouponfee());
				old.setXimafee(old.getXimafee());
				old.setPlatformfee(old.getPlatformfee()+ptCommissions.getPlatformfee());//平台费
				lists.add(old) ;
			}else{
				lists.add(ptCommissions);
			}
		}
		
		for (PtCommissions ptCommissions : lists) {
			//公共部分优惠在这里扣掉,包括存款优惠
			Double couponfee = 0.0 ;
			Double depositfee= 0.0;
			if(null == maps.get(ptCommissions.getId().getAgent())){
				for (Proposal proposal : proposals) {
					if(!proposal.getAgent().equals(ptCommissions.getId().getAgent()) || proposal.getFlag() != 2){
						continue ;
					}
					if(proposal.getType() == 506 || proposal.getType() == 509 ||  proposal.getType() == 518){
						couponfee += proposal.getAmount() ;
						maps.put(ptCommissions.getId().getAgent(), platform) ;
					}else if(proposal.getType()==502){
						//去掉秒存的优惠0.5%
						DetachedCriteria cashinDc = DetachedCriteria.forClass(Cashin.class);
						cashinDc.add(Restrictions.eq("pno", proposal.getPno()));
						List<Cashin> cashIns = this.getTaskDao().findByCriteria(cashinDc) ;
						if(null != cashIns && cashIns.size()==1){
							Cashin in = cashIns.get(0);
							depositfee += in.getFee(); 
							maps.put(ptCommissions.getId().getAgent(), platform) ;
						}
					}
				}
			}
			ptCommissions.setCouponfee(ptCommissions.getCouponfee() + couponfee);
			ptCommissions.setDepositfee(ptCommissions.getDepositfee()+depositfee);
			
			/*Double platformfee = 0.0  ;
			if(ptCommissions.getId().getPlatform().equals("liveall")){
				platformfee = ptCommissions.getProfitall()*0.1;
			}else if(ptCommissions.getId().getPlatform().equals("slotmachine")){
				platformfee =ptCommissions.getProfitall()*0.15;
			}
			
			ptCommissions.setPlatformfee(platformfee);*/
			Double platformfee = ptCommissions.getPlatformfee();
			Double progressive_bets = 0.0;//扣除Pt奖池赢得
			if("slotmachine".equals(platform)){
				for (Object object : newPtLists) {
					Object[] objects = (Object[])object;
					if(objects[0] != null){
						String agent = objects[0].toString();
						if(StringUtil.equals(agent, ptCommissions.getId().getAgent())){
							progressive_bets = (Double) objects[1];
							break;
						}
					}
				}
			}
			ptCommissions.setProgressive_bets(progressive_bets);
			
			
			Double amount = (ptCommissions.getProfitall()-platformfee-ptCommissions.getXimafee() - ptCommissions.getCouponfee() - progressive_bets - ptCommissions.getDepositfee())*ptCommissions.getPercent();
			ptCommissions.setAmount(amount);
			
			try {
				proposalDao.save(ptCommissions);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	@Override
	public void excuteCommissions() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String addPhone(File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addAgTry(File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String autoAddXimaPtProposal(List<PtProfit> ptProfit) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addPtCoupon(File file, String loginname) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String systemXimaForKg(File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addAgentPhone(File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String systemXimaForGf(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addMail(File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String systemXimaForAgSlot(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<XimaDataVo> excelToPNGVo(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bean4Xima> excelToPtSkyVo(File file) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Bean4Xima> excelToDTFishVo(File file) {
		// TODO Auto-generated method stub
		return null;
	}

}
