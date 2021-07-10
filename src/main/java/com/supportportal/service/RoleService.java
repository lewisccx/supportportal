package com.supportportal.service;

import com.supportportal.domain.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRole();

    List<Role> getCompatibleRoleByRoleOid(String roleOid);

}
