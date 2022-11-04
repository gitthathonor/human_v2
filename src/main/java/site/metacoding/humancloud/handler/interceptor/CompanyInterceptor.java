package site.metacoding.humancloud.handler.interceptor;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.humancloud.domain.company.CompanyDao;
import site.metacoding.humancloud.domain.resume.ResumeDao;
import site.metacoding.humancloud.dto.SessionUser;
import site.metacoding.humancloud.dto.company.CompanyRespDto.CompanyFindById;
import site.metacoding.humancloud.dto.resume.ResumeRespDto.ResumeFindById;

@Slf4j
@RequiredArgsConstructor
public class CompanyInterceptor implements HandlerInterceptor {

    private final CompanyDao companyDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // url요청의 {id}
        String uri = request.getRequestURI();
        String[] uriArray = uri.split("/");
        int companyId = Integer.parseInt(uriArray[uriArray.length - 1]);

        Optional<CompanyFindById> companyOP = companyDao.findById(companyId);
        Integer companySessionId = companyOP.get().getCompanyId();

        // 세션의 id
        HttpSession session = request.getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        int sessionUserId = sessionUser.getId();

        // 업데이트 딜리트
        String httpMethod = request.getMethod();
        if (httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
            if (companySessionId == sessionUserId) {
                return true;
            }
            throw new RuntimeException("권한이 없습니다.");
        }

        return true;
    }
}