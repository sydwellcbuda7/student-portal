package za.co.student_management.student_portal.config.webApp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import za.co.student_management.student_portal.config.interceptor.PermissionsInterceptor;

@Configuration
@Slf4j
public class WebAppConfig implements WebMvcConfigurer {

    private final PermissionsInterceptor permissionsInterceptor;

    @Autowired
    public WebAppConfig(PermissionsInterceptor permissionsInterceptor) {
        this.permissionsInterceptor = permissionsInterceptor;
    }

    @Override
    public void addInterceptors (InterceptorRegistry interceptorRegistry) {
        log.info("this method will get invoked by container while deployment");
        interceptorRegistry.addInterceptor(permissionsInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/swagger**", "/webjars/springfox-swagger-ui/**",
                        "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**",
                        "/favicon.ico", "/index.html");
    }
}
