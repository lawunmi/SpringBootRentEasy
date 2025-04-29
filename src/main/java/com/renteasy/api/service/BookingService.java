package com.renteasy.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renteasy.api.entity.Booking;
import com.renteasy.api.entity.BookingMapper;
import com.renteasy.api.entity.BookingResponseDTO;
import com.renteasy.api.entity.BookingStatus;
import com.renteasy.api.entity.BookingUpdateRequest;
import com.renteasy.api.entity.Listing;
import com.renteasy.api.entity.PaymentStatus;
import com.renteasy.api.entity.User;
import com.renteasy.api.repository.BookingRepository;
import com.renteasy.api.repository.ListingRepository;
import com.renteasy.api.repository.UserRepository;
import java.util.stream.Collectors;


@Service
public class BookingService {
	
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ListingRepository listingRepository;
	
	class ResourceNotFoundException extends RuntimeException {
	    public ResourceNotFoundException(String message) {
	        super(message);
	    }
	}
	
	public BookingResponseDTO singleBooking(String id) {	 
		 
	 Booking booking = bookingRepository.findById(id)
	         .orElseThrow(() -> {return new ResourceNotFoundException("Booking not found");});
	 
	 User user = userRepository.findById(booking.getUserId())
	         .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	 
	 Listing listing = listingRepository.findById(booking.getListingId())
	         .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
	 
	 return BookingMapper.reponseDTO(booking, user, listing);
	 
	}
	
	public List<BookingResponseDTO> allBookings() {
	    List<Booking> bookings = bookingRepository.findAll();
	    
	    return bookings.stream()
	        .map(booking -> {
	            User user = userRepository.findById(booking.getUserId())
	                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	                
	            Listing listing = listingRepository.findById(booking.getListingId())
	                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
	                
	            return BookingMapper.reponseDTO(booking, user, listing);
	        })
	        .collect(Collectors.toList());
	}
	
	public BookingResponseDTO createBooking(Booking booking) {
	    // Validate input booking details
	    validateBookingInput(booking);
	    
	    // Fetch the listing to get the price per night
	    Listing listing = listingRepository.findById(booking.getListingId())
	        .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
	    
	    // Calculate total price
	    BigDecimal totalPrice = calculateTotalPrice(listing, booking.getStartDate(), booking.getEndDate());
	    
	    // Set the calculated total price
	    booking.setTotalPrice(totalPrice);
	    
	    // Set the initial booking status
	    booking.setStatus(BookingStatus.PENDING);
	    
	    // Save the booking
	    Booking savedBooking = bookingRepository.save(booking);
	    
	    // Fetch user details
	    User user = userRepository.findById(savedBooking.getUserId())
	        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    
	    // Return response DTO with calculated price
	    return BookingMapper.reponseDTO(savedBooking, user, listing);
	}
	
	public List<BookingResponseDTO> getBookingsByUserId(String userId) {
	    List<Booking> bookings = bookingRepository.findByUserId(userId);
	        
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	        
	    return bookings.stream()
	        .map(booking -> {
	            Listing listing = listingRepository.findById(booking.getListingId())
	                .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
	                
	            return BookingMapper.reponseDTO(booking, user, listing);
	        })
	        .collect(Collectors.toList());
	}
	
	public List<BookingResponseDTO> getBookingsByListingId(String listingId) {
	    List<Booking> bookings = bookingRepository.findByListingId(listingId);
	        
	    Listing listing = listingRepository.findById(listingId)
	        .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
	        
	    return bookings.stream()
	        .map(booking -> {
	            User user = userRepository.findById(booking.getUserId())
	                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	                
	            return BookingMapper.reponseDTO(booking, user, listing);
	        })
	        .collect(Collectors.toList());
	}
	
	public BookingResponseDTO updateBookingStatus(String id, BookingStatus status) {
	    Booking existingBooking = bookingRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
	    
	    // Update only the status fields
	    existingBooking.setStatus(status);
	    
	    Booking updatedBooking = bookingRepository.save(existingBooking);
	    
	    User user = userRepository.findById(updatedBooking.getUserId())
	        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	        
	    Listing listing = listingRepository.findById(updatedBooking.getListingId())
	        .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
	        
	    return BookingMapper.reponseDTO(updatedBooking, user, listing);
	}
	
	public BookingResponseDTO updateBookingDetails(String id, BookingUpdateRequest updateRequest) {
	    Booking existingBooking = bookingRepository.findByStringId(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
	    
	    // Validate if booking is in a state that allows updates
	    if (existingBooking.getStatus() == BookingStatus.CANCELLED || 
	        existingBooking.getStatus() == BookingStatus.COMPLETED) {
	        throw new IllegalStateException("Cannot update a booking that is already cancelled or completed");
	    }
	    
	    // Update fields that users are allowed to modify
	    if (updateRequest.getStartDate() != null) {
	        existingBooking.setStartDate(updateRequest.getStartDate());
	    }
	    
	    if (updateRequest.getEndDate() != null) {
	        existingBooking.setEndDate(updateRequest.getEndDate());
	    }
	    
	    // Optional: Recalculate total price if dates changed
	    if (updateRequest.getStartDate() != null || updateRequest.getEndDate() != null) {
	        BigDecimal newPrice = calculateBookingPrice(
	            existingBooking.getListingId(), 
	            existingBooking.getStartDate(), 
	            existingBooking.getEndDate()
	        );
	        existingBooking.setTotalPrice(newPrice);
	    }
	    
	    // Save the updated booking
	    Booking updatedBooking = bookingRepository.save(existingBooking);
	    
	    // Get related entities
	    User user = userRepository.findById(updatedBooking.getUserId())
	        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	        
	    Listing listing = listingRepository.findById(updatedBooking.getListingId())
	        .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
	    
	    return BookingMapper.reponseDTO(updatedBooking, user, listing);
	}
	
	private BigDecimal calculateBookingPrice(String listingId, LocalDate startDate, LocalDate endDate) {
	    Listing listing = listingRepository.findById(listingId)
	        .orElseThrow(() -> new ResourceNotFoundException("Listing not found"));
	    
	    long days = ChronoUnit.DAYS.between(startDate, endDate);
	    
	    return listing.getPrice().multiply(BigDecimal.valueOf(days));
	}
	
	public void deleteBooking(String id) {
	    Booking booking = bookingRepository.findByStringId(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
	    
	    if (booking.getStatus() == BookingStatus.COMPLETED) {
	        throw new IllegalStateException("Cannot delete a completed booking");
	    }
	    
	    bookingRepository.delete(booking);
	}
	
	private void validateBookingInput(Booking booking) {
	    // Validate required fields
	    
	    if (booking.getStartDate() == null || booking.getEndDate() == null) {
	        throw new IllegalArgumentException("Start and end dates are required");
	    }
	    
	    // Validate date range
	    if (booking.getStartDate().isAfter(booking.getEndDate())) {
	        throw new IllegalArgumentException("Start date must be before end date");
	    }
	    
	}
	
	private BigDecimal calculateTotalPrice(Listing listing, LocalDate startDate, LocalDate endDate) {
	    // Calculate number of nights
	    long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
	    
	    if (numOfDays <= 0) {
	        throw new IllegalArgumentException("Booking must be for at least one night");
	    }
	    
	    // Calculate total price
	    BigDecimal pricePerNight = listing.getPrice();
	    BigDecimal totalPrice = pricePerNight.multiply(BigDecimal.valueOf(numOfDays));
	    
	    return totalPrice;
	}
}
