package com.example.house_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "street")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Street {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "street_id")
    private Long streetId;

    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @OneToMany(mappedBy = "street", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<House> houses;
}