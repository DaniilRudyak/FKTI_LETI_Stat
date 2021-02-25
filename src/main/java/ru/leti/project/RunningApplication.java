package ru.leti.project;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication()
public class RunningApplication  {
    public static void main(String[] args) {
        SpringApplication.run(RunningApplication.class, args);
    }
    @Bean
    HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
//    @Override
//    public void customize(ConfigurableServletWebServerFactory factory) {
//        factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/error_access.html"));
//
//    }

}
