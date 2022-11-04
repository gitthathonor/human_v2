package site.metacoding.humancloud.dto.resume;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

public class ResumeReqDto {

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
    private MultipartFile[] file;
  }

  @Getter
  @Setter
  public static class ResumeUpdateReqDto {
    private Integer resumeId;
    private Integer resumeUserId;
    private String resumeTitle;
    private String resumeEducation;
    private String resumeCareer;
    private String resumePhoto;
    private String resumeLink;
    private List<String> categoryList;
    private MultipartFile[] file;
  }
}
