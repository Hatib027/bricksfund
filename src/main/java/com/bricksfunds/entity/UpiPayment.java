package com.bricksfunds.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Data
@Entity
@Table(name="upi_payment")
public class UpiPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    private Double amount;
    private String upiId;
    private String dateofrequest;
    private int status;
    private byte[] file;
    private String OriginalFileName ;
    private String remark;

   public UpiPayment(){

    }
}





