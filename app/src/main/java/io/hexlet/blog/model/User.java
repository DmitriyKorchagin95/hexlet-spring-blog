package io.hexlet.blog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

//curl -X POST http://localhost:8080/api/users \
//  -H "Content-Type: application/json" \
//  -d '{
//    "firstname": "Dmitriy",
//    "lastname": "Korchagin",
//    "email": "dmitriy@example.com",
//    "birthday": "1995-03-23"
//  }'