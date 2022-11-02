package site.metacoding.humancloud.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.humancloud.domain.company.Company;
import site.metacoding.humancloud.domain.company.CompanyDao;
import site.metacoding.humancloud.domain.recruit.Recruit;
import site.metacoding.humancloud.domain.recruit.RecruitDao;
import site.metacoding.humancloud.domain.resume.Resume;
import site.metacoding.humancloud.domain.resume.ResumeDao;
import site.metacoding.humancloud.domain.subscribe.SubscribeDao;
import site.metacoding.humancloud.dto.company.CompanyReqDto.CompanyJoinReqDto;
import site.metacoding.humancloud.dto.company.CompanyReqDto.CompanyUpdateReqDto;
import site.metacoding.humancloud.dto.company.CompanyRespDto.CompanyFindById;
import site.metacoding.humancloud.dto.dummy.response.page.PagingDto;
import site.metacoding.humancloud.util.SHA256;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyDao companyDao;
	private final SubscribeDao subscribeDao;
	private final RecruitDao recruitDao;
	private final ResumeDao resumeDao;
	private final SHA256 sha256;

	// 회원 username 중복체크
	public boolean checkSameUsername(String companyUsername) {
		Company companyPS = companyDao.findAllUsername(companyUsername);
		if (companyPS == null) {
			return false;
		} else {
			return true;
		}
	}

	// 기업 회원 등록
	@Transactional
	public void 기업회원등록(MultipartFile file, CompanyJoinReqDto companyJoinReqDto) throws Exception {
		int pos = file.getOriginalFilename().lastIndexOf(".");
		String extension = file.getOriginalFilename().substring(pos + 1);
		String filePath = "C:\\temp\\img\\";
		String logoSaveName = UUID.randomUUID().toString();
		String logo = logoSaveName + "." + extension;

		File makeFileFolder = new File(filePath);
		if (!makeFileFolder.exists()) {
			if (!makeFileFolder.mkdir()) {
				throw new Exception("File.mkdir():Fail.");
			}
		}

		File dest = new File(filePath, logo);
		try {
			Files.copy(file.getInputStream(), dest.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String encPassword = sha256.encrypt(companyJoinReqDto.getCompanyPassword());
		companyJoinReqDto.setCompanyPassword(encPassword);
		Company company = companyJoinReqDto.toEntity(logo);
		companyDao.save(company);
	}

	// // 기업 정보 상세보기
	// public Company getCompanyDetail(Integer companyId) {
	// Company companyPS = companyDao.findById(companyId);

	// // 전화번호 포매팅
	// String fomat = "(\\d{2,3})(\\d{3,4})(\\d{4})";
	// if (Pattern.matches(fomat, companyPS.getCompanyPhoneNumber())) {
	// String result = companyPS.getCompanyPhoneNumber().replaceAll(fomat,
	// "$1-$2-$3");
	// companyPS.toPhoneNumber(result);
	// }

	// return companyPS;
	// }

	// 기업 리스트 보기
	public Map<String, Object> getCompanyList(Integer page) {
		if (page == null) {
			page = 0;
		}
		int startNum = page * 20;
		PagingDto paging = companyDao.paging(page);
		paging.dopaging();

		Map<String, Object> result = new HashMap<>();
		result.put("paging", paging);
		result.put("list", companyDao.findAll(startNum));
		return result;
	}

	// 기업정보 수정
	@Transactional
	public CompanyFindById 기업정보수정(Integer id, MultipartFile file, CompanyUpdateReqDto companyUpdateReqDto)
			throws Exception {
		// Optional로 영속화 및 null 체크
		Optional<CompanyFindById> companyOP = companyDao.findById(id);
		if (companyOP.isEmpty()) {
			throw new RuntimeException("기업정보가 없습니다.");
		}

		// 이미지 파일 작업
		int pos = file.getOriginalFilename().lastIndexOf(".");
		String extension = file.getOriginalFilename().substring(pos + 1);
		String filePath = "C:\\temp\\img\\";
		String logoSaveName = UUID.randomUUID().toString();
		String logo = logoSaveName + "." + extension;

		File makeFileFolder = new File(filePath);
		if (!makeFileFolder.exists()) {
			if (!makeFileFolder.mkdir()) {
				throw new Exception("File.mkdir():Fail.");
			}
		}

		File dest = new File(filePath, logo);
		try {
			Files.copy(file.getInputStream(), dest.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		companyUpdateReqDto.setCompanyLogo(logo);

		String encPassword = sha256.encrypt(companyUpdateReqDto.getCompanyPassword());
		companyUpdateReqDto.setCompanyPassword(encPassword);

		// 2. updateDto를 companyPS에 업데이트
		Company companyPS = companyOP.get().toEntity();
		companyPS.update(companyUpdateReqDto);

		// 3. update
		companyDao.update(companyPS);
		log.debug("디버그 : " + id);
		Optional<CompanyFindById> companyOP2 = companyDao.findById(id);
		return companyOP2.get();
	}

	// 기업정보 삭제
	@Transactional
	public void deleteCompany(Integer id) {
		companyDao.deleteById(id);
	}

	// public SessionUser 로그인(CompanyLoginReqDto companyLoginReqDto) {
	// Company companyPS =
	// companyDao.findByUsername(companyLoginReqDto.getCompanyUsername());
	// String encPassword = sha256.encrypt(companyLoginReqDto.getCompanyPassword());
	// if (companyPS == null) {
	// throw new RuntimeException("회원가입 되지 않았습니다.");
	// } else {
	// if (!companyPS.getCompanyPassword().equals(encPassword)) {
	// throw new RuntimeException("아이디 혹은 패스워드가 잘못 입력되었습니다.");
	// }
	// return SessionUser.builder().company(companyPS).build();
	// }

	// }

	public List<Recruit> 채용공고리스트불러오기(Integer id) {
		for (int i = 0; i < recruitDao.findByCompanyId(id).size(); i++) {
			System.out.println(recruitDao.findByCompanyId(id).get(i).getRecruitTitle());
		}
		return recruitDao.findByCompanyId(id);
	}

	public List<Resume> 지원목록보기(Integer companyId) {
		return resumeDao.applyResumeList(companyId);
	}
}
