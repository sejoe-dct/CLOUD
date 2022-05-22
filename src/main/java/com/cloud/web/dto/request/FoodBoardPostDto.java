package com.cloud.web.dto.request;

import com.cloud.web.domain.FoodBoard;
import com.cloud.web.domain.FoodType;
import com.cloud.web.domain.LocationType;
import com.cloud.web.domain.User;
import com.cloud.web.domain.enums.AttachmentType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
/**
 * 실제 데이터가 이동하는 부분 user 정보까지 합쳐서
 */
public class FoodBoardPostDto {

    private User user;

    private LocationType locationType;
    private FoodType foodType;
    private String title;
    private String preview;
    private String address;
    private String info;
    private double rate;


    private Map<AttachmentType, List<MultipartFile>> attachmentFiles = new ConcurrentHashMap<>();

    /*
        dto에는 user 정보와 , 저장할 파일 리스트를 가지고 있다.
     */
    @Builder
    public FoodBoardPostDto(User user, LocationType locationType, FoodType foodType, String title, String preview,
                            String address, String info, double rate,
                            Map<AttachmentType, List<MultipartFile>> attachmentFiles) {

        this.user = user;
        this.locationType = locationType;
        this.foodType = foodType;
        this.title = title;
        this.preview = preview;
        this.address = address;
        this.info = info;
        this.rate = rate;
        this.attachmentFiles = attachmentFiles;

    }

    /**
     * 실제 repository에서 저장하기 직전 dto를 Entity로 바꿔주는 역할
     * @return
     */
    public FoodBoard createBoard() {

        return FoodBoard.builder()
                .user(user)
                .locationType(locationType)
                .foodType(foodType)
                .title(title)
                .preview(preview)
                .address(address)
                .info(info)
                .rate(rate)
                .attachedFiles(new ArrayList<>())
                .build();

    }


}
