package site.metacoding.humancloud.dto.apply;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.humancloud.domain.apply.Apply;

public class ApplyReqDto {
    @Getter
    @Setter
    public class ApplySaveReqDto {
        private Integer applyRecruitId;
        private Integer applyResumeId;
        private Timestamp applyCreatedAt;

        public ApplySaveReqDto(Integer applyRecruitId, Integer applyResumeId, Timestamp applyCreatedAt) {
            this.applyRecruitId = applyRecruitId;
            this.applyResumeId = applyResumeId;
            this.applyCreatedAt = applyCreatedAt;
        }

    }
}
