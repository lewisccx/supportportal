package com.supportportal.service;

import com.supportportal.domain.Role;
import com.supportportal.domain.User;
import com.supportportal.exception.domain.EmailExistException;
import com.supportportal.exception.domain.UsernameExistException;

import java.util.List;
import java.util.Set;

public interface UserService {

    User register(String nric, String name, String salutation, String userInitial, String email, String displayName, String appt, Set<Role> roleSet) throws  UsernameExistException, EmailExistException;
    User findUserByNric(String nric);
    List<User> getUsers();

}
