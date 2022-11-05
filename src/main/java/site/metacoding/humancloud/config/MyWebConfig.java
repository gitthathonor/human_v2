package site.metacoding.humancloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import site.metacoding.humancloud.domain.resume.ResumeDao;
import site.metacoding.humancloud.handler.TestInterceptor;
import site.metacoding.humancloud.handler.interceptor.UserAuthInterceptor;
import site.metacoding.humancloud.handler.interceptor.ResumeInterceptor;
import site.metacoding.humancloud.handler.interceptor.RoleInterceptor;

@RequiredArgsConstructor
@Configuration
public class MyWebConfig implements WebMvcConfigurer {

    private final ResumeDao resumeDao;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///C:/temp/img/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleInterceptor());
        registry.addInterceptor(new UserAuthInterceptor())
                .addPathPatterns("/s/user/**");
        registry.addInterceptor(new ResumeInterceptor(resumeDao))
                .addPathPatterns("/s/resume/**");
    }

}
