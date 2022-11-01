package site.metacoding.humancloud.dto.dummy.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginDto {
    private String username;
    private String password;

    public LoginDto(String username) {
        this.username = username;
    }
}