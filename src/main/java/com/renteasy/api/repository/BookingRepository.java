package com.renteasy.api.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.renteasy.api.entity.Booking;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String>{
	@Query("{ '_id': ?0 }")
	Optional<Booking> findByStringId(String id);
	List<Booking> findByUserId(String userId);
	List<Booking> findByListingId(String listingId);
}
