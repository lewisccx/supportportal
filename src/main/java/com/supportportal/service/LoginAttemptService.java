package com.supportportal.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPT = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private  LoadingCache<String, Integer> loginAttemptCache;

    public LoginAttemptService() {
        super();
        loginAttemptCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, MINUTES)
                .maximumSize(100).build(
                        new CacheLoader<String, Integer>() {
                            @Override
                            public Integer load(String s) {
                                return 0;
                            }
                        }
                );
    }

    public void evictUserFromLoginAttemptCache(String nric) {
        loginAttemptCache.invalidate(nric);
    }

    public void addUserToLoginAttemptCache(String nric) {
        try{
            int attempts = 0;
            attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(nric);
            loginAttemptCache.put(nric, attempts);
        }catch (ExecutionException e){
            e.printStackTrace();
        }

    }

    public boolean hasExceededMaxAttempts(String nric)  {
        try {
            return loginAttemptCache.get(nric) >= MAXIMUM_NUMBER_OF_ATTEMPT;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
