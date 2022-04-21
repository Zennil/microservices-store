package com.zenil.store.customer.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tbl_customer")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El numero de documento no debe ser vacío")
    @Size(min = 8, max = 8, message = "El tamaño del número de documento es 8")
    @Column(name = "number_id", unique = true, length = 8, nullable = false)
    private String numberId;

    @NotEmpty(message = "El nombre no puede ser vacío")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty(message = "El appellido no puede ser vacío")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty(message = "El correo no debe ser vacío")
    @Email(message = "No es una dirección de correo bien formada")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "photo_url")
    private String photoUrl;

    @NotNull(message = "La region no puede ser vacía")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Region region;

    private String state;
}
