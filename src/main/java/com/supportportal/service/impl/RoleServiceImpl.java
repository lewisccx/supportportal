package com.supportportal.service.impl;

import com.supportportal.domain.Role;
import com.supportportal.repository.RoleRepository;
import com.supportportal.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getCompatibleRoleByRoleOid(String roleOid) {
        List<String> roleOidList = roleRepository.getCompatibleRolesOidByRoleOid(roleOid);
        List<Role> roleList = new ArrayList<>();
        for (String _roleOid: roleOidList) {
            Role role = roleRepository.findByRoleOid(Integer.parseInt( _roleOid));
            roleList.add(role);
        }
        
        return roleList;
    }


}
