package r16a.Athena.models.dto;

import lombok.Data;

@Data
public class UserRegister {
    private String name;
    private String surname;
    private String email;
    private String userName;
    private String password;
}
