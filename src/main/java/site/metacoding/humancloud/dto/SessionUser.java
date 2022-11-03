package site.metacoding.humancloud.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.metacoding.humancloud.domain.company.Company;
import site.metacoding.humancloud.domain.user.User;

@NoArgsConstructor
@Setter
@Getter
public class SessionUser {
    private User user;
    private Company company;
    private Integer statue; // 0 = user, 1 = company

    @Builder
    public SessionUser(User user, Company company, Integer statue) {
        this.user = user;
        this.company = company;
        this.statue = statue;
    }

}
