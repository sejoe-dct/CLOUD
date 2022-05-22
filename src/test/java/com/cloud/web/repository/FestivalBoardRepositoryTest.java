package com.cloud.web.repository;

import com.cloud.web.domain.FestivalBoard;
import com.cloud.web.domain.FestivalCmt;
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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FestivalBoardRepositoryTest {

    @Autowired
    private FestivalBoardRepository festivalBoardRepository;

    @Autowired
    private FestivalCmtRepository festivalCmtRepository;

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
    public void 행사_게시글_삭제시_하위_데이터도_삭제_확인() throws Exception {
        //given
        festivalBoardRepository.deleteById(1L);

        //when
        List<FestivalBoard> all = festivalBoardRepository.findAll();

        List<FestivalCmt> all1 = festivalCmtRepository.findAll();
        //then
        assertThat(all.size()).isEqualTo(3);
        assertThat(all1.size()).isEqualTo(3);

     }



}