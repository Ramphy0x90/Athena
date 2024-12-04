package r16a.Athena.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User {
    public enum UserStatus {
        ACTIVE,
        DISABLED,
        LOCKED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String surname;
    private String secret;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String userName;
    private String password;
    private boolean internal = false;

    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status = UserStatus.DISABLED;

    @ManyToMany
    @JoinTable(
            name = "user_client",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private Set<Client> clients = new HashSet<>();
}
