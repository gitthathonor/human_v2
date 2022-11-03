package site.metacoding.humancloud.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import site.metacoding.humancloud.domain.user.User;
import site.metacoding.humancloud.domain.user.UserDao;
import site.metacoding.humancloud.dto.dummy.request.resume.UpdateDto;
import site.metacoding.humancloud.dto.dummy.response.page.PagingDto;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeSaveReqDto;
import site.metacoding.humancloud.dto.resume.ResumeRespDto.ResumeDetailRespDto;

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
    public void 이력서수정(Integer resumeId, UpdateDto updateDto) {
        resumeDao.update(updateDto);
        categoryDao.deleteByResumeId(resumeId);
        for (String category : updateDto.getCategoryList()) {
            Category categoryElement = new Category(resumeId, category);
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
        // User user = userDao.findById(userId);
        // Resume resume = resumeDao.findById(resumeId);
        // List<Category> categories = categoryDao.findByResumeId(resumeId);
        // log.debug("디버그 : " + user.getName() + user.getPhoneNumber());
        // log.debug("디버그 : " + resume.getResumeCareer() + resume.getResumeLink());
        // resumeDetailRespDto.toEntity(user);
        // resumeDetailRespDto.toEntity(resume);
        // resumeDetailRespDto.setCategoryList(categories);
        return resumeDetailRespDto;
    }

    // 이력서 목록
    public Map<String, Object> 이력서목록보기(Integer page) {
        if (page == null) {
            page = 0;
        }
        int startNum = page * 20;
        PagingDto paging = resumeDao.paging(page);
        paging.dopaging();

        Map<String, Object> resumeList = new HashMap<>();
        resumeList.put("paging", paging);
        resumeList.put("resume", resumeDao.findAll(startNum));
        resumeList.put("category", categoryDao.distinctName());
        return resumeList;
    }

    public List<Resume> 분류별이력서목록보기(String category) {
        List<Category> categories = categoryDao.findByName(category);

        List<Resume> resumes = new ArrayList<>();

        for (Category c : categories) {
            if (c.getCategoryResumeId() != null) {
                resumes.add(resumeDao.findById(c.getCategoryResumeId()));
            }
        }
        return resumes;
    }

    public List<Resume> 정렬하기(@Param("orderList") String orderList, @Param("companyId") Integer companyId) {
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

    public List<Resume> 최신순보기() {
        return resumeDao.orderByCreatedAt();
    }

    public List<Resume> 경력순보기() {
        return resumeDao.orderByCareer();
    }

    public List<Resume> 학력순보기() {
        return resumeDao.orderByEducation();
    }

    public List<Resume> 추천순보기(Integer companyId) {
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
