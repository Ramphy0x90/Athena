package r16a.Athena.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private UserStatus status = UserStatus.DISABLED;
}
