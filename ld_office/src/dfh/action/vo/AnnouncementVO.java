package dfh.action.vo;

import java.io.Serializable;

public class AnnouncementVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7724236554396222674L;
	
	private Integer id;
	private String type;
	private String content;
	private String createtime;
	private String title;
	
	public AnnouncementVO() {
		// TODO Auto-generated constructor stub
	}
	
	public AnnouncementVO(Integer id,String createtime,String title){
		this.id=id;
		this.createtime=createtime;
		this.title=title;
	}
	
	public AnnouncementVO(Integer id,String content,String createtime,String title){
		this.id=id;
		this.content=content;
		this.createtime=createtime;
		this.title=title;
	}
	
	public AnnouncementVO(Integer id,String type,String content,String createtime,String title){
		this.id=id;
		this.type=type;
		this.content=content;
		this.createtime=createtime;
		this.title=title;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


}
