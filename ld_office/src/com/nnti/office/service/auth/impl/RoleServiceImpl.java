package com.nnti.office.service.auth.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.office.dao.auth.MybatisOperatorDao;
import com.nnti.office.dao.auth.RoleDao;
import com.nnti.office.model.auth.Role;
import com.nnti.office.model.auth.UserRole;
import com.nnti.office.service.auth.PermissionService;
import com.nnti.office.service.auth.RoleService;
import com.nnti.office.util.CollectionUtil;
import com.nnti.office.util.StringUtil;

@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	MybatisOperatorDao mybatisOperatorDao;
	
	@Override
	public List<Role> getAllRole() {
		return roleDao.getAllRole();
	}

	@Override
	public Role getRoleById(Integer id) {
		return roleDao.getRoleById(id);
	}

	@Override
	public void updateRole(Role role) {
		roleDao.updateRole(role);
	}

	@Override
	public List<Role> searchRole(Role role) {
		return roleDao.searchRole(role);
	}

	@Override
	public void deleteRole(Integer id) {
		roleDao.deleteRole(id);
		roleDao.deleteUserRoleByRoleId(id);
		permissionService.deleteRolePermissionByRoleId(id);
	}

	@Override
	public void insertRole(Role role) {
		role.setCreateTime(new Date());
		roleDao.insertRole(role);
	}

	@Override
	public void deleteUserRoleByUsername(String username) {
		roleDao.deleteUserRoleByUsername(username);
	}

	@Override
	public List<Role> getAllRoleWithStatus(String username) {
		List<Role> allRoleList = roleDao.getAllRole();
		List<Integer> roleIdList = roleDao.getRoleIdList(username);
		for(Role role : allRoleList) {
			if(roleIdList.contains(role.getId())) {
				role.setChecked("checked");
			}else {
				role.setChecked("");
			}
		}
		return allRoleList;
	}

	@Override
	public void updateOperatorRole(String username, List<Integer> roleIdList) {
		List<Integer> oldRoleIdList = roleDao.getRoleIdList(username);
		List<Integer> addIdList = CollectionUtil.getAddList(oldRoleIdList,roleIdList);
		List<Integer> deleteIdList = CollectionUtil.getDeleteList(oldRoleIdList,roleIdList);
		//insert
		if(CollectionUtil.isNotEmpty(addIdList)) {
			List<UserRole> addUserRoleList = new ArrayList<UserRole>();
			for(Integer roleId : addIdList) {
				UserRole userRole = new UserRole();
				userRole.setUsername(username);
				userRole.setRoleId(roleId);
				userRole.setCreateTime(new Date());
				addUserRoleList.add(userRole);
			}
			roleDao.batchInsertUserRole(addUserRoleList);
		}
		//delete
		if(CollectionUtil.isNotEmpty(deleteIdList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("username", username);
			params.put("roleIdList", deleteIdList);
			roleDao.batchDeleteUserRole(params);
		}
		if(CollectionUtil.isNotEmpty(roleIdList)) {
			Role role = roleDao.getRoleById(roleIdList.get(0));
			String roleCode = role.getCode();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("username", username);
			params.put("authority",roleCode);
			mybatisOperatorDao.updateAuthority(params);
		}
	}

	@Override
	public List<Role> getRoleByUsername(String username) {
		List<Integer> roleIdList = roleDao.getRoleIdList(username);
		if(CollectionUtil.isEmpty(roleIdList)) {
			return new ArrayList<Role>();
		}
		List<Role> roleList = roleDao.getRoleList(roleIdList);
		return roleList;
	}

	@Override
	public List<Role> getRoleListByNames(String names) {
		if(StringUtil.isEmpty(names)) {
			throw new RuntimeException("role names is empty");
		}
		String[] nameArray = names.split(",");
		List<String> roleNameList = Arrays.asList(nameArray);
		return roleDao.getRoleListByNameList(roleNameList);
	}
	
}
