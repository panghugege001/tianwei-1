package dfh.action.office;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import zzp.common.net.HttpRequest;
import zzp.common.net.HttpResponse;
import zzp.common.net.Method;

import dfh.action.SubActionSupport;
import dfh.model.HdImage;
import dfh.service.interfaces.ProposalService;
import dfh.utils.Page;
import dfh.utils.PageQuery;

/**
 * 
 * @author sun
 * 
 */
public class OfficeImageAction extends SubActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 331603000178571132L;

	private static String imageips = "122.152.174.56:6116、218.213.197.108:6116、218.213.197.80:6116";

	private ProposalService proposalService;
	
	private String imageName;
	private Date startTime;
	private Date endTime;
	private Integer imageStatus;
	private String serviceIp;
	private String remark;
	private String errormsg;
	private Integer pageIndex;// 当前页
	private Integer size;// 每页显示的行数

	private File image001; // 上传的文件
	private String image001FileName; // 文件名称
	private String image001ContentType; // 文件类型

	private File image002; // 上传的文件
	private String image002FileName; // 文件名称
	private String image002ContentType; // 文件类型

	private File image003; // 上传的文件
	private String image003FileName; // 文件名称
	private String image003ContentType; // 文件类型

	private File image004; // 上传的文件
	private String image004FileName; // 文件名称
	private String image004ContentType; // 文件类型

	private HdImage hdImage = new HdImage();

	public String uploadImage() {
		try {
			ServletActionContext.getRequest().setCharacterEncoding("UTF-8");
			String imagePath = ServletActionContext.getServletContext().getRealPath("");
			HdImage hdImage = new HdImage();
			// 首页小图片
			if (image001 != null) {
				String image001path = "/hdImage/home";
				File savedir = new File(imagePath + "" + image001path);
				if (!savedir.getParentFile().exists()) {
					savedir.getParentFile().mkdirs();
				}
				String imageName = UUID.randomUUID().toString() + getExtensionName(image001FileName);
				File savefile = new File(savedir, imageName);
				FileUtils.copyFile(image001, savefile);
				hdImage.setImage001(image001path + "/" + imageName);
				hdImage.setImageType(0);
			}
			// 活动条
			if (image002 != null) {
				String image002path = "/hdImage/pro";
				File savedir = new File(imagePath + "" + image002path);
				if (!savedir.getParentFile().exists()) {
					savedir.getParentFile().mkdirs();
				}
				String imageName = UUID.randomUUID().toString() + getExtensionName(image002FileName);
				File savefile = new File(savedir, imageName);
				FileUtils.copyFile(image002, savefile);
				hdImage.setImage002(image002path + "/" + imageName);
				hdImage.setImageType(0);
			}
			// 活动大图
			if (image003 != null) {
				String image003path = "/hdImage/detail";
				File savedir = new File(imagePath + "" + image003path);
				if (!savedir.getParentFile().exists()) {
					savedir.getParentFile().mkdirs();
				}
				String imageName = UUID.randomUUID().toString() + getExtensionName(image003FileName);
				File savefile = new File(savedir, imageName);
				FileUtils.copyFile(image003, savefile);
				hdImage.setImage003(image003path + "/" + imageName);
				hdImage.setImageType(0);
			}
			// 首页轮播
			if (image004 != null) {
				String image004path = "/hdImage/flash";
				File savedir = new File(imagePath + "" + image004path);
				if (!savedir.getParentFile().exists()) {
					savedir.getParentFile().mkdirs();
				}
				String imageName = UUID.randomUUID().toString() + getExtensionName(image004FileName);
				File savefile = new File(savedir, imageName);
				FileUtils.copyFile(image004, savefile);
				hdImage.setImage004(image004path + "/" + imageName);
				hdImage.setImageType(1);
			}
			if (imageName != null && !imageName.equals("")) {
				hdImage.setImageName(imageName);
			}
			hdImage.setImageStatus(0);
			hdImage.setImageIp(imageips);
			hdImage.setRemark(remark);
			hdImage.setCreatedate(new Timestamp(new Date().getTime()));
			if (startTime != null) {
				hdImage.setImageStart(startTime);
			}
			if (endTime != null) {
				hdImage.setImageEnd(endTime);
			}
			proposalService.getHibernateTemplate().save(hdImage);
			setErrormsg("保存成功");
		} catch (Exception e) {
			setErrormsg("保存失败");
		}
		return "input";
	}

	public String queryImage() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(HdImage.class);
			if (startTime != null)
				dc = dc.add(Restrictions.le("imageStart", startTime));
			if (endTime != null)
				dc = dc.add(Restrictions.gt("imageEnd", endTime));
			if (StringUtils.isNotEmpty(imageName)) {
				dc = dc.add(Restrictions.like("imageName", "%" + imageName + "%"));
			}
			if (imageStatus != null) {
				dc = dc.add(Restrictions.eq("imageStatus", imageStatus));
			}
			Order o = Order.desc("createdate");
