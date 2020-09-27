package dfh.service.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.ProfitVO;
import dfh.dao.ProposalDao;
import dfh.model.Proposal;
import dfh.model.PtCommissions;
import dfh.model.PtProfit;
import dfh.model.Users;
import dfh.model.Xima;
import dfh.model.bean.Bean4Xima;
import dfh.model.bean.XimaDataVo;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

public class SystemBatchAutoAddProfit extends AbstractBatchGameinfoServiceImpl  {
	
	private Logger log=Logger.getLogger(SystemBatchAutoAddProfit.class);
	private ProposalDao proposalDao;
	private Sheet sheet = null;
	private Workbook wb = null;
	private InputStream stream = null;
	
	@Override
	public String autoAddXimaProposal(File file,Double rate) throws Exception {
		// TODO Auto-generated method stub
		String msg=null;
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
		//Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
		stream = new FileInputStream(file.toString());
		wb = Workbook.getWorkbook(stream);
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		
		int rows = sheet.getRows(); // 行数
		int cols = sheet.getColumns();// 列数
		
		
		try {
			for (int i = 1; i < rows; i++) {
				String string=this.getStringValue(i, 0);
				//Users user = (Users) this.getUserDao().get(Users.class,string.substring(5,string.length()),LockMode.UPGRADE);
				Users user = (Users) this.getUserDao().get(Users.class,string,LockMode.UPGRADE);
				if (user==null) {
					log.info("用户："+string+"，不存在");
					continue;
				}
				if(user.getLevel()<2){
					log.info("用户："+string.substring(5,string.length())+"，等级未达到负盈利反赠优惠");
					continue;
				}
				if(this.getNumberValue(i, 1)>0){
					log.info("用户："+string+"，没有负盈利");
					continue;
				}
				DetachedCriteria proposaldc = DetachedCriteria.forClass(Proposal.class);
				proposaldc.add(Restrictions.eq("loginname", user.getLoginname()));
				proposaldc.add(Restrictions.eq("flag", 2));
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH,1);//
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				//calendar2.add(Calendar.DAY_OF_MONTH, -1);
				starttime=calendar.getTime();
				proposaldc.add(Restrictions.le("createTime",calendar.getTime() ));
				//System.out.println(calendar.getTime());
				calendar.add(Calendar.DAY_OF_MONTH, -31);
				//System.out.println(calendar.getTime());
				proposaldc.add(Restrictions.ge("createTime",calendar.getTime() ));
				proposaldc.add(Restrictions.ne("type", 502));
				proposaldc.add(Restrictions.ne("type", 503));
			//	System.out.println(proposaldc.toString());
				List<Proposal> proposals=this.getTaskDao().findByCriteria(proposaldc);
				Double amount=0.00;
				for (Proposal proposal : proposals){
					amount+=proposal.getAmount();
				}
				Double temp=amount+this.getNumberValue(i, 1);
				if(temp>=0){
					log.info("用户："+string.substring(5,string.length())+"，没有负盈利");
					continue;
				}
				
				//XimaVO ximaObject = new XimaVO(this.getNumberValue(i, 1),user.getLoginname(),rate);
				ProfitVO profitVO=new ProfitVO(user.getLoginname(), temp, user.getLevel());
				String remark = "负盈利反赠";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.PROFIT);
				log.info("正在处理提案号："+pno);
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "负盈利反赠", temp, profitVO.getProfitAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(calendar.getTime()), profitVO.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.PROFIT.getCode(), user.getLevel(), user.getLoginname(), profitVO.getProfitAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.getProposalService().save(xima);
				this.getProposalService().save(proposal);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			this.closeFile();
			file.delete();
		}
		
		return msg;
	}
	@Override
	public String systemXimaForBBin(File file, String gamekind) {
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
		DetachedCriteria proposaldc = DetachedCriteria.forClass(Proposal.class);
	//	proposaldc.add(Restrictions.eq("loginname", user.getLoginname()));
		proposaldc.add(Restrictions.eq("flag", 2));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,1);//
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		//calendar2.add(Calendar.DAY_OF_MONTH, -1);
		//starttime=calendar.getTime();
		proposaldc.add(Restrictions.le("createTime",calendar.getTime() ));
		System.out.println(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, -31);
		//System.out.println(calendar.getTime());
		proposaldc.add(Restrictions.ge("createTime",calendar.getTime() ));
		proposaldc.add(Restrictions.ne("type", 502));
		proposaldc.add(Restrictions.ne("type", 503));
		System.out.println(proposaldc);
	}

	@Override
	public String autoAddCommissions(File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Bean4Xima> excelToNTwoVo(File file) {
		// TODO Auto-generated method stub
		return null;
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
	public String addLiveGameCommissions(String executetime) {
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
	public void convertAndSaveCommissions(List<PtCommissions> commLists,
			String platform, List<Proposal> proposals, List<Object> newPtLists) {
		// TODO Auto-generated method stub
		 
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
