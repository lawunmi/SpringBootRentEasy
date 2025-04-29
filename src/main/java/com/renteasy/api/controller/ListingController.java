package com.renteasy.api.controller;

import com.renteasy.api.entity.Listing;
import com.renteasy.api.repository.ListingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
@Tag(name = "Listing Controller", description = "APIs for managing listings including creation, retrieval, and search.")
public class ListingController {

    @Autowired
    private ListingRepository listingRepository;

    @Operation(summary = "Create a new listing", description = "Creates a new listing with the owner set as the authenticated user.")
    @PostMapping
    public Listing createListing(@RequestBody Listing listing, Authentication authentication) {
        listing.setOwnerId(authentication.getName());
        return listingRepository.save(listing);
    }

    @Operation(summary = "Get all listings", description = "Fetches all the listings available in the system.")
    @GetMapping
    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    @Operation(summary = "Search listings by location and/or category", description = "Search listings based on location, category, or both.")
    @GetMapping("/search")
    public List<Listing> searchListings(
            @Parameter(description = "Location to filter the listings by")
            @RequestParam(required = false) String location,

            @Parameter(description = "Category to filter the listings by")
            @RequestParam(required = false) String category
    ) {
        if (location != null && category != null) {
            return listingRepository.findByLocationAndCategory(location, category);
        } else if (location != null) {
            return listingRepository.findByLocation(location);
        } else if (category != null) {
            return listingRepository.findByCategory(category);
        } else {
            return listingRepository.findAll();
        }
    }
}
