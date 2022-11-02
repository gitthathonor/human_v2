package site.metacoding.humancloud.web;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.domain.recruit.Recruit;
import site.metacoding.humancloud.domain.user.User;
import site.metacoding.humancloud.dto.ResponseDto;
import site.metacoding.humancloud.dto.dummy.request.recruit.SaveDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitSaveReqDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitUpdateReqDto;
import site.metacoding.humancloud.service.ApplyService;
import site.metacoding.humancloud.service.CompanyService;
import site.metacoding.humancloud.service.RecruitService;

@RequiredArgsConstructor
@RestController
public class RecruitController {

  private final RecruitService recruitService;
  private final CompanyService companyService;
  private final ApplyService applyService;

  // main
  @GetMapping("/")
  public ResponseDto<?> main(Model model) {
    model.addAttribute("list", recruitService.메인공고목록보기());
    return new ResponseDto<>(1, "성공", recruitService.메인공고목록보기());
  }

  // @GetMapping("recruit/update/{id}")
  // public ResponseDto<?> updateFrom(@PathVariable(required = false) Integer id,
  // Model model) {
  // Recruit recruitPS = recruitService.공고상세페이지(id);
  // model.addAttribute("Recruit", recruitPS);
  // return new ResponseDto<>(1, "성공", recruitService.공고상세페이지(id));
  // }

  @PutMapping("recruit/update/{id}")
  public @ResponseBody ResponseDto<?> update(@PathVariable Integer id,
      @RequestBody RecruitUpdateReqDto recruitUpdateReqDto) {

    recruitService.구인공고업데이트(id, recruitUpdateReqDto);

    return new ResponseDto<>(1, "성공", null);
  }

  @GetMapping("/recruit/detail/{id}/{userId}")
  public ResponseDto<?> recruit_Detail(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId,
      Model model) {

    // model.addAttribute("Recruit", recruitPS);
    // model.addAttribute("company",
    // companyService.getCompanyDetail(recruitPS.getRecruitCompanyId()));
    // model.addAttribute("apply", applyService.이력서목록보기(userId));
    return new ResponseDto<>(1, "성공", recruitService.공고상세페이지(id, userId));
  }

  // @GetMapping("/recruit/saveForm/{companyId}")
  // public String writeFrom(@PathVariable Integer companyId, Model model) {
  // model.addAttribute("company", companyService.getCompanyDetail(companyId));
  // return "page/recruit/saveForm";
  // }

  @PostMapping("/recruit/save")
  public @ResponseBody ResponseDto<?> write(@RequestBody RecruitSaveReqDto recruitSaveReqDto) {
    return new ResponseDto<>(1, "성공", recruitService.구인공고작성(recruitSaveReqDto));
  }

  @GetMapping("/recruit/list")
  public ResponseDto<?> viewList(Model model, @Param("page") Integer page) {
    model.addAttribute("recruits", recruitService.채용공고목록보기(page));
    return new ResponseDto<>(1, "성공", model);
  }

  @PostMapping("/recruit/category")
  public @ResponseBody ResponseDto<?> viewCategory(@RequestBody Category category, @Param("page") Integer page) {
    return new ResponseDto<>(1, "OK", recruitService.분류별채용공고목록보기(category.getCategoryName(), page));
  }

  @PostMapping("/recruit/list")
  public @ResponseBody ResponseDto<?> orderRecruitList(@RequestParam("order") String order, @RequestBody User user) {
    return new ResponseDto<>(1, "ok", recruitService.정렬하기(order, user.getUserId()));
  }

  @DeleteMapping("/recruit/delete/{recruitId}")
  public @ResponseBody ResponseDto<?> recruitDelete(@PathVariable Integer recruitId) {
    Integer code = recruitService.공고삭제하기(recruitId);
    return new ResponseDto<>(code, "ok", null);
  }

}
