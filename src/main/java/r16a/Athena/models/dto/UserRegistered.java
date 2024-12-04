package r16a.Athena.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistered {
    private String redirectUrl;
    private UserRestricted user;
}
