package com.cloud.web.repository;

import com.cloud.web.domain.*;
import com.cloud.web.dto.request.FoodBoardCondition;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FoodBoardRepositoryTest {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private FoodTypeRepository foodTypeRepository;
    @Autowired
    private FoodCmtRepository foodCmtRepository;


    /**
     * 쿼리 Dsl 를 위해서 필요한 부분
     */
    @Autowired
    private FoodBoardRepository foodBoardRepository;
    @Autowired
    private EntityManager em;


/*
    @Autowired private DataSource database;

    private static boolean dataLoaded = false;

    @Before // before test method
    public void setup() throws SQLException {

        if(!dataLoaded) {
            try (Connection con = database.getConnection()) {

                ScriptUtils.executeSqlScript(con, new ClassPathResource("/com/cloud/web/repository/cloud_init.sql"));
                dataLoaded = true;
            }
        }
    }
*/


    @Test
    public void check_게시글_생성시간_성공() throws Exception {
        //given
        User user = userRepository.findById(1L).get();
        LocationType locationType = locationTypeRepository.findById(1L).get();
        FoodType foodType = foodTypeRepository.findById(1L).get();


        //FoodBoard foodBoard = new FoodBoard(user, locationType, foodType, "맛집 게시글 test", "test", "사진경로", "주소", "맛집 정보", 4.3);

        //when
        //foodBoardRepository.save(foodBoard);

        //then
        //assertThat(foodBoard.getCreatedDate()).isNotNull();
     }

     @Test
     public void chech_게시글_수정시간_성공() throws Exception {
         User user = userRepository.findById(1L).get();
         LocationType locationType = locationTypeRepository.findById(1L).get();
         FoodType foodType = foodTypeRepository.findById(1L).get();


         //FoodBoard foodBoard = new FoodBoard(user, locationType, foodType, "맛집 게시글 test", "test", "사진경로", "주소", "맛집 정보", 4.3);

         //when
         //foodBoardRepository.save(foodBoard);

         //foodBoard.setAddress("주소 변경했음");

         //then
         //assertThat(foodBoard.getModifiedDate()).isNotNull();

      }


      @Test
      public void 하위테이블까지_delete되는지_황인() throws Exception {
          //given

          //when
          foodBoardRepository.deleteById(1L);

          List<FoodBoard> all = foodBoardRepository.findAll();

          assertThat(all.size()).isEqualTo(3);

          List<FoodCmt> all1 = foodCmtRepository.findAll();

          assertThat(all1.size()).isEqualTo(3);

      }


    @Test
    public void foodBoard_Condition_Check(){

        FoodBoardCondition condition = FoodBoardCondition.builder()
                .title("tt")
                .foodType_id(3L)
                .locationType_Id(2L).build();

        List<FoodBoard> searchedList = foodBoardRepository.search(condition);

        Assertions.assertThat(searchedList.size()).isEqualTo(1);


    }






}