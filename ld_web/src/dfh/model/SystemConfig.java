package dfh.model;

import javax.persistence.*;
import java.util.Date;


/**
 * 系统配置表
 * @author jat
 *
 */
@Entity
@Table(name="systemconfig"
    ,catalog="lbao"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class SystemConfig implements java.io.Serializable {


    // Fields    

     private String id;
     private String typeNo;//配置项目代码                           例如 ：type001   type001
     private String typeName;//配置项目名称                                    发件人                 发件人
     private String itemNo;//配置项的值对应的编码                        1         2
     private String value;//配置项的值                                               千亿1      千亿2
     private String note ;//备注
     private String flag;//是否禁用    是  否
     private String updateby;//更新人
     private Date updatetime;//更新时间


   
    // Property accessors
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @javax.persistence.Column(name = "id")
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    @javax.persistence.Column(name = "typeNo")
	public String getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}
	 @javax.persistence.Column(name = "typename")
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	 @javax.persistence.Column(name = "itemno")
	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	 @javax.persistence.Column(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	 @javax.persistence.Column(name = "note")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	 @javax.persistence.Column(name = "flag")
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
    
	@javax.persistence.Column(name = "updateby")
	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

	@javax.persistence.Column(name = "updatetime")
	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
    

}