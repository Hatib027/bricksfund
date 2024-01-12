package com.bricksfunds.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name="eventmaster")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String events;
    private String description;
    private String locations;
    private String eventDate;
}





