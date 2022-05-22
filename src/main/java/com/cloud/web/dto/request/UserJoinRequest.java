package com.cloud.web.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * userloginRequest에서
 * userJoinRequest로 이름변경
 */
@Getter
@NoArgsConstructor
@Setter
public class UserJoinRequest {

    private String username; // 아이디
    private String password; // 비밀번호
    private String name;
    private String nickname;
    private String email;


}
