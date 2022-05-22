package com.cloud.web.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "festival_cmt")
public class FestivalCmt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "festival_cmt_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_festival_cmt_to_users"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_board_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_festival_cmt_to_festival_board"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FestivalBoard festivalBoard;

    @Column(name = "festival_cmt" , length = 2000 , nullable = false)
    private String festivalCmt;

    public FestivalCmt() {
    }

    public FestivalCmt(User user, FestivalBoard festivalBoard, String festivalCmt) {
        this.user = user;
        this.festivalBoard = festivalBoard;
        this.festivalCmt = festivalCmt;
    }
}
