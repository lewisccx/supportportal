package com.supportportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="IPS_PERMISSION")
@SequenceGenerator(name="permission_seq", initialValue=3000000, allocationSize=1)
public class Permission
{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="permission_seq")
    @Column(name="permission_oid", unique = true)
    private int permissionOid;
    @Column(nullable = false, length = 40)
    private String module;
    @Column(nullable = false, unique = true, length = 100)
    private String url;
    @Column(nullable = false)
    private String action;
    public Permission(String module, String url, String action) {
        this.module = module;
        this.url = url;
        this.action = action;
    }
}
