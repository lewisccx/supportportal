package com.supportportal.service;

import com.supportportal.domain.Role;
import com.supportportal.domain.User;
import com.supportportal.exception.domain.EmailExistException;
import com.supportportal.exception.domain.UsernameExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserService {

    User register(String nric, String name, String salutation, String userInitial, String email, String displayName, String appt) throws  UsernameExistException, EmailExistException;
    User findUserByNric(String nric);

    Page<User> getAll(String filter, boolean sorted, Pageable pageable);
    Page<User> getAll(Pageable paging);

    Page<User> search(String query, Pageable pageable);
    User updateUser(String nric, String name, String salutation, String userInitial, String email, String displayName, String appt, Set<Role> roleSet);

    void deleteUser(String nric);
    Boolean existsUserByNric(String nric);
    User submitNewUserAccessControl(String nric, String name, String salutation, String userInitial, String email, String displayName, String appt, String[] roleSet, String submittedBy);

    Boolean existsUserByEmail(String email);


}
