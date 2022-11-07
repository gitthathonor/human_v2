package site.metacoding.humancloud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.domain.category.CategoryDao;
import site.metacoding.humancloud.domain.company.Company;
import site.metacoding.humancloud.domain.company.CompanyDao;
import site.metacoding.humancloud.domain.recruit.Recruit;
import site.metacoding.humancloud.domain.recruit.RecruitDao;
import site.metacoding.humancloud.domain.resume.ResumeDao;
import site.metacoding.humancloud.dto.company.CompanyRespDto.CompanyFindById;
import site.metacoding.humancloud.dto.dummy.request.recruit.SaveDto;
import site.metacoding.humancloud.dto.dummy.response.page.PagingDto;
import site.metacoding.humancloud.dto.dummy.response.recruit.CompanyRecruitDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitSaveReqDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitUpdateReqDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto.CompanyRecruitDtoRespDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto.RecruitDetailRespDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto.RecruitListByCompanyIdRespDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecruitService {

    private final RecruitDao recruitDao;
    private final CategoryDao categoryDao;
    private final CompanyDao companyDao; // 공고 작성 회사 정보 to Object
    private final ResumeDao resumeDao; // 이력서 목록 findByUserId to LIST

    
    public Optional<RecruitDetailRespDto> 공고상세페이지(Integer recruitId, Integer userId) {
        Optional<RecruitDetailRespDto> recruitOP = recruitDao.findById(recruitId);

        if(recruitOP.isPresent()){
            List<Category> categoryList = categoryDao.findByRecruitId(recruitId);
            //Optional<List<RecruitListByCompanyIdRespDto>> recruitListByCompanyId = recruitDao
             //       .findByCompanyId(recruitOP.get().getRecruitCompanyId());
            recruitOP.get().setResume(resumeDao.findByUserId(userId));
            recruitOP.get().setCategory(categoryList);
            //recruitOP.get().setRecruitListByCompanyId(recruitListByCompanyId.get());
    
        }else{
            throw new RuntimeException("공고가 존재하지 않습니다");
        }
        
        return recruitOP;
        
    }

    @Transactional
    public RecruitRespDto 구인공고업데이트(Integer id, RecruitUpdateReqDto recruitUpdateReqDto) {

        recruitUpdateReqDto.setRecruitId(id); // URL 로 ID 받는 값을 주입 해줘야 함
        Optional<Recruit> recruitPS = recruitDao.findByIdyet(id);
        if (recruitPS.isPresent()) {
            Category category = new Category(id, null, null);

            // 기존의 카테고리 없애고
            categoryDao.deleteByRecruitId(id);
            // 새로 수정된 사항대로 체크리스트 INSERT
            for (String i : recruitUpdateReqDto.getRecruitCategoryList()) {
                category.setCategoryName(i);
                categoryDao.save(category);
            }
            recruitDao.update(recruitUpdateReqDto);
            return new RecruitRespDto(recruitUpdateReqDto);
        } else {
            throw new RuntimeException("Recruit : 업데이트 할 항목이 존재하지 않습니다.");
        }
    }

    @Transactional
    public RecruitRespDto 구인공고작성(RecruitSaveReqDto recruitSaveReqDto) {

        recruitDao.save(recruitSaveReqDto);
        Category category = new Category(recruitSaveReqDto.getRecruitId(), null, null);

        for (String i : recruitSaveReqDto.getRecruitCategoryList()) {
            category.setCategoryName(i);
            categoryDao.save(category);
        }
        return new RecruitRespDto(recruitSaveReqDto);
    }


    public List<CompanyRecruitDtoRespDto> 메인공고목록보기() {
        Optional<List<CompanyRecruitDtoRespDto>> recruitPS = recruitDao.joinCompanyRecruit(0);
        List<CompanyRecruitDtoRespDto> result = new ArrayList<>();
        int endFor;
        if(recruitPS.isPresent()){
            if (recruitPS.get().size() < 5) {
                endFor = recruitPS.get().size();
            } else {
                endFor = 6;
            }
    
            for (int i = 0; i < endFor; i++) {
                result.add(recruitPS.get().get(i));
            }
            return result;
        }else{
            throw new RuntimeException("목록이 없습네당");
        }
        

        
    }

    public Map<String, Object> 채용공고목록보기(Integer page) {
        if (page == null) {
            page = 0;
        }
        int startNum = page * 20;
        PagingDto paging = recruitDao.paging(page);
        paging.dopaging();

        Map<String, Object> recruitList = new HashMap<>();
        recruitList.put("paging", paging);
        recruitList.put("recruit", recruitDao.joinCompanyRecruit(startNum));
        recruitList.put("category", categoryDao.distinctName());
        return recruitList;
    }

    public List<Recruit> 분류별채용공고목록보기(String categoryName, Integer page) {
        // 페이징
        if (page == null) {
            page = 0;
        }
        int startNum = page * 20;
        PagingDto paging = recruitDao.paging(page);
        paging.dopaging();

        // 비즈니스로직
        List<Recruit> recruits = recruitDao.findByCategoryName(startNum);
        return recruits;
    }

    public List<Recruit> 정렬하기(@Param("orderList") String orderList, Integer userId) {
        if (orderList.equals("recent")) {
            return 최신순보기();
        } else if (orderList.equals("career")) {
            return 경력순보기();
        } else {
            return 추천순보기(userId);
        }
    }

    public List<Recruit> 최신순보기() {
        return recruitDao.orderByCreatedAt();
    }

    public List<Recruit> 경력순보기() {
        return recruitDao.orderByCareer();
    }

    public void 최신순기업리스트() {
        List<Company> companies = new ArrayList<>();
        List<Recruit> recruitPS = recruitDao.orderByCreatedAt(); // 내림차순 작성일 정렬
        for (Recruit r : recruitPS) {
            Optional<CompanyFindById> companyPS = companyDao.findById(r.getRecruitCompanyId());
            if (companies.size() > 5) {
                break;
            }
        }
    }

    public List<Recruit> 추천순보기(Integer userId) {
        return recruitDao.orderByrecommend(userId);
    }

    @Transactional
    public Integer 공고삭제하기(Integer recruitId) {
        Optional<RecruitDetailRespDto> recruitOP = recruitDao.findById(recruitId);
        if (recruitOP.isPresent()) {
            // 기존의 카테고리 없애고
            categoryDao.deleteByRecruitId(recruitId);
            recruitDao.deleteById(recruitId);
            return 1;
        } else {
            throw new RuntimeException("삭제 할 게시글이 존재하지 않습니다.");
        }

    }
}