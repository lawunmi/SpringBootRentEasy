package com.renteasy.api.repository;

import com.renteasy.api.entity.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ListingRepository extends MongoRepository<Listing, String> {
    List<Listing> findByLocation(String location);
    List<Listing> findByCategory(String category);
    List<Listing> findByLocationAndCategory(String location, String category);
    /*@Query(value = "{ '_id' : ?0 }", fields = "{ 'description' : 1, '_id': 0 }")
    String findDescriptionById(String id);
    @Query(value = "{ '_id' : ?0 }", fields = "{ 'title' : 1, '_id': 0 }")
    String findTitleById(String id);*/
}