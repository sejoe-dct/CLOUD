package com.cloud.web.repository;

import com.cloud.web.domain.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodTypeRepository extends JpaRepository<FoodType,Long> {
}
