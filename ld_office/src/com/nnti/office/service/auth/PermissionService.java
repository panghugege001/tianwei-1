package com.nnti.office.service.auth;

import java.util.List;

import com.nnti.office.model.auth.Permission;
import com.nnti.office.model.auth.Role;

public interface PermissionService {
	
	public List<Permission> getUserPermissionTree(String username);
	
	public List<Permission> getUserPermission(String username);
	
	public List<String> getUserPermissionUrl(String username);
	
	public List<String> getUserPermissionCode(String username);
	
	public List<Permission> getAllPermission();
	
	public Permission getPermissionById(Integer id);
	
	public void updatePermission(Permission permission);
	
	public void deletePermission(Integer id);
	
	public void deleteRolePermissionByRoleId(Integer roleId);
	
	public List<Permission> searchPermission(Permission permission);
	
	public void insertPermission(Permission permission);
	
	public List<Permission> getAllPermissionWithStatus(Integer roleId);
	
	public void updateRolePermission(Integer roleId,List<Integer> permissionIdList);
	
	public List<String> getAllProtectedUri();
	
	public List<Permission> upSearch(Integer id);
	
	public List<Permission> downSearch(Integer pid);
	
	public List<Role> getAllRolePermissionWithStatus(Integer permissionId);
	
	public void updatePermissionRole(Integer permissionId,List<Integer> roleIdList);
	
}
