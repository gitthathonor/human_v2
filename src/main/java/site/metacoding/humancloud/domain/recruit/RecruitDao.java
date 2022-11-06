package site.metacoding.humancloud.domain.recruit;

import java.util.List;
import java.util.Optional;

import site.metacoding.humancloud.dto.company.CompanyRespDto.CompanyMypageRespDto.CompanyRecruitDto;
import site.metacoding.humancloud.dto.dummy.response.page.PagingDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitSaveReqDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitUpdateReqDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto.CompanyRecruitDtoRespDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto.RecruitDetailRespDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto.RecruitListByCompanyIdRespDto;

public interface RecruitDao {
	public void save(RecruitSaveReqDto recruitSaveReqDto);

	public Optional<Recruit> findByIdyet(Integer id);

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

	// 기업 마이페이지 시에 작성한 채용공고 리스트 보여주기
	public List<CompanyRecruitDto> findByCompanyId2(Integer companyId);
}
