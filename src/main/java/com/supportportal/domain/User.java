package com.supportportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="IPS_USER")
@SequenceGenerator(name="user_seq", initialValue= 3000000, allocationSize=1)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_seq")
    @Column(name="user_oid", unique = true)
    private int userOid;
    @Column(nullable = false, unique = true, length = 9)
    private String nric;
    @Column(name="staff_id",nullable = false, unique = true, length = 108)
    private String staffId;
    @Column(nullable = false, length = 66)
    private String name;
    @Column(nullable = false, length = 3)
    private String salutation;
    @Column(nullable = false, name="user_initial", length = 3)
    private String userInitial;
    public void setUserInitial(String userInitial) {
        this.userInitial =  userInitial.toUpperCase();
    }
    @Column(name="contact_no", length = 15)
    private String contactNo;
    @Column(name="contact_ext", length = 4)
    private String contactExt;
    @Column(name="fax_no", length = 15)
    private String faxNo;
    @Column(nullable = false, length = 80)
    private String email;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dteLastLogin;
    @Temporal(TemporalType.TIMESTAMP)
    //@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Asia/Singapore")
    private Date dteLastLogout;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dteExpire;
    @Column(nullable = false,name="display_name",  length = 66)
    private String displayName;
    @Column(nullable = false, length = 30)
    private String appt;

    @Column(nullable = false)
    private Boolean locked = false;

    //many to many rel with Role
    @ManyToMany
    @JoinTable(
            name = "IPS_USER_ROLE",
            joinColumns = @JoinColumn(name = "user_oid"),
            inverseJoinColumns = @JoinColumn(name = "role_oid")
    )
    private Set<Role> roleSet = new HashSet<>();

    public void addRole(Role role){
        this.roleSet.add(role);
    }

    public void removeRole(Role role){
        this.roleSet.remove(role);
    }

    public void removeAllRole(){this.roleSet.clear();}

    public User(String nric, String name, String salutation, String userInitial, String email, String displayName, String appt, Set<Role> roleSet) {
        this.nric = nric.toUpperCase();
        this.name = (name);
        this.salutation = StringUtils.capitalize(salutation);
        this.userInitial = userInitial.toUpperCase();
        this.email = email;
        this.displayName = (displayName);
        this.appt = appt.toUpperCase();
        this.roleSet = roleSet;
        this.locked = false;
    }
}
