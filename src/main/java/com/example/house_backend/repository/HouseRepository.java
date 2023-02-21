package com.example.house_backend.repository;

import com.example.house_backend.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface HouseRepository extends JpaRepository<House, Long> {
    @Query(value = "select district_id,a.amount,name,latitude,longitude\n" +
            "from (\n" +
            "      (select count(district) as amount,district\n" +
            "      from houses\n" +
            "      where (bedroom between ?1 and ?2 and price between ?3 and ?4)\n" +
            "     group by district)\n" +
            "      a left join district on district_id= district)",nativeQuery = true)
    List<Map<String,Object>> getFilteredAmountByDistrict(int min_room, int max_room, int min_price, int max_price);

    @Query(value = "select * from district",nativeQuery = true)
    List<Map<String,Object>> getAmountByDistrict();

    @Query(value = "select street_id ,count(houses.house_id)amount,s.name,latitude,longitude\n" +
            "from (\n" +
            "     houses join street s on houses.street = s.street_id\n" +
            "    )\n" +
            "where (longitude between ?1 and ?2 and latitude between ?3 and ?4\n" +
            "      and bedroom between ?5 and ?6 and price between ?7 and ?8)\n" +
            "group by street_id",nativeQuery = true)
    List<Map<String,Object>> getAmountByStreet(Double ln_low, Double ln_high, Double lat_low, Double lat_high, int min_room, int max_room, int min_price, int max_price);

    @Query(value="select neighbourhood_id,name,latitude,longitude,(MAX(house_grade)+0.2831*score) as MaxGrade,count(b.house_id)amount from\n" +
            "(select neighbourhood_id,name,latitude,longitude,a.house_id,score,(0.3294*count(favor_id)/10+0.3875*(6000/price)) as house_grade from\n" +
            "(select neighbourhood_id,house_id,neighbourhood.name,latitude,longitude,score,price from neighbourhood join houses on neighbourhood.neighbourhood_id = houses.neighbourhood\n" +
            "where longitude between ?1 and ?2 and latitude between ?3 and ?4 and bedroom between ?5 and ?6 and price between ?7 and ?8) a\n" +
            "left join favor on a.house_id=favor.house_id group by a.house_id)b\n" +
            "group by neighbourhood_id",nativeQuery = true)
    List<Map<String,Object>> getAmountByNeighbourhood(Double ln_low, Double ln_high, Double lat_low, Double lat_high, int min_room, int max_room, int min_price, int max_price);

    @Query(value="select * from (Select houses.house_id,houses.name,houses.price,houses.images,houses.square,houses.face,houses.bedroom,houses.livingroom,count(favor_id),(0.3294*count(favor_id)/10+0.3875*(6000/price)+0.2831*score) as grade from favor,houses,neighbourhood where houses.house_id=favor.house_id and houses.neighbourhood=neighbourhood.neighbourhood_id group by houses.house_id) as table1 order by grade DESC LIMIT 0, 20",nativeQuery = true)
    List<Map<String,Object>> getRecommendedHouses();

    @Query(value="Select * from (select users.user_id as uid,abs(avg(0.3294*num/10+0.3875*(6000/houses.price)+0.2831*score)-?)as dif from (select count(favor.favor_id)as num from favor,users where favor.favor_id=users.user_id)as table1,favor,users,houses,neighbourhood where favor.user_id=users.user_id and favor.house_id=houses.house_id and houses.neighbourhood=neighbourhood.neighbourhood_id group by users.user_id) as table2 order by dif LIMIT 0, 3",nativeQuery = true)
    List<Map<String,Object>> getUserRecommended(Double userAvg);

    @Query(value="Select abs((0.3294*num/10+0.3875*(6000/houses.price)+0.2831*score)-?) as dif, houses.house_id,houses.name,houses.price,houses.images,houses.square,houses.face,houses.bedroom,houses.livingroom from (select count(favor.favor_id) as num,house_id from favor group by house_id)  as table1 left join houses on table1.house_id=houses.house_id join neighbourhood on neighbourhood=neighbourhood.neighbourhood_id\n" +
            "order by dif limit 20;",nativeQuery = true)
    List<Map<String,Object>> getHouseRecommended(Double houseAvg);
}
