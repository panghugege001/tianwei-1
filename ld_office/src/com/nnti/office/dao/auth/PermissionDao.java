package com.nnti.office.dao.auth;

import java.util.List;
import java.util.Map;

import com.nnti.office.model.auth.Permission;
import com.nnti.office.model.auth.RolePermission;

public interface PermissionDao {
	
	public List<Integer> getPermissonIdList(List<Integer> roleIdList);
	
	public List<Integer> getRolePermissonIdList(Integer roleId);
	
	public List<Permission> getPermissionList(List<Integer> idList);
	
	public List<String> getUserPermissionUrl(List<Integer> idList);
	
	public List<String> getUserPermissionCode(List<Integer> idList);
	
	public List<Permission> getAllPermission();
	
	public List<Permission> getSubPermission(Integer pid);
	
	public Permission getPermissionById(Integer id);
	
	public void updatePermission(Permission permission);
	
	public void deletePermission(Integer id);
	
	public List<Permission> searchPermission(Permission permission);
	
	public void insertPermission(Permission permission);
	
	public void deleteRolePermissionByPermissionId(Integer permissionId);
	
	public void deleteRolePermissionByRoleId(Integer roleId);
	
	public void batchInsertRolePermission(List<RolePermission> rolePermissionList);
	
	public void batchDeleteRolePermission(Map<String,Object> params);
	
	public void batchDeletePermissionRole(Map<String,Object> params);
	
	public List<String> getAllProtectedUri();
	
	public List<Integer> getRoleIdListOfPermission(Integer permissionId);
	
}
