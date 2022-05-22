package com.cloud.web.dto.request;

import com.cloud.web.domain.BaseTimeEntity;
import com.cloud.web.domain.FoodType;
import com.cloud.web.domain.LocationType;
import com.cloud.web.domain.User;
import com.cloud.web.domain.enums.AttachmentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 해당 dto는 단순히 등록 웹 맛집 등록 폼에서 보여지는 값만 가져온다.
 * 추후 DTO로 변환을 통해 누가 업로드 했는지 알기 위해
 * user 추가할 예정 BoardPostDto에서..
 */
@Getter
@Setter // 바인딩 하기 위해서 반드시 존재해야 한다.
@NoArgsConstructor
public class FoodBoardPostFormDto extends BaseTimeEntity {


    //private Long id;
    //private User user;
    //private String picture; // 사진 경로 타입 의문,,
    //private Integer like;

    /**
     * 실제 웹 맛집 등록 폼으로부터 입력 받는 값들만 선언
     */
    private LocationType locationType;
    private FoodType foodType;
    private String title;
    private String preview;
    private String address;
    private String info;
    private double rate;


    /**
     * 추가적으로 해당 이미지 파일 과 일반 파일도 list로 함께 웹 폼에서 받는다.
     */
    private List<MultipartFile> imageFiles;
    //private List<MultipartFile> generalFiles;



/*
    @Builder
    public FoodBoardSaveDto(User user, LocationType locationType, FoodType foodType, String title, String preview, String picture, String address, String info, double rate, List<MultipartFile> imageFiles, List<MultipartFile> generalFiles ) {
        this.user = user;
        this.locationType = locationType;
        this.foodType = foodType;
        this.title = title;
        this.preview = preview;
        this.picture = picture;
        this.address = address;
        this.info = info;
        this.rate = rate;

        this.imageFiles = (imageFiles != null) ? imageFiles : new ArrayList<>();
        this.generalFiles = (generalFiles != null) ? generalFiles : new ArrayList<>();

    }
*/

    /**
     *  우선 살펴보면 dto를 만들때, 일반 파일에 아무 값도 들어가 있지 않으면 그냥 null인 list가 담긴다.
     *  추가적으로 단순히 웹 폼에서의 데이터를 가져오기 때문에 user의 정보가 저장 X
     *
     *  바인딩의 목적으로 만들어졌다.
     */
    @Builder
    public FoodBoardPostFormDto(LocationType locationType, FoodType foodType, String title, String preview, String address,
                                String info, double rate, List<MultipartFile> imageFiles ) {

        this.locationType = locationType;
        this.foodType = foodType;
        this.title = title;
        this.preview = preview;
        this.address = address;
        this.info = info;
        this.rate = rate;
        this.imageFiles = (imageFiles != null) ? imageFiles : new ArrayList<>() ;


    }


    /**
     * web에서 입력 값을 다시 user 정보가 담긴 dto로 다시 생성
     *
     *  웹에서 값을 입력 후 controller로 보내면 해당 웹 폼 dto가 바인딩 되어서 받는다..
     *  바인딩이 되더라도, user 정보가 없기 떄문에 user 정보를 담을 dto를 생성할 필요가 있다.
     *
     *  해당 BoardPostDto은 Entity로 전환되기 직전의 dto이므로 모든 정보를 담고 있어야 한다. (user 정보까지)
     */
    public FoodBoardPostDto createBoardPostDto(User user) {

        // 웹에서 2개의 날것의 list를 받아서 하나의 Map 형태로 만들어서 BoardPostDto를 생성한다.
        // 여전히 BoardPostDto에서도 아직 날것의 2개의 list이지만

        // FoodBoardService 에서 해당 날갓의 2개 List를 -.> 하나의 의미있는 list로 묶어준다.

        Map<AttachmentType, List<MultipartFile>> attachments = getAttachmentTypeListMap();

        return FoodBoardPostDto.builder()
                .user(user)
                .locationType(locationType)
                .foodType(foodType)
                .title(title)
                .preview(preview)
                .address(address)
                .info(info)
                .rate(rate)
                .attachmentFiles(attachments)
                .build();
    }

    /**
     * 일반 사진 파일과 첨부 파일을 하나의 MAP형태로 묶어주는 역할
     * @return
     */
    private Map<AttachmentType, List<MultipartFile>> getAttachmentTypeListMap() {

        Map<AttachmentType, List<MultipartFile>> attachments = new ConcurrentHashMap<>();

        attachments.put(AttachmentType.IMAGE, imageFiles);
        //attachments.put(AttachmentType.GENERAL, generalFiles);

        return attachments;
    }






}
