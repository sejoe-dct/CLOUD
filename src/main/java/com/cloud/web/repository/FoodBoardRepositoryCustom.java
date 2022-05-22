package com.cloud.web.repository;

import com.cloud.web.domain.FoodBoard;
import com.cloud.web.dto.request.FoodBoardCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodBoardRepositoryCustom {

    List<FoodBoard> search(FoodBoardCondition condition);

    Page<FoodBoard> searchPageSimple(FoodBoardCondition condition, Pageable pageable);

}
