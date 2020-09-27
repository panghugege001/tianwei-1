package dfh.model;

/**
 * 郵件內容
 * @author jad
 *
 */
public class Yjnr {
	
	private String id;//邮件模板ID
	
	private String name;//邮件模板名称
	
	private String subject;//邮件标题
	
	private String updated;
	
	private String created;
	
	private String folderId;
	
	private String tracking;
	
	private String bodyHtml;
	
	private String bodyText;
	
	
//	private String content;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	/*public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}*/

	public String getTracking() {
		return tracking;
	}

	public void setTracking(String tracking) {
		this.tracking = tracking;
	}

	public String getBodyHtml() {
		return bodyHtml;
	}

	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	public String getBodyText() {
		return bodyText;
	}

	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}
	
	
}
