package com.robo.conf;

import com.robo.Entities.User;
import com.robo.Entities.UserSettings;
import com.robo.repository.UserDetailsRepo;
import com.robo.repository.UserSettingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.time.LocalDateTime;

//@EnableOAuth2Client
@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/",
                        "/login**",
                        "/js/**",
                        "/css/**",
                        "/error**").permitAll()
                .anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and()
                .csrf().disable();
    }

    @Autowired
    UserSettingsRepo userSettingsRepo;

//
//    @Autowired
//    OAuth2ClientContext oauth2ClientContext;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .antMatcher("/**").authorizeRequests()
//                .antMatchers("/", "/login**", "/js**", "/webjars/**", "/error**").permitAll().anyRequest()
//                .authenticated().and().exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")).and().logout()
//                .logoutSuccessUrl("/").permitAll()
//                .and()
//                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
////                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .csrf().disable();
//
//    }
//
//    @Bean
//    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
//        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(filter);
//        registration.setOrder(-100);
//        return registration;
//    }
//
//    private Filter ssoFilter() {
//        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter(
//                "/login");
//        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oauth2ClientContext);
//        googleFilter.setRestTemplate(googleTemplate);
//        UserInfoTokenServices tokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(),
//                google().getClientId());
//        tokenServices.setRestTemplate(googleTemplate);
//        googleFilter.setTokenServices(
//                new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId()));
//        return googleFilter;
//    }
//
//    @Bean
//    @ConfigurationProperties("google.client")
//    public AuthorizationCodeResourceDetails google() {
//        return new AuthorizationCodeResourceDetails();
//    }
//
//    @Bean
//    @ConfigurationProperties("google.resource")
//    public ResourceServerProperties googleResource() {
//        return new ResourceServerProperties();
//    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/static/**", "/webjars/**");
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserDetailsRepo userDetailsRepo) {

        return map -> {
            String id = (String) map.get("sub");

            User user = userDetailsRepo.findById(id).orElseGet(() -> new User(
                    id,
                    (String) map.get("name"),
                    (String) map.get("picture"),
                    (String) map.get("email"))
            );

            user.setLastVisit(LocalDateTime.now());

            userSettingsRepo.save(userSettingsRepo
                    .findByUserId(id)
                    .orElseGet(() -> new UserSettings(id)));

            return userDetailsRepo.save(user);
        };
    }

}

