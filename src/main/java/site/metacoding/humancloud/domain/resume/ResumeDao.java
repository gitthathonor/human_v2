package site.metacoding.humancloud.domain.resume;

import java.util.List;
import java.util.Optional;

import site.metacoding.humancloud.dto.dummy.response.page.PagingDto;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeSaveReqDto;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeUpdateReqDto;
import site.metacoding.humancloud.dto.resume.ResumeRespDto.ResumeFindAllDto;
import site.metacoding.humancloud.dto.resume.ResumeRespDto.ResumeFindById;

public interface ResumeDao {

	public void save(ResumeSaveReqDto ResumeSaveReqDto);

	public Optional<ResumeFindById> findById(Integer id);

	public List<ResumeFindAllDto> findAll(int startNum);

	public void update(ResumeUpdateReqDto resumeUpdateReqDto);

	public void deleteById(Integer id);

	public List<Resume> findByUserId(Integer userId);

	public void deleteByUserId(Integer userId);

	public Resume sumReadCount(Integer userId);

	public List<ResumeFindAllDto> orderByCareer();

	public List<ResumeFindAllDto> orderByEducation();

	public List<ResumeFindAllDto> orderByCreatedAt();

	public List<ResumeFindAllDto> orderByRecommend(Integer companyId);

	public PagingDto paging(Integer page);

	public void updateReadCount(Integer resumeId);

	public List<Resume> applyResumeList(Integer companyId);

}
