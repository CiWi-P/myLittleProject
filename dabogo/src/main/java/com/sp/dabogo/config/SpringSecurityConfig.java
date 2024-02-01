package com.sp.dabogo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import com.sp.dabogo.security.AjaxSessionTimeoutFilter;
import com.sp.dabogo.security.LoginFailureHandler;
import com.sp.dabogo.security.LoginSuccessHandler;

/*
- 시큐리티 자동 설정을 제거할 경우
  @SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
  public class Ss2Application {
      // ..
  }

 - WebSecurityConfigurerAdapter
   5.7에서 Deprecated
 
 - Spring Security 5.7.10
 - 참고
    antMatchers()는 5.8에서 Deprecated 되고 requestMatchers()로 변경 됨
    
 - Spring Security 6.x
   antMatchers()는 에러가 발생하고 requestMatchers()로 변경 됨

 - JDBC 연동은 UserDetailsService 구현클래스 작성
*/

@Configuration
@EnableWebSecurity
// @ComponentScan(basePackages = { "com.sp.dabogo" })
public class SpringSecurityConfig {
	// @Autowired
	// private DataSource dataSource;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	// configure HTTP security
    	
    	String[] excludeUri = {"/", "/index.jsp", "/member/login", "/member/member",
    			"/member/userIdCheck", "/member/complete", "/member/pwdFind", "/member/expired",
    			"/dist/**", "/uploads/photo/**"};
    	
    	http.cors(Customizer.withDefaults())
    		.csrf(AbstractHttpConfigurer::disable)
    		.headers(headers -> headers
    			.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    	
        http
        	.authorizeHttpRequests( authorize ->  authorize
        		.requestMatchers(excludeUri).permitAll()
        		.requestMatchers("/admin/**").hasRole("ADMIN")
    			.requestMatchers("/**").hasAnyRole("USER", "ADMIN")   // configurer에서 ROLE_ 붙여줌
                .anyRequest().authenticated() // 설정외 모든요청은 권한과 무관하고 로그인 유저만 사용
        	)
        	.formLogin(login -> login
                .loginPage("/member/login")
                .loginProcessingUrl("/member/login")
                .usernameParameter("userId")
                .passwordParameter("userPwd")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                .permitAll()
        	)
        	.logout(logout -> logout
        		.logoutUrl("/member/logout")
        		.invalidateHttpSession(true)
        		.deleteCookies("JSESSIONID")
        		.logoutSuccessUrl("/")
        	)
        	.addFilterAfter(ajaxSessionTimeoutFilter(), ExceptionTranslationFilter.class)
        	.sessionManagement(management -> management
        		.maximumSessions(1)
        		.expiredUrl("/member/expired"));

        // 인증 거부 관련 처리
        http.exceptionHandling((exceptionConfig)-> exceptionConfig.accessDeniedPage("/member/noAuthorized"));

        return http.build();
    }
    
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    LoginSuccessHandler loginSuccessHandler() {
    	LoginSuccessHandler handler = new LoginSuccessHandler();
    	handler.setDefaultUrl("/");
    	return handler;
    }

    @Bean
    LoginFailureHandler loginFailureHandler() {
    	LoginFailureHandler handler = new LoginFailureHandler();
    	handler.setDefaultFailureUrl("/member/login?login_error");
    	return handler;
    }

    @Bean
    AjaxSessionTimeoutFilter ajaxSessionTimeoutFilter() {
    	AjaxSessionTimeoutFilter filter = new AjaxSessionTimeoutFilter();
    	filter.setAjaxHeader("AJAX");
    	return filter;
    }
/*
    // @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        final String usernameQuery = "SELECT userId AS username, userPwd AS password, enabled FROM member WHERE userId=?";
        final String authQuery = "SELECT userId AS username, authority AS authority FROM memberAuthority WHERE userId=?";

        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery(usernameQuery)
            .authoritiesByUsernameQuery(authQuery)
            .passwordEncoder(passwordEncoder());
    }
*/    
}
