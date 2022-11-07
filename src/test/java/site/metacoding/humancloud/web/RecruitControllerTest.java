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
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitSaveReqDto;
import site.metacoding.humancloud.dto.recruit.RecruitReqDto.RecruitUpdateReqDto;
import site.metacoding.humancloud.dto.recruit.RecruitRespDto.CompanyRecruitDtoRespDto;
import site.metacoding.humancloud.dto.user.UserRespDto.UserFindById;

@Sql({ "classpath:ddl.sql", "classpath:dml.sql" })
@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class RecruitControllerTest {

        // header json
        private static final String APPLICATION_JSON = "application/json; charset=utf-8";
        // private static final SessionUser sessionUser =
        // SessionUser.builder().id(1).username("ssar").role(0).build();
        // private static final SessionUser sessionCom =
        // SessionUser.builder().id(1).username("adt").role(1).build();

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
        public void recruitmain_test() throws Exception {
                // given , 한 페이지에서 볼 목록 개수 10 = 0 개 / 0 = 10 개

                // when
                ResultActions resultActions = mvc.perform(
                                MockMvcRequestBuilders.get("/")
                                                .accept(APPLICATION_JSON));
                // then
                resultActions.andExpect(MockMvcResultMatchers.status().isOk());
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));

        }

        @Test
        public void recruitdetail_test() throws Exception {
                // given
                Integer recruitId = 1;
                Integer userId = 1;

                // when
                ResultActions resultActions = mvc.perform(
                                MockMvcRequestBuilders.get("/recruit/detail/" + recruitId + "/" + userId)
                                                .accept(APPLICATION_JSON));

                // then
                resultActions.andExpect(MockMvcResultMatchers.status().isOk());
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
        }

        @Test
        public void recruitsave_test() throws Exception {
                // given
                RecruitSaveReqDto recruitSaveReqDto = RecruitSaveReqDto.builder()
                                .recruitTitle("제제제제제제목")
                                .recruitContent("내내내낸내용")
                                .recruitCareer("1년")
                                .recruitCompanyId(1)
                                .recruitLocation("서울")
                                .recruitSalary(5000)
                                .recruitCategoryList(null).build();
                String body = om.writeValueAsString(recruitSaveReqDto);

                // then
                ResultActions resultActions = mvc.perform(
                                MockMvcRequestBuilders.post("/s/recruit/save").content(body)
                                                .contentType(APPLICATION_JSON)
                                                .accept(APPLICATION_JSON).session(session));

                // then
                resultActions.andExpect(MockMvcResultMatchers.status().isOk());
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
        }

        @Test
        public void recruitupdate_test() throws Exception {
                // given
                Integer recruitId = 1;

                RecruitUpdateReqDto recruitUpdateReqDto = RecruitUpdateReqDto.builder()
                                .recruitTitle("제제제제제제목")
                                .recruitContent("내내내낸내용")
                                .recruitCareer("1년")
                                .recruitId(recruitId)
                                .recruitCompanyId(1)
                                .recruitLocation("서울")
                                .recruitSalary(5000)
                                .recruitCategoryList(null).build();

                String body = om.writeValueAsString(recruitUpdateReqDto);

                // when
                ResultActions resultActions = mvc.perform(
                                MockMvcRequestBuilders.put("/s/recruit/update/" + recruitId).content(body)
                                                .contentType(APPLICATION_JSON)
                                                .accept(APPLICATION_JSON).session(session));

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