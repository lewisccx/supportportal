package com.supportportal.repository;

import com.supportportal.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select distinct rcc.coexist_role_oid from IPS_ROLE_COMBO_CONTROL rcc  where rcc.role_oid = ?1", nativeQuery = true)
    List<String> getCompatibleRolesOidByRoleOid(String roleOid);

    Role findByRoleOid(int roleOid);
}
