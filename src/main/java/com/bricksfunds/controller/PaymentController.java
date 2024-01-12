package com.bricksfunds.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bricksfunds.entity.BankDetailsAdmin;
import com.bricksfunds.entity.BankMaster;
import com.bricksfunds.entity.Payment;
import com.bricksfunds.entity.UpiPayment;
import com.bricksfunds.entity.User;
import com.bricksfunds.service.BankDetailsAdminService;
import com.bricksfunds.service.BankService;
import com.bricksfunds.service.PaymentService;
import com.bricksfunds.service.UserService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/payment")
public class PaymentController {
        @Autowired
        private PaymentService paymentService;
        
        @Autowired
        private BankService bankService;
        
        @Autowired
        private UserService userService;
        
        @Autowired
        private BankDetailsAdminService bankDetailsAdminService;

        @PostMapping("/addpayment")
        public  void  createUser(
                @RequestParam("file")  MultipartFile file, @RequestParam("data")  String payment
                ) throws ParseException, IOException {

            Gson g = new Gson();
            Payment s = g.fromJson(payment, Payment.class);
            System.out.println(s.getBankName());
                s.setFile(file.getBytes());
                s.setOriginalFileName(file.getOriginalFilename());
                paymentService.create(s);

            
        }
    
    @GetMapping("/getAllBank")
    public List<BankMaster> getAllBanks(){
    	return this.bankService.getAllBank();
    }
    
    @PostMapping("/storeFile")
    public void addFile(@RequestParam("file") MultipartFile file) {
    	System.out.println(file);
    }
    
    @GetMapping("/payment-history")
    public List<?> getPaymentHistory(HttpServletRequest request) {
    	Principal p = request.getUserPrincipal();
    	User user = userService.getUser(p.getName());
    	
    	List<?> payment = this.paymentService.getPaymentHistory(user.getUsername());
    	return payment;
    	
    }
    @PostMapping("/addmoneytoupi")
    public void createPaymentfromUpi (@RequestParam("file")  MultipartFile file, @RequestParam("data")  String upiPayment
            ) throws ParseException, IOException {

        Gson g = new Gson();
        UpiPayment upi = g.fromJson(upiPayment, UpiPayment.class);
        upi.setFile(file.getBytes());
        upi.setOriginalFileName(file.getOriginalFilename());
        paymentService.createupi(upi);

       
    }
    @GetMapping("/paymentUPI-history")
    public List<?> getPaymentUpiHistory(HttpServletRequest request) {
    	Principal p = request.getUserPrincipal();
    	User user = userService.getUser(p.getName());
    	
    	List<?> UpiPayment = this.paymentService.getPaymentUpiHistory(user.getUsername());
    	return UpiPayment;
    	
    }
    
    @GetMapping("/getBankDetails-Admin")
    public BankDetailsAdmin getBankDetailsAdmin() {
    	BankDetailsAdmin baFirst = this.bankDetailsAdminService.getBanksDetailById(1);
    	
    	return baFirst;
    }
    
}
