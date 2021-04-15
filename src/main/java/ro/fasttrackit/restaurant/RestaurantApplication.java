package ro.fasttrackit.restaurant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ro.fasttrackit.restaurant.domain.City;
import ro.fasttrackit.restaurant.domain.Restaurant;
import ro.fasttrackit.restaurant.repository.CitiesRepository;
import ro.fasttrackit.restaurant.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class RestaurantApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }

    @Profile("romania")
    @Bean
    CommandLineRunner atStartup(RestaurantRepository repository, CitiesRepository citiesRepository) {
        citiesRepository.saveAll(List.of(new City("Oradea"), new City("Bucharest")));
        return args -> {
            repository.saveAll(List.of(
                    new Restaurant("Via29", 5, "Oradea", LocalDate.now()),
                    new Restaurant("Burger Factory", 4, "Bucharest", LocalDate.now())
            ));
        };
    }

    @Profile("hungary")
    @Bean
    CommandLineRunner atStartup2(RestaurantRepository repository, CitiesRepository citiesRepository) {
        citiesRepository.saveAll(List.of(new City("Debrecen"), new City("Budapest")));
        return args -> {
            repository.saveAll(List.of(
                    new Restaurant("Rukola", 4, "Debrecen", LocalDate.now()),
                    new Restaurant("Caviar & Bull Budapest", 5, "Budapest", LocalDate.now())
            ));
        };
    }
}
