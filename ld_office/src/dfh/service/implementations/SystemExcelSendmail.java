package dfh.service.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import dfh.model.Proposal;
import dfh.model.PtCommissions;
import dfh.model.PtProfit;
import dfh.model.bean.Bean4Xima;
import dfh.model.bean.XimaDataVo;
import dfh.utils.Constants;

public class SystemExcelSendmail extends AbstractBatchGameinfoServiceImpl {
	
	private Logger log=Logger.getLogger(SystemExcelSendmail.class);
	
	private String msg;// 发送的内容可为HTML
	private String froEmail;// 发件人
	private String froEmailPassword;// 对应的邮箱密码
	private String emailServer;// EMAIL服务器地址
	private String emailPort;// 端口号
	private File file;
	private Sheet sheet = null;
	private Workbook wb = null;
	private InputStream stream = null;

	@Override
	public String autoAddCommissions(File file) throws Exception {
		log.info("start sendMailByAsynchronousMode email:");
		this.file=file;
		TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					send();
				} catch (Exception e) {
					log.info(e.getMessage());
				}
			}
		});
		return null;
	}
	@Override
	public String systemXimaForBBin(File file, String gamekind) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bean4Xima> excelToNTwoVo(File file) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 同步发送Email 可接爱多个收件人
	 */
	public void send() {
		try {
			this.froEmail="cs@qy8.cc";
			this.emailServer="youjian.qy8.cc";
			this.froEmailPassword="123456";
			this.msg="<P>亲爱的玩家</P>"
+"<P>您好：</P>"

+"<P>我们这里是千亿娱乐城，实力雄厚的亚洲博彩新贵。公司博彩牌照注册于菲律宾，是网络博彩委员会成员，有菲律宾政府根据严密指南所颁发的合法经营执照，同时也被网络博彩委员会指定有合法博彩牌照的网上娱乐场。您是可以在菲律宾唯一正规的网络注册网址www.firstcagayan.com查询到我们公司的牌照号码（056）的。</P>"

+"<P>在这寒冷的冬季，我们为新老玩家准备了多项优惠活动，相信总有一项适合您。您可以登录我们的网站www.qy8.cc进行浏览。目前我们的活动主要有: </P>"
+"<P> 一、首存优惠100元起送68%，最高可达6800元；</P>"
+" <P>二、再存优惠100元起送30%，最高可达3000元；</P>"
+"<P>三、返水最高达1%，天天结算，一周最高返8万；</P>"
+"<P>四、千亿推广王活动，奖金最高2888元；</P>"
+"<P> 五、使用全新自助存款，赠送1%存款优惠；</P>"
+"<P>六、提款5分钟到账，每日提款不限次数，最高每日可提款28万元</P>"

+"<P>我们一直本著以人为本，追求卓越品质的宗旨，拥有友好、高质和高效的客服团队，一年365天，一天24小时全天候提供实时的客户支援；配合先进严紧的技术人才，不断研发，创新和了解玩家的需要，并力求紧贴市场的脉搏，务求打造一个全世界最优质，最具趣味性，最安全而又最易操作的在线博彩娱乐互动名牌。</P>"

+"<P>只要您来到我们千亿娱乐城，我们的贴心服务一定会给您带来别样的感受。如果您有任何问题，可以随时联系我们的在线客服、QQ客服（QQ号码：938054984）或者拨打我们的400免费电话（电话号码：4000-689-380）。我们随时恭候您的光临。千亿娱乐祝您生活愉快。好运不间断！</P>"

+"<P>千亿娱乐客服</P>";
			Properties props = new Properties();
			props.put("mail.smtp.host", this.emailServer);
			props.put("mail.smtp.auth", "true");
			props.put("mail.transport.protocol","smtp");
			
			Session ssn = Session.getInstance(props);
			ssn.setDebug(true);
			
			stream = new FileInputStream(file.toString());
			wb = Workbook.getWorkbook(stream);
			if (wb == null) {
				log.info("打开文件失败");
				return;
			}
			sheet = wb.getSheet(0); // 取得工作表
			
			int rows = sheet.getRows(); // 行数
			int cols = sheet.getColumns();// 列数
			System.out.println(rows);
			Transport transport = ssn.getTransport("smtp");
			transport.connect(this.emailServer, this.froEmail, this.froEmailPassword);
//			
			InternetAddress fromAddress = new InternetAddress(froEmail);
			MimeMessage message = new MimeMessage(ssn);
			message.setFrom(fromAddress);
			for (int i = 0; i < rows; i++) {
				String email=this.getStringValue(i, 0);
				Thread.sleep(10000);
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(email.trim()));
				message.setSubject("十一月寒冬送温暖，千亿好礼送不停");
				message.setText(msg);
				message.setContent(msg, "text/html ;charset=" + Constants.DEFAULT_ENCODING);
				transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
				
			}
//			Transport transport = ssn.getTransport("smtp");
//			transport.connect(this.emailServer, this.froEmail, this.froEmailPassword);
			
			transport.close();
			wb.close();
			stream.close();
			log.info("stop send email!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String autoAddXimaProposal(File file, Double rate) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excuteAutoXimaProposal() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excuteCommissions() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateXimaStatus() throws Exception {
		// TODO Auto-generated method stub
		
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFroEmail() {
		return froEmail;
	}

	public void setFroEmail(String froEmail) {
		this.froEmail = froEmail;
	}

	public String getFroEmailPassword() {
		return froEmailPassword;
	}

	public void setFroEmailPassword(String froEmailPassword) {
		this.froEmailPassword = froEmailPassword;
	}

	public String getEmailServer() {
		return emailServer;
	}

	public void setEmailServer(String emailServer) {
		this.emailServer = emailServer;
	}

	public String getEmailPort() {
		return emailPort;
	}

	public void setEmailPort(String emailPort) {
		this.emailPort = emailPort;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
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
