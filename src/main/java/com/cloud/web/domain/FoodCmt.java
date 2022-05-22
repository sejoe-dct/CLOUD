package com.cloud.web.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter // 우선은 지금 foodBoard에서 list로 담고 있는 cmt에 foodBoard를 저장하기 위해서 임시적으로 setter 사용,,,권장 X , 추가적으로 값을 바인딩하기 위해서 필요
@Table(name = "food_cmt" )
public class FoodCmt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_cmt_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_food_cmt_to_users"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_board_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_food_cmt_to_food_board"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FoodBoard foodBoard;

    @Column(name = "food_cmt" , length = 2000 , nullable = false)
    private String foodCmt;


    public FoodCmt() {
    }

    @Builder
    public FoodCmt(User user, FoodBoard foodBoard, String foodCmt) {
        this.user = user;
        this.foodBoard = foodBoard;
        this.foodCmt = foodCmt;

    }
}
