package r16a.Athena.models.dto;

import lombok.Data;

@Data
public class UserRegisterConfirm {
    private String usernameEmail;
    private Integer verificationCode;
}
