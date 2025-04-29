package com.renteasy.api.controller;

import com.renteasy.api.entity.Inquiry;
import com.renteasy.api.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
public class InquiryController {

    @Autowired
    private InquiryRepository inquiryRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public Inquiry sendInquiry(@RequestBody Inquiry inquiry) {
        inquiry.setTimestamp(LocalDateTime.now());
        Inquiry saved = inquiryRepo.save(inquiry);
        messagingTemplate.convertAndSend("/topic/notifications", "New inquiry for listing: " + inquiry.getListingId());
        return saved;
    }

    @GetMapping("/listing/{listingId}")
    public List<Inquiry> getInquiries(@PathVariable String listingId) {
        return inquiryRepo.findByListingId(listingId);
    }
}