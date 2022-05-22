package com.cloud.web.dto.response;

import com.cloud.web.domain.FoodBoard;
import com.cloud.web.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserResponse {

    private Long db_id;
    private String name;
    private String nickname;
    private String email;
    private Role role;
    private List<FoodBoard> foodBoardList;

    @Builder
    public UserResponse(Long db_id, String name, String nickname, String email , Role role , List<FoodBoard> foodBoardList) {
        this.db_id = db_id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.foodBoardList = foodBoardList;
    }
}
