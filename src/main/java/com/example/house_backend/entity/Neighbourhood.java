package com.example.house_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "neighbourhood")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Neighbourhood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "neighbourhood_id")
    private Long neighbourhoodId;

    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Float score;

    @OneToMany(mappedBy = "neighbourhood", fetch = FetchType.LAZY)
    private List<House> houses;
}
