package com.renteasy.api.controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.renteasy.api.entity.Booking;
import com.renteasy.api.entity.BookingResponseDTO;
import com.renteasy.api.entity.BookingStatus;
import com.renteasy.api.entity.BookingUpdateRequest;
import com.renteasy.api.entity.PaymentStatus;
import com.renteasy.api.repository.ListingRepository;
import com.renteasy.api.repository.UserRepository;
import com.renteasy.api.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/bookings")
@Tag(name = "Booking Controller", description = "APIs for managing bookings including creation, retrieval, update, and delete.")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	@Operation(summary = "All bookings", description = "Get all bookings")
	@GetMapping
	private ResponseEntity<ApiResponse> getAllBookings() {
		try {
			return new ResponseEntity<>(
					ApiResponse.success("Booking detail", bookingService.allBookings()),
					HttpStatus.OK
				);
		}
		catch(Exception e) {
			return new ResponseEntity<>(
					ApiResponse.error(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR
			);	
		}
		//return new ResponseEntity<List<Booking>>(bookingService.allBookings(), HttpStatus.OK);
	} 
	
	@Operation(summary = "Get a particular booking ", description = "Get booking by id")
	@GetMapping("/{id}")
	private ResponseEntity<ApiResponse> getBookingById(@PathVariable String id){
		try {
			return new ResponseEntity<>(
					ApiResponse.success("Booking detail", bookingService.singleBooking(id)),
					HttpStatus.OK
				);
		}
		catch(Exception e) {
			return new ResponseEntity<>(
					ApiResponse.error(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR
			);	
		}
		//return new ResponseEntity<Optional<Booking>>(bookingService.singleBooking(id), HttpStatus.OK);
	}
	
	@Operation(summary = "New booking", description = "Create a new booking")
	@PostMapping
	private ResponseEntity<ApiResponse> createBooking(@RequestBody Booking booking){
		try {
			BookingResponseDTO createdBooking = bookingService.createBooking(booking);
			return new ResponseEntity<>(
				ApiResponse.success("New booking created", createdBooking),
				HttpStatus.CREATED
			);
	    } catch(IllegalArgumentException e) {
	        return new ResponseEntity<>(
	            ApiResponse.error(e.getMessage()),
	            HttpStatus.BAD_REQUEST
	        );
		}catch(Exception e) {
			return new ResponseEntity<>(
					ApiResponse.error(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
		//return new ResponseEntity<>(bookingService.createBooking(booking), HttpStatus.CREATED);
	}
	
	
	@Operation(summary = "Get bookings by user", description = "Get all bookings particular to a user")
	@GetMapping("users/{userId}")
	private ResponseEntity<ApiResponse> getBookingsByUserId(@PathVariable String userId){
		try {
			return new ResponseEntity<>(
					ApiResponse.success("Booking detail", bookingService.getBookingsByUserId(userId)),
					HttpStatus.OK
				);
		}
		catch(Exception e) {
			return new ResponseEntity<>(
					ApiResponse.error(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR
			);	
		}
		//return new ResponseEntity<Optional<Booking>>(bookingService.getBookingByUserId(userId), HttpStatus.OK);
	}
	
	@Operation(summary = "Get bookings based on listing", description = "Get bookings related to a particular listing")
	@GetMapping("listing/{listingId}")
	private ResponseEntity<ApiResponse> getBookingsByListingId(@PathVariable String listingId){
		try {
			return new ResponseEntity<>(
					ApiResponse.success("Booking detail", bookingService.getBookingsByListingId(listingId)),
					HttpStatus.OK
				);
		}
		catch(Exception e) {
			return new ResponseEntity<>(
					ApiResponse.error(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR
			);	
		}
		//return new ResponseEntity<Optional<Booking>>(bookingService.getBookingByListingId(listingId), HttpStatus.OK);
	}
	
	@Operation(summary = "Update booking status", description = "Update bookings based on the id")
	@PatchMapping("statusUpdate/{id}")
	private ResponseEntity<ApiResponse> updateBookingStatus(@PathVariable String id, @RequestParam BookingStatus status){
		try {
			BookingResponseDTO statusUpdate = bookingService.updateBookingStatus(id, status);
			return new ResponseEntity<>(
					ApiResponse.success("Booking detail", statusUpdate),
					HttpStatus.OK
				);
		}
		catch(Exception e) {
			return new ResponseEntity<>(
					ApiResponse.error(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR
			);	
		}	
		//return new ResponseEntity<Booking>(bookingService.updateBookingStatus(id, booking), HttpStatus.OK);
	}
	
	@Operation(summary = "Update booking details", description = "Update booking details based on the id")
	@PutMapping("update/{id}")
	private ResponseEntity<ApiResponse> updateBookingDetails(@PathVariable String id, @RequestBody BookingUpdateRequest booking){
		try {
			BookingResponseDTO updatedBooking = bookingService.updateBookingDetails(id, booking);
			return new ResponseEntity<>(
					ApiResponse.success("Booking detail", updatedBooking),
					HttpStatus.OK
				);
		}
		catch(Exception e) {
			return new ResponseEntity<>(
					ApiResponse.error(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR
			);	
		}	
		//return new ResponseEntity<Booking>(bookingService.updateBookingStatus(id, booking), HttpStatus.OK);
	}
	
	@Operation(summary = "Delete a booking", description = "Delete an existing booking")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteBooking(@PathVariable String id) {
	    try {
	        bookingService.deleteBooking(id);
	        return new ResponseEntity<>(
	            ApiResponse.success("Booking deleted successfully", null),
	            HttpStatus.OK
	        );
	    } catch (Exception e) {
	        return new ResponseEntity<>(
	            ApiResponse.error(e.getMessage()),
	            HttpStatus.INTERNAL_SERVER_ERROR
	        );
	    }
	}
	
}
