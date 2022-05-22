package com.cloud.web.repository;

import com.cloud.web.domain.FoodBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodBoardRepository extends JpaRepository<FoodBoard,Long> , FoodBoardRepositoryCustom {
}
