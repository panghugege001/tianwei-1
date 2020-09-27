package dfh.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.SQLQuery;
import dfh.action.vo.FundStatisticsVO;

public class FundStatisticsDao extends UniversalDao {
	
	/**
	 * 根据指定时间获取存款资金来往明细（记录数、总额度、银行名称），按银行进行分组统计
	 * @param start
	 * @param end
	 * @return FundStatisticsVO
	 * @throws Exception
	 */
	public List<FundStatisticsVO> getCashinFundDetails(Date start,Date end,int offset,int length)throws Exception{
		String sql="select count(*) count,sum(p.amount),c.corpbankname from proposal p inner join cashin c on p.pno=c.pno where p.flag=2 and p.createtime >= ? and p.createtime < ? group by c.corpbankname order by count desc";
		SQLQuery q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, start).setParameter(1, end).setFirstResult(offset).setMaxResults(length);
		Object object = q.list();
		return getCashinOrCashoutList(object);
	}
	
	/**
	 * 获取指定银行、指定时间范围内的存款记录数、总额度和银行名称
	 * @param start
	 * @param end
	 * @param bankName
	 * @return FundStatisticsVO
	 * @throws Exception 
	 */
	public List<FundStatisticsVO> getCashinFundDetails(Date start,Date end,String bankName,int offset,int length)throws Exception{
		String sql="select count(*) count,sum(p.amount),c.corpbankname from proposal p inner join cashin c on p.pno=c.pno where p.flag=2 and p.createtime >= ? and p.createtime < ? and c.corpbankname=? order by count desc";
		SQLQuery q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, start).setParameter(1, end).setParameter(2, bankName).setFirstResult(offset).setMaxResults(length);
		Object object = q.uniqueResult();
		return getCashinOrCashoutList(object);
	}
	
	/**
	 * 根据指定时间获取提款资金来往明细（记录数、总额度、银行名称），按银行进行分组统计
	 * @param start
	 * @param end
	 * @return FundStatisticsVO
	 * @throws Exception
	 */
	public List<FundStatisticsVO> getCashoutFundDetails(Date start,Date end,int offset,int length)throws Exception{
		String sql="select count(*) count,sum(p.amount),c.bank from proposal p inner join cashout c on p.pno=c.pno where p.flag=2 and p.createtime >= ? and p.createtime < ? group by c.bank order by count desc";
		SQLQuery q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, start).setParameter(1, end).setFirstResult(offset).setMaxResults(length);
		Object object = q.list();
		return getCashinOrCashoutList(object);
	}

	/**
	 * 获取指定银行、指定时间范围内的提款记录数、总额度和银行名称
	 * @param start
	 * @param end
	 * @param bankName
	 * @return FundStatisticsVO
	 * @throws Exception 
	 */
	public List<FundStatisticsVO> getCashoutFundDetails(Date start,Date end,String bankName,int offset,int length)throws Exception{
		String sql="select count(*) count,sum(p.amount),c.bank from proposal p inner join cashout c on p.pno=c.pno where p.flag=2 and p.createtime >= ? and p.createtime < ? and c.bank=? order by count desc";
		SQLQuery q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, start).setParameter(1, end).setParameter(2, bankName).setFirstResult(offset).setMaxResults(length);
		Object object = q.uniqueResult();
		return getCashinOrCashoutList(object);
	}

	
	/**
	 * 查询指定时间范围内的存款总记录数、总额度
	 * @param start
	 * @param end
	 * @return FundStatisticsVO
	 * @throws Exception
	 */
	public FundStatisticsVO getCashinTotalReccord(Date start,Date end)throws Exception{
		String sql="select count(*),sum(p.amount) from proposal p inner join cashin c on p.pno=c.pno where p.flag=2 and p.createtime >= ? and p.createtime < ?";
		SQLQuery q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, start).setParameter(1, end);
		Object object = q.uniqueResult();
		return getTotalReccordVO(object);
	}
	
	
	/**
	 * 根据给的时间范围、银行名称，得到存款总记录数和总额度
	 * @param start
	 * @param end
	 * @param bankName
	 * @return
	 * @throws Exception
	 */
	public FundStatisticsVO getCashinTotalReccord(Date start,Date end,String bankName)throws Exception{
		String sql="select count(*),sum(p.amount) from proposal p inner join cashin c on p.pno=c.pno where p.flag=2 and p.createtime >= ? and p.createtime < ? and c.corpbankname=?";
		SQLQuery q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, start).setParameter(1, end).setParameter(2, bankName);
		Object object = q.uniqueResult();
		return getTotalReccordVO(object);
	}
	/**
	 * 查询指定时间范围内的提款总记录数、总额度
	 * @param start
	 * @param end
	 * @return FundStatisticsVO
	 * @throws Exception
	 */
	public FundStatisticsVO getCashoutTotalReccord(Date start,Date end)throws Exception{
		String sql="select count(*),sum(p.amount) from proposal p inner join cashout c on p.pno=c.pno where p.flag=2 and p.createtime >= ? and p.createtime < ?";
		SQLQuery q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, start).setParameter(1, end);
		Object object = q.uniqueResult();
		return getTotalReccordVO(object);
	}
	
	
	/**
	 * 根据给的时间范围、银行名称，得到总提款记录数和总额度
	 * @param start
	 * @param end
	 * @param bankName
	 * @return
	 * @throws Exception
	 */
	public FundStatisticsVO getCashoutTotalReccord(Date start,Date end,String bankName)throws Exception{
		String sql="select count(*),sum(p.amount) from proposal p inner join cashout c on p.pno=c.pno where p.flag=2 and p.createtime >= ? and p.createtime < ? and c.bank=?";
		SQLQuery q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, start).setParameter(1, end).setParameter(2, bankName);
		Object object = q.uniqueResult();
		return getTotalReccordVO(object);
	}
	

	private FundStatisticsVO getTotalReccordVO(Object object)throws Exception{
		if (object!=null) {
			Object[] result=(Object[]) object;
			return new FundStatisticsVO(((BigInteger)result[0]).intValue(),(Double)result[1]);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<FundStatisticsVO> getCashinOrCashoutList(Object object)throws Exception{
		List<FundStatisticsVO> fundDetail=null;
		if (object!=null) {
			fundDetail=new ArrayList<FundStatisticsVO>();
			if (object instanceof Object[]) {
				Object[] objects=(Object[]) object;
				int dataCount=((BigInteger)objects[0]).intValue();
				if (dataCount==0) {
					return null;
				}
				fundDetail.add(new FundStatisticsVO(dataCount, (Double)objects[1], (String)objects[2]));
			}else{
				List<Object[]> result=(List<Object[]>) object;
				for (int i = 0; i < result.size(); i++) {
					Object[] objects = result.get(i);
					int dataCount=((BigInteger)objects[0]).intValue();
					if (dataCount==0) {
						continue;
					}
					fundDetail.add(new FundStatisticsVO(((BigInteger)objects[0]).intValue(), (Double)objects[1], (String)objects[2]));
				}
			}
		}
		return fundDetail;
	}
	
	

}
