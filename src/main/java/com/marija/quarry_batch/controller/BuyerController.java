package com.marija.quarry_batch.controller;

import com.marija.quarry_batch.model.Buyer;
import com.marija.quarry_batch.model.CompanyBuyer;
import com.marija.quarry_batch.model.IndividualBuyer;
import com.marija.quarry_batch.service.BuyerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buyers")
public class BuyerController {

    private final BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @PostMapping("/individual")
    public void createIndividual(@RequestBody IndividualBuyer buyer) {
        buyerService.createBuyer(buyer);
    }

    @PostMapping("/company")
    public void createCompany(@RequestBody CompanyBuyer buyer) {
        buyerService.createBuyer(buyer);
    }

    @GetMapping
    public List<Buyer> getAll() {
        return buyerService.findAll();
    }

    @GetMapping("/search")
    public List<Buyer> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String type
    ) {
        return buyerService.search(name, email, type);
    }

}
