package com.springsecurity.demo.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author wanli zhou
 * @created 2017-10-23 11:14 PM.
 */
@Profile("n3_preAuth_selfAuth_logout")
@EnableWebSecurity
// jsr250Enabled @RolesAllowed({"ROLE_SITE_ADMIN","ROLE_CATALOG_ADMIN"})
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
//securedEnabled @Secured({"ROLE_SITE_ADMIN", "ROLE_SUPPLIER", "ROLE_CSR", "ROLE_SUPPLIER_ADMIN"})
//@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http
        .csrf().disable()
        .authorizeRequests()
            .antMatchers("/switch_user").hasRole("SUPER_ADMIN")
            .antMatchers("/switch_user_exit").hasRole("PREVIOUS_ADMINISTRATOR")
            .antMatchers("/p/manager-user", "/p/all-login-users").hasAnyRole("SUPER_ADMIN", "ADMIN")
            .antMatchers("/p/manager-user/").hasAnyRole("SUPER_ADMIN", "ADMIN")
            .antMatchers("/p/invalidSession", "/p/sessiontimeout").permitAll()
            .antMatchers("/css/**", "/fonts/**", "/js/**").permitAll()
            .antMatchers(HttpMethod.POST, "/ajax_login*").permitAll()
            .anyRequest().permitAll()
        .and()
                .rememberMe()
                //在这里设置没效果放在 selfAuthenFilter中设置
                .userDetailsService(userDetailsServiceBean())
                .key("remember-me")
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("remember-me")
                .tokenValiditySeconds(86400)
                .tokenRepository(persistentTokenRepository())
        .and()
                .formLogin().loginPage("/p/login").permitAll()
        .and()
                .logout().logoutUrl("/logout_request").deleteCookies("JSESSIONID").logoutSuccessHandler(selfLogoutSuccessHandler()).permitAll()
        .and()
            .addFilterAt(preAuthenticatedProcessingFilter(), AbstractPreAuthenticatedProcessingFilter.class)
            .addFilterAfter(selfAuthenFilter(), PreAuthenticatedProcessingFilter.class)
            .addFilterAt(switchUserFilter(), SwitchUserFilter.class)
            .addFilterAt(concurrentSessionFilter(), ConcurrentSessionFilter.class)
        .exceptionHandling().authenticationEntryPoint(authEntryPoint())
        .and()//http://www.baeldung.com/spring-security-session http://blog.csdn.net/xingxianbiao/article/details/50856672
            .sessionManagement().sessionAuthenticationStrategy(compositeSessionAuthenticationStrategy())
                .invalidSessionUrl("/p/invalidSession")
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().changeSessionId()//如果另外一个用户用上一个用户留下来的sessionID登录的话改变sessionID
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/p/sessiontimeout")
                .sessionRegistry(sessionRegistry());
        ;


    }

    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter(){
        return new ConcurrentSessionFilter(sessionRegistry(), "/p/sessiontimeout");
    }
    @Bean
    public CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy(){
        ConcurrentSessionControlAuthenticationStrategy con = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        con.setExceptionIfMaximumExceeded(false);
        return new CompositeSessionAuthenticationStrategy(Arrays.asList(
            con,
            new SessionFixationProtectionStrategy(),
            new RegisterSessionAuthenticationStrategy(sessionRegistry())
        ));

    }
    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    @Bean
    public AuthenticationEntryPoint authEntryPoint() {
        return new Http403ForbiddenEntryPoint();
    }

    @Bean
    public PersistentTokenBasedRememberMeServices tokenBasedRememberMeServices(){
        PersistentTokenBasedRememberMeServices t =  new PersistentTokenBasedRememberMeServices("remember-me", userDetailsServiceBean(), persistentTokenRepository());
        t.setAlwaysRemember(true);
        return t;
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }
    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() {
        return new SelfSwitchUserDetailsService();
    }

    @Bean
    public SwitchUserFilter switchUserFilter() {
        SwitchUserFilter filter = new SwitchUserFilter();
        filter.setUserDetailsService(userDetailsServiceBean());
        filter.setUsernameParameter("username");
        filter.setSwitchUserUrl("/switch_user");
        filter.setExitUserUrl("/switch_user_exit");
        filter.setTargetUrl("/");
        //filter.setSuccessHandler(authenticationSuccessHandler);
        //filter.setFailureHandler(authenticationFailureHandler());

        return filter;
    }

    @Bean
    public PreAuthUserAuthenticationManager preAuthUserAuthenticationManager(){
        return new PreAuthUserAuthenticationManager();

    }

    @Bean
    public PreAuthenticatedProcessingFilter preAuthenticatedProcessingFilter(){
        return  new PreAuthenticatedProcessingFilter(preAuthUserAuthenticationManager());
    }

    @Bean
    public SelfLogoutSuccessHandler selfLogoutSuccessHandler(){
        return new SelfLogoutSuccessHandler();
    }
    @Bean
    public SelfAuthenticationSuccessHandler selfAuthenticationSuccessHandler(){
        return new SelfAuthenticationSuccessHandler();
    }

    @Bean
    public SelfAuthenticationFailureHandler selfAuthenticationFailureHandler(){
        return new SelfAuthenticationFailureHandler();
    }
    @Bean
    public SelfAuthenticationProvider selfAuthenticationProvider(){
        return new SelfAuthenticationProvider();
    }
    @Bean
    public SelfAuthenticationProcessingFilter  selfAuthenFilter(){
        SelfAuthenticationProcessingFilter selfAuthenFilter = new SelfAuthenticationProcessingFilter(selfAuthenticationProvider());
        selfAuthenFilter.setAuthenticationSuccessHandler(selfAuthenticationSuccessHandler());
        selfAuthenFilter.setAuthenticationFailureHandler(selfAuthenticationFailureHandler());
        selfAuthenFilter.setRememberMeServices(tokenBasedRememberMeServices());
        selfAuthenFilter.setSessionAuthenticationStrategy(compositeSessionAuthenticationStrategy());
        return selfAuthenFilter;
    }


}
