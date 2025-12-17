package app.Users.domain;

import app.Users.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    //Since Roles are basically enums, they cannot be forced many to one, so we use @CollectionTable to ensure they are stored in a different table as normalized, otherwise we would get userId=1 -> {admin, staff} now we have userId_1 admin userId_1 staff separately
    @ElementCollection(fetch = FetchType.EAGER) //Roles are required immediately cannot wait for roles after authentication
    @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"))
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Set<UserRole> roles;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean active= true;

    @PrePersist
    protected void onCreate(){
        this.createdAt= LocalDateTime.now();
        this.updatedAt= LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt= LocalDateTime.now();
    }
}
