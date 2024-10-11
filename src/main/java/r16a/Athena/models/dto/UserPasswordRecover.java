package r16a.Athena.models.dto;

import lombok.Data;

@Data
public class UserPasswordRecover {
    private String userName;
    private String password;
    private String passwordCheck;
}
