package ro.fasttrackit.restaurant.model;

import lombok.Value;

import java.util.List;

@Value
public class RestaurantFilters {
    List<Integer> stars;
    String city;
}
