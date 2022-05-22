package com.cloud.web.repository;

import com.cloud.web.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    // 맛집 게시글에서 이미 등록된 사진을 삭제한다. , Modifying을 붙여줘야 한다.
    @Modifying
    @Query("delete from Attachment a where a.board.id = :id")
    void deleteByFoodId(@Param("id") Long id );


}
