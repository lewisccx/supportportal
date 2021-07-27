package com.supportportal.repository;

import com.supportportal.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    void deleteByNric(String nric);
    User findUserByNric(String nric);
    User findUserByEmail(String email);
    Boolean existsUserByNric(String nric);
    Boolean existsUserByEmail(String email);
    @Query(value="select ips_user.* from ips_user where ips_user.name like %:query% or ips_user.display_name like  %:query% or  ips_user.email like %:query%", nativeQuery = true)
    Page<User> findByNameOrDisplayNameOrEmailLike(@Param("query") String query, Pageable pageable);
   // Page<User> findAll(Pageable pageable);
}
