package com.example.house_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="houses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "house_id")
    private Long houseId;

    private String name;
    private Long price;
    private String images;
    private String detailURL;
    private String introduction;
    private BigDecimal square;
    private String face;
    private Long bedroom;
    private Long livingroom;
    private Long bathroom;
    private String floor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district")
    private District district;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "street")
    private Street street;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "neighbourhood")
    private Neighbourhood neighbourhood;

    @ManyToMany(mappedBy = "favors", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> collectors = new ArrayList<>();

    @Override
    public String toString() {
        return "House{" +
                "houseId=" + houseId +
                ", name='" + name + '\'' +
                '}';
    }
}

