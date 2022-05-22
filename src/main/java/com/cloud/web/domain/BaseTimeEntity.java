package com.cloud.web.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * BaseTimeEntity class is automatically managing
 * createdDate and modifiedDate of all entities.
 * @author soochan lee
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(name="created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name="modified_date")
    private LocalDateTime modifiedDate;
}
