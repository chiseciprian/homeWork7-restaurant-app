package ro.fasttrackit.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.fasttrackit.restaurant.domain.Restaurant;
import ro.fasttrackit.restaurant.exception.ValidationException;
import ro.fasttrackit.restaurant.repository.CitiesRepository;
import ro.fasttrackit.restaurant.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Optional.empty;


@Component
@RequiredArgsConstructor
public class RestaurantValidator {
    private final RestaurantRepository restaurantRepository;
    private final CitiesRepository citiesRepository;

    public void validateNewThrow(Restaurant newRestaurant) {
        validate(newRestaurant, true)
                .ifPresent(ex -> {
                    throw ex;
                });
    }

    public void validateReplaceThrow(long restaurantId, Restaurant newRestaurant) {
        exists(restaurantId)
                .or(() -> validate(newRestaurant, false))
                .ifPresent(ex -> {
                    throw ex;
                });
    }

    private Optional<ValidationException> exists(long restaurantId) {
        return restaurantRepository.existsById(restaurantId)
                ? empty()
                : Optional.of(new ValidationException("Restaurant with id " + restaurantId + " doesn't exist"));
    }

    private Optional<ValidationException> validate(Restaurant newRestaurant, boolean newEntity) {
        if (newRestaurant.getName() == null) {
            return Optional.of(new ValidationException("Name cannot be null"));
        } else if (newRestaurant.getCity() == null) {
            return Optional.of(new ValidationException("City cannot be null"));
        } else if (newRestaurant.getSince() == null) {
            return Optional.of(new ValidationException("Date since restaurant is open cannot be null"));
        } else if (citiesRepository.existsByCity(newRestaurant.getCity())) {
            return Optional.of(new ValidationException("No Restaurants in your location"));
        } else if (newEntity && restaurantRepository.existsByNameAndCity(newRestaurant.getName(), newRestaurant.getCity())) {
            return Optional.of(new ValidationException("Name cannot be duplicate"));
        } else if (!newEntity && restaurantRepository.existsByNameAndIdNot(newRestaurant.getName(), newRestaurant.getId())) {
            return Optional.of(new ValidationException("Name cannot be duplicate"));
        } else if (newEntity && newRestaurant.getSince() == null) {
            return Optional.of(new ValidationException("Date cannot be null"));
        } else if (newEntity && !validateDate(newRestaurant.getSince())) {
            return Optional.of(new ValidationException("Insert a valid date"));
        } else if (newRestaurant.getStars() <= 0) {
            return Optional.of(new ValidationException("Stars should not be lower than 1"));
        } else if (newRestaurant.getStars() >= 5) {
            return Optional.of(new ValidationException("Stars should not be higher than 5"));
        } else {
            return empty();
        }
    }

    private boolean validateDate(LocalDate since) {
        return !since.isAfter(LocalDate.now());
    }

    public void validateExistsOrThrow(long productId) {
        exists(productId).ifPresent(ex -> {
            throw ex;
        });
    }
}
