package dfh.action.customer;

import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import dfh.action.SubActionSupport;

/**
 * 
 * @author sun
 * 
 */
public class OfficeImageAction extends SubActionSupport {
	
	
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
	
	private String image001path;
	private String image002path;
	private String image003path;
	private String image004path;
	
	
	public String uploadImage() {
		PrintWriter out = null;
		try {
			ServletActionContext.getRequest().setCharacterEncoding("UTF-8");
			String imagepath = ServletActionContext.getServletContext().getRealPath("");
			// 首页小图片
			if (image001 != null) {
				File savedir = new File(imagepath + "/" + image001path.replaceAll(getExtensionName(image001path), ""));
				if (!savedir.getParentFile().exists()) {
					savedir.getParentFile().mkdirs();
				}
				File savefile = new File(savedir, image001FileName);
				FileUtils.copyFile(image001, savefile);
			}
			// 活动条
			if (image002 != null) {
				File savedir = new File(imagepath + "/" + image002path.replaceAll(getExtensionName(image002path), ""));
				if (!savedir.getParentFile().exists()) {
					savedir.getParentFile().mkdirs();
				}
				File savefile = new File(savedir, image002FileName);
				FileUtils.copyFile(image002, savefile);
			}
			// 活动大图
			if (image003 != null) {
				File savedir = new File(imagepath + "/" + image003path.replaceAll(getExtensionName(image003path), ""));
				if (!savedir.getParentFile().exists()) {
					savedir.getParentFile().mkdirs();
				}
				File savefile = new File(savedir, image003FileName);
				FileUtils.copyFile(image003, savefile);
			}
			// 首页轮播
			if (image004 != null) {
				File savedir = new File(imagepath + "/" + image004path.replaceAll(getExtensionName(image004path), ""));
				if (!savedir.getParentFile().exists()) {
					savedir.getParentFile().mkdirs();
				}
				File savefile = new File(savedir, image004FileName);
				FileUtils.copyFile(image004, savefile);
			}
			out = this.getResponse().getWriter();
			out.print("200");
			out.flush();
		} catch (Exception e) {
			if (out != null) {
				out.print("300");
				out.flush();
			} else {
				try {
					out = this.getResponse().getWriter();
					out.print("300");
					out.flush();
				} catch (Exception e1) {
					System.out.println(e1.toString());
				}
			}
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('/');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot);
			}
		}
		return filename;
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
	public String getImage001path() {
		return image001path;
	}
	public void setImage001path(String image001path) {
		this.image001path = image001path;
	}
	public String getImage002path() {
		return image002path;
	}
	public void setImage002path(String image002path) {
		this.image002path = image002path;
	}
	public String getImage003path() {
		return image003path;
	}
	public void setImage003path(String image003path) {
		this.image003path = image003path;
	}
	public String getImage004path() {
		return image004path;
	}
	public void setImage004path(String image004path) {
		this.image004path = image004path;
	}
	
	
	
}
