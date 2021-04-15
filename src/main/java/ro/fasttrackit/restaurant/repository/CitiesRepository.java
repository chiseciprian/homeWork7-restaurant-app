package ro.fasttrackit.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.restaurant.domain.City;

@Repository
public interface CitiesRepository extends JpaRepository<City, Long> {

    boolean existsByCity(String city);
}
