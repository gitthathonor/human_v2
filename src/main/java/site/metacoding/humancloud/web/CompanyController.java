package site.metacoding.humancloud.web;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import site.metacoding.humancloud.dto.ResponseDto;
import site.metacoding.humancloud.dto.SessionUser;
import site.metacoding.humancloud.dto.company.CompanyReqDto.CompanyJoinReqDto;
import site.metacoding.humancloud.dto.company.CompanyReqDto.CompanyUpdateReqDto;
import site.metacoding.humancloud.service.CompanyService;
import site.metacoding.humancloud.service.SubscribeService;
import site.metacoding.humancloud.util.annotation.Auth;

@RestController
@RequiredArgsConstructor
public class CompanyController {

	private final CompanyService companyService;
	private final SubscribeService subscribeService;
	private final HttpSession session;

	// 기업 회원가입
	@PostMapping(value = "/company/join", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseDto<?> joinCompanyInfo(@RequestPart("file") MultipartFile file,
			@RequestPart("companyJoinReqDto") CompanyJoinReqDto companyJoinReqDto) throws Exception {
		return new ResponseDto<>(1, "기업 등록 성공", companyService.기업회원등록(file, companyJoinReqDto));
	}

	// 기업 정보 상세보기
	@GetMapping("/company/{id}")
	public ResponseDto<?> getCompanyDetail(@PathVariable Integer id) {
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		Integer userId = 0;
		if (sessionUser != null && sessionUser.getRole() == 0) {
			userId = sessionUser.getId();
			return new ResponseDto<>(1, "기업정보 상세보기 성공", companyService.기업정보상세보기(userId, id));
		}
		return new ResponseDto<>(1, "기업정보 상세보기 성공", companyService.기업정보상세보기(userId, id));
	}

	// 기업 리스트 보기
	@GetMapping("/company")
	public String getCompanyList(Model model, @Param("page") Integer page) {
		model.addAttribute("companyList", companyService.getCompanyList(page));
		return "page/company/companyList";
	}

	// 기업 정보 수정
	@Auth(role = 1)
	@PutMapping(value = "/s/company/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseDto<?> updateCompanyInfo(@PathVariable Integer id, @RequestPart("file") MultipartFile file,
			@RequestPart("companyUpdateReqDto") CompanyUpdateReqDto companyUpdateReqDto) throws Exception {
		return new ResponseDto<>(1, "기업정보 수정완료", companyService.기업정보수정(id, file, companyUpdateReqDto));
	}

	// 기업 정보 삭제
	@Auth(role = 1)
	@DeleteMapping("/s/company/{id}")
	public ResponseDto<?> deleteCompanyInfo(@PathVariable Integer id) {
		companyService.기업정보삭제(id);
		return new ResponseDto<>(1, "기업정보 삭제 완료", null);
	}

	@GetMapping("/company/mypage")
	public String viewMypage(@RequestParam Integer id, Model model) {
		Integer countApply = companyService.지원목록보기(id).size();
		if (companyService.지원목록보기(id) == null) {
			countApply = 0;
		}

		model.addAttribute("countApply", countApply);
		// model.addAttribute("company", companyService.getCompanyDetail(id));
		model.addAttribute("recruitList", companyService.채용공고리스트불러오기(id));
		return "page/user/mypage";
	}

	@GetMapping("/company/{companyId}/applyList")
	public String applyList(@PathVariable Integer companyId, Model model) {
		model.addAttribute("apply", companyService.지원목록보기(companyId));
		return "page/company/applyList";
	}

}
