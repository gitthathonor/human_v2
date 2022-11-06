package site.metacoding.humancloud.web;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.domain.company.CompanyDao;
import site.metacoding.humancloud.domain.resume.ResumeDao;
import site.metacoding.humancloud.domain.user.UserDao;
import site.metacoding.humancloud.dto.SessionUser;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeSaveReqDto;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeUpdateReqDto;

@Sql({ "classpath:ddl.sql", "classpath:dml.sql" })
@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class ResumeControllerTest {

    // header json
    private static final String APPLICATION_JSON = "application/json; charset=utf-8";

    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private MockHttpSession userSession;

    @Autowired
    private MockHttpSession companySession;

    @Autowired
    private ResumeDao resumeDao;
    @Autowired
    private ResumeController resumeController;

    @BeforeEach
    public void userSessionInit() {
        userSession.setAttribute("sessionUser",
                SessionUser.builder().id(1).username("ssar").role(1).build());
    }

    @BeforeEach
    public void companySessionInit() {
        companySession.setAttribute("sessionUser",
                SessionUser.builder().id(1).username("adt").role(1).build());
    }

    @Test
    public void 인터셉터테스트() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/test"));
    }

    @Test
    public void create_test() throws Exception {
        // given
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Java");
        categoryList.add("JavaScript");

        String fileName = "testCustomerUpload.jpg";

        ResumeSaveReqDto resumeSaveReqDto = new ResumeSaveReqDto(
                1,
                "이력서 테스트중",
                "1년이상 ~ 3년미만",
                "신입",
                fileName,
                "http:localhost:8000",
                categoryList);

        String body = om.writeValueAsString(resumeSaveReqDto);
        // when

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.post(
                        "/s/resume/save")
                        .content(body).contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON).session(userSession));

        // then
        MvcResult mvcResult = resultActions.andReturn();

        log.debug("디버그 : " + mvcResult);

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    @Test
    public void detailResume_test() throws Exception {
        // given
        Integer resumeId = 1;
        Integer userId = 1;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get("/s/resume/detail/" + resumeId + "/" + userId)
                        .accept(APPLICATION_JSON)
                        .session(userSession));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));

    }

    @Test
    public void updateResume_test() throws Exception {
        // given
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Java");
        categoryList.add("JavaScript");

        String fileName = "testCustomerUpload.jpg";

        ResumeUpdateReqDto resumeUpdateReqDto = new ResumeUpdateReqDto(
                1,
                "이력서 테스트중",
                "1년이상 ~ 3년미만",
                "신입",
                fileName,
                "http:localhost:8000",
                categoryList);

        String body = om.writeValueAsString(resumeUpdateReqDto);
        // when

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.put(
                        "/s/resume/update/" + resumeUpdateReqDto.getResumeId())
                        .content(body).contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON).session(userSession));

        // then
        MvcResult mvcResult = resultActions.andReturn();

        log.debug("디버그 : " + mvcResult);

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    @Test
    public void deleteResume_test() throws Exception {
        // given
        Integer resumeId = 1;
        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.delete("/s/resume/deleteById/" + resumeId)
                        .session(userSession));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
        // then
    }

    @Test
    public void viewList_test() throws Exception {
        // given
        Integer page = 1;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get("/s/resume?page=" + page)
                        .accept(APPLICATION_JSON)
                        .session(companySession));

        // then

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));

    }

    @Test
    public void viewCategory_test() throws Exception {
        // given
        Category category = new Category(1, "Java");

        Integer page = 0;

        String body = om.writeValueAsString(category);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.post(
                        "/s/resume?page=" + page + "&category=" + category.getCategoryName())
                        .content(body).contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON).session(companySession));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());

        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    @Test
    public void orderList_test() throws Exception {
        // given
        String order = "education";
        Integer page = 0;

        Integer companyId = 1;
        String body = om.writeValueAsString(companyId);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.post(
                        "/s/resume/list?page=" + page + "&order=" + order)
                        .content(body).contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON).session(companySession));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }
}
