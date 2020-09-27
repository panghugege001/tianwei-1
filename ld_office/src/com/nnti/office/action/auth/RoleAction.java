package com.nnti.office.action.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.nnti.office.model.auth.Permission;
import com.nnti.office.model.auth.Role;
import com.nnti.office.model.common.ResponseData;
import com.nnti.office.model.common.ResponseEnum;
import com.nnti.office.service.auth.RoleService;
import com.nnti.office.util.StringUtil;
import com.nnti.office.util.StrutsPrintUtil;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("/role")
@ResultPath(value="/")
public class RoleAction extends ActionSupport implements SessionAware,ServletResponseAware{
	
	private static final long serialVersionUID = -1712231721492697085L;
	private Logger logger = Logger.getLogger(RoleAction.class);
	
	private Map<String,Object> session;
	private HttpServletResponse response;
	private Integer id;
	private Role role;
	private String username;
	private String roleIdArray;
	
	@Autowired
	RoleService roleService;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getRoleIdArray() {
		return roleIdArray;
	}

	public void setRoleIdArray(String roleIdArray) {
		this.roleIdArray = roleIdArray;
	}

	@Action(value="getAllRole")
	public void getAllRole() {
		ResponseData responseData = new ResponseData();
		List<Role> roleList = null;
		try {
			roleList = roleService.getAllRole();
		}catch(Exception e) {
			logger.error("获取所有角色列表异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(roleList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getRoleById")
	public void getRoleById() {
		ResponseData responseData = new ResponseData();
		Role role = null;
		try {
			role = roleService.getRoleById(id);
		}catch(Exception e) {
			logger.error("按照ID获取角色异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(role);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="updateRole" )
	public void updateRole() {
		ResponseData responseData = new ResponseData();
		//validation
		if(role == null || role.getId() == null || StringUtil.isEmpty(role.getName()) ) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			roleService.updateRole(role);
		}catch(Exception e) {
			logger.error("修改角色异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="searchRole" )
	public void searchRole() {
		ResponseData responseData = new ResponseData();
		List<Role> roleList = null;
		//validation
		if(role == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			roleList = roleService.searchRole(role);
		}catch(Exception e) {
			logger.error("搜索角色异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(roleList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="deleteRole" )
	public void deleteRole() {
		ResponseData responseData = new ResponseData();
		//validation
		if(id == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			roleService.deleteRole(id);
		}catch(Exception e) {
			logger.error("删除角色异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="addRole" )
	public void addRole() {
		ResponseData responseData = new ResponseData();
		//validation
		if(role == null || StringUtil.isEmpty(role.getName()) || StringUtil.isEmpty(role.getCode())) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			roleService.insertRole(role);
		}catch(Exception e) {
			logger.error("创建角色异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getAllRoleWithStatus")
	public void getAllRoleWithStatus() {
		ResponseData responseData = new ResponseData();
		List<Role> roleList = null;
		try {
			roleList = roleService.getAllRoleWithStatus(username);
		}catch(Exception e) {
			logger.error("获取所有角色列表附带用户所属状态异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(roleList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="updateOperatorRole" )
	public void updateOperatorRole() {
		ResponseData responseData = new ResponseData();
		//validation
		if(roleIdArray == null || username == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			List<Integer> roleIdList = new ArrayList<Integer>();
			if(StringUtil.isNotEmpty(roleIdArray)) {
				String[] StringIdArray = roleIdArray.split(",");
				for(String stringId : StringIdArray) {
					roleIdList.add(Integer.valueOf(stringId));
				}
			}
			roleService.updateOperatorRole(username,roleIdList);
			
		}catch(Exception e) {
			logger.error("修改用户角色异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	
}
