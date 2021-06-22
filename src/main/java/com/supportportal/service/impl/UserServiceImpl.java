package com.supportportal.service.impl;

import com.supportportal.domain.Role;
import com.supportportal.domain.User;
import com.supportportal.domain.UserPrincipal;
import com.supportportal.exception.domain.EmailExistException;
import com.supportportal.exception.domain.UsernameExistException;
import com.supportportal.repository.UserRepository;
import com.supportportal.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.supportportal.constant.UserImplConstant.*;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String nric) throws UsernameNotFoundException {
        User user = userRepository.findUserByNric(nric);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + nric);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + nric);
        } else {
            user.setDteLastLogin(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + nric);
            return userPrincipal;
        }
    }

    @Override
    public User register(String nric, String name, String salutation, String userInitial, String email, String displayName, String appt, Set<Role> roleSet) throws UsernameExistException, EmailExistException {
        validateNricAndEmail(nric, email);
        User user = new User();
        String staffId = generateStaffId();
        LOGGER.info("Staff Id: " + staffId);
        String encodedStaffId= encodeStaffId(staffId);
        user.setNric(nric);
        user.setEmail(email);
        user.setStaffId(encodedStaffId);
        user.setName(name);
        user.setSalutation(salutation);
        user.setUserInitial(userInitial);
        user.setDisplayName(displayName);
        user.setAppt(appt);
        user.setRoleSet(roleSet);
        userRepository.save(user);
        return user;
    }

    @Override
    public User findUserByNric(String nric) {
        return userRepository.findUserByNric(nric);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }


    private String encodeStaffId(String staffId) {
        return passwordEncoder.encode(staffId);
    }

    private String generateStaffId() {


        return RandomStringUtils.randomNumeric(10);
    }


    private User validateNricAndEmail(String newNric, String newEmail) throws  UsernameExistException, EmailExistException {
        User userByNric = userRepository.findUserByNric(newNric);
        User userByNewEmail = userRepository.findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(newNric)) {
            User currentUser = userRepository.findUserByNric(newNric);

            if (userByNric != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (userByNric != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

}
