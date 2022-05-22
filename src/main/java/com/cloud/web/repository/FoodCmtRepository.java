package com.cloud.web.repository;

import com.cloud.web.domain.FoodBoard;
import com.cloud.web.domain.FoodCmt;
import com.cloud.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodCmtRepository extends JpaRepository<FoodCmt,Long> {

    @Query("SELECT c FROM FoodCmt c WHERE c.foodBoard = :foodBoard")
    List<FoodCmt> findByFoodBoard(@Param("foodBoard") FoodBoard foodBoard);


}
