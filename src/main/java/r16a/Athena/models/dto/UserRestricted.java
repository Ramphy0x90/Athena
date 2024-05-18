package r16a.Athena.models.dto;

import lombok.Data;

@Data
public class UserRestricted {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String userName;
}
