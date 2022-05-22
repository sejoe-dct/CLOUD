package com.cloud.web.config.auth;



import com.cloud.web.domain.User;
import com.cloud.web.dto.response.UserResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


//  시큐리티가 /login 주소 요쳥이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되어 로그인 성공을 하면 해당 유저의 정보를 시큐리티가 가지고 있는 고유한 session에 저장을 한다.
// 같은 session 공간인데 자신 만의 시큐리티 session 공간을 가진다.
// (Security ContextHolder) 라는 Key가 되는 곳에 session 정보를 저장을 한다.
// contextHolder에 들어갈 수 있는 session 정보의 Object가 정해져 있다.
// security 자기 자신이 가지고 있는 고유한 session 저장소 ( Security ContextHolder )에는 특정 Object로 들어와야 하는데
// 특정 Object는 Authenticaion 객체이여야 한다.
// Authentication 안에는 결국 User 정보가 있어야 한다.
// User의 정보도 특정한 Type으로 감싸져여 한다. User 정보를 담고 있는 객체의 Type은 -> UserDetails라는 타입의 객체로 로그인에 성공한 user 정보가 감싸져 있어야 한다.
// Security Session ( Security ContextHolder ) -> Authentication -> UserDetails (실제 User 정보를 감싸고 있다.)
// UserDetails 객체를 get으로 꺼내면, 드디어 최종적으로 User Class의 객체에 접근할 수 있다.
// 어떻게 UserDetails에서 User Class 객체로 접근할 수 있었을까??
// PrincipalDetails 해당 객체가 UserDetails랑 같은 Type으로 실제 로그인에 성공한 User Class 객체 정보를 담고 있으면 접근이 가능하다.
// 따라서ㅣ,,, 내 생각에는 로그인에 성공한 User Class 객체를 PrincipalDetails 로 감싸서 Security Session, Authenticaion에 넣을  수 있다.
// Authenticaion 객체를 만들어서 security session에 넣어야 한다.
// PrincipalDetails 는 PrincipalDetailsService와는 달리 @Bean으로 등록하지 않았다!! 왜?? 강제로 띄울 예정이다.


// 일단 우선적으로 내 생각에는 User 가 로그인을 성공했는지에 대한 과정이 먼저 우선되어야 한다. ( 그래야지 로그인된 해당 유저로부터 권한 ROLE를 가져올 수 있다.)
// 즉 이 Class 보다 먼저 선수행 되어야 하는 과정이 필요하다 (로그인 폼에 입력한 정보가 DB에 등록된 유저인지 확인하는 과정)

//@bean으로 등록된 PrincipalDetailsService 에서 loadUserByUsername() 으로 부터 사용이 되기 때문에 굳이 @Bean으로 등록하지 X

/**
 * getUser 추가
 */
public class PrincipalDetails implements UserDetails {

    // override 해줘야 한다.
    // 우리의 로그인에 성공한 정보는 User Class 객체가 가지고 있다.
    private User user; //콤포지션

    // PrincipalDetails 가 실제 로그인에 성공한 User 정보를 담기 위해서 생성자로 해당 User 객체를 받아서 감싸준다.
    public PrincipalDetails(User user) {
        this.user = user;
    }

    public UserResponse getUser() {

        UserResponse userDto = UserResponse.builder()
                .db_id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail()).build();

        return userDto;
    }

    // return Type이 Collection에 GrantedAuthority 를 return 하는 return Type이 필요하다.
    // 해당 User의 권한을 return 하는 곳 , 기본 security에서는 user가 로그인할 시점에 user 권한을 받는 것이 아닌, 회원 가입 시점에서부터
    // 이미 USER 라는 권한을 가지고 있다. 그래서 로그인 시점에서는 해당 로그인이 성공하면 실제 해당 유저 정보를 가지고 있는 DB에서
    // 권한 정보를 가져오는 역할을 한다. 그래서 사실, PrincipalDetails 가 감싸고 있는 로그인에 성공한 User Class 객체가 가지고 있는
    // ROLE 정보를 get으로 가져오면 된다. 그렇게 할 수 있었던 이유는, 로그인에 성공된 해당 User class 객체 안에 이미 반영된 USER 권한을 가지고 있기 때문에
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // 나의 경우는 Enum Role Type 이다. ...
        // user.getRoleType(); // 근데 user ROLE이 Enum이다. String Type 으로 바꿔줄 필요 있다. 그래서 GrantedAuthority 으로 만들어주는게 필요하다.

        Collection<GrantedAuthority> collection = new ArrayList<>();
        // collection 안에는 GrantedAuthority이 들어가야 하니깐 새롭게 GrantedAuthority 객체를 생성해준다.
        collection.add(new GrantedAuthority() {
            // 정해진 메소드에서는 String Type만을 return 할 수 있다.
            @Override
            public String getAuthority() {
                return user.getRoleType().getAuthority(); // getRoleType 는 Enum 객체를 반환하지만 그 안에서 String 형태를 하고 있는 Authority 를 반환하고자 한다.
            }
        });

        return collection; // 그리고 Collection 형태로 하는 이유는 한 User에 대해서 여러 권한을 가지고 있을 수 있기 때문이다.
    }

    @Override // 로그인 한 사용의 비밀번호를 얻어서 Spring Security가 알아서 확인해준다.
    public String getPassword() {
        return user.getPassword();
    }

    // 사용자의 id를 반환 unique 한 값 ( 즉 사용자의 이름이 아니다.)
    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    // 계정이 만료 되었니 물어보면은??그냥 아니요 라고 우선적으로 답을 한다. (TRUE 설정)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 니 계정 잠겻니?? 일단 아니요 라고 대답한다. TRUE 설정
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 니 계정의 비밀번호가 1년이 지냤니?? 이거 구현할꺼야?? 아니요 (TRUE 설정)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 니 계정이 활성화 되었니?? 이거 구현할꺼니?? 아니요 (TRUE 설정)
    @Override
    public boolean isEnabled() {

        // 언제 어떻게 활성화를 하냐면
        // 예를 들어서 우리 사이트에서 1년 동안 회원이 로그인 안하면 휴면 계정으로 바꾼다고 하면은
        // user 모델에 Timestamp으로 loginDate를 만들어줘야 한다.
        // 현재 시간 - user.getLoginDate() (마지막 로그인 시간)  : 1년을 초과하면 return false 한다.;

        return true;
    }





}
