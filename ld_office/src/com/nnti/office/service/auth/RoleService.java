package com.nnti.office.service.auth;

import java.util.List;

import com.nnti.office.model.auth.Role;

public interface RoleService {
	
	public List<Role> getAllRole();
	
	public List<Role> getRoleByUsername(String username);
	
	public Role getRoleById(Integer id);
	
	public void updateRole(Role role);
	
	public List<Role> searchRole(Role role);
	
	public void deleteRole(Integer id);
	
	public void insertRole(Role role);
	
	public void deleteUserRoleByUsername(String username);
	
	public List<Role> getAllRoleWithStatus(String username);
	
	public void updateOperatorRole(String username,List<Integer> roleIdList);
	
	public List<Role> getRoleListByNames(String names);
	
}
