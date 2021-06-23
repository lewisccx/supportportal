package com.supportportal.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoleSet();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            // do here
            Set<Permission> permissions = role.getPermissionSet();
            for (Permission permission : permissions){
                authorities.add(new SimpleGrantedAuthority(role.getRole()+":"+ permission.getModule()+":"+permission.getAction()));
            }
        }
        /*
        * TODO
        *  Should design more complex access combination
        * */
        return authorities;    }

    @Override
    public String getPassword() {
        return this.user.getStaffId();
    }

    @Override
    public String getUsername() {
        return this.user.getNric();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        ZoneId defaultZoneId = ZoneId.of("Asia/Singapore");
        LocalDate currentDate = LocalDate.now();
        LocalDate currentDateMinus3Months = currentDate.minusDays(90);
        Date date = Date.from(currentDateMinus3Months.atStartOfDay(defaultZoneId).toInstant());
        if (user.getDteLastLogin() != null) {
            return  !user.getDteLastLogin().before(date);
        }
        return true;
    }
}
