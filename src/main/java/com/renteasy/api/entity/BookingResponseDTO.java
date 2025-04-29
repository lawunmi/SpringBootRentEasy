package com.renteasy.api.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.renteasy.api.repository.BookingRepository;
import com.renteasy.api.repository.ListingRepository;
import com.renteasy.api.repository.UserRepository;

//Create a BookingResponseDTO
public class BookingResponseDTO {
 private String bookingId;
 private String userName; 
 private String listingTitle; 
 private BookingStatus status;
 private LocalDate startDate;
 private LocalDate endDate;
 private BigDecimal totalPrice;
 
 // Constructors, getters, setters
 public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getListingTitle() {
		return listingTitle;
	}

	public void setListingTitle(String listingTitle) {
		this.listingTitle = listingTitle;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
