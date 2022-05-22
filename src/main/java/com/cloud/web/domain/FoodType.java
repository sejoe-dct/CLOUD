package com.cloud.web.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "food_type")
public class FoodType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_type_id")
    private Long id;

    @Column(name = "food_type" , columnDefinition = " varchar(255) NOT NULL UNIQUE ")
    private String type;

    public FoodType() {
    }

    @Builder
    public FoodType(Long id, String type) {
        this.id = id;
        this.type = type;
    }
}
