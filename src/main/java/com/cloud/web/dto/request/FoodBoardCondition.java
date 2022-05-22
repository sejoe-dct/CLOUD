package com.cloud.web.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class FoodBoardCondition {

    // 맛집 제목으로 검색
    private String title;
    // 맛집 지역으로 검색
    private Long locationType_Id;
    // 맛집 음식 유형으로 검색
    private Long foodType_id;

    @Builder
    public FoodBoardCondition(String title, Long locationType_Id, Long foodType_id) {
        this.title = title;
        this.locationType_Id = locationType_Id;
        this.foodType_id = foodType_id;
    }

}
