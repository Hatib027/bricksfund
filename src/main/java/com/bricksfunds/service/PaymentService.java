package com.bricksfunds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bricksfunds.entity.BankDetailsAdmin;
import com.bricksfunds.entity.Payment;
import com.bricksfunds.entity.UpiPayment;
import com.bricksfunds.repo.PaymentRepository;
import com.bricksfunds.repo.PaymentUPIRepository;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    
    @Autowired
    private PaymentUPIRepository paymentUPIRepository;

    public Payment create( Payment payment) {
       return paymentRepository.save(payment);
    }
    
    public List<?> getPaymentHistory(String username){
    	
    	return this.paymentRepository.findByUserName(username);
    }
    public UpiPayment createupi( UpiPayment upipayment) {
        return paymentUPIRepository.save(upipayment);
    }
    
 public List<?> getPaymentUpiHistory(String username){
    	
    	return this.paymentUPIRepository.findByUserName(username);
    }
 
 public List<?> fetchAllUserUpi() {
     return paymentUPIRepository.fetchAllUserUpi();
 }

 public List<?> fetchAllUserIfcc() {
     return paymentRepository.fetchAllUserIfcc();
 }

public Payment findByIdIb(Long id){
     return paymentRepository.findById(id).get();
 }

 public UpiPayment findByIdUpi(Long id){
     return paymentUPIRepository.findById(id).get();
 }
 
 public List<UpiPayment> reportUpi(){
     return paymentUPIRepository.findByStatusOrderByIdDesc(1);
 }
 public List<Payment> reportIb(){
     return  paymentRepository.findByStatusOrderByIdDesc(1);
 }

}
