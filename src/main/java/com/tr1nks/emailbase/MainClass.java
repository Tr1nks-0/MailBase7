package com.tr1nks.emailbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({
        "com.tr1nks.emailbase.config",
        "com.tr1nks.emailbase.controller",
        "com.tr1nks.emailbase.model.services",
        "com.tr1nks.emailbase.model.utils",
        "com.tr1nks.emailbase.model.engines",
        "com.tr1nks.emailbase.model.formatters",
})

/*@ComponentScan({
        "com.tr1nks.config",
        "com.tr1nks.controller",
        "com.tr1nks.model.services",
        "com.tr1nks.model.utils",
        "com.tr1nks.model.engines",
        "com.tr1nks.model.formatters",
})*/


public class MainClass extends SpringBootServletInitializer {
    public static final Class[] classes = {MainClass.class,};
    public static ApplicationContext ac;

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "true");
        System.setProperty("spring.thymeleaf.cache", "false");
        ac = SpringApplication.run(classes, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(classes);
    }
}
/*





}*/