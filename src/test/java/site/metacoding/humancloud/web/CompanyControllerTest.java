package site.metacoding.humancloud.web;

import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.metacoding.humancloud.domain.company.Company;
import site.metacoding.humancloud.domain.company.CompanyDao;
import site.metacoding.humancloud.dto.SessionUser;
import site.metacoding.humancloud.dto.company.CompanyReqDto.CompanyUpdateReqDto;
import site.metacoding.humancloud.util.SHA256;

@Sql({ "classpath:ddl.sql", "classpath:dml.sql" })
@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class CompanyControllerTest {

    // header json
    private static final String APPLICATION_JSON = "application/json; charset=utf-8";

    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mvc;
    private MockHttpSession session;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private CompanyController companyController;
    @Autowired
    private SHA256 sha256;

    @BeforeEach
    public void sessionInit() {
        session = new MockHttpSession();
        session.setAttribute("sessionUser",
                SessionUser.builder().id(1).username("ssar").role(1).build());
    }

    @BeforeEach
    public void dataInit() {
        String encPassword = sha256.encrypt("1234");
        Company company = Company.builder()
                .companyUsername("ssar")
                .companyPassword(encPassword)
                .companyEmail("ssar@nate.com")
                .companyAddress("양정동")
                .companyLogo("동부 자동차 손해보험.png")
                .companyName("동부").build();
        companyDao.save(company);
    }

    // @Test
    // public void 인터셉터테스트() throws Exception {
    // mvc.perform(MockMvcRequestBuilders.get("/test"));
    // }

    @Test
    public void 기업회원수정테스트() throws Exception {
        // given
        Integer id = 1;
        MultipartFile file = new MockMultipartFile("file", "네이버.jpg", "/img/**", "<<jpg data>>".getBytes());

        int pos = file.getOriginalFilename().lastIndexOf(".");
        String extension = file.getOriginalFilename().substring(pos + 1);
        String filePath = "C:\\temp\\img\\";
        String logoSaveName = UUID.randomUUID().toString();
        String logo = logoSaveName + "." + extension;

        CompanyUpdateReqDto companyUpdateReqDto = new CompanyUpdateReqDto();
        companyUpdateReqDto.setCompanyLogo(logo);
        companyUpdateReqDto.setCompanyPassword("4567");
        companyUpdateReqDto.setCompanyEmail("ssar@gmail.com");
        companyUpdateReqDto.setCompanyName("엘지");
        companyUpdateReqDto.setCompanyAddress("남천동");
        companyUpdateReqDto.setCompanyPhoneNumber("01033338888");

        String body = om.writeValueAsString(companyUpdateReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.put("/s/company/" + id).content(body)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                        .session(session));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.companyName").value("엘지"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.companyEmail").value(" ssar@gmail.com"));
    }

}
