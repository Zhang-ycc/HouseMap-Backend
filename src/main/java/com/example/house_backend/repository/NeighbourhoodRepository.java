package com.example.house_backend.repository;

import com.example.house_backend.entity.District;
import com.example.house_backend.entity.Neighbourhood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NeighbourhoodRepository extends JpaRepository<Neighbourhood, Long> {
    Neighbourhood getNeighbourhoodByNeighbourhoodId(Long id);

    @Query(value = "select * from neighbourhood where (longitude between ?1 and ?2 and latitude between ?3 and ?4)",nativeQuery = true)
    List<Neighbourhood> getScreenRangeNeighbourhood(Double ln_low, Double ln_high, Double lat_low, Double lat_high);
}
