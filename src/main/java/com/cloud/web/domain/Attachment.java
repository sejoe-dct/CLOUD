package com.cloud.web.domain;

import com.cloud.web.domain.enums.AttachmentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originFilename;

    private String storeFilename;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="food_board_id")
    private FoodBoard board;

    @Enumerated(EnumType.STRING)
    private AttachmentType attachmentType;

    @Builder
    public Attachment( String originFileName, String storePath, AttachmentType attachmentType) {
        this.originFilename = originFileName;
        this.storeFilename = storePath;
        this.attachmentType = attachmentType;

    }
}
