package site.metacoding.humancloud.dto.resume;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.domain.resume.Resume;
import site.metacoding.humancloud.dto.user.UserRespDto.UserFindById;

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

  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public static class ResumeFindById {
    private Integer resumeId;
    private Integer resumeUserId;
    private String resumeTitle;
    private String resumeEducation;
    private String resumeCareer;
    private String resumePhoto;
    private String resumeLink;

    public Resume toEntity() {
      return Resume.builder()
          .resumeId(this.resumeId)
          .resumeUserId(this.resumeUserId)
          .resumeTitle(this.resumeTitle)
          .resumeEducation(this.resumeEducation)
          .resumeCareer(this.resumeCareer)
          .resumePhoto(this.resumePhoto)
          .resumeLink(this.resumeLink)
          .build();
    }
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

    public UserFindById toUserEntity(UserFindById userFindById) {
      this.name = userFindById.getName();
      this.email = userFindById.getEmail();
      this.phoneNumber = userFindById.getPhoneNumber();
      return userFindById;
    }

    public ResumeFindById toResumeEntity(ResumeFindById resumeFindById) {
      this.resumeId = resumeFindById.getResumeId();
      this.resumeUserId = resumeFindById.getResumeUserId();
      this.resumeTitle = resumeFindById.getResumeTitle();
      this.resumeEducation = resumeFindById.getResumeEducation();
      this.resumeCareer = resumeFindById.getResumeCareer();
      this.resumePhoto = resumeFindById.getResumePhoto();
      this.resumeLink = resumeFindById.getResumeLink();
      return resumeFindById;
    }

  }

  @Setter
  @Getter
  public static class ResumeUpdateRespDto {
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

    public UserFindById toUserEntity(UserFindById userFindById) {
      this.name = userFindById.getName();
      this.email = userFindById.getEmail();
      this.phoneNumber = userFindById.getPhoneNumber();
      return userFindById;
    }

    public ResumeFindById toResumeEntity(ResumeFindById resumeFindById) {
      this.resumeId = resumeFindById.getResumeId();
      this.resumeUserId = resumeFindById.getResumeUserId();
      this.resumeTitle = resumeFindById.getResumeTitle();
      this.resumeEducation = resumeFindById.getResumeEducation();
      this.resumeCareer = resumeFindById.getResumeCareer();
      this.resumePhoto = resumeFindById.getResumePhoto();
      this.resumeLink = resumeFindById.getResumeLink();
      return resumeFindById;
    }

  }

}
