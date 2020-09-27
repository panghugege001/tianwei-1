package com.nnti.office.service.auth.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.office.dao.auth.PermissionDao;
import com.nnti.office.dao.auth.RoleDao;
import com.nnti.office.model.auth.Permission;
import com.nnti.office.model.auth.Role;
import com.nnti.office.model.auth.RolePermission;
import com.nnti.office.model.auth.UserRole;
import com.nnti.office.service.auth.PermissionService;
import com.nnti.office.service.auth.RoleService;
import com.nnti.office.util.CollectionUtil;

@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	PermissionDao permissionDao;
	
	@Autowired
	RoleService roleService;

	@Override
	public List<Permission> getUserPermissionTree(String username) {
		List<Integer> roleIdList = roleDao.getRoleIdList(username);
		List<Integer> permissionIdList = null;
		if(CollectionUtil.isNotEmpty(roleIdList)) {
			permissionIdList = permissionDao.getPermissonIdList(roleIdList);
		}
		List<Permission> permissionList = null;
		if(CollectionUtil.isNotEmpty(permissionIdList)) {
			permissionList = permissionDao.getPermissionList(permissionIdList);
		}
		createPermissionTree(permissionList);
		return permissionList;
	}
	
	@Override
	public List<Permission> getUserPermission(String username) {
		List<Integer> roleIdList = roleDao.getRoleIdList(username);
		List<Integer> permissionIdList = null;
		if(CollectionUtil.isNotEmpty(roleIdList)) {
			permissionIdList = permissionDao.getPermissonIdList(roleIdList);
		}
		List<Permission> permissionList = null;
		if(CollectionUtil.isNotEmpty(permissionIdList)) {
			permissionList = permissionDao.getPermissionList(permissionIdList);
		}
		return permissionList;
	}
	
	private List<Permission> createPermissionTree(List<Permission> permissionList) {
		if(CollectionUtil.isEmpty(permissionList)) {
			return new ArrayList<Permission>();
		}
		List<Permission> mainPermissionList = new ArrayList<Permission>();
		List<Permission> subPermissionList = new ArrayList<Permission>();
		for(Permission permission : permissionList) {
			if(permission.getPid().equals(0)) {
				mainPermissionList.add(permission);
			}else {
				subPermissionList.add(permission);
			}
		}
		
		for(Permission mainPermission : mainPermissionList) {
			for(Permission subPermission : subPermissionList) {
				if(subPermission.getPid().equals(mainPermission.getId())) {
					mainPermission.getSubPermissonList().add(subPermission);
				}
			}
		}
		return mainPermissionList;
	}
	
	private List<Permission> createOrderedPermissionList(List<Permission> permissionList) {
		List<Permission> orderedPermissionList = new ArrayList<Permission>();
		List<Permission> mainPermissionList = new ArrayList<Permission>();
		List<Permission> subPermissionList = new ArrayList<Permission>();
		for(Permission permission : permissionList) {
			if(permission.getPid().equals(0)) {
				mainPermissionList.add(permission);
			}else {
				subPermissionList.add(permission);
			}
		}
		for(Permission mainPermission : mainPermissionList) {
			orderedPermissionList.add(mainPermission);
			for(Permission subPermission : subPermissionList) {
				if(subPermission.getPid().equals(mainPermission.getId())) {
					orderedPermissionList.add(subPermission);
				}
			}
		}
		for(Permission permission : subPermissionList) {
			if(! orderedPermissionList.contains(permission)) {
				orderedPermissionList.add(permission);
			}
		}
		return orderedPermissionList;
	}
	
	@Override
	public List<Permission> getAllPermission() {
		List<Permission> permissionList = permissionDao.getAllPermission(); 
		return createOrderedPermissionList(permissionList);
	}

	@Override
	public Permission getPermissionById(Integer id) {
		return permissionDao.getPermissionById(id);
	}

	@Override
	public void updatePermission(Permission permission) {
		permissionDao.updatePermission(permission);
	}

	@Override
	public void deletePermission(Integer id) {
		permissionDao.deletePermission(id);
		permissionDao.deleteRolePermissionByPermissionId(id);
	}

	@Override
	public List<Permission> searchPermission(Permission permission) {
		List<Permission> permissionList = permissionDao.searchPermission(permission); 
		return createOrderedPermissionList(permissionList);
	}

	@Override
	public void insertPermission(Permission permission) {
		permission.setCreateTime(new Date());
		permissionDao.insertPermission(permission);
	}
	
	public List<Integer> getRolePermissonIdList(Integer roleId) {
		return permissionDao.getRolePermissonIdList(roleId);
	}
	
	@Override
	public List<Permission> getAllPermissionWithStatus(Integer roleId) {
		List<Permission> allPermissionList = permissionDao.getAllPermission();
		allPermissionList = createOrderedPermissionList(allPermissionList);
		List<Integer> rolePermissionIdList = permissionDao.getRolePermissonIdList(roleId);
		for(Permission permission : allPermissionList) {
			if(rolePermissionIdList.contains(permission.getId())) {
				permission.setChecked("checked");
			}else {
				permission.setChecked("");
			}
		}
		return allPermissionList;
	}

	@Override
	public void updateRolePermission(Integer roleId, List<Integer> permissionIdList) {
		List<Integer> rolePermissionIdList = permissionDao.getRolePermissonIdList(roleId);
		List<Integer> addList = CollectionUtil.getAddList(rolePermissionIdList,permissionIdList);
		List<Integer> deleteList = CollectionUtil.getDeleteList(rolePermissionIdList,permissionIdList);
		//insert
		if(CollectionUtil.isNotEmpty(addList)) {
			List<RolePermission> addRolePermissionList = new ArrayList<RolePermission>();
			for(Integer permissionId : addList) {
				RolePermission rolePermission = new RolePermission();
				rolePermission.setRoleId(roleId);
				rolePermission.setPermissionId(permissionId);
				rolePermission.setCreateTime(new Date());
				addRolePermissionList.add(rolePermission);
			}
			permissionDao.batchInsertRolePermission(addRolePermissionList);
		}
		//delete
		if(CollectionUtil.isNotEmpty(deleteList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("roleId", roleId);
			params.put("permissionIdList", deleteList);
			permissionDao.batchDeleteRolePermission(params);
		}
	}
	
	@Override
	public void deleteRolePermissionByRoleId(Integer roleId) {
		permissionDao.deleteRolePermissionByRoleId(roleId);
	}

	@Override
	public List<String> getAllProtectedUri() {
		return permissionDao.getAllProtectedUri();
	}

	@Override
	public List<String> getUserPermissionUrl(String username) {
		List<Integer> roleIdList = roleDao.getRoleIdList(username);
		List<Integer> permissionIdList = null;
		if(CollectionUtil.isNotEmpty(roleIdList)) {
			permissionIdList = permissionDao.getPermissonIdList(roleIdList);
		}
		List<String> userPermissionUrlList = null;
		if(CollectionUtil.isNotEmpty(permissionIdList)) {
			userPermissionUrlList = permissionDao.getUserPermissionUrl(permissionIdList);
		}
		return userPermissionUrlList;
	}
	
	@Override
	public List<String> getUserPermissionCode(String username) {
		List<Integer> roleIdList = roleDao.getRoleIdList(username);
		List<Integer> permissionIdList = null;
		if(CollectionUtil.isNotEmpty(roleIdList)) {
			permissionIdList = permissionDao.getPermissonIdList(roleIdList);
		}
		List<String> userUermissionCode = null;
		if(CollectionUtil.isNotEmpty(permissionIdList)) {
			userUermissionCode = permissionDao.getUserPermissionCode(permissionIdList);
		}
		return userUermissionCode;
	}

	@Override
	public List<Permission> upSearch(Integer id) {
		List<Permission> permissionList = new ArrayList<Permission>();
		Permission permission = permissionDao.getPermissionById(id);
		permissionList.add(permission);
		return permissionList;
	}
	
	@Override
	public List<Permission> downSearch(Integer pid) {
		List<Permission> permissionList = permissionDao.getSubPermission(pid);
		return permissionList;
	}
	
	@Override
	public List<Role> getAllRolePermissionWithStatus(Integer permissionId) {
		List<Role> allRoleList = roleService.getAllRole();
		List<Integer> roleIdList = permissionDao.getRoleIdListOfPermission(permissionId);
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
	public void updatePermissionRole(Integer permissionId, List<Integer> roleIdList) {
		List<Integer> oldRoleIdList = permissionDao.getRoleIdListOfPermission(permissionId);
		List<Integer> addIdList = CollectionUtil.getAddList(oldRoleIdList,roleIdList);
		List<Integer> deleteIdList = CollectionUtil.getDeleteList(oldRoleIdList,roleIdList);
		//insert
		if(CollectionUtil.isNotEmpty(addIdList)) {
			List<RolePermission> addPermissionRoleList = new ArrayList<RolePermission>();
			for(Integer roleId : addIdList) {
				RolePermission rolePermission = new RolePermission();
				rolePermission.setPermissionId(permissionId);
				rolePermission.setRoleId(roleId);
				rolePermission.setCreateTime(new Date());
				addPermissionRoleList.add(rolePermission);
			}
			permissionDao.batchInsertRolePermission(addPermissionRoleList);
		}
		//delete
		if(CollectionUtil.isNotEmpty(deleteIdList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("permissionId", permissionId);
			params.put("roleIdList", deleteIdList);
			permissionDao.batchDeletePermissionRole(params);
		}
	}

}
