package com.bricksfunds.controller;

import com.bricksfunds.entity.Event;
import com.bricksfunds.service.EventService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/add")
    public Event saveEvent(@RequestBody String event){
        System.out.println(event);
        Gson g = new Gson();
        Event s = g.fromJson(event, Event.class);

        return eventService.create(s);
    }

    @GetMapping("/get-events")
    public List<Event> getEvents(){
        return eventService.getAllEvents();
    }

  

    @PostMapping("/get-events")
    public Event getEventbyId(){
//        return eventService.get();
        return new Event();
    }
}
