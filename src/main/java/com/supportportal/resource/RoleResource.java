package com.supportportal.resource;

import com.supportportal.domain.Role;
import com.supportportal.domain.User;
import com.supportportal.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = {  "/role"})
public class RoleResource {

    private final RoleService roleService;

    @Autowired
    public RoleResource(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Role>> getAllUser() {
        List<Role> roles = roleService.getAllRole();
        return new ResponseEntity<>(roles, OK);
    }

    @GetMapping("/combo/{roleOid}")
    public ResponseEntity<List<Role>> getCompatibleRoles(@PathVariable("roleOid") String roleOid) {
        List<Role> roles = roleService.getCompatibleRoleByRoleOid(roleOid);
        return new ResponseEntity<>(roles, OK);
    }
}
