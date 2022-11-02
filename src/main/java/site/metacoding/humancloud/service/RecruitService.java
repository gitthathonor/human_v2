package site.metacoding.humancloud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.domain.category.CategoryDao;
import site.metacoding.humancloud.domain.company.Company;
import site.metacoding.humancloud.domain.company.CompanyDao;
import site.metacoding.humancloud.domain.recruit.Recruit;
import site.metacoding.humancloud.domain.recruit.RecruitDao;
import site.metacoding.humancloud.dto.dummy.request.recruit.SaveDto;
import site.metacoding.humancloud.dto.dummy.response.page.PagingDto;
import site.metacoding.humancloud.dto.dummy.response.recruit.CompanyRecruitDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitSaveReqDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitUpdateReqDto;

@RequiredArgsConstructor
@Service
public class RecruitService {

    private final RecruitDao recruitDao;
    private final CategoryDao categoryDao;
    private final CompanyDao companyDao;

    public Recruit 공고상세페이지(Integer recruitId) {
        Recruit recruitPS = recruitDao.findById(recruitId);
        Company companyPS = companyDao.findById(recruitPS.getRecruitCompanyId());
        List<Category> categoryList = categoryDao.findByRecruitId(recruitId);
        List<Recruit> recruitListByCompanyId = recruitDao.findByCompanyId(recruitPS.getRecruitCompanyId());
        recruitPS.setCategory(categoryList);
        recruitPS.setCompany(companyPS);
        recruitPS.setRecruitListByCompanyId(recruitListByCompanyId);
        return recruitPS;
    }

    @Transactional
    public void 구인공고업데이트(Integer id, RecruitUpdateReqDto recruitUpdateReqDto) {

        recruitUpdateReqDto.setRecruitId(id);

        Recruit recruitPS = recruitDao.findById(id);
        if (recruitPS != null) {
            Category category = new Category(id, null, null);

            // 기존의 카테고리 없애고
            categoryDao.deleteByRecruitId(id);
            // 새로 수정된 사항대로 체크리스트 INSERT
            for (String i : recruitUpdateReqDto.getRecruitCategoryList()) {
                category.setCategoryName(i);
                categoryDao.save(category);
            }
            recruitDao.update(recruitUpdateReqDto);
        }
    }

    @Transactional
    public void 구인공고작성(RecruitSaveReqDto recruitSaveReqDto) {
        recruitDao.save(recruitSaveReqDto);
        Category category = new Category(recruitSaveReqDto.getRecruitId(), null, null);

        for (String i : recruitSaveReqDto.getRecruitCategoryList()) {
            category.setCategoryName(i);
            categoryDao.save(category);
        }

        return;
    }

    public List<CompanyRecruitDto> 메인공고목록보기() {
        List<CompanyRecruitDto> recruitPS = recruitDao.joinCompanyRecruit(0);
        List<CompanyRecruitDto> result = new ArrayList<>();
        int endFor;
        if (recruitPS.size() < 5) {
            endFor = recruitPS.size();
        } else {
            endFor = 6;
        }

        for (int i = 0; i < endFor; i++) {
            result.add(recruitPS.get(i));
        }

        return result;
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
            Company companyPS = companyDao.findById(r.getRecruitCompanyId());
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
        Recruit recruitPS = recruitDao.findById(recruitId);
        if (recruitPS != null) {
            // 기존의 카테고리 없애고
            categoryDao.deleteByRecruitId(recruitId);
            recruitDao.deleteById(recruitId);

            return 1;
        }
        return 0;

    }
}