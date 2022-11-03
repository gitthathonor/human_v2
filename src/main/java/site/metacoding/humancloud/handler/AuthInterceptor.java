package site.metacoding.humancloud.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import site.metacoding.humancloud.util.annotation.Auth;

@Component
public class AuthInterceptor implements HandlerInterceptor {

  // 핸들러(컨트룰러 메서드)가 실행되기 전 : 권한을 확인하기 위해

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HandlerMethod method = (HandlerMethod) handler; // 요청을 처리할 메서드
    Auth auth = method.getMethodAnnotation(Auth.class); // 어노테이셔 ㄴ값 받아오기

    if (auth == null) {
      return true;
    } // 어노테이션이 없는 경우, 권한 필요 없는 메서드임 => 통과

    // 1. 세션 체크
    HttpSession session = request.getSession();
    // 근데 필터로 거르니 체크 안 해도 됨

    // 2. 권한 체크
    int role = auth.role(); // 0 일반 1 기업 2 관리자
    System.out.println("---------------------------------");
    System.out.println(auth.role());
    System.out.println(session.getAttribute("role"));

    int sessionUserRole = Integer.parseInt(session.getAttribute("role").toString());

    if (role == sessionUserRole) {
      return true;
    }

    throw new RuntimeException("권한이 없습니다.");
  }

}
