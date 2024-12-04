package r16a.Athena.models.dto;

import lombok.Data;

@Data
public class UserPasswordRecover {
    private String usernameEmail;
    private String password;
    private String passwordCheck;
}
