package site.metacoding.humancloud;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
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
import site.metacoding.humancloud.domain.user.UserDao;
import site.metacoding.humancloud.dto.SessionUser;
import site.metacoding.humancloud.dto.company.CompanyReqDto.CompanyJoinReqDto;
import site.metacoding.humancloud.dto.user.UserRespDto.UserFindById;

@Sql({ "classpath:ddl.sql", "classpath:dml.sql" })
@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class SettingTest {

    // header json
    private static final String APPLICATION_JSON = "application/json; charset=utf-8";

    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mvc;
    private MockHttpSession session;
    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void sessionInit() {
        session = new MockHttpSession();
        session.setAttribute("sessionUser",
                SessionUser.builder().id(1).username("adt").role(1).build());
    }

    @Test
    public void 기업회원가입테스트() throws Exception {
        // given

        // 요청 dto 객체 멀티파트로 변환
        CompanyJoinReqDto companyJoinReqDtoData = CompanyJoinReqDto.builder()
                .companyId(4)
                .companyUsername("test")
                .companyPassword("123")
                .companyName("comtest")
                .companyEmail("test@natev.com")
                .companyPhoneNumber("01099988873")
                .companyAddress("s")
                .build();
        String joinData = om.writeValueAsString(companyJoinReqDtoData);
        MockMultipartFile companyJoinReqDto = new MockMultipartFile("companyJoinReqDto", "companyJoinReqDtoData",
                "application/json", joinData.getBytes(StandardCharsets.UTF_8));

        // 요청 파일 멀티파트로 변환
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "form-data",
                "test file".getBytes(StandardCharsets.UTF_8));

        // when
        ResultActions resultActions = mvc.perform(
                multipart("/company/join")
                        .file(file)
                        .file(companyJoinReqDto));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    @Test
    public void db_test() {
        UserFindById user = userDao.findById(1).orElseThrow(
                () -> new RuntimeException("아이디 없어"));
        System.out.println("username : " + user.getUsername());
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
                        .session(session));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));

    }

    @Test
    public void create_test() throws Exception {
        // given
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Java");
        categoryList.add("JavaScript");

        String fileName = "testCustomerUpload.jpg";

        // ResumeSaveReqDto resumeSaveReqDto = ResumeSaveReqDto.builder()
        // .resumeId(1)
        // .resumeUserId(1)
        // .resumeTitle("test")
        // .resumeEducation("high")
        // .resumeCareer("1년")
        // .resumePhoto(fileName)
        // .resumeLink("link.com")
        // .categoryList(Arrays.asList("자바"))
        // .build();

        String body = om.writeValueAsString(null);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.post(
                        "/s/resume/save")
                        .content(body).contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON).session(session));

        // then
        MvcResult mvcResult = resultActions.andReturn();

        log.debug("디버그 : " + mvcResult);

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    @Test
    public void viewCategory_test() throws Exception {
        // given
        Category category = new Category(1, "Java");
        Integer page = 0;
        String testCategory = "Java";

        String body = om.writeValueAsString(category);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.post(
                        "/resume")
                        .content(body).contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON).session(session));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }
}
