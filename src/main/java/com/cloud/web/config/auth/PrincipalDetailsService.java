package com.cloud.web.config.auth;


import com.cloud.web.domain.User;
import com.cloud.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Authenticaion 객체를 만들기 위해서 만든 Class이다.
// 그 전 단계인 PrincipalDetails 는 로그인에 성공한 User Class 객체를 담고 있다.
// Service라고 띄워준다.  Bean 등록
// Security 설정에서 그러니깐, SecurityConfig에서 loginProcessingUrl("/login") 으로 걸어놨다.
// "/login" 요청이 오면, 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername() 함수가 실행된다. 하나의 규칙이다.!!!!!
// UserDetailsService 타입으로 반드시 만들어줘야 한다.
// 그래야지 UserDetailsService Type으로 등록된 loadUserByUsername 가 호출이 된다.
// @Service라고 하면 PrincipalDetailsService 가 IoC에 등록이 된다. IoC에 등록이 되어 있으니깐 자동으로 호출이 가능하다.

@Slf4j
@Service
public class PrincipalDetailsService implements UserDetailsService {

    // override 해줘야 한다.
    // 아래 주석문 정말 중요하다.!!!!!!!!!!!!
    // loadUserByUsername(String username) 에서 사용되고 있는 username은
    // 로그인 폼이 있는 페이지에서 해당 사용자가 로그인을 위해 입력한 "아이디"가 들어갈 자리(칸)의 '속성 id' 변수명이랑 일치해야 한다!!!!! 정말 중요!!!!!!!!!!!
    // 만약  <input type="text" name="username" placeholder="Username 입력" /><br>
    // 위에서 name="username"이 아닌 "id"라고 하면 Security가 알아채지 못 하고 못 가져온다.
    // 메소드 파리미터읜 username은 절대 건들면 안된다!!!!
    // 만약 바꾸고 싶다고 하면은 Security Config에 있는  .usernameParameter("사용자가 정의한 username, loadbyUsername") 처럼 정의 해줘야 한다.
    /*
     * .and()
             .formLogin()
             .loginPage("/login")
             .usernameParameter("사용자가 정의한 username, loadbyUsername 메소드와 매핑하기 위해서")
             .loginProcessingUrl("/login")
             .defaultSuccessUrl("/");
             *
             *
             * 그냥 되도록 이면 바꾸지 말고 그래도 username으로 로그인 폼에서 사용해라!!!!
     */
    // 그래서 흐름을 정리해보면
    // 로그인 폼에서 로그인하는 버튼을 클릭하면 현재 프로젝트에서는 "/login"으로 들어가게 되고 해당 경로는
    // SecurityConfig에서 설정한 경로가 우선권을 가진다.
    // 스프링은 Ioc 컨테이너에서 "userDetailsService" 로 등록된 Type을 찾는다.
    // PrincipalDetailsService 가 찾아지면 바로, "loadUserByUsername" 을 호출한다.
    // 그때 로그인 폼으로부터 넘어온 "username" 파라미터를 가져온다.
    // 그러면 이제 loadUserByUsername()에서 뭘 하면 되냐면?? 우선은 userRepository를 Autowired 해준다.
    // 로그인 폼으로 넘어온 "username" 즉 유저 고유 아이디를 가지고 실제 DB에 유저 정보가 있는지 확인 과정 필요

    @Autowired
    private UserRepository userRepository;

    @Override // 해당 username은 사용자 고유한 Id 값이다 unique로 설정된!!!
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("username    " + username);

        //Optional<User> userEntity = userRepository.findByLoginId(username);

        User userEntity = userRepository.findByLoginId(username).orElse(null);


        // 바로 이 시점이 security가 알아서 로그인 폼에 입력한 사용자 정보를 가지고
        // 실제 DB에서 데이터가 있는지 findByLoginId()를 통해서 확인한다.
        // 해당 유저가 존재를 하면, 로그인에 성공하였고 그 User Class 객체를
        // PrincipalDetails 로 감싸서 여기서 잠깐, PrincipalDetails로 감싸졌다는 의미는
        // 해당 User의 Class 객체가 Authentication로 감싸졌다는 의미이다.
        // 그러고 나서 PrincipalDetailsService 에 return으로 넘겨준다.
        // 이렇게 @Bean 으로 등록된 PrincipalDetailsService 가 PrincipalDetails를 사용하기 때문에 굳이
        // PrincipalDetails 를 Bean으로 등록시킬 이유가 없다.

        // 만약 여기서 찾았다고 하면은 , 즉 로그인 하고자 하는 유저를 찾았다고 하면
        // UserDetails를 return 하게 되어 있다.
        // 시큐리티 session => Authentication => UserDetails 원래 이런 과정이있다.
        // PrincipalDetails 안에 반드시 User Entity가 들어가는것이 활용하기가 편하다.
        // return 된 값은 시큐리티 session => Authentication (내부에 UserDetails가 들어가게 된다.)
        // 그러면 Session 에서는 Security session( 내부에 Authentication (내부에 UserDetails가 들어가게 된다.))

        // 다시 말해 return 된 PrincipalDetails 가 Authentication 에 쏙 들어가게 되고
        // 그러면서 이 Authentication 을 Security Session에 넣어준다.
        // 이러한 모든 과정을 loadUserByUsername 가 다 알아서 해준다.
        // 이렇게 하면 로그인이 완료가 된다.

        if(userEntity != null) {
           return new PrincipalDetails(userEntity);
        }

        return null;

        // 이제 이렇게 하고 로그인한 user의 권한을 확인하고 싶다고 하면
        // PrincipalDetailsService 를 Autowired를 하고 가져온 객체에서
        // loadUserByUsername() 를 호출하여, UserDetails를 가져오고
        // 반환된 PrincipalDetails 안에서 만들어진 getAuthorities() 통해 Collection Type으로 ROLE을 가져올 수 있다.
    }
}
