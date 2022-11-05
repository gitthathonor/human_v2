package site.metacoding.humancloud;

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
        session.setAttribute("sessionUser", SessionUser.builder().id(1).username("ssar").role(0).build());
    }

    @Test
    public void db_test() {
        UserFindById user = userDao.findById(1).orElseThrow(
                () -> new RuntimeException("아이디 없어"));
        System.out.println("username : " + user.getUsername());
    }

}
