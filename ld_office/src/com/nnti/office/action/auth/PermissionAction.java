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

import com.nnti.office.model.auth.Operator;
import com.nnti.office.model.auth.Permission;
import com.nnti.office.model.auth.Role;
import com.nnti.office.model.common.ResponseData;
import com.nnti.office.model.common.ResponseEnum;
import com.nnti.office.model.i18n.I18n;
import com.nnti.office.service.auth.PermissionService;
import com.nnti.office.util.StringUtil;
import com.nnti.office.util.StrutsPrintUtil;
import com.opensymphony.xwork2.ActionSupport;

import dfh.utils.Constants;

@Namespace("/permission")
@ResultPath(value="/")
public class PermissionAction extends ActionSupport implements SessionAware,ServletResponseAware{
	private static final long serialVersionUID = 4705050111782885763L;
	private Logger logger = Logger.getLogger(PermissionAction.class);
	
	private Map<String,Object> session;
	private HttpServletResponse response;
	private Integer id;
	private Permission permission;
	private Integer roleId;
	private String permissionIdArray;
	private Integer permissionId;
	private String roleIdArray;
	private String operationNameArray;
	
	@Autowired
	PermissionService permissionService;
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
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

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public String getPermissionIdArray() {
		return permissionIdArray;
	}

	public void setPermissionIdArray(String permissionIdArray) {
		this.permissionIdArray = permissionIdArray;
	}
	
	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}
	
	public String getRoleIdArray() {
		return roleIdArray;
	}

	public void setRoleIdArray(String roleIdArray) {
		this.roleIdArray = roleIdArray;
	}

	public String getOperationNameArray() {
		return operationNameArray;
	}

	public void setOperationNameArray(String operationNameArray) {
		this.operationNameArray = operationNameArray;
	}

	@Action(value="getPermissionList")
	public void getPermissionList() {
		ResponseData responseData = new ResponseData();
		List<Permission> permissionList = null;
		try {
			Operator operator = (Operator) session.get(Constants.SESSION_OPERATORID);
			permissionList = permissionService.getUserPermissionTree(operator.getUsername());
		}catch(Exception e) {
			logger.error("获取用户权限列表异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(permissionList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getAllPermission")
	public void getAllPermission() {
		ResponseData responseData = new ResponseData();
		List<Permission> permissionList = null;
		try {
			permissionList = permissionService.getAllPermission();
		}catch(Exception e) {
			logger.error("获取所有权限列表异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(permissionList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getAllPermissionWithStatus")
	public void getAllPermissionWithStatus() {
		ResponseData responseData = new ResponseData();
		List<Permission> permissionList = null;
		try {
			permissionList = permissionService.getAllPermissionWithStatus(roleId);
		}catch(Exception e) {
			logger.error("获取所有权限列表附带角色所属状态异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(permissionList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getPermissionById")
	public void getPermissionById() {
		ResponseData responseData = new ResponseData();
		Permission permission = null;
		try {
			permission = permissionService.getPermissionById(id);
		}catch(Exception e) {
			logger.error("按照ID获取权限异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(permission);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="updatePermission" )
	public void updatePermission() {
		ResponseData responseData = new ResponseData();
		//validation
		if(permission == null || permission.getId() == null || StringUtil.isEmpty(permission.getName()) ) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			permissionService.updatePermission(permission);
		}catch(Exception e) {
			logger.error("更新权限异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="deletePermission" )
	public void deletePermission() {
		ResponseData responseData = new ResponseData();
		//validation
		if(id == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			permissionService.deletePermission(id);
		}catch(Exception e) {
			logger.error("删除权限异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="upSearch" )
	public void upSearch() {
		ResponseData responseData = new ResponseData();
		List<Permission> permissionList = null;
		//validation
		if(permission.getId() == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			permissionList = permissionService.upSearch(permission.getId());
		}catch(Exception e) {
			logger.error("搜索上一级权限异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(permissionList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="downSearch" )
	public void downSearch() {
		ResponseData responseData = new ResponseData();
		List<Permission> permissionList = null;
		//validation
		if(permission.getPid() == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			permissionList = permissionService.downSearch(permission.getPid());
		}catch(Exception e) {
			logger.error("搜索下一级权限异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(permissionList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="searchPermission" )
	public void searchPermission() {
		ResponseData responseData = new ResponseData();
		List<Permission> permissionList = null;
		//validation
		if(permission == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			permissionList = permissionService.searchPermission(permission);
		}catch(Exception e) {
			logger.error("搜索权限异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(permissionList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="addPermission" )
	public void addPermission() {
		ResponseData responseData = new ResponseData();
		//validation
		if(permission == null || permission.getPid() == null || StringUtil.isEmpty(permission.getName()) || StringUtil.isEmpty(permission.getType())) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			permissionService.insertPermission(permission);
		}catch(Exception e) {
			logger.error("创建权限异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="updateRolePermission" )
	public void updateRolePermission() {
		ResponseData responseData = new ResponseData();
		//validation
		if(permissionIdArray == null || roleId == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			List<Integer> permissionIdList = new ArrayList<Integer>();
			if(StringUtil.isNotEmpty(permissionIdArray)) {
				String[] StringIdArray = permissionIdArray.split(",");
				for(String stringId : StringIdArray) {
					permissionIdList.add(Integer.valueOf(stringId));
				}
			}
			permissionService.updateRolePermission(roleId,permissionIdList);
			
		}catch(Exception e) {
			logger.error("修改角色权限异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getAllRolePermissionWithStatus")
	public void getAllRolePermissionWithStatus() {
		ResponseData responseData = new ResponseData();
		List<Role> roleList = null;
		try {
			roleList = permissionService.getAllRolePermissionWithStatus(permissionId);
		}catch(Exception e) {
			logger.error("获取所有权限列表附带角色所属状态异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(roleList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="updatePermissionRole" )
	public void updatePermissionRole() {
		ResponseData responseData = new ResponseData();
		//validation
		if(roleIdArray == null || permissionId == null) {
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
			permissionService.updatePermissionRole(permissionId,roleIdList);
		}catch(Exception e) {
			logger.error("修改权限角色异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="unAuthorizedCodeList" )
	public void getUnAuthorizedCodeList() {
		ResponseData responseData = new ResponseData();
		List<String> unAuthorizedCodeList = null;
		try {
			String[] codeArray = operationNameArray.split(",");
			List<String> myCodeList = (List<String>)session.get(Constants.SESSION_AUTH_CODE);
			unAuthorizedCodeList = new ArrayList<String>();
			for(String code : codeArray) {
				if(! myCodeList.contains(code)) {
					unAuthorizedCodeList.add(code);
				}
			}
		}catch(Exception e) {
			logger.error("获取操作权限数据异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(unAuthorizedCodeList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
}
