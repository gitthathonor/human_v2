package site.metacoding.humancloud.dto.recruit;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.domain.company.Company;
import site.metacoding.humancloud.domain.recruit.Recruit;

public class RecruitReqDto {

    @Getter
    @Setter
    public static class RecruitSaveReqDto {
        private Integer recruitId;
        private String recruitTitle;
        private String recruitCareer;
        private Integer recruitSalary;
        private String recruitLocation;
        private Integer recruitReadCount;
        private String recruitContent;
        private Integer recruitCompanyId;
        private String recruitDeadline;

        private List<String> recruitCategoryList;

        public Recruit toEntity() {

            return Recruit.builder().recruitTitle(recruitTitle).recruitCareer(recruitCareer)
                    .recruitSalary(recruitSalary).recruitLocation(recruitLocation).recruitReadCount(recruitReadCount)
                    .recruitContent(recruitContent).recruitCompanyId(recruitCompanyId).recruitDeadline(recruitDeadline)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class RecruitUpdateReqDto {
        private Integer recruitId;
        private String recruitTitle;
        private String recruitCareer;
        private Integer recruitSalary;
        private String recruitLocation;
        private Integer recruitReadCount;
        private String recruitContent;
        private Integer recruitCompanyId;
        private String recruitDeadline;

        private List<String> recruitCategoryList;

        public Recruit toEntity() {
            return Recruit.builder().recruitId(recruitId).recruitTitle(recruitTitle).recruitCareer(recruitCareer)
                    .recruitSalary(recruitSalary).recruitLocation(recruitLocation).recruitReadCount(recruitReadCount)
                    .recruitContent(recruitContent).recruitCompanyId(recruitCompanyId).recruitDeadline(recruitDeadline)
                    .build();
        }
    }

}
