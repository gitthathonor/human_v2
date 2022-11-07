package site.metacoding.humancloud.handler.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.humancloud.dto.SessionUser;

@Slf4j
@RequiredArgsConstructor
public class CompanyAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("디버그 : 기업 아서 11");
        // url요청의 {id}
        String uri = request.getRequestURI();
        String[] uriArray = uri.split("/");
        int companyId = Integer.parseInt(uriArray[uriArray.length - 1]);
        log.debug("디버그 : " + companyId);
        log.debug("디버그 : 기업 아서 222");
        // 세션의 id
        HttpSession session = request.getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        int sessionUserId = sessionUser.getId();

        // 업데이트 딜리트
        String httpMethod = request.getMethod();
        if (httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
            if (companyId == sessionUserId) {
                return true;
            }
            throw new RuntimeException("권한이 없습니다.");
        }

        return true;
    }
}