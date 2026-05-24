package io.hexlet.blog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @NotBlank
    @NotNull
    @Column(name = "first_name")
    private String firstname;

    @NotBlank
    @NotNull
    @Column(name = "last_name")
    private String lastname;

    @NotBlank
    @NotNull
    @Column(name = "email",unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(name = "birthday")
    private LocalDate birthday;
}

//curl -X POST http://localhost:8080/api/users \
//  -H "Content-Type: application/json" \
//  -d '{
//    "firstname": "Dmitriy",
//    "lastname": "Korchagin",
//    "email": "dmitriy@example.com",
//    "birthday": "1995-03-23"
//  }'