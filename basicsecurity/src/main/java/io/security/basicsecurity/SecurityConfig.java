package io.security.basicsecurity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("{noop}1111").roles("USER");    //noop은 암호화를 하지 않음.
        auth.inMemoryAuthentication().withUser("sys").password("{noop}1111").roles("SYS","USER");
        auth.inMemoryAuthentication().withUser("admin").password("{noop}1111").roles("ADMIN","USER","SYS");


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/admin/pay").hasRole("ADMIN")
                .antMatchers("/admin/**").access("hasRole('ADMIN')||hasRole('SYS')")
                .anyRequest().authenticated();

        http
                .formLogin()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        RequestCache requestCache = new HttpSessionRequestCache();
                        SavedRequest saveRequest = requestCache.getRequest(request, response);
                        String redirectUrl = saveRequest.getRedirectUrl();
                        response.sendRedirect(redirectUrl);
                    }
                });

        http
                .exceptionHandling()
//                .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                    @Override
//                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//                        response.sendRedirect("/login");
//                    }
//                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        response.sendRedirect("/denied");
                    }
                });


        /*http
                .authorizeRequests()
                .anyRequest().authenticated();
        http
                .formLogin();
        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                ;*/



         /*http
                 .authorizeRequests()
                 .anyRequest().authenticated()
        .and()
                 .formLogin()
                 //.loginPage("/loginPage")
                 .defaultSuccessUrl("/")
                 .failureUrl("/login")
                 .usernameParameter("userId")
                 .passwordParameter("passwd")
                 .loginProcessingUrl("/login-proc")
                 .successHandler(new AuthenticationSuccessHandler() {
                     @Override
                     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                         System.out.println("authentication"+authentication.getName());
                         response.sendRedirect("/");
                     }
                 })
                 .failureHandler(new AuthenticationFailureHandler() {
                     @Override
                     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                         System.out.println("exception"+exception.getMessage());
                         response.sendRedirect("/login");
                     }
                 })
                 .permitAll()
                 .and().logout()
                 .logoutUrl("/logout")
                 .logoutSuccessUrl("/login")
                 .addLogoutHandler(new LogoutHandler() {
                     @Override
                     public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                         HttpSession session = request.getSession();
                         session.invalidate();
                     }
                 })
                 .logoutSuccessHandler(new LogoutSuccessHandler() {
                     @Override
                     public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                         response.sendRedirect("/login");
                     }
                 })
                 .and().rememberMe()
                 .rememberMeParameter("remember")
                 .tokenValiditySeconds(3600)
                 .userDetailsService(userDetailsService);*/

    }
}
