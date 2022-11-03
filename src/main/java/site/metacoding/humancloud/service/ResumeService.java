package site.metacoding.humancloud.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.domain.category.CategoryDao;
import site.metacoding.humancloud.domain.resume.Resume;
import site.metacoding.humancloud.domain.resume.ResumeDao;
import site.metacoding.humancloud.domain.user.UserDao;
import site.metacoding.humancloud.dto.category.CategoryRespDto.CategoryFindByResumeId;
import site.metacoding.humancloud.dto.dummy.response.page.PagingDto;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeSaveReqDto;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeUpdateReqDto;
import site.metacoding.humancloud.dto.resume.ResumeRespDto.ResumeCategoryDto;
import site.metacoding.humancloud.dto.resume.ResumeRespDto.ResumeDetailRespDto;
import site.metacoding.humancloud.dto.resume.ResumeRespDto.ResumeFindAllDto;
import site.metacoding.humancloud.dto.resume.ResumeRespDto.ResumeFindAllRespDto;
import site.metacoding.humancloud.dto.resume.ResumeRespDto.ResumeFindById;
import site.metacoding.humancloud.dto.user.UserRespDto.UserFindById;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResumeService {

    private final ResumeDao resumeDao;
    private final CategoryDao categoryDao;
    private final UserDao userDao;

    @Transactional(rollbackFor = RuntimeException.class)
    public void 이력서삭제(Integer resumeId) {
        resumeDao.deleteById(resumeId);
        categoryDao.deleteByResumeId(resumeId);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void 이력서수정(Integer resumeId, MultipartFile file, ResumeUpdateReqDto resumeUpdateReqDto) throws Exception {
        Optional<ResumeFindById> resumeOP = resumeDao.findById(resumeId);

        if (!(resumeOP.isPresent())) {
            throw new RuntimeException("해당 하는 이력서가 없습니다. 이력서 Id : " + resumeId);
        }

        if (resumeOP.get().getResumeUserId() != resumeUpdateReqDto.getResumeUserId()) {
            throw new RuntimeException("해당 유저는 이력서 Id가 " + resumeId + "인 이력서를 수정할 권한이 없습니다.");
        }

        String imgName = insertImg(file);
        resumeUpdateReqDto.setResumePhoto(imgName);

        resumeDao.update(resumeUpdateReqDto);
        categoryDao.deleteByResumeId(resumeUpdateReqDto.getResumeId());
        for (String category : resumeUpdateReqDto.getCategoryList()) {
            Category categoryElement = new Category(resumeUpdateReqDto.getResumeId(), category);
            categoryDao.save(categoryElement);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void 이력서저장(MultipartFile file, ResumeSaveReqDto resumeSaveReqDto) throws Exception {

        String imgName = insertImg(file);
        resumeSaveReqDto.setResumePhoto(imgName);

        resumeDao.save(resumeSaveReqDto);
        for (String category : resumeSaveReqDto.getCategoryList()) {
            Category categoryElement = new Category(resumeSaveReqDto.getResumeId(), category);
            categoryDao.save(categoryElement);
        }
    }

    public ResumeDetailRespDto 이력서상세보기(@Param("resumeId") Integer resumeId, @Param("userId") Integer userId) {
        ResumeDetailRespDto resumeDetailRespDto = new ResumeDetailRespDto();
        Optional<UserFindById> userOP = userDao.findById(userId);
        Optional<ResumeFindById> resumeOP = resumeDao.findById(resumeId);
        List<CategoryFindByResumeId> categories = categoryDao.findByResumeId(resumeId);

        if (userOP.isPresent()) {
            resumeDetailRespDto.toUserEntity(userOP.get());
        } else {
            throw new RuntimeException("해당하는 Id를 가진 유저가 없습니다. 유저 ID : " + userId);
        }

        if (resumeOP.isPresent()) {
            resumeDetailRespDto.toResumeEntity(resumeOP.get());
        } else {
            throw new RuntimeException("해당하는 Id를 가진 이력서가 없습니다. 이력서 ID : " + resumeId);
        }

        resumeDetailRespDto.setCategoryList(categories);

        return resumeDetailRespDto;

    }

    // 이력서 목록
    public ResumeFindAllRespDto 이력서목록보기(Integer page) {
        if (page == null) {
            page = 0;
        }
        int startNum = page * 20;
        PagingDto paging = resumeDao.paging(page);
        paging.dopaging();
        ResumeFindAllRespDto resumeFindAllRespDto = new ResumeFindAllRespDto();
        resumeFindAllRespDto.dopaging(paging);

        resumeFindAllRespDto.setResumeList(resumeDao.findAll(startNum));
        resumeFindAllRespDto.setCategoryList(categoryDao.distinctName());

        return resumeFindAllRespDto;
    }

    public List<Resume> 분류별이력서목록보기(String category) {
        List<Category> categories = categoryDao.findByName(category);

        List<Resume> resumes = new ArrayList<>();

        for (Category c : categories) {
            if (c.getCategoryResumeId() != null) {
                // resumes.add(resumeDao.findById(c.getCategoryResumeId()));
            }
        }
        return resumes;
    }

    public List<ResumeFindAllDto> 정렬하기(@Param("orderList") String orderList, @Param("companyId") Integer companyId) {
        if (orderList.equals("recent")) {
            return 최신순보기();
        } else if (orderList.equals("career")) {
            return 경력순보기();
        } else if (orderList.equals("education")) {
            return 학력순보기();
        } else {
            return 추천순보기(companyId);
        }
    }

    public List<ResumeFindAllDto> 최신순보기() {
        return resumeDao.orderByCreatedAt();
    }

    public List<ResumeFindAllDto> 경력순보기() {
        return resumeDao.orderByCareer();
    }

    public List<ResumeFindAllDto> 학력순보기() {
        return resumeDao.orderByEducation();
    }

    public List<ResumeFindAllDto> 추천순보기(Integer companyId) {
        return resumeDao.orderByRecommend(companyId);
    }

    public void 열람횟수증가(Integer resumeId) {
        resumeDao.updateReadCount(resumeId);
    }

    private String insertImg(MultipartFile file) throws Exception {
        int pos = file.getOriginalFilename().lastIndexOf(".");
        String extension = file.getOriginalFilename().substring(pos + 1);
        String filePath = "C:\\temp\\img\\";
        String imgSaveName = UUID.randomUUID().toString();
        String imgName = imgSaveName + "." + extension;

        File makeFileFolder = new File(filePath);
        if (!makeFileFolder.exists()) {
            if (!makeFileFolder.mkdir()) {
                throw new Exception("File.mkdir():Fail.");
            }
        }

        File dest = new File(filePath, imgName);
        try {
            Files.copy(file.getInputStream(), dest.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgName;
    }
}
