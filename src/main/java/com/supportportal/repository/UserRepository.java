package com.supportportal.repository;

import com.supportportal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUserOid(int  id);
    void deleteByNric(String nric);
    User findUserByNric(String nric);
    User findUserByEmail(String email);
}
