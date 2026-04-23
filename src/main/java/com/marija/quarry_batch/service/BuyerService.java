package com.marija.quarry_batch.service;

import com.marija.quarry_batch.model.Buyer;
import com.marija.quarry_batch.model.CompanyBuyer;
import com.marija.quarry_batch.model.IndividualBuyer;
import com.marija.quarry_batch.repository.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyerService {

    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public void createBuyer(Buyer buyer) {

        if (buyer instanceof IndividualBuyer) {
            buyerRepository.saveIndividual((IndividualBuyer) buyer);
        } else if (buyer instanceof CompanyBuyer) {
            buyerRepository.saveCompany((CompanyBuyer) buyer);
        }
    }

    public List<Buyer> search(String name, String email, String type) {
        return buyerRepository.search(name, email, type);
    }

    public List<Buyer> findAll() {
        return buyerRepository.findAll();
    }
}
