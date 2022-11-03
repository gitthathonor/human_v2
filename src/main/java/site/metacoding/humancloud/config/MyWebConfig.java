package site.metacoding.humancloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import site.metacoding.humancloud.handler.AuthInterceptor;
import site.metacoding.humancloud.handler.TestInterceptor;

@RequiredArgsConstructor
@Configuration
public class MyWebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///C:/temp/img/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
        // .addPathPatterns("/s/**"); // 이 오류 모름
        // registry.addInterceptor(new TestInterceptor());
    }

}
