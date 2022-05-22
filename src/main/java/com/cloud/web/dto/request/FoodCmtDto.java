package com.cloud.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodCmtDto {

    private String cmt; // 댓글 창에서 댓글을 입력 받기 위한 필드

    // 댓글을 작성한 user 정보는 springContextHolder에서 가져온다.

    // 어떤 id의 게시글에서 작성할 것에 대한 정보는 Url에 담겨 있다. ( URL에 해당 게시글의 db 아이디가 담겨 있다 )

    @Builder
    public FoodCmtDto(String cmt) {
        this.cmt = cmt;
    }
}

