package org.route.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
/** GIVEN - do not edit. View name "carts/view" -> src/main/resources/templates/carts/view.html */
@Configuration
@EnableWebMvc
public class WebConfig {
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver r = new ClassLoaderTemplateResolver();
        r.setPrefix("templates/");
        r.setSuffix(".html");
        r.setTemplateMode(TemplateMode.HTML);
        r.setCharacterEncoding("UTF-8");
        r.setCacheable(false);
        return r;
    }
    @Bean
    public SpringTemplateEngine templateEngine(ClassLoaderTemplateResolver resolver) {
        SpringTemplateEngine e = new SpringTemplateEngine();
        e.setTemplateResolver(resolver);
        return e;
    }
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine engine) {
        ThymeleafViewResolver v = new ThymeleafViewResolver();
        v.setTemplateEngine(engine);
        v.setCharacterEncoding("UTF-8");
        return v;
    }

}
