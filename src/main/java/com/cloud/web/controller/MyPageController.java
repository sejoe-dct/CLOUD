package com.cloud.web.controller;


import com.cloud.web.config.auth.PrincipalDetails;
import com.cloud.web.domain.*;
import com.cloud.web.domain.enums.AttachmentType;
import com.cloud.web.dto.request.FoodBoardPostDto;
import com.cloud.web.dto.request.FoodBoardPostFormDto;
import com.cloud.web.dto.response.FoodBoardShowDto;
import com.cloud.web.dto.response.UserResponse;
import com.cloud.web.repository.*;
import com.cloud.web.service.AttachmentServiceImpl;
import com.cloud.web.service.FoodBoardService;
import com.cloud.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class MyPageController {

    @Autowired
    private UserService userService;
    @Autowired
    private FoodBoardService foodBoardService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodBoardRepository foodBoardRepository;
    @Autowired
    private FoodTypeRepository foodTypeRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private AttachmentServiceImpl attachmentServiceImpl;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private FileStore fileStore;


    /**
     * 로그인 한 User의 마이 페이지 가기
     * @return 마이 페이지를 반환한다.
     */
    @GetMapping("/myPage")
    public String myPage(Model model , Authentication authentication){

        // 사용자의 정보를 출력한다.
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        UserResponse user = userService.findUserById(userDetails.getUser().getDb_id());

        // 1. 로그인한 사용자의 정보를 확인할 수 있다.
        model.addAttribute("user" , user);

        // 2. 자신이 작성한 게시글의 목록을 확인 할 수 있다.
        model.addAttribute("foodBoards", user.getFoodBoardList());

        return "myPage/info";
    }


    /**
     * 맛집 게시글 목록에서 특정 게시물을 수정할 수 있는 수정 폼으로 간다.
     * @param id 수정하고자 하는 맛집 게시물의 db_id
     * @return 맛집 게시글을 수정할 수 있는 폼 (해당 게시글의 데이터가 담긴다. + 사진이랑 )
     */
    @GetMapping("/myPage/foods/{id}")
    public String myFoodBoardPage(@PathVariable Long id, Model model){

        // 수정 폼에서 해당 게시글의 이미 등록된 사진을 보여준다.
        FoodBoardShowDto foodBoardShowDto = foodBoardService.showByFoodBoardId(id);
        List<Attachment> attachedFiles = foodBoardShowDto.getAttachedFiles();

        // 수정할 폼에 해당 게시글이 가지고 있던 원래 데이터를 담아서 넘겨준다.
        FoodBoardPostFormDto foodBoardPostFormDto = foodBoardService.showUpdateFormById(id);

        // 맛집 유형과 위치 정보를 가져온다.
        List<FoodType> foodTypeList = foodTypeRepository.findAll();
        List<LocationType> locationTypeList = locationTypeRepository.findAll();

        // 수정 하고자 하는 게시글에 데이터를 뿌려준다.
        model.addAttribute("foodBoardPostFormDto" , foodBoardPostFormDto);

        // 등록된 게시물이 가지는 사진을 볼 수 있다.
        model.addAttribute("attachedFiles", attachedFiles);
        model.addAttribute("foodTypeList", foodTypeList);
        model.addAttribute("locationTypeList", locationTypeList);

        // foodBoardPostFormDto 에는 id값을 가지고 있지 않음으로 따로 넙겨준다.
        model.addAttribute("id", id);

        return "myPage/updateFoodForm";
    }

    /**
     * 해당 게시글의 정보를 수정 요청을 할 수 있는 api
     * @param id 수정하고자 하는 게시물의 db_id
     * @param foodBoardPostFormDto 수정할 값들을 받는 dto
     * @return redirect: /myPage/foods/{id} 로 가서 수정한 내역을 확인할 수 있도록 넘어간다.
     */
    @PostMapping("/myPage/foods/{id}")
    @Transactional
    public String updateFoodBoard(@PathVariable Long id, @ModelAttribute FoodBoardPostFormDto  foodBoardPostFormDto ,
                                  Authentication authentication) throws IOException {

        // 사용자의 정보를 출력한다.
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getUser().getDb_id()).orElse(null);

        FoodBoard updated = foodBoardService.update(id, user, foodBoardPostFormDto);

        FoodBoard saved = foodBoardRepository.save(updated);

        String aa = String.valueOf(id);

        return "redirect:/myPage/foods/" + aa;
    }


}
