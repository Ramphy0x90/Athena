package r16a.Athena.models.dto;

import lombok.Data;

@Data
public class ClientRestricted {
    private Integer id;
    private String name;
    private String description;
    private String icon;
    private String baseUrl;
    private String postAuthUrl;
}
