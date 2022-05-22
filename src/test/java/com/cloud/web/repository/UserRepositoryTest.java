package com.cloud.web.repository;

import com.cloud.web.domain.*;
import com.cloud.web.domain.enums.Role;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;



//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // 해당 어노테이셔은 Test Method마다 Spring을 새로 띄운다.
//@Sql(value = "cloud_init.sql" )
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional //@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodBoardRepository foodBoardRepository;
    @Autowired
    private FoodCmtRepository foodCmtRepository;
    @Autowired
    private FestivalBoardRepository festivalBoardRepository;
    @Autowired
    private FestivalCmtRepository festivalCmtRepository;
    @Autowired
    private EntityManager em;



    /**
     * 전체 Test Method 실행시 , Spring이 한번만 띄워지고 한번만 SQL이 들어가도록 하고 싶었다.
     * 각 test method가 끝나고 난뒤 변경 사항에 대해서만 트랜잭션을 하고 싶었다.
     *
     * DirtiesContext 를 사용하면, test method 실행할때 마다 spring을 새로 띄우는 현상 발생 -> 적절하지 X
     *
     * 아쉽게도 내가 원한는 방향대로 하는 어노테이션은 찾지 못 했다. ( 클래스 단위 Test를 진행할때 SQL이 한번만 실행 )
     * 따라서 각 test Method 실행 전에 DB로 부터 한번이라도 Connectino을 받아오면 더 이상 SQL를 실행하지 않는 로직 구현
     *
     * dataLoaded는 하나의 flag 역할을 한다. ->> 이렇게 함으로써 Class Test Method 실행시 Spring이 한번만 띄워지고, SQL도 전체 한번만 실행된
     *
     * 추가적으로, test code에서는 어쩔 수 없이 ddl_auto를 create를 설정하고 실제 WebApplication은 ddl_auto를 none으로 따로 설정하기 위해서
     * Test 파일에 application.properties를 넣어준다.
     */
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


  @Test
  public void user저장_default_USER_check() throws Exception {
      //given
      User user = new User("a555", "a555", "이수찬", "이수찬닉","tncksdl05@gmail.com", Role.ROLE_USER);

      //when
      User saved = userRepository.save(user);

      //then
      assertThat(user).isEqualTo(saved);

   }

   @Test
   public void user_Update_success() throws Exception {
       //given
       Optional<User> searched = userRepository.findById(1L);

       //when
       searched.get().setName("이수찬2"); // dirty check (변경 감지)를 통해서 update

       //then
       Optional<User> searched2 = userRepository.findById(1L);
       assertThat(searched2.get().getName()).isEqualTo("이수찬2"); // 실제 DB에서도 이수찬2로 이름이 변경된것을 확인 가능

   }


    @Test
    public void loginId_unique_check_success() throws Exception {
        //given
        // 똑같은 a111 아이디로 회원가입 시도,
        User user = new User("a111", "a555", "이수찬","이수찬닉" ,"tncksdl05@gmail.com" , Role.ROLE_USER);

        //when , then -> loginId 중복 가입 했을시 unique 제약 조건에 걸린 Exception 발생 확인
        assertThatThrownBy(() -> userRepository.save(user) )
                .isInstanceOf(DataIntegrityViolationException.class);
     }


     @Test
     public void 회원을_삭제할때_회원이_작성한_모든하위데이터_삭제_확인() throws Exception {
         //given

         //when
         userRepository.deleteById(1L);

         em.flush(); // flush를 해주는 이유. Db에 바로 반영을 해주기 위해서

         List<FoodBoard> all = foodBoardRepository.findAll();
         List<FoodCmt> all1 = foodCmtRepository.findAll();
         List<FestivalBoard> all2 = festivalBoardRepository.findAll();
         List<FestivalCmt> all3 = festivalCmtRepository.findAll();

         //then
         assertThat(all.size()).isEqualTo(3);
         assertThat(all1.size()).isEqualTo(3);
         assertThat(all2.size()).isEqualTo(3);
         assertThat(all3.size()).isEqualTo(3);



      }






}