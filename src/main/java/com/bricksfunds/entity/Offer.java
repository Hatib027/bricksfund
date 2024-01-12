package com.bricksfunds.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="offermaster")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String offer;
    private String offerDate;
    private byte[] image;
    private String originalFileName ;
}





