package com.bricksfunds.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bricksfunds.entity.BankDetailsAdmin;
import com.bricksfunds.entity.Event;
import com.bricksfunds.entity.InternetBankingWithdrawl;
import com.bricksfunds.entity.InvestmentMaster;
import com.bricksfunds.entity.Offer;
import com.bricksfunds.entity.Payment;
import com.bricksfunds.entity.RefPercentage;
import com.bricksfunds.entity.ReferMaster;
import com.bricksfunds.entity.UPIWithdrawl;
import com.bricksfunds.entity.UpiActionAdmin;
import com.bricksfunds.entity.UpiPayment;
import com.bricksfunds.entity.User;
import com.bricksfunds.service.BankDetailsAdminService;
import com.bricksfunds.service.EventService;
import com.bricksfunds.service.InvestmentMasterService;
import com.bricksfunds.service.OfferService;
import com.bricksfunds.service.PaymentService;
import com.bricksfunds.service.ReferService;
import com.bricksfunds.service.UserService;
import com.bricksfunds.service.WithdrawlService;
import com.google.gson.Gson;
import com.bricksfunds.service.UserServiceImpl;
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	UserService userService;
	
	@Autowired
	BankDetailsAdminService bankDetailService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	InvestmentMasterService investmentMasterService;
	
	@Autowired
	WithdrawlService withDrawlService;
	
	@Autowired
	ReferService referService;
	
	@Autowired
	private EventService eventService;

	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	private OfferService offerService;
	
	private static final DecimalFormat dfs = new DecimalFormat("0.000");
	
	
	
	@GetMapping("/fetchAllUser")
	public List<User> getAllUser(){
		
		List<User> allUser = this.userServiceImpl.findByEnable();
		
		return allUser;
	}
	
	@PostMapping("/addBankDetails")
	public void addBankDetails(@RequestBody BankDetailsAdmin bankDetailsAdmin) {
		
		BankDetailsAdmin baFirst = this.bankDetailService.getBanksDetailById(1);
		
		if(baFirst == null) {
			
			BankDetailsAdmin ba= this.bankDetailService.addBankDetails(bankDetailsAdmin);
		}else {
			
			baFirst.setAccountNumber(bankDetailsAdmin.getAccountNumber());
			baFirst.setIfscCode(bankDetailsAdmin.getIfscCode());
			baFirst.setSecurityCode(bankDetailsAdmin.getSecurityCode());
			baFirst.setUpiId(bankDetailsAdmin.getUpiId());
			baFirst.setAccountHoldername(bankDetailsAdmin.getAccountHoldername());
			BankDetailsAdmin ba= this.bankDetailService.addBankDetails(baFirst);
		}
		
		
		
		
	}
	
	@GetMapping("/getBankDetailsAdmin/{code}")
	public BankDetailsAdmin getBankDetailsAdmin(@PathVariable("code") String securityCode) throws Exception {
		
		BankDetailsAdmin ba = this.bankDetailService.getBankDetailsAdmin(Integer.parseInt(securityCode));
		
		if(ba == null) {
			throw new Exception("Security Code Wrong");
		}
		
		return ba;
	}
	
	
	@GetMapping("/fetchAllUserUpi")
	public List<?> fetchAllUserUpi(){

		List<?> fetchAllUserUpi = this.paymentService.fetchAllUserUpi();

		return fetchAllUserUpi;
	}

	@GetMapping("/fetchAllUserIfcc")
	public  List<?>  fetchAllUserIfcc(){
		List<?> fetchAllUserIfcc = this.paymentService.fetchAllUserIfcc();
		return fetchAllUserIfcc;
	}
	
	@GetMapping("/findByUpiId/{id}")
	public  UpiPayment  findByUpiId(@PathVariable("id") Long id){
		UpiPayment py = paymentService.findByIdUpi(id);
		return py;

	}


	@GetMapping("/findByIbId/{id}")
	public  Payment findByIbId(@PathVariable("id") Long id){
		Payment py = paymentService.findByIdIb(id);
		return py;


	}
	
	@PostMapping("/acceptOrRejectFundReqIb")
	public void acceptOrRejectFundReqIb(@RequestBody Payment payment) {
		User user = userService.getUser(payment.getUserName());

		String friendRefer = user.getReferralcode();
		
		if(payment.getStatus() == 2) {
			this.paymentService.create(payment);
		}else {
			
			this.paymentService.create(payment);
			
			InvestmentMaster i = investmentMasterService.findByUserId(user);
			
			if(i == null) {
				InvestmentMaster mlm = new InvestmentMaster();
				try {
					mlm.setCurrentInvestment(payment.getAmount());
					mlm.setTotalInvestment(payment.getAmount());
					
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
				}
				mlm.setUser(user);
				
				int level=0;
				if(payment.getAmount()>24 && payment.getAmount()<501) {
					level =1;
				}else if(payment.getAmount()>500 && payment.getAmount()<5000) {
					level=2;
				}else if(payment.getAmount()>4999){
					level = 3;
				}else {
					level = 0;
				}
				mlm.setLevel(level);
				SimpleDateFormat ds =new SimpleDateFormat("dd-MM-yyyy");
				if(level == 1) {
					Calendar c= Calendar.getInstance();
			        c.add(Calendar.DATE, 150);
			        Date d=c.getTime();
			        mlm.setDateOfExpireInvestment(ds.format(d));
			        
				}else if(level ==2) {
					Calendar c= Calendar.getInstance();
			        c.add(Calendar.DATE, 90);
			        Date d=c.getTime();
			        mlm.setDateOfExpireInvestment(ds.format(d));
				}else if(level == 3){
					Calendar c= Calendar.getInstance();
			        c.add(Calendar.DATE, 30);
			        Date d=c.getTime();
			        
			        mlm.setDateOfExpireInvestment(ds.format(d));
				}
				
				Date d = new Date();
				
				String dateOfToday = ds.format(d);
				
				mlm.setDateOfInvestment(dateOfToday);
				
				InvestmentMaster ij = investmentMasterService.addInvestment(mlm);
			}
			else {
				InvestmentMaster im = new InvestmentMaster();
				im.setId(i.getId());
				Double currentInvestMent;
				if(i.getCurrentInvestment() != null) {
					
					currentInvestMent = i.getCurrentInvestment() + payment.getAmount();
				}else {
					currentInvestMent = payment.getAmount();
				}
			
				
				Double totalInvestment;
				if(i.getTotalInvestment() != null) {
					totalInvestment = i.getTotalInvestment() +payment.getAmount();
				}else {
					 totalInvestment = payment.getAmount();
				}
				
				Double intrest = i.getTotalIntrest();
				if(i.getTotalIntrest() == null) {
					intrest = 0.0;
				}
				im.setTotalIntrest(intrest);
				
				Double totalProfit = i.getTotalProfit();
				if(i.getTotalProfit() == null) {
					totalProfit =0.0;
				}
				im.setTotalProfit(totalProfit);
				
				im.setUser(user);
				im.setCurrentInvestment(currentInvestMent);
				im.setTotalInvestment(totalInvestment);
				int level = 0;
				
				if(totalInvestment>24 && totalInvestment<1001) {
					level =1;
				}else if(totalInvestment>1000 && totalInvestment<5001) {
					level=2;
				}else if(totalInvestment>5000){
					level = 3;
				}else {
					level = 0;
				}
				im.setLevel(level);
				im.setReferralAmount(i.getReferralAmount());
				SimpleDateFormat ds =new SimpleDateFormat("dd-MM-yyyy");
				if(level == 1) {
					Calendar c= Calendar.getInstance();
			        c.add(Calendar.DATE, 150);
			        Date d=c.getTime();
			        im.setDateOfExpireInvestment(ds.format(d));
			        
				}else if(level ==2) {
					Calendar c= Calendar.getInstance();
			        c.add(Calendar.DATE, 90);
			        Date d=c.getTime();
			        im.setDateOfExpireInvestment(ds.format(d));
				}else if(level == 3){
					Calendar c= Calendar.getInstance();
			        c.add(Calendar.DATE, 30);
			        Date d=c.getTime();
			        im.setDateOfExpireInvestment(ds.format(d));
				}
                Date d = new Date();
				
				String dateOfToday = ds.format(d);
				
				im.setDateOfInvestment(dateOfToday);
				
				InvestmentMaster ij = investmentMasterService.addInvestment(im);
				
			}
			
			if(!friendRefer.equals(null)) {
				String[] firendReferMain = friendRefer.split("/");
				String s =firendReferMain[firendReferMain.length-1];
				int friendId= (Integer.parseInt(s));
				long l = friendId;
				User friendReferU = this.userService.findById(l);
				
				RefPercentage percentage = referService.getReferPercentageById(1);
//				done
				Double userAmount = payment.getAmount();
				int percentageByAdmin = percentage.getPercentage();
				
				Double mainAmount = (userAmount * percentageByAdmin)/100;
				
				InvestmentMaster ipk = investmentMasterService.findByUserId(friendReferU);
				
				if(ipk == null) {
					
					InvestmentMaster mklm = new InvestmentMaster();
					
					try {
						
						mklm.setReferralAmount(mainAmount);
					}catch(Exception e) {
						System.out.println(e.getMessage()+"jpd");
					}
					
					mklm.setUser(friendReferU);
					
					InvestmentMaster imk = investmentMasterService.addInvestment(mklm);
				}else {
					InvestmentMaster mk = new InvestmentMaster();
					mk.setId(ipk.getId());
					mk.setUser(friendReferU);
					mk.setDateOfInvestment(ipk.getDateOfInvestment());
					mk.setDateOfExpireInvestment(ipk.getDateOfExpireInvestment());
					
					// Double intrest = ipk.getTotalIntrest();
					// if(i.getTotalIntrest() == null) {
					// 	intrest = 0.0;
					// }
					// mk.setTotalIntrest(intrest);
					
					// Double totalProfit = ipk.getTotalProfit();
					// if(i.getTotalProfit() == null) {
					// 	totalProfit =0.0;
					// }
					// mk.setTotalProfit(totalProfit);


						Double intrest = ipk.getTotalIntrest();
						
						
						String checkDouble="";
						if(intrest != null) {
							checkDouble = intrest.toString();
						}
						
						if(checkDouble.equals("")) {
							intrest = 0.0;
						}
						mk.setTotalIntrest(intrest);
						
						Double totalProfit = ipk.getTotalProfit();
						String checkDouble1 ="";
						if(totalProfit != null) {
							checkDouble1= totalProfit.toString();
						}
						
						if(checkDouble1.equals("")) {
							totalProfit =0.0;
						}
						mk.setTotalProfit(totalProfit);
					
					if(ipk.getReferralAmount() == null) {
					
						Double refAmount = mainAmount;
						mk.setReferralAmount(refAmount);
					}else {
					
						
						Double refAmount = mainAmount + ipk.getReferralAmount();
						mk.setReferralAmount(refAmount);
					}
					
					mk.setTotalInvestment(ipk.getTotalInvestment());
					mk.setLevel(ipk.getLevel());
					mk.setCurrentInvestment(ipk.getCurrentInvestment());
					
					
					InvestmentMaster imj = investmentMasterService.addInvestment(mk);
				}
				
				ReferMaster ref = new ReferMaster();
				
				ref.setAmount(mainAmount);
               Date d = new Date();
				
                SimpleDateFormat dsf =new SimpleDateFormat("dd-MM-yyyy");
				String dateOfTodaymain = dsf.format(d);
				ref.setDate(dateOfTodaymain);
				
				ref.setFriendRefer(user);
				ref.setUserRefer(friendReferU);
			    ReferMaster rem = this.referService.addReferData(ref);
				
			}
			
			
		}
		}
		
		
		
		@PostMapping("/acceptOrRejectFundReqUpi")
		public void acceptOrRejectFundReqUpi(@RequestBody UpiPayment payment) {
			User user = userService.getUser(payment.getUserName());

			String friendRefer = user.getReferralcode();
			
			if(payment.getStatus() == 2) {
				this.paymentService.createupi(payment);
			}else {
				
				this.paymentService.createupi(payment);
				
				InvestmentMaster i = investmentMasterService.findByUserId(user);
				
				if(i == null) {
					InvestmentMaster mlm = new InvestmentMaster();
					try {
						mlm.setCurrentInvestment(payment.getAmount());
						mlm.setTotalInvestment(payment.getAmount());
						
					}
					catch(Exception e) {
						System.out.println(e.getMessage());
					}
					mlm.setUser(user);
					
					int level=0;
					if(payment.getAmount()>24 && payment.getAmount()<501) {
						level =1;
					}else if(payment.getAmount()>500 && payment.getAmount()<5000) {
						level=2;
					}else if(payment.getAmount()>4999){
						level = 3;
					}else {
						level = 0;
					}
					mlm.setLevel(level);
					SimpleDateFormat ds =new SimpleDateFormat("dd-MM-yyyy");
					if(level == 1) {
						Calendar c= Calendar.getInstance();
				        c.add(Calendar.DATE, 150);
				        Date d=c.getTime();
				        mlm.setDateOfExpireInvestment(ds.format(d));
				        
					}else if(level ==2) {
						Calendar c= Calendar.getInstance();
				        c.add(Calendar.DATE, 90);
				        Date d=c.getTime();
				        mlm.setDateOfExpireInvestment(ds.format(d));
					}else if(level == 3){
						Calendar c= Calendar.getInstance();
				        c.add(Calendar.DATE, 30);
				        Date d=c.getTime();
				        
				        mlm.setDateOfExpireInvestment(ds.format(d));
					}
					
					Date d = new Date();
					
					String dateOfToday = ds.format(d);
					
					mlm.setDateOfInvestment(dateOfToday);
					InvestmentMaster ij = investmentMasterService.addInvestment(mlm);
				}
				else {
					InvestmentMaster im = new InvestmentMaster();
					im.setId(i.getId());
					
					Double intrest = i.getTotalIntrest();
					if(i.getTotalIntrest() == null) {
						intrest = 0.0;
					}
					im.setTotalIntrest(intrest);
					
					Double totalProfit = i.getTotalProfit();
					if(i.getTotalProfit() == null) {
						totalProfit =0.0;
					}
					im.setTotalProfit(totalProfit);
					Double currentInvestMent;
					if(i.getCurrentInvestment() != null) {
						
						currentInvestMent = i.getCurrentInvestment() + payment.getAmount();
					}else {
						currentInvestMent = payment.getAmount();
					}
				
					im.setReferralAmount(i.getReferralAmount());
					
					Double totalInvestment;
					if(i.getTotalInvestment() != null) {
						totalInvestment = i.getTotalInvestment() +payment.getAmount();
					}else {
						 totalInvestment = payment.getAmount();
					}
					
					im.setUser(user);
					im.setCurrentInvestment(currentInvestMent);
					im.setTotalInvestment(totalInvestment);
					int level = 0;
					
					if(totalInvestment>24 && totalInvestment<1001) {
						level =1;
					}else if(totalInvestment>1000 && totalInvestment<5001) {
						level=2;
					}else if(totalInvestment>5000){
						level = 3;
					}else {
						level = 0;
					}
					im.setLevel(level);
					
					SimpleDateFormat ds =new SimpleDateFormat("dd-MM-yyyy");
					if(level == 1) {
						Calendar c= Calendar.getInstance();
				        c.add(Calendar.DATE, 150);
				        Date d=c.getTime();
				        im.setDateOfExpireInvestment(ds.format(d));
				        
					}else if(level ==2) {
						Calendar c= Calendar.getInstance();
				        c.add(Calendar.DATE, 90);
				        Date d=c.getTime();
				        im.setDateOfExpireInvestment(ds.format(d));
					}else if(level == 3){
						Calendar c= Calendar.getInstance();
				        c.add(Calendar.DATE, 30);
				        Date d=c.getTime();
				        im.setDateOfExpireInvestment(ds.format(d));
					}
	                Date d = new Date();
					
					String dateOfToday = ds.format(d);
					
					im.setDateOfInvestment(dateOfToday);
					
					InvestmentMaster ij = investmentMasterService.addInvestment(im);
					
				}
				
				if(!friendRefer.equals(null)) {
					String[] firendReferMain = friendRefer.split("/");
					String s =firendReferMain[firendReferMain.length-1];
					int friendId= (Integer.parseInt(s));
					long l = friendId;
					User friendReferU = this.userService.findById(l);
					
					RefPercentage percentage = referService.getReferPercentageById(1);
					
					Double userAmount = payment.getAmount();
					int percentageByAdmin = percentage.getPercentage();
					
					Double mainAmount = (userAmount * percentageByAdmin)/100;
					
					InvestmentMaster ipk = investmentMasterService.findByUserId(friendReferU);
					
					if(ipk == null) {
						
						InvestmentMaster mklm = new InvestmentMaster();
						
						try {
							
							mklm.setReferralAmount(mainAmount);
						}catch(Exception e) {
							System.out.println(e.getMessage()+"jpd");
						}
						
						mklm.setUser(friendReferU);
						
						InvestmentMaster imk = investmentMasterService.addInvestment(mklm);
					}else {
						InvestmentMaster mk = new InvestmentMaster();
						mk.setId(ipk.getId());
						mk.setUser(friendReferU);
						mk.setDateOfInvestment(ipk.getDateOfInvestment());
						mk.setDateOfExpireInvestment(ipk.getDateOfExpireInvestment());
						
						// Double intrest = ipk.getTotalIntrest();
						// if(i.getTotalIntrest() == null) {
						// 	intrest = 0.0;
						// }
						// mk.setTotalIntrest(intrest);
						
						// Double totalProfit = ipk.getTotalProfit();
						// if(i.getTotalProfit() == null) {
						// 	totalProfit =0.0;
						// }
						// mk.setTotalProfit(totalProfit);

						Double intrest = ipk.getTotalIntrest();
						
						
						String checkDouble="";
						if(intrest != null) {
							checkDouble = intrest.toString();
						}
						
						if(checkDouble.equals("")) {
							intrest = 0.0;
						}
						mk.setTotalIntrest(intrest);
						
						Double totalProfit = ipk.getTotalProfit();
						String checkDouble1 ="";
						if(totalProfit != null) {
							checkDouble1= totalProfit.toString();
						}
						
						if(checkDouble1.equals("")) {
							totalProfit =0.0;
						}
						mk.setTotalProfit(totalProfit);
						if(ipk.getReferralAmount() == null) {
							
							Double refAmount = mainAmount;
							mk.setReferralAmount(refAmount);
						}else {
						
							
							Double refAmount = mainAmount + ipk.getReferralAmount();
							mk.setReferralAmount(refAmount);
						}
						
						mk.setTotalInvestment(ipk.getTotalInvestment());
						mk.setLevel(ipk.getLevel());
						mk.setCurrentInvestment(ipk.getCurrentInvestment());
						
						
						InvestmentMaster imj = investmentMasterService.addInvestment(mk);
					}
					
					ReferMaster ref = new ReferMaster();
					
					ref.setAmount(mainAmount);
	               Date d = new Date();
					
	               SimpleDateFormat dsf =new SimpleDateFormat("dd-MM-yyyy");
					String dateOfTodaymain = dsf.format(d);
					ref.setDate(dateOfTodaymain);
					
					ref.setFriendRefer(user);
					ref.setUserRefer(friendReferU);
				    ReferMaster rem = this.referService.addReferData(ref);
					
				}
				
				
				
			}
		
		
	}
		
		
		@GetMapping("/getReferPercentage")
		public RefPercentage getReferPercentage() {
			RefPercentage m  = new RefPercentage();
			try {
				m=this.referService.getReferPercentageById(1);
			}catch(Exception e) {
				
			}
			
			return m ;
		}
		
		@PostMapping("/addReferPercentage")
		public RefPercentage addReferPercentage(@RequestBody RefPercentage referPercentage) {
			return this.referService.addReferPercentage(referPercentage);
		}
		
		@GetMapping("/getUpiAction")
		public UpiActionAdmin getUpiAction() {
			UpiActionAdmin a = new UpiActionAdmin();
			
			try {
				a=this.withDrawlService.getUpiActionById(1);
			}catch(Exception e) {
				
			}
			return a;
		}
		
		@PostMapping("/addUpiAction")
		public UpiActionAdmin addUpiAction(@RequestBody UpiActionAdmin upiAction) {
			return this.withDrawlService.addUpiAction(upiAction);
		}
		
	    @GetMapping("/upi-widthrawl-all")
	    public List<?> getAllUpiWidthrawlRequestUpi(){
	      return  withDrawlService.getAllStatusZeroUpi();
	    }

	    @GetMapping("/ib-widthrawl-all")
	    public List<?> getAllUpiWidthrawlRequestIb(){
	        return  withDrawlService.getAllStatusZeroIb();
	    }
	    
	    @PostMapping("/acceptWithdrawlRequestIb")
	    public void acceptWithdrawlIb(@RequestBody InternetBankingWithdrawl ib) {
	    	
	    	User user =this.userService.getUser(ib.getUserName());
	    	InvestmentMaster i = this.investmentMasterService.findByUserId(user);
	    	
	    	Double referralAmount = i.getReferralAmount();
	    	Double currentInvestment = i.getCurrentInvestment();
	    	Double totalIntrest = i.getTotalIntrest();
	    	InvestmentMaster imk = new InvestmentMaster();
	    	
	    	
	    	
	    	if(ib.getStatus() == 2) {
	    		withDrawlService.createIb(ib);
	    	}else {
	    		withDrawlService.createIb(ib);
	    	
	    		if(i.getReferralAmount() == null) {
	    			referralAmount =0.0;
	    		}
	    		if(i.getTotalIntrest() == null) {
	    			totalIntrest = 0.0;
	    		}
	    	if(ib.getAmount() == referralAmount || ib.getAmount() > referralAmount)
	    	{
	    		Double amt = referralAmount-ib.getAmount();
	    		imk.setReferralAmount(0.0);
	    		Double absAmt =  Math.abs(amt);
	    		if(absAmt < i.getTotalIntrest() || absAmt == i.getTotalIntrest()) {
	    			Double amts = totalIntrest - absAmt;
	    			imk.setTotalIntrest(amts);
	    			imk.setCurrentInvestment(i.getCurrentInvestment());
	    		}else {
	    			Double amtss = totalIntrest - absAmt;
	    			imk.setTotalIntrest(0.0);
	    			Double bjs = Math.abs(amtss);
	    			Double newAmount  =  currentInvestment - bjs;
		    		imk.setCurrentInvestment(Double.parseDouble(dfs.format(newAmount)));
	    		}
	    		
	    		
	    		
	    	}else {
	    		Double amt = referralAmount-ib.getAmount();
	    		
	    		imk.setReferralAmount(Double.parseDouble(dfs.format(amt)));
	    		imk.setCurrentInvestment(i.getCurrentInvestment());
	    		
	    	}	
	    	
	    	if(imk.getCurrentInvestment()==null || imk.getCurrentInvestment() == 0.0) {
	    		SimpleDateFormat d =new SimpleDateFormat("dd-MM-yyyy");
   			    String date = d.format(new Date());
    			imk.setDateOfExpireInvestment(date);
	    		imk.setDateOfInvestment(i.getDateOfInvestment());
	    	}else {
	    		imk.setDateOfExpireInvestment(i.getDateOfExpireInvestment());
	    		imk.setDateOfInvestment(i.getDateOfInvestment());
	    	}
	    	imk.setId(i.getId());
	    	imk.setLevel(i.getLevel());
	    	imk.setUser(user);
	    	imk.setTotalInvestment(i.getTotalInvestment());
	    	imk.setTotalProfit(i.getTotalProfit());
	    	
	    	investmentMasterService.addInvestment(imk);
	    	System.out.println(imk);

	    	
	    }
	    }
	    
	    @PostMapping("/acceptWithdrawlRequestUpi")
	    public void acceptWithdrawlUpi(@RequestBody UPIWithdrawl ib) {
	    	
	    	User user =this.userService.getUser(ib.getUserName());
	    	InvestmentMaster i = this.investmentMasterService.findByUserId(user);
	    	
	    	Double referralAmount = i.getReferralAmount();
	    	Double currentInvestment = i.getCurrentInvestment();
	    	Double totalIntrest = i.getTotalIntrest();
	    	InvestmentMaster imk = new InvestmentMaster();
	    	
	    	
	    	
	    	if(ib.getStatus() == 2) {
	    		withDrawlService.createUpi(ib);
	    	}else {
	    		withDrawlService.createUpi(ib);
	    		if(i.getReferralAmount() == null) {
	    			referralAmount =0.0;
	    		}if(i.getTotalIntrest() == null) {
	    			totalIntrest = 0.0;
	    		}
	    	if(ib.getAmount() == referralAmount || ib.getAmount() > referralAmount)
	    	{
	    		Double amt = referralAmount-ib.getAmount();
	    		imk.setReferralAmount(0.0);
	    		Double absAmt =  Math.abs(amt);
	    		if(absAmt < i.getTotalIntrest() || absAmt == i.getTotalIntrest()) {
	    			Double amts = totalIntrest - absAmt;
	    			imk.setTotalIntrest(amts);
	    			imk.setCurrentInvestment(i.getCurrentInvestment());
	    		}else {
	    			Double amtss = totalIntrest - absAmt;
	    			imk.setTotalIntrest(0.0);
	    			Double bjs = Math.abs(amtss);
	    			Double newAmount  =  currentInvestment - bjs;
		    		imk.setCurrentInvestment(Double.parseDouble(dfs.format(newAmount)));
	    		}
	    		
	    		
	    	}else {
	    		Double amt = referralAmount-ib.getAmount();
	    		
	    		imk.setReferralAmount(Double.parseDouble(dfs.format(amt)));
	    		imk.setCurrentInvestment(i.getCurrentInvestment());
	    		
	    	}	
	    	
	    	if(imk.getCurrentInvestment()==null || imk.getCurrentInvestment() == 0.0) {
	    		SimpleDateFormat d =new SimpleDateFormat("dd-MM-yyyy");
   			    String date = d.format(new Date());
    			imk.setDateOfExpireInvestment(date);
	    		imk.setDateOfInvestment(i.getDateOfInvestment());
	    	}else {
	    		imk.setDateOfExpireInvestment(i.getDateOfExpireInvestment());
	    		imk.setDateOfInvestment(i.getDateOfInvestment());
	    	}
	    	imk.setId(i.getId());
	    	imk.setLevel(i.getLevel());
	    	imk.setUser(user);
	    	imk.setTotalInvestment(i.getTotalInvestment());
	    	imk.setTotalProfit(i.getTotalProfit());
	    	
	    	investmentMasterService.addInvestment(imk);
	    

	    	
	    }
	    }
	    
	    @GetMapping("/getUpiDataById/{id}")
	    public UPIWithdrawl getUpiWithdrawlDataByUsername(@PathVariable("id") int id) {
	    	return this.withDrawlService.findByIdUpi(id);
	    }
	    
	    @GetMapping("/getIbDataById/{id}")
	    public InternetBankingWithdrawl getIbWithdrawlDataByUsername(@PathVariable("id") int id) {
	    	return this.withDrawlService.findByIdIb(id);
	    }
		
	    
	    @PostMapping("/getInvestmentData")
	    public InvestmentMaster getInvestmentData(@RequestBody Map<String,String> username) {
	    	User user = this.userService.getUser(username.get("username"));
	    	return this.investmentMasterService.findByUserId(user);
	    }
	    
	    @GetMapping("/getAdminHomePage")
	    public Map<String,String> getAdminHome(){
	    	
	    	List<String> list =  this.investmentMasterService.getTotal();
	    	
	    	String totalInvestment="";
	    	String totalCurrent ="";
	    	
	    	
			//String s =firendReferMain[firendReferMain.length-1];
	    	
	    	for(String s : list) {
	    		String[] firendReferMain = s.split(",");
	    		int i=0;
	    		for(String a : firendReferMain) {
	    			if(i==0) {
	    				totalCurrent = a;
	    			}else {
	    				totalInvestment=a;
	    			}
	    			i++;
	    		}
	    		break;
	    	}
	    	
	    	List<User> u =this.userService.findAllUser();
	    	
	    	Map<String,String> map = new HashMap<>();
	    	
	    	
	    	int s = u.size();
	    	
	    	String m = ""+s;
	    	
	    	
	    	map.put("totalCurrent",  totalCurrent);
	    	map.put("totalInvestment", totalInvestment);
	    	map.put("totalUsers",m);
	    	
	    	return map;
	    }
	    
	    
	    @PostMapping("event/add")
		public Event saveEvent(@RequestBody String event){
			System.out.println(event);
			Gson g = new Gson();
			Event s = g.fromJson(event, Event.class);
			System.out.println(s.toString());
			return eventService.create(s);
		}

		@GetMapping("event/get-events")
		public List<Event> getEvents(){

			return eventService.getAllEvents();
		}

		@GetMapping("event/delete/{id}")
		public void getEvents(@PathVariable("id") int id){

			 eventService.deleteById(id);
		}



		@PostMapping("offer/add")
		public Offer saveEvent(@RequestParam("file") MultipartFile file, @RequestParam("data")  String offer)throws Exception {
			System.out.println(offer);
			Gson g = new Gson();
			Offer s = g.fromJson(offer,Offer.class);
			s.setImage(file.getBytes());
			s.setOriginalFileName(file.getOriginalFilename());
			return offerService.create(s);
		}

		@GetMapping("offer/get-offer")
		public List<Offer> getOffers(){
			return offerService.getAllOffers();
		}

		@GetMapping  ("offer/delete/{id}")
		public void getOffers(@PathVariable("id") int id){
			 System.out.println(id);
			 offerService.deleteById(id);
		}
	    
		

		
		@GetMapping("/getExcelRefer/{id}")
		public ResponseEntity<byte[]>  referExcel(@PathVariable String id) throws IOException {
			
           XSSFWorkbook workbook = new XSSFWorkbook();
			
			XSSFSheet sheet = workbook.createSheet("Completed-Order");
			
			
		
			XSSFRow rowhead = sheet.createRow((short)0); 
			
			XSSFFont font = workbook.createFont();
			XSSFCellStyle style = workbook.createCellStyle();
			font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
			font.setFontHeightInPoints((short)10);
			font.setBold(true);
			style.setFont(font);
			
			
			
			if(id.equals("1"))
			{
	//Deposites
				
				rowhead.createCell(0).setCellValue("id");
				rowhead.createCell(1).setCellValue("Username");
				rowhead.createCell(2).setCellValue("Amount");
				rowhead.createCell(3).setCellValue("Upi id");
				rowhead.createCell(4).setCellValue("Date Of request");
				rowhead.createCell(5).setCellValue("Remark");
				rowhead.createCell(6).setCellValue("Email");
				rowhead.createCell(7).setCellValue("Phone");
				for(int j = 0; j<=7; j++) {
					rowhead.getCell(j).setCellStyle(style);
					sheet.autoSizeColumn(j);
				}

				List<UpiPayment> list =this.paymentService.reportUpi();
				int rownum =1;

				for(UpiPayment up:list) {
					XSSFRow row = sheet.createRow((short) rownum);
					if (up != null) {
						
						User user = this.userService.getUser(up.getUserName());
						row.createCell(0).setCellValue(rownum);
						if (up.getUserName() != null) {
							row.createCell(1).setCellValue(up.getUserName());
						} else {
							row.createCell(1).setCellValue("-");
						}
						if (up.getAmount() != null) {
							row.createCell(2).setCellValue(up.getAmount());
						} else {
							row.createCell(2).setCellValue("-");
						}
						if (up.getUpiId() != null) {
							row.createCell(3).setCellValue(up.getUpiId());
						} else {
							row.createCell(3).setCellValue("-");
						}
						if (up.getDateofrequest() != null) {
							row.createCell(4).setCellValue(up.getDateofrequest());
						} else {
							row.createCell(4).setCellValue("-");
						}
						if (up.getRemark() != null) {
							row.createCell(5).setCellValue(up.getRemark());
						} else {
							row.createCell(5).setCellValue("-");
						}

						row.createCell(6).setCellValue(user.getEmail());
						row.createCell(7).setCellValue(user.getPhone());
						
						for(int j=0;j<= 8;j++) {
							sheet.autoSizeColumn(j);
						}
						rownum++;
					}
				}
			

			}
			if(id.equals("2"))
			{
	//Deposites
			
				rowhead.createCell(0).setCellValue("id");
				rowhead.createCell(1).setCellValue("Username");
				rowhead.createCell(2).setCellValue("Amount");
				rowhead.createCell(3).setCellValue("Account-Number");
				rowhead.createCell(4).setCellValue("Ifsc");
				rowhead.createCell(5).setCellValue("Date Of request");
				rowhead.createCell(6).setCellValue("Remark");
				rowhead.createCell(7).setCellValue("Email");
				rowhead.createCell(8).setCellValue("Phone");
				for(int j = 0; j<=8; j++) {
					rowhead.getCell(j).setCellStyle(style);
					sheet.autoSizeColumn(j);
				}

				List<Payment> list =this.paymentService.reportIb();
				int rownum =1;

				for(Payment up:list) {
					XSSFRow row = sheet.createRow((short) rownum);
					if (up != null) {
						row.createCell(0).setCellValue(rownum);
						User user = this.userService.getUser(up.getUserName());
						if (up.getUserName() != null) {
							row.createCell(1).setCellValue(up.getUserName());
						} else {
							row.createCell(1).setCellValue("-");
						}
						if (up.getAmount() != null) {
							row.createCell(2).setCellValue(up.getAmount());
						} else {
							row.createCell(2).setCellValue("-");
						}
						
						if (up.getAccountNumber() != null) {
							row.createCell(3).setCellValue(up.getAccountNumber());
						} else {
							row.createCell(3).setCellValue("-");
						}
						if (up.getIffcCode() != null) {
							row.createCell(4).setCellValue(up.getIffcCode());
						} else {
							row.createCell(4).setCellValue("-");
						}
						if (up.getDateofrequest() != null) {
							row.createCell(5).setCellValue(up.getDateofrequest());
						} else {
							row.createCell(5).setCellValue("-");
						}
						if (up.getRemark() != null) {
							row.createCell(6).setCellValue(up.getRemark());
						} else {
							row.createCell(6).setCellValue("-");
						}
						row.createCell(7).setCellValue(user.getEmail());
						row.createCell(8).setCellValue(user.getPhone());

						for(int j=0;j<= 8;j++) {
							sheet.autoSizeColumn(j);
						}
						rownum++;
					}
				}
			}
			if(id.equals("3"))
			{
	//Deposites
				
				rowhead.createCell(0).setCellValue("id");
				rowhead.createCell(1).setCellValue("Username");
				rowhead.createCell(2).setCellValue("Amount");
				rowhead.createCell(3).setCellValue("Account-Number");
				rowhead.createCell(4).setCellValue("Ifsc");
				rowhead.createCell(5).setCellValue("Date ");
				rowhead.createCell(6).setCellValue("Holder name");
				rowhead.createCell(7).setCellValue("Email");
				rowhead.createCell(8).setCellValue("Phone");
				
				for(int j = 0; j<=8; j++) {
					rowhead.getCell(j).setCellStyle(style);
					sheet.autoSizeColumn(j);
				}

				List<InternetBankingWithdrawl> list = this.withDrawlService.reportIb();
				int rownum =1;

				for(InternetBankingWithdrawl up:list) {
					XSSFRow row = sheet.createRow((short) rownum);
					if (up != null) {
						row.createCell(0).setCellValue(rownum);
						User user = this.userService.getUser(up.getUserName());
						if (up.getUserName() != null) {
							row.createCell(1).setCellValue(up.getUserName());
						} else {
							row.createCell(1).setCellValue("-");
						}
						if (up.getAmount() != null) {
							row.createCell(2).setCellValue(up.getAmount());
						} else {
							row.createCell(2).setCellValue("-");
						}
						if (up.getAccountNumber() != null) {
							row.createCell(3).setCellValue(up.getAccountNumber());
						} else {
							row.createCell(3).setCellValue("-");
						}
						if (up.getIfscCode() != null) {
							row.createCell(4).setCellValue(up.getIfscCode());
						} else {
							row.createCell(4).setCellValue("-");
						}
						if (up.getDate() != null) {
							row.createCell(5).setCellValue(up.getDate());
						} else {
							row.createCell(5).setCellValue("-");
						}
						if (up.getHolderName() != null) {
							row.createCell(6).setCellValue(up.getHolderName());
						} else {
							row.createCell(6).setCellValue("-");
						}

						row.createCell(7).setCellValue(user.getEmail());
						row.createCell(8).setCellValue(user.getPhone());
						rownum++;
						
						for(int j=0;j<= 8;j++) {
							sheet.autoSizeColumn(j);
						}
					}
				}
			}
			if(id.equals("4"))
			{
	//Deposites
				
				rowhead.createCell(0).setCellValue("id");
				rowhead.createCell(1).setCellValue("Username");
				rowhead.createCell(2).setCellValue("Amount");
				rowhead.createCell(3).setCellValue("UpiId ");
				rowhead.createCell(4).setCellValue("Date ");
				rowhead.createCell(5).setCellValue("Holder name");
				rowhead.createCell(6).setCellValue("Email");
				rowhead.createCell(7).setCellValue("Phone");
				for(int j = 0; j<=7; j++) {
					rowhead.getCell(j).setCellStyle(style);
					sheet.autoSizeColumn(j);
				}

				List<UPIWithdrawl> list =this.withDrawlService.reportUpi();
				int rownum =1;

				for(UPIWithdrawl up:list) {
					XSSFRow row = sheet.createRow((short) rownum);
					User user = this.userService.getUser(up.getUserName());
					if (up != null) {
						row.createCell(0).setCellValue(rownum);
						if (up.getUserName() != null) {
							row.createCell(1).setCellValue(up.getUserName());
						} else {
							row.createCell(1).setCellValue("-");
						}
						if (up.getAmount() != null) {
							row.createCell(2).setCellValue(up.getAmount());
						} else {
							row.createCell(2).setCellValue("-");
						}
						if (up.getUpiId() != null) {
							row.createCell(3).setCellValue(up.getUpiId());
						} else {
							row.createCell(3).setCellValue("-");
						}
						if (up.getDate() != null) {
							row.createCell(4).setCellValue(up.getDate());
						} else {
							row.createCell(4).setCellValue("-");
						}
						if (up.getHolderName() != null) {
							row.createCell(5).setCellValue(up.getHolderName());
						} else {
							row.createCell(5).setCellValue("-");
						}
						row.createCell(6).setCellValue(user.getEmail());
						row.createCell(7).setCellValue(user.getPhone());
						for(int j=0;j<= 7;j++) {
							sheet.autoSizeColumn(j);
						}
						rownum++;
					}
				}
			}
			
			
			
			if(id.equals("6")) {
				rowhead.createCell(0).setCellValue("S.No.");  
				
				rowhead.createCell(1).setCellValue("Username");  
				rowhead.createCell(2).setCellValue("referral-Amount"); 
				rowhead.createCell(3).setCellValue("Investment-Raised");
				rowhead.createCell(4).setCellValue("Email");
				rowhead.createCell(5).setCellValue("Phone");
			
				List<User> users= this.userServiceImpl.findByEnable();
				
				for(int j = 0; j<=5; j++) {
					rowhead.getCell(j).setCellStyle(style);
					sheet.autoSizeColumn(j);
			}
				
	           int rownum =1;
				
				for(User im: users) {
					XSSFRow row = sheet.createRow((short)rownum); 
					row.createCell(0).setCellValue(rownum);
					row.createCell(1).setCellValue(im.getUsername());
					
					String referTotalAmount = this.referService.getReferDataExcel(im);
					
					if(referTotalAmount == null) {
						row.createCell(2).setCellValue("0.0");
					}else {
						row.createCell(2).setCellValue(referTotalAmount);
					}
					
					String raisedAmount =this.investmentMasterService.findTotalByReferExcel(im.getYourrefercode());
					
					if(raisedAmount == null) {
						row.createCell(3).setCellValue("0.0");
					}else {
						row.createCell(3).setCellValue(raisedAmount);
					}
					row.createCell(4).setCellValue(im.getEmail());
					row.createCell(5).setCellValue(im.getPhone());
					for(int j=0;j<= 5;j++) {
						sheet.autoSizeColumn(j);
					}
					rownum++;
				}
			}
			
			if(id.equals("5")) {
				rowhead.createCell(0).setCellValue("S.No.");  
				
				rowhead.createCell(1).setCellValue("Username");  
				rowhead.createCell(2).setCellValue("Date-Last-Investment"); 
				rowhead.createCell(3).setCellValue("Current-Investment");  
				rowhead.createCell(4).setCellValue("Total-Investment");  
				rowhead.createCell(5).setCellValue("Level");
				rowhead.createCell(6).setCellValue("DateOf-Expire-Investment");
				rowhead.createCell(7).setCellValue("Total-Profit");
				rowhead.createCell(8).setCellValue("Current-Profit");
				rowhead.createCell(9).setCellValue("Live-ReferralAmount");
				rowhead.createCell(10).setCellValue("Email");
				rowhead.createCell(11).setCellValue("Phone");
				
				for(int j = 0; j<=11; j++) {
					rowhead.getCell(j).setCellStyle(style);
					sheet.autoSizeColumn(j);
			}
				
				
				List<InvestmentMaster> i =this.investmentMasterService.findForReportLevel();
				int rownum =1;
				
				for(InvestmentMaster im: i) {
					XSSFRow row = sheet.createRow((short)rownum); 
					if(im !=null) {
						row.createCell(0).setCellValue(rownum);
						row.createCell(1).setCellValue(im.getUser().getUsername());
						if(im.getDateOfInvestment() != null) {
							row.createCell(2).setCellValue(im.getDateOfInvestment());
						}else {
							row.createCell(2).setCellValue("-");
						}
						
						if(im.getCurrentInvestment() != null) {
							row.createCell(3).setCellValue(im.getCurrentInvestment());
						}else {
							row.createCell(3).setCellValue(0.0);
						}
						
						
						if(im.getTotalInvestment() != null) {
							row.createCell(4).setCellValue(im.getTotalInvestment());
						}else {
							row.createCell(4).setCellValue(0.0);
						}
						
						
						if(im.getLevel() != 0) {
							row.createCell(5).setCellValue(im.getLevel());
						}else {
							row.createCell(5).setCellValue(0);
						}
						
						if(im.getDateOfExpireInvestment() != null) {
							row.createCell(6).setCellValue(im.getDateOfExpireInvestment());
						}else {
							row.createCell(6).setCellValue("-");
						}
						if(im.getTotalProfit() != null) {
							row.createCell(7).setCellValue(im.getTotalProfit());
						}else {
							row.createCell(7).setCellValue(0.0);
						}
						
						if(im.getTotalIntrest() != null) {
							row.createCell(8).setCellValue(im.getTotalIntrest());
						}else {
							row.createCell(8).setCellValue(0.0);
						}
						
						if(im.getReferralAmount() != null) {
							row.createCell(9).setCellValue(im.getReferralAmount());
						}else {
							row.createCell(9).setCellValue(0.0);
						}
						row.createCell(10).setCellValue(im.getUser().getEmail());
						row.createCell(11).setCellValue(im.getUser().getPhone());
						for(int j=0;j<= 11;j++) {
							sheet.autoSizeColumn(j);
						}
						rownum++;
						
					}
				}
			}
			
			
			
			 ByteArrayOutputStream stream = new ByteArrayOutputStream();
				workbook.write(stream);
				
				workbook.close();
				
				 HttpHeaders headers = new HttpHeaders();
			        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
			        headers.setContentDispositionFormData("attachment", "sample.xlsx");
			        
			        return ResponseEntity.ok()
			                .headers(headers)
			                .body(stream.toByteArray());
			}
		
		
}
