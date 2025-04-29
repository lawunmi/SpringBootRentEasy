package com.renteasy.api.repository;

import com.renteasy.api.entity.Inquiry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InquiryRepository extends MongoRepository<Inquiry, String> {
    List<Inquiry> findByListingId(String listingId);
}