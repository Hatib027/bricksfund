package com.bricksfunds.controller;

import com.bricksfunds.entity.Event;
import com.bricksfunds.entity.Offer;
import com.bricksfunds.entity.Payment;
import com.bricksfunds.service.EventService;
import com.bricksfunds.service.OfferService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @PostMapping("/add")
    public Offer saveEvent(@RequestParam("file") MultipartFile file, @RequestParam("data")  String offer)throws ParseException, IOException{
        System.out.println(offer);
        Gson g = new Gson();
        Offer s = g.fromJson(offer,Offer.class);
        s.setImage(file.getBytes());
        s.setOriginalFileName(file.getOriginalFilename());
        return offerService.create(s);
    }


//    @PostMapping("/get-offers")
//    public List<Offer> getOffers(){
//        return offerService.getAllOffers();
//    }
    @GetMapping ("/get-offers")
    public List<Offer> getOffers(){

        List<Offer> x =  offerService.getAllOffers();
    
        return x;
    }



}
