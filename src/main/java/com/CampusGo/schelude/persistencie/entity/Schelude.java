package com.CampusGo.schelude.persistencie.entity;

import com.CampusGo.subject.persistencie.entity.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCHELUDE")
public class Schelude {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private Integer code;

    @Column(name = "codeAsignatureFk", insertable = false, updatable = false)
    private Integer codeAsignatureFk;

    @Column(length = 10)
    private String dia;

    @Column(length = 5)
    private String horaInicial;

    @Column(length = 5)
    private String horaFinal;


    @ManyToOne
    @JoinColumn(name = "codeAsignatureFk", referencedColumnName = "code")
    private Subject subject;

}
