package site.metacoding.humancloud.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.humancloud.domain.category.Category;
import site.metacoding.humancloud.dto.SessionUser;
import site.metacoding.humancloud.dto.dummy.response.page.PagingDto;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeSaveReqDto;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeUpdateReqDto;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeViewCategoryReqDto;
import site.metacoding.humancloud.dto.resume.ResumeReqDto.ResumeViewOrderListReqDto;

@Sql({ "classpath:ddl.sql", "classpath:dml.sql" })
@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class ResumeControllerTest {

        // header json
        private static final String APPLICATION_JSON = "application/json; charset=utf-8";
        private static final SessionUser sessionUser = SessionUser.builder().id(1).username("ssar").role(0).build();
        private static final SessionUser sessionCom = SessionUser.builder().id(1).username("adt").role(1).build();
        @Autowired
        private ObjectMapper om;
        @Autowired
        private MockMvc mvc;

        @Autowired
        private MockHttpSession session;

        @Test
        public void create_test() throws Exception {
                // given
                List<String> categoryList = new ArrayList<>();
                categoryList.add("Java");
                categoryList.add("JavaScript");

                String uploadFile = "testImage.jpg";

                int pos = uploadFile.lastIndexOf(".");
                String extension = uploadFile.substring(pos + 1);
                String filePath = "C:\\temp\\img\\";
                String imgSaveName = UUID.randomUUID().toString();
                String imgName = imgSaveName + "." + extension;

                ResumeSaveReqDto resumeSaveReqDto = new ResumeSaveReqDto(
                                1,
                                "이력서 테스트중",
                                "1년이상 ~ 3년미만",
                                "신입",
                                imgName,
                                "http:localhost:8000",
                                categoryList);

                String body = om.writeValueAsString(resumeSaveReqDto);
                MockMultipartFile resumeSaveReqDtoFile = new MockMultipartFile("ResumeSaveReqDto", "ResumeSaveReqDto",
                                "application/json", body.getBytes(StandardCharsets.UTF_8));

                MockMultipartFile file = new MockMultipartFile("file", uploadFile, "form-data",
                                filePath.getBytes(StandardCharsets.UTF_8));

                session.setAttribute("sessionUser", sessionUser);

                // when
                ResultActions resultActions = mvc.perform(
                                multipart("/s/resume/save")
                                                .file(file)
                                                .file(resumeSaveReqDtoFile)
                                                .session(session));

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
                session.setAttribute("sessionUser", sessionUser);
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
        public void updateResume_test() throws Exception {
                // given
                List<String> categoryList = new ArrayList<>();
                categoryList.add("Java");
                categoryList.add("JavaScript");
                Integer resumeId = 1;
                String uploadFile = "testImage.jpg";

                int pos = uploadFile.lastIndexOf(".");
                String extension = uploadFile.substring(pos + 1);
                String filePath = "C:\\temp\\img\\";
                String imgSaveName = UUID.randomUUID().toString();
                String imgName = imgSaveName + "." + extension;

                ResumeUpdateReqDto resumeUpdateReqDto = new ResumeUpdateReqDto(1,
                                1,
                                "이력서 테스트중",
                                "1년이상 ~ 3년미만",
                                "신입",
                                imgName,
                                "http:localhost:8000",
                                categoryList);

                String body = om.writeValueAsString(resumeUpdateReqDto);

                MockMultipartFile resumeUpdateReqDtoFile = new MockMultipartFile("resumeUpdateReqDto",
                                "resumeUpdateReqDto",
                                "application/json", body.getBytes(StandardCharsets.UTF_8));

                MockMultipartFile file = new MockMultipartFile("file", uploadFile, "form-data",
                                filePath.getBytes(StandardCharsets.UTF_8));

                session.setAttribute("sessionUser", sessionUser);

                // when

                ResultActions resultActions = mvc.perform(
                                multipart("/s/resume/update/" + resumeId)
                                                .file(file)
                                                .file(resumeUpdateReqDtoFile)
                                                .with(req -> {
                                                        req.setMethod("PUT");
                                                        return req;
                                                })
                                                .session(session));

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
                session.setAttribute("sessionUser", sessionUser);
                // when

                ResultActions resultActions = mvc.perform(
                                MockMvcRequestBuilders.delete("/s/resume/deleteById/" + resumeId)
                                                .session(session));

                // then
                resultActions.andExpect(MockMvcResultMatchers.status().isOk());
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
                // then
        }

        @Test
        public void viewList_test() throws Exception {
                // given
                session.setAttribute("sessionUser", sessionCom);
                // when

                ResultActions resultActions = mvc.perform(
                                MockMvcRequestBuilders.get("/s/resume").param("page", "1")
                                                .accept(APPLICATION_JSON)
                                                .session(session));

                resultActions.andExpect(MockMvcResultMatchers.status().isOk());
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));

        }

        @Test
        public void viewCategory_test() throws Exception {
                // given
                ResumeViewCategoryReqDto resumeViewCategoryReqDto = new ResumeViewCategoryReqDto("Java", 0);

                String body = om.writeValueAsString(resumeViewCategoryReqDto);

                session.setAttribute("sessionUser", sessionCom);
                // when
                ResultActions resultActions = mvc.perform(
                                MockMvcRequestBuilders.post(
                                                "/s/resume")
                                                .content(body).contentType(APPLICATION_JSON)
                                                .accept(APPLICATION_JSON).session(session));

                // then
                resultActions.andExpect(MockMvcResultMatchers.status().isOk());

                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
        }

        @Test
        public void orderList_test() throws Exception {
                // given
                ResumeViewOrderListReqDto resumeViewOrderListReqDto = new ResumeViewOrderListReqDto("recent", 1, 0);
                String body = om.writeValueAsString(resumeViewOrderListReqDto);
                session.setAttribute("sessionUser", sessionCom);
                // when
                ResultActions resultActions = mvc.perform(
                                MockMvcRequestBuilders.post(
                                                "/s/resume/list")
                                                .content(body).contentType(APPLICATION_JSON)
                                                .accept(APPLICATION_JSON).session(session));

                // then
                resultActions.andExpect(MockMvcResultMatchers.status().isOk());
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
        }
}
