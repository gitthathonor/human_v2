package site.metacoding.humancloud.dto.resume;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.domain.resume.Resume;
import site.metacoding.humancloud.domain.user.User;

public class ResumeRespDto {

  @Getter
  public static class ResumeSaveRespDto {
    private Integer resumeUserId;
    private String resumeTitle;
    private String resumeEducation;
    private String resumeCareer;
    private String resumePhoto;
    private String resumeLink;
    private List<String> categoryList;
  }

  @Setter
  @Getter
  public static class ResumeDetailRespDto {
    private Integer resumeId;
    private Integer resumeUserId;
    private String resumeTitle;
    private String resumeEducation;
    private String resumeCareer;
    private String resumePhoto;
    private String resumeLink;
    private String name;
    private String email;
    private String phoneNumber;
    private List<Category> categoryList;

    public User toEntity(User user) {
      this.name = user.getName();
      this.email = user.getEmail();
      this.phoneNumber = user.getPhoneNumber();
      return user;
    }

    public Resume toEntity(Resume resume) {
      this.resumeId = resume.getResumeId();
      this.resumeUserId = resume.getResumeUserId();
      this.resumeTitle = resume.getResumeTitle();
      this.resumeEducation = resume.getResumeEducation();
      this.resumeCareer = resume.getResumeCareer();
      this.resumePhoto = resume.getResumePhoto();
      this.resumeLink = resume.getResumeLink();
      return resume;
    }

  }
}
