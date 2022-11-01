package site.metacoding.humancloud.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import site.metacoding.humancloud.domain.user.User;
import site.metacoding.humancloud.dto.ResponseDto;
import site.metacoding.humancloud.dto.dummy.request.user.JoinDto;
import site.metacoding.humancloud.dto.dummy.request.user.LoginDto;
import site.metacoding.humancloud.dto.user.UserReqDto.JoinReqDto;
import site.metacoding.humancloud.dto.user.UserReqDto.UserUpdateReqDto;
import site.metacoding.humancloud.dto.user.UserRespDto.UserMypageRespDto;
import site.metacoding.humancloud.service.UserService;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    @PostMapping("/join")
    public ResponseDto<?> joinUser(@RequestBody JoinReqDto joinReqDto) {
        return new ResponseDto<>(1, "ok", userService.회원가입(joinReqDto));
    }

    @PutMapping("/s/user/{id}")
    public ResponseDto<?> update(@PathVariable Integer id,
            @RequestBody UserUpdateReqDto userUpdateReqDto) {
        return new ResponseDto<>(1, "ok", userService.회원업데이트(id, userUpdateReqDto));
    }

    @DeleteMapping("/s/user/{id}")
    public ResponseDto<?> delete(@PathVariable Integer id) {
        userService.회원탈퇴(id);
        return new ResponseDto<>(1, "ok", null);
    }

    @GetMapping("/s/mypage")
    public ResponseDto<?> viewUserMypage(@RequestParam Integer id) {
        return new ResponseDto<>(1, "ok", userService.마이페이지보기(id));
    }

    // @GetMapping("/user/{id}")
    // public ResponseDto<?> updateMypage(@PathVariable Integer id) {
    // return new ResponseDto<>(1, "ok", userService.유저정보보기(id));
    // }

    // @GetMapping("/logout")
    // public String logout() {
    // session.invalidate();
    // return "redirect:/";
    // }

    // @GetMapping("/login")
    // public String loginForm() {
    // return "page/user/login";
    // }

    // @PostMapping("/user/login")
    // public @ResponseBody ResponseDto<?> login(@RequestBody LoginDto loginDto,
    // HttpServletRequest request) {
    // User result = userService.로그인(loginDto);
    // if (result != null) {
    // HttpSession session = request.getSession();
    // session.setAttribute("principal", result);
    // }
    // return new ResponseDto<>(1, "1", result);
    // }

    // @GetMapping("/user/usernameSameCheck")
    // public @ResponseBody ResponseDto<?>
    // usernameSameCheck(@RequestParam("username") String username) {
    // Boolean result = userService.유저네임중복체크(username);
    // if (result == true) {
    // return new ResponseDto<>(1, "ok", true);
    // }
    // return new ResponseDto<>(1, "same id", false);
    // }

    // @GetMapping("/join")
    // public String userSaveForm() {
    // return "page/user/userSaveForm";
    // }

}
