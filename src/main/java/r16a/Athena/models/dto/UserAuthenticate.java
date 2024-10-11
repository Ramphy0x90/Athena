package r16a.Athena.models.dto;

import lombok.Data;

@Data
public class UserAuthenticate {
    private String usernameEmail;
    private String password;
}
