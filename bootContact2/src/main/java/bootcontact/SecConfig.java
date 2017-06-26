package bootcontact;

import static bootcontact.AppConst.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import bootcontact.service.UserService;

@Configuration
public class SecConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(URL_ROOT, URL_HOME).permitAll()
            .antMatchers("/css/**", "js/**", "/os/**").permitAll()
            .antMatchers(URL_ADMIN + "/**").hasRole("ADMIN")
            .antMatchers(URL_USER_LIST + "/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and().formLogin()
            .loginPage(URL_LOGIN).defaultSuccessUrl(URL_HOME).permitAll()
            .and().logout().permitAll();
        //http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}