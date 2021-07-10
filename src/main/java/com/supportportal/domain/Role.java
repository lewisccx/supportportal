package com.supportportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="IPS_ROLE")
@SequenceGenerator(name="role_seq", initialValue=3000000, allocationSize=1)
public class Role
{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="role_seq")
    @Column(name="role_oid", unique = true)
    private int roleOid;
    @Column(nullable = false, unique = true, length = 20)
    private String role;
    @Column(nullable = false, length = 40)
    private String descr;

    public Role(String role, String descr) {
        this.role = role.toUpperCase();
        this.descr = descr.toUpperCase();

    }

    //many to many rel with Permission
    @ManyToMany
    @JoinTable(
            name = "IPS_ROLE_PERMISSION",
            joinColumns = @JoinColumn(name = "role_oid"),
            inverseJoinColumns = @JoinColumn(name = "permission_oid")
    )
    //remove @JsonIgnore to display child object: Permission object
    @JsonIgnore
    private Set<Permission> permissionSet = new HashSet<>();

    public void addPermission(Permission permission){
        this.permissionSet.add(permission);
    }

    public void removePermission(Permission permission){
        this.permissionSet.remove(permission);
    }
}