//			dc = dc.addOrder(Order.desc("createdate"));
			Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());

		}
		return INPUT;
	}

	public String updateImagePage() {
		hdImage = (HdImage) proposalService.getHibernateTemplate().find("FROM HdImage WHERE id=" + hdImage.getId()).get(0);
		startTime = hdImage.getImageStart();
		endTime = hdImage.getImageEnd();
		imageName = hdImage.getImageName();
		remark=hdImage.getRemark();
		if (hdImage.getImageType() != null && hdImage.getImageType()==1) {
			return "imageSwfUpdate";
		}
		return "imageUpdate";
	}

	public String updateImage() {
		try {
			hdImage = (HdImage) proposalService.getHibernateTemplate().find("FROM HdImage WHERE id=" + hdImage.getId()).get(0);
			if (hdImage != null) {
				Boolean f=false;
				if (startTime != null) {
					hdImage.setImageStart(startTime);
				}
				if (endTime != null) {
					hdImage.setImageEnd(endTime);
				}
				if (imageName != null && !imageName.equals("")) {
					hdImage.setImageName(imageName);
				}
				if (remark != null && !remark.equals("")) {
					hdImage.setRemark(remark);
				}
				ServletActionContext.getRequest().setCharacterEncoding("UTF-8");
				String imagePath = ServletActionContext.getServletContext().getRealPath("");
				// 首页小图片
				if (image001 != null) {
					String image001path = "/hdImage/home";
					File savedir = new File(imagePath + "" + image001path);
					if (!savedir.getParentFile().exists()) {
						savedir.getParentFile().mkdirs();
					}
					String imageName = UUID.randomUUID().toString() + getExtensionName(image001FileName);
					File savefile = new File(savedir, imageName);
					FileUtils.copyFile(image001, savefile);
					hdImage.setImage001(image001path + "/" + imageName);
					hdImage.setImageType(0);
					f=true;
				}
				// 活动条
				if (image002 != null) {
					String image002path = "/hdImage/pro";
					File savedir = new File(imagePath + "" + image002path);
					if (!savedir.getParentFile().exists()) {
						savedir.getParentFile().mkdirs();
					}
					String imageName = UUID.randomUUID().toString() + getExtensionName(image002FileName);
					File savefile = new File(savedir, imageName);
					FileUtils.copyFile(image002, savefile);
					hdImage.setImage002(image002path + "/" + imageName);
					hdImage.setImageType(0);
					f=true;
				}
				// 活动大图
				if (image003 != null) {
					String image003path = "/hdImage/detail";
					File savedir = new File(imagePath + "" + image003path);
					if (!savedir.getParentFile().exists()) {
						savedir.getParentFile().mkdirs();
					}
					String imageName = UUID.randomUUID().toString() + getExtensionName(image003FileName);
					File savefile = new File(savedir, imageName);
					FileUtils.copyFile(image003, savefile);
					hdImage.setImage003(image003path + "/" + imageName);
					hdImage.setImageType(0);
					f=true;
				}
				// 首页轮播
				if (image004 != null) {
					String image004path = "/hdImage/flash";
					File savedir = new File(imagePath + "" + image004path);
					if (!savedir.getParentFile().exists()) {
						savedir.getParentFile().mkdirs();
					}
					String imageName = UUID.randomUUID().toString() + getExtensionName(image004FileName);
					File savefile = new File(savedir, imageName);
					FileUtils.copyFile(image004, savefile);
					hdImage.setImage004(image004path + "/" + imageName);
					hdImage.setImageType(1);
					f=true;
				}
				if(f){
					hdImage.setImageIp(imageips);
				}
				proposalService.getHibernateTemplate().merge(hdImage);
				setErrormsg("更新成功");
			}
		} catch (Exception e) {
			setErrormsg("更新失败");
		}
		return INPUT;
	}
	
	public String updateServiceImage() {
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			hdImage = (HdImage) proposalService.getHibernateTemplate().find("FROM HdImage WHERE id=" + hdImage.getId()).get(0);
			if (hdImage == null) {
				out.println("活动不存在！");
				out.flush();
				return null;
			}
			ServletActionContext.getRequest().setCharacterEncoding("UTF-8");
			String imagePath = ServletActionContext.getServletContext().getRealPath("");
			HttpRequest request = new HttpRequest("http://" + serviceIp + "/asp/uploadImage.aspx");
			request.setMethod(Method.POST);
			if (hdImage.getImage001() != null && !hdImage.getImage001().equals("")) {
				request.addParam("image001path", hdImage.getImage001());
				request.addFile("image001", new File(imagePath + "/" + hdImage.getImage001()));
			}
			if (hdImage.getImage002() != null && !hdImage.getImage002().equals("")) {
				request.addParam("image002path", hdImage.getImage002());
				request.addFile("image002", new File(imagePath + "/" + hdImage.getImage002()));
			}
			if (hdImage.getImage003() != null && !hdImage.getImage003().equals("")) {
				request.addParam("image003path", hdImage.getImage003());
				request.addFile("image003", new File(imagePath + "/" + hdImage.getImage003()));
			}
			if (hdImage.getImage004() != null && !hdImage.getImage004().equals("")) {
				request.addParam("image004path", hdImage.getImage004());
				request.addFile("image004", new File(imagePath + "/" + hdImage.getImage004()));
			}
			HttpResponse response = request.exeute();
			int httpCode = response.getHttpCode();
			if (httpCode == 200) {
//				String imageServiceIp = hdImage.getImageIp().replaceAll(serviceIp, "");
//				hdImage.setImageIp(imageServiceIp);
//				proposalService.getHibernateTemplate().merge(hdImage);
				out.println("更新成功");
			} else {
				out.println("更新失败,远程服务器无响应！错误代码："+httpCode);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("更新失败:" + e.getMessage().toString());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}
	
	public String updateImageStatusServiceImage(){
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			hdImage = (HdImage) proposalService.getHibernateTemplate().find("FROM HdImage WHERE id=" + hdImage.getId()).get(0);
			if (hdImage == null) {
				out.println("活动不存在！");
				out.flush();
				return null;
			}
			if(imageStatus!=null){
				hdImage.setImageStatus(imageStatus);
			}
			proposalService.getHibernateTemplate().merge(hdImage);
			out.println("更新成功");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("更新失败:" + e.getMessage().toString());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot);
			}
		}
		return filename;
	}

	public static String getImageips() {
		return imageips;
	}

	public static void setImageips(String imageips) {
		OfficeImageAction.imageips = imageips;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getImageStatus() {
		return imageStatus;
	}

	public void setImageStatus(Integer imageStatus) {
		this.imageStatus = imageStatus;
	}

	public String getServiceIp() {
		return serviceIp;
	}

	public void setServiceIp(String serviceIp) {
		this.serviceIp = serviceIp;
	}

	public File getImage001() {
		return image001;
	}

	public void setImage001(File image001) {
		this.image001 = image001;
	}

	public String getImage001FileName() {
		return image001FileName;
	}

	public void setImage001FileName(String image001FileName) {
		this.image001FileName = image001FileName;
	}

	public String getImage001ContentType() {
		return image001ContentType;
	}

	public void setImage001ContentType(String image001ContentType) {
		this.image001ContentType = image001ContentType;
	}

	public File getImage002() {
		return image002;
	}

	public void setImage002(File image002) {
		this.image002 = image002;
	}

	public String getImage002FileName() {
		return image002FileName;
	}

	public void setImage002FileName(String image002FileName) {
		this.image002FileName = image002FileName;
	}

	public String getImage002ContentType() {
		return image002ContentType;
	}

	public void setImage002ContentType(String image002ContentType) {
		this.image002ContentType = image002ContentType;
	}

	public File getImage003() {
		return image003;
	}

	public void setImage003(File image003) {
		this.image003 = image003;
	}

	public String getImage003FileName() {
		return image003FileName;
	}

	public void setImage003FileName(String image003FileName) {
		this.image003FileName = image003FileName;
	}

	public String getImage003ContentType() {
		return image003ContentType;
	}

	public void setImage003ContentType(String image003ContentType) {
		this.image003ContentType = image003ContentType;
	}

	public File getImage004() {
		return image004;
	}

	public void setImage004(File image004) {
		this.image004 = image004;
	}

	public String getImage004FileName() {
		return image004FileName;
	}

	public void setImage004FileName(String image004FileName) {
		this.image004FileName = image004FileName;
	}

	public String getImage004ContentType() {
		return image004ContentType;
	}

	public void setImage004ContentType(String image004ContentType) {
		this.image004ContentType = image004ContentType;
	}

	public HdImage getHdImage() {
		return hdImage;
	}

	public void setHdImage(HdImage hdImage) {
		this.hdImage = hdImage;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
