package site.metacoding.humancloud.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.humancloud.domain.user.UserDao;
import site.metacoding.humancloud.dto.SessionUser;
import site.metacoding.humancloud.dto.user.UserRespDto.UserFindById;

@Sql({ "classpath:ddl.sql", "classpath:dml.sql" })
@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class UserControllerTest {
    private static final String APPLICATION_JSON = "application/json; charset=utf-8";

    @Autowired
    private ObjectMapper om;
    @Autowired
    private static MockMvc mvc;
    private MockHttpSession session;
    @Autowired
    private UserDao userDao;

    @Test
    public void dataInsert_test() {
        UserFindById user = userDao.findById(1).orElseThrow(
                () -> new RuntimeException("아이디 없어"));
        System.out.println("username : " + user.getUsername());
    }

    @BeforeEach
    public void sessionInit() {
        session = new MockHttpSession();
        session.setAttribute("sessionUser", SessionUser.builder().id(1).username("ssar").role(0).build());
    }

    @Test
    public void 유저마이페이지보기() throws Exception {

        // given
        Long userId = 1L;

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/s/mypage/" + userId).accept(APPLICATION_JSON).session(session));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        log.debug("디버그 : " + mvcResult.getResponse().getContentAsString());
        // resultActions.andExpect(status().isOk());
        // resultActions.andExpect(jsonPath("$.data.title").value("스프링1강"));
    }

}
