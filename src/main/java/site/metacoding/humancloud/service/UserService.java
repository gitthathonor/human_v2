package site.metacoding.humancloud.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.humancloud.domain.company.Company;
import site.metacoding.humancloud.domain.resume.Resume;
import site.metacoding.humancloud.domain.resume.ResumeDao;
import site.metacoding.humancloud.domain.subscribe.SubscribeDao;
import site.metacoding.humancloud.domain.user.User;
import site.metacoding.humancloud.domain.user.UserDao;
import site.metacoding.humancloud.dto.dummy.request.user.JoinDto;
import site.metacoding.humancloud.dto.dummy.request.user.LoginDto;
import site.metacoding.humancloud.dto.dummy.response.user.CompanyRankingDto;
import site.metacoding.humancloud.dto.user.UserReqDto.JoinReqDto;
import site.metacoding.humancloud.dto.user.UserReqDto.UserUpdateReqDto;
import site.metacoding.humancloud.dto.user.UserRespDto.JoinRespDto;
import site.metacoding.humancloud.dto.user.UserRespDto.UserMypageRespDto;
import site.metacoding.humancloud.dto.user.UserRespDto.JoinRespDto.UserUpdateRespDto;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserDao userDao;
    private final ResumeDao resumeDao;
    private final SubscribeDao subscribeDao;

    public JoinRespDto 회원가입(JoinReqDto joinReqDto) {
        boolean checkUsername = 유저네임중복체크(joinReqDto.getUsername());
        if (checkUsername == false) {
            throw new RuntimeException("아이디 중복 오류");
        }
        User user = joinReqDto.toEntity();
        userDao.save(user);
        return new JoinRespDto(user);
    }

    public UserUpdateRespDto 회원업데이트(Integer id, UserUpdateReqDto userUpdateReqDto) {
        User userPS = userDao.findById(id);
        userUpdateReqDto.setUserId(id);

        if (userPS == null) {
            throw new RuntimeException("잘못된 요청입니다");
        }

        // 영속화
        userPS.update(userUpdateReqDto);
        userDao.update(userPS);

        return new UserUpdateRespDto(userPS);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void 회원탈퇴(Integer id) {
        User userPS = userDao.findById(id);
        if (userPS == null) {
            throw new RuntimeException("잘못된 요청입니다");
        }

        // 해당 유저의 이력서 삭제
        List<Resume> resumes = resumeDao.findByUserId(id);
        if (resumes != null) {
            resumeDao.deleteByUserId(id);
        }

        userDao.deleteById(id);
    }

    @Transactional
    public void 마이페이지보기(Integer id) {
        // 리스폰스 생성 및 user정보
        UserMypageRespDto userMypageRespDto = new UserMypageRespDto(유저정보보기(id));
        // 구독 기업 목록
        userMypageRespDto.setCompanyList(subscribeDao.findCompanyByUserId(id));
        // 작성한 이력서 목록
        userMypageRespDto.setResumeList(resumeDao.findByUserId(id));

    }

    public boolean 유저네임중복체크(String username) {
        User userPS = userDao.findAllUsername(username);
        if (userPS == null) {
            return true;
        }
        return false;
    }

    // public User 로그인(LoginDto loginDto) {
    // User userPS = userDao.findByUsername(loginDto.getUsername());
    // if (userPS == null) {
    // return null;
    // }
    // if (loginDto.getPassword().equals(userPS.getPassword())) {
    // return userPS;
    // }
    // return null;
    // }

    // 서비스 내에서 사용하는 메서드
    public User 유저정보보기(Integer userId) {
        // 유저 정보
        User userPS = userDao.findById(userId);

        // 전화번호 포매팅
        String fomat = "(\\d{3})(\\d{3,4})(\\d{4})";
        if (Pattern.matches(fomat, userPS.getPhoneNumber())) {
            String result = userPS.getPhoneNumber().replaceAll(fomat, "$1-$2-$3");
        }
        return userPS;
    }

    public Map<String, Object> 이력서보기(Integer userId) {
        // 열람 횟수 보기(리절트 타입 인트면 좋음)

        // 이력서 목록보기 (제목, 등록카테고리, 날짜 정도 필요)
        try {
            Integer countResume = resumeDao.sumReadCount(userId).getResumeReadCount();

            List<Resume> resumePS = resumeDao.findByUserId(userId); // 리절트 타입 : resume

            Map<String, Object> result = new HashMap<>();
            result.put("readCount", countResume);
            result.put("resume", resumePS);

            return result;
        } catch (NullPointerException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("readCount", 0);
            result.put("resume", null);

            return result;
        }

    }

    public List<CompanyRankingDto> 추천기업목록보기() {
        for (CompanyRankingDto c : userDao.findByRank()) {
            System.out.println(c.getName());
        }
        return userDao.findByRank();
    }

}
