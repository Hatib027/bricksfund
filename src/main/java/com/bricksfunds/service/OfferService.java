package com.bricksfunds.service;

import com.bricksfunds.entity.Offer;
import com.bricksfunds.repo.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {


    @Autowired
    private OfferRepository offerRepository;

    public Offer create(Offer offer){
        return offerRepository.save(offer);
    }

    public List<Offer> getAllOffers(){
        return offerRepository.findAll();
    }

    public void deleteById(int id) {
        offerRepository.deleteById(id);
    }
}
