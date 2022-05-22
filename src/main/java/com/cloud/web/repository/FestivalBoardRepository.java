package com.cloud.web.repository;


import com.cloud.web.domain.FestivalBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalBoardRepository extends JpaRepository<FestivalBoard,Long> {
}
