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

import lombok.RequiredArgsConstructor;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.domain.recruit.Recruit;
import site.metacoding.humancloud.domain.user.User;
import site.metacoding.humancloud.dto.ResponseDto;
import site.metacoding.humancloud.dto.dummy.request.recruit.SaveDto;
import site.metacoding.humancloud.service.ApplyService;
import site.metacoding.humancloud.service.CompanyService;
import site.metacoding.humancloud.service.RecruitService;

@RequiredArgsConstructor
@Controller
public class RecruitController {

  private final RecruitService recruitService;
  private final CompanyService companyService;
  private final ApplyService applyService;

  // main
  @GetMapping("/")
  public String main(Model model) {
    model.addAttribute("list", recruitService.메인공고목록보기());
    return "page/main";
  }

  @GetMapping("recruit/update/{id}")
  public String updateFrom(@PathVariable(required = false) Integer id, Model model) {
    Recruit recruitPS = recruitService.공고상세페이지(id);
    model.addAttribute("Recruit", recruitPS);
    return "page/recruit/updateForm";
  }

  @PutMapping("recruit/update")
  public @ResponseBody ResponseDto<?> update(@RequestBody SaveDto saveDto) {

    recruitService.구인공고업데이트(saveDto);

    return new ResponseDto<>(1, "성공", null);
  }

  @GetMapping("/recruit/detail/{id}/{userId}")
  public String recruit_Detail(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId, Model model) {
    Recruit recruitPS = recruitService.공고상세페이지(id);
    model.addAttribute("Recruit", recruitPS);
    model.addAttribute("company", companyService.getCompanyDetail(recruitPS.getRecruitCompanyId()));
    model.addAttribute("apply", applyService.이력서목록보기(userId));
    return "page/recruit/detail";
  }

  @GetMapping("/recruit/saveForm/{companyId}")
  public String writeFrom(@PathVariable Integer companyId, Model model) {
    model.addAttribute("company", companyService.getCompanyDetail(companyId));
    return "page/recruit/saveForm";
  }

  @PostMapping("/recruit/save")
  public @ResponseBody ResponseDto<?> write(@RequestBody SaveDto saveDto) {

    recruitService.구인공고작성(saveDto);

    return new ResponseDto<>(1, "성공", null);
  }

  @GetMapping("/recruit/list")
  public String viewList(Model model, @Param("page") Integer page) {
    model.addAttribute("recruits", recruitService.채용공고목록보기(page));
    return "page/recruit/recruitList";
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
