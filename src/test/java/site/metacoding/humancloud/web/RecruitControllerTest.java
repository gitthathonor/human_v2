package site.metacoding.humancloud.web;

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
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class RecruitControllerTest {

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
        MockMultipartFile companyJoinReqDto = new MockMultipartFile("companyJoinReqDto",
                "companyJoinReqDtoData",
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
    public void 채용공고목록보기테스트() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get("/recruit/list")
                        .accept(APPLICATION_JSON));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));

    }
}