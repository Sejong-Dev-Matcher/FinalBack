package SDM.springmvc.basic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/user/login/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS") // OPTIONS 메서드를 추가
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}

