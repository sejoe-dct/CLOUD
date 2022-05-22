package com.cloud.web.domain;

import com.cloud.web.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * default 속성을 이용하고 싶을때는 columnDefinition 를 통해서 table에 들어갈 필드 속성을 '모두' 작성한다.
 * unique = true 속성을 사용하게 되면, uniqye 제약조건의 이름이 특정한 코드의 문자열로 할당된다.
 *
 * 또 다른 방법으로 default값을 넣어줄때는 ColumnDefault를 통해서 Table에도 default값이 ~가 들어와야 한다고 명시를 하고
 * (실제 의미 X , 동작 시점은 save()로 DB에 한번 들어갔다가 한번 꺼내서 영속성 컨텍스트에 관리를 하고 나서 다시 DB에 들어갈때 작동 )
 * 따라서 테이블에는 그냥 명시만 해주고 실제 동작은 prePersist()를 통해서 override 구현해주도록 한다.
 */
@Entity
@Getter
@Table(name = "users" )
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_loginId" , columnDefinition = "varchar(255) NOT NULL UNIQUE ")
    private String loginId; // 사용자 로그인 아이디

    @Column(name = "user_password" , nullable = false)
    private String password;

    @Column(name = "user_name" , nullable = false)
    private String name; // 사용자 실제 이름 ex) 이수찬

    @Column(name = "user_nickname" , columnDefinition = " varchar(255) NOT NULL UNIQUE ")
    private String nickname;

    @Column(name = "user_email" , nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 10 , nullable = false)
    @ColumnDefault("'ROLE_USER'") // 의미 없음 @persist에 의해서 , 이렇게 해서 얻을 수 있는 이점은 prePersist할때 USER만 들어올  수 있고 , 다른 값이 들어오면 error 뱉는다.
    private Role roleType;


    /**
     * User 입자에서 자신이 등록한 게시물의 리스트를 확인한다.
     * 일대다 양방향 연관관계에서 List로 사용해서 활용 추가 - 2022-05-19
     */
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<FoodBoard> foodBoards = new ArrayList<>();

    /**
     * 연관 관계 편의 메서드
     * User Entity가 Db에 저장되는 시점에는 게시글이 없음으로
     * 게시글이 save될때 해당 user의 게시글 list에 데이터를 넣어준다.
     */
    public void addFoodBoard(FoodBoard foodBoard) {
        this.foodBoards.add(foodBoard);
    }







    /**
     * persist() 메서드는 save()에 의해서 엔티티가 영속성컨텍스트에 관리하기 직전에 호출 된다.
     * 식별자 생성 전략을 사용한 경우 엔티티에 식별자는 아직 존재 하지 않는다. 새로운 인스턴스를 merge할 때도 수행된다
     * DB에 default 값을 넣어줄때 유용하다.
     */
    // @PrePersist
    //  public void prePersist(){
    //    this.roleType = this.roleType == null ? Role.ROLE_USER : this.roleType;
    //  }

    public User() {
    }

    @Builder
    public User(String loginId, String password, String name, String nickname, String email, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.roleType = role;

    }
}
