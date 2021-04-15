package ro.fasttrackit.restaurant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.restaurant.domain.Restaurant;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant getById(Long id);

    void deleteById(Long id);

    Page<Restaurant> findByStarsIn(List<Integer> stars, Pageable pageable);

    Page<Restaurant> findByCity(String city, Pageable pageable);

    Page<Restaurant> findByStarsInAndCity(List<Integer> stars, String city, Pageable pageable);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByNameAndCity(String name, String city);
}
