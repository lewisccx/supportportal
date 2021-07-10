package com.supportportal.resource;

import com.supportportal.domain.HttpResponse;
import com.supportportal.domain.Role;
import com.supportportal.domain.User;
import com.supportportal.domain.UserPrincipal;
import com.supportportal.exception.ExceptionHandling;
import com.supportportal.exception.domain.EmailExistException;
import com.supportportal.exception.domain.UserNotFoundException;
import com.supportportal.exception.domain.UsernameExistException;
import com.supportportal.service.UserService;
import com.supportportal.utility.JWTTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.supportportal.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static com.supportportal.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = { "/", "/user"})
public class UserResource extends ExceptionHandling {
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserResource(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getNric(), user.getStaffId());
        User loginUser = userService.findUserByNric(user.getNric());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User newUser = userService.register(user.getNric(),
                user.getName(), user.getSalutation(),
                user.getUserInitial(),user.getEmail(),
                user.getDisplayName(),user.getAppt(),
                user.getRoleSet());
        return new ResponseEntity<>(newUser, OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
//    @PostMapping("/update")
//    public ResponseEntity<User> updateUser(
//
//            @RequestBody User user
//    )   {
//        User updatedUser = userService.updateUser(user.getNric(),user.getName(),user.getSalutation(),user.getUserInitial(),user.getEmail(),user.getDisplayName(), user.getAppt(), user.getLocked(),user.getRoleSet());
//        return new ResponseEntity<>(updatedUser, OK);
//    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(
            @RequestParam("nric") String nric,
            @RequestParam("name") String name,
            @RequestParam("salutation") String salutation,
            @RequestParam("userInitial") String userInitial,
            @RequestParam("email") String email,
            @RequestParam("displayName") String displayName,
            @RequestParam("appt") String appt,
            @RequestParam("roles") String[] roles
    ){
        User updatedUser = userService.updateUser(nric,name,salutation,
                userInitial,email,displayName, appt, roles);
        return new ResponseEntity<>(updatedUser, OK);
    }

    @GetMapping("/find")
    public ResponseEntity<User> getUser(@RequestParam("nric") String nric) {
        User user = userService.findUserByNric(nric);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, OK);
    }
    @DeleteMapping("/delete/{nric}")
    @PreAuthorize("hasAnyAuthority('ADMIN:user:update')")
    public ResponseEntity<HttpResponse> deleteUser( @RequestHeader("authorization") String authorization , @PathVariable("nric") String nric) {
        String formattedAuth = authorization.replace(TOKEN_PREFIX,"");
        String LogonNric = jwtTokenProvider.getSubject(formattedAuth);
        LOGGER.info(LogonNric);

       if(!nric.equals(LogonNric)){
            userService.deleteUser(nric);
            return response(NO_CONTENT, "User deleted successfully");
       }else{
           return response(CONFLICT, "Unable to delete your own account");
       }
    }

    private  ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message){
       HttpResponse body = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase(),message);
        return new ResponseEntity<>( body,httpStatus);
    }
    @GetMapping("/admin/login")
    public String admin() {

        return "hello";
    }
}
