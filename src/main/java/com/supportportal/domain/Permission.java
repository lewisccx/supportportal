package com.supportportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

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
    private Boolean isReadOnly;

    public Permission(String module, String url, Boolean isReadOnly) {
        this.module = module;
        this.url = url;
        this.isReadOnly = isReadOnly;
    }
}
