package com.cloud.web.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "location_type")
public class LocationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_type_id")
    private Long id;

    @Column(name = "location_type" , columnDefinition = " varchar(255) NOT NULL UNIQUE ")
    private String type;

    public LocationType() {
    }

    @Builder
    public LocationType(Long id, String type) {
        this.id = id;
        this.type = type;
    }
}
