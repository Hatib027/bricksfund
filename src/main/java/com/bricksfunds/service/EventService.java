package com.bricksfunds.service;

import com.bricksfunds.entity.Event;
import com.bricksfunds.repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService    {


    @Autowired
    private EventRepository eventRepository;

    public Event create(Event event){
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    public void deleteById(int id) {
        eventRepository.deleteById(id);
    }
}
