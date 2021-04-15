package ro.fasttrackit.restaurant.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.restaurant.domain.Restaurant;
import ro.fasttrackit.restaurant.model.RestaurantFilters;
import ro.fasttrackit.restaurant.service.CollectionResponse;
import ro.fasttrackit.restaurant.service.PageInfo;
import ro.fasttrackit.restaurant.service.RestaurantService;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantsController {
    private final RestaurantService service;

    @GetMapping
    CollectionResponse<Restaurant> getRestaurants(
            RestaurantFilters filters,
            Pageable pageable) {
        Page<Restaurant> restaurantsPage = service.getAll(filters, pageable);
        return CollectionResponse.<Restaurant>builder()
                .content(restaurantsPage.getContent())
                .pageInfo(PageInfo.builder()
                        .totalPages(restaurantsPage.getTotalPages())
                        .totalElements(restaurantsPage.getNumberOfElements())
                        .crtPage(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .build())
                .build();
    }

    @GetMapping(path = "{restaurantId}")
    Restaurant getRestaurant(@PathVariable long restaurantId) {
        return service.getRestaurant(restaurantId);
    }

    @PostMapping
    Restaurant addProduct(@RequestBody Restaurant newRestaurant) {
        return service.addRestaurant(newRestaurant);
    }

    @PutMapping(path = "{restaurantId}")
    Restaurant replaceRestaurant(@PathVariable long restaurantId, @RequestBody Restaurant newRestaurant) {
        return service.replaceRestaurant(restaurantId, newRestaurant);
    }

    @PatchMapping("{restaurantId}")
    Restaurant patchRestaurant(@PathVariable long restaurantId, @RequestBody JsonPatch patch) {
        return service.patchRestaurant(restaurantId, patch);
    }

    @DeleteMapping(path = "{restaurantId}")
    void deleteRestaurant(@PathVariable long restaurantId) {
        service.deleteRestaurant(restaurantId);
    }
}
