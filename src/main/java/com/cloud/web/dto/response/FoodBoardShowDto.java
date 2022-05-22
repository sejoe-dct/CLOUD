package com.cloud.web.dto.response;

import com.cloud.web.domain.Attachment;
import com.cloud.web.domain.FoodType;
import com.cloud.web.domain.LocationType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 게시글에서 상세 페이지로 갔을때 보여지는 dto
 */
@Getter
@Setter
@NoArgsConstructor
public class FoodBoardShowDto {

    private Long id;
    private LocationType locationType;
    private FoodType foodType;
    private String title;
    private String preview;
    private String address;
    private String info;
    private double rate;

    private List<Attachment> attachedFiles;

    @Builder
    @QueryProjection
    public FoodBoardShowDto(Long id, LocationType locationType, FoodType foodType, String title,
                            String preview, String address, String info, double rate,
                            List<Attachment> attachedFiles) {
        this.id = id;
        this.locationType = locationType;
        this.foodType = foodType;
        this.title = title;
        this.preview = preview;
        this.address = address;
        this.info = info;
        this.rate = rate;
        this.attachedFiles = attachedFiles;
    }



}
