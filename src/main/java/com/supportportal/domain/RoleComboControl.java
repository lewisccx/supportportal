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
@Table(name="IPS_ROLE_COMBO_CONTROL")
@SequenceGenerator(name="role_combo_control_seq", initialValue=1000000, allocationSize=1)
public class RoleComboControl {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="role_combo_control_seq")
    @Column(name="role_combo_control_oid", unique = true)
    private int roleComboControlOid;

    @Column(nullable = false, length = 20)
    private int role_oid;

    @Column(nullable = false, length = 20)
    private int coexist_role_oid;
}
