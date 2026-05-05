package com.marija.quarry_batch.controller;

import com.marija.quarry_batch.model.Buyer;
import com.marija.quarry_batch.model.CompanyBuyer;
import com.marija.quarry_batch.model.IndividualBuyer;
import com.marija.quarry_batch.service.BuyerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/update")
    public void updateBuyer(@RequestBody Map<String, Object> body) {

        String type = (String) body.get("type");

        if ("INDIVIDUAL".equalsIgnoreCase(type)) {
            IndividualBuyer b = new IndividualBuyer();
            b.setId(Long.valueOf(body.get("id").toString()));
            b.setName((String) body.get("name"));
            b.setPhoneNumber((String) body.get("phoneNumber"));
            b.setEmail((String) body.get("email"));
            b.setPersonalId((String) body.get("personalId"));

            buyerService.updateBuyer(b);

        } else if ("COMPANY".equalsIgnoreCase(type)) {
            CompanyBuyer b = new CompanyBuyer();
            b.setId(Long.valueOf(body.get("id").toString()));
            b.setName((String) body.get("name"));
            b.setPhoneNumber((String) body.get("phoneNumber"));
            b.setEmail((String) body.get("email"));
            b.setTaxId((String) body.get("taxId"));

            buyerService.updateBuyer(b);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBuyer(@PathVariable Long id) {
        buyerService.deleteBuyer(id);
    }

}
