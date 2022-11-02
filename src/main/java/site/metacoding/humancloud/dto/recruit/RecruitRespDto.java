package site.metacoding.humancloud.dto.recruit;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.domain.company.Company;
import site.metacoding.humancloud.domain.recruit.Recruit;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitSaveReqDto;

public class RecruitRespDto {

    @Getter
    @Setter
    public static class RecruitSaveRespDto {
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

        public RecruitSaveRespDto(RecruitSaveReqDto recruitSaveReqDto) {
            this.recruitId = recruitSaveReqDto.getRecruitId();
            this.recruitTitle = recruitSaveReqDto.getRecruitTitle();
            this.recruitCareer = recruitSaveReqDto.getRecruitCareer();
            this.recruitSalary = recruitSaveReqDto.getRecruitSalary();
            this.recruitLocation = recruitSaveReqDto.getRecruitLocation();
            this.recruitReadCount = recruitSaveReqDto.getRecruitReadCount();
            this.recruitContent = recruitSaveReqDto.getRecruitContent();
            this.recruitCompanyId = recruitSaveReqDto.getRecruitCompanyId();
            this.recruitDeadline = recruitSaveReqDto.getRecruitDeadline();
            this.recruitCategoryList = recruitSaveReqDto.getRecruitCategoryList();
        }

    }

    @Getter
    @Setter
    public static class RecruitViewRespDto {
        private Integer recruitId;
        private String recruitTitle;
        private String recruitCareer;
        private Integer recruitSalary;
        private String recruitLocation;
        private String recruitContent;
        private Integer recruitReadCount;
        private Integer recruitCompanyId;
        private String recruitDeadline;
        private Timestamp recruitCreatedAt;
        private String recruitStartDay;

        private Company company;
        private List<Category> category;
        private List<Recruit> recruitListByCompanyId;

    }

    // public void setRecruitCreatedAt(Timestamp recruitCreatedAt) {
    // SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
    // String t = form.format(recruitCreatedAt);
    // this.recruitStartDay = t;
    // }

}
