package com.supportportal.repository;

import com.supportportal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserOid(int  id);

    User findUserByNric(String nric);
    User findUserByEmail(String email);
}
