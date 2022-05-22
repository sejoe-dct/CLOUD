package com.cloud.web.repository;

import com.cloud.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "select u from User u where u.loginId = :loginId")
    Optional<User> findByLoginId(@Param("loginId") String loginId);

    @Override
    Optional<User> findById(Long aLong);
}
