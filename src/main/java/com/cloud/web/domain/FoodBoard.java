package com.cloud.web.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "food_board")
public class FoodBoard extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_food_board_to_users"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_type_id" ,nullable = false , foreignKey = @ForeignKey(name = "fk_food_board_to_location_type"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LocationType locationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_type_id", nullable = false , foreignKey = @ForeignKey(name = "fk_food_board_to_food_type"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FoodType foodType;

    //
    @Column(name = "food_title" , nullable = false)
    private String title;

    @Column(name = "food_preview" , nullable = false)
    private String preview;

    //@Column(name = "food_picture" )
    //private String picture; // 사진 경로 타입 의문,,

    @Column(name = "food_address" , length = 2000 , nullable = false)
    private String address;

    @Column(name = "food_info" , length = 2000 , nullable = false)
    private String info;

    @Column(name = "food_rate" , nullable = false)
    private double rate;

    @Column(name = "food_like" )
    @ColumnDefault(" 0 ") // 의미 없음 @persist에 의해서
    private Integer like;

    @PrePersist
    public void prePersist(){
        this.like = this.like == null ? 0 : this.like;
    }

    public FoodBoard() {
    }


    /**
       foodBoard에서 list 형태로 파일 목록을 가지고 있다.
    */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Attachment> attachedFiles = new ArrayList<>();

    /**
     * 연관 관계 편의 메서드
     * @param attachedFiles 맛집 게시글에 등록시 첨부하는 사진 목록
     */
    public void addAttachedFiles(List<Attachment> attachedFiles) {
        attachedFiles.stream().forEach(e->e.setBoard(this)); // 내가 추가함!!!!!
        this.attachedFiles = attachedFiles;
    }


    /**
     * FoodBoard는 cmt을 알기 위해서
     * 일대다 양방향 연관관계에서 list로 사용해서 활용 추가 - 2022-05-17
     */
    @OneToMany(mappedBy = "foodBoard" , cascade = CascadeType.ALL)
    private List<FoodCmt> foodCmts = new ArrayList<FoodCmt>();

    /**
     * 연관 관계 편의 메서드
     */
    public void addFoodCmts(FoodCmt foodCmt) {

        foodCmt.setFoodBoard(this);
        this.foodCmts.add(foodCmt);
       /*
        FoodCmt build = foodCmt.builder()
                .user(foodCmt.getUser())
                .foodBoard(this)
                .foodCmt(foodCmt.getFoodCmt()).build();
        */
    }


    public FoodBoard changeToEntity(Long id, User user, LocationType locationType,
                            FoodType foodType, String title, String preview ,
                            String address, String info, double rate,
                            List<Attachment> attachedFiles){
        this.id = id;
        this.user = user;
        this.locationType = locationType;
        this.foodType = foodType;
        this.title = title;
        this.preview = preview;
        this.address = address;
        this.info = info;
        this.rate = rate;
        this.attachedFiles = attachedFiles;

        return this;
    }



    @Builder
    public FoodBoard(Long id, User user, LocationType locationType, FoodType foodType, String title, String preview , String address, String info, double rate,
                     List<Attachment> attachedFiles) {
        this.id = id;
        this.user = user;
        this.locationType = locationType;
        this.foodType = foodType;
        this.title = title;
        this.preview = preview;
        //this.picture = picture;
        this.address = address;
        this.info = info;
        this.rate = rate;
        this.attachedFiles = attachedFiles;
    }
}
