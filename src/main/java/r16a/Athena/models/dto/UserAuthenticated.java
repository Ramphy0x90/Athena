package r16a.Athena.models.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class UserAuthenticated extends UserRestricted {
    private String token;
}
