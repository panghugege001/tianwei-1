package dfh.service.implementations;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ibm.icu.text.Collator;

import dfh.action.vo.FirstCashinUsresVO;
import dfh.action.vo.FirstCashinUsresVoOfTotal;
import dfh.dao.LogDao;
import dfh.dao.ProposalDao;
import dfh.dao.UserDao;
import dfh.model.Creditlogs;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.service.interfaces.IFirstCashinUsres;
import dfh.utils.DateUtil;

public class FirstCashinUsres implements IFirstCashinUsres {
	
	private Logger log=Logger.getLogger(FirstCashinUsres.class);
	private DecimalFormat df=new DecimalFormat("##,###.00");
	private UserDao userdao;
	private ProposalDao proposalDao;
	private String msg;
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx=new FileSystemXmlApplicationContext("D:\\Workspaces\\MyEclipse 8.x\\e68\\application\\Ea_office\\WebRoot\\WEB-INF\\applicationContext.xml");
		IFirstCashinUsres f=(IFirstCashinUsres) ctx.getBean("firstCashinUsres");
		
	}
	
	public List getUserList(String loginname){
		// TODO Auto-generated method stub
		Users user = this.getUsers(loginname);
		if (user==null) {
			return null;
		}else{
			// exec query
			Object o = proposalDao.getProposalObject(loginname);
			if (o==null) {  // 存款记录为空，users表isCashin标志为0。
				this.msg="额度记录有误，会员账号："+loginname;
				log.error("额度记录有误，会员账号："+loginname);
				return null;
			}else{
				FirstCashinUsresVoOfTotal total=null;
				List list=new ArrayList();
				Proposal c=(Proposal) o;
				try {
					FirstCashinUsresVO vo = this.toVO(c,user,proposalDao.getFistConcessionsUsers(user.getLoginname()));
					list.add(vo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("根据给定用户查询首存数据时，发生异常", e);
					this.msg="系统异常，请重新尝试或与技术联系";
					return null;
				}
				
				return list;
			}
		}
	}
	
	
	private List getvalue(String oneDate, String twoDate,int offset,int length,String checktype,String checkcontent)throws Exception{
		if (oneDate==null||oneDate.trim().equals("")) {
			oneDate="1900-01-01";
		}
		if (twoDate==null||twoDate.trim().equals("")) {
			twoDate=DateUtil.fmtDateForBetRecods(new Date());
		}
		List firstCashinUsers=null;
		if (checktype!=null&&!checktype.trim().equals("")) {
			checktype=checktype.trim();
			if(checktype.equals("1")){
				// 查询指定账号的首存数据
				if (checkcontent==null||checkcontent.equals("")) {
					this.msg="请输入真钱账号";
					return null;
				}
				return this.getUserList(checkcontent);
			}else if(checktype.equals("0")) {
				// 查询全部会员类型的首存用户
				firstCashinUsers = proposalDao.getFirstCashinUsers(oneDate, twoDate, offset, length);
			}else{
				Users user=null;
				checkcontent=checkcontent.trim();
				if (checktype.equals("2")) {
					// 通过代理账号检索
					if (checkcontent==null||checkcontent.equals("")) {
						this.msg="请输入代理账号";
						return null;
					}
					Object object = userdao.get(Users.class, checkcontent);
					if (object==null) {
						this.msg="您输入的代理账号不存在，请重新输入";
						return null;
					}else{
						user=(Users) object;
					}
				}else if (checktype.equals("3")) {
					if (checkcontent==null||checkcontent.equals("")) {
						this.msg="请输入代理网址";
						return null;
					}
					user=userdao.getAgentByWebsite(checkcontent);
					if (user==null) {
						this.msg="您输入的代理网址，尚未注册；请重新输入";
						return null;
					}
				}
				firstCashinUsers=proposalDao.getFirstCashinUsers(oneDate, twoDate, offset, length, user.getLoginname());
			}
		}else{
			this.msg="检索类型不可为空";
			return null;
		}
		if (firstCashinUsers==null||firstCashinUsers.size()<=0) {
			this.msg="检索范围内不包含首存会员，请重新输入检索条件";
		}
		return firstCashinUsers;
		
	}
	
	

	public List getUserList(String oneDate, String twoDate,int pageno,int length,String checktype,String checkcontent) {
		// TODO Auto-generated method stub
		try {
			int offset=(pageno-1)*length;
			List firstCashinUsers = this.getvalue(oneDate, twoDate, offset, length, checktype, checkcontent);	// 得到指定时间范围内的会员首次存款数据
			if (firstCashinUsers==null) {
				return null;
			}else{
				if(checktype!=null&&!checktype.trim().equals("")&&checktype.equals("1")) //指定账号查询
					return firstCashinUsers;
				
				List names = this.getNames(firstCashinUsers);
				List users = userdao.getUsers(names);										// 根据给定的会员名集合，得到其相关的所有会员数据
				List fistConcessionsUsers = proposalDao.getFistConcessionsUsers(names);		// 根据给定的会员名集合，得到其相关的所有首存优惠数据
				
				// 组合vo数据 
//				List recvList=this.toVO(firstCashinUsers, users, fistConcessionsUsers,null);
				//Collections.sort(recvList, new FirstCashinUsersComparator()); // first cashin time  asc .
				return this.toVO(firstCashinUsers, users, fistConcessionsUsers,null);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			this.msg="查询失败，请重新尝试";
			log.error("查询首次存款用户信息时，发生异常。", e);
			return null;
		}
	}
	
	
	
	
	
	private List toVO(List firstCashinUsers,List users,List fistConcessionsUsers,FirstCashinUsresVoOfTotal total){
		List voList=new ArrayList();
		while(firstCashinUsers.size()!=0) {
			FirstCashinUsresVO vo=null;
			Proposal proposal=(Proposal) firstCashinUsers.remove(0);
			for (int j = 0; j < users.size(); j++) {
				Users user=(Users) users.get(j);
				if (proposal.getLoginname().equalsIgnoreCase(user.getLoginname())) {
					// 得到首次存款会员数据
					vo =new FirstCashinUsresVO(user.getLoginname(), user.getAliasName(),user.getAccountName(),
							proposal.getAmount(),
							DateUtil.fmtDateForBetRecods(new Date(user.getCreatetime().getTime())),
							DateUtil.fmtDateForBetRecods(new Date(proposal.getCreateTime().getTime())), 
							user.getReferWebsite());
					vo.setHowToKnow(user.getHowToKnow());
					
					if (fistConcessionsUsers==null||fistConcessionsUsers.size()<=0) {
						vo.setConcessionsAmount(0d);
					}else{
						for (int k = 0; k < fistConcessionsUsers.size(); k++) {
							Proposal concessionsUsers=(Proposal) fistConcessionsUsers.get(k);
							if (user.getLoginname().equalsIgnoreCase(concessionsUsers.getLoginname())) {
								// 得到会员首存优惠金额
								vo.setConcessionsAmount(concessionsUsers.getAmount());
								fistConcessionsUsers.remove(concessionsUsers);
								break;
							}
						}
						
					}
					users.remove(user);
					break;
				}
			}
			if (vo!=null) {
				voList.add(vo);
				if (total!=null) {
					total.setFirstAmount(vo.getFirstAmount());
					total.setConcessionsAmount(vo.getConcessionsAmount());
					total.setPersonCount(1);
				}
				
			}
			
		}
		if (total!=null) {
			total.setUserList(voList);
		}
		return voList;
	}
	
	
	
	
	
	
	
	
	
	
	private List getNames(List firstCashinUsers){
		int size = firstCashinUsers.size();
		List<String> names=new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			Proposal proposal=(Proposal) firstCashinUsers.get(i);
			names.add(proposal.getLoginname());
		}
		return names;
	}
	
	
	
	
	
	
	
	private Users getUsers(String loginname){
		Object o = userdao.get(Users.class, loginname);
		if (o==null) {
			this.msg="该账号不存在";
			return null;
		}else{
			Users user=(Users) o;
			if(user.getFlag()==1){
				this.msg="该账号已被禁用";
				return null;
			}else if (user.getIsCashin()==1) {
				this.msg="该会员没有存款记录";
				return null;
			}else{
				return user;
			}
		}
	}
	
	
	
	
	
	
	private FirstCashinUsresVO toVO(Proposal proposal,Users user,Proposal concessionsUsers){
		FirstCashinUsresVO vo = new FirstCashinUsresVO();
			vo.setLoginName(user.getLoginname());
			vo.setAliasName(user.getAliasName());
			vo.setAccountName(user.getAccountName());
			vo.setFirstAmount(proposal.getAmount());
			vo.setRegisterTime(DateUtil.fmtDateForBetRecods(new Date(user.getCreatetime().getTime())));
			vo.setFirstTime(DateUtil.fmtDateForBetRecods(new Date(proposal.getCreateTime().getTime())));
			//vo.setSpaceDays(String.valueOf((proposal.getCreateTime().getTime()-user.getCreatetime().getTime())/(1000*60*60*24)));
			vo.setReferWebsite(user.getReferWebsite());
			vo.setHowToKnow(user.getHowToKnow());
			if (concessionsUsers!=null) {
				vo.setConcessionsAmount(concessionsUsers.getAmount());
			}
		return vo;
	}
	

	
	
	
	
	
	public FirstCashinUsresVoOfTotal getTotalPageNO(String oneDate, String twoDate,String checktype,String checkcontent) {
		// TODO Auto-generated method stub
		try{
			List firstCashinUsers = (List) this.getvalue(oneDate, twoDate, 0, 99999, checktype, checkcontent);
			if (firstCashinUsers==null||firstCashinUsers.size()==0) {
				return null;
			}else{
				FirstCashinUsresVoOfTotal total=new FirstCashinUsresVoOfTotal();
				if(checktype!=null&&!checktype.trim().equals("")&&checktype.equals("1")) {
					FirstCashinUsresVO vo=(FirstCashinUsresVO) firstCashinUsers.get(0);
					total.setUserList(firstCashinUsers);
					total.setPersonCount(1);
					total.setFirstAmount(vo.getFirstAmount());
					total.setConcessionsAmount(vo.getConcessionsAmount());
				}else{
					List names = this.getNames(firstCashinUsers);
					List users = userdao.getUsers(names);										
					List fistConcessionsUsers = proposalDao.getFistConcessionsUsers(names);	
					total.setUserList(this.toVO(firstCashinUsers, users, fistConcessionsUsers, total));
				}
					
				return total;
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.msg="查询失败";
			log.error("查询指定范围内的首存用户数量时，发生异常。", e);
			return null;
		}
	}
	
	
	
	
	
	
	public ProposalDao getProposalDao() {
		return proposalDao;
	}

	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}
	
	public UserDao getUserdao() {
		return userdao;
	}

	public void setUserdao(UserDao userdao) {
		this.userdao = userdao;
	}

	
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return msg;
	}


}

class FirstCashinUsersComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		FirstCashinUsresVO vo1=(FirstCashinUsresVO) o1;
		FirstCashinUsresVO vo2=(FirstCashinUsresVO) o2;
		Collator collator=Collator.getInstance(Locale.CHINA);
		return collator.compare(vo1.getFirstTime(), vo2.getFirstTime());
	}
	
}

