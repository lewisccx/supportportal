package com.supportportal.resource;

import com.supportportal.domain.HttpResponse;
import com.supportportal.domain.PageObject;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
                user.getDisplayName(),user.getAppt());
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
//            @RequestParam("nric") String nric,
//            @RequestParam("name") String name,
//            @RequestParam("salutation") String salutation,
//            @RequestParam("userInitial") String userInitial,
//            @RequestParam("email") String email,
//            @RequestParam("displayName") String displayName,
//            @RequestParam("appt") String appt,
//            @RequestParam("roles") String[] roles
            @RequestBody User user
    ){
        User updatedUser = userService.updateUser(user.getNric(),user.getName(),user.getSalutation(),
                user.getUserInitial(),user.getEmail(),user.getDisplayName(), user.getAppt(), user.getRoleSet());
        return new ResponseEntity<>(updatedUser, OK);
    }

    @GetMapping("/find")
    public ResponseEntity<User> getUser(@RequestParam("nric") String nric) {
        User user = userService.findUserByNric(nric);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping(value = "/exist",  params = "nric")
    public ResponseEntity<Boolean> existsUserByNric(@RequestParam String nric) {
        Boolean exists = userService.existsUserByNric(nric);
        return new ResponseEntity<>(exists, OK);
    }

    @GetMapping(value = "/exist",  params = "email")
    public ResponseEntity<Boolean> existsUserByEmail(@RequestParam String email) {
        Boolean exists = userService.existsUserByEmail(email);
        return new ResponseEntity<>(exists, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<PageObject> getAllUsers(
            @RequestParam(value = "filter",required = false) String filter,
            @RequestParam(value = "sorted",required = false) boolean sorted,
            @RequestParam(required = false, value = "query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            List<User> users = new ArrayList<User>();
            Pageable paging = PageRequest.of(page,size);
            Page<User> pageUsers;
            if(filter != null)
                pageUsers = userService.getAll(filter,sorted,paging);
            else if(query != null)
                pageUsers = userService.search(query, paging);
            else
                pageUsers = userService.getAll(paging);
            users  = pageUsers.getContent();
            PageObject pageObject = new PageObject(users, pageUsers.getNumber(), (int) pageUsers.getTotalElements(),pageUsers.getTotalPages());
            return new ResponseEntity<>(pageObject,HttpStatus.OK);
        }
        catch (Exception ex){
            PageObject pageObject =  new PageObject();
            return new ResponseEntity<>(pageObject, INTERNAL_SERVER_ERROR);
        }


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

}
