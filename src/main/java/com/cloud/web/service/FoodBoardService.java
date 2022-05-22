package com.cloud.web.service;

import com.cloud.web.domain.*;
import com.cloud.web.domain.enums.AttachmentType;
import com.cloud.web.dto.request.FoodBoardPostDto;
import com.cloud.web.dto.request.FoodBoardPostFormDto;
import com.cloud.web.dto.request.FoodCmtDto;
import com.cloud.web.dto.response.FoodBoardShowDto;
import com.cloud.web.repository.AttachmentRepository;
import com.cloud.web.repository.FoodBoardRepository;
import com.cloud.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FoodBoardService {


    @Autowired
    private FoodBoardRepository foodBoardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttachmentServiceImpl attachmentServiceImpl;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private FileStore fileStore;

/*
    public FoodBoard save(Long user_db_id , FoodBoardSaveDto foodBoardDto){

        User user = userRepository.findById(user_db_id).get();

        FoodBoard saveFoodBoard = FoodBoard.builder().user(user)
                .locationType(foodBoardDto.getLocationType())
                .foodType(foodBoardDto.getFoodType())
                .title(foodBoardDto.getTitle())
                .preview(foodBoardDto.getPreview())
                .address(foodBoardDto.getAddress())
                .info(foodBoardDto.getInfo())
                .rate(foodBoardDto.getRate()).build();

        FoodBoard saved = foodBoardRepository.save(saveFoodBoard);
        return saved;
    }
*/


    /**
     *     FoodBoard를 추가할때 첨부파일도 List에 저장함과 동시에 첨부파일 DB table에도 저장을 한다. (영속화되어 저장)
     *     web으로 넘어온 순수 form dto를
     *     user 정보가 담긴 BoardPostDto로 바꾸고
     *     해당 BoardPostDto를 최종적으로 DB에 저장하려고 한다.
     *
     *     BoardPostDto 에는 날것의 Mul Class Type의 2개의 List가 존재하고 있다.
     *     get을 통해서 가져오려고 한다. - 첫줄
     *
     *     날것의 2개 Mul Class Type을 1개의 Attachment Type List으로 바꿔준다. - 첫줄
     *
      */
    public FoodBoard post(FoodBoardPostDto boardPostDto) throws IOException {

        List<Attachment> attachments = attachmentServiceImpl.saveAttachments(boardPostDto.getAttachmentFiles());

        // 사용자가 웹에서 작성한 데이터 + 사용자 정보를 담은 dto에서 createBoard()를 하면
        // 해당 정보 모두들 담은 Entity 객체 FoodBoard를 반환 ( 해당 Entity 안에는 파일 리스트가 공백인 상태)
        FoodBoard board = boardPostDto.createBoard();
        board.addAttachedFiles(attachments);

        //수정사항 편의 메서드를 user에 넣는게 아니라 맛집 게시글이 등록될떄 user의 list에도 갖고자 한다.
        board.getUser().addFoodBoard(board);

        // Cascade.ALL안에 포함되어 있는 Cascade.Persist를 통해 같이 영속화되어 저장된다.
        return foodBoardRepository.save(board);
    }

    /**
     * 사용자가 보기 원하는 맛집 게시글의 상세 페이지를 반환
     * FoodBoardShowDto 반환
     * @param id 맛집 게시글의 고유 db_id
     */
    public FoodBoardShowDto showByFoodBoardId(Long id){

        FoodBoard entity = foodBoardRepository.findById(id).orElse(null);

        FoodBoardShowDto dto = FoodBoardShowDto.builder()
                .id(entity.getId())
                .locationType(entity.getLocationType())
                .foodType(entity.getFoodType())
                .address(entity.getAddress())
                .preview(entity.getPreview())
                .info(entity.getInfo())
                .rate(entity.getRate())
                .title(entity.getTitle())
                .attachedFiles(entity.getAttachedFiles()).build();

        return dto;

    }


    /**
     * 사용자가 수정하기 원하는 게시글의 수정 폼에 이미 등록된 데이터를 뿌려주기 위해서
     * FoodBoardPostFormDto 를 반환한다.
     * FoodBoardPostFormDto 를 반환할때는 새로운 사진을 입력 받기 위해서 imageFiles는 new ArrayList로 반환한다.
     * FoodBoardShowDto 를 사용할 수 없는 이유는 해당 Dto에서는 List imagies 데이터 형식이 MultipartFile가 아니기 때문이다.
     * @param id 수정하고자 하는 게시글의 id
     * @return 수정 폼에 뿌려질 데이터가 담긴 foodBoardPostFormDto
     */
    public FoodBoardPostFormDto showUpdateFormById(Long id){

        FoodBoard entity = foodBoardRepository.findById(id).get();

        FoodBoardPostFormDto foodBoardPostFormDto = FoodBoardPostFormDto.builder()
                .title(entity.getTitle())
                .preview(entity.getPreview())
                .address(entity.getAddress())
                .info(entity.getInfo())
                .rate(entity.getRate())
                .locationType(entity.getLocationType())
                .foodType(entity.getFoodType())
                .imageFiles(new ArrayList<>())
                .build();

        return foodBoardPostFormDto;
    }



    public void saveFoodCmt(Long user_db_id, Long id, FoodCmtDto foodCmtDto){

        // 로그인 사용자 중 누가 작성했는지 알기 위해서 해당 Entity 객체를 FoodCmt에 넣기 위해서 필요
        User user = userRepository.findById(user_db_id).orElse(null);

        // 어떤 맛집 게시글에 댓글을 작성했는지 확인하기 위해서
        FoodBoard foodBoard = foodBoardRepository.findById(id).orElse(null);

        String cmt = foodCmtDto.getCmt(); // 사용자가 작성한 String 댓글

        FoodCmt foodcmt = FoodCmt.builder()
                .user(user)
                .foodBoard(foodBoard)
                .foodCmt(cmt).build();

        // 영속성 컨텍스트에서 관리중인 Entity 객체에 Cmts 추가 , cascade=ALL에 의해서 연관된 entity 함께 persist 된다.
        foodBoard.addFoodCmts(foodcmt);

        // 영속성 컨텍스트에서 관리되고 있는 객체를 Dirty check를 함으로써 Cmt를 추가하고자 한다.
        // 현재 FoodBoard Entity는 이미 한번 DB_ID 값을 가지고 있기 때문에 Dirty Check 가능하다.
        foodBoardRepository.save(foodBoard);
    }




    public FoodBoard update(Long id, User user, FoodBoardPostFormDto foodBoardPostFormDto) throws IOException {

        // JPA의 더티 채킹을 활용하기 위해 entity를 영속성 컨텍스트에 관리 되도록 한다.
        FoodBoard foodBoard = foodBoardRepository.findById(id).orElse(null);

        // 수정하고자 하는 파일이 있으면 , 게시글이 원래 가지고 있던 사진들 삭제 처리
        // 수정하고자 하는 파일이 없으면 , 기존의 사진들을 삭제하지 않는다.
        if (foodBoardPostFormDto.getImageFiles().get(0).getBytes().length != 0) {
            attachmentRepository.deleteByFoodId(id);
        }

        // foodBoardPostFormDto 으로 부터 받아온 multi type의 file list를 Attachment List으로 바꿔준다.
        // fileStore 를 통해서 dto로 넘어온 날것의 file에서 이름을 추출하고 각종 작업을 거쳐 new file(저장경로+파일이름)을 생성해주면 된다.
        List<MultipartFile> imageFiles = foodBoardPostFormDto.getImageFiles();
        List<Attachment> attachments = fileStore.storeFiles(imageFiles, AttachmentType.IMAGE);

        // 각 사진들의 주인인 맛집 게시글을 setter를 통해 설정해준다. cascade=ALL 옵션에 의해서 저장될때 함꼐 Db에 persist된다.
        attachments.stream().forEach(e-> e.setBoard(foodBoard));

        FoodBoard changed = foodBoard.changeToEntity(id
                , user, foodBoardPostFormDto.getLocationType(), foodBoardPostFormDto.getFoodType(), foodBoardPostFormDto.getTitle()
                , foodBoardPostFormDto.getPreview(), foodBoardPostFormDto.getAddress(), foodBoardPostFormDto.getInfo()
                , foodBoardPostFormDto.getRate(), attachments);


        return changed;
    }





}
