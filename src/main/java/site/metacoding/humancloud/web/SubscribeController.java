package site.metacoding.humancloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import site.metacoding.humancloud.domain.subscribe.Subscribe;
import site.metacoding.humancloud.dto.ResponseDto;
import site.metacoding.humancloud.dto.subscribe.SubscribeReqDto.SubscribeSaveReqDto;
import site.metacoding.humancloud.service.SubscribeService;

@RequiredArgsConstructor
@Controller
public class SubscribeController {

    private final SubscribeService subscribeService;

    @DeleteMapping("/s/subscribe/{userId}/{companyId}")
    public @ResponseBody ResponseDto<?> deleteSubscribe(@PathVariable("userId") Integer userId,
            @PathVariable("companyId") Integer companyId) {
        subscribeService.구독취소(userId, companyId);
        return new ResponseDto<>(1, "OK", null);
    }

    @PostMapping("/s/subscribe")
    public @ResponseBody ResponseDto<?> subscribeCompany(@RequestBody SubscribeSaveReqDto subscribeSaveReqDto) {
        return new ResponseDto<>(1, "ok", subscribeService.구독하기(subscribeSaveReqDto));
    }
}
