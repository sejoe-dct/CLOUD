package com.cloud.web.config.Security;

import com.cloud.web.config.auth.LoginFailureHandler;
import com.cloud.web.config.auth.LoginSuccessHandler;
import com.cloud.web.config.auth.WebAccessDeniedHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URLEncoder;

/**
 * .defaultSuccessUrl("/") 를
 * successHandler(new LoginSuccessHandler()) 여기에서 처리하도록 구현했음
 */


@Configuration
@EnableWebSecurity // 해당 클래스가 시큐리티 필터 체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true , prePostEnabled = true)  // secured 어노테이션 활성화 Controller 주소위에 @secured라고 하면 // preAuthorize 활성화 또한 동시에 PostAuthorize 활성화 근데 해당 PostAuthorize는 해당 메소드가 끝나고 실행해서 추천 X
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private  LoginFailureHandler loginFailureHandler;

    private  LoginSuccessHandler loginSuccessHandler;

    private  WebAccessDeniedHandler webAccessDeniedHandler; //


    @Autowired
    public SecurityConfig(LoginFailureHandler loginFailureHandler, LoginSuccessHandler loginSuccessHandler, WebAccessDeniedHandler webAccessDeniedHandler) {
        this.loginFailureHandler = loginFailureHandler;
        this.loginSuccessHandler = loginSuccessHandler;
        this.webAccessDeniedHandler = webAccessDeniedHandler;
    }

    // 시큐리티 에서 비밀번호를 암호화 하기 위해서 사용한다.
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }


    /* static 관련설정은 무시 */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring().antMatchers( "/static/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/foodForm/**").authenticated() // 여기서 굉장히 중요한 기능이!! 로그인 하지 않은 user 사용자가 해당 주소로 갈려고 하면 최소한은 로그인은 되어야 하기 때문에 /login으로 넘어가고, 넘어가서 로그인이 인증이 되면 리다이렉트 주소로 /user로 온다. (접속을 시도한 시점의 URL로 온다.)
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 권한이 없으면 403 나와야 한다. hasRole은 앞에 "ROLE_"을 붙인 상태에서 확인하고, hasAnyAuthority는 붙지 않는다.?? 그래서 접근은 가능하지만 @Secured 기능에서 사용되지 않는다.
                .anyRequest().permitAll()

                .and() // 로그인 조차 하지 않았을때 여기로 넘어온다. USER로 로그인 했는데 권한이 없는 경우, 403 Error가 난다.
                .formLogin()
                .loginPage("/login?alertmsg=" + URLEncoder.encode("로그인 필요합니다.", "UTF-8")) // URL에 "/logout" 이라고 하면 "/loginForm" 경로로 다시 돌아가고 결국 LoginForm이 나타난다.
                .loginProcessingUrl("/loginProcess") // login 주소가 호출이 되면 시큐리티가 대신 낚아채서 대신 로그인을 진행한다. 사용자 원하느걸로 해도 ㄱㅊ 근데 html에서의 action이랑 같은 설정으로 해야한다.
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
                //.defaultSuccessUrl("/") // successHandler에서 일반 로그인 시 "/"로 설정, 권한 필요한 페이지 승인 거절 났을때 우선 로그인 페이지로 이동하여 성공하면 , 요청했던 페이지로 redirect


                .and()
                .logout() // "/logout"이 default 값이다.
                .logoutSuccessUrl("/") //main으로 돌아가게 설정
                .deleteCookies("JSESSIONID")

                .and()
                .exceptionHandling().accessDeniedHandler(webAccessDeniedHandler); // 모든 403에 대한 에러를 처리하는 핸들러





    }
}
