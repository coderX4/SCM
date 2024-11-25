package com.scm.config;

import com.scm.services.impl.SecurityCustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Autowired
    private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;
    //user create and login using java code with in memory service
    //this method is only for testing
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("admin123")
//                .password("admin123")
//                .roles("ADMIN","USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    @Autowired
    private SecurityCustomUserDetailService userDetailsService;

    @Autowired
    private AuthenticationFailure authenticationFailure;

    @Autowired
    private LogoutSuccess logoutSuccess;

    //url configured here
    //whether which url should be protected and which should not
    //Configuration of Authentication Provider
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //user detail service object
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        //password encoder object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //configuration
        httpSecurity.authorizeHttpRequests(authorize->{
            //authorize.requestMatchers("/home","/signup").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });
        //form default login
        //if any changes we have to do it here

        //this is default loginform provided by spring security
        //httpSecurity.formLogin(Customizer.withDefaults());

        httpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/do-authenticate");
            formLogin.successForwardUrl("/after-authenticate");
            formLogin.failureUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

            formLogin.failureHandler(authenticationFailure);
//            formLogin.successHandler(new AuthenticationSuccessHandler() {
//                @Override
//                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//
//                }
//            });
//            formLogin.failureHandler(new AuthenticationFailureHandler() {
//
//                @Override
//                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//
//                }
//            });
        });

        httpSecurity.logout(logout->{
            logout.logoutUrl("/user/do-logout");
            logout.logoutSuccessUrl("/login?logout=true");
            logout.logoutSuccessHandler(logoutSuccess);
        });

        //oauth2 configurations
        httpSecurity.oauth2Login(oauth2Login->{
            oauth2Login.loginPage("/login");
            oauth2Login.successHandler(oAuthAuthenticationSuccessHandler);
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
