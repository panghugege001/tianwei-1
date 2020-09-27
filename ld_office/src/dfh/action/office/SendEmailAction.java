package dfh.action.office;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



import java.util.Random;

import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;











import dfh.action.SubActionSupport;
import dfh.model.Sendemail;
import dfh.service.interfaces.OperatorService;
import dfh.service.interfaces.ProposalService;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.StringUtil;
import dfh.utils.sendemail.SendEmailWs;
import dfh.utils.sendemail.SendEmailWsByEdm;

public class SendEmailAction extends SubActionSupport {

	private static Logger log = Logger.getLogger(SendEmailAction.class);
	private static final int BUFFER_SIZE = 16 * 1024;
	private ProposalService proposalService;
	private OperatorService operatorService;
	private String email;
	private Integer pageIndex;
	private Integer size;
	private String ids;
	private String zt;
	private String remark;
	private String errormsg;
	private String fileName;
	private String excelFileName;
	private File myFile;
	private Sheet sheet;
	private String sendflag;
	private String emailflag;
	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public OperatorService getOperatorService() {
		return operatorService;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public String getSendflag() {
		return sendflag;
	}

	public void setSendflag(String sendflag) {
		this.sendflag = sendflag;
	}

	public String getEmailflag() {
		return emailflag;
	}

	public void setEmailflag(String emailflag) {
		this.emailflag = emailflag;
	}
	

	/**
	 * 查询Sendemail
	 * @return
	 */
	public String getSendemail(){
		DetachedCriteria dc = DetachedCriteria.forClass(Sendemail.class);
		if (StringUtils.isNotEmpty(email)) {
			dc.add(Restrictions.eq("email", email));
		}
		if (StringUtils.isNotEmpty(emailflag)) {
			dc.add(Restrictions.eq("emailflag",emailflag));
		}
		if (StringUtils.isNotEmpty(sendflag)) {
			dc.add(Restrictions.eq("sendflag",sendflag)); 
		}
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	/**
	 * 邮件群发的方法
	 * @return
	 */
	public String youjianBySelf() {  
		System.out.println("邮件发送开始---");
		DetachedCriteria dc = DetachedCriteria.forClass(Sendemail.class);
		dc.add(Restrictions.eq("sendflag", "1"));
		dc.add(Restrictions.in("emailflag",Arrays.asList(new String[]{"0","1","2","3"})));
		List<Sendemail> list = proposalService.findByCriteria(dc,0,1);
		if(null!=list&&list.size()>0){
			Sendemail sendemail = list.get(0);
			String str="true";
			/*if(sendemail.getEmailflag().equals("2")){// 需要检测
				str=SendEmailWsByEdm.checkemailFlag(sendemail.getEmail());
				if("true".equals(str)){
					sendemail.setEmailflag("1");
				}else{
					sendemail.setEmailflag("0");
				}
			}else if(sendemail.getEmailflag().equals("1")||sendemail.getEmailflag().equals("3")){
				str="true";
			}*/
			if("true".equals(str)){
				StringBuffer btStr=new StringBuffer();//标题随机汉字
				for(int i=0;i<3;i++){
					btStr=btStr.append(getRandomHan());
				}
				/*StringBuffer  nrSbf=new StringBuffer();
				for(int i=0;i<500;i++){//内容随机汉字
					nrSbf=nrSbf.append(getRandomHan());
					if(i%50==0)
					nrSbf.append(",");
				}*/
				boolean b=SendEmailWs.sendEmailsBySelf(sendemail.getEmail(),sendemail.getBt()/*+RandomUtils.nextDouble()*/,sendemail.getNr());
				System.out.println("邮件发送结束---"+sendemail.getEmail()); 
				if(b){
					sendemail.setSendflag("2");
				}else{
					sendemail.setSendflag("3");	
				}
			}else{
			}
			proposalService.saveOrUpdate(sendemail);
		}
		return null;
	}
	
	private Object[] Object(String string, String string2, String string3,
			String string4) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 更新邮件群发信息
	 * @return
	 */
	public String updateSendemail() {
		PrintWriter out = null;
		String[] idNu = ids.split(",");
		DetachedCriteria dc = DetachedCriteria.forClass(Sendemail.class);
		dc.add(Restrictions.in("id", StringUtil.strArray2intArray(idNu)));
		List<Sendemail> list = proposalService.findByCriteria(dc);
		for (int i = 0; i < list.size(); i++) {
			Sendemail sendemail = list.get(i);
			sendemail.setSendflag("1");
			sendemail.setBt(zt);
			sendemail.setNr(remark);
			operatorService.saveOrUpdate(sendemail);
		}
		try {
			out = this.getResponse().getWriter();
			out.println("审核成功，已加入发送队列");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	/**
	 * 导入新邮件地址
	 * @return
	 */
	public String  addEmail() {
		try {
			if (null == fileName || fileName.equals("")) {
				setErrormsg("请先提交文件");
				return INPUT;
			}
			String filehouzhui = getExtention(fileName);
			System.out.println(filehouzhui);
			if (!filehouzhui.equals(".xls")) {
				setErrormsg("文件格式必须是excel");
				return INPUT;
			}
			excelFileName = new Date().getTime() + getExtention(fileName);
			File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
			copy(myFile, file);
			InputStream stream = new FileInputStream(myFile.toString());
			Workbook wb = Workbook.getWorkbook(stream);
			if (wb == null) {
				log.info("打开文件失败");
				setErrormsg("打开文件失败");
				return INPUT;
			}
			sheet = wb.getSheet(0); // 取得工作表
			int rows = sheet.getRows(); // 行数
			int cols = sheet.getColumns();// 列数
			try {
				for (int i = 0; i < rows; i++) {
					String stringEmail=this.getStringValue(i, 0);
					String stringUserName="";
					if(cols>1){
					 stringUserName=this.getStringValue(i, 1);
					}
					Sendemail sd = new Sendemail();
					sd.setEmail(stringEmail);
					sd.setUsername(stringUserName);
					sd.setEmailflag("2");
					sd.setSendflag("0");
					proposalService.save(sd);
				}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setErrormsg("保存成功");
		return INPUT;
	}
	
	private static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
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
	
	private static void copy(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		private Random ran = new Random();
	    private final static int delta = 0x9fa5 - 0x4e00 + 1;
	     /**
	      * 随机文字
	      * @return
	      */
	    public char getRandomHan() {
	        return (char)(0x4e00 + ran.nextInt(delta)); 
	    }

	    
	    public void setMyFileFileName(String fileName) { 
			this.fileName = fileName;
		}
}
