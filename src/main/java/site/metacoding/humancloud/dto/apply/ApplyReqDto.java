package site.metacoding.humancloud.dto.apply;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ApplyReqDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ApplySaveReqDto {
        private Integer applyId;
        private Integer applyRecruitId;
        private Integer applyResumeId;
    }
}
