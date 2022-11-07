package site.metacoding.humancloud.domain.recruit;

import java.util.List;
import java.util.Optional;

import site.metacoding.humancloud.dto.dummy.response.page.PagingDto;
import site.metacoding.humancloud.dto.dummy.response.recruit.CompanyRecruitDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitSaveReqDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitUpdateReqDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto.CompanyRecruitDtoRespDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto.RecruitDetailRespDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto.RecruitListByCompanyIdRespDto;

public interface RecruitDao {
	public void save(RecruitSaveReqDto recruitSaveReqDto);

	public Optional<RecruitDetailRespDto> findById(Integer id);

	public List<Recruit> findAll();

	public void update(RecruitUpdateReqDto recruitUpdateReqDto);

	public void deleteById(Integer id);

	public List<Recruit> orderByCreatedAt();

	public List<Recruit> orderByCareer();

	public void findByCareer();

	public Optional<List<RecruitListByCompanyIdRespDto>> findByCompanyId(Integer id);

	public Optional<List<CompanyRecruitDtoRespDto>> joinCompanyRecruit(int startNum);

	public List<Recruit> orderByrecommend(Integer userId);

	public List<RecruitDetailRespDto> findByTitle(String recruitTitle);

	public PagingDto paging(Integer page);

	public List<Recruit> findByCategoryName(Integer startNum);

	public void deleteByCompanyId(Integer companyId);
}
