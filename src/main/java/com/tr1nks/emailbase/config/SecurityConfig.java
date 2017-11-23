package com.tr1nks.emailbase.config;

import com.tr1nks.emailbase.model.entities.entityenums.SiteRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
//@PropertySource("classpath:rootAdmin.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static String HAS_ROLE = "hasRole('";
    private static String HAS_ANY_ROLE = "hasAnyRole('";
    private static String COMMA = "', '";
    private static String TAIL = "')";
    @Resource
    private DataSource dataSource;
//    @Value("${admin.root.enabled}")
//    private boolean enabledRootAdmin;
//    @Value("${admin.root.email}")
//    private String emailRootAdmin;
//    @Value("${admin.root.password}")
//    private String passwordRootAdmin;

    @Override
    public void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests()
                .antMatchers("/main/**").access(hasAnyRole(SiteRoles.ADMIN, SiteRoles.USER))
                .antMatchers("/upload/**").access(hasAnyRole(SiteRoles.ADMIN, SiteRoles.USER))
                .antMatchers("/user/**").access(hasAnyRole(SiteRoles.ADMIN, SiteRoles.USER))
                .antMatchers("/students/**").access(hasAnyRole(SiteRoles.ADMIN, SiteRoles.USER))
                .antMatchers("/teachers/**").access(hasAnyRole(SiteRoles.ADMIN, SiteRoles.USER))
                .antMatchers("/emailToOutController/**").permitAll()
                .antMatchers("/EmailToOutController/**").permitAll()//backward capability
//                .antMatchers("/signup/**").permitAll()
                .antMatchers("/index/**").permitAll()
                .and().formLogin()
                .loginPage("/index").defaultSuccessUrl("/main", false).failureUrl("/index?error=true")
                .and().logout()
                .logoutUrl("/logout").logoutSuccessUrl("/index")
                .and().csrf().disable();
    }

    /**
     * create a 'hasRole('.....')' string
     * for more readable security config
     *
     * @param roles roles to be in 'hasRole('.....')'
     * @return string for config
     */
    private static String hasAnyRole(SiteRoles... roles) {
        StringBuilder builder = new StringBuilder(HAS_ANY_ROLE);
        Arrays.stream(roles).forEach(r -> builder.append(r.getRoleWithPrefix()).append(COMMA));
        return builder.replace(builder.lastIndexOf(COMMA), builder.lastIndexOf(COMMA) + 4, TAIL).toString();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
//        if (enabledRootAdmin) {
//            builder.inMemoryAuthentication()
//                    .withUser(emailRootAdmin)
//                    .password(passwordRootAdmin)
//                    .roles(SiteRoles.ADMIN.getRole(), SiteRoles.USER.getRole());
//        }
        builder.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select login, password,TRUE  from user where login=?")
                .authoritiesByUsernameQuery("SELECT login, concat('ROLE_',role) FROM  user WHERE login=?");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/emailToOutController");
//        web.ignoring().antMatchers("/theme", "theme");
    }
}
