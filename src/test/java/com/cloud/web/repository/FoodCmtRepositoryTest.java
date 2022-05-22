package com.cloud.web.repository;

import com.cloud.web.domain.FoodCmt;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FoodCmtRepositoryTest {

    @Autowired
    private FoodBoardRepository foodBoardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private FoodTypeRepository foodTypeRepository;
    @Autowired
    private FoodCmtRepository foodCmtRepository;
    @Autowired
    private EntityManager em;

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
    public void delete_cmt() throws Exception {
        //given

        //when
        foodCmtRepository.deleteById(1L);


        List<FoodCmt> all = foodCmtRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(3);


    }


}