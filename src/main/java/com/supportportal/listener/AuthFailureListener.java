package com.supportportal.listener;

import com.supportportal.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class AuthFailureListener {

    private final LoginAttemptService loginAttemptService;

    @Autowired
    public AuthFailureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthFailure(AuthenticationFailureBadCredentialsEvent event)  {
        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof  String){
            String nric = (String) event.getAuthentication().getPrincipal();
            loginAttemptService.addUserToLoginAttemptCache(nric);
        }
    }


}
