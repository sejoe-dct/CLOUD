package com.cloud.web.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "festival_board")
public class FestivalBoard {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "festival_board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_festival_board_to_users"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    // 아래부터 Admin 등록 필요 필드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_type_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_festival_board_location_type"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LocationType locationType;

    @Column(name = "festival_info" , length = 2000 , nullable = false)
    private String info;

    @Column(name = "festival_picture" )
    private String picture;

    @Column(name = "festival_is_opened" )
    @ColumnDefault(" 0 ") // 의미 없음 @persist에 의해서
    private Boolean isOpened;

    @Column(name = "festival_like" )
    @ColumnDefault(" 0 ") // 의미 없음 @persist에 의해서
    private Integer like; // 추천수, 좋아요

    @PrePersist
    public void prePersist(){
        this.isOpened = this.isOpened == null ? false : this.isOpened;
        this.like = this.like == null ? 0 : this.like;
    }


    // 아래부터 open API
    private String fstvlNm;		//축제명 O

    private String opar;		//개최장소 O

    private String fstvlStartDate;		//축제시작일자 O

    private String fstvlEndDate;		//축제종료일자 O

    private String fstvlCo;		//축제내용 O

    private String mnnst;		//주관기관

    private String auspcInstt;		//주최기관

    private String suprtInstt;		//후원기관

    private String phoneNumber;		//전화번호 O?

    private String homepageUrl;		//홈페이지주소 O?

    private String relateInfo;		//관련정보

    private String rdnmadr;		//소재지도로명주소 O (roadnumaddress)

    private String lnmadr;		//소재지지번주소

    private String latitude;		//위도 O

    private String longitude;		//경도 O

    private String referenceDate;		//데이터기준일자

    private String instt_code;		//제공기관코드

    private String instt_nm;		//제공기관기관명


    public FestivalBoard() {
    }

}
