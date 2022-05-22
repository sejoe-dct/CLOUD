package com.cloud.web.domain.enums;


/*
 * JWT가 아닌 단순 Spring Security를 통해서 권한을 부여, 검증하기 하는 환경에서는
 * Enum 각 객체 명을 앞에는 ROLE_ 으로 구성하고 각각의 String 필드값도 "ROLE_ " 와 같은 형식을 유지하도록 한다.
 * Enum 객체의 String 필드값도 UserDetails에서 public Collection<? extends GrantedAuthority> getAuthorities() 으로 String 값을 사용하기 때문에
 * String 필드 값에도 "ROLE_"형식을 유지하는 것이 좋다.
 *
 * ROLE_ 로 앞에 구성을 하면 , Security Config에서 hasRole("ROLE_USER") 로 사용할 수 있고
 * 각 Controller Method에서 @Secured("ROLE_ADMIN") 와  @PreAuthorize("hasRole('ROLE_ADMIN')") 를 붙여서 사용이 가능하다.
 * 다양한 상황에 대해서도 유연하게 대처하기 위해 가능한 "ROLE_ " 형식으로 만들자.
 *
 * 단순 Spring Security에서는 User의 권한을 DB에 담는다.
 * JWT는 권한 정보를 DB에 담지 않고 Token에 담는다.
 * 단순 Spring Security는 사용자의 로그인 상태는 서버 session에 담는다.
 * JWT는 만료 기간도 포함하고 있는 Token을 사용자 Cookie에 담는다.
 * JWT에 담을 Token의 Type은 String으로 넣는다. Emum Class의 String 필드 값으로 들어간다.
 *
 */
public enum Role {


    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");


    private String Authority;

    Role(String authority) {
        this.Authority = authority;
    }

    public String getAuthority() {
        return Authority;
    }
}
