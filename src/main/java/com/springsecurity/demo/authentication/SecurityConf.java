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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

import javax.sql.DataSource;

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
        .authorizeRequests()
            .antMatchers("/switch_user").hasRole("SUPER_ADMIN")
            .antMatchers("/switch_user_exit").hasRole("PREVIOUS_ADMINISTRATOR")
            .antMatchers("/p/manager-user*").hasAnyRole("SUPER_ADMIN", "ADMIN")
            .antMatchers("/p/manager-user/").hasAnyRole("SUPER_ADMIN", "ADMIN")
            .antMatchers("/css/**", "/fonts/**", "/js/**").permitAll()
            .antMatchers(HttpMethod.POST, "/ajax_login*").permitAll()
            .anyRequest().permitAll()
            .and().csrf().disable()
        .rememberMe().key("remember-me")
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("remember-me")
                .tokenValiditySeconds(86400).tokenRepository(persistentTokenRepository())
        .and().formLogin().loginPage("/p/login").permitAll()
        .and().logout().logoutUrl("/logout_request").logoutSuccessHandler(selfLogoutSuccessHandler()).permitAll()
        .and()
            .addFilterAt(preAuthenticatedProcessingFilter(), AbstractPreAuthenticatedProcessingFilter.class)
            .addFilterAfter(selfAuthenFilter(), PreAuthenticatedProcessingFilter.class)
            .addFilterAt(switchUserFilter(), SwitchUserFilter.class)
            .addFilterAt(remberMeFilter(), RememberMeAuthenticationFilter.class)
//        .exceptionHandling().authenticationEntryPoint(authEntryPoint())
//        .and()
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        ;


    }


    @Bean
    public AuthenticationEntryPoint authEntryPoint() {
        return new Http403ForbiddenEntryPoint();
    }

    @Bean
    public RememberMeAuthenticationFilter remberMeFilter(){
       return  new RememberMeAuthenticationFilter(selfRembermeAuthenticationManager(), tokenBasedRememberMeServices());

    }
    @Bean
    public SelfRembermeAuthenticationManager selfRembermeAuthenticationManager(){
        return new SelfRembermeAuthenticationManager();
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
        return new PreAuthenticatedProcessingFilter(preAuthUserAuthenticationManager());
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
        return selfAuthenFilter;
    }


}
