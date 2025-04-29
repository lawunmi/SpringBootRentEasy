package com.renteasy.api.entity;

public class BookingMapper {
	public static BookingResponseDTO reponseDTO(Booking booking, User user, Listing listing) {
		BookingResponseDTO response = new BookingResponseDTO();
		 response.setBookingId(booking.getBookingId());
		 response.setUserName(user.getUsername()); 
		 response.setListingTitle(listing.getTitle()); 
		 response.setStatus(booking.getStatus());
		 response.setStartDate(booking.getStartDate());
		 response.setEndDate(booking.getEndDate());
		 response.setTotalPrice(booking.getTotalPrice());
		return response;
	}
}
