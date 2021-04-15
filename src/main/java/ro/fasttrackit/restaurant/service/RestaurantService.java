package ro.fasttrackit.restaurant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.fasttrackit.restaurant.domain.Restaurant;
import ro.fasttrackit.restaurant.exception.ResourceNotFoundException;
import ro.fasttrackit.restaurant.model.RestaurantFilters;
import ro.fasttrackit.restaurant.repository.RestaurantRepository;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;
    private final RestaurantValidator validator;
    private final ObjectMapper mapper;

    public Page<Restaurant> getAll(RestaurantFilters filters, Pageable pageable) {
        if (!isEmpty(filters.getStars()) && filters.getCity() == null) {
            return repository.findByStarsIn(filters.getStars(), pageable);
        } else if (!isEmpty(filters.getStars()) && filters.getCity() != null) {
            return repository.findByStarsInAndCity(filters.getStars(), filters.getCity(), pageable);
        } else if (isEmpty(filters.getStars()) && filters.getCity() != null) {
            return repository.findByCity(filters.getCity(), pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    public Restaurant addRestaurant(Restaurant newRestaurant) {
        validator.validateNewThrow(newRestaurant);
        return repository.save(newRestaurant);
    }

    public Restaurant getRestaurant(Long restaurantId) {
        return repository.getById(restaurantId);
    }

    public void deleteRestaurant(long restaurantId) {
        repository.deleteById(restaurantId);
    }

    public Restaurant replaceRestaurant(long restaurantId, Restaurant newRestaurant) {
        newRestaurant.setId(restaurantId);
        validator.validateReplaceThrow(restaurantId, newRestaurant);

        Restaurant dbRestaurant = repository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find restaurant with id " + restaurantId));
        copyRestaurant(newRestaurant, dbRestaurant);
        return repository.save(dbRestaurant);
    }

    private void copyRestaurant(Restaurant newRestaurant, Restaurant dbRestaurant) {
        dbRestaurant.setCity(newRestaurant.getCity());
        dbRestaurant.setName(newRestaurant.getName());
        dbRestaurant.setStars(newRestaurant.getStars());
    }

    @SneakyThrows
    public Restaurant patchRestaurant(long restaurantId, JsonPatch patch) {
        validator.validateExistsOrThrow(restaurantId);
        Restaurant dbRestaurant = repository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find restaurant with id " + restaurantId));

        JsonNode patchedRestaurantJson = patch.apply(mapper.valueToTree(dbRestaurant));
        Restaurant patchedProduct = mapper.treeToValue(patchedRestaurantJson, Restaurant.class);
        return replaceRestaurant(restaurantId, patchedProduct);
    }
}
