package site.metacoding.humancloud.dto.resume;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ResumeReqDto {

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class ResumeSaveReqDto {
    private Integer resumeId;
    private Integer resumeUserId;
    private String resumeTitle;
    private String resumeEducation;
    private String resumeCareer;
    private String resumePhoto;
    private String resumeLink;
    private List<String> categoryList;
  }

}
