package site.metacoding.humancloud.dto.company;

import java.sql.Timestamp;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.metacoding.humancloud.domain.company.Company;
import site.metacoding.humancloud.domain.recruit.Recruit;

public class CompanyRespDto {

    @Getter
    @Setter
    public static class CompanyFindById {
        private Integer companyId;
        private String companyUsername;
        private String companyPassword;
        private String companyName;
        private String companyEmail;
        private String companyPhoneNumber;
        private String companyAddress;
        private String companyLogo;
        private Timestamp companyCreatedAt;

        public Company toEntity() {
            return Company.builder()
                    .companyId(companyId)
                    .companyUsername(companyUsername)
                    .companyName(companyName)
                    .companyEmail(companyEmail)
                    .companyPhoneNumber(companyPhoneNumber)
                    .companyAddress(companyAddress)
                    .companyLogo(companyLogo)
                    .companyCreatedAt(companyCreatedAt)
                    .build();
        }

        @Builder
        public CompanyFindById(Integer companyId, String companyUsername, String companyPassword, String companyName,
                String companyEmail, String companyPhoneNumber, String companyAddress, String companyLogo,
                Timestamp companyCreatedAt) {
            this.companyId = companyId;
            this.companyUsername = companyUsername;
            this.companyPassword = companyPassword;
            this.companyName = companyName;
            this.companyEmail = companyEmail;
            this.companyPhoneNumber = companyPhoneNumber;
            this.companyAddress = companyAddress;
            this.companyLogo = companyLogo;
            this.companyCreatedAt = companyCreatedAt;
        }

    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class CompanyJoinRespDto {
        private String companyName;
        private String companyEmail;
        private String companyPhoneNumber;
        private String companyAddress;
        private String companyLogo;

        public CompanyJoinRespDto(CompanyFindById companyFindById) {
            this.companyName = companyFindById.getCompanyName();
            this.companyEmail = companyFindById.getCompanyEmail();
            this.companyPhoneNumber = companyFindById.getCompanyPhoneNumber();
            this.companyAddress = companyFindById.getCompanyAddress();
            this.companyLogo = companyFindById.getCompanyLogo();
        }

        public void toPhoneNumber(String num) {
            this.companyPhoneNumber = num;
        }
    }

    @Setter
    @Getter
    public static class CompanyUpdateRespDto {
        private String companyUsername;
        private String companyName;
        private String companyEmail;
        private String companyPhoneNumber;
        private String companyAddress;
        private String companyLogo;

        public CompanyUpdateRespDto(CompanyFindById companyFindById) {
            this.companyUsername = companyFindById.getCompanyUsername();
            this.companyName = companyFindById.getCompanyName();
            this.companyEmail = companyFindById.getCompanyEmail();
            this.companyPhoneNumber = companyFindById.getCompanyPhoneNumber();
            this.companyAddress = companyFindById.getCompanyAddress();
            this.companyLogo = companyFindById.getCompanyLogo();
        }

    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class CompanyDetailRespDto {
        private String companyName;
        private String companyEmail;
        private String companyPhoneNumber;
        private String companyAddress;
        private String companyLogo;
        private boolean isSub;

        public CompanyDetailRespDto(CompanyFindById companyFindById, Boolean isSub) {
            this.companyName = companyFindById.getCompanyName();
            this.companyEmail = companyFindById.getCompanyEmail();
            this.companyPhoneNumber = companyFindById.getCompanyPhoneNumber();
            this.companyAddress = companyFindById.getCompanyAddress();
            this.companyLogo = companyFindById.getCompanyLogo();
            this.isSub = isSub;
        }

        public void toPhoneNumber(String num) {
            this.companyPhoneNumber = num;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class CompanyMypageRespDto {
        private String companyEmail; // 이메일
        private String companyPhoneNumber; // 전화번호
        private int applyCount; // 지원횟수
        private List<Recruit> recruitList; // 기업회원이 작성한 공고 리스트

    }

}
