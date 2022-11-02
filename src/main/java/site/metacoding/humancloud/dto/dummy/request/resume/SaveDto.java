package site.metacoding.humancloud.dto.dummy.request.resume;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveDto {
  private Integer resumeId;
  private Integer resumeUserId;
  private String resumeTitle;
  private String resumeEducation;
  private String resumeCareer;
  private String resumePhoto;
  private String resumeLink;
  private List<String> categoryList;

  public SaveDto(Integer resumeUserId, String resumeTitle, String resumeEducation, String resumeCareer,
      String resumePhoto, String resumeLink, List<String> categoryList) {
    this.resumeUserId = resumeUserId;
    this.resumeTitle = resumeTitle;
    this.resumeEducation = resumeEducation;
    this.resumeCareer = resumeCareer;
    this.resumePhoto = resumePhoto;
    this.resumeLink = resumeLink;
    this.categoryList = categoryList;
  }

}
