package dfh.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.Action;

import dfh.action.SubActionSupport;
import dfh.model.Bankinfo;
import dfh.model.Const;
import dfh.model.PayMerchant;
import dfh.model.SystemConfig;
import dfh.model.notdb.BankVo;
import dfh.service.interfaces.IBankinfoService;
import dfh.service.interfaces.ISystemConfigService;
import dfh.utils.GsonUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.RemoteUtils;

public class SystemConfigAction extends SubActionSupport {

	/**
	 * 对系统配置表的操作
	 */
	private static final long serialVersionUID = -1182716425426537063L;
	private Logger log = Logger.getLogger(SystemConfigAction.class);
	private ISystemConfigService systemConfigService;
	  private String id;
	  private String typeno;//配置项目代码                         
	  private String typename;//配置项目名称                            
	  private String itemno;//配置项的值对应的编码                  
	  private String value;//配置项的值                                          
	  private String note ;//备注
	  private Integer pageIndex;
	  private String flag;//是否禁用
	
	
	public String querySystemConfig(){
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if(!StringUtils.isEmpty(typeno)){
			dc.createCriteria("typeNo", typeno);
		}
		if(!StringUtils.isEmpty(typename)){
			dc.createCriteria("typeName", typename);
		}
		if(!StringUtils.isEmpty(itemno)){
			dc.createCriteria("itemNo", itemno);
		}
		Page page = PageQuery.queryForPagenation(systemConfigService.getHibernateTemplate(), dc, pageIndex, 50, null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	/**
	 * 新增或者修改商户号
	 * @return
	 */
	public String updateSystemConfig(){
		SystemConfig systemConfig=null;
		if(!StringUtils.isEmpty(id)){
			id=id.replace("ids", "");
			systemConfig=(SystemConfig) systemConfigService.get(SystemConfig.class, id);
		}
		if(null==systemConfig||systemConfig.getId()==null){
			systemConfig = new SystemConfig();
		}
		systemConfig.setItemNo(itemno);
		systemConfig.setNote(note);
		systemConfig.setTypeName(typename);
		systemConfig.setTypeNo(typeno);
		systemConfig.setValue(value);
		systemConfig.setFlag(flag);
		String a="";
		if(null!=systemConfig.getId()){
			systemConfigService.updateSystemConfig(systemConfig);
		}else {
			a=systemConfigService.saveSystemConfig(systemConfig);
		}
		PrintWriter out=null;
		try {
			out =this.getResponse().getWriter();
			out.println("操作成功##"+a);
			out.flush();
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			out.flush();
		}
		
		return null;
	}
	
	public void deletesystemConfig(){
		id=id.replace("ids", "");
		systemConfigService.deleteSystemConfig(SystemConfig.class, id);
		PrintWriter out=null;
		try {
			out =this.getResponse().getWriter();
			out.println("删除成功");
			out.flush();
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			out.flush();
		}
	}
	
	
	
	
	public ISystemConfigService getSystemConfigService() {
		return systemConfigService;
	}
	public void setSystemConfigService(ISystemConfigService systemConfigService) {
		this.systemConfigService = systemConfigService;
	}
	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeno() {
		return typeno;
	}
	public void setTypeno(String typeno) {
		this.typeno = typeno;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getItemno() {
		return itemno;
	}
	public void setItemno(String itemno) {
		this.itemno = itemno;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}

}
