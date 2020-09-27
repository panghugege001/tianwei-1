package com.nnti.office.dao.auth;

import java.util.List;
import java.util.Map;

import com.nnti.office.model.auth.Role;
import com.nnti.office.model.auth.UserRole;

public interface RoleDao {
	
	public List<Integer> getRoleIdList(String username);
	
	public List<Role> getRoleList(List<Integer> idList);
	
	public List<Role> getAllRole();
	
	public Role getRoleById(Integer id);
	
	public void updateRole(Role role);
	
	public List<Role> searchRole(Role role);
	
	public void deleteRole(Integer id);
	
	public void insertRole(Role role);
	
	public void deleteUserRoleByUsername(String username);
	
	public void deleteUserRoleByRoleId(Integer roleId);
	
	public void batchInsertUserRole(List<UserRole> userRoleList);
	
	public void insertUserRole(UserRole userRole);
	
	public void batchDeleteUserRole(Map<String,Object> params);
	
	public List<Role> getRoleListByNameList(List<String> roleNameList);
	
	public Integer getRoleId(String code);
	
}
