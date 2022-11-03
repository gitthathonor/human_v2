package site.metacoding.humancloud.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String username;
    private String password;
    private int role;
}
